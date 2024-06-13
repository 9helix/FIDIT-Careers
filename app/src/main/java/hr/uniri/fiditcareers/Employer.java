package hr.uniri.fiditcareers;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Employer {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "Employer Name")
    public String employerName;

    @ColumnInfo(name = "E-mail")
    public String email;

    @ColumnInfo(name = "Password")
    public String password;
}
