package com.mahmoudtarrasse.baking;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mahmoudtarrasse.baking.ui.MainActivity;
import com.mahmoudtarrasse.baking.ui.StepActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by mahmoud on 18/05/17.
 */

@RunWith(AndroidJUnit4.class)
public class StepActivityTest {
    @Rule
    public ActivityTestRule<StepActivity> mActivityRule =
            new ActivityTestRule<StepActivity>(StepActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, MainActivity.class);
                    result.putExtra(Utility.EXTRA_STEP_ID, 1);
                    result.putExtra(Utility.EXTRA_RECIPE_ID, 1);
                    return result;
                }
            };

    @Test
    public void nextTest(){
        Espresso.onView(withId(R.id.step_name)).check(ViewAssertions.matches(isDisplayed()));
        Espresso.onView(withId(R.id.next_button)).perform(click());
        Espresso.onView(withId(R.id.step_name)).check(ViewAssertions.matches(isDisplayed()));
    }
    @Test
    public void prevTest(){
        Espresso.onView(withId(R.id.step_name)).check(ViewAssertions.matches(isDisplayed()));
        Espresso.onView(withId(R.id.prev_button)).perform(click());
        Espresso.onView(withId(R.id.step_name)).check(ViewAssertions.matches(isDisplayed()));
    }


}

























