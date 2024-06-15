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
import android.widget.Toast;

import java.util.HashMap;

public class StudentEdit extends Fragment {
    public StudentEdit() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        DashboardStudent dashboardStudent = (DashboardStudent) getActivity();
        assert dashboardStudent != null;
        HashMap<String, Object> studentData = dashboardStudent.studentData;
        int id = (Integer) studentData.get("id");

        String name = (String) studentData.get("name");
        EditText editNameText = getView().findViewById(R.id.editNameText);
        editNameText.setText(name);

        String surname = (String) studentData.get("surname");
        EditText editSurnameTxt = getView().findViewById(R.id.editSurnameTxt);
        editSurnameTxt.setText(surname);

        int year = (Integer) studentData.get("year");
        EditText editStudyYearTxt = getView().findViewById(R.id.editStudyYearTxt);
        editStudyYearTxt.setText(Integer.toString(year));

        String email = (String) studentData.get("email");
        EditText editEmailTxt = getView().findViewById(R.id.editEmailTxt);
        editEmailTxt.setText(email);

        String pass = (String) studentData.get("pass");
        EditText editPassTxt = getView().findViewById(R.id.editPassTxt);
        editPassTxt.setText(pass);

        String about = (String) studentData.get("about");
        EditText editAboutTxt = getView().findViewById(R.id.editAboutTxt);
        editAboutTxt.setText(about);

        EditText editPassConfTxt = getView().findViewById(R.id.editPassConfTxt);
        editPassConfTxt.setText(pass);

        Button saveButton = getView().findViewById(R.id.saveBtn);
        saveButton.setOnClickListener(view1 -> {
            String newName = editNameText.getText().toString();
            String newSurname = editSurnameTxt.getText().toString();
            String studyYear = editStudyYearTxt.getText().toString();
            String newEmail = editEmailTxt.getText().toString();
            String newPass = editPassTxt.getText().toString();
            String newAbout = editAboutTxt.getText().toString();

            if(newName.isEmpty()) {
                editNameText.setError("Unesite ime.");
                return;
            }
            if(newSurname.isEmpty()) {
                editSurnameTxt.setError("Unesite prezime.");
                return;
            }
            if (studyYear.isEmpty()) {
                editStudyYearTxt.setError("Unesite godinu studija.");
                return;
            } else {
                int yearOfStudyInt =  Integer.parseInt(studyYear);

                if(yearOfStudyInt < 1 || yearOfStudyInt > 5) {
                    editStudyYearTxt.setError("Godina studija mora biti u intervalu od 1 do 5.");
                    return;
                }
            }
            if(newEmail.isEmpty()) {
                editEmailTxt.setError("Unesite e-mail.");
                return;
            }
            if(newPass.isEmpty()) {
                editPassTxt.setError("Unesite lozinku.");
                return;
            }
            if(newPass.length()<4) {
                editPassTxt.setError("Lozinka mora biti duga bar 4 znaka.");
                return;
            }
            if(!newPass.equals(editPassConfTxt.getText().toString())) {
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Lozinke se ne podudaraju.", Toast.LENGTH_SHORT).show());
                return;
            }

            int newYear = Integer.parseInt(studyYear);
            new Thread(() -> {
                AppDatabase appDatabase = dashboardStudent.appDatabase;
                Student student = appDatabase.studentDao().getStudentById(id);

                student.email = newEmail;
                student.name = newName;
                student.surname = newSurname;
                student.studyYear = newYear;
                student.aboutMe = newAbout;
                appDatabase.studentDao().updateStudent(student);

                ((GlobalVariable) getActivity().getApplication()).setEmail(newEmail);
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getActivity(), "Informacije ažurirane!", Toast.LENGTH_SHORT).show();
                });
            }).start();

            studentData.put("name", newName);
            studentData.put("surname", newSurname);
            studentData.put("year", newYear);
            studentData.put("email", newEmail);
            studentData.put("pass", newPass);
            studentData.put("about", newAbout);

            Intent in = new Intent(getActivity(), DashboardStudent.class);
            startActivity(in);
        });
    }
}