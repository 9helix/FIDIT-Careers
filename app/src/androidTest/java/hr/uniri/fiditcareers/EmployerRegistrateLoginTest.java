package hr.uniri.fiditcareers;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class EmployerRegistrateLoginTest {
    @Test
    public void registerAndLogin() {
        Result result = new JUnitCore().run(EmployerRegistrationTest.class);
        System.out.println("Result: " + result.wasSuccessful());

        // Login process
        // Click on the email EditText, enter the registered email address
        Espresso.onView(ViewMatchers.withId(R.id.editEmailTxt))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("ris@ris.hr"), ViewActions.closeSoftKeyboard());

        // Click on the password EditText, enter the registered password
        Espresso.onView(ViewMatchers.withId(R.id.editPassTxt))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("1234"), ViewActions.closeSoftKeyboard());

        // Click on the login button
        Espresso.onView(ViewMatchers.withId(R.id.loginBtn)).perform(ViewActions.click());
    }
}
