package hr.uniri.fiditcareers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PostsDisplayEmployer extends Fragment {
    private AppDatabase appDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentHolder = inflater.inflate(R.layout.fragment_posts_display_employer, container, false);
        appDatabase = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "app-db").build();

        FloatingActionButton fab = parentHolder.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddPost()).commit();
        });

        new Thread(() -> {
            List<Post> oglasi = appDatabase.postDao().getAll();
            oglasi.forEach((n) -> printOglasi(n));
        }).start();

        return parentHolder;
    }

    public void printOglasi(Post x) {
        String s = x.id + "\nNaziv posla: " + x.jobName
                + "\nminim. godina studija: " + x.reqStudyYear
                + "\nzahtjevi: " + x.requirements
                + "\nopis: " + x.desc
                + "\ne-mail: " + x.email
                + "\nopcija: " + x.onsiteOnline
                + "\nlokacija: " + x.location
                + "\ntelefon. broj: " + x.phone
                + "\nID zaposlenika: " + x.employerId
                + "\nZaposlenik: " + x.employerName
                + "\ndatum objave: " + x.datePosted;
        Log.d("PostsDisplayEmployer", s);
    }
}