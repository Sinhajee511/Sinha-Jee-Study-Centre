package com.example.ui.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.ScholarGold
import com.example.ui.theme.ScholarPrimary

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit
) {
    var currentPage by remember { mutableIntStateOf(0) }

    val slides = listOf(
        OnboardingSlide(
            title = "Read Your Textbooks Digitally",
            subtitleHindi = "डिजिटल पाठ्यपुस्तकें एवं संपूर्ण नोट्स",
            description = "कक्षा 10 और 12 के छात्रों के लिए बिहार बोर्ड एवं अन्य राज्य बोर्डों की आधिकारिक एवं प्रमाणित पाठ्यपुस्तकें अब आधुनिक डिजिटल रूप में।",
            icon = Icons.Default.AutoStories,
            accentColor = ScholarPrimary
        ),
        OnboardingSlide(
            title = "Practice Questions & Quizzes",
            subtitleHindi = "अध्यायवार प्रश्न बैंक और लाइव क्विज़",
            description = "MCQs, लघु उत्तरीय, दीर्घ उत्तरीय प्रश्न, और पिछले वर्षों के प्रश्न (PYQ) विस्तृत व्याख्या के साथ हल करें।",
            icon = Icons.Default.Quiz,
            accentColor = ScholarGold
        ),
        OnboardingSlide(
            title = "Track Your Learning Progress",
            subtitleHindi = "अपनी अध्ययन प्रगति और स्कोर देखें",
            description = "दैनिक अध्ययन लक्ष्य, अध्यायवार प्रगति बार, और स्मार्ट रिवीजन सुझावों के साथ बोर्ड परीक्षा में अव्वल आएं।",
            icon = Icons.Default.Equalizer,
            accentColor = Color(0xFF2E7D32)
        )
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "SINHA JEE STUDY CENTRE",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = ScholarPrimary,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Digital Textbook • Notes • Practice",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (currentPage < slides.size - 1) {
                    OutlinedButton(
                        onClick = onFinished,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.testTag("skip_onboarding_button")
                    ) {
                        Text("Skip")
                    }
                }
            }

            // Slide content
            AnimatedContent(
                targetState = currentPage,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "OnboardingSlide"
            ) { page ->
                val slide = slides[page]
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(slide.accentColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = slide.icon,
                            contentDescription = null,
                            modifier = Modifier.size(72.dp),
                            tint = slide.accentColor
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = slide.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = slide.subtitleHindi,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = slide.accentColor,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = slide.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                }
            }

            // Indicators & Action buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dots
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    slides.indices.forEach { index ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .height(8.dp)
                                .width(if (index == currentPage) 24.dp else 8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    if (index == currentPage) ScholarPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = {
                        if (currentPage < slides.size - 1) {
                            currentPage++
                        } else {
                            onFinished()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("start_learning_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ScholarPrimary)
                ) {
                    Text(
                        text = if (currentPage < slides.size - 1) "Next →" else "Start Learning (पढ़ाई शुरू करें) →",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

private data class OnboardingSlide(
    val title: String,
    val subtitleHindi: String,
    val description: String,
    val icon: ImageVector,
    val accentColor: Color
)
