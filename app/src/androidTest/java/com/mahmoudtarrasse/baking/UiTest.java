package com.mahmoudtarrasse.baking;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import com.mahmoudtarrasse.baking.ui.MainActivity;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by mahmoud on 13/05/17.
 */

@RunWith(AndroidJUnit4.class)
public class UiTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Test
    public void testList(){
        Espresso.onView(
                ViewMatchers.withId(R.id.recipes_list)
        ).perform(ViewActions.click())
        .check(ViewAssertions.doesNotExist());
    }
}
