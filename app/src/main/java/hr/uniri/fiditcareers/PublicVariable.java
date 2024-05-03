package hr.uniri.fiditcareers;

import android.app.Application;

public class PublicVariable extends Application {
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String someVariable) {
        this.email = someVariable;
    }
}
