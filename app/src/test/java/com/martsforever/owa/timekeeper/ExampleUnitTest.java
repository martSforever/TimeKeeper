package com.martsforever.owa.timekeeper;

import org.junit.Test;

import java.util.Date;

import cn.bmob.v3.datatype.BmobDate;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void bmobDate() throws Exception {
        BmobDate date = new BmobDate(new Date());
        System.out.println(date.getDate());
    }

}