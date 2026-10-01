package com.example.ui.screens.textbook

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.ReadingTheme
import com.example.data.local.entity.ChapterEntity
import com.example.ui.components.WatermarkOverlay
import com.example.ui.theme.ScholarEmerald
import com.example.ui.theme.ScholarGold
import com.example.ui.theme.ScholarPrimary
import com.example.ui.theme.SepiaBackground
import com.example.ui.theme.SepiaBorder
import com.example.ui.theme.SepiaSurface
import com.example.ui.theme.SepiaText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DigitalTextbookReaderScreen(
    chapterId: String,
    subjectTitle: String,
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit
) {
    val chapterFlow = remember(chapterId) { viewModel.repository.getChapterFlow(chapterId) }
    val chapter by chapterFlow.collectAsState(initial = null)

    val fontSize by viewModel.fontSize.collectAsState()
    val readingTheme by viewModel.readingTheme.collectAsState()
    val isFullscreen by viewModel.isFullscreen.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    var showSettingsSheet by remember { mutableStateOf(false) }
    var showSearchInChapter by remember { mutableStateOf(false) }
    var searchKeyword by remember { mutableStateOf("") }
    var showAiAssistantSheet by remember { mutableStateOf(false) }
    var showTocSheet by remember { mutableStateOf(false) }
    var isBookmarked by remember { mutableStateOf(false) }

    val subjectChaptersFlow = remember(chapter?.subjectId) {
        val sId = chapter?.subjectId
        if (sId != null) viewModel.repository.getChaptersBySubject(sId)
        else kotlinx.coroutines.flow.flowOf(emptyList())
    }
    val subjectChapters by subjectChaptersFlow.collectAsState(initial = emptyList())
    val currentChapterIndex = subjectChapters.indexOfFirst { it.id == chapterId }
    val prevChapter = if (currentChapterIndex > 0) subjectChapters[currentChapterIndex - 1] else null
    val nextChapter = if (currentChapterIndex >= 0 && currentChapterIndex < subjectChapters.size - 1) subjectChapters[currentChapterIndex + 1] else null

    // Check bookmark
    LaunchedEffect(chapterId) {
        isBookmarked = viewModel.repository.isBookmarked(chapterId)
    }

    // Auto save progress on entering/scrolling
    LaunchedEffect(chapter) {
        chapter?.let {
            viewModel.saveReadingProgress(it.id, 80, it.lastReadSection, it.lastReadPage)
        }
    }

    // Theme color mappings
    val backgroundColor = when (readingTheme) {
        ReadingTheme.DAY -> Color.White
        ReadingTheme.SEPIA -> SepiaBackground
        ReadingTheme.NIGHT -> Color(0xFF121212)
    }
    val contentTextColor = when (readingTheme) {
        ReadingTheme.DAY -> Color(0xFF1E293B)
        ReadingTheme.SEPIA -> SepiaText
        ReadingTheme.NIGHT -> Color(0xFFE2E8F0)
    }
    val cardBg = when (readingTheme) {
        ReadingTheme.DAY -> Color(0xFFF8FAFC)
        ReadingTheme.SEPIA -> SepiaSurface
        ReadingTheme.NIGHT -> Color(0xFF1E293B)
    }

    Scaffold(
        topBar = {
            if (!isFullscreen) {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = subjectTitle,
                                style = MaterialTheme.typography.titleSmall,
                                color = ScholarPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "अध्याय ${chapter?.chapterNumber ?: 1}: ${chapter?.titleHindi ?: ""}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack, modifier = Modifier.testTag("reader_back_button")) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        // TTS Read Aloud
                        IconButton(
                            onClick = {
                                if (isSpeaking) {
                                    viewModel.stopSpeaking()
                                } else {
                                    val textToRead = "${chapter?.titleHindi}. ${chapter?.introduction}. ${chapter?.textbookContent}"
                                    viewModel.speakText(textToRead)
                                }
                            },
                            modifier = Modifier.testTag("reader_tts_button")
                        ) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                                contentDescription = "Read Aloud",
                                tint = if (isSpeaking) ScholarEmerald else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Search inside chapter
                        IconButton(onClick = { showSearchInChapter = !showSearchInChapter }) {
                            Icon(Icons.Default.Search, contentDescription = "Search inside")
                        }

                        // Text Size & Display Settings
                        IconButton(onClick = { showSettingsSheet = true }) {
                            Icon(Icons.Default.FormatSize, contentDescription = "Display Settings")
                        }

                        // Bookmark
                        IconButton(
                            onClick = {
                                chapter?.let {
                                    viewModel.toggleBookmark("chapter", it.id, it.titleHindi, subjectTitle)
                                    isBookmarked = !isBookmarked
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (isBookmarked) ScholarGold else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Fullscreen
                        IconButton(onClick = { viewModel.toggleFullscreen() }) {
                            Icon(Icons.Default.Fullscreen, contentDescription = "Toggle Fullscreen")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
                )
            }
        },
        bottomBar = {
            if (!isFullscreen) {
                // Bottom Chapter Navigation
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                if (prevChapter != null) {
                                    onNavigate("textbook_reader/${prevChapter.id}/${prevChapter.titleHindi}")
                                } else {
                                    onBack()
                                }
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(if (prevChapter != null) "← पिछला (${prevChapter.chapterNumber})" else "← सूची")
                        }

                        Button(
                            onClick = { showTocSheet = true },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("विषय सूची", color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp)
                        }

                        Button(
                            onClick = { showAiAssistantSheet = true },
                            colors = ButtonDefaults.buttonColors(containerColor = ScholarPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("AI", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                if (nextChapter != null) {
                                    onNavigate("textbook_reader/${nextChapter.id}/${nextChapter.titleHindi}")
                                } else {
                                    onBack()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ScholarEmerald),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(if (nextChapter != null) "अगला (${nextChapter.chapterNumber}) →" else "पूर्ण ✓")
                        }
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(backgroundColor)
        ) {
            // Mandatory Educational Watermark
            WatermarkOverlay(text = "SINHA JEE STUDY CENTRE")

            Column(modifier = Modifier.fillMaxSize()) {
                // Search inside chapter bar
                AnimatedVisibility(visible = showSearchInChapter) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = searchKeyword,
                                onValueChange = { searchKeyword = it },
                                placeholder = { Text("अध्याय में खोजें (जैसे: परिभाषा, सूत्र...)") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = { showSearchInChapter = false }) {
                                Text("✕")
                            }
                        }
                    }
                }

                // Chapter Tabs (Textbook / Notes / Questions / Quiz / Video / AI)
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = ScholarPrimary,
                    edgePadding = 12.dp
                ) {
                    val tabs = listOf(
                        "📖 Textbook" to "पाठ्यपुस्तक",
                        "📝 Notes" to "नोट्स",
                        "❓ Questions" to "प्रश्न बैंक",
                        "🎯 Quiz" to "क्विज़",
                        "🤖 AI Assistant" to "एआई सहायक"
                    )
                    tabs.forEachIndexed { index, (en, hi) ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = "$hi ($en)",
                                    fontSize = 12.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }

                // Content View according to selected tab
                val listState = rememberLazyListState()

                when (selectedTab) {
                    0 -> {
                        // Main Textbook Reader
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp),
                            contentPadding = PaddingValues(vertical = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Header Banner
                            item {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = subjectTitle,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = ScholarPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "अध्याय ${chapter?.chapterNumber ?: 1}: ${chapter?.titleHindi ?: ""}",
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = contentTextColor
                                    )
                                    Text(
                                        text = chapter?.titleEnglish ?: "",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = contentTextColor.copy(alpha = 0.7f)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    HorizontalDivider()
                                }
                            }

                            // Introduction
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = cardBg)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = "📌 प्रस्तावना (Introduction)",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = ScholarPrimary
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = chapter?.introduction ?: "",
                                            fontSize = fontSize.sp,
                                            lineHeight = (fontSize * 1.5).sp,
                                            color = contentTextColor
                                        )
                                    }
                                }
                            }

                            // Learning Objectives
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = ScholarEmerald.copy(alpha = 0.08f))
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = "🎯 अध्ययन के उद्देश्य (Learning Objectives)",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = ScholarEmerald
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = chapter?.learningObjectives ?: "",
                                            fontSize = (fontSize - 1).sp,
                                            lineHeight = (fontSize * 1.45).sp,
                                            color = contentTextColor
                                        )
                                    }
                                }
                            }

                            // Main Textbook Body
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Text(
                                        text = "📖 मुख्य पाठ्य सामग्री (Textbook Content)",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = ScholarPrimary
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Render rich textbook paragraphs
                                    val paragraphs = (chapter?.textbookContent ?: "").split("\n\n")
                                    paragraphs.forEach { para ->
                                        if (para.startsWith("###")) {
                                            Text(
                                                text = para.removePrefix("###").trim(),
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = ScholarPrimary,
                                                modifier = Modifier.padding(top = 16.dp, bottom = 6.dp)
                                            )
                                        } else if (para.startsWith(">")) {
                                            // Definition or Formula box
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(ScholarGold.copy(alpha = 0.12f))
                                                    .padding(14.dp)
                                            ) {
                                                Text(
                                                    text = para.removePrefix(">").trim(),
                                                    fontSize = fontSize.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = contentTextColor,
                                                    lineHeight = (fontSize * 1.5).sp
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(10.dp))
                                        } else {
                                            Text(
                                                text = para,
                                                fontSize = fontSize.sp,
                                                lineHeight = (fontSize * 1.6).sp,
                                                color = contentTextColor,
                                                modifier = Modifier.padding(bottom = 12.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            // Chapter Summary Box
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = cardBg)
                                ) {
                                    Column(modifier = Modifier.padding(18.dp)) {
                                        Text(
                                            text = "📝 अध्याय का सारांश (Chapter Summary)",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = ScholarPrimary
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = chapter?.summary ?: "",
                                            fontSize = fontSize.sp,
                                            lineHeight = (fontSize * 1.5).sp,
                                            color = contentTextColor
                                        )
                                    }
                                }
                            }

                            // End watermark acknowledgment
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "— SINHA JEE STUDY CENTRE DIGITAL TEXTBOOK —",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "सर्वाधिकार सुरक्षित • बिहार बोर्ड एवं अन्य राज्य बोर्ड हेतु",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    1 -> {
                        // Quick embedded Notes
                        EmbeddedNotesView(
                            chapterId = chapterId,
                            viewModel = viewModel,
                            fontSize = fontSize
                        )
                    }

                    2 -> {
                        // Quick embedded Questions
                        EmbeddedQuestionsView(
                            chapterId = chapterId,
                            viewModel = viewModel
                        )
                    }

                    3 -> {
                        // Quick Chapter Quiz Launcher
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Quiz,
                                contentDescription = null,
                                tint = ScholarPrimary,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "इस अध्याय का ऑनलाइन टेस्ट (Chapter Quiz)",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "परीक्षा जैसे वातावरण में टाइमर के साथ 10 महत्वपूर्ण MCQs हल करें और अपना स्कोर देखें।",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = { onNavigate("quiz/${chapter?.subjectId ?: "all"}/$chapterId") },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ScholarPrimary),
                                modifier = Modifier.fillMaxWidth().height(50.dp)
                            ) {
                                Text("क्विज़ शुरू करें (Start Quiz) →", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    4 -> {
                        // AI Study Assistant integrated
                        EmbeddedAiAssistantView(
                            chapter = chapter,
                            viewModel = viewModel
                        )
                    }
                }
            }

            // Floating exit fullscreen button if fullscreen
            if (isFullscreen) {
                IconButton(
                    onClick = { viewModel.toggleFullscreen() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(Icons.Default.FullscreenExit, contentDescription = "Exit Fullscreen", tint = Color.White)
                }
            }
        }
    }

    // Display & Reader Settings Sheet
    if (showSettingsSheet) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { showSettingsSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "पठन सेटिंग्स (Reading Settings)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Font size slider
                Text(text = "फ़ॉन्ट आकार (Font Size): ${fontSize.toInt()}sp", fontWeight = FontWeight.SemiBold)
                Slider(
                    value = fontSize,
                    onValueChange = { viewModel.setFontSize(it) },
                    valueRange = 14f..26f,
                    steps = 6
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Theme switcher: Day, Sepia, Night
                Text(text = "पृष्ठभूमि मोड (Reading Mode):", fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AssistChip(
                        onClick = { viewModel.setReadingTheme(ReadingTheme.DAY) },
                        label = { Text("☀️ Day (सफेद)") },
                        modifier = Modifier.weight(1f)
                    )
                    AssistChip(
                        onClick = { viewModel.setReadingTheme(ReadingTheme.SEPIA) },
                        label = { Text("📜 Sepia (सेपिया)") },
                        modifier = Modifier.weight(1f)
                    )
                    AssistChip(
                        onClick = { viewModel.setReadingTheme(ReadingTheme.NIGHT) },
                        label = { Text("🌙 Night (रात्रि)") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // AI Study Assistant Bottom Sheet
    if (showAiAssistantSheet) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { showAiAssistantSheet = false },
            sheetState = sheetState
        ) {
            EmbeddedAiAssistantView(
                chapter = chapter,
                viewModel = viewModel,
                modifier = Modifier.padding(16.dp)
            )
        }
    }

    // Table of Contents Sheet
    if (showTocSheet) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { showTocSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "विषय सूची (Table of Contents)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$subjectTitle के सभी अध्याय",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(14.dp))
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().height(350.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(subjectChapters) { ch ->
                        val isCurrent = ch.id == chapterId
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showTocSheet = false
                                    if (!isCurrent) {
                                        onNavigate("textbook_reader/${ch.id}/${ch.titleHindi}")
                                    }
                                },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isCurrent) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "अध्याय ${ch.chapterNumber}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = ScholarPrimary
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = ch.titleHindi,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium
                                    )
                                    Text(
                                        text = ch.titleEnglish,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (isCurrent) {
                                    Text(
                                        text = "वर्तमान में पढ़ रहे हैं",
                                        fontSize = 10.sp,
                                        color = ScholarEmerald,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmbeddedNotesView(
    chapterId: String,
    viewModel: MainViewModel,
    fontSize: Float
) {
    val notesFlow = remember(chapterId) { viewModel.repository.getNotesByChapter(chapterId) }
    val notes by notesFlow.collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(notes) { note ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = note.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ScholarPrimary
                        )
                        IconButton(onClick = { viewModel.toggleBookmark("note", note.id, note.title, "Notes") }) {
                            Icon(
                                imageVector = if (note.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = null,
                                tint = if (note.isBookmarked) ScholarGold else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = note.content,
                        fontSize = fontSize.sp,
                        lineHeight = (fontSize * 1.5).sp
                    )
                }
            }
        }
    }
}

@Composable
private fun EmbeddedQuestionsView(
    chapterId: String,
    viewModel: MainViewModel
) {
    val questionsFlow = remember(chapterId) { viewModel.repository.getQuestionsByChapter(chapterId) }
    val questions by questionsFlow.collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(questions) { q ->
            var selectedOption by remember { mutableStateOf<String?>(null) }
            var isSubmitted by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = ScholarPrimary.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = q.questionType,
                            style = MaterialTheme.typography.labelSmall,
                            color = ScholarPrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = q.questionText,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (q.questionType == "MCQ" && q.optionA != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        val options = listOf(
                            "A" to q.optionA,
                            "B" to q.optionB,
                            "C" to q.optionC,
                            "D" to q.optionD
                        )

                        options.forEach { (key, optText) ->
                            if (optText != null) {
                                val isSelected = selectedOption == key
                                val isCorrectOption = q.correctAnswer.equals(key, ignoreCase = true)
                                val optBg = when {
                                    isSubmitted && isCorrectOption -> ScholarEmerald.copy(alpha = 0.2f)
                                    isSubmitted && isSelected && !isCorrectOption -> Color.Red.copy(alpha = 0.15f)
                                    isSelected -> ScholarPrimary.copy(alpha = 0.15f)
                                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(optBg)
                                        .clickable(enabled = !isSubmitted) { selectedOption = key }
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "$key.",
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.width(24.dp)
                                    )
                                    Text(text = optText, modifier = Modifier.weight(1f))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (!isSubmitted) {
                            Button(
                                onClick = { isSubmitted = true },
                                enabled = selectedOption != null,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("उत्तर जांचें (Check Answer)")
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "सही उत्तर: ${q.correctAnswer}",
                                        fontWeight = FontWeight.Bold,
                                        color = ScholarEmerald
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = "व्याख्या: ${q.explanation}", fontSize = 13.sp)
                                }
                            }
                        }
                    } else {
                        // Subjective question
                        Spacer(modifier = Modifier.height(10.dp))
                        var showAnswer by remember { mutableStateOf(false) }
                        if (!showAnswer) {
                            OutlinedButton(
                                onClick = { showAnswer = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("आदर्श उत्तर देखें (Show Answer)")
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text(text = "उत्तर: ${q.correctAnswer}", fontWeight = FontWeight.SemiBold)
                                    if (q.explanation.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = "विवरण: ${q.explanation}", fontSize = 13.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmbeddedAiAssistantView(
    chapter: ChapterEntity?,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val aiResponse by viewModel.aiResponse.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()
    var customQuery by remember { mutableStateOf("") }

    val chapterContext = "${chapter?.titleHindi} (${chapter?.titleEnglish}): ${chapter?.introduction}"

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = ScholarPrimary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "AI Study Assistant (एआई अध्ययन सहायक)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "पाठ्यपुस्तक पर आधारित सटीक एवं सरल समाधान",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Quick Prompt Suggestions
        Text(text = "त्वरित प्रश्न (Quick Prompts):", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            AssistChip(
                onClick = { viewModel.askAiAssistant("Explain this topic simply in Hindi", chapterContext) },
                label = { Text("सरल भाषा में समझाएं", fontSize = 11.sp) }
            )
            AssistChip(
                onClick = { viewModel.askAiAssistant("Give a real-life example of this topic", chapterContext) },
                label = { Text("दैनिक उदाहरण दें", fontSize = 11.sp) }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            AssistChip(
                onClick = { viewModel.askAiAssistant("Explain important formulas in this chapter", chapterContext) },
                label = { Text("सूत्र समझाएं", fontSize = 11.sp) }
            )
            AssistChip(
                onClick = { viewModel.askAiAssistant("Summarize this chapter for exam revision", chapterContext) },
                label = { Text("अध्याय का सारांश", fontSize = 11.sp) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = customQuery,
                onValueChange = { customQuery = it },
                placeholder = { Text("कुछ भी पूछें (Ask anything)...") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (customQuery.isNotBlank()) {
                        viewModel.askAiAssistant(customQuery, chapterContext)
                        customQuery = ""
                    }
                },
                enabled = !isAiLoading && customQuery.isNotBlank()
            ) {
                Text("पूछें")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isAiLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = ScholarPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "अध्ययन सामग्री का विश्लेषण हो रहा है...", fontSize = 12.sp)
                }
            }
        } else if (aiResponse != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = aiResponse ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(onClick = { viewModel.clearAiResponse() }) {
                            Text("साफ करें (Clear)")
                        }
                    }
                }
            }
        }
    }
}
