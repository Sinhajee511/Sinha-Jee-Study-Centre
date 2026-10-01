package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.BookmarkDao
import com.example.data.local.dao.ChapterDao
import com.example.data.local.dao.FormulaDao
import com.example.data.local.dao.NoteDao
import com.example.data.local.dao.NotificationDao
import com.example.data.local.dao.QuestionDao
import com.example.data.local.dao.QuizAttemptDao
import com.example.data.local.dao.SubjectDao
import com.example.data.local.dao.UserDao
import com.example.data.local.entity.BookmarkEntity
import com.example.data.local.entity.ChapterEntity
import com.example.data.local.entity.FormulaEntity
import com.example.data.local.entity.NoteEntity
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.QuestionEntity
import com.example.data.local.entity.QuizAttemptEntity
import com.example.data.local.entity.SubjectEntity
import com.example.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        SubjectEntity::class,
        ChapterEntity::class,
        NoteEntity::class,
        QuestionEntity::class,
        QuizAttemptEntity::class,
        BookmarkEntity::class,
        FormulaEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun subjectDao(): SubjectDao
    abstract fun chapterDao(): ChapterDao
    abstract fun noteDao(): NoteDao
    abstract fun questionDao(): QuestionDao
    abstract fun quizAttemptDao(): QuizAttemptDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun formulaDao(): FormulaDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sinhajee_study_centre.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
