package com.example.ui.screens.auth

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
fun LoginScreen(
    onLoginSuccess: (name: String, phone: String, studentClass: String, board: String, medium: String, stream: String) -> Unit
) {
    var name by remember { mutableStateOf("Aman Sinha") }
    var phone by remember { mutableStateOf("9876543210") }
    var otp by remember { mutableStateOf("123456") }
    var isOtpSent by remember { mutableStateOf(false) }

    var selectedClass by remember { mutableStateOf("10") }
    var selectedStream by remember { mutableStateOf("Science") }
    var selectedBoard by remember { mutableStateOf("Bihar Board (BSEB)") }
    var selectedMedium by remember { mutableStateOf("Hindi") }

    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Branding Emblem
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(ScholarPrimary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "Logo",
                    tint = ScholarPrimary,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "SINHA JEE STUDY CENTRE",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = ScholarPrimary,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Digital Textbook • Notes • Questions • Practice",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = if (!isOtpSent) "Student Registration / Login" else "OTP Verification",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "प्रवेश अनिवार्य है • अपनी अध्ययन सामग्री तक तुरंत पहुंचें",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (!isOtpSent) {
                        // Name
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("विद्यार्थी का नाम (Full Name)") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_name_input"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Phone
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("मोबाइल नंबर (Mobile Number)") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_phone_input"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Class Choice
                        Text(
                            text = "कक्षा चुनें (Select Class):",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            FilterChip(
                                selected = selectedClass == "10",
                                onClick = { selectedClass = "10" },
                                label = { Text("Class 10 (मैट्रिक)") },
                                modifier = Modifier.testTag("class_10_chip")
                            )
                            FilterChip(
                                selected = selectedClass == "12",
                                onClick = { selectedClass = "12" },
                                label = { Text("Class 12 (इंटरमीडिएट)") },
                                modifier = Modifier.testTag("class_12_chip")
                            )
                        }

                        // Stream if Class 12
                        if (selectedClass == "12") {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "संकाय (Stream):",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("Science", "Arts", "Commerce").forEach { stream ->
                                    FilterChip(
                                        selected = selectedStream == stream,
                                        onClick = { selectedStream = stream },
                                        label = { Text(stream) }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Board Choice
                        Text(
                            text = "बोर्ड चुनें (Select Board):",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Bihar Board (BSEB)", "CBSE", "State Board").forEach { board ->
                                FilterChip(
                                    selected = selectedBoard == board,
                                    onClick = { selectedBoard = board },
                                    label = { Text(board, fontSize = 12.sp) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Medium Choice
                        Text(
                            text = "माध्यम (Medium):",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            FilterChip(
                                selected = selectedMedium == "Hindi",
                                onClick = { selectedMedium = "Hindi" },
                                label = { Text("हिंदी (Hindi)") }
                            )
                            FilterChip(
                                selected = selectedMedium == "English",
                                onClick = { selectedMedium = "English" },
                                label = { Text("English") }
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                if (phone.isNotBlank() && name.isNotBlank()) {
                                    isOtpSent = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("send_otp_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ScholarPrimary)
                        ) {
                            Text("Get OTP (ओटीपी प्राप्त करें) →", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        // OTP View
                        Text(
                            text = "$phone पर 6 अंकों का सत्यापन कोड भेजा गया है",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = otp,
                            onValueChange = { otp = it },
                            label = { Text("Enter OTP") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_otp_input"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "💡 डेमो टेस्ट कोड: 123456",
                            style = MaterialTheme.typography.labelSmall,
                            color = ScholarGold
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                onLoginSuccess(
                                    name,
                                    phone,
                                    selectedClass,
                                    selectedBoard,
                                    selectedMedium,
                                    if (selectedClass == "10") "General" else selectedStream
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("verify_otp_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ScholarPrimary)
                        ) {
                            Text("Verify & Continue (सत्यापित करें) ✓", fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        TextButton(
                            onClick = { isOtpSent = false },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("← Edit Information (जानकारी बदलें)")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "By continuing, you agree to our Terms & Privacy Policy.\nSINHA JEE STUDY CENTRE • Empowering Students",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
