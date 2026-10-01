package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Long = 1,
    val phone: String = "",
    val name: String = "",
    val studentClass: String = "10", // "10" or "12"
    val board: String = "Bihar Board (BSEB)",
    val medium: String = "Hindi", // "Hindi" or "English"
    val stream: String = "General", // "General", "Science", "Arts", "Commerce"
    val avatarId: Int = 1,
    val studyStreakDays: Int = 3,
    val totalStudyMinutes: Int = 180,
    val lastActiveTimestamp: Long = System.currentTimeMillis(),
    val isLoggedIn: Boolean = false,
    val isAdmin: Boolean = false
)

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey val id: String,
    val studentClass: String, // "10" or "12"
    val stream: String, // "General", "Science", "Arts", "Commerce"
    val titleHindi: String,
    val titleEnglish: String,
    val iconName: String,
    val colorHex: String,
    val orderIndex: Int
)

@Entity(tableName = "chapters")
data class ChapterEntity(
    @PrimaryKey val id: String,
    val subjectId: String,
    val studentClass: String,
    val chapterNumber: Int,
    val titleHindi: String,
    val titleEnglish: String,
    val introduction: String,
    val learningObjectives: String,
    val textbookContent: String,
    val summary: String,
    val videoUrl: String? = null,
    val videoTitle: String? = null,
    val isCompleted: Boolean = false,
    val readingProgressPercent: Int = 0,
    val lastReadSection: String = "Introduction",
    val lastReadTimestamp: Long = 0L,
    val totalPages: Int = 24,
    val lastReadPage: Int = 1,
    val isDownloaded: Boolean = false
)

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey val id: String,
    val chapterId: String,
    val subjectId: String,
    val title: String,
    val noteType: String, // "complete", "short", "important_points", "easy_explanation", "exam_notes"
    val content: String,
    val isBookmarked: Boolean = false
)

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey val id: String,
    val chapterId: String,
    val subjectId: String,
    val studentClass: String,
    val questionType: String, // "MCQ", "VERY_SHORT", "SHORT", "LONG", "PYQ", "IMPORTANT"
    val questionText: String,
    val optionA: String? = null,
    val optionB: String? = null,
    val optionC: String? = null,
    val optionD: String? = null,
    val correctAnswer: String,
    val explanation: String,
    val year: Int? = null, // for PYQ
    val isBookmarked: Boolean = false
)

@Entity(tableName = "quiz_attempts")
data class QuizAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val quizTitle: String,
    val subjectId: String,
    val chapterId: String? = null,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val skippedAnswers: Int,
    val scorePercentage: Int,
    val timeTakenSeconds: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val itemType: String, // "chapter", "note", "question", "formula"
    val referenceId: String,
    val title: String,
    val subtitle: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "formulas")
data class FormulaEntity(
    @PrimaryKey val id: String,
    val subjectId: String,
    val studentClass: String,
    val topic: String,
    val formulaTitle: String,
    val formulaMath: String,
    val explanation: String,
    val unitOrLaw: String
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val message: String,
    val type: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)
