package hr.uniri.fiditcareers;

import android.view.ViewGroup;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

// end-to-end test
@RunWith(AndroidJUnit4.class)
public class EmployerPostCreationTest {
    /*
        @Rule
        public ActivityScenarioRule<EmployerLogin> activityRule =
                new ActivityScenarioRule<>(EmployerLogin.class);
    */
    @Test
    public void testLoginAndPostCreation() {
        Result result = new JUnitCore().run(EmployerRegistrationTest.class);
        // Login process
        // Click on the email EditText, enter the registered email address
        System.out.println("Result: " + result.wasSuccessful());
        Espresso.onView(ViewMatchers.withId(R.id.editEmailTxt))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("ris@ris.hr"), ViewActions.closeSoftKeyboard());

        // Click on the password EditText, enter the registered password
        Espresso.onView(ViewMatchers.withId(R.id.editPassTxt))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("1234"), ViewActions.closeSoftKeyboard());

        // Click on the login button
        Espresso.onView(ViewMatchers.withId(R.id.loginBtn)).perform(ViewActions.click());

        // Post creation process
        // Click on the new post button
        Espresso.onView(ViewMatchers.withId(R.id.fab)).perform(ViewActions.click());

        // Click on the post title EditText, enter a title
        Espresso.onView(ViewMatchers.withId(R.id.jobNameTxt))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("Junior developer"), ViewActions.closeSoftKeyboard());

        // Click on the post content EditText, enter some content
        Espresso.onView(ViewMatchers.withId(R.id.requiredYearOfStudyTxt))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("2"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.requirementsTxt))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("Znanje C++-a i Pythona\nPoznavanje objektnog programiranja"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.spinnerOptions)).perform(ViewActions.click());
        Espresso.onData(Matchers.anything()).atPosition(1).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.descriptionTxt))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("Testiranje C++ i Python skripti."), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.emailTxt))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("info@ris.hr"), ViewActions.closeSoftKeyboard());


        // Click on the submit button
        Espresso.onView(ViewMatchers.withId(R.id.postBtn)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.internshipDelete)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.yesBtn)).perform(ViewActions.click());


        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
        Espresso.onView(ViewMatchers.withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_logout));

    }
}
