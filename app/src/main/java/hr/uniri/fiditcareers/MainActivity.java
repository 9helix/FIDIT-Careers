package hr.uniri.fiditcareers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button loginBtn = findViewById(R.id.loginBtn);
        EditText emailTxt = findViewById(R.id.emailTxt);
        EditText passTxt = findViewById(R.id.passTxt);

        loginBtn.setOnClickListener(new View.OnClickListener()  {
            public void onClick(View view) {
                String email = emailTxt.getText().toString();
                Log.d("MainActivity", "Email: " + email);
                String password = passTxt.getText().toString(); // Get password from passTxt
                Log.d("MainActivity", "Password: " + password);

                Intent myIntent = new Intent(MainActivity.this, DashboardStudent.class);
                startActivity(myIntent);
            }

        });
    }
}