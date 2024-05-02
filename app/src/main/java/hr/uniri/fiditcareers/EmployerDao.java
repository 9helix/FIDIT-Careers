package hr.uniri.fiditcareers;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
@Dao
public interface EmployerDao {
    @Insert
    void insert(Employer employer);
    @Query("SELECT * FROM Employer")
    List<Employer> getAll();
    @Query("SELECT * FROM Employer WHERE `E-mail` = :email")
    Employer getEmployerByEmail(String email);

    @Query("DELETE FROM Employer WHERE `E-mail` = :email")
    void deleteEmployerByEmail(String email);

}
