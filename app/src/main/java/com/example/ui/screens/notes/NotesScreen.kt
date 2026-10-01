package com.example.ui.screens.notes

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.MainViewModel
import com.example.data.local.entity.NoteEntity
import com.example.ui.components.SinhaTopAppBar
import com.example.ui.theme.ScholarEmerald
import com.example.ui.theme.ScholarGold
import com.example.ui.theme.ScholarPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    chapterId: String,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var currentChapterId by remember(chapterId) { mutableStateOf(chapterId.ifBlank { "c10_sci_ch1" }) }

    val notesFlow = remember(currentChapterId) { viewModel.repository.getNotesByChapter(currentChapterId) }
    val notes by notesFlow.collectAsState(initial = emptyList())
    val chapterFlow = remember(currentChapterId) { viewModel.repository.getChapterFlow(currentChapterId) }
    val chapter by chapterFlow.collectAsState(initial = null)
    val allChapters by viewModel.repository.getAllChaptersFlow().collectAsState(initial = emptyList())

    val isSpeaking by viewModel.isSpeaking.collectAsState()

    var selectedFilter by remember { mutableStateOf("all") }
    var searchQuery by remember { mutableStateOf("") }
    var showChapterPickerSheet by remember { mutableStateOf(false) }

    val allNotes = if (notes.isNotEmpty()) {
        notes
    } else if (chapter != null) {
        listOf(
            NoteEntity(
                id = "gen_note_1_${chapter!!.id}",
                chapterId = chapter!!.id,
                subjectId = chapter!!.subjectId,
                title = "⚡ त्वरित पुनरावलोकन (Quick Revision Notes)",
                noteType = "short",
                content = "• ${chapter!!.summary}\n• ${chapter!!.introduction}"
            ),
            NoteEntity(
                id = "gen_note_2_${chapter!!.id}",
                chapterId = chapter!!.id,
                subjectId = chapter!!.subjectId,
                title = "⭐ महत्वपूर्ण अध्ययन बिंदु (Important Points)",
                noteType = "important_points",
                content = chapter!!.learningObjectives
            ),
            NoteEntity(
                id = "gen_note_3_${chapter!!.id}",
                chapterId = chapter!!.id,
                subjectId = chapter!!.subjectId,
                title = "📌 परीक्षा विशेष सारांश (Board Exam Special)",
                noteType = "exam_notes",
                content = "इस अध्याय के मुख्य सूत्र, परिभाषाएं एवं वस्तुनिष्ठ प्रश्नों का गहन अध्ययन करें।"
            )
        )
    } else emptyList()

    val filteredNotes = allNotes
        .filter { selectedFilter == "all" || it.noteType == selectedFilter }
        .filter {
            searchQuery.isBlank() ||
            it.title.contains(searchQuery, ignoreCase = true) ||
            it.content.contains(searchQuery, ignoreCase = true)
        }

    Scaffold(
        topBar = {
            SinhaTopAppBar(
                title = "हस्तलिखित व परीक्षा नोट्स",
                subtitle = chapter?.let { "अध्याय ${it.chapterNumber}: ${it.titleHindi}" } ?: "Complete & Short Revision Notes",
                showBackButton = true,
                onBackClick = onBack,
                actions = {
                    // TTS Read Aloud
                    IconButton(
                        onClick = {
                            if (isSpeaking) {
                                viewModel.stopSpeaking()
                            } else {
                                val text = filteredNotes.joinToString(". ") { "${it.title}: ${it.content}" }
                                viewModel.speakText(text)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                            contentDescription = "Read Notes",
                            tint = if (isSpeaking) ScholarEmerald else MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Chapter switcher
                    IconButton(onClick = { showChapterPickerSheet = true }) {
                        Icon(Icons.Default.MenuBook, contentDescription = "Change Chapter")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Search inside notes
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("नोट्स में खोजें (Search notes)...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Category Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == "all",
                    onClick = { selectedFilter = "all" },
                    label = { Text("सभी (All)") }
                )
                FilterChip(
                    selected = selectedFilter == "short",
                    onClick = { selectedFilter = "short" },
                    label = { Text("⚡ शॉर्ट नोट्स") }
                )
                FilterChip(
                    selected = selectedFilter == "important_points",
                    onClick = { selectedFilter = "important_points" },
                    label = { Text("⭐ मुख्य बिंदु") }
                )
                FilterChip(
                    selected = selectedFilter == "exam_notes",
                    onClick = { selectedFilter = "exam_notes" },
                    label = { Text("📌 परीक्षा स्पेशल") }
                )
            }

            if (filteredNotes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "कोई नोट्स नहीं मिला।",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(filteredNotes) { note ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("note_card_${note.id}"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = note.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = ScholarPrimary,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Row {
                                        IconButton(
                                            onClick = {
                                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                clipboard.setPrimaryClip(ClipData.newPlainText("Note", "${note.title}\n\n${note.content}"))
                                                Toast.makeText(context, "नोट्स कॉपी कर लिया गया!", Toast.LENGTH_SHORT).show()
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ContentCopy,
                                                contentDescription = "Copy",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        IconButton(onClick = { viewModel.toggleBookmark("note", note.id, note.title, "Notes") }) {
                                            Icon(
                                                imageVector = if (note.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                                contentDescription = "Bookmark",
                                                tint = if (note.isBookmarked) ScholarGold else MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = note.content,
                                    style = MaterialTheme.typography.bodyMedium,
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Chapter Picker Sheet
    if (showChapterPickerSheet) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { showChapterPickerSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "अध्याय चुनें (Select Chapter)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().height(360.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(allChapters) { ch ->
                        val isSelected = ch.id == currentChapterId
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    currentChapterId = ch.id
                                    showChapterPickerSheet = false
                                },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "कक्षा ${ch.studentClass} • अ.${ch.chapterNumber}",
                                    fontWeight = FontWeight.Bold,
                                    color = ScholarPrimary,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = ch.titleHindi,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 14.sp,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
