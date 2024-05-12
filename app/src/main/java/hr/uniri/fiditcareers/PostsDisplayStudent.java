package hr.uniri.fiditcareers;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class PostsDisplayStudent extends Fragment {
    private AppDatabase appDatabase;
    RecyclerView recyclerView;
    MyAdapter adapter;
    SearchView searchView;
    List<Post> posts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentHolder = inflater.inflate(R.layout.fragment_posts_display_student, container, false);

        recyclerView = parentHolder.findViewById(R.id.recyclerView);
        searchView = parentHolder.findViewById(R.id.search);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        appDatabase = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "app-db").build();

        new Thread(() -> {
            posts = appDatabase.postDao().getAll();
            adapter = new MyAdapter(getActivity(), posts);
            recyclerView.setAdapter(adapter);
        }).start();

        return parentHolder;
    }

    private void searchList(String text){
        List<Post> dataSearchList = new ArrayList<>();
        for (Post data : posts){
            if (data.jobName.toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(data);
            }
        }
        if (!(dataSearchList.isEmpty())){
            adapter.setSearchList(dataSearchList);
        }
    }
}