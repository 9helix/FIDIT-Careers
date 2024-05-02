package hr.uniri.fiditcareers;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Student {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "Name")
    public String name;
    @ColumnInfo(name = "Surname")
    public String surname;
    @ColumnInfo(name = "Year of Study")
    public int studyYear;
    @ColumnInfo(name = "E-mail")
    public String email;
    @ColumnInfo(name = "Password")
    public String password;
}

