package com.example.data.seed

import com.example.data.local.AppDatabase
import com.example.data.local.entity.ChapterEntity
import com.example.data.local.entity.FormulaEntity
import com.example.data.local.entity.NoteEntity
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.QuestionEntity
import com.example.data.local.entity.SubjectEntity
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object InitialDataSeeder {

    suspend fun seedDatabaseIfEmpty(database: AppDatabase) = withContext(Dispatchers.IO) {
        val userDao = database.userDao()
        val existingUser = userDao.getUser()
        if (existingUser != null) {
            return@withContext // Database already seeded
        }

        // 1. Initial Student Profile (Compulsory registration can update this or student can edit)
        userDao.insertUser(
            UserEntity(
                id = 1,
                phone = "9876543210",
                name = "Aman Kumar Sinha",
                studentClass = "10",
                board = "Bihar Board (BSEB)",
                medium = "Hindi",
                stream = "General",
                avatarId = 1,
                studyStreakDays = 5,
                totalStudyMinutes = 240,
                isLoggedIn = true,
                isAdmin = false
            )
        )

        // 2. Class 10 & 12 Subjects
        val subjects = listOf(
            // Class 10
            SubjectEntity("c10_sci", "10", "General", "विज्ञान (Science)", "Science", "science", "#1E88E5", 1),
            SubjectEntity("c10_math", "10", "General", "गणित (Mathematics)", "Mathematics", "calculate", "#FB8C00", 2),
            SubjectEntity("c10_sst", "10", "General", "सामाजिक विज्ञान (Social Science)", "Social Science", "public", "#43A047", 3),
            SubjectEntity("c10_hin", "10", "General", "हिंदी (Hindi - गोधूलि)", "Hindi", "menu_book", "#E53935", 4),
            SubjectEntity("c10_eng", "10", "General", "अंग्रेजी (English)", "English", "translate", "#8E24AA", 5),
            SubjectEntity("c10_san", "10", "General", "संस्कृत (Sanskrit - पीयूषम्)", "Sanskrit", "auto_stories", "#3949AB", 6),

            // Class 12 - Science
            SubjectEntity("c12_phy", "12", "Science", "भौतिकी (Physics)", "Physics", "bolt", "#D81B60", 1),
            SubjectEntity("c12_chem", "12", "Science", "रसायन शास्त्र (Chemistry)", "Chemistry", "science", "#00ACC1", 2),
            SubjectEntity("c12_math", "12", "Science", "गणित (Mathematics)", "Mathematics", "calculate", "#FB8C00", 3),
            SubjectEntity("c12_bio", "12", "Science", "जीव विज्ञान (Biology)", "Biology", "eco", "#43A047", 4),
            SubjectEntity("c12_hin_sci", "12", "Science", "हिंदी (Hindi - दिगंत)", "Hindi", "menu_book", "#E53935", 5),
            SubjectEntity("c12_eng_sci", "12", "Science", "अंग्रेजी (English - 100 Marks)", "English", "translate", "#8E24AA", 6),

            // Class 12 - Arts
            SubjectEntity("c12_hist", "12", "Arts", "इतिहास (History)", "History", "history_edu", "#8D6E63", 1),
            SubjectEntity("c12_geo", "12", "Arts", "भूगोल (Geography)", "Geography", "terrain", "#26A69A", 2),
            SubjectEntity("c12_pol", "12", "Arts", "राजनीति शास्त्र (Political Science)", "Political Science", "gavel", "#5C6BC0", 3),
            SubjectEntity("c12_eco_arts", "12", "Arts", "अर्थशास्त्र (Economics)", "Economics", "trending_up", "#7CB342", 4),
            SubjectEntity("c12_hin_arts", "12", "Arts", "हिंदी (Hindi - दिगंत)", "Hindi", "menu_book", "#E53935", 5),
            SubjectEntity("c12_eng_arts", "12", "Arts", "अंग्रेजी (English - 100 Marks)", "English", "translate", "#8E24AA", 6),

            // Class 12 - Commerce
            SubjectEntity("c12_acc", "12", "Commerce", "लेखाशास्त्र (Accountancy)", "Accountancy", "account_balance", "#00897B", 1),
            SubjectEntity("c12_bst", "12", "Commerce", "व्यवसाय अध्ययन (Business Studies)", "Business Studies", "business_center", "#F4511E", 2),
            SubjectEntity("c12_eco_comm", "12", "Commerce", "अर्थशास्त्र (Economics)", "Economics", "trending_up", "#7CB342", 3),
            SubjectEntity("c12_math_comm", "12", "Commerce", "गणित (Mathematics)", "Mathematics", "calculate", "#FB8C00", 4),
            SubjectEntity("c12_hin_comm", "12", "Commerce", "हिंदी (Hindi - दिगंत)", "Hindi", "menu_book", "#E53935", 5),
            SubjectEntity("c12_eng_comm", "12", "Commerce", "अंग्रेजी (English - 100 Marks)", "English", "translate", "#8E24AA", 6)
        )
        database.subjectDao().insertSubjects(subjects)

        // 3. Digital Textbook Chapters
        val chapters = listOf(
            ChapterEntity(
                id = "c10_sci_ch1",
                subjectId = "c10_sci",
                studentClass = "10",
                chapterNumber = 1,
                titleHindi = "रासायनिक अभिक्रियाएं एवं समीकरण",
                titleEnglish = "Chemical Reactions and Equations",
                introduction = "दैनिक जीवन में होने वाले रासायनिक परिवर्तनों जैसे दूध से दही बनना, लोहे में जंग लगना, भोजन का पकना आदि का अध्ययन। रासायनिक समीकरण द्वारा इन्हें संक्षिप्त रूप में व्यक्त किया जाता है।",
                learningObjectives = "• रासायनिक अभिक्रिया के लक्षण समझना\n• रासायनिक समीकरण को संतुलित (Balance) करना सीखना\n• संयोजन, वियोजन, विस्थापन और द्विविस्थापन अभिक्रियाओं की पहचान\n• उपचयन (Oxidation) और अपचयन (Reduction) की अवधारणा\n• संक्षारण (Corrosion) और विकृतगंधिता (Rancidity) के बचाव के उपाय",
                textbookContent = """
### 1.1 रासायनिक अभिक्रिया किसे कहते हैं? (Chemical Reaction)
जब एक या एक से अधिक पदार्थ आपस में क्रिया करके भिन्न गुणधर्मों वाले नए पदार्थ बनाते हैं, तो इस प्रक्रम को **रासायनिक अभिक्रिया** कहते हैं।

> **परिभाषा (Definition):**
> रासायनिक परिवर्तन के दौरान नए रासायनिक बंध बनते हैं तथा पुराने बंध टूटते हैं।
> उदाहरण: जब मैग्नीशियम रिबन को वायु में जलाया जाता है, तो मैग्नीशियम ऑक्साइड का श्वेत चूर्ण बनता है।
> **2Mg + O₂ → 2MgO**

---

### 1.2 रासायनिक अभिक्रिया के प्रमुख लक्षण
1. **अवस्था में परिवर्तन (Change in State)**
2. **रंग में परिवर्तन (Change in Colour)**
3. **गैस का उत्सर्जन (Evolution of Gas)**
4. **तापमान में परिवर्तन (Change in Temperature)**

---

### 1.3 रासायनिक समीकरण को संतुलित करना (Balancing Chemical Equations)
द्रव्यमान संरक्षण के नियमानुसार: किसी भी रासायनिक अभिक्रिया में द्रव्यमान का न तो निर्माण होता है और न ही विनाश। इसलिए तीर के दोनों ओर प्रत्येक तत्व के परमाणुओं की संख्या बराबर होनी चाहिए।

**संतुलित करने की विधि (Hit and Trial Method):**
- असंतुलित: `Fe + H₂O → Fe₃O₄ + H₂`
- संतुलित: `3Fe + 4H₂O → Fe₃O₄ + 4H₂`

---

### 1.4 रासायनिक अभिक्रियाओं के प्रकार (Types of Reactions)
1. **संयोजन अभिक्रिया (Combination Reaction):**
   दो या अधिक अभिकारक मिलकर एकल उत्पाद बनाते हैं।
   *उदाहरण:* `C + O₂ → CO₂` (कोयले का दहन), `CaO + H₂O → Ca(OH)₂ + ऊष्मा` (बिना बुझा चूना)

2. **वियोजन / अपघटन अभिक्रिया (Decomposition Reaction):**
   एकल अभिकारक टूट कर दो या अधिक छोटे उत्पाद बनाता है।
   *उदाहरण:* `2FeSO₄ —(ऊष्मा)→ Fe₂O₃ + SO₂ + SO₃`

3. **विस्थापन अभिक्रिया (Displacement Reaction):**
   अधिक क्रियाशील तत्व कम क्रियाशील तत्व को उसके यौगिक से विस्थापित कर देता है।
   *उदाहरण:* `Fe + CuSO₄ (नीला) → FeSO₄ (हरा) + Cu`

4. **द्विविस्थापन अभिक्रिया (Double Displacement Reaction):**
   अभिकारकों के बीच आयनों का परस्पर आदान-प्रदान होता है।
   *उदाहरण:* `Na₂SO₄ + BaCl₂ → BaSO₄↓ (सफेद अवक्षेप) + 2NaCl`

5. **उपचयन एवं अपचयन (Redox Reactions):**
   - **उपचयन (Oxidation):** ऑक्सीजन की वृद्धि या हाइड्रोजन का ह्रास।
   - **अपचयन (Reduction):** ऑक्सीजन का ह्रास या हाइड्रोजन की वृद्धि।
   - *उदाहरण:* `CuO + H₂ —(ऊष्मा)→ Cu + H₂O` (CuO का Cu में अपचयन, H₂ का H₂O में उपचयन)

---

### 1.5 दैनिक जीवन में उपचयन के प्रभाव
- **संक्षारण (Corrosion):** धातु जब नमी और वायु के संपर्क में आती है तो नष्ट होने लगती है (जैसे लोहे पर भूरी परत)।
  *बचाव:* जस्तीकरण (Galvanization), पेंट करना, तेल लगाना।
- **विकृतगंधिता (Rancidity):** वसायुक्त और तैलीय खाद्य पदार्थ जब उपचयित होते हैं तो उनका स्वाद और गंध बदल जाती है।
  *बचाव:* नाइट्रोजन गैस प्रवाहित करना, वायुरोधी बर्तनों में रखना।
                """.trimIndent(),
                summary = "रासायनिक अभिक्रिया में अभिकारकों से उत्पाद बनते हैं। द्रव्यमान संरक्षण के नियम के कारण समीकरण को संतुलित किया जाता है। मुख्य अभिक्रियाएं: संयोजन, वियोजन, विस्थापन, द्विविस्थापन और रेडॉक्स। संक्षारण व विकृतगंधिता से बचाव के लिए नाइट्रोजन गैस व गैल्वनीकरण उपयोगी हैं।",
                videoUrl = "https://www.youtube.com/watch?v=demo1",
                videoTitle = "रासायनिक अभिक्रियाएं - संपूर्ण अध्याय व्याख्या",
                readingProgressPercent = 65,
                lastReadSection = "1.4 रासायनिक अभिक्रियाओं के प्रकार",
                lastReadTimestamp = System.currentTimeMillis() - 1000 * 60 * 30,
                totalPages = 18,
                lastReadPage = 8,
                isDownloaded = true
            ),
            ChapterEntity(
                id = "c10_sci_ch2",
                subjectId = "c10_sci",
                studentClass = "10",
                chapterNumber = 2,
                titleHindi = "अम्ल, क्षारक एवं लवण",
                titleEnglish = "Acids, Bases and Salts",
                introduction = "दैनिक जीवन में खट्टे और कड़वे पदार्थों का अध्ययन। लिटमस पेपर, pH स्केल और दैनिक जीवन में उपयोगी लवण जैसे विरंजक चूर्ण, बेकिंग सोडा, प्लास्टर ऑफ पेरिस।",
                learningObjectives = "• अम्ल एवं क्षारक के भौतिक व रासायनिक गुणधर्म समझना\n• सूचक (Indicators) तथा pH स्केल की अवधारणा\n• लवण के निर्माण एवं उनके औद्योगिक उपयोग जानना",
                textbookContent = """
### 2.1 अम्ल और क्षारक की पहचान
- **अम्ल (Acid):** स्वाद में खट्टे होते हैं। नीले लिटमस को लाल कर देते हैं। जलीय विलयन में H⁺ आयन देते हैं। (उदा. HCl, H₂SO₄, सिरका/CH₃COOH)
- **क्षारक (Base):** स्वाद में कड़वे तथा छूने में साबुन जैसे चिकने होते हैं। लाल लिटमस को नीला करते हैं। जलीय विलयन में OH⁻ आयन देते हैं। (उदा. NaOH, KOH)

---

### 2.2 सूचक (Indicators)
- **प्राकृतिक सूचक:** लिटमस (लाइकेन से प्राप्त), हल्दी, लाल पत्तागोभी।
- **संश्लेषित सूचक:** मिथाइल ऑरेंज (अम्ल में लाल, क्षार में पीला), फिनॉल्फथेलिन (अम्ल में रंगहीन, क्षार में गुलाबी)।
- **घ्राण सूचक (Olfactory Indicators):** वैनिला, प्याज, लौंग का तेल।

---

### 2.3 pH स्केल (pH Scale)
विलयन में हाइड्रोजन आयन की सांद्रता मापने वाला स्केल।
- pH < 7 : अम्लीय (Acidic)
- pH = 7 : उदासीन (Neutral) जैसे शुद्ध जल
- pH > 7 : क्षारकीय (Basic)
*मानव शरीर 7.0 से 7.8 pH सीमा में कार्य करता है। रक्त का pH लगभग 7.4 होता है।*

---

### 2.4 महत्वपूर्ण लवण एवं उनके सूत्र
1. **विरंजक चूर्ण (Bleaching Powder):** `CaOCl₂`
   *उपयोग:* पीने के जल को जीवाणुरहित करने में, वस्त्र उद्योग में विरंजन।
2. **बेकिंग सोडा (Baking Soda):** `NaHCO₃`
   *उपयोग:* एंटासिड के रूप में, सोडा-अम्ल अग्निशामक में, बेकिंग पाउडर बनाने में।
3. **धावन सोडा (Washing Soda):** `Na₂CO₃·10H₂O`
   *उपयोग:* कांच, साबुन एवं कागज उद्योग में, जल की स्थायी कठोरता हटाने में।
4. **प्लास्टर ऑफ पेरिस (P.O.P):** `CaSO₄·½H₂O`
   *उपयोग:* टूटी हड्डियों को सही स्थान पर स्थिर रखने, खिलौने एवं मूर्तियां बनाने में।
                """.trimIndent(),
                summary = "अम्ल H⁺ आयन तथा क्षारक OH⁻ आयन देते हैं। pH स्केल 0 से 14 तक होता है। महत्वपूर्ण लवणों के सूत्र: CaOCl₂, NaHCO₃, Na₂CO₃·10H₂O, CaSO₄·½H₂O।",
                readingProgressPercent = 30,
                lastReadSection = "2.2 सूचक (Indicators)",
                lastReadTimestamp = System.currentTimeMillis() - 1000 * 60 * 180,
                totalPages = 20,
                lastReadPage = 4,
                isDownloaded = false
            ),
            ChapterEntity(
                id = "c10_sci_ch6",
                subjectId = "c10_sci",
                studentClass = "10",
                chapterNumber = 6,
                titleHindi = "जैव प्रक्रम (Life Processes)",
                titleEnglish = "Life Processes",
                introduction = "सजीवों द्वारा अपने अस्तित्व को बनाए रखने के लिए किए जाने वाले अनिवार्य प्रक्रम: पोषण, श्वसन, वहन और उत्सर्जन का विस्तृत वैज्ञानिक अध्ययन।",
                learningObjectives = "• स्वपोषी एवं विषमपोषी पोषण समझना\n• प्रकाश संश्लेषण की रासायनिक समीकरण\n• मानव पाचन तंत्र, श्वसन तंत्र तथा हृदय की कार्यविधि\n• मानव उत्सर्जन तंत्र (वृक्क/नेफ्रॉन) का सचित्र अध्ययन",
                textbookContent = """
### 6.1 पोषण (Nutrition)
- **स्वपोषी पोषण (Autotrophic Nutrition):** हरे पौधे सूर्य के प्रकाश तथा पर्णहरिम की उपस्थिति में CO₂ और जल से ग्लूकोज बनाते हैं।
  `6CO₂ + 12H₂O —(प्रकाश/क्लोरोफिल)→ C₆H₁₂O₆ + 6O₂ + 6H₂O`
- **विषमपोषी पोषण:** जो भोजन के लिए दूसरों पर निर्भर रहते हैं (शाकाहारी, मांसाहारी, परजीवी, मृतजीवी)।

---

### 6.2 मानव पाचन तंत्र (Human Digestive System)
- **मुख (Mouth):** लार ग्रंथि से टायलिन (एमाइलेज) एंजाइम निकलता है जो स्टार्च को माल्टोज में बदलता है।
- **आमाशय (Stomach):** हाइड्रोक्लोरिक अम्ल (HCl), पेप्सिन (प्रोटीन पाचक), और श्लेष्मा (म्यूकस)।
- **यकृत (Liver):** पित्त रस (Bile Juice) उत्पन्न करता है जो वसा का इमल्सीकरण करता है।
- **अग्न्याशय (Pancreas):** ट्रिप्सिन (प्रोटीन), लाइपेज (इमल्सीकृत वसा), एमाइलेज स्रावित करता है।
- **क्षुद्रांत्र (Small Intestine):** पचे भोजन का अवशोषण दीर्घरोम (Villi) द्वारा होता है।

---

### 6.3 मानव हृदय एवं परिसंचरण तंत्र
मानव हृदय 4 कोष्ठों (दो अलिंद तथा दो निलय) में बंटा होता है।
- महाधमनी (Aorta) ऑक्सीजनित रक्त पूरे शरीर में भेजती है।
- फुफ्फुस धमनी अनाक्सीजनित रक्त फेफड़ों तक ले जाती है।
- रक्तदाब (Blood Pressure) सामान्यतः 120/80 mm Hg होता है, जिसे स्फिग्मोमैनोमीटर से मापते हैं।
                """.trimIndent(),
                summary = "जैव प्रक्रम जीवन के रखरखाव के लिए अनिवार्य हैं। प्रकाश संश्लेषण में ऑक्सीजन मुक्त होती है। हृदय का दोहरा परिसंचरण मानव में दक्ष ऑक्सीजन आपूर्ति सुनिश्चित करता है।",
                readingProgressPercent = 0,
                lastReadSection = "Introduction",
                lastReadTimestamp = 0L,
                totalPages = 24,
                lastReadPage = 1,
                isDownloaded = false
            ),

            // Class 10 Math
            ChapterEntity(
                id = "c10_math_ch1",
                subjectId = "c10_math",
                studentClass = "10",
                chapterNumber = 1,
                titleHindi = "वास्तविक संख्याएं (Real Numbers)",
                titleEnglish = "Real Numbers",
                introduction = "यूक्लिड विभाजन प्रमेयिका, अंकगणित की आधारभूत प्रमेय, अपरिमेय संख्याओं का सत्यापन और सांत/असांत आवर्ती दशमलव प्रसार।",
                learningObjectives = "• यूक्लिड विभाजन एल्गोरिथ्म से HCF ज्ञात करना\n• अभाज्य गुणनखंड विधि से HCF और LCM\n• √2, √3, √5 को अपरिमेय सिद्ध करना",
                textbookContent = """
### 1.1 यूक्लिड विभाजन प्रमेयिका (Euclid's Division Lemma)
दो धनात्मक पूर्णांकों a और b के लिए अद्वितीय पूर्ण संख्याएं q और r विद्यमान होती हैं कि:
**a = bq + r ,  जहाँ 0 ≤ r < b**

---

### 1.2 HCF और LCM में संबंध
किन्हीं दो धनात्मक पूर्णांकों a और b के लिए:
**HCF(a, b) × LCM(a, b) = a × b**

---

### 1.3 अपरिमेय संख्याओं का पुनर्भ्रमण
- प्रमेय: मान लीजिए p एक अभाज्य संख्या है। यदि p, a² को विभाजित करता है, तो p, a को भी विभाजित करेगा।
- सिद्ध करें कि √2 एक अपरिमेय संख्या है (विरोधाभास विधि - Proof by Contradiction)।
                """.trimIndent(),
                summary = "a = bq + r (0 ≤ r < b)। दो संख्याओं का गुणनफल = उनके HCF और LCM का गुणनफल।",
                readingProgressPercent = 50,
                lastReadSection = "1.2 HCF और LCM में संबंध",
                lastReadTimestamp = System.currentTimeMillis() - 1000 * 60 * 600,
                totalPages = 14,
                lastReadPage = 5,
                isDownloaded = true
            ),

            // Class 12 Physics
            ChapterEntity(
                id = "c12_phy_ch1",
                subjectId = "c12_phy",
                studentClass = "12",
                chapterNumber = 1,
                titleHindi = "विद्युत आवेश तथा क्षेत्र",
                titleEnglish = "Electric Charges and Fields",
                introduction = "स्थिर वैद्युतिकी, कूलॉम का नियम, विद्युत क्षेत्र रेखाएं, विद्युत द्विध्रुव, और गाउस का नियम एवं उसके अनुप्रयोग।",
                learningObjectives = "• आवेश का क्वांटीकरण तथा संरक्षण\n• सदिश रूप में कूलॉम का नियम\n• गाउस की प्रमेय तथा अनंत लंबाई के चालक तार हेतु विद्युत क्षेत्र की तीव्रता",
                textbookContent = """
### 1.1 विद्युत आवेश के मूल गुण (Basic Properties of Charge)
1. **आवेशों की योज्यता (Additivity):** कुल आवेश Q = q₁ + q₂ + q₃ + ...
2. **आवेश का संरक्षण (Conservation):** विलगित निकाय का कुल आवेश नियत रहता है।
3. **आवेश का क्वांटीकरण (Quantization):** Q = ± ne , जहाँ e = 1.6 × 10⁻¹⁹ C

---

### 1.2 कूलॉम का नियम (Coulomb's Law)
दो स्थिर बिंदु आवेशों के बीच आकर्षण या प्रतिकर्षण बल:
**F = (1 / 4πε₀) × (|q₁ q₂| / r²)**
जहाँ (1 / 4πε₀) ≈ 9 × 10⁹ N m²/C² तथा ε₀ = 8.854 × 10⁻¹² C²N⁻¹m⁻²

---

### 1.3 गाउस का नियम (Gauss's Law)
किसी बंद पृष्ठ से गुजरने वाला कुल विद्युत फ्लक्स पृष्ठ द्वारा परिबद्ध कुल आवेश का 1/ε₀ गुना होता है:
**Φ = ∮ E·dA = q_enclosed / ε₀**
                """.trimIndent(),
                summary = "स्थिर विद्युत आवेश बल आरोपित करते हैं। गाउस की प्रमेय सममित आवेश वितरण के लिए विद्युत क्षेत्र तीव्रता निकालने में अत्यधिक सहायक है।",
                readingProgressPercent = 20,
                lastReadSection = "1.2 कूलॉम का नियम",
                lastReadTimestamp = System.currentTimeMillis() - 1000 * 60 * 900,
                totalPages = 28,
                lastReadPage = 5,
                isDownloaded = false
            ),

            // Class 12 Chemistry
            ChapterEntity(
                id = "c12_chem_ch1",
                subjectId = "c12_chem",
                studentClass = "12",
                chapterNumber = 1,
                titleHindi = "विलयन (Solutions)",
                titleEnglish = "Solutions",
                introduction = "द्विअंगी विलयन, सांद्रता व्यक्त करने की विधियां, हेनरी का नियम, राउल्ट का नियम, और अणुसंख्य गुणधर्म।",
                learningObjectives = "• मोलरता, मोललता और मोल अंश की गणना\n• हेनरी तथा राउल्ट के नियम\n• क्वथनांक उन्नयन, हिमांक अवनमन और परासरण दाब\n• वान्ट हॉफ गुणांक (i)",
                textbookContent = """
### 1.1 सांद्रता व्यक्त करने की विधियां
- **मोलरता (Molarity, M):** 1 लीटर विलयन में विलेय के मोलों की संख्या।
- **मोललता (Molality, m):** 1 किलोग्राम विलायक में विलेय के मोलों की संख्या (ताप पर निर्भर नहीं करती)।
- **मोल अंश (Mole Fraction, X):** X_A = n_A / (n_A + n_B)

---

### 1.2 राउल्ट का नियम (Raoult's Law)
वाष्पशील द्रवों के विलयन में प्रत्येक घटक का आंशिक वाष्प दाब विलयन में उसके मोल अंश के समानुपाती होता है:
**P_A = P_A° × X_A**

---

### 1.3 अणुसंख्य गुणधर्म (Colligative Properties)
1. वाष्प दाब का आपेक्षिक अवनमन: (P₁° - P₁) / P₁° = X₂
2. क्वथनांक का उन्नयन: ΔTb = Kb × m
3. हिमांक का अवनमन: ΔTf = Kf × m
4. परासरण दाब: π = CRT
                """.trimIndent(),
                summary = "अणुसंख्य गुणधर्म केवल विलेय के कणों की संख्या पर निर्भर करते हैं। वान्ट हॉफ गुणांक आयनन या संगुणन को व्यक्त करता है।",
                readingProgressPercent = 0,
                lastReadSection = "Introduction",
                lastReadTimestamp = 0L,
                totalPages = 22,
                lastReadPage = 1,
                isDownloaded = false
            ),

            // Class 10 Social Science
            ChapterEntity(
                id = "c10_sst_ch1",
                subjectId = "c10_sst",
                studentClass = "10",
                chapterNumber = 1,
                titleHindi = "यूरोप में राष्ट्रवाद का उदय",
                titleEnglish = "The Rise of Nationalism in Europe",
                introduction = "19वीं सदी में यूरोप में राष्ट्रवाद की लहर, फ्रांसीसी क्रांति, नेपोलियन की नागरिक संहिता (1804), और इटली व जर्मनी का एकीकरण।",
                learningObjectives = "• 1789 की फ्रांसीसी क्रांति का प्रभाव समझना\n• उदारवादी राष्ट्रवाद के मायने\n• इटली (मेत्सिनी, काबूर, गैरीबाल्डी) और जर्मनी (बिस्मार्क) का एकीकरण",
                textbookContent = """
### 1.1 फ्रांसीसी क्रांति और राष्ट्र का विचार
1789 की फ्रांसीसी क्रांति के साथ राष्ट्रवाद की पहली स्पष्ट अभिव्यक्ति हुई।
- पितृभूमि (la patrie) और नागरिक (le citoyen) के विचारों पर बल।
- एक नया फ्रांसीसी तिरंगा झंडा चुना गया जिसने पुराने राजसी ध्वज की जगह ली।
- आंतरिक सीमा शुल्क समाप्त किए गए और नाप-तौल की एक समान व्यवस्था लागू की गई।

---

### 1.2 नेपोलियन की नागरिक संहिता (1804 का नेपोलियन कोड)
नेपोलियन ने प्रशासनिक क्षेत्र में क्रांतिकारी सुधार किए:
1. जन्म पर आधारित सभी विशेषाधिकार समाप्त किए।
2. कानून के समक्ष समानता और संपत्ति के अधिकार को सुरक्षित बनाया।
3. सामंती व्यवस्था को समाप्त किया और किसानों को भू-दासत्व से मुक्ति दिलाई।

---

### 1.3 इटली और जर्मनी का एकीकरण
- **इटली का एकीकरण:** ज्युसेपे मेत्सिनी (यंग इटली), काउंट काबूर (कूटनीतिज्ञ), और गैरीबाल्डी (रेड शर्ट्स)। 1861 में विक्टर इमैनुएल द्वितीय को एकीकृत इटली का राजा घोषित किया गया।
- **जर्मनी का एकीकरण:** ऑटो वॉन बिस्मार्क (रक्त और लौह की नीति - Blood and Iron Policy)। 1871 में वर्साय में प्रशा के राजा विलियम प्रथम को जर्मन सम्राट घोषित किया गया।
                """.trimIndent(),
                summary = "यूरोप में राष्ट्रवाद ने निरंकुश राजतंत्रों का अंत कर आधुनिक राष्ट्र-राज्यों की नींव रखी। बिस्मार्क ने जर्मनी तथा काबूर व गैरीबाल्डी ने इटली का एकीकरण किया।",
                readingProgressPercent = 15,
                lastReadSection = "1.2 नेपोलियन की नागरिक संहिता",
                lastReadTimestamp = 0L,
                totalPages = 20,
                lastReadPage = 3,
                isDownloaded = false
            ),

            // Class 10 Hindi (गोधूलि)
            ChapterEntity(
                id = "c10_hin_ch1",
                subjectId = "c10_hin",
                studentClass = "10",
                chapterNumber = 1,
                titleHindi = "श्रम विभाजन और जाति प्रथा",
                titleEnglish = "Shram Vibhajan aur Jati Pratha",
                introduction = "बाबा साहेब डॉ. भीमराव अंबेडकर द्वारा रचित यह निबंध आधुनिक सभ्य समाज में कार्यकुशलता के नाम पर जाति प्रथा के आधार पर श्रम विभाजन की विसंगतियों पर प्रहार करता है।",
                learningObjectives = "• जाति प्रथा के आर्थिक एवं सामाजिक दुष्परिणाम समझना\n• सच्चे लोकतंत्र की आधारशिला: स्वतंत्रता, समता और भ्रातृभाव\n• आदर्श समाज की परिकल्पना",
                textbookContent = """
### 1.1 विडंबना की बात
आज के युग में भी 'जातिवाद के पोषकों' की कमी नहीं है। इसके समर्थक तर्क देते हैं कि कार्यकुशलता के लिए श्रम विभाजन आवश्यक है, और जाति प्रथा भी तो श्रम विभाजन का ही दूसरा रूप है।

---

### 1.2 जाति प्रथा का दूसरा पहलू
जाति प्रथा केवल श्रम विभाजन ही नहीं है, बल्कि यह श्रमिकों का विभिन्न वर्गों में अस्वाभाविक विभाजन भी करती है।
- भारत की जाति प्रथा श्रमिकों का अस्वाभाविक विभाजन ही नहीं करती, बल्कि विभाजित विभिन्न वर्गों को एक-दूसरे की अपेक्षा ऊँच-नीच भी करार देती है।
- जाति प्रथा पेशे का दोषपूर्ण पूर्वनिर्धारण ही नहीं करती, बल्कि मनुष्य को जीवन भर के लिए एक पेशे में बांध भी देती है।

---

### 1.3 लेखक की दृष्टि में आदर्श समाज
लेखक के अनुसार आदर्श समाज वह है जिसमें:
1. **स्वतंत्रता (Liberty)**
2. **समता (Equality)**
3. **भ्रातृभाव (Fraternity)** हो। भ्रातृभाव का अर्थ है भाईचारा, जिसमें दूध और पानी के मिश्रण की तरह सब एक-दूसरे से जुड़े हों। इसी का दूसरा नाम लोकतंत्र है।
                """.trimIndent(),
                summary = "जाति प्रथा श्रम विभाजन का स्वाभाविक रूप नहीं है क्योंकि यह मनुष्य की रुचि और कार्यक्षमता पर आधारित नहीं है। सच्चा लोकतंत्र स्वतंत्रता, समानता और बंधुत्व पर आधारित होता है।",
                readingProgressPercent = 40,
                lastReadSection = "1.3 आदर्श समाज",
                lastReadTimestamp = 0L,
                totalPages = 12,
                lastReadPage = 4,
                isDownloaded = true
            ),

            // Class 10 English (Panorama)
            ChapterEntity(
                id = "c10_eng_ch1",
                subjectId = "c10_eng",
                studentClass = "10",
                chapterNumber = 1,
                titleHindi = "The Pace for Living",
                titleEnglish = "The Pace for Living (R.C. Hutchinson)",
                introduction = "R.C. Hutchinson captures the agony of modern man and how the fast pace of modern life puts undue pressure on the mind and soul.",
                learningObjectives = "• Understand the theme of fast-paced modern life\n• Appreciate the satirical portrayal of the Irish corn-merchant\n• Examine the difference between slow thinkers and fast thinkers",
                textbookContent = """
### 1.1 The Play and the Corn-Merchant
The author watched a play in Dublin where the chief character was an elderly corn-merchant in a small Irish country town. He was a man of many anxieties — his heart was dickey, his nephew was cheating him, and his wife had the fantastic notion of spending £10 on a holiday.

---

### 1.2 The Cry of Modern Speed
The corn-merchant uttered a memorable cry: 'They tell me there's an aeroplane now that goes at 1,000 miles an hour. Now that's too fast!'
The author admits that he himself enjoys high speed when driving a car at 90 miles an hour, provided he is not driving. But fast thinking can be a disadvantage in everyday tests designed only for speedy minds.
                """.trimIndent(),
                summary = "Modern life moves at an exceptionally high speed. While speed has advantages in transport, it often puts high psychological stress on slow thinkers.",
                readingProgressPercent = 10,
                lastReadSection = "1.1 The Play and the Corn-Merchant",
                lastReadTimestamp = 0L,
                totalPages = 10,
                lastReadPage = 1,
                isDownloaded = false
            ),

            // Class 10 Sanskrit (पीयूषम्)
            ChapterEntity(
                id = "c10_san_ch1",
                subjectId = "c10_san",
                studentClass = "10",
                chapterNumber = 1,
                titleHindi = "मंगलम् (उपनिषद्)",
                titleEnglish = "Mangalam (Upanishads)",
                introduction = "उपनिषदों से संकलित इस प्रथम पाठ में वैदिक ऋषियों द्वारा सत्य, आत्मा और परमात्मा के परम स्वरूप का उद्घाटन किया गया है।",
                learningObjectives = "• उपनिषद् और वैदिक दर्शन का परिचय\n• सत्यमेव जयते का मूल स्रोत (मुण्डकोपनिषद्)\n• आत्मा का गूढ़ स्वरूप समझना",
                textbookContent = """
### 1.1 हिरण्मयेन पात्रेण... (ईशावास्योपनिषद्)
**हिरण्मयेन पात्रेण सत्यस्यापिहितं मुखम्।**
**तत्त्वं पूषन्नपावृणु सत्यधर्माय दृष्टये॥**
*अर्थ:* हे पूषन् (सूर्य)! सत्य का मुख सोने जैसे ज्योतिर्मय पात्र से ढका हुआ है। सत्य धर्म के दर्शन हेतु आप उस आवरण को हटा दीजिए।

---

### 1.2 अणोरणीयान् महतो महीयान्... (कठोपनिषद्)
**अणोरणीयान् महतो महीयान्, आत्मास्य जन्तोर्निहितो गुहायाम्।**
*अर्थ:* प्राणियों के हृदय रूपी गुफा में रहने वाली आत्मा अणु से भी सूक्ष्म और महान से भी महान है।

---

### 1.3 सत्यमेव जयते नानृतम्... (मुण्डकोपनिषद्)
**सत्यमेव जयते नानृतं सत्येन पन्था विततो देवयानः।**
*अर्थ:* सत्य की ही जीत होती है, असत्य की नहीं। सत्य से ही देवयान का मार्ग प्रशस्त होता है। जिस प्रकार बहती हुई नदियां नाम और रूप को छोड़कर समुद्र में विलीन हो जाती हैं, उसी प्रकार ज्ञानी पुरुष परमात्मा में एकाकार हो जाता है।
                """.trimIndent(),
                summary = "सत्य ही परम ब्रह्म का स्वरूप है। आत्मा हृदय रूपी गुफा में स्थित है। मुण्डकोपनिषद् का महामंत्र 'सत्यमेव जयते' भारत का राष्ट्रीय आदर्श वाक्य है।",
                readingProgressPercent = 50,
                lastReadSection = "1.3 सत्यमेव जयते",
                lastReadTimestamp = 0L,
                totalPages = 8,
                lastReadPage = 2,
                isDownloaded = false
            ),

            // Class 12 Biology
            ChapterEntity(
                id = "c12_bio_ch1",
                subjectId = "c12_bio",
                studentClass = "12",
                chapterNumber = 1,
                titleHindi = "पुष्पी पादपों में लैंगिक जनन",
                titleEnglish = "Sexual Reproduction in Flowering Plants",
                introduction = "आवृतबीजी पौधों (Angiosperms) में पुष्प की संरचना, परागण (Pollination), दोहरा निषेचन (Double Fertilization) और भ्रूणपोष का विकास।",
                learningObjectives = "• पुंकेसर और स्त्रीकेसर की आंतरिक संरचना\n• स्वपरागण एवं परपरागण के साधन\n• दोहरा निषेचन तथा त्रिसंलयन (Triple Fusion)",
                textbookContent = """
### 1.1 पुष्प की संरचना (Structure of a Flower)
पुष्प आवृतबीजियों का मुख्य जननांग है।
- **पुमंग (Androecium):** नर जननांग, जिसकी इकाई पुंकेसर (Stamen) है। परागकोश में लघुबीजाणुजनन द्वारा परागकण (Pollen Grains) बनते हैं।
- **जायांग (Gynoecium):** मादा जननांग, जिसकी इकाई अंडप/स्त्रीकेसर (Carpel/Pistil) है। बीजांड में गुरुबीजाणुजनन द्वारा भ्रूणकोष (Embryo Sac) बनता है।

---

### 1.2 दोहरा निषेचन (Double Fertilization)
यह आवृतबीजी पादपों का अनूठा लक्षण है:
1. **युग्मक संलयन (Syngamy):** एक नर युग्मक (n) + अंड कोशिका (n) → द्विगुणित युग्मनज (2n) बनता है।
2. **त्रिसंलयन (Triple Fusion):** दूसरा नर युग्मक (n) + दो ध्रुवीय केंद्रक (2n) → प्राथमिक भ्रूणपोष केंद्रक (PEN, 3n) बनता है।
                """.trimIndent(),
                summary = "पुष्प जनन अंग है। परागण के बाद दोहरा निषेचन होता है जिससे युग्मनज (भ्रूण) और त्रिगुणित भ्रूणपोष (Endosperm) बनता है जो भ्रूण को पोषण देता है।",
                readingProgressPercent = 10,
                lastReadSection = "1.2 दोहरा निषेचन",
                lastReadTimestamp = 0L,
                totalPages = 24,
                lastReadPage = 2,
                isDownloaded = false
            ),

            // Class 12 History
            ChapterEntity(
                id = "c12_hist_ch1",
                subjectId = "c12_hist",
                studentClass = "12",
                chapterNumber = 1,
                titleHindi = "ईंटें, मनके तथा अस्थियां (हड़प्पा सभ्यता)",
                titleEnglish = "Bricks, Beads and Bones (Harappan Civilisation)",
                introduction = "सिंधु घाटी सभ्यता का नगर नियोजन, विशाल स्नानागार, अन्न भंडार, मुहरें, व्यापारिक संबंध और पतन के प्रमुख कारण।",
                learningObjectives = "• हड़प्पा संस्कृति की खोज और काल निर्धारण\n• मोहनजोदड़ो का नगर नियोजन और जल निकास प्रणाली\n• हड़प्पा सभ्यता की लिपि और मुहरें",
                textbookContent = """
### 1.1 मोहनजोदड़ो: एक नियोजित शहरी केंद्र
हड़प्पा सभ्यता का सबसे अनूठा पहलू शहरी केंद्रों का विकास था।
- **दुर्ग (Citadel):** ऊंचाई पर बना पश्चिमी भाग, जहाँ सार्वजनिक संरचनाएं जैसे 'विशाल स्नानागार' (The Great Bath) और 'मालगोदाम' स्थित थे।
- **निचला शहर (Lower Town):** आवासीय क्षेत्र, जहाँ सड़कें एक-दूसरे को समकोण पर काटती थीं (ग्रिड पद्धति)।
- **जल निकास प्रणाली (Drainage System):** हर घर की नाली सड़क की ढकी हुई मुख्य नाली से जुड़ी होती थी।

---

### 1.2 मुहरें और सामाजिक-आर्थिक जीवन
- मुहरें सेलखड़ी (Steatite) पत्थर से बनाई जाती थीं जिन पर जानवरों के चित्र और अपठित लिपि अंकित है।
- चन्हुदड़ो मनके बनाने का प्रमुख केंद्र था। लोथल से बंदरगाह (Dockyard) के साक्ष्य मिले हैं।
                """.trimIndent(),
                summary = "हड़प्पा सभ्यता अपनी उन्नत नगर नियोजन, नालियों की व्यवस्था और व्यापार के लिए विश्वविख्यात कांस्य युगीन सभ्यता थी।",
                readingProgressPercent = 25,
                lastReadSection = "1.1 मोहनजोदड़ो",
                lastReadTimestamp = 0L,
                totalPages = 26,
                lastReadPage = 4,
                isDownloaded = false
            ),

            // Class 12 Accountancy
            ChapterEntity(
                id = "c12_acc_ch1",
                subjectId = "c12_acc",
                studentClass = "12",
                chapterNumber = 1,
                titleHindi = "साझेदारी लेखांकन - आधारभूत तत्व",
                titleEnglish = "Accounting for Partnership: Basic Concepts",
                introduction = "भारतीय साझेदारी अधिनियम 1932, साझेदारी संलेख (Partnership Deed), लाभ-हानि नियोजन खाता (P&L Appropriation A/c), और पूंजी खाते।",
                learningObjectives = "• साझेदारी की परिभाषा एवं विशेषताएं\n• साझेदारी संलेख की अनुपस्थिति में लागू होने वाले नियम\n• स्थिर एवं परिवर्तनशील पूंजी खातों में अंतर",
                textbookContent = """
### 1.1 साझेदारी का अर्थ (Meaning of Partnership)
भारतीय साझेदारी अधिनियम 1932 की धारा 4 के अनुसार: "साझेदारी उन व्यक्तियों का आपसी संबंध है जिन्होंने किसी ऐसे व्यवसाय के लाभों को बांटने का समझौता किया है, जो उन सबके द्वारा या उनमें से किसी एक द्वारा सबकी ओर से चलाया जाता है।"

---

### 1.2 साझेदारी संलेख के अभाव में नियम
यदि कोई संलेख न हो, तो:
1. लाभ और हानि बराबर अनुपात में बांटी जाएगी।
2. पूंजी पर कोई ब्याज नहीं दिया जाएगा।
3. आहरण (Drawings) पर कोई ब्याज नहीं लिया जाएगा।
4. साझेदार को ऋण पर 6% वार्षिक दर से ब्याज पाने का अधिकार है।
5. किसी साझेदार को वेतन या कमीशन नहीं दिया जाएगा।
                """.trimIndent(),
                summary = "साझेदारी में न्यूनतम 2 और अधिकतम 50 सदस्य हो सकते हैं। संलेख न होने पर ऋण पर 6% ब्याज मिलता है और लाभ-हानि बराबर बंटती है।",
                readingProgressPercent = 0,
                lastReadSection = "Introduction",
                lastReadTimestamp = 0L,
                totalPages = 22,
                lastReadPage = 1,
                isDownloaded = false
            ),

            // Class 12 Math
            ChapterEntity(
                id = "c12_math_ch1",
                subjectId = "c12_math",
                studentClass = "12",
                chapterNumber = 1,
                titleHindi = "संबंध एवं फलन (Relations & Functions)",
                titleEnglish = "Relations and Functions",
                introduction = "संबंधों के प्रकार: स्वतुल्य, सममित तथा संक्रामक संबंध। तुल्यता संबंध (Equivalence Relation) एवं फलनों के प्रकार (एकैकी तथा आच्छादक)।",
                learningObjectives = "• रिक्त एवं सार्वत्रिक संबंध समझना\n• स्वतुल्य, सममित व संक्रामक संबंध\n• तुल्यता संबंध का सत्यापन\n• एकैकी (Injective) और आच्छादक (Surjective) फलन",
                textbookContent = """
### 1.1 संबंधों के प्रकार (Types of Relations)
किसी समुच्चय A पर परिभाषित संबंध R:
1. **स्वतुल्य (Reflexive):** यदि प्रत्येक a ∈ A के लिए (a, a) ∈ R हो।
2. **सममित (Symmetric):** यदि (a, b) ∈ R से निष्कर्ष निकलता है कि (b, a) ∈ R हो।
3. **संक्रामक (Transitive):** यदि (a, b) ∈ R तथा (b, c) ∈ R से निष्कर्ष निकलता है कि (a, c) ∈ R हो।

> **तुल्यता संबंध (Equivalence Relation):**
> यदि कोई संबंध R स्वतुल्य, सममित और संक्रामक तीनों हो, तो उसे तुल्यता संबंध कहते हैं।

---

### 1.2 फलनों के प्रकार
- **एकैकी (One-One):** f(x₁) = f(x₂) होने पर सदैव x₁ = x₂ प्राप्त हो।
- **आच्छादक (Onto):** Y के प्रत्येक अवयव y के लिए X में कम से कम एक पूर्व-प्रतिबिंब x विद्यमान हो।
                """.trimIndent(),
                summary = "तुल्यता संबंध स्वतुल्य, सममित व संक्रामक होता है।",
                readingProgressPercent = 0,
                lastReadSection = "Introduction",
                lastReadTimestamp = 0L,
                totalPages = 18,
                lastReadPage = 1,
                isDownloaded = false
            ),

            // Class 12 Hindi
            ChapterEntity(
                id = "c12_hin_ch1",
                subjectId = "c12_hin_sci",
                studentClass = "12",
                chapterNumber = 1,
                titleHindi = "बातचीत (बालकृष्ण भट्ट)",
                titleEnglish = "Baatcheet",
                introduction = "भारतेंदु युग के प्रमुख निबंधकार बालकृष्ण भट्ट द्वारा रचित बातचीत की महत्ता एवं संवाद कौशल पर आधारित निबंध।",
                learningObjectives = "• वाक्शक्ति का महत्व समझना\n• बेन जॉनसन तथा एडिसन के विचार\n• असल बातचीत का स्वरूप",
                textbookContent = """
### 1.1 वाक्शक्ति का वरदान
ईश्वर ने मनुष्य को जो अनेक शक्तियां दी हैं, उनमें वाक्शक्ति एक अनुपम वरदान है। बोलने से ही मनुष्य के रूप का साक्षात्कार होता है (बेन जॉनसन)।

---

### 1.2 असल बातचीत
एडिसन के अनुसार असल बातचीत केवल दो व्यक्तियों के बीच ही हो सकती है, जब वे एक-दूसरे के सम्मुख अपना हृदय खोलते हैं।
                """.trimIndent(),
                summary = "वाक्शक्ति ईश्वर का अनुपम उपहार है। आत्म-संवाद बातचीत का सबसे उत्तम माध्यम है।",
                readingProgressPercent = 0,
                lastReadSection = "Introduction",
                lastReadTimestamp = 0L,
                totalPages = 12,
                lastReadPage = 1,
                isDownloaded = false
            ),

            // Class 12 English
            ChapterEntity(
                id = "c12_eng_ch1",
                subjectId = "c12_eng_sci",
                studentClass = "12",
                chapterNumber = 1,
                titleHindi = "Indian Civilization and Culture",
                titleEnglish = "Indian Civilization and Culture (M. Gandhi)",
                introduction = "Mahatma Gandhi highlights the unmatched moral and spiritual depth of Indian civilization.",
                learningObjectives = "• Appreciate Gandhi's views on civilization\n• Performance of duty and observance of morality\n• Real happiness lies in contentment",
                textbookContent = """
### 1.1 The Sound Foundation
Indian civilization has evolved to withstand the test of time. While western civilization promotes material cravings, Indian civilization elevates the moral being.

---

### 1.2 Restless Mind
Mind is a restless bird; the more it gets, the more it wants, and still remains unsatisfied. Our ancestors therefore counselled self-restraint.
                """.trimIndent(),
                summary = "Indian civilization values duty, morality, and inner contentment over material greed.",
                readingProgressPercent = 0,
                lastReadSection = "Introduction",
                lastReadTimestamp = 0L,
                totalPages = 14,
                lastReadPage = 1,
                isDownloaded = false
            ),

            // Class 12 Geography
            ChapterEntity(
                id = "c12_geo_ch1",
                subjectId = "c12_geo",
                studentClass = "12",
                chapterNumber = 1,
                titleHindi = "मानव भूगोल - प्रकृति एवं विषय क्षेत्र",
                titleEnglish = "Human Geography: Nature & Scope",
                introduction = "मानव और पर्यावरण के संबंधों का भौगोलिक अध्ययन। निश्चयवाद, संभववाद और ग्रिफिथ टेलर का नव-निश्चयवाद।",
                learningObjectives = "• मानव भूगोल की परिभाषाएं\n• पर्यावरणीय निश्चयवाद बनाम संभववाद\n• नव-निश्चयवाद (रुको और जाओ)",
                textbookContent = """
### 1.1 मानव भूगोल का अर्थ
मानव भूगोल भौतिक पर्यावरण और मानव समाज के अंतर्संबंधों का संश्लेषित अध्ययन है (फ्रेडरिक रेड्जेल)।

---

### 1.2 नव-निश्चयवाद (Neo-Determinism)
ग्रिफिथ टेलर ने 'रुको और जाओ निश्चयवाद' की संकल्पना प्रस्तुत की जो सतत पोषणीय विकास का आधार है।
                """.trimIndent(),
                summary = "मानव भूगोल मानव व प्रकृति के सामंजस्य को रेखांकित करता है।",
                readingProgressPercent = 0,
                lastReadSection = "Introduction",
                lastReadTimestamp = 0L,
                totalPages = 14,
                lastReadPage = 1,
                isDownloaded = false
            ),

            // Class 12 Political Science
            ChapterEntity(
                id = "c12_pol_ch1",
                subjectId = "c12_pol",
                studentClass = "12",
                chapterNumber = 1,
                titleHindi = "राष्ट्र-निर्माण की चुनौतियाँ",
                titleEnglish = "Challenges of Nation Building",
                introduction = "स्वतंत्रता के समय भारत के सामने उपस्थित तीन मुख्य चुनौतियाँ और 565 देशी रियासतों का एकीकरण।",
                learningObjectives = "• 1947 विभाजन की त्रासदी\n• एकता, लोकतंत्र और विकास की चुनौतियाँ\n• सरदार वल्लभभाई पटेल की ऐतिहासिक भूमिका",
                textbookContent = """
### 1.1 तीन प्रमुख चुनौतियाँ
1. एकता और अखंडता बनाए रखना
2. लोकतांत्रिक प्रणाली की स्थापना
3. समतामूलक समग्र विकास

---

### 1.2 रियासतों का विलय
सरदार पटेल की दूरदर्शिता से 565 देशी रियासतों का भारतीय संघ में ऐतिहासिक विलय संपन्न हुआ।
                """.trimIndent(),
                summary = "स्वतंत्र भारत ने एकता, लोकतंत्र और विकास की चुनौतियों पर विजय पाई।",
                readingProgressPercent = 0,
                lastReadSection = "Introduction",
                lastReadTimestamp = 0L,
                totalPages = 16,
                lastReadPage = 1,
                isDownloaded = false
            ),

            // Class 12 Business Studies
            ChapterEntity(
                id = "c12_bst_ch1",
                subjectId = "c12_bst",
                studentClass = "12",
                chapterNumber = 1,
                titleHindi = "प्रबंध की प्रकृति एवं महत्व",
                titleEnglish = "Nature & Significance of Management",
                introduction = "प्रबंध की अवधारणा, प्रभावशीलता बनाम कार्यकुशलता, और समन्वय (Coordination) की भूमिका।",
                learningObjectives = "• प्रबंध की परिभाषा व विशेषताएं\n• प्रभावशीलता और कार्यकुशलता\n• समन्वय - प्रबंध का सार",
                textbookContent = """
### 1.1 प्रबंध का अर्थ
प्रबंध संगठनात्मक लक्ष्यों को प्रभावपूर्ण और कुशलतापूर्वक प्राप्त करने की प्रक्रिया है।

---

### 1.2 समन्वय (Coordination)
समन्वय सभी प्रबंधकीय कार्यों को जोड़ने वाला अनिवार्य धागा है।
                """.trimIndent(),
                summary = "प्रबंध गतिशील और बहुआयामी है। समन्वय प्रबंध का सार है।",
                readingProgressPercent = 0,
                lastReadSection = "Introduction",
                lastReadTimestamp = 0L,
                totalPages = 16,
                lastReadPage = 1,
                isDownloaded = false
            )
        )
        database.chapterDao().insertChapters(chapters)

        // 4. Notes Section (Complete, Short, Important Points, Easy Explanation, Exam Notes)
        val notes = listOf(
            NoteEntity(
                id = "note_c10_sci_1",
                chapterId = "c10_sci_ch1",
                subjectId = "c10_sci",
                title = "⚡ त्वरित पुनरावलोकन (Quick Revision Notes)",
                noteType = "short",
                content = """
• **रासायनिक अभिक्रिया:** पुराने बंध टूटकर नए बंध बनते हैं।
• **समीकरण संतुलन:** द्रव्यमान संरक्षण का नियम (LHS परमाणु = RHS परमाणु)।
• **ऊष्माक्षेपी (Exothermic):** ऊष्मा निकलती है (उदा. श्वसन, प्राकृतिक गैस दहन)।
• **ऊष्माशोषी (Endothermic):** ऊष्मा अवशोषित होती है (उदा. प्रकाश संश्लेषण)।
• **विकृतगंधिता बचाव:** नाइट्रोजन गैस, एंटीऑक्सीडेंट्स, एयरटाइट कंटेनर।
                """.trimIndent()
            ),
            NoteEntity(
                id = "note_c10_sci_2",
                chapterId = "c10_sci_ch1",
                subjectId = "c10_sci",
                title = "⭐ महत्वपूर्ण बिंदु (Important Points for Board Exam)",
                noteType = "important_points",
                content = """
1. बिना बुझे चूने (CaO) पर जल डालने पर बुझा चूना Ca(OH)₂ बनता है तथा अत्यधिक ऊष्मा निकलती है।
2. दीवारों पर सफेदी करने के 2-3 दिन बाद चमक आने का कारण वायुमंडलीय CO₂ से क्रिया कर CaCO₃ (कैल्शियम कार्बोनेट) की पतली परत बनना है।
3. श्वसन एक ऊष्माक्षेपी अभिक्रिया है (ग्लूकोज + O₂ → CO₂ + H₂O + ऊर्जा)।
4. सिल्वर क्लोराइड (AgCl) को सूर्य के प्रकाश में रखने पर धूसर रंग का Ag धातु बनता है, जिसका उपयोग श्याम-श्वेत फोटोग्राफी में होता है।
                """.trimIndent()
            ),
            NoteEntity(
                id = "note_c10_sci_3",
                chapterId = "c10_sci_ch1",
                subjectId = "c10_sci",
                title = "🧠 सरल भाषा में समझें (Easy Explanation)",
                noteType = "easy_explanation",
                content = """
लोहे में जंग लगना कोई जादू नहीं बल्कि एक रासायनिक प्रक्रिया है। जब लोहा खुली हवा की नमी (जलवाष्प) और ऑक्सीजन के संपर्क में आता है, तो वह धीरे-धीरे जलकृत आयरन ऑक्साइड (Fe₂O₃·xH₂O) बना लेता है, जिसे हम जंग कहते हैं। 
चिप्स के पैकेट में नाइट्रोजन गैस इसलिए भरी जाती है ताकि चिप्स में मौजूद तेल हवा की ऑक्सीजन से क्रिया करके बदबूदार न हो जाए!
                """.trimIndent()
            ),
            NoteEntity(
                id = "note_c10_sci_4",
                chapterId = "c10_sci_ch1",
                subjectId = "c10_sci",
                title = "📌 बिहार बोर्ड परीक्षा स्पेशल नोट्स (Exam Top Notes)",
                noteType = "exam_notes",
                content = """
विगत वर्षों में बार-बार पूछे गए प्रश्न:
1. रेडॉक्स अभिक्रिया क्या है? उदाहरण देकर समझाएं। (5 अंक)
2. संतुलित रासायनिक समीकरण क्या है? इसे संतुलित करना क्यों आवश्यक है? (2 अंक)
3. वियोजन अभिक्रिया को संयोजन अभिक्रिया के विपरीत क्यों कहा जाता है? (2 अंक)
4. संक्षारण और विकृतगंधिता में अंतर स्पष्ट करें। (3 अंक)
                """.trimIndent()
            )
        )
        database.noteDao().insertNotes(notes)

        // 5. Practice Questions & MCQs & PYQs
        val questions = listOf(
            QuestionEntity(
                id = "q_c10_sci_1",
                chapterId = "c10_sci_ch1",
                subjectId = "c10_sci",
                studentClass = "10",
                questionType = "MCQ",
                questionText = "मैग्नीशियम रिबन को वायु में जलाने पर किस रंग की ज्वाला उत्पन्न होती है?",
                optionA = "लाल",
                optionB = "चमकदार श्वेत (Bright White)",
                optionC = "नीली",
                optionD = "पीली",
                correctAnswer = "B",
                explanation = "मैग्नीशियम रिबन वायु की ऑक्सीजन के साथ अत्यंत तीव्र क्रिया करके चमकदार श्वेत (Dazzling white) लौ के साथ जलता है और मैग्नीशियम ऑक्साइड (MgO) का श्वेत चूर्ण बनाता है।"
            ),
            QuestionEntity(
                id = "q_c10_sci_2",
                chapterId = "c10_sci_ch1",
                subjectId = "c10_sci",
                studentClass = "10",
                questionType = "MCQ",
                questionText = "श्वसन किस प्रकार की रासायनिक अभिक्रिया है?",
                optionA = "ऊष्माक्षेपी (Exothermic)",
                optionB = "ऊष्माशोषी (Endothermic)",
                optionC = "संयोजन",
                optionD = "अपघटन",
                correctAnswer = "A",
                explanation = "श्वसन के दौरान कोशिकाओं में ग्लूकोज ऑक्सीजन के साथ क्रिया करके ऊर्जा (ATP) उत्पन्न करता है, अतः यह एक ऊष्माक्षेपी अभिक्रिया है।"
            ),
            QuestionEntity(
                id = "q_c10_sci_3",
                chapterId = "c10_sci_ch1",
                subjectId = "c10_sci",
                studentClass = "10",
                questionType = "MCQ",
                questionText = "चिप्स की थैली में कौन सी अक्रिय गैस भरी जाती है ताकि विकृतगंधिता न हो?",
                optionA = "ऑक्सीजन",
                optionB = "हाइड्रोजन",
                optionC = "नाइट्रोजन (Nitrogen)",
                optionD = "क्लोरीन",
                correctAnswer = "C",
                explanation = "नाइट्रोजन एक कम क्रियाशील गैस है जो तैलीय खाद्य पदार्थों को उपचयित (Oxidize) होने से बचाती है।"
            ),
            QuestionEntity(
                id = "q_c10_sci_4",
                chapterId = "c10_sci_ch1",
                subjectId = "c10_sci",
                studentClass = "10",
                questionType = "MCQ",
                questionText = "सिल्वर क्लोराइड (AgCl) का रंग कैसा होता है जब इसे सूर्य के प्रकाश में रखा जाता है?",
                optionA = "श्वेत से धूसर (Grey)",
                optionB = "नीला",
                optionC = "काला",
                optionD = "गुलाबी",
                correctAnswer = "A",
                explanation = "2AgCl —(सूर्य का प्रकाश)→ 2Ag + Cl₂। प्रकाश अपघटन के कारण श्वेत AgCl धूसर रंग के धात्विक Ag में बदल जाता है।"
            ),
            QuestionEntity(
                id = "q_c10_sci_5",
                chapterId = "c10_sci_ch1",
                subjectId = "c10_sci",
                studentClass = "10",
                questionType = "SHORT",
                questionText = "संयोजन अभिक्रिया और वियोजन अभिक्रिया में मुख्य अंतर क्या है? प्रत्येक का एक संतुलित समीकरण लिखें।",
                correctAnswer = "संयोजन में दो या अधिक अभिकारक मिलकर एक उत्पाद बनाते हैं (C + O₂ → CO₂)। वियोजन में एक अभिकारक टूटकर दो या अधिक उत्पाद बनाता है (CaCO₃ → CaO + CO₂)।",
                explanation = "दोनों अभिक्रियाएं परस्पर विपरीत प्रकृति की होती हैं।"
            ),
            QuestionEntity(
                id = "q_c10_sci_pyq_2024",
                chapterId = "c10_sci_ch1",
                subjectId = "c10_sci",
                studentClass = "10",
                questionType = "PYQ",
                questionText = "[BSEB 2024] Fe₂O₃ + 2Al → Al₂O₃ + 2Fe यह किस प्रकार की रासायनिक अभिक्रिया है?",
                optionA = "संयोजन अभिक्रिया",
                optionB = "द्विविस्थापन अभिक्रिया",
                optionC = "वियोजन अभिक्रिया",
                optionD = "विस्थापन अभिक्रिया (Displacement)",
                correctAnswer = "D",
                year = 2024,
                explanation = "यहाँ अधिक क्रियाशील धातु एल्युमिनियम (Al), आयरन ऑक्साइड में से आयरन (Fe) को विस्थापित कर रही है।"
            ),
            QuestionEntity(
                id = "q_c10_sci_pyq_2025",
                chapterId = "c10_sci_ch1",
                subjectId = "c10_sci",
                studentClass = "10",
                questionType = "PYQ",
                questionText = "[BSEB 2025 Model] बुझे हुए चूने का रासायनिक सूत्र क्या है?",
                optionA = "CaO",
                optionB = "Ca(OH)₂",
                optionC = "CaCO₃",
                optionD = "CaCl₂",
                correctAnswer = "B",
                year = 2025,
                explanation = "बिना बुझा चूना CaO होता है। जल मिलाने पर बुझा चूना Ca(OH)₂ (कैल्शियम हाइड्रॉक्साइड) बनता है।"
            )
        )
        database.questionDao().insertQuestions(questions)

        // 6. Formulas & Quick Revision Section
        val formulas = listOf(
            FormulaEntity("f_math_1", "c10_math", "10", "वास्तविक संख्याएं", "HCF व LCM संबंध", "HCF(a, b) × LCM(a, b) = a × b", "दो संख्याओं का गुणनफल उनके महत्तम समापवर्तक और लघुत्तम समापवर्त्य के गुणनफल के बराबर होता है।", "अंकगणित की आधारभूत प्रमेय"),
            FormulaEntity("f_math_2", "c10_math", "10", "द्विघात समीकरण", "द्विघाती सूत्र (श्रीधराचार्य सूत्र)", "x = [-b ± √(b² - 4ac)] / 2a", "ax² + bx + c = 0 के मूल ज्ञात करने का सार्वत्रिक सूत्र। विविक्तकर D = b² - 4ac", "D > 0 वास्तविक व भिन्न मूल, D = 0 बराबर मूल"),
            FormulaEntity("f_math_3", "c10_math", "10", "त्रिकोणमिति", "त्रिकोणमितीय सर्वसमिकाएं", "sin²θ + cos²θ = 1\n1 + tan²θ = sec²θ\n1 + cot²θ = cosec²θ", "सभी त्रिकोणमितीय अनुपातों के मूल संबंध।", "त्रिकोणमिति आधार"),
            FormulaEntity("f_sci_1", "c10_sci", "10", "प्रकाश परावर्तन", "दर्पण सूत्र (Mirror Formula)", "1/f = 1/v + 1/u", "जहाँ f = फोकस दूरी, v = प्रतिबिंब दूरी, u = बिंब (वस्तु) दूरी। आवर्धन m = -v/u = h'/h", "चिह्न परिपाटी आवश्यक"),
            FormulaEntity("f_sci_2", "c10_sci", "10", "विद्युत (Electricity)", "ओम का नियम (Ohm's Law)", "V = I × R", "नियत ताप पर किसी चालक के सिरों का विभवांतर उसमें प्रवाहित धारा के समानुपाती होता है। प्रतिरोध की इकाई ओम (Ω) है।", "V = Volt, I = Ampere, R = Ohm"),
            FormulaEntity("f_phy12_1", "c12_phy", "12", "स्थिर वैद्युतिकी", "कूलॉम का नियम", "F = (1 / 4πε₀) × (|q₁ q₂| / r²)", "दो बिंदु आवेशों के मध्य लगने वाला आकर्षण अथवा प्रतिकर्षण बल। ε₀ = 8.854 × 10⁻¹² C²N⁻¹m⁻²", "N (न्यूटन)"),
            FormulaEntity("f_phy12_2", "c12_phy", "12", "स्थिर वैद्युतिकी", "गाउस का प्रमेय", "Φ = ∮ E·dA = q_in / ε₀", "किसी बंद पृष्ठ से संबद्ध कुल विद्युत फ्लक्स उस पृष्ठ द्वारा परिबद्ध कुल आवेश का 1/ε₀ गुना होता है।", "वेबर या N·m²/C")
        )
        database.formulaDao().insertFormulas(formulas)

        // 7. Initial Notifications
        val notifications = listOf(
            NotificationEntity(
                title = "📚 नए अध्याय डिजिटल रूप में उपलब्ध!",
                message = "कक्षा 10 विज्ञान एवं कक्षा 12 भौतिकी के डिजिटल नोट्स एवं प्रश्न बैंक अपडेट कर दिए गए हैं।",
                type = "chapter",
                isRead = false
            ),
            NotificationEntity(
                title = "🎯 आज का दैनिक अभ्यास लक्ष्य",
                message = "आज 1 अध्याय पढ़ें और 20 MCQs हल करके अपनी अध्ययन निरंतरता (Streak) बनाए रखें!",
                type = "quiz",
                isRead = false
            ),
            NotificationEntity(
                title = "🏆 सिन्हा जी स्टडी सेंटर में आपका स्वागत है",
                message = "डिजिटल पाठ्यपुस्तक, हस्तलिखित नोट्स, पिछले वर्षों के प्रश्न (PYQ) और टेस्ट सीरीज़ अब आपकी जेब में!",
                type = "general",
                isRead = true
            )
        )
        notifications.forEach { database.notificationDao().insertNotification(it) }
    }
}
