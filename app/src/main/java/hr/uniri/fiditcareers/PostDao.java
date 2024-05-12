package hr.uniri.fiditcareers;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface PostDao {
    @Insert
    void insert(Post post);

    @Query("SELECT * FROM Post")
    List<Post> getAll();

    @Query("SELECT * FROM Post WHERE `Employer Id` = :id")
    List<Post> getPostsByEmployerId(int id);

    @Query("SELECT * FROM Post WHERE id = :id")
    Post getPostById(int id);

    @Query("DELETE FROM Post WHERE id = :id")
    void deletePostById(int id);

    @Query("UPDATE Post SET `Employer Name` = :employerName WHERE `Employer Id` = :id")
    void updateEmployersOnPost(String employerName, int id);

    @Update
    void updatePost(Post post);
}

