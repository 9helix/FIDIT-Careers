package hr.uniri.fiditcareers;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.Objects;

public class EditPost extends Fragment {
    int postId;
    private AppDatabase appDatabase;

    public EditPost() {
        // Required empty public constructor
    }

    public static EditPost newInstance(int id) {
        EditPost fragment = new EditPost();
        Bundle args = new Bundle();
        args.putInt("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postId = getArguments().getInt("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_post, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        appDatabase = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "app-db").build();

        Button savePostBtn = getView().findViewById(R.id.savePostBtn);
        EditText jobNameTxt = getView().findViewById(R.id.editJobNameTxt);
        EditText requiredYearOfStudyTxt = getView().findViewById(R.id.editRequiredYearOfStudyTxt);
        EditText requirementsTxt = getView().findViewById(R.id.editRequirementsTxt);
        Spinner spinnerOption = getView().findViewById(R.id.spinnerOptions);
        EditText locationTxt = getView().findViewById(R.id.editLocationTxt);
        EditText descriptionTxt = getView().findViewById(R.id.editDescTxt);
        EditText emailTxt = getView().findViewById(R.id.editEmailTxt);
        EditText phoneNumberTxt = getView().findViewById(R.id.editPhoneNumberTxt);
        TableRow locationTxtRow = getView().findViewById(R.id.locationTxtRow);

        // when dropdown option is selected
        spinnerOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int onsiteOnline = spinnerOption.getSelectedItemPosition();
                if (onsiteOnline == 0) { // onsite option was selected
                    locationTxtRow.setVisibility(View.VISIBLE);
                } else { // online option was selected
                    locationTxtRow.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        new Thread(() -> {
            Post post = appDatabase.postDao().getPostById(postId);
            jobNameTxt.setText(post.jobName);
            requiredYearOfStudyTxt.setText(Integer.toString(post.reqStudyYear));
            requirementsTxt.setText(post.requirements);
            locationTxt.setText(post.location);
            descriptionTxt.setText(post.desc);
            emailTxt.setText(post.email);
            phoneNumberTxt.setText(post.phone);

            if (Objects.equals(post.onsiteOnline, "onsite")) {
                spinnerOption.setSelection(0);
                locationTxtRow.setVisibility(View.VISIBLE);
            } else { // online
                spinnerOption.setSelection(1);
                locationTxtRow.setVisibility(View.GONE);
            }
        }).start();

        savePostBtn.setOnClickListener(view1 -> {
            String jobName = jobNameTxt.getText().toString();
            String requiredYearOfStudy = requiredYearOfStudyTxt.getText().toString();
            String requirements = requirementsTxt.getText().toString();
            int onsiteOnline = spinnerOption.getSelectedItemPosition();
            String location = locationTxt.getText().toString();
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
                int reqYearOfStudy =  Integer.parseInt(requiredYearOfStudy);
                Post post = appDatabase.postDao().getPostById(postId);

                post.jobName = jobName;
                post.reqStudyYear = reqYearOfStudy;
                post.requirements = requirements;
                post.desc = description;
                post.email = email;
                post.phone = phoneNumber;

                if (onsiteOnline == 0) { // onsite
                    post.onsiteOnline = "onsite";
                    post.location = location;
                } else { // online
                    post.onsiteOnline = "online";
                    post.location = "online";
                }
                // update post
                appDatabase.postDao().updatePost(post);

                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Uspješno ažuriran oglas!", Toast.LENGTH_SHORT).show());
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PostsDisplayEmployer()).commit();
            }).start();
        });
    }
}