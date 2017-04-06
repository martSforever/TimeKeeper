package com.martsforever.owa.timekeeper.util;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.martsforever.owa.timekeeper.javabean.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OWA on 2017/4/5.
 */

public class DataUtils {

    /*get test friends data*/
    public static List<AVUser> getFriendsData() {
        List<AVUser> persons = new ArrayList<>();
        AVUser p1 = new AVUser();
        p1.setUsername("gailun");
        p1.put(Person.NICK_NAME,"盖伦");
        p1.setPassword("111111");
        AVUser p2 = new AVUser();
        p2.setUsername("aixi");
        p2.put(Person.NICK_NAME,"艾希");
        p2.setPassword("111111");
        AVUser p3 = new AVUser();
        p3.setUsername("kaitelin");
        p3.put(Person.NICK_NAME,"凯特琳");
        p3.setPassword("111111");
        AVUser p4 = new AVUser();
        p4.setUsername("katelinna");
        p4.put(Person.NICK_NAME,"卡特琳娜");
        p4.setPassword("111111");
        AVUser p5 = new AVUser();
        p5.setUsername("yi");
        p5.put(Person.NICK_NAME,"易大师");
        p5.setPassword("111111");
        AVUser p6 = new AVUser();
        p6.setUsername("taidamier");
        p6.put(Person.NICK_NAME,"泰达米尔");
        p6.setPassword("111111");
        AVUser p7 = new AVUser();
        p7.setUsername("gujialasi");
        p7.put(Person.NICK_NAME,"古加拉斯");
        p7.setPassword("111111");
        AVUser p8 = new AVUser();
        p8.setUsername("moganna");
        p8.put(Person.NICK_NAME,"莫甘娜");
        p8.setPassword("111111");
        AVUser p9 = new AVUser();
        p9.setUsername("leiouna");
        p9.put(Person.NICK_NAME,"蕾欧娜");
        p9.setPassword("111111");
        AVUser p10 = new AVUser();
        p10.setUsername("namei");
        p10.put(Person.NICK_NAME,"娜美");
        p10.setPassword("111111");
        AVUser p11 = new AVUser();
        p11.setUsername("delaiesi");
        p11.put(Person.NICK_NAME,"德莱厄斯");
        p11.setPassword("111111");
        AVUser p12 = new AVUser();
        p12.setUsername("lakesi");
        p12.put(Person.NICK_NAME,"拉克丝");
        p12.setPassword("111111");

        persons.add(p1);
        persons.add(p2);
        persons.add(p3);
        persons.add(p4);
        persons.add(p5);
        persons.add(p6);
        persons.add(p7);
        persons.add(p8);
        persons.add(p9);
        persons.add(p10);
        persons.add(p11);
        persons.add(p12);
        return persons;
    }

    /*batch register users*/
    public static void signUsers(List<AVUser> users){
        for (final AVUser user:users){
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null){
                        System.out.println(user.get(Person.NICK_NAME).toString()+" sign up successful");
                    }
                    else {
                        System.out.println(e.getMessage());
                    }
                }
            });
        }
    }

}
