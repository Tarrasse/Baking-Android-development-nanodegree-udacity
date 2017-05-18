package com.mahmoudtarrasse.baking;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;


import com.mahmoudtarrasse.baking.ui.MainActivity;
import com.mahmoudtarrasse.baking.ui.RecipeActivity;
import com.mahmoudtarrasse.baking.ui.StepActivity;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;



/**
 * Created by mahmoud on 18/05/17.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityRule =
            new ActivityTestRule<RecipeActivity>(RecipeActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, MainActivity.class);
                    result.putExtra(Utility.EXTRA_RECIPE_ID, 1);
                    return result;
                }
            };


    @Test
    public void checkIfIngredientsExists(){
        Espresso.onView(ViewMatchers.withId(R.id.ingredients_list_linear_layout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
    @Test
    public void goToStepActivity(){
        Intents.init();

        Espresso.onView(
                ViewMatchers.withId(R.id.steps_recycler_view)
        ).perform(ViewActions.click());
        intended(toPackage(Utility.CONTENT_AUTHORITY));
        intended(hasComponent(StepActivity.class.getName()));
        Intents.release();
    }


}
