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

public class StudentRegistration extends AppCompatActivity {
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_registration);
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
        EditText nameTxt = findViewById(R.id.nameTxt);
        EditText surnameTxt = findViewById(R.id.surnameTxt);
        EditText studyYearTxt = findViewById(R.id.studyYearTxt);
        EditText confirmPassTxt = findViewById(R.id.confirmPassTxt);

        loginBtn.setOnClickListener(view -> {
            String name = nameTxt.getText().toString();
            String surname = surnameTxt.getText().toString();
            int studyYear = Integer.parseInt(studyYearTxt.getText().toString());
            String email = emailTxt.getText().toString();
            Log.d("MainActivity", "Email: " + email);
            String password = passTxt.getText().toString(); // Get password from passTxt
            Log.d("MainActivity", "Password: " + password);
            String confirmPass = confirmPassTxt.getText().toString();

            //Intent i = new Intent(MainActivity.this,StudentLogin.class);
            //startActivity(i);
            if(name.isEmpty()) {
                emailTxt.setError("Unesite ime");
                return;
            }
            if(surname.isEmpty()) {
                emailTxt.setError("Unesite prezime");
                return;
            }
            if(studyYear>5 || studyYear<1) {
                emailTxt.setError("Godina studija mora biti u intervalu od 1 do 5.");
                return;
            }
            if(email.isEmpty()) {
                emailTxt.setError("Unesite e-mail.");
                return;
            }
            if(password.length()<4) {
                passTxt.setError("Lozinka mora biti duga bar 4 znaka.");
                return;
            }
            if(!password.equals(confirmPass)) {
                //confirmPassTxt.setError("Lozinke se ne podudaraju.");
                runOnUiThread(() -> Toast.makeText(StudentRegistration.this, "Lozinke se ne podudaraju.", Toast.LENGTH_SHORT).show());

                return;
            }
            new Thread(() -> {
                // Check if a student with the given email exists in the database
                Student student = appDatabase.studentDao().getStudentByEmail(email);


                // If count is greater than 0, a student with the given email exists
                if (student != null) {
                    Log.d("MainActivity", "Student with the given email already exists.");
                    runOnUiThread(() -> Toast.makeText(StudentRegistration.this, "Student s tom e-mail adresom već postoji.", Toast.LENGTH_SHORT).show());

                } else {
                    // Create a new student object
                    Student newStudent = new Student();
                    newStudent.email = email;
                    newStudent.password = password;
                    newStudent.name = name;
                    newStudent.surname = surname;
                    newStudent.studyYear = studyYear;

                    // Insert the student object into the database
                    appDatabase.studentDao().insert(newStudent);
                    Log.d("MainActivity", "Student registration successful.");
                }
            }).start();

        });

        registerTxt.setOnClickListener(view -> {
            Intent i = new Intent(StudentRegistration.this,StudentLogin.class);
            startActivity(i);
        });
        roleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Intent i = new Intent(StudentRegistration.this,EmployerRegistration.class);
            startActivity(i);
        });
    }
}