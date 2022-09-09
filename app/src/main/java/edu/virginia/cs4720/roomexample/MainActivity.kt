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
 *
 */

class MainActivity : AppCompatActivity() {

    private val appDatabase by lazy { AppRoomDatabase.getDatabase(this).studentDao()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val student1 = Student("mss2x", "Mark")

//        Log.i("RoomAppExample", appDatabase.getStudentListByComputingID().toString())
//
//        lifecycleScope.launch {
//            appDatabase.insert(student1)
//        }
//
//        Log.i("RoomAppExample", appDatabase.getStudentListByComputingID().toString())
    }

    fun printDB(view: View) {
        Log.i("RoomAppExample", appDatabase.getStudentListByComputingID().toString())
    }

    fun addStudent(view: View) {
        val compID = findViewById<EditText>(R.id.editTextCompId)
        val name = findViewById<EditText>(R.id.editTextName)

        lifecycleScope.launch {
            appDatabase.insert(Student(compID.text.toString(), name.text.toString()))
        }

        Log.i("RoomAppExample", compID.text.toString() + " added!")
    }

}



@Entity(tableName = "student_table")
data class Student(@PrimaryKey val computingID: String, val name: String)

@Dao
interface StudentDao {

    @Query("Select * from student_table order by computingID ASC")
    fun getStudentListByComputingID(): List<Student>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(student: Student)

    @Query("Delete from student_table")
    suspend fun deleteAll()
}