package edu.virginia.cs4720.roomexample

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * This class represents the database itself.  It identifies the Data Access Object
 * (DAO) that can be used to read and write data.  It also initializes an empty
 * database if needed.  Notice the call to .allowMainThreadQueries() in the
 * creation of the database.  By default, all db queries in Android have to be
 * done OFF of the main thread to prevent the app hanging, etc.  For testing
 * purposes (and because I didn't want to deal with launching AsyncTasks),
 * adding this call overrides that requirement.
 */

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Student::class), version = 1, exportSchema = false)
public abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun studentDao(): StudentDao

    // Singleton prevents multiple instances of database opening at the
    // same time.

    companion object {
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getDatabase(context: Context): AppRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    "app_database"
                )
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }


    }
}