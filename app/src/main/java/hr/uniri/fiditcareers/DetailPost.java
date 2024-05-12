package hr.uniri.fiditcareers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailPost extends Fragment {
    int postId;
    private AppDatabase appDatabase;

    public DetailPost() {
        // Required empty public constructor
    }

    public static DetailPost newInstance(int id) {
        DetailPost fragment = new DetailPost();
        Bundle args = new Bundle();
        args.putInt("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postId = getArguments().getInt("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentHolder = inflater.inflate(R.layout.fragment_detail_post, container, false);
        appDatabase = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "app-db").build();

        TextView detailJobName = parentHolder.findViewById(R.id.detailJobName);
        TextView detailEmployer = parentHolder.findViewById(R.id.detailEmployer);
        TextView detailDate = parentHolder.findViewById(R.id.detailDate);
        TextView detailOnsiteOnline = parentHolder.findViewById(R.id.detailOnsiteOnline);
        TextView detailLocation = parentHolder.findViewById(R.id.detailLocation);
        TextView detailRequirements = parentHolder.findViewById(R.id.detailRequirements);
        TextView detailReqYearOfStudy = parentHolder.findViewById(R.id.detailReqYearOfStudy);
        TextView detailEmail = parentHolder.findViewById(R.id.detailEmail);
        TextView detailPhone = parentHolder.findViewById(R.id.detailPhone);
        TextView detailDesc = parentHolder.findViewById(R.id.detailDesc);

        new Thread(() -> {
            Post post = appDatabase.postDao().getPostById(postId);
            detailJobName.setText(post.jobName);
            detailEmployer.setText(post.employerName);
            detailDate.setText(post.datePosted);
            detailOnsiteOnline.setText(post.onsiteOnline);
            detailLocation.setText(post.location);
            detailRequirements.setText(post.requirements);
            detailReqYearOfStudy.setText(Integer.toString(post.reqStudyYear));
            detailEmail.setText(post.email);
            detailPhone.setText(post.phone);
            detailDesc.setText(post.desc);
        }).start();
        return parentHolder;
    }
}