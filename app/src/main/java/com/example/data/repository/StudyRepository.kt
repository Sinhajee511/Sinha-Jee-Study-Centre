package com.example.data.repository

import com.example.data.local.AppDatabase
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

class StudyRepository(val database: AppDatabase) {

    // User
    val userFlow: Flow<UserEntity?> = database.userDao().getUserFlow()
    suspend fun getUser(): UserEntity? = database.userDao().getUser()
    suspend fun saveUser(user: UserEntity) = database.userDao().insertUser(user)
    suspend fun updateUser(user: UserEntity) = database.userDao().updateUser(user)

    // Subjects
    fun getSubjects(studentClass: String, stream: String): Flow<List<SubjectEntity>> {
        return if (studentClass == "10") {
            database.subjectDao().getSubjectsByClass("10")
        } else {
            database.subjectDao().getSubjectsByClassAndStream("12", stream)
        }
    }
    suspend fun addSubject(subject: SubjectEntity) = database.subjectDao().insertSubject(subject)
    suspend fun deleteSubject(subject: SubjectEntity) = database.subjectDao().deleteSubject(subject)

    // Chapters
    fun getChaptersBySubject(subjectId: String): Flow<List<ChapterEntity>> =
        database.chapterDao().getChaptersBySubject(subjectId)

    fun getAllChaptersFlow(): Flow<List<ChapterEntity>> =
        database.chapterDao().getAllChaptersFlow()

    fun getRecentlyReadChapters(studentClass: String): Flow<List<ChapterEntity>> =
        database.chapterDao().getRecentlyReadChapters(studentClass)

    fun getDownloadedChapters(): Flow<List<ChapterEntity>> =
        database.chapterDao().getDownloadedChapters()

    fun getChapterFlow(chapterId: String): Flow<ChapterEntity?> =
        database.chapterDao().getChapterFlow(chapterId)

    suspend fun getChapter(chapterId: String): ChapterEntity? =
        database.chapterDao().getChapterById(chapterId)

    suspend fun updateReadingProgress(chapterId: String, progressPercent: Int, section: String, page: Int) {
        val chapter = database.chapterDao().getChapterById(chapterId) ?: return
        val updated = chapter.copy(
            readingProgressPercent = progressPercent,
            lastReadSection = section,
            lastReadPage = page,
            lastReadTimestamp = System.currentTimeMillis(),
            isCompleted = progressPercent >= 100
        )
        database.chapterDao().updateChapter(updated)
    }

    suspend fun toggleChapterDownload(chapterId: String, isDownloaded: Boolean) {
        val chapter = database.chapterDao().getChapterById(chapterId) ?: return
        database.chapterDao().updateChapter(chapter.copy(isDownloaded = isDownloaded))
    }

    suspend fun addOrUpdateChapter(chapter: ChapterEntity) =
        database.chapterDao().insertChapter(chapter)

    suspend fun deleteChapter(chapterId: String) =
        database.chapterDao().deleteChapterById(chapterId)

    // Notes
    fun getNotesByChapter(chapterId: String): Flow<List<NoteEntity>> =
        database.noteDao().getNotesByChapter(chapterId)

    fun getAllNotesFlow(): Flow<List<NoteEntity>> =
        database.noteDao().getAllNotesFlow()

    fun getBookmarkedNotes(): Flow<List<NoteEntity>> =
        database.noteDao().getBookmarkedNotes()

    suspend fun addNote(note: NoteEntity) = database.noteDao().insertNote(note)
    suspend fun toggleNoteBookmark(note: NoteEntity) =
        database.noteDao().updateNote(note.copy(isBookmarked = !note.isBookmarked))

    // Questions & Quizzes
    fun getQuestionsByChapter(chapterId: String): Flow<List<QuestionEntity>> =
        database.questionDao().getQuestionsByChapter(chapterId)

    fun getAllQuestionsFlow(): Flow<List<QuestionEntity>> =
        database.questionDao().getAllQuestionsFlow()

    fun getQuestionsByClassFlow(studentClass: String): Flow<List<QuestionEntity>> =
        database.questionDao().getQuestionsByClass(studentClass)

    fun getMCQsByChapter(chapterId: String): Flow<List<QuestionEntity>> =
        database.questionDao().getMCQsByChapter(chapterId)

    suspend fun getMCQsBySubject(subjectId: String): List<QuestionEntity> =
        database.questionDao().getMCQsBySubject(subjectId)

    fun getPYQsByClass(studentClass: String): Flow<List<QuestionEntity>> =
        database.questionDao().getPYQsByClass(studentClass)

    fun getBookmarkedQuestions(): Flow<List<QuestionEntity>> =
        database.questionDao().getBookmarkedQuestions()

    suspend fun addQuestion(question: QuestionEntity) =
        database.questionDao().insertQuestion(question)

    suspend fun getAllQuestions(): List<QuestionEntity> =
        database.questionDao().searchQuestions("")

    suspend fun getQuestionsForChapter(chapterId: String): List<QuestionEntity> =
        database.questionDao().searchQuestions("")

    suspend fun toggleQuestionBookmark(question: QuestionEntity) =
        database.questionDao().updateQuestion(question.copy(isBookmarked = !question.isBookmarked))

    // Quiz Attempts
    val allQuizAttempts: Flow<List<QuizAttemptEntity>> = database.quizAttemptDao().getAllAttempts()
    suspend fun recordQuizAttempt(attempt: QuizAttemptEntity) =
        database.quizAttemptDao().insertAttempt(attempt)

    // Bookmarks
    val allBookmarks: Flow<List<BookmarkEntity>> = database.bookmarkDao().getAllBookmarks()
    suspend fun isBookmarked(referenceId: String): Boolean =
        database.bookmarkDao().getBookmarkByReference(referenceId) != null

    suspend fun toggleBookmark(type: String, refId: String, title: String, subtitle: String) {
        val existing = database.bookmarkDao().getBookmarkByReference(refId)
        if (existing != null) {
            database.bookmarkDao().deleteBookmarkByReference(refId)
        } else {
            database.bookmarkDao().insertBookmark(
                BookmarkEntity(
                    itemType = type,
                    referenceId = refId,
                    title = title,
                    subtitle = subtitle
                )
            )
        }
    }

    // Formulas
    fun getFormulas(studentClass: String): Flow<List<FormulaEntity>> =
        database.formulaDao().getFormulasByClass(studentClass)

    // Notifications
    val notifications: Flow<List<NotificationEntity>> = database.notificationDao().getAllNotifications()
    suspend fun sendNotification(title: String, message: String, type: String) =
        database.notificationDao().insertNotification(
            NotificationEntity(title = title, message = message, type = type)
        )
    suspend fun markNotificationRead(id: Long) = database.notificationDao().markAsRead(id)

    // Global Search
    suspend fun searchAll(query: String): SearchResults {
        val cleanQuery = query.trim()
        if (cleanQuery.isEmpty()) return SearchResults()
        val chapters = database.chapterDao().searchChapters(cleanQuery)
        val notes = database.noteDao().searchNotes(cleanQuery)
        val questions = database.questionDao().searchQuestions(cleanQuery)
        val formulas = database.formulaDao().searchFormulas(cleanQuery)
        return SearchResults(chapters, notes, questions, formulas)
    }
}

data class SearchResults(
    val chapters: List<ChapterEntity> = emptyList(),
    val notes: List<NoteEntity> = emptyList(),
    val questions: List<QuestionEntity> = emptyList(),
    val formulas: List<FormulaEntity> = emptyList()
)
