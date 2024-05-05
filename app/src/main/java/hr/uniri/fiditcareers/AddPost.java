package hr.uniri.fiditcareers;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

        postBtn.setOnClickListener(view -> {
            String jobName = jobNameTxt.getText().toString();
            String requiredYearOfStudy = requiredYearOfStudyTxt.getText().toString();
            String requirements = requirementsTxt.getText().toString();
            String description = descriptionTxt.getText().toString();
            String email = emailTxt.getText().toString();
            String phoneNumber = phoneNumberTxt.getText().toString();

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
            if(description.isEmpty()) {
                descriptionTxt.setError("Unesite opis posla.");
                return;
            }
            if(email.isEmpty() && phoneNumber.isEmpty()) {
                Toast.makeText(getActivity(), "Morate unijeti barem email ili telefonski broj.", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                String employerEmail = ((PublicVariable) getActivity().getApplication()).getEmail();
                Employer employer = appDatabase.employerDao().getEmployerByEmail(employerEmail);
                int reqYearOfStudy =  Integer.parseInt(requiredYearOfStudy);

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
                newPost.datePosted = sdf.format(currentDate);
                // add created post to database
                appDatabase.postDao().insert(newPost);

                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Uspješno stvoren oglas!", Toast.LENGTH_SHORT).show());
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PostsDisplayEmployer()).commit();
            }).start();
        });

        return parentHolder;
    }
}