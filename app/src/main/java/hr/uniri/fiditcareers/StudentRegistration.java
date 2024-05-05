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
        EditText emailTxt = findViewById(R.id.editEmailTxt);
        EditText passTxt = findViewById(R.id.editPassTxt);
        TextView registerTxt = findViewById(R.id.registrationRedirect);
        Switch roleSwitch = findViewById(R.id.roleSwitch2);
        EditText nameTxt = findViewById(R.id.nameTxt);
        EditText surnameTxt = findViewById(R.id.editSurnameTxt);
        EditText studyYearTxt = findViewById(R.id.editStudyYearTxt);
        EditText confirmPassTxt = findViewById(R.id.confirmPassTxt);

        loginBtn.setOnClickListener(view -> {
            String name = nameTxt.getText().toString();
            String surname = surnameTxt.getText().toString();
            String studyYear = studyYearTxt.getText().toString();
            String email = emailTxt.getText().toString();
            String password = passTxt.getText().toString(); // Get password from passTxt
            String confirmPass = confirmPassTxt.getText().toString();

            if(name.isEmpty()) {
                nameTxt.setError("Unesite ime.");
                return;
            }
            if(surname.isEmpty()) {
                surnameTxt.setError("Unesite prezime.");
                return;
            }
            if (studyYear.isEmpty()) {
                studyYearTxt.setError("Unesite godinu studija.");
                return;
            } else {
                int yearOfStudyInt =  Integer.parseInt(studyYear);

                if(yearOfStudyInt < 1 || yearOfStudyInt > 5) {
                    studyYearTxt.setError("Godina studija mora biti u intervalu od 1 do 5.");
                    return;
                }
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
                runOnUiThread(() -> Toast.makeText(StudentRegistration.this, "Lozinke se ne podudaraju.", Toast.LENGTH_SHORT).show());
                return;
            }
            new Thread(() -> {
                // Check if a student with the given email exists in the database
                Student student = appDatabase.studentDao().getStudentByEmail(email);

                // If count is greater than 0, a student with the given email exists
                if (student != null) {
                    runOnUiThread(() -> Toast.makeText(StudentRegistration.this, "Student s tom e-mail adresom već postoji.", Toast.LENGTH_SHORT).show());
                } else {
                    // Create a new student object
                    int yearOfStudyInt = Integer.parseInt(studyYear);

                    Student newStudent = new Student();
                    newStudent.email = email;
                    newStudent.password = password;
                    newStudent.name = name;
                    newStudent.surname = surname;
                    newStudent.studyYear = yearOfStudyInt;

                    // Insert the student object into the database
                    appDatabase.studentDao().insert(newStudent);
                    runOnUiThread(() -> Toast.makeText(StudentRegistration.this, "Registracija uspjela.", Toast.LENGTH_SHORT).show());
                    Intent i = new Intent(StudentRegistration.this,StudentLogin.class);
                    startActivity(i);

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