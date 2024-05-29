package hr.uniri.fiditcareers;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.KeyEvent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class StudentLoginEditDataTest {
    @Rule
    public ActivityScenarioRule<StudentRegistration> activityRule =
            new ActivityScenarioRule<>(StudentRegistration.class);

    @Test
    public void LoginEditTest() {
        Espresso.onView(ViewMatchers.withId(R.id.nameTxt))
                .perform(click())
                .perform(ViewActions.typeText("Dino"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.editSurnameTxt))
                .perform(click())
                .perform(ViewActions.typeText("Grzinic"), ViewActions.closeSoftKeyboard());

        Espresso.onView(withId(R.id.editStudyYearTxt))
                .perform(click())
                .perform(ViewActions.typeText("2"), ViewActions.closeSoftKeyboard());

        Espresso.onView(withId(R.id.editEmailTxt))
                .perform(click())
                .perform(ViewActions.typeText("test@mail.com"), ViewActions.closeSoftKeyboard());

        Espresso.onView(withId(R.id.editPassTxt))
                .perform(click())
                .perform(ViewActions.typeText("1234"), ViewActions.closeSoftKeyboard());

        Espresso.onView(withId(R.id.confirmPassTxt))
                .perform(click())
                .perform(ViewActions.typeText("1234"), ViewActions.closeSoftKeyboard());

        // Click on the registration button
        Espresso.onView(ViewMatchers.withId(R.id.loginBtn)).perform(click());


        // include previously made student login test
        Result result = new JUnitCore().run(StudentLoginTest.class);
        System.out.println("Result: " + result.wasSuccessful());


        // open sidebar menu and choose edit data option
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        Espresso.onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_edit_student));

        // edit some data about student

        String name = "Dino";
        // delete name in the first text field
        for(int i=0; i <= name.length(); i++) {
            Espresso.onView(ViewMatchers.withId(R.id.editNameText))
                    .perform(click())
                    .perform(pressKey(KeyEvent.KEYCODE_DEL));
        }
        Espresso.onView(ViewMatchers.withId(R.id.editNameText))
                .perform(ViewActions.typeText("Marin"), ViewActions.closeSoftKeyboard());

        name = "Grzinic";
        // delete name in the second text field
        for(int i=0; i < name.length(); i++) {
            Espresso.onView(ViewMatchers.withId(R.id.editSurnameTxt))
                    .perform(click())
                    .perform(pressKey(KeyEvent.KEYCODE_DEL));
        }
        Espresso.onView(ViewMatchers.withId(R.id.editSurnameTxt))
                .perform(click())
                .perform(ViewActions.typeText("Rabadija"), ViewActions.closeSoftKeyboard());

        // save edited data
        Espresso.onView(ViewMatchers.withId(R.id.saveBtn)).perform(click());

        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
    }
}
