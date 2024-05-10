package hr.uniri.fiditcareers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<Post> dataList;
    public void setSearchList(List<Post> dataSearchList){
        this.dataList = dataSearchList;
        notifyDataSetChanged();
    }
    public MyAdapter(Context context, List<Post> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_student, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.internshipName.setText(dataList.get(position).jobName);
        holder.internshipDate.setText(dataList.get(position).datePosted);
        holder.internshipEmployer.setText(dataList.get(position).employerName);
        holder.internshipOnsiteOnline.setText(dataList.get(position).onsiteOnline);
        holder.internshipLocation.setText(dataList.get(position).location);
        holder.internshipReqYearOfStudy.setText(Integer.toString(dataList.get(position).reqStudyYear));
        holder.internshipId.setText(Integer.toString(dataList.get(position).id));

        holder.recCard.setOnClickListener(view -> {
            // send id of selected post to the fragment with details about the selected post
            DetailPost fragment = DetailPost.newInstance(dataList.get(holder.getAdapterPosition()).id);
            ((FragmentActivity)context).getSupportFragmentManager()
                    .beginTransaction().replace(R.id.fragment_container, fragment).commit();
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
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        internshipName = itemView.findViewById(R.id.internshipName);
        internshipDate = itemView.findViewById(R.id.internshipDate);
        internshipEmployer = itemView.findViewById(R.id.internshipEmployer);
        internshipOnsiteOnline = itemView.findViewById(R.id.internshipOnsiteOnline);
        internshipLocation = itemView.findViewById(R.id.internshipLocation);
        internshipReqYearOfStudy = itemView.findViewById(R.id.internshipReqYearOfStudy);
        internshipId = itemView.findViewById(R.id.internshipId);
        recCard = itemView.findViewById(R.id.recCard);
    }
}
