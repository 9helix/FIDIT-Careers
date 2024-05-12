package hr.uniri.fiditcareers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PostsDisplayEmployer extends Fragment {
    private AppDatabase appDatabase;
    RecyclerView recyclerView;
    MyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentHolder = inflater.inflate(R.layout.fragment_posts_display_employer, container, false);
        
        FloatingActionButton fab = parentHolder.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddPost()).commit();
        });

        recyclerView = parentHolder.findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        appDatabase = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "app-db").build();
        
        new Thread(() -> {
            String employerEmail = ((GlobalVariable) getActivity().getApplication()).getEmail();
            Employer employer = appDatabase.employerDao().getEmployerByEmail(employerEmail);

            List<Post> posts = appDatabase.postDao().getPostsByEmployerId(employer.id);
            adapter = new MyAdapter(getActivity(), posts);
            recyclerView.setAdapter(adapter);
        }).start();

        return parentHolder;
    }
}