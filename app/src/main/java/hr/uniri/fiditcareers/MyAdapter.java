package hr.uniri.fiditcareers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private String type;
    private List<Post> dataList;

    public void setSearchList(List<Post> dataSearchList){
        this.dataList = dataSearchList;
        notifyDataSetChanged();
    }

    // constructor
    public MyAdapter(Context context, List<Post> dataList){
        this.context = context;
        // get the type of user from global variable
        type = ((GlobalVariable) context.getApplicationContext()).getType();
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.internship_post, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // writing data into text views from recycler item
        holder.internshipName.setText(dataList.get(position).jobName);
        holder.internshipDate.setText(dataList.get(position).datePosted);
        holder.internshipEmployer.setText(dataList.get(position).employerName);
        holder.internshipOnsiteOnline.setText(dataList.get(position).onsiteOnline);
        holder.internshipLocation.setText(dataList.get(position).location);
        holder.internshipReqYearOfStudy.setText(Integer.toString(dataList.get(position).reqStudyYear));
        holder.internshipId.setText(Integer.toString(dataList.get(position).id));

        // get id for each post
        int postId = dataList.get(holder.getAdapterPosition()).id;
        if (type.equals("student")) { // display of posts for students
            holder.appliedStudentsButton.setVisibility(View.GONE);
            holder.internshipEdit.setVisibility(View.GONE);
            holder.internshipDelete.setVisibility(View.GONE);
            holder.internshipEmployer.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            // when post is clicked
            holder.recCard.setOnClickListener(view -> {
                // send id of selected post to the fragment with details about the selected post
                DetailPost fragment = DetailPost.newInstance(postId);
                ((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction().replace(R.id.fragment_container, fragment).commit();
            });
        } else { // display of posts for employers
            holder.appliedStudentsButton.setVisibility(View.VISIBLE);
            holder.internshipEdit.setVisibility(View.VISIBLE);
            holder.internshipDelete.setVisibility(View.VISIBLE);
            holder.internshipEmployer.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

            // when edit icon is clicked
            holder.internshipEdit.setOnClickListener(view -> {
                EditPost fragment = EditPost.newInstance(postId);
                ((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction().replace(R.id.fragment_container, fragment).commit();
            });

            // when trash icon is clicked
            holder.internshipDelete.setOnClickListener(view -> {
                DeletePost dialogFragment = DeletePost.newInstance(postId);
                dialogFragment.show(((FragmentActivity)context).getSupportFragmentManager(),"Obriši oglas");
            });
        }

        Button appliedStudentsButton = holder.appliedStudentsButton;
        appliedStudentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    //create appdatabase
                    AppDatabase appDatabase = Room.databaseBuilder(context,
                            AppDatabase.class, "app-db").build();
                    // Retrieve the current post
                    Post post = appDatabase.postDao().getPostById(postId);

                    // Get the list of applied student emails from the post
                    List<String> appliedStudentEmails = post.getAppliedStudentIdsList();

                    List<String> appliedStudentData= new ArrayList<>();
                    for (String email : appliedStudentEmails) {
                        Student student=appDatabase.studentDao().getStudentByEmail(email);
                        appliedStudentData.add(student.name+" "+student.surname+" - "+email);
                    }
                    // Convert the list to a string with each email separated by a newline
                    String appliedStudentEmailsString = String.join("\n", appliedStudentData);

                    // Display the emails in a dialog on the main thread
                    ((FragmentActivity)context).runOnUiThread(() -> {
                        new AlertDialog.Builder(context)
                                .setTitle("Prijavljeni studenti")
                                .setMessage(appliedStudentEmailsString)
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                    });
                }).start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{
    TextView internshipName, internshipDate, internshipEmployer, internshipId,
            internshipOnsiteOnline, internshipLocation, internshipReqYearOfStudy;
    ImageView internshipEdit, internshipDelete;
    CardView recCard;
    Button appliedStudentsButton;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        // initializing elements from recycle item
        internshipName = itemView.findViewById(R.id.internshipName);
        internshipDate = itemView.findViewById(R.id.internshipDate);
        internshipEmployer = itemView.findViewById(R.id.internshipEmployer);
        internshipOnsiteOnline = itemView.findViewById(R.id.internshipOnsiteOnline);
        internshipLocation = itemView.findViewById(R.id.internshipLocation);
        internshipReqYearOfStudy = itemView.findViewById(R.id.internshipReqYearOfStudy);
        internshipId = itemView.findViewById(R.id.internshipId);
        internshipEdit = itemView.findViewById(R.id.internshipEdit);
        internshipDelete = itemView.findViewById(R.id.internshipDelete);
        recCard = itemView.findViewById(R.id.recCard);
        appliedStudentsButton = itemView.findViewById(R.id.appliedStudentsButton);
    }
}