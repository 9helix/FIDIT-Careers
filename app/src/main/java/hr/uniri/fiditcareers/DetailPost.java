package hr.uniri.fiditcareers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
                AppDatabase.class, "app-db").allowMainThreadQueries().build();
        Button backButton = parentHolder.findViewById(R.id.backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostsDisplayStudent homeFragment = new PostsDisplayStudent();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();

            }
        });

        Button applyButton = parentHolder.findViewById(R.id.applyButton);
        Button appliedButton = parentHolder.findViewById(R.id.appliedButton);

        new Thread(() -> {
            // Retrieve the current student's email
            String currentStudentEmail = ((GlobalVariable) getActivity().getApplication()).getEmail();

            // Retrieve the current post
            Post post = appDatabase.postDao().getPostById(postId);

            // Get the list of applied student emails from the post
            List<String> appliedStudentIds = post.getAppliedStudentIdsList();
            applyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(appliedStudentIds == null){
                        //List<String> l1 = new ArrayList<String>();
                        appliedStudentIds.add(currentStudentEmail);

                        post.setAppliedStudentIdsList(appliedStudentIds);
                    }
                    else{
                        appliedStudentIds.add(currentStudentEmail);
                        post.setAppliedStudentIdsList(appliedStudentIds);}

                    // Update the post in the database
                    appDatabase.postDao().updatePost(post);
                    applyButton.setVisibility(View.GONE);
                    appliedButton.setVisibility(View.VISIBLE);
                    Log.d("appliedStudents apply", post.appliedStudentIds);
                }
            });
            appliedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(() -> {
                        // Retrieve the current student's email
                        String currentStudentEmail = ((GlobalVariable) getActivity().getApplication()).getEmail();

                        // Retrieve the current post
                        Post post = appDatabase.postDao().getPostById(postId);


                        // Remove the current student's email from the list
                        appliedStudentIds.remove(currentStudentEmail);
                        post.setAppliedStudentIdsList(appliedStudentIds);

                        // Update the post in the database
                        appDatabase.postDao().updatePost(post);

                        // Update the UI on the main thread
                        getActivity().runOnUiThread(() -> {
                            appliedButton.setVisibility(View.GONE);
                            applyButton.setVisibility(View.VISIBLE);
                        });
                        Log.d("appliedStudents remove", post.appliedStudentIds);
                    }).start();

                }
});
            // Check if the list contains the current student's email
            if (appliedStudentIds!=null && appliedStudentIds.contains(currentStudentEmail)) {
                // If the student has already applied, disable the apply button or change its text
                getActivity().runOnUiThread(() -> {
                    applyButton.setVisibility(View.GONE);
                    appliedButton.setVisibility(View.VISIBLE);
                });
            } else {
                applyButton.setVisibility(View.VISIBLE);

            }}).start();



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