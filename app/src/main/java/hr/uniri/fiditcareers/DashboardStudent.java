package hr.uniri.fiditcareers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.room.Room;

import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

public class DashboardStudent extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawerLayout;
    public AppDatabase appDatabase;
    public HashMap<String, Object> studentData = new HashMap<>();
    private String studentEmailArg = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_student);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open,
                R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "app-db").build();

        new Thread(() -> {
            String email = ((PublicVariable) this.getApplication()).getEmail();
            Student student = appDatabase.studentDao().getStudentByEmail(email);

            TextView studentNameSurname, studentEmail, employerContact;
            View header = navigationView.getHeaderView(0);

            // sets name, surname and email of logged student on menu
            studentNameSurname = header.findViewById(R.id.nameSurnamePlaceholder);
            studentEmail = header.findViewById(R.id.emailPlaceholder);

            studentNameSurname.setText(student.name + " " + student.surname);

            studentEmailArg = student.email;
            studentData.put("id", student.id);
            studentData.put("email", student.email);
            studentData.put("pass", student.password);
            studentData.put("about", student.aboutMe);
            studentData.put("name", student.name);
            studentData.put("surname", student.surname);
            studentData.put("year", student.studyYear);


            studentEmail.setText(student.email);
        }).start();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent in = new Intent(DashboardStudent.this,DashboardStudent.class);
                startActivity(in);
                break;
            case R.id.nav_edit_student:
                SearchView searchView = findViewById(R.id.search);
                searchView.setVisibility(View.GONE);

                StudentEditFragment fragment = StudentEditFragment.newInstance(studentEmailArg);
                getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment).commit();

                //Toast.makeText(this, "Settings clicked!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "Uspješna odjava!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(DashboardStudent.this,StudentLogin.class);
                startActivity(i);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}