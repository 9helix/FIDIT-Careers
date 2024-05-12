package hr.uniri.fiditcareers;

import android.app.Application;

public class GlobalVariable extends Application {
    private String email, type;

    public String getEmail() {
        return email;
    }

    public void setEmail(String someVariable) {
        this.email = someVariable;
    }

    public String getType() {
        return type;
    }

    public void setType(String someVariable) {
        this.type = someVariable;
    }
}
