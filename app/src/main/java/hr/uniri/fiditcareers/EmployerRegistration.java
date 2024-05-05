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

public class EmployerRegistration extends AppCompatActivity {
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employer_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "app-db").build();

        Button loginBtn = findViewById(R.id.loginBtn);
        EditText emailTxt = findViewById(R.id.editEmailTxt);
        EditText passTxt = findViewById(R.id.editPassTxt);
        TextView registerTxt = findViewById(R.id.registrationRedirect);
        Switch roleSwitch = findViewById(R.id.roleSwitch2);
        EditText confirmPassTxt = findViewById(R.id.confirmPassTxt);
        EditText employerNametxt = findViewById(R.id.employerNameTxt);

        loginBtn.setOnClickListener(view -> {
            String email = emailTxt.getText().toString();
            String password = passTxt.getText().toString(); // Get password from passTxt
            String confirmPass = confirmPassTxt.getText().toString();
            String employerName = employerNametxt.getText().toString();

            if(employerName.isEmpty()) {
                employerNametxt.setError("Unesite naziv tvrtke");
                return;
            }
            if(email.isEmpty()) {
                emailTxt.setError("Unesite e-mail.");
                return;
            }
            if(!password.equals(confirmPass)) {
                //confirmPassTxt.setError("Lozinke se ne podudaraju.");
                runOnUiThread(() -> Toast.makeText(EmployerRegistration.this, "Lozinke se ne podudaraju.", Toast.LENGTH_SHORT).show());
                return;
            }

            new Thread(() -> {
                // Check if an employer with the given email exists in the database
                Employer employer = appDatabase.employerDao().getEmployerByEmail(email);

                // If count is greater than 0, an employer with the given email exists
                if (employer != null) {
                    runOnUiThread(() -> Toast.makeText(EmployerRegistration.this, "Poslodavac s tom e-mail adresom već postoji.", Toast.LENGTH_SHORT).show());

                } else {
                    // Create a new employer object
                    Employer newEmployer = new Employer();
                    newEmployer.email = email;
                    newEmployer.password = password;
                    newEmployer.employerName = employerName;

                    // Insert the employer object into the database
                    appDatabase.employerDao().insert(newEmployer);
                    runOnUiThread(() -> Toast.makeText(EmployerRegistration.this, "Registracija uspjela.", Toast.LENGTH_SHORT).show());

                    Intent i = new Intent(EmployerRegistration.this,EmployerLogin.class);
                    startActivity(i);
                }
            }).start();
        });

        registerTxt.setOnClickListener(view -> {
            Intent i = new Intent(EmployerRegistration.this,EmployerLogin.class);
            startActivity(i);
        });
        roleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Intent i = new Intent(EmployerRegistration.this,StudentRegistration.class);
            startActivity(i);
        });
    }
}