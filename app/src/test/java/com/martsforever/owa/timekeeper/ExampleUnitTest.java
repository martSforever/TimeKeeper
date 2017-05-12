package com.martsforever.owa.timekeeper;

import com.martsforever.owa.timekeeper.main.todo.festival.FestivalOfMonth;
import com.martsforever.owa.timekeeper.main.todo.festival.FestivalUtils;
import com.martsforever.owa.timekeeper.main.todo.festival.Lunar;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    static SimpleDateFormat chineseDateFormat = new SimpleDateFormat(
            "yyyy年MM月dd日");

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testFestivalInterface() throws Exception {
        System.out.println(FestivalUtils.getDayInfo(2017,1,1).toString());
    /*    List<FestivalOfMonth> dayOfMonthInfoList = FestivalUtils.getMonthInfo(2017, 1);
        for (FestivalOfMonth dayOfMonthInfo : dayOfMonthInfoList) {
            System.out.println(dayOfMonthInfo.toString());
        }*/
      /*  List<FestivalOfYear> festivalOfYearList = FestivalUtils.getYearInfo(2017);
        for (FestivalOfYear festivalOfYear : festivalOfYearList) {
            System.out.println(festivalOfYear.toString());
        }*/
    }

    @Test
    public void testLunar() throws Exception {
        Calendar today = Calendar.getInstance();
        today.setTime(chineseDateFormat.parse("2017年10月29日"));
        Lunar lunar = new Lunar(today);
        System.out.println("北京时间：" + chineseDateFormat.format(today.getTime())
                + "　农历" + lunar);
        System.out.println(lunar.getLunarMonthAndDay());
    }

}