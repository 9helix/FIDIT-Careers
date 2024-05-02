package hr.uniri.fiditcareers;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
@Dao
public interface StudentDao {
    @Insert
    void insert(Student student);
    @Query("SELECT * FROM Student")
    List<Student> getAll();

    @Query("SELECT * FROM Student WHERE `E-mail` = :email")
    Student getStudentByEmail(String email);
}

