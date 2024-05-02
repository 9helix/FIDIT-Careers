package hr.uniri.fiditcareers;
import androidx.room.Database;
import androidx.room.RoomDatabase;
@Database(entities = {Student.class,Employer.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract StudentDao studentDao();
    public abstract EmployerDao employerDao();
}

