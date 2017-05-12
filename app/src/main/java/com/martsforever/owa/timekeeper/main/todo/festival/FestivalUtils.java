package com.martsforever.owa.timekeeper.main.todo.festival;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 万年历调用示例代码 － 聚合数据
 * 在线接口文档：http://www.juhe.cn/docs/177
 **/

public class FestivalUtils {
    public static final String URL_DAY = "http://v.juhe.cn/calendar/day";
    public static final String URL_MONTH = "http://v.juhe.cn/calendar/month";
    public static final String URL_YEAR = "http://v.juhe.cn/calendar/year";

    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    //配置您申请的KEY
    public static final String APPKEY = "ac5b0c2afb244baa5ef31ceb16bef730";

    //1.获取当天的详细信息
    public static FestivalOfDay getDayInfo(Object year, Object month, Object day) {
        FestivalOfDay festivalOfDay = null;
        String result = null;
        String url = URL_DAY;//请求接口地址
        Map params = new HashMap();//请求参数
        String date = year.toString() + "-" + month.toString() + "-" + day.toString();
        params.put("key", APPKEY);//您申请的appKey
        params.put("date", date);//指定日期,格式为YYYY-MM-DD,如月份和日期小于10,则取个位,如:2012-1-1
        try {
            result = net(url, params, "GET");
            JSONObject object = JSONObject.parseObject(result);
            if (object.getInteger("error_code") == 0) {
                JSONObject data = (JSONObject.parseObject(object.getString("result"))).getJSONObject("data");
                festivalOfDay = new FestivalOfDay(data.getString("holiday"), data.getString("avoid"), data.getString("animalsYear"), data.getString("weekday"), data.getString("suit"), data.getString("lunarYear"), data.getString("lunar"), data.getString("date"));
                return festivalOfDay;
            } else {
                System.out.println(object.get("error_code") + ":" + object.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //2.获取当月近期假期
    public static List<FestivalOfMonth> getMonthInfo(Object year, Object month) {
        List<FestivalOfMonth> festivalOfMonthList = new ArrayList<>();
        String result = null;
        String url = URL_MONTH;//请求接口地址
        Map params = new HashMap();//请求参数
        String date = year.toString() + "-" + month.toString();
        params.put("key", APPKEY);//您申请的appKey
        params.put("year-month", date);//指定月份,格式为YYYY-MM,如月份和日期小于10,则取个位,如:2012-1
        try {
            result = net(url, params, "GET");
            JSONObject object = JSONObject.parseObject(result);
            if (object.getInteger("error_code") == 0) {
                JSONArray jsonArray = JSONArray.parseArray(((object.getJSONObject("result")).getJSONObject("data")).get("holiday").toString());
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    festivalOfMonthList.add(new FestivalOfMonth(jsonObject.getString("desc"), jsonObject.getString("festival"), jsonObject.getString("name")));
                }
            } else {
                System.out.println(object.get("error_code") + ":" + object.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return festivalOfMonthList;
    }

    //3.获取当年的假期列表
    public static List<FestivalOfYear> getYearInfo(Object year) {
        List<FestivalOfYear> festivalOfYearList = new ArrayList<>();
        String result = null;
        String url = URL_YEAR;//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("key", APPKEY);//您申请的appKey
        params.put("year", year.toString());//指定年份,格式为YYYY,如:2015

        try {
            result = net(url, params, "GET");
            JSONObject object = JSONObject.parseObject(result);
            if (object.getInteger("error_code") == 0) {
                JSONArray jsonArray = JSONArray.parseArray(((object.getJSONObject("result")).getJSONObject("data")).get("holidaylist").toString());
                for (int i = 0; i < jsonArray.size(); i++) {
                    festivalOfYearList.add(new FestivalOfYear(jsonArray.getJSONObject(i).getString("name"), jsonArray.getJSONObject(i).getString("startday")));
                }
            } else {
                System.out.println(object.get("error_code") + ":" + object.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return festivalOfYearList;
    }

    /**
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return 网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map params, String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if (method == null || method.equals("GET")) {
                strUrl = strUrl + "?" + urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if (method == null || method.equals("GET")) {
                conn.setRequestMethod("GET");
            } else {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params != null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    //将map型转为请求参数型
    public static String urlencode(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}