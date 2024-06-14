package hr.uniri.fiditcareers;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteAccount extends DialogFragment {

    String deletionEmail, type;
    private AppDatabase appDatabase;

    public DeleteAccount() {
        // Required empty public constructor
    }

    public static DeleteAccount newInstance(String email, String type) {
        DeleteAccount fragment = new DeleteAccount();
        Bundle args = new Bundle();

        args.putString("email", email);
        args.putString("type", type);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // get stored post's ID
            deletionEmail = getArguments().getString("email");
            type = getArguments().getString("type");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_delete_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        appDatabase = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "app-db").build();
        TextView yesBtn = getView().findViewById(R.id.yesBtn);
        TextView noBtn = getView().findViewById(R.id.noBtn);

        yesBtn.setOnClickListener(view1 -> {
            new Thread(() -> {
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Uspješno obrisan korisnički račun!", Toast.LENGTH_SHORT).show());

                if (type.equals("student")) { // deletes student from database
                    appDatabase.studentDao().deleteStudentByEmail(deletionEmail);

                    // redirect user to student login screen
                    Intent i = new Intent(getActivity(), StudentLogin.class);
                    startActivity(i);
                } else {
                    Employer employer = appDatabase.employerDao().getEmployerByEmail(deletionEmail);
                    // deletes all posts from employer
                    appDatabase.postDao().deletePostsByEmployerId(employer.id);
                    // deletes employer from database
                    appDatabase.employerDao().deleteEmployer(employer);

                    // redirect user to employer login screen
                    Intent i = new Intent(getActivity(), EmployerLogin.class);
                    startActivity(i);
                }
            }).start();
            onDestroyView();
        });
        noBtn.setOnClickListener(view1 -> {
            onDestroyView();
        });
    }

    // closes pop-up for delete confirmation
    public void onDestroyView() {
        super.onDestroyView();
    }
}