package com.example.brendon.productivityapp;

import org.junit.Test;

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
    public void planShouldSave() throws Exception {
        String plan = "1. Wake up\n2. Shower\n3. Eat Breakfast\n";

        Goal testGoal = new Goal();
        testGoal.setPlan(plan);
        testGoal.savePlan();
        assertEquals(plan, testGoal.getPlan());
    }

    @Test
    public void shouldReturnMilli() throws Exception {
        Time time = new Time();
        time.setHours(2);
        time.setMinutes(30);
        time.setSeconds(5);
        assertEquals(time.getMilliseconds(), 9005000);
    }

    @Test
    public void shouldReturnTrue() throws Exception {
        Settings mySettings = new Settings();

        mySettings.setNotifications(true);
        assertTrue(mySettings.isNotifications());
    }
}

