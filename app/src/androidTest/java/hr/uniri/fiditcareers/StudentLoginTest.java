package hr.uniri.fiditcareers;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

//unit test
@RunWith(AndroidJUnit4.class)

    public class StudentLoginTest {

    @Rule
    public ActivityScenarioRule<StudentLogin> activityRule =
            new ActivityScenarioRule<>(StudentLogin.class);

    @Test
    public void testLogin() {
        // Click on the email EditText, enter an email address
        Espresso.onView(ViewMatchers.withId(R.id.editEmailTxt))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("test@mail.com"), ViewActions.closeSoftKeyboard());

        // Click on the password EditText, enter a password
        Espresso.onView(ViewMatchers.withId(R.id.editPassTxt))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("1234"), ViewActions.closeSoftKeyboard());

        // Click on the login button
        Espresso.onView(ViewMatchers.withId(R.id.loginBtn)).perform(ViewActions.click());
    }

}
