package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserAccountEntity::class,
        InstituteRecordEntity::class,
        NoteRecordEntity::class,
        JobRecordEntity::class,
        ApplicationRecordEntity::class,
        ResumeRecordEntity::class,
        TestResultRecordEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SkillszDatabase : RoomDatabase() {
    abstract fun skillszDao(): SkillszDao

    companion object {
        @Volatile
        private var INSTANCE: SkillszDatabase? = null

        fun getDatabase(context: Context): SkillszDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SkillszDatabase::class.java,
                    "skillsz_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
