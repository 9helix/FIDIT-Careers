package hr.uniri.fiditcareers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
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

public class DashboardEmployer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawerLayout;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_employer);

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
            Employer employer = appDatabase.employerDao().getEmployerByEmail(email);

            TextView employerName, employerEmail, employerContact;
            View header = navigationView.getHeaderView(0);

            // sets name, surname and email of logged employer on menu
            employerName = header.findViewById(R.id.nameSurnamePlaceholder);
            employerEmail = header.findViewById(R.id.emailPlaceholder);
            employerContact = header.findViewById(R.id.employerContact);

            employerName.setText(employer.employerName);
            employerEmail.setText(employer.email);
            employerContact.setText(employer.contact);
        }).start();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Toast.makeText(this, "Home clicked!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_edit_student:
                Toast.makeText(this, "Settings clicked!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "Uspješna odjava!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(DashboardEmployer.this,EmployerLogin.class);
                startActivity(i);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}