package hr.uniri.fiditcareers;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matchers;
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
        Result result = new JUnitCore().run(EmployerRegistrateLoginTest.class);

        // Post creation process
        // Click on the new post button
        Espresso.onView(withId(R.id.fab)).perform(ViewActions.click());

        // Click on the post title EditText, enter a title
        Espresso.onView(withId(R.id.jobNameTxt))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("Junior developer"), ViewActions.closeSoftKeyboard());

        // Click on the post content EditText, enter some content
        Espresso.onView(withId(R.id.requiredYearOfStudyTxt))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("2"), ViewActions.closeSoftKeyboard());

        Espresso.onView(withId(R.id.requirementsTxt))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("Znanje C++-a i Pythona\nPoznavanje objektnog programiranja"), ViewActions.closeSoftKeyboard());

        Espresso.onView(withId(R.id.spinnerOptions)).perform(ViewActions.click());
        Espresso.onData(Matchers.anything()).atPosition(1).perform(ViewActions.click());

        Espresso.onView(withId(R.id.descriptionTxt))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("Testiranje C++ i Python skripti."), ViewActions.closeSoftKeyboard());

        Espresso.onView(withId(R.id.emailTxt))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("info@ris.hr"), ViewActions.closeSoftKeyboard());


        // Click on the submit button
        Espresso.onView(withId(R.id.postBtn)).perform(ViewActions.click());

        // delete created post
        Espresso.onView(ViewMatchers.withId(R.id.internshipDelete)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.yesBtn)).perform(ViewActions.click());

        // logout from app
        Espresso.onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        Espresso.onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_logout));
    }
}
