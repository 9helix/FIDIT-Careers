package hr.uniri.fiditcareers;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentEditFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private AppDatabase appDatabase;

    public StudentEditFragment() {
        // Required empty public constructor
    }


    public static StudentEditFragment newInstance(String studentEmail) {
        StudentEditFragment fragment = new StudentEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, studentEmail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String studentEmail = getArguments().getString(ARG_PARAM1);
            Log.d("StudentEditFragment", "Student email: " + studentEmail);
        }
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

        //String idStr= (String) ;
        int id = (Integer) studentData.get("id");
        TextView studentId = getView().findViewById(R.id.studentIdTxt);
        studentId.setText(getString(R.string.account_id, id));

        String name = (String) studentData.get("name");
        EditText nameEditText = getView().findViewById(R.id.editNameText);
        nameEditText.setText(name);

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
            String newName = nameEditText.getText().toString();
            String newSurname = editSurnameTxt.getText().toString();
            int newYear = Integer.parseInt(editStudyYearTxt.getText().toString());
            String newEmail = editEmailTxt.getText().toString();
            String newPass = editPassTxt.getText().toString();
            String newAbout = editAboutTxt.getText().toString();
            if(!newPass.equals(editPassConfTxt.getText().toString())) {
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Lozinke se ne podudaraju.", Toast.LENGTH_SHORT).show());
                return;
            }
            new Thread(() -> {
                AppDatabase appDatabase = dashboardStudent.appDatabase;
                Student student = appDatabase.studentDao().getStudentById(id);
                student.email = newEmail;
                student.name = newName;
                student.surname = newSurname;
                student.studyYear = newYear;
                appDatabase.studentDao().updateStudent(student);
                ((PublicVariable) getActivity().getApplication()).setEmail(newEmail);
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

            Log.d("StudentEditFragment", "Student data: " + studentData);
            Intent in = new Intent(getActivity(),DashboardStudent.class);
            startActivity(in);

        });
    }
}