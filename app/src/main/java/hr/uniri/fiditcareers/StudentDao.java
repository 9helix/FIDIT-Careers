package hr.uniri.fiditcareers;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.Update;

import java.util.List;
@Dao
public interface StudentDao {
    @Insert
    void insert(Student student);

    @Query("SELECT * FROM Student WHERE `E-mail` = :email")
    Student getStudentByEmail(String email);

    @Query("SELECT * FROM Student WHERE id = :id")
    Student getStudentById(int id);

    @Update
    void updateStudent(Student student);
}

