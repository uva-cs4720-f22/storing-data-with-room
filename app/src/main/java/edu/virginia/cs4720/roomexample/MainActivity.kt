package edu.virginia.cs4720.roomexample

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import androidx.room.*
import kotlinx.coroutines.launch

/**************
 * Tutorials reference:
 * https://developer.android.com/codelabs/android-room-with-a-view-kotlin#0
 * https://developer.android.com/training/data-storage/room/accessing-data
 * https://johncodeos.com/how-to-use-room-in-android-using-kotlin/
 * https://howtodoandroid.com/room-database-android/
 * https://medium.com/huawei-developers/room-database-with-kotlin-mvvm-architecture-477c3ad3c264
 *
 * Make sure to check out the additions in the build.gradle file!
 */

class MainActivity : AppCompatActivity() {

    // Get a conneciton to the database.  The call of "by lazy" makes it so that
    // initializing the database doesn't have to hinder the app's normal loading
    // and it only gets allocated if needed.  Hence lazy initialization.
    private val appDatabase by lazy { AppRoomDatabase.getDatabase(this).studentDao()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Creating an example student object
        val student1 = Student("mss2x", "Mark")

    }

    fun printDB(view: View) {
        Log.i("RoomAppExample", appDatabase.getStudentListByComputingID().toString())
    }

    fun addStudent(view: View) {
        val compID = findViewById<EditText>(R.id.editTextCompId)
        val name = findViewById<EditText>(R.id.editTextName)

        // lifecycleScope.launch allows me to call a method that has the suspend keyword
        // which is a coroutine.  Basically, it's kind of like threading, but within
        // the same thread, and allow some forms of concurrency.  In this case,
        // we don't want an insert to hold up other things happening.
        lifecycleScope.launch {
            appDatabase.insert(Student(compID.text.toString(), name.text.toString()))
        }

        Log.i("RoomAppExample", compID.text.toString() + " added!")
    }

}


// By adding the @Entity annotation here, we are saying that this class should
// be a table in the database
@Entity(tableName = "student_table")
data class Student(@PrimaryKey val computingID: String, val name: String)

// This defines the Data Access Object for the data class above.
// Effectively it allows you to connect an SQL query to a function call
@Dao
interface StudentDao {

    @Query("Select * from student_table order by computingID ASC")
    fun getStudentListByComputingID(): List<Student>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(student: Student)

    @Query("Delete from student_table")
    suspend fun deleteAll()
}