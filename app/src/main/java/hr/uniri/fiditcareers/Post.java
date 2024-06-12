package hr.uniri.fiditcareers;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @ColumnInfo(name = "Onsite Or Online")
    public String onsiteOnline;

    @ColumnInfo(name = "Location")
    public String location;

    @ColumnInfo(name = "Description")
    public String desc;

    @ColumnInfo(name = "Date Posted")
    public String datePosted;

    @ColumnInfo(name = "E-mail")
    public String email;

    @ColumnInfo(name = "Phone Number")
    public String phone;

    @ColumnInfo(name = "Employer Id")
    public int employerId;

    @ColumnInfo(name = "Employer Name")
    public String employerName;


    @ColumnInfo(name = "Applied Student Emails")
    public String appliedStudentIds;

    public List<String> getAppliedStudentIdsList() {
        if (appliedStudentIds == null || appliedStudentIds.isEmpty()) {
            return new ArrayList<String>();
        }
        return Stream.of(appliedStudentIds.split(","))
                .collect(Collectors.toList());
    }

    public void setAppliedStudentIdsList(List<String> studentIds) {
        this.appliedStudentIds = String.join(",", studentIds);
    }
}

