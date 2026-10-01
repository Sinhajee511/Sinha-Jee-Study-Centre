package com.example.ui.screens.pyq

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.MainViewModel
import com.example.data.local.entity.QuestionEntity
import com.example.ui.components.SinhaTopAppBar
import com.example.ui.theme.ScholarEmerald
import com.example.ui.theme.ScholarGold
import com.example.ui.theme.ScholarPrimary

@Composable
fun PreviousYearQuestionsScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val pyqs by viewModel.pyqs.collectAsState()
    val user by viewModel.currentUser.collectAsState()
    var selectedYear by remember { mutableStateOf<Int?>(null) }

    val filtered = if (selectedYear == null) {
        pyqs
    } else {
        pyqs.filter { it.year == selectedYear }
    }

    Scaffold(
        topBar = {
            SinhaTopAppBar(
                title = "विगत वर्षों के प्रश्न (PYQ Bank)",
                subtitle = "कक्षा ${user?.studentClass ?: "10"} • बिहार बोर्ड / स्टेट बोर्ड",
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
            // Year filter chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedYear == null,
                    onClick = { selectedYear = null },
                    label = { Text("सभी वर्ष") }
                )
                listOf(2025, 2024, 2023, 2022).forEach { year ->
                    FilterChip(
                        selected = selectedYear == year,
                        onClick = { selectedYear = year },
                        label = { Text("$year Board") }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filtered) { q ->
                    PyqItemCard(question = q)
                }
            }
        }
    }
}

@Composable
private fun PyqItemCard(question: QuestionEntity) {
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
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ScholarGold.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "बिहार बोर्ड ${question.year ?: 2024}",
                        style = MaterialTheme.typography.labelSmall,
                        color = ScholarGold,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = question.questionText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Options if MCQ
            if (question.optionA != null) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "A. ${question.optionA}", fontSize = 13.sp)
                    Text(text = "B. ${question.optionB}", fontSize = 13.sp)
                    Text(text = "C. ${question.optionC}", fontSize = 13.sp)
                    Text(text = "D. ${question.optionD}", fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Solution box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(ScholarEmerald.copy(alpha = 0.1f))
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "सही उत्तर: ${question.correctAnswer}",
                        fontWeight = FontWeight.Bold,
                        color = ScholarEmerald
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "व्याख्या: ${question.explanation}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
