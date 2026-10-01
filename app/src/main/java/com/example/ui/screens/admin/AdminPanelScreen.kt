package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.MainViewModel
import com.example.data.local.entity.ChapterEntity
import com.example.data.local.entity.QuestionEntity
import com.example.ui.components.SinhaTopAppBar
import com.example.ui.theme.ScholarEmerald
import com.example.ui.theme.ScholarGold
import com.example.ui.theme.ScholarPrimary

@Composable
fun AdminPanelScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            SinhaTopAppBar(
                title = "व्यवस्थापक पैनल (Admin CMS)",
                subtitle = "पाठ्यपुस्तक व परीक्षा सामग्री प्रबंधन",
                showBackButton = true,
                onBackClick = onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = ScholarPrimary
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("अध्याय जोड़ें") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("MCQ जोड़ें") }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("सूचना भेजें") }
                )
            }

            when (selectedTab) {
                0 -> AddChapterTab(viewModel = viewModel)
                1 -> AddQuestionTab(viewModel = viewModel)
                2 -> BroadcastNotificationTab(viewModel = viewModel)
            }
        }
    }
}

@Composable
private fun AddChapterTab(viewModel: MainViewModel) {
    var studentClass by remember { mutableStateOf("10") }
    var subjectId by remember { mutableStateOf("c10_sci") }
    var chapterNumber by remember { mutableStateOf("3") }
    var titleHindi by remember { mutableStateOf("") }
    var titleEnglish by remember { mutableStateOf("") }
    var intro by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(text = "नया डिजिटल अध्याय प्रकाशित करें (Publish Chapter)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = studentClass == "10",
                    onClick = { studentClass = "10"; subjectId = "c10_sci" },
                    label = { Text("Class 10") }
                )
                FilterChip(
                    selected = studentClass == "12",
                    onClick = { studentClass = "12"; subjectId = "c12_phy" },
                    label = { Text("Class 12") }
                )
            }
        }

        item {
            OutlinedTextField(
                value = chapterNumber,
                onValueChange = { chapterNumber = it },
                label = { Text("अध्याय संख्या (Chapter Number)") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = titleHindi,
                onValueChange = { titleHindi = it },
                label = { Text("अध्याय का नाम हिंदी में (Hindi Title)") },
                modifier = Modifier.fillMaxWidth().testTag("admin_chapter_title_hi")
            )
        }

        item {
            OutlinedTextField(
                value = titleEnglish,
                onValueChange = { titleEnglish = it },
                label = { Text("English Title") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = intro,
                onValueChange = { intro = it },
                label = { Text("प्रस्तावना (Introduction)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
        }

        item {
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("मुख्य पाठ्य सामग्री (Textbook Content)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )
        }

        item {
            OutlinedTextField(
                value = summary,
                onValueChange = { summary = it },
                label = { Text("अध्याय का सारांश (Summary)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
        }

        item {
            Button(
                onClick = {
                    if (titleHindi.isNotBlank()) {
                        val num = chapterNumber.toIntOrNull() ?: 3
                        val newId = "${subjectId}_ch$num"
                        val chapter = ChapterEntity(
                            id = newId,
                            subjectId = subjectId,
                            studentClass = studentClass,
                            chapterNumber = num,
                            titleHindi = titleHindi,
                            titleEnglish = if (titleEnglish.isBlank()) titleHindi else titleEnglish,
                            introduction = intro.ifBlank { "इस अध्याय में विस्तृत अध्ययन प्रस्तुत किया गया है।" },
                            learningObjectives = "• मुख्य अवधारणाओं की समझ\n• परीक्षा उपयोगी प्रश्न",
                            textbookContent = content.ifBlank { "पाठ्य सामग्री व्यवस्थापक द्वारा जोड़ी गई है।" },
                            summary = summary.ifBlank { "अध्याय का संक्षिप्त सारांश।" },
                            isDownloaded = false
                        )
                        viewModel.adminAddChapter(chapter)
                        isSuccess = true
                        titleHindi = ""
                        titleEnglish = ""
                        content = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("publish_chapter_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ScholarEmerald)
            ) {
                Text("Publish Chapter (तुरंत प्रकाशित करें) ✓", fontWeight = FontWeight.Bold)
            }
        }

        if (isSuccess) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = ScholarEmerald.copy(alpha = 0.15f))
                ) {
                    Text(
                        text = "✅ बधाई! नया अध्याय सफलतापूर्वक प्रकाशित हो गया और छात्रों की ऐप में दिखने लगा है।",
                        color = ScholarEmerald,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AddQuestionTab(viewModel: MainViewModel) {
    var qText by remember { mutableStateOf("") }
    var optA by remember { mutableStateOf("") }
    var optB by remember { mutableStateOf("") }
    var optC by remember { mutableStateOf("") }
    var optD by remember { mutableStateOf("") }
    var correctOpt by remember { mutableStateOf("A") }
    var explanation by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(text = "नया MCQ प्रश्न जोड़ें", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        item {
            OutlinedTextField(
                value = qText,
                onValueChange = { qText = it },
                label = { Text("प्रश्न (Question)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
        }

        item {
            OutlinedTextField(value = optA, onValueChange = { optA = it }, label = { Text("विकल्प A") }, modifier = Modifier.fillMaxWidth())
        }
        item {
            OutlinedTextField(value = optB, onValueChange = { optB = it }, label = { Text("विकल्प B") }, modifier = Modifier.fillMaxWidth())
        }
        item {
            OutlinedTextField(value = optC, onValueChange = { optC = it }, label = { Text("विकल्प C") }, modifier = Modifier.fillMaxWidth())
        }
        item {
            OutlinedTextField(value = optD, onValueChange = { optD = it }, label = { Text("विकल्प D") }, modifier = Modifier.fillMaxWidth())
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("A", "B", "C", "D").forEach { opt ->
                    FilterChip(
                        selected = correctOpt == opt,
                        onClick = { correctOpt = opt },
                        label = { Text("सही: $opt") }
                    )
                }
            }
        }

        item {
            OutlinedTextField(
                value = explanation,
                onValueChange = { explanation = it },
                label = { Text("विस्तृत व्याख्या (Explanation)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
        }

        item {
            Button(
                onClick = {
                    if (qText.isNotBlank()) {
                        val q = QuestionEntity(
                            id = "q_admin_${System.currentTimeMillis()}",
                            chapterId = "c10_sci_ch1",
                            subjectId = "c10_sci",
                            studentClass = "10",
                            questionType = "MCQ",
                            questionText = qText,
                            optionA = optA,
                            optionB = optB,
                            optionC = optC,
                            optionD = optD,
                            correctAnswer = correctOpt,
                            explanation = explanation.ifBlank { "सही विकल्प $correctOpt है।" }
                        )
                        viewModel.adminAddQuestion(q)
                        isSuccess = true
                        qText = ""
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ScholarPrimary)
            ) {
                Text("प्रश्न सेव करें (Save Question)")
            }
        }

        if (isSuccess) {
            item {
                Text("✅ प्रश्न सफलतापूर्वक जोड़ दिया गया!", color = ScholarEmerald, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun BroadcastNotificationTab(viewModel: MainViewModel) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(text = "विद्यार्थियों को नई सूचना भेजें (Broadcast Notification)", fontWeight = FontWeight.Bold, fontSize = 16.sp)

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("शीर्षक (Title)") },
            placeholder = { Text("उदा. 📚 कक्षा 10 विज्ञान का नया अध्याय उपलब्ध!") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("संदेश (Message)") },
            placeholder = { Text("प्रिय विद्यार्थियों, बोर्ड परीक्षा की तैयारी हेतु नए नोट्स अपलोड हो चुके हैं।") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Button(
            onClick = {
                if (title.isNotBlank()) {
                    viewModel.adminBroadcastNotification(title, message, "general")
                    isSuccess = true
                    title = ""
                    message = ""
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ScholarGold)
        ) {
            Icon(Icons.Default.Campaign, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Broadcast Notification")
        }

        if (isSuccess) {
            Text("✅ सूचना सभी विद्यार्थियों को प्रेषित कर दी गई है!", color = ScholarEmerald, fontWeight = FontWeight.Bold)
        }
    }
}
