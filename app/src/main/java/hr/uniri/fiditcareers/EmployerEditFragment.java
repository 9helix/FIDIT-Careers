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
 * Use the {@link EmployerEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployerEditFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private AppDatabase appDatabase;

    public EmployerEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment EmployerEditFragment.
     */

    public static EmployerEditFragment newInstance(String param1) {
        EmployerEditFragment fragment = new EmployerEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String employerEmail = getArguments().getString(ARG_PARAM1);
            Log.d("EmployerEditFragment", "Employer email: " + employerEmail);
        }
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
        TextView employerId = getView().findViewById(R.id.employerIdText);
        employerId.setText(getString(R.string.account_id, id));

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
            if(!newPass.equals(editPassConfTxt.getText().toString())) {
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Lozinke se ne podudaraju.", Toast.LENGTH_SHORT).show());
                return;
            }
            new Thread(() -> {
                AppDatabase appDatabase = dashboardEmployer.appDatabase;
                Student student = appDatabase.studentDao().getStudentById(id);
                student.email = newEmail;
                student.name = newName;
                appDatabase.studentDao().updateStudent(student);
                ((PublicVariable) getActivity().getApplication()).setEmail(newEmail);
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getActivity(), "Informacije ažurirane!", Toast.LENGTH_SHORT).show();
                });
            }).start();

            employerData.put("name", newName);
            employerData.put("email", newEmail);
            employerData.put("pass", newPass);

            Log.d("StudentEditFragment", "Student data: " + employerData);
            Intent in = new Intent(getActivity(),DashboardStudent.class);
            startActivity(in);

        });
    }
}