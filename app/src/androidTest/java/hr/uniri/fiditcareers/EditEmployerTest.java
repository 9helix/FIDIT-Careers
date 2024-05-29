package hr.uniri.fiditcareers;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EditEmployerTest {
    private AppDatabase appDatabase;
    private PostDao postDao;
    private EmployerDao employerDao;

    @Before
    public void createDB() {
        // access app context in test env
        Context context = ApplicationProvider.getApplicationContext();
        // creates in-memory database
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        postDao = appDatabase.postDao();
        employerDao = appDatabase.employerDao();
    }

    @Test
    public void CreatePost() throws Exception {
        // firstly create employer
        Employer employer = new Employer();
        int employerID = 1;

        employer.id = employerID;
        employer.employerName = "RIS d.o.o.";
        employer.password = "lozinka";
        employerDao.insert(employer);

        // update info of created employer
        Employer insertedEmployer = employerDao.getEmployerById(employerID);
        insertedEmployer.employerName = "Prospekt d.o.o.";
        insertedEmployer.email = "prospekt@mail.com";
        insertedEmployer.password = "prospekt";
        employerDao.updateEmployer(insertedEmployer);

        assertEquals(employerID, insertedEmployer.id);
    }

    @After
    public void closeDB() {
        appDatabase.close();
    }
}
