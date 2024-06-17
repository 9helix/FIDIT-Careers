package hr.uniri.fiditcareers;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
                AtomicReference<SharedPreferences> sharedPreferences = new AtomicReference<>(getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE));
                boolean isLoggedIn = sharedPreferences.get().getBoolean("isLoggedIn", false);
                String savedEmail = sharedPreferences.get().getString("email", ""); // retrieve email
                if (isLoggedIn) {
                    if (savedEmail.equals(deletionEmail)) {
                        SharedPreferences.Editor myEdit = sharedPreferences.get().edit();
                        myEdit.remove("isLoggedIn");
                        myEdit.remove("email");
                        myEdit.remove("type");
                        myEdit.apply();
                    }
                }
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Uspješno obrisan korisnički račun!", Toast.LENGTH_SHORT).show());

                if (type.equals("student")) { // deletes student from database
                    appDatabase.studentDao().deleteStudentByEmail(deletionEmail);
                    new Thread(() -> {
                        // Retrieve all posts
                        List<Post> allPosts = appDatabase.postDao().getAll();

                        for (Post post : allPosts) {
                            // Get the list of applied student emails
                            List<String> appliedStudentIds = post.getAppliedStudentIdsList();

                            // Check if deletionEmail is in the list
                            if (appliedStudentIds.contains(deletionEmail)) {
                                // If it is, remove it
                                appliedStudentIds.remove(deletionEmail);

                                // Set the updated list back to the post
                                post.setAppliedStudentIdsList(appliedStudentIds);

                                // Update the post in the database
                                appDatabase.postDao().updatePost(post);
                            }
                        }
                    }).start();
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