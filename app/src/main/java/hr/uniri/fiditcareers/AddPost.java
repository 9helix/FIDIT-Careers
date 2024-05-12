package hr.uniri.fiditcareers;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPost extends Fragment {
    private AppDatabase appDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View parentHolder = inflater.inflate(R.layout.fragment_add_post, container, false);
        appDatabase = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "app-db").build();

        Button postBtn = parentHolder.findViewById(R.id.postBtn);
        EditText jobNameTxt = parentHolder.findViewById(R.id.jobNameTxt);
        EditText requiredYearOfStudyTxt = parentHolder.findViewById(R.id.requiredYearOfStudyTxt);
        EditText requirementsTxt = parentHolder.findViewById(R.id.requirementsTxt);
        EditText descriptionTxt = parentHolder.findViewById(R.id.descriptionTxt);
        EditText emailTxt = parentHolder.findViewById(R.id.emailTxt);
        EditText phoneNumberTxt = parentHolder.findViewById(R.id.phoneNumberTxt);
        Spinner spinnerOption = parentHolder.findViewById(R.id.spinnerOptions);
        EditText locationTxt = parentHolder.findViewById(R.id.locationTxt);

        // when dropdown option is selected
        spinnerOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int onsiteOnline = spinnerOption.getSelectedItemPosition();
                if (onsiteOnline == 0) { // onsite option was selected
                    locationTxt.setVisibility(View.VISIBLE);
                } else { // online option was selected
                    locationTxt.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        postBtn.setOnClickListener(view -> {
            String jobName = jobNameTxt.getText().toString();
            String requiredYearOfStudy = requiredYearOfStudyTxt.getText().toString();
            String requirements = requirementsTxt.getText().toString();
            String description = descriptionTxt.getText().toString();
            String email = emailTxt.getText().toString();
            String phoneNumber = phoneNumberTxt.getText().toString();
            int onsiteOnline = spinnerOption.getSelectedItemPosition();
            String location = locationTxt.getText().toString();

            if(jobName.isEmpty()) {
                jobNameTxt.setError("Unesite naziv posla.");
                return;
            }
            if (requiredYearOfStudy.isEmpty()) {
                requiredYearOfStudyTxt.setError("Unesite minimalnu godinu studija.");
                return;
            } else {
                int reqYearOfStudy =  Integer.parseInt(requiredYearOfStudy);

                if(reqYearOfStudy < 1 || reqYearOfStudy > 5) {
                    requiredYearOfStudyTxt.setError("Godina studija mora biti u intervalu od 1 do 5.");
                    return;
                }
            }
            if(requirements.isEmpty()) {
                requirementsTxt.setError("Unesite potrebne vještine.");
                return;
            }
            if (onsiteOnline == 0) { // onsite
                if(location.isEmpty()) {
                    locationTxt.setError("Unesite lokaciju izvođenja posla.");
                    return;
                }
            }
            if(description.isEmpty()) {
                descriptionTxt.setError("Unesite opis posla.");
                return;
            }
            if(email.isEmpty() && phoneNumber.isEmpty()) {
                Toast.makeText(getActivity(), "Morate unijeti barem email ili telefonski broj.", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                String employerEmail = ((GlobalVariable) getActivity().getApplication()).getEmail();
                Employer employer = appDatabase.employerDao().getEmployerByEmail(employerEmail);
                int reqYearOfStudy =  Integer.parseInt(requiredYearOfStudy);

                // reformats the timestamp
                SimpleDateFormat sdf = new SimpleDateFormat("dd. MM. yyyy.");
                Date date = new Date();
                Timestamp currentDate = new Timestamp(date.getTime());

                // create new post
                Post newPost = new Post();
                newPost.jobName = jobName;
                newPost.reqStudyYear = reqYearOfStudy;
                newPost.requirements = requirements;
                newPost.desc = description;
                newPost.email = email;
                newPost.phone = phoneNumber;
                newPost.employerId = employer.id;
                newPost.employerName = employer.employerName;
                newPost.datePosted = sdf.format(currentDate);

                if (onsiteOnline == 0) { // onsite
                    newPost.onsiteOnline = "onsite";
                    newPost.location = location;
                } else { // online
                    newPost.onsiteOnline = "online";
                    newPost.location = "online";
                }
                // add created post to database
                appDatabase.postDao().insert(newPost);

                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Uspješno stvoren oglas!", Toast.LENGTH_SHORT).show());
                Intent in = new Intent(getActivity(), DashboardEmployer.class);
                startActivity(in);
            }).start();

        });

        return parentHolder;
    }
}