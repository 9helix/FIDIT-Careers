package hr.uniri.fiditcareers;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

// unit test
@RunWith(AndroidJUnit4.class)
public class EmployerRegistrationTest {

    @Rule
    public ActivityScenarioRule<EmployerRegistration> activityRule =
            new ActivityScenarioRule<>(EmployerRegistration.class);

    @Test
    public void testRegistration() {
        Espresso.onView(ViewMatchers.withId(R.id.employerNameTxt))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("RIS d.o.o."), ViewActions.closeSoftKeyboard());

        // Click on the email EditText, enter an email address
        Espresso.onView(ViewMatchers.withId(R.id.editEmailTxt))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("ris@ris.hr"), ViewActions.closeSoftKeyboard());

        // Click on the password EditText, enter a password
        Espresso.onView(ViewMatchers.withId(R.id.editPassTxt))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("1234"), ViewActions.closeSoftKeyboard());

        // Click on the confirm password EditText, enter the same password
        Espresso.onView(ViewMatchers.withId(R.id.confirmPassTxt))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("1234"), ViewActions.closeSoftKeyboard());

        // Click on the registration button
        Espresso.onView(ViewMatchers.withId(R.id.loginBtn)).perform(ViewActions.click());
    }
}
