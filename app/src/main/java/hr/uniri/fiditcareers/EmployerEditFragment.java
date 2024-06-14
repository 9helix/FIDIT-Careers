package hr.uniri.fiditcareers;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

public class EmployerEditFragment extends Fragment {
    public EmployerEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employer_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        DashboardEmployer dashboardEmployer = (DashboardEmployer) getActivity();
        assert dashboardEmployer != null;
        HashMap<String, Object> employerData = dashboardEmployer.employerData;
        int id = (Integer) employerData.get("id");

        String name = (String) employerData.get("name");
        EditText nameEditText = getView().findViewById(R.id.editNameText);
        nameEditText.setText(name);

        String email = (String) employerData.get("email");
        EditText editEmailTxt = getView().findViewById(R.id.editEmailTxt);
        editEmailTxt.setText(email);

        String pass = (String) employerData.get("pass");
        EditText editPassTxt = getView().findViewById(R.id.editPassTxt);
        editPassTxt.setText(pass);

        EditText editPassConfTxt = getView().findViewById(R.id.editPassConfTxt);
        editPassConfTxt.setText(pass);

        Button saveButton = getView().findViewById(R.id.saveBtn);
        saveButton.setOnClickListener(view1 -> {
            String newName = nameEditText.getText().toString();
            String newEmail = editEmailTxt.getText().toString();
            String newPass = editPassTxt.getText().toString();

            if(newName.isEmpty()) {
                nameEditText.setError("Unesite naziv tvrtke");
                return;
            }
            if(newEmail.isEmpty()) {
                editEmailTxt.setError("Unesite e-mail.");
                return;
            }
            if(newPass.isEmpty()) {
                editPassTxt.setError("Unesite lozinku.");
                return;
            }
            if(!newPass.equals(editPassConfTxt.getText().toString())) {
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Lozinke se ne podudaraju.", Toast.LENGTH_SHORT).show());
                return;
            }

            new Thread(() -> {
                AppDatabase appDatabase = dashboardEmployer.appDatabase;
                Employer employer = appDatabase.employerDao().getEmployerById(id);

                employer.employerName = newName;
                employer.email = newEmail;
                appDatabase.employerDao().updateEmployer(employer);
                // update employer name on posts
                appDatabase.postDao().updateEmployersOnPost(newName, id);

                ((GlobalVariable) getActivity().getApplication()).setEmail(newEmail);
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getActivity(), "Informacije ažurirane!", Toast.LENGTH_SHORT).show();
                });
            }).start();

            employerData.put("name", newName);
            employerData.put("email", newEmail);
            employerData.put("pass", newPass);

            Intent in = new Intent(getActivity(), DashboardEmployer.class);
            startActivity(in);
        });
    }
}