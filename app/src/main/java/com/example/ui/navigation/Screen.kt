package com.example.ui.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Home : Screen("home")
    object ClassSelection : Screen("class_selection")
    object SubjectChapters : Screen("subject_chapters/{subjectId}/{subjectTitle}") {
        fun createRoute(subjectId: String, subjectTitle: String) = "subject_chapters/$subjectId/$subjectTitle"
    }
    object TextbookReader : Screen("textbook_reader/{chapterId}/{subjectTitle}") {
        fun createRoute(chapterId: String, subjectTitle: String) = "textbook_reader/$chapterId/$subjectTitle"
    }
    object Notes : Screen("notes/{chapterId}") {
        fun createRoute(chapterId: String) = "notes/$chapterId"
    }
    object QuestionBank : Screen("question_bank/{chapterId}") {
        fun createRoute(chapterId: String) = "question_bank/$chapterId"
    }
    object Quiz : Screen("quiz/{subjectId}/{chapterId}") {
        fun createRoute(subjectId: String, chapterId: String = "all") = "quiz/$subjectId/$chapterId"
    }
    object Formulas : Screen("formulas")
    object PYQ : Screen("pyq")
    object Progress : Screen("progress")
    object Bookmarks : Screen("bookmarks")
    object Downloads : Screen("downloads")
    object Search : Screen("search")
    object Profile : Screen("profile")
    object AdminPanel : Screen("admin_panel")
    object AboutUs : Screen("about_us")
    object PrivacyPolicy : Screen("privacy_policy")
    object Notifications : Screen("notifications")
}
