package com.example.ui.screens.profile

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.MainViewModel
import com.example.ui.components.SinhaBottomNavigation
import com.example.ui.components.SinhaTopAppBar
import com.example.ui.theme.ScholarGold
import com.example.ui.theme.ScholarPrimary

@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit
) {
    val user by viewModel.currentUser.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }
    var editName by remember(user?.name) { mutableStateOf(user?.name ?: "") }
    var editBoard by remember(user?.board) { mutableStateOf(user?.board ?: "Bihar Board (BSEB)") }
    var editMedium by remember(user?.medium) { mutableStateOf(user?.medium ?: "Hindi") }

    Scaffold(
        topBar = {
            SinhaTopAppBar(
                title = "विद्यार्थी प्रोफ़ाइल (Student Profile)",
                subtitle = "SINHA JEE STUDY CENTRE"
            )
        },
        bottomBar = {
            SinhaBottomNavigation(currentRoute = "profile", onNavigate = onNavigate)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(ScholarPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = user?.name?.take(1) ?: "A",
                                color = Color.White,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = user?.name ?: "विद्यार्थी का नाम",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "+91 ${user?.phone ?: "9876543210"}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "कक्षा ${user?.studentClass ?: "10"} • ${user?.board ?: "Bihar Board"} • ${user?.medium ?: "Hindi"} Medium",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = ScholarPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        androidx.compose.material3.OutlinedButton(
                            onClick = { showEditDialog = true },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("✏️ प्रोफ़ाइल संपादित करें (Edit Profile)")
                        }
                    }
                }
            }

            // Quick Switch Class & Stream
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "कक्षा एवं संकाय बदलें (Switch Class):", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = { viewModel.switchClass("10") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Class 10")
                            }
                            Button(
                                onClick = { viewModel.switchClass("12") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Class 12")
                            }
                        }
                    }
                }
            }

            // Menu Items
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        ProfileMenuItem(
                            icon = Icons.Default.Bookmark,
                            title = "सहेजे गए पाठ (My Bookmarks)",
                            onClick = { onNavigate("bookmarks") }
                        )
                        ProfileMenuItem(
                            icon = Icons.Default.Download,
                            title = "डाउनलोड की गई सामग्री (Downloads)",
                            onClick = { onNavigate("downloads") }
                        )
                        ProfileMenuItem(
                            icon = Icons.Default.Notifications,
                            title = "सूचनाएं एवं अपडेट (Notifications)",
                            onClick = { onNavigate("notifications") }
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                        // Admin Mode Switch
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = ScholarGold)
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(text = "Admin Panel (कंटेंट मैनेजमेंट)", fontWeight = FontWeight.SemiBold)
                                    Text(text = "अध्याय, नोट्स व प्रश्न जोड़ने हेतु", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Switch(
                                checked = user?.isAdmin == true,
                                onCheckedChange = {
                                    viewModel.toggleAdminMode(it)
                                    if (it) onNavigate("admin_panel")
                                },
                                modifier = Modifier.testTag("admin_mode_toggle")
                            )
                        }

                        if (user?.isAdmin == true) {
                            ProfileMenuItem(
                                icon = Icons.Default.AdminPanelSettings,
                                title = "व्यवस्थापक डैशबोर्ड खोलें (Open Admin CMS)",
                                onClick = { onNavigate("admin_panel") }
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                        ProfileMenuItem(
                            icon = Icons.Default.Info,
                            title = "सिन्हा जी स्टडी सेंटर के बारे में (About Us)",
                            onClick = { onNavigate("about_us") }
                        )
                        ProfileMenuItem(
                            icon = Icons.Default.Policy,
                            title = "गोपनीयता नीति (Privacy Policy)",
                            onClick = { onNavigate("privacy_policy") }
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.logout() }
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🚪 लॉगआउट (Log Out)",
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // App Version
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SINHA JEE STUDY CENTRE • v1.0.0",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Designed for Bihar Board & Indian State Board Students",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    if (showEditDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("प्रोफ़ाइल संपादित करें (Edit Profile)", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    androidx.compose.material3.OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("पूरा नाम (Full Name)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    androidx.compose.material3.OutlinedTextField(
                        value = editBoard,
                        onValueChange = { editBoard = it },
                        label = { Text("बोर्ड (Board Name)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    androidx.compose.material3.OutlinedTextField(
                        value = editMedium,
                        onValueChange = { editMedium = it },
                        label = { Text("माध्यम (Hindi / English)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editName.isNotBlank()) {
                            viewModel.updateProfile(editName, editBoard, editMedium)
                            showEditDialog = false
                        }
                    }
                ) {
                    Text("सहेजें (Save)")
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showEditDialog = false }) {
                    Text("रद्द करें (Cancel)")
                }
            }
        )
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = ScholarPrimary, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(14.dp))
        Text(text = title, modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium, fontSize = 14.sp)
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
    }
}
