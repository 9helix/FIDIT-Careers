package hr.uniri.fiditcareers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "app-db").build();

        Button loginBtn = findViewById(R.id.loginBtn);
        EditText emailTxt = findViewById(R.id.emailTxt);
        EditText passTxt = findViewById(R.id.passTxt);
        TextView registerTxt = findViewById(R.id.registrationRedirect);
        Switch roleSwitch = findViewById(R.id.roleSwitch2);

        loginBtn.setOnClickListener(view -> {
            String email = emailTxt.getText().toString();
            Log.d("MainActivity", "Email: " + email);
            String password = passTxt.getText().toString(); // Get password from passTxt
            Log.d("MainActivity", "Password: " + password);

            //Intent i = new Intent(MainActivity.this,StudentLogin.class);
            //startActivity(i);


            new Thread(() -> {
                // Check if a student with the given email exists in the database
                Student student = appDatabase.studentDao().getStudentByEmail(email);

                // If count is greater than 0, a student with the given email exists
                if (student != null && student.password.equals(password)) {
                    Log.d("MainActivity", "Login successful.");
                } else {
                    Log.d("MainActivity", "Login failed.");
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