package com.example.ui.screens.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.MainViewModel
import com.example.data.local.entity.QuestionEntity
import com.example.ui.components.SinhaTopAppBar
import com.example.ui.theme.ScholarEmerald
import com.example.ui.theme.ScholarGold
import com.example.ui.theme.ScholarPrimary
import kotlinx.coroutines.delay

@Composable
fun QuizScreen(
    subjectId: String,
    chapterId: String,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val quizQuestions by viewModel.quizQuestions.collectAsState()
    val selectedAnswers by viewModel.selectedAnswers.collectAsState()
    val currentIndex by viewModel.currentQuestionIndex.collectAsState()
    val isSubmitted by viewModel.isQuizSubmitted.collectAsState()
    val lastAttempt by viewModel.lastQuizAttempt.collectAsState()

    var timerSeconds by remember { mutableIntStateOf(600) } // 10 minutes timer
    var isTimerRunning by remember { mutableStateOf(true) }

    // Load quiz questions
    LaunchedEffect(subjectId, chapterId) {
        val allQ = viewModel.repository.getAllQuestions()
        val filtered = if (chapterId != "all") {
            allQ.filter { it.chapterId == chapterId && (it.questionType == "MCQ" || it.optionA != null) }
        } else if (subjectId != "all") {
            allQ.filter { it.subjectId == subjectId && (it.questionType == "MCQ" || it.optionA != null) }
        } else {
            allQ.filter { it.questionType == "MCQ" || it.optionA != null }
        }
        val mcqs = if (filtered.isNotEmpty()) filtered else allQ.filter { it.questionType == "MCQ" || it.optionA != null }
        if (mcqs.isNotEmpty()) {
            viewModel.startQuiz(mcqs)
            timerSeconds = 600
            isTimerRunning = true
        }
    }

    // Timer countdown
    LaunchedEffect(isTimerRunning, isSubmitted) {
        while (isTimerRunning && !isSubmitted && timerSeconds > 0) {
            delay(1000)
            timerSeconds--
            if (timerSeconds <= 0) {
                viewModel.submitQuiz("अध्याय मॉक टेस्ट", subjectId, chapterId)
            }
        }
    }

    if (isSubmitted && lastAttempt != null) {
        // Result Screen
        QuizResultView(
            attempt = lastAttempt!!,
            questions = quizQuestions,
            selectedAnswers = selectedAnswers,
            onRetry = {
                viewModel.startQuiz(quizQuestions)
                timerSeconds = 600
                isTimerRunning = true
            },
            onClose = {
                viewModel.resetQuizState()
                onBack()
            }
        )
    } else {
        // Active Quiz Screen
        Scaffold(
            topBar = {
                SinhaTopAppBar(
                    title = "ऑनलाइन मॉक टेस्ट (Online Test)",
                    subtitle = "क्वेश्चन ${currentIndex + 1} / ${quizQuestions.size.coerceAtLeast(1)}",
                    showBackButton = true,
                    onBackClick = {
                        viewModel.resetQuizState()
                        onBack()
                    },
                    actions = {
                        // Timer chip
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.padding(end = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Alarm,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = ScholarPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                val mins = timerSeconds / 60
                                val secs = timerSeconds % 60
                                Text(
                                    text = String.format("%02d:%02d", mins, secs),
                                    fontWeight = FontWeight.Bold,
                                    color = ScholarPrimary,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                )
            }
        ) { padding ->
            if (quizQuestions.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Text("क्विज़ प्रश्न लोड हो रहे हैं...")
                }
            } else {
                val q = quizQuestions[currentIndex]
                val selectedOpt = selectedAnswers[currentIndex]

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        // Progress Bar
                        LinearProgressIndicator(
                            progress = { (currentIndex + 1f) / quizQuestions.size },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = ScholarPrimary
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = ScholarPrimary.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = "प्रश्न ${currentIndex + 1}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = ScholarPrimary,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = q.questionText,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 24.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Options A, B, C, D
                        val options = listOfNotNull(
                            q.optionA?.let { "A" to it },
                            q.optionB?.let { "B" to it },
                            q.optionC?.let { "C" to it },
                            q.optionD?.let { "D" to it }
                        )

                        options.forEach { (key, optText) ->
                            val isSelected = selectedOpt == key
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isSelected) ScholarPrimary.copy(alpha = 0.15f)
                                        else MaterialTheme.colorScheme.surface
                                    )
                                    .clickable { viewModel.selectQuizAnswer(currentIndex, key) }
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (isSelected) ScholarPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = key,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Text(
                                    text = optText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // Navigation bar (Previous, Skip, Next / Submit)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                if (currentIndex > 0) {
                                    viewModel.currentQuestionIndex.value--
                                }
                            },
                            enabled = currentIndex > 0,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("← पिछला")
                        }

                        if (currentIndex < quizQuestions.size - 1) {
                            Button(
                                onClick = { viewModel.currentQuestionIndex.value++ },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ScholarPrimary)
                            ) {
                                Text("अगला →")
                            }
                        } else {
                            Button(
                                onClick = { viewModel.submitQuiz("अध्याय मॉक टेस्ट", subjectId, chapterId) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ScholarEmerald)
                            ) {
                                Text("सबमिट करें ✓")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizResultView(
    attempt: com.example.data.local.entity.QuizAttemptEntity,
    questions: List<QuestionEntity>,
    selectedAnswers: Map<Int, String>,
    onRetry: () -> Unit,
    onClose: () -> Unit
) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(ScholarGold.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = ScholarGold,
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "क्विज़ परिणाम (Quiz Scorecard)",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if (attempt.scorePercentage >= 75) "उत्कृष्ट प्रदर्शन! शानदार तैयारी!" else "अच्छा प्रयास! अभ्यास जारी रखें।",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Score Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${attempt.scorePercentage}%",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Black,
                        color = ScholarPrimary
                    )
                    Text(
                        text = "कुल अंक: ${attempt.correctAnswers} / ${attempt.totalQuestions}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "सही (Correct)", fontSize = 12.sp, color = ScholarEmerald)
                            Text(text = "${attempt.correctAnswers}", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = ScholarEmerald)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "गलत (Wrong)", fontSize = 12.sp, color = Color.Red)
                            Text(text = "${attempt.wrongAnswers}", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Red)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "छोड़े गए (Skipped)", fontSize = 12.sp, color = Color.Gray)
                            Text(text = "${attempt.skippedAnswers}", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Gray)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Buttons
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onRetry,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Replay, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("पुनः प्रयास (Retry)")
                }

                Button(
                    onClick = onClose,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ScholarPrimary)
                ) {
                    Text("समाप्त (Done)")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Detailed Question Review
            Text(
                text = "विस्तृत उत्तर समीक्षा (Question Review)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(12.dp))

            questions.forEachIndexed { index, q ->
                val chosen = selectedAnswers[index]
                val isCorrect = chosen.equals(q.correctAnswer, ignoreCase = true)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "प्र. ${index + 1}: ${q.questionText}",
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "आपका उत्तर: ${chosen ?: "छोड़ दिया"} | सही उत्तर: ${q.correctAnswer}",
                            color = if (isCorrect) ScholarEmerald else Color.Red,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "व्याख्या: ${q.explanation}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
