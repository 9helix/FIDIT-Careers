package hr.uniri.fiditcareers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.concurrent.atomic.AtomicReference;

public class StudentLogin extends AppCompatActivity {
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AtomicReference<SharedPreferences> sharedPreferences = new AtomicReference<>(getSharedPreferences("MySharedPref", MODE_PRIVATE));
        boolean isLoggedIn = sharedPreferences.get().getBoolean("isLoggedIn", false);
        String savedEmail = sharedPreferences.get().getString("email", ""); // retrieve email
        String type = sharedPreferences.get().getString("type", ""); // retrieve email
        if (isLoggedIn) {
            ((GlobalVariable) this.getApplication()).setEmail(savedEmail);
            ((GlobalVariable) this.getApplication()).setType(type);

            if (type.equals("employer")){
                Intent i = new Intent(StudentLogin.this, DashboardEmployer.class);
                startActivity(i);
                finish();
            } else {
                // Use the email for whatever you need
                Intent i = new Intent(StudentLogin.this, DashboardStudent.class);
                startActivity(i);
                finish();
            }
        }

        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "app-db").build();

        Button loginBtn = findViewById(R.id.loginBtn);
        EditText emailTxt = findViewById(R.id.editEmailTxt);
        EditText passTxt = findViewById(R.id.editPassTxt);
        TextView registerTxt = findViewById(R.id.registrationRedirect);
        Switch roleSwitch = findViewById(R.id.roleSwitch2);

        loginBtn.setOnClickListener(view -> {
            String email = emailTxt.getText().toString();
            String password = passTxt.getText().toString(); // Get password from passTxt

            new Thread(() -> {
                // Check if a student with the given email exists in the database
                Student student = appDatabase.studentDao().getStudentByEmail(email);

                // If count is greater than 0, a student with the given email exists
                if (student != null && student.password.equals(password)) {
                    runOnUiThread(() -> Toast.makeText(StudentLogin.this, "Prijava uspješna!", Toast.LENGTH_SHORT).show());

                    CheckBox remember = findViewById(R.id.checkBox2);
                    if(remember.isChecked()){
                        SharedPreferences.Editor myEdit = sharedPreferences.get().edit();
                        myEdit.putBoolean("isLoggedIn", true);
                        myEdit.putString("email", email); // store email
                        myEdit.putString("type", "student");
                        myEdit.apply();
                    }

                    // store email and type in global variables
                    ((GlobalVariable) this.getApplication()).setEmail(email);
                    ((GlobalVariable) this.getApplication()).setType("student");

                    Intent i = new Intent(StudentLogin.this,DashboardStudent.class);
                    startActivity(i);
                } else {
                    runOnUiThread(() -> Toast.makeText(StudentLogin.this, "Neuspjela prijava. Provjerite podatke.", Toast.LENGTH_SHORT).show());
                }
            }).start();
        });

        registerTxt.setOnClickListener(view -> {
            Intent i = new Intent(StudentLogin.this, StudentRegistration.class);
            startActivity(i);
        });
        roleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Intent i = new Intent(StudentLogin.this, EmployerLogin.class);
            startActivity(i);
        });
    }
}