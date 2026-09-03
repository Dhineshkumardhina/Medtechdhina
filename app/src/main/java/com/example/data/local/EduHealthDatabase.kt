package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
  entities = [
    StudentEntity::class,
    ScreeningRecordEntity::class,
    DoctorReportEntity::class
  ],
  version = 1,
  exportSchema = false
)
@TypeConverters(HealthTypeConverters::class)
abstract class EduHealthDatabase : RoomDatabase() {

  abstract fun studentDao(): StudentDao
  abstract fun healthRecordDao(): HealthRecordDao
  abstract fun doctorReportDao(): DoctorReportDao

  fun screeningDao(): HealthRecordDao = healthRecordDao()

  companion object {
    @Volatile
    private var INSTANCE: EduHealthDatabase? = null

    fun getDatabase(context: Context): EduHealthDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          EduHealthDatabase::class.java,
          "eduhealth_local.db"
        )
          .fallbackToDestructiveMigration()
          .build()
        INSTANCE = instance
        instance
      }
    }
  }
}
