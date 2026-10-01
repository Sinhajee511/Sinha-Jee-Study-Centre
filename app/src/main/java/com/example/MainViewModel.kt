package com.example

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.GeminiStudyAssistant
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
import com.example.data.repository.SearchResults
import com.example.data.repository.StudyRepository
import com.example.data.seed.InitialDataSeeder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

enum class ReadingTheme {
    DAY, SEPIA, NIGHT
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val database = AppDatabase.getInstance(application)
    val repository = StudyRepository(database)

    // TTS engine
    private var tts: TextToSpeech? = null
    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    init {
        viewModelScope.launch {
            InitialDataSeeder.seedDatabaseIfEmpty(database)
        }
        initTts(application)
    }

    private fun initTts(application: Application) {
        tts = TextToSpeech(application) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val hiLocale = Locale.forLanguageTag("hi-IN")
                val result = tts?.setLanguage(hiLocale)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts?.setLanguage(Locale.ENGLISH)
                }
                tts?.setOnUtteranceProgressListener(object : android.speech.tts.UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        _isSpeaking.value = true
                    }
                    override fun onDone(utteranceId: String?) {
                        _isSpeaking.value = false
                    }
                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        _isSpeaking.value = false
                    }
                })
            }
        }
    }

    fun speakText(text: String) {
        if (_isSpeaking.value) {
            stopSpeaking()
            return
        }
        val cleanText = text.replace(Regex("[#*`>_-]"), " ")
        tts?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, null, "TextbookTTS")
        _isSpeaking.value = true
    }

    fun stopSpeaking() {
        tts?.stop()
        _isSpeaking.value = false
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
    }

    // User Flow
    val currentUser: StateFlow<UserEntity?> = repository.userFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun loginOrRegister(name: String, phone: String, studentClass: String, board: String, medium: String, stream: String) {
        viewModelScope.launch {
            val user = UserEntity(
                id = 1,
                name = name,
                phone = phone,
                studentClass = studentClass,
                board = board,
                medium = medium,
                stream = stream,
                isLoggedIn = true,
                isAdmin = false
            )
            repository.saveUser(user)
        }
    }

    fun switchClass(newClass: String) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val newStream = if (newClass == "10") "General" else if (user.stream == "General") "Science" else user.stream
            repository.updateUser(user.copy(studentClass = newClass, stream = newStream))
        }
    }

    fun switchStream(newStream: String) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            repository.updateUser(user.copy(stream = newStream))
        }
    }

    fun toggleAdminMode(isAdmin: Boolean) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            repository.updateUser(user.copy(isAdmin = isAdmin))
        }
    }

    fun updateProfile(name: String, board: String, medium: String) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            repository.updateUser(user.copy(name = name, board = board, medium = medium))
        }
    }

    fun logout() {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            repository.updateUser(user.copy(isLoggedIn = false))
        }
    }

    // Subjects
    val subjects: StateFlow<List<SubjectEntity>> = currentUser.flatMapLatest { user ->
        val c = user?.studentClass ?: "10"
        val s = user?.stream ?: "General"
        repository.getSubjects(c, s)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Recently read
    val recentlyRead: StateFlow<List<ChapterEntity>> = currentUser.flatMapLatest { user ->
        val c = user?.studentClass ?: "10"
        repository.getRecentlyReadChapters(c)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All Bookmarks
    val bookmarks: StateFlow<List<BookmarkEntity>> = repository.allBookmarks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Downloaded Chapters
    val downloadedChapters: StateFlow<List<ChapterEntity>> = repository.getDownloadedChapters()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Notifications
    val notifications: StateFlow<List<NotificationEntity>> = repository.notifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Quiz Attempts
    val quizAttempts: StateFlow<List<QuizAttemptEntity>> = repository.allQuizAttempts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Formulas
    val formulas: StateFlow<List<FormulaEntity>> = currentUser.flatMapLatest { user ->
        val c = user?.studentClass ?: "10"
        repository.getFormulas(c)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // PYQs
    val pyqs: StateFlow<List<QuestionEntity>> = currentUser.flatMapLatest { user ->
        val c = user?.studentClass ?: "10"
        repository.getPYQsByClass(c)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Reader state
    val fontSize = MutableStateFlow(18f)
    val readingTheme = MutableStateFlow(ReadingTheme.DAY)
    val isFullscreen = MutableStateFlow(false)

    fun setFontSize(size: Float) {
        fontSize.value = size.coerceIn(14f, 28f)
    }

    fun setReadingTheme(theme: ReadingTheme) {
        readingTheme.value = theme
    }

    fun toggleFullscreen() {
        isFullscreen.value = !isFullscreen.value
    }

    // Save reading position
    fun saveReadingProgress(chapterId: String, progress: Int, section: String, page: Int) {
        viewModelScope.launch {
            repository.updateReadingProgress(chapterId, progress, section, page)
        }
    }

    fun toggleDownload(chapterId: String, currentStatus: Boolean) {
        viewModelScope.launch {
            repository.toggleChapterDownload(chapterId, !currentStatus)
        }
    }

    fun toggleBookmark(type: String, refId: String, title: String, subtitle: String) {
        viewModelScope.launch {
            repository.toggleBookmark(type, refId, title, subtitle)
        }
    }

    // Global Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow(SearchResults())
    val searchResults: StateFlow<SearchResults> = _searchResults.asStateFlow()

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            _searchResults.value = repository.searchAll(query)
        }
    }

    // AI Study Assistant state
    private val _aiResponse = MutableStateFlow<String?>(null)
    val aiResponse: StateFlow<String?> = _aiResponse.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    fun askAiAssistant(prompt: String, chapterContext: String) {
        viewModelScope.launch {
            _isAiLoading.value = true
            _aiResponse.value = null
            val response = GeminiStudyAssistant.askAssistant(prompt, chapterContext)
            _aiResponse.value = response
            _isAiLoading.value = false
        }
    }

    fun clearAiResponse() {
        _aiResponse.value = null
    }

    // Quiz Session State
    val quizQuestions = MutableStateFlow<List<QuestionEntity>>(emptyList())
    val selectedAnswers = MutableStateFlow<Map<Int, String>>(emptyMap())
    val currentQuestionIndex = MutableStateFlow(0)
    val isQuizSubmitted = MutableStateFlow(false)
    val lastQuizAttempt = MutableStateFlow<QuizAttemptEntity?>(null)

    fun startQuiz(questions: List<QuestionEntity>) {
        quizQuestions.value = questions.shuffled()
        selectedAnswers.value = emptyMap()
        currentQuestionIndex.value = 0
        isQuizSubmitted.value = false
        lastQuizAttempt.value = null
    }

    fun resetQuizState() {
        quizQuestions.value = emptyList()
        selectedAnswers.value = emptyMap()
        currentQuestionIndex.value = 0
        isQuizSubmitted.value = false
        lastQuizAttempt.value = null
    }

    fun selectQuizAnswer(questionIndex: Int, answer: String) {
        if (isQuizSubmitted.value) return
        val current = selectedAnswers.value.toMutableMap()
        current[questionIndex] = answer
        selectedAnswers.value = current
    }

    fun submitQuiz(quizTitle: String, subjectId: String, chapterId: String? = null) {
        if (isQuizSubmitted.value) return
        isQuizSubmitted.value = true

        val qList = quizQuestions.value
        var correct = 0
        var wrong = 0
        var skipped = 0

        qList.forEachIndexed { index, q ->
            val ans = selectedAnswers.value[index]
            if (ans == null) {
                skipped++
            } else if (ans.equals(q.correctAnswer, ignoreCase = true)) {
                correct++
            } else {
                wrong++
            }
        }

        val total = qList.size
        val scorePercent = if (total > 0) (correct * 100) / total else 0

        val attempt = QuizAttemptEntity(
            quizTitle = quizTitle,
            subjectId = subjectId,
            chapterId = chapterId,
            totalQuestions = total,
            correctAnswers = correct,
            wrongAnswers = wrong,
            skippedAnswers = skipped,
            scorePercentage = scorePercent,
            timeTakenSeconds = 120,
            timestamp = System.currentTimeMillis()
        )

        lastQuizAttempt.value = attempt
        viewModelScope.launch {
            repository.recordQuizAttempt(attempt)
        }
    }

    // Admin Operations
    fun adminAddChapter(chapter: ChapterEntity) {
        viewModelScope.launch {
            repository.addOrUpdateChapter(chapter)
        }
    }

    fun adminDeleteChapter(chapterId: String) {
        viewModelScope.launch {
            repository.deleteChapter(chapterId)
        }
    }

    fun adminAddQuestion(question: QuestionEntity) {
        viewModelScope.launch {
            repository.addQuestion(question)
        }
    }

    fun adminAddNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.addNote(note)
        }
    }

    fun adminBroadcastNotification(title: String, message: String, type: String) {
        viewModelScope.launch {
            repository.sendNotification(title, message, type)
        }
    }
}
