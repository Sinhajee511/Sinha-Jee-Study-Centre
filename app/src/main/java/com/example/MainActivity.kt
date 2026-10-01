package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ui.screens.admin.AdminPanelScreen
import com.example.ui.screens.auth.LoginScreen
import com.example.ui.screens.bookmarks.BookmarksScreen
import com.example.ui.screens.bookmarks.DownloadsScreen
import com.example.ui.screens.classes.ClassSelectionScreen
import com.example.ui.screens.formulas.FormulaRevisionScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.info.AboutUsScreen
import com.example.ui.screens.info.NotificationsScreen
import com.example.ui.screens.info.PrivacyPolicyScreen
import com.example.ui.screens.notes.NotesScreen
import com.example.ui.screens.onboarding.OnboardingScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.progress.ProgressDashboardScreen
import com.example.ui.screens.pyq.PreviousYearQuestionsScreen
import com.example.ui.screens.questions.QuestionBankScreen
import com.example.ui.screens.quiz.QuizScreen
import com.example.ui.screens.search.GlobalSearchScreen
import com.example.ui.screens.textbook.ChapterListScreen
import com.example.ui.screens.textbook.DigitalTextbookReaderScreen
import com.example.ui.theme.SinhaJeeStudyCentreTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SinhaJeeStudyCentreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigationHost(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavigationHost(viewModel: MainViewModel) {
    val user by viewModel.currentUser.collectAsState()
    var hasCompletedOnboarding by remember { mutableStateOf(false) }

    // Navigation Stack
    val backstack = remember { mutableStateListOf("home") }
    val currentRoute = backstack.lastOrNull() ?: "home"

    fun navigate(route: String) {
        if (route == currentRoute) return
        if (route in listOf("home", "class_selection", "progress", "profile")) {
            // Root tab navigation
            backstack.clear()
            backstack.add(route)
        } else {
            backstack.add(route)
        }
    }

    fun popBack() {
        if (backstack.size > 1) {
            backstack.removeAt(backstack.lastIndex)
        }
    }

    BackHandler(enabled = backstack.size > 1) {
        popBack()
    }

    // Compulsory Onboarding & Login check
    if (user == null || !user!!.isLoggedIn) {
        if (!hasCompletedOnboarding) {
            OnboardingScreen(onFinished = { hasCompletedOnboarding = true })
        } else {
            LoginScreen(
                onLoginSuccess = { name, phone, studentClass, board, medium, stream ->
                    viewModel.loginOrRegister(name, phone, studentClass, board, medium, stream)
                }
            )
        }
        return
    }

    // Router
    when {
        currentRoute == "home" -> {
            HomeScreen(viewModel = viewModel, onNavigate = { navigate(it) })
        }
        currentRoute == "class_selection" -> {
            ClassSelectionScreen(viewModel = viewModel, onNavigate = { navigate(it) })
        }
        currentRoute.startsWith("subject_chapters/") -> {
            val parts = currentRoute.removePrefix("subject_chapters/").split("/")
            val subjectId = parts.getOrNull(0) ?: "c10_sci"
            val subjectTitle = parts.getOrNull(1) ?: "विज्ञान"
            ChapterListScreen(
                subjectId = subjectId,
                subjectTitle = subjectTitle,
                viewModel = viewModel,
                onNavigate = { navigate(it) },
                onBack = { popBack() }
            )
        }
        currentRoute.startsWith("textbook_reader/") -> {
            val parts = currentRoute.removePrefix("textbook_reader/").split("/")
            val chapterId = parts.getOrNull(0) ?: "c10_sci_ch1"
            val subjectTitle = parts.getOrNull(1) ?: "विज्ञान"
            DigitalTextbookReaderScreen(
                chapterId = chapterId,
                subjectTitle = subjectTitle,
                viewModel = viewModel,
                onNavigate = { navigate(it) },
                onBack = { popBack() }
            )
        }
        currentRoute.startsWith("notes/") -> {
            val chapterId = currentRoute.removePrefix("notes/").ifBlank { "c10_sci_ch1" }
            NotesScreen(
                chapterId = chapterId,
                viewModel = viewModel,
                onBack = { popBack() }
            )
        }
        currentRoute.startsWith("question_bank/") -> {
            val chapterId = currentRoute.removePrefix("question_bank/").ifBlank { "c10_sci_ch1" }
            QuestionBankScreen(
                chapterId = chapterId,
                viewModel = viewModel,
                onBack = { popBack() }
            )
        }
        currentRoute.startsWith("quiz/") -> {
            val parts = currentRoute.removePrefix("quiz/").split("/")
            val subjectId = parts.getOrNull(0) ?: "all"
            val chapterId = parts.getOrNull(1) ?: "all"
            QuizScreen(
                subjectId = subjectId,
                chapterId = chapterId,
                viewModel = viewModel,
                onBack = { popBack() }
            )
        }
        currentRoute == "formulas" -> {
            FormulaRevisionScreen(viewModel = viewModel, onBack = { popBack() })
        }
        currentRoute == "pyq" -> {
            PreviousYearQuestionsScreen(viewModel = viewModel, onBack = { popBack() })
        }
        currentRoute == "progress" -> {
            ProgressDashboardScreen(viewModel = viewModel, onNavigate = { navigate(it) })
        }
        currentRoute == "bookmarks" -> {
            BookmarksScreen(
                viewModel = viewModel,
                onNavigate = { navigate(it) },
                onBack = { popBack() }
            )
        }
        currentRoute == "downloads" -> {
            DownloadsScreen(
                viewModel = viewModel,
                onNavigate = { navigate(it) },
                onBack = { popBack() }
            )
        }
        currentRoute == "search" -> {
            GlobalSearchScreen(
                viewModel = viewModel,
                onNavigate = { navigate(it) },
                onBack = { popBack() }
            )
        }
        currentRoute == "profile" -> {
            ProfileScreen(viewModel = viewModel, onNavigate = { navigate(it) })
        }
        currentRoute == "admin_panel" -> {
            AdminPanelScreen(viewModel = viewModel, onBack = { popBack() })
        }
        currentRoute == "about_us" -> {
            AboutUsScreen(onBack = { popBack() })
        }
        currentRoute == "privacy_policy" -> {
            PrivacyPolicyScreen(onBack = { popBack() })
        }
        currentRoute == "notifications" -> {
            NotificationsScreen(viewModel = viewModel, onBack = { popBack() })
        }
        else -> {
            HomeScreen(viewModel = viewModel, onNavigate = { navigate(it) })
        }
    }
}
