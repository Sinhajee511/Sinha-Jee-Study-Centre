package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.data.local.entity.BookmarkEntity
import com.example.data.local.entity.ChapterEntity
import com.example.data.local.entity.FormulaEntity
import com.example.data.local.entity.NoteEntity
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.QuestionEntity
import com.example.data.local.entity.QuizAttemptEntity
import com.example.data.local.entity.SubjectEntity
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = 1 LIMIT 1")
    fun getUserFlow(): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = 1 LIMIT 1")
    suspend fun getUser(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)
}

@Dao
interface SubjectDao {
    @Query("SELECT * FROM subjects WHERE studentClass = :studentClass ORDER BY orderIndex ASC")
    fun getSubjectsByClass(studentClass: String): Flow<List<SubjectEntity>>

    @Query("SELECT * FROM subjects WHERE studentClass = :studentClass AND (stream = :stream OR stream = 'General') ORDER BY orderIndex ASC")
    fun getSubjectsByClassAndStream(studentClass: String, stream: String): Flow<List<SubjectEntity>>

    @Query("SELECT * FROM subjects WHERE id = :subjectId LIMIT 1")
    suspend fun getSubjectById(subjectId: String): SubjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubjects(subjects: List<SubjectEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject: SubjectEntity)

    @Delete
    suspend fun deleteSubject(subject: SubjectEntity)
}

@Dao
interface ChapterDao {
    @Query("SELECT * FROM chapters WHERE subjectId = :subjectId ORDER BY chapterNumber ASC")
    fun getChaptersBySubject(subjectId: String): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters ORDER BY studentClass ASC, chapterNumber ASC")
    fun getAllChaptersFlow(): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE studentClass = :studentClass ORDER BY lastReadTimestamp DESC LIMIT 5")
    fun getRecentlyReadChapters(studentClass: String): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE isDownloaded = 1")
    fun getDownloadedChapters(): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE id = :chapterId LIMIT 1")
    fun getChapterFlow(chapterId: String): Flow<ChapterEntity?>

    @Query("SELECT * FROM chapters WHERE id = :chapterId LIMIT 1")
    suspend fun getChapterById(chapterId: String): ChapterEntity?

    @Query("SELECT COUNT(*) FROM chapters WHERE studentClass = :studentClass AND isCompleted = 1")
    fun getCompletedChaptersCount(studentClass: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM chapters WHERE studentClass = :studentClass")
    fun getTotalChaptersCount(studentClass: String): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<ChapterEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapter(chapter: ChapterEntity)

    @Update
    suspend fun updateChapter(chapter: ChapterEntity)

    @Query("DELETE FROM chapters WHERE id = :chapterId")
    suspend fun deleteChapterById(chapterId: String)

    @Query("SELECT * FROM chapters WHERE titleHindi LIKE '%' || :query || '%' OR titleEnglish LIKE '%' || :query || '%' OR textbookContent LIKE '%' || :query || '%'")
    suspend fun searchChapters(query: String): List<ChapterEntity>
}

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes WHERE chapterId = :chapterId")
    fun getNotesByChapter(chapterId: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes ORDER BY id ASC")
    fun getAllNotesFlow(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE isBookmarked = 1")
    fun getBookmarkedNotes(): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: List<NoteEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%'")
    suspend fun searchNotes(query: String): List<NoteEntity>
}

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions WHERE chapterId = :chapterId")
    fun getQuestionsByChapter(chapterId: String): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions ORDER BY id ASC")
    fun getAllQuestionsFlow(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE studentClass = :studentClass ORDER BY id ASC")
    fun getQuestionsByClass(studentClass: String): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE chapterId = :chapterId AND questionType = 'MCQ'")
    fun getMCQsByChapter(chapterId: String): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE subjectId = :subjectId AND questionType = 'MCQ'")
    suspend fun getMCQsBySubject(subjectId: String): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE studentClass = :studentClass AND questionType = 'PYQ' ORDER BY year DESC")
    fun getPYQsByClass(studentClass: String): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE isBookmarked = 1")
    fun getBookmarkedQuestions(): Flow<List<QuestionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuestionEntity)

    @Update
    suspend fun updateQuestion(question: QuestionEntity)

    @Query("SELECT * FROM questions WHERE questionText LIKE '%' || :query || '%' OR explanation LIKE '%' || :query || '%'")
    suspend fun searchQuestions(query: String): List<QuestionEntity>
}

@Dao
interface QuizAttemptDao {
    @Query("SELECT * FROM quiz_attempts ORDER BY timestamp DESC")
    fun getAllAttempts(): Flow<List<QuizAttemptEntity>>

    @Query("SELECT * FROM quiz_attempts WHERE subjectId = :subjectId ORDER BY timestamp DESC")
    fun getAttemptsBySubject(subjectId: String): Flow<List<QuizAttemptEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: QuizAttemptEntity)
}

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarks ORDER BY timestamp DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmarks WHERE referenceId = :referenceId LIMIT 1")
    suspend fun getBookmarkByReference(referenceId: String): BookmarkEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE referenceId = :referenceId")
    suspend fun deleteBookmarkByReference(referenceId: String)
}

@Dao
interface FormulaDao {
    @Query("SELECT * FROM formulas WHERE studentClass = :studentClass")
    fun getFormulasByClass(studentClass: String): Flow<List<FormulaEntity>>

    @Query("SELECT * FROM formulas WHERE studentClass = :studentClass AND subjectId = :subjectId")
    fun getFormulasBySubject(studentClass: String, subjectId: String): Flow<List<FormulaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFormulas(formulas: List<FormulaEntity>)

    @Query("SELECT * FROM formulas WHERE formulaTitle LIKE '%' || :query || '%' OR topic LIKE '%' || :query || '%' OR explanation LIKE '%' || :query || '%'")
    suspend fun searchFormulas(query: String): List<FormulaEntity>
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)
}
