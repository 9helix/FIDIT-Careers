package hr.uniri.fiditcareers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

public class MainActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    private boolean isStudent = true;
    private boolean isLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "app-db").build();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button loginBtn = findViewById(R.id.loginBtn);
        EditText emailTxt = findViewById(R.id.editEmailTxt);
        EditText passTxt = findViewById(R.id.editPassTxt);
        TextView registerTxt = findViewById(R.id.registrationRedirect);
        TextView loginTitle = findViewById(R.id.loginTitle);
        TextView callTxt = findViewById(R.id.callTxt);
        Switch roleSwitch = findViewById(R.id.roleSwitch2);
        EditText nameTxt = findViewById(R.id.nameTxt);
        EditText surnameTxt = findViewById(R.id.editSurnameTxt);
        EditText studyYearTxt = findViewById(R.id.editStudyYearTxt);
        EditText confirmPassTxt = findViewById(R.id.confirmPassTxt);

        EditText employerNametxt = findViewById(R.id.employerNameTxt);
        EditText contactTxt = findViewById(R.id.contactTxt);

        nameTxt.setVisibility(View.GONE);
        surnameTxt.setVisibility(View.GONE);
        studyYearTxt.setVisibility(View.GONE);
        confirmPassTxt.setVisibility(View.GONE);
        employerNametxt.setVisibility(View.GONE);
        contactTxt.setVisibility(View.GONE);

        loginBtn.setOnClickListener(view -> {
            String email = emailTxt.getText().toString();
            Log.d("MainActivity", "Email: " + email);
            String password = passTxt.getText().toString(); // Get password from passTxt
            Log.d("MainActivity", "Password: " + password);

            Intent i = new Intent(MainActivity.this,StudentLogin.class);
            startActivity(i);
            /*
            if (isLogin) {
                if (isStudent) {            // Create a new thread to perform database operations
                    new Thread(() -> {
                        // Check if a student with the given email exists in the database
                        Student student = appDatabase.studentDao().getStudentByEmail(email);

                        // If count is greater than 0, a student with the given email exists
                        if (student != null && student.password.equals(password)) {
                            Log.d("MainActivity", "Login successful.");
                        } else {
                            Log.d("MainActivity", "Login failed.");
                        }
                    }).start();
                } else {
                    new Thread(() -> {
                        // Check if an employer with the given email exists in the database
                        Employer employer = appDatabase.employerDao().getEmployerByEmail(email);

                        // If count is greater than 0, an employer with the given email exists
                        if (employer != null && employer.password.equals(password)) {
                            Log.d("MainActivity", "Login successful.");
                        } else {
                            Log.d("MainActivity", "Login failed.");
                        }
                    }).start();
                }
            } else {
                if (isStudent) {
                    new Thread(() -> {
                        // Check if a student with the given email exists in the database
                        Student student = appDatabase.studentDao().getStudentByEmail(email);

                        // If count is greater than 0, a student with the given email exists
                        if (student != null) {
                            Log.d("MainActivity", "Student with the given email already exists.");
                        } else {
                            // Create a new student object
                            Student newStudent = new Student();
                            newStudent.email = email;
                            newStudent.password = password;

                            // Insert the student object into the database
                            appDatabase.studentDao().insert(newStudent);
                            Log.d("MainActivity", "Student registration successful.");
                        }
                    }).start();
                } else {
                    new Thread(() -> {
                        // Check if an employer with the given email exists in the database
                        Employer employer = appDatabase.employerDao().getEmployerByEmail(email);

                        // If count is greater than 0, an employer with the given email exists
                        if (employer != null) {
                            Log.d("MainActivity", "Employer with the given email already exists.");
                        } else {
                            // Create a new employer object
                            Employer newEmployer = new Employer();
                            newEmployer.email = email;
                            newEmployer.password = password;

                            // Insert the employer object into the database
                            appDatabase.employerDao().insert(newEmployer);
                            Log.d("MainActivity", "Employer registration successful.");
                        }
                    }).start();
                }
            }*/


            //Intent myIntent = new Intent(MainActivity.this, DashboardStudent.class);
            //startActivity(myIntent);
        });
        registerTxt.setOnClickListener(view -> {
            if (isLogin) {
                if (roleSwitch.isChecked()) {loginTitle.setText("Registracija za poslodavce");
                    employerNametxt.setVisibility(View.VISIBLE);
                    contactTxt.setVisibility(View.VISIBLE);

                    nameTxt.setVisibility(View.GONE);
                    surnameTxt.setVisibility(View.GONE);
                    studyYearTxt.setVisibility(View.GONE);
                }
                else {
                    loginTitle.setText("Registracija za studente");
                    nameTxt.setVisibility(View.VISIBLE);
                    surnameTxt.setVisibility(View.VISIBLE);
                    studyYearTxt.setVisibility(View.VISIBLE);

                    employerNametxt.setVisibility(View.GONE);
                    contactTxt.setVisibility(View.GONE);
                }


                confirmPassTxt.setVisibility(View.VISIBLE);

                callTxt.setText("Postojeći korisnik? ");
                registerTxt.setText("Prijavi se.");
                loginBtn.setText("Registriraj se");
            } else {
                if (roleSwitch.isChecked()) {loginTitle.setText("Prijava za poslodavce");
                    employerNametxt.setVisibility(View.GONE);
                    contactTxt.setVisibility(View.GONE);
                }
                else {loginTitle.setText("Prijava za studente");
                    nameTxt.setVisibility(View.GONE);
                    surnameTxt.setVisibility(View.GONE);
                    studyYearTxt.setVisibility(View.GONE);}
                confirmPassTxt.setVisibility(View.GONE);

                callTxt.setText("Novi korisnik? ");
                registerTxt.setText("Registriraj se.");
                loginBtn.setText("Prijavi se");
            }
            isLogin = !isLogin;
        });
        roleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (isLogin)
                    loginTitle.setText("Prijava za poslodavce");
                else
                    loginTitle.setText("Registracija za poslodavce");
            } else {
                if (isLogin)
                    loginTitle.setText("Prijava za studente");
                else
                    loginTitle.setText("Registracija za studente");
            }
            isStudent = !isStudent;
        });
    }
}