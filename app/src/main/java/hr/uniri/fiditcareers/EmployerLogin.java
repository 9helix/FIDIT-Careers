package hr.uniri.fiditcareers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.util.List;

public class EmployerLogin extends AppCompatActivity {
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employer_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "app-db").build();

        Button loginBtn = findViewById(R.id.loginBtn);
        EditText emailTxt = findViewById(R.id.emailTxt);
        EditText passTxt = findViewById(R.id.passTxt);
        TextView registerTxt = findViewById(R.id.registrationRedirect);
        Switch roleSwitch = findViewById(R.id.roleSwitch2);

        loginBtn.setOnClickListener(view -> {
            String email = emailTxt.getText().toString();
            String password = passTxt.getText().toString(); // Get password from passTxt

            new Thread(() -> {
                // Check if an employer with the given email exists in the database
                Employer employer = appDatabase.employerDao().getEmployerByEmail(email);

                // If count is greater than 0, an employer with the given email exists
                if (employer != null && employer.password.equals(password)) {
                    runOnUiThread(() -> Toast.makeText(EmployerLogin.this, "Prijava uspješna!", Toast.LENGTH_SHORT).show());

                    // store email in a public variable
                    ((PublicVariable) this.getApplication()).setEmail(email);
                    Intent i = new Intent(EmployerLogin.this,DashboardEmployer.class);
                    startActivity(i);
                } else {
                    runOnUiThread(() -> Toast.makeText(EmployerLogin.this, "Neuspjela prijava. Provjerite podatke.", Toast.LENGTH_SHORT).show());
                }
            }).start();

        });

        registerTxt.setOnClickListener(view -> {
            Intent i = new Intent(EmployerLogin.this,EmployerRegistration.class);
            startActivity(i);
        });
        roleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Intent i = new Intent(EmployerLogin.this,StudentLogin.class);
            startActivity(i);
        });
    }
}