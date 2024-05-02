package hr.uniri.fiditcareers;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;
import java.util.List;

@Entity(foreignKeys = @ForeignKey(entity = Employer.class,
        parentColumns = "id",
        childColumns = "Employer Id"))
public class Post {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "Job Name")
    public String jobName;
    @ColumnInfo(name = "Required Year of Study")
    public int reqStudyYear;
    @ColumnInfo(name = "Requirements")
    public String requirements;
    @ColumnInfo(name = "Description")
    public String desc;
    @ColumnInfo(name = "Date Posted")
    public String datePosted;
    @ColumnInfo(name = "Employer Id")
    public int employerId;
}

