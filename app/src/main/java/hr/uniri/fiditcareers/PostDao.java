package hr.uniri.fiditcareers;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
@Dao
public interface PostDao {
    @Insert
    void insert(Post post);
    @Query("SELECT * FROM Post")
    List<Post> getAll();

}

