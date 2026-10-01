package com.example.ui.screens.info

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.MainViewModel
import com.example.ui.components.SinhaTopAppBar
import com.example.ui.theme.ScholarGold
import com.example.ui.theme.ScholarPrimary

@Composable
fun AboutUsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            SinhaTopAppBar(
                title = "सिन्हा जी स्टडी सेंटर के बारे में",
                subtitle = "About SINHA JEE STUDY CENTRE",
                showBackButton = true,
                onBackClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "SINHA JEE STUDY CENTRE",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = ScholarPrimary
                        )
                        Text(
                            text = "Digital Textbook • Notes • Questions • Practice",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "सिन्हा जी स्टडी सेंटर बिहार बोर्ड तथा अन्य हिंदी माध्यम राज्य बोर्डों के कक्षा 10 एवं 12 के छात्र-छात्राओं को समर्पित एक आधुनिक डिजिटल शैक्षणिक मंच है।\n\nहमारा उद्देश्य हर छात्र तक उच्च कोटि की प्रमाणित डिजिटल पाठ्यपुस्तकें, हस्तलिखित नोट्स, अध्यायवार प्रश्न बैंक और ऑनलाइन क्विज़ पहुँचाना है।",
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(text = "📞 संपर्क सूत्र (Contact Us)", fontWeight = FontWeight.Bold, color = ScholarPrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "• ईमेल: support@sinhajeestudy.edu")
                        Text(text = "• हेल्पलाइन: +91 98765 43210 (सुबह 9 से शाम 7 बजे तक)")
                        Text(text = "• पता: सिन्हा जी स्टडी सेंटर, पटना, बिहार")
                    }
                }
            }
        }
    }
}

@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            SinhaTopAppBar(
                title = "गोपनीयता नीति (Privacy Policy)",
                subtitle = "Student Data Protection & Security",
                showBackButton = true,
                onBackClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(text = "Privacy & Student Safety Policy", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "1. डेटा सुरक्षा: सिन्हा जी स्टडी सेंटर विद्यार्थियों की व्यक्तिगत जानकारी जैसे नाम, मोबाइल नंबर और अध्ययन प्रगति को सुरक्षित रखता है।\n\n" +
                            "2. विज्ञापन रहित अनुभव: विद्यार्थियों की पढ़ाई में किसी भी प्रकार का व्यवधान न आए, इसलिए पाठ्यपुस्तकों में गैर-शैक्षणिक विज्ञापन नहीं दिखाए जाते।\n\n" +
                            "3. ऑफ़लाइन अध्ययन: छात्र-छात्राओं द्वारा डाउनलोड की गई पाठ्यपुस्तक सामग्री उनके उपकरण में सुरक्षित रहती है।\n\n" +
                            "4. कॉपीराइट अनुपालन: ऐप में उपलब्ध सभी शैक्षिक सामग्री शैक्षणिक उद्देश्यों एवं मार्गदर्शन हेतु अधिकृत है।",
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationsScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val notifications by viewModel.notifications.collectAsState()

    Scaffold(
        topBar = {
            SinhaTopAppBar(
                title = "सूचनाएं एवं अपडेट (Notifications)",
                subtitle = "नए अध्याय, नोट्स व परीक्षा अलर्ट",
                showBackButton = true,
                onBackClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(notifications) { notif ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Icon(Icons.Default.Notifications, contentDescription = null, tint = ScholarGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = notif.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = notif.message, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
