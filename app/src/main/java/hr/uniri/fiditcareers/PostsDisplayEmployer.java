package hr.uniri.fiditcareers;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PostsDisplayEmployer extends Fragment {
    private AppDatabase appDatabase;
    RecyclerView recyclerView;
    MyAdapter adapter;

    public PostsDisplayEmployer() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts_display_employer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab = getView().findViewById(R.id.fab);
        // when floating action button is clicked
        fab.setOnClickListener(view1 -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddPost()).commit();
        });

        TextView noPostsMessage = getView().findViewById(R.id.noPostsText);
        recyclerView = getView().findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        appDatabase = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "app-db").build();

        // get stored email from global variable
        String employerEmail = ((GlobalVariable) getActivity().getApplication()).getEmail();
        new Thread(() -> {
            Employer employer = appDatabase.employerDao().getEmployerByEmail(employerEmail);
            List<Post> posts = appDatabase.postDao().getPostsByEmployerId(employer.id);

            getActivity().runOnUiThread(() -> {
                // if there are no posts, display appropriate message
                if (posts.isEmpty()) {
                    noPostsMessage.setVisibility(View.VISIBLE);
                } else {
                    adapter = new MyAdapter(getActivity(), posts);
                    recyclerView.setAdapter(adapter);
                    noPostsMessage.setVisibility(View.GONE);
                }
            });
        }).start();
    }
}