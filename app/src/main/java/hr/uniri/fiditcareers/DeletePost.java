package hr.uniri.fiditcareers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DeletePost extends DialogFragment {

    int postId;
    private AppDatabase appDatabase;

    public DeletePost() {
        // Required empty public constructor
    }

    public static DeletePost newInstance(int id) {
        DeletePost fragment = new DeletePost();
        Bundle args = new Bundle();
        // store selected post's ID as argument
        args.putInt("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // get stored post's ID
            postId = getArguments().getInt("id");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_delete_post,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        appDatabase = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "app-db").build();
        TextView yesBtn = getView().findViewById(R.id.yesBtn);
        TextView noBtn = getView().findViewById(R.id.noBtn);

        yesBtn.setOnClickListener(view1 -> {
            new Thread(() -> {
                appDatabase.postDao().deletePostById(postId);
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Uspješno obrisan oglas!", Toast.LENGTH_SHORT).show());
                // refresh employer's dashboard
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PostsDisplayEmployer()).commit();
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