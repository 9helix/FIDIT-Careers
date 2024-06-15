package hr.uniri.fiditcareers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.room.Room;

import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class DashboardEmployer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawerLayout;
    public AppDatabase appDatabase;
    public HashMap<String, Object> employerData = new HashMap<>();
    public String employerEmailArg;

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

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PostsDisplayEmployer()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "app-db").build();

        new Thread(() -> {
            String email = ((GlobalVariable) this.getApplication()).getEmail();
            Employer employer = appDatabase.employerDao().getEmployerByEmail(email);

            // gets the header menu
            TextView employerName, employerEmail;
            View header = navigationView.getHeaderView(0);

            ImageView myImageView = header.findViewById(R.id.myImageView);
            myImageView.setImageResource(R.drawable.round_business_24);

            // sets name and email of logged employer on menu
            employerName = header.findViewById(R.id.nameSurnamePlaceholder);
            employerEmail = header.findViewById(R.id.emailPlaceholder);
            employerName.setText(employer.employerName);
            employerEmail.setText(employer.email);

            employerEmailArg = employer.email;
            employerData.put("id", employer.id);
            employerData.put("name", employer.employerName);
            employerData.put("email", employer.email);
            employerData.put("pass", employer.password);
        }).start();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PostsDisplayEmployer()).commit();
                break;
            case R.id.nav_edit_student:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EmployerEdit()).commit();
                break;
            case R.id.nav_delete:
                DeleteAccount dialogFragment = DeleteAccount.newInstance(employerEmailArg, "employer");
                dialogFragment.show(getSupportFragmentManager(),"Obriši korisnički račun");
                break;
            case R.id.nav_logout:
                AtomicReference<SharedPreferences> sharedPreferences = new AtomicReference<>(getSharedPreferences("MySharedPref", MODE_PRIVATE));
                SharedPreferences.Editor myEdit = sharedPreferences.get().edit();
                myEdit.putBoolean("isLoggedIn", false);
                myEdit.putString("email", ""); // store email
                myEdit.putString("type", "");
                myEdit.apply();

                Toast.makeText(this, "Uspješna odjava!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(DashboardEmployer.this,EmployerLogin.class);
                startActivity(i);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}