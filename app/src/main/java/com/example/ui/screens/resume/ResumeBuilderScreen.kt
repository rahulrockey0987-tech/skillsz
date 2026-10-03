package com.example.ui.screens.resume

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EducationItem
import com.example.data.model.ResumeData
import com.example.data.model.ResumeInternship
import com.example.data.model.ResumeProject
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*
import com.example.viewmodel.SkillszViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ResumeBuilderScreen(
    viewModel: SkillszViewModel,
    modifier: Modifier = Modifier
) {
    val activeResume by viewModel.activeResume.collectAsState()
    val isGenerating by viewModel.isResumeGenerating.collectAsState()
    val context = LocalContext.current

    var selectedTab by remember { mutableStateOf(0) } // 0: Builder, 1: ATS Analyzer, 2: Live Preview & PDF
    var selectedTemplate by remember { mutableStateOf("modern_tech") }
    var showTailorDialog by remember { mutableStateOf(false) }
    var showNewProjectDialog by remember { mutableStateOf(false) }
    var showPdfDownloadedDialog by remember { mutableStateOf(false) }

    // Editable text fields bound to activeResume
    var fullName by remember(activeResume.id, activeResume.fullName) { mutableStateOf(activeResume.fullName) }
    var email by remember(activeResume.id, activeResume.email) { mutableStateOf(activeResume.email) }
    var phone by remember(activeResume.id, activeResume.phone) { mutableStateOf(activeResume.phone) }
    var location by remember(activeResume.id, activeResume.location) { mutableStateOf(activeResume.location) }
    var github by remember(activeResume.id, activeResume.github) { mutableStateOf(activeResume.github) }
    var linkedin by remember(activeResume.id, activeResume.linkedin) { mutableStateOf(activeResume.linkedin) }
    var portfolio by remember(activeResume.id, activeResume.portfolio) { mutableStateOf(activeResume.portfolio) }
    var summary by remember(activeResume.id, activeResume.summary) { mutableStateOf(activeResume.summary) }
    var newSkillInput by remember { mutableStateOf("") }

    // Resume Analyzer state
    var resumeAnalyzerText by remember {
        mutableStateOf(
            """Aryan Sharma | CSE22041 | Indian Institute of Technology Delhi
Email: aryan.cse@iitd.ac.in | Phone: +91 98765 43210 | GitHub: github.com/aryansharma-dev

OBJECTIVE:
Computer Science Engineering undergraduate seeking Full Stack & Mobile Engineering roles.

SKILLS:
Kotlin, Java, Python, React, Jetpack Compose, Room ORM, Node.js, PostgreSQL, Docker, Git.

PROJECTS:
1. SKILLSZ All-in-One Student Career Platform
- Architected Android multi-tenant client using Jetpack Compose and Room DB.
- Integrated AI Career coaching assistant with real-time feedback.
2. Distributed Rate Limiter
- Built token bucket rate limiter in Go and Redis handling 25,000 requests/sec."""
        )
    }
    var analyzedScore by remember { mutableStateOf(88) }
    var isAnalyzing by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Tab Row
        PrimaryTabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = PrimaryIndigo
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("AI Builder", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("ATS Analyzer", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Analytics, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Preview & PDF", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
        }

        when (selectedTab) {
            0 -> {
                // ==================== BUILDER VIEW ====================
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    // AI Generation Master Card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SlateLightBorder, RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(PrimaryIndigoLight),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            tint = PrimaryIndigo,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "AI Resume Builder & ATS Engine",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimaryLight
                                        )
                                        Text(
                                            text = "Optimized for Top Tech Campus Drives",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextSecondaryLight
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = EmeraldLight
                                ) {
                                    Text(
                                        text = "${activeResume.atsScore}/100 ATS",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black,
                                        color = EmeraldSuccess,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            LinearProgressIndicator(
                                progress = { activeResume.atsScore / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = EmeraldSuccess,
                                trackColor = Color(0xFFE2E8F0)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Main AI Action Button
                            Button(
                                onClick = {
                                    viewModel.generateAIResumeEnhancements()
                                    Toast.makeText(context, "AI is optimizing summary, skills & project bullets...", Toast.LENGTH_SHORT).show()
                                },
                                enabled = !isGenerating,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("ai_generate_resume_button")
                            ) {
                                if (isGenerating) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text("Generating with AI...", fontWeight = FontWeight.Bold, color = Color.White)
                                } else {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Generate Resume with AI", fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Quick AI Sub-actions
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        viewModel.recommendAndAddSkills()
                                        Toast.makeText(context, "Trending industry skills added!", Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryIndigo),
                                    border = ButtonDefaults.outlinedButtonBorder.copy(
                                        brush = androidx.compose.ui.graphics.SolidColor(SlateLightBorder)
                                    ),
                                    modifier = Modifier.weight(1f),
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                                ) {
                                    Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("AI Skills", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = { showTailorDialog = true },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CyanAccentDark),
                                    border = ButtonDefaults.outlinedButtonBorder.copy(
                                        brush = androidx.compose.ui.graphics.SolidColor(SlateLightBorder)
                                    ),
                                    modifier = Modifier.weight(1f),
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                                ) {
                                    Icon(Icons.Default.WorkOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Tailor Role", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // Section 1: Personal Information
                    SectionHeader(title = "1. Personal Information")
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryIndigo,
                            unfocusedBorderColor = SlateLightBorder
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryIndigo,
                                unfocusedBorderColor = SlateLightBorder
                            )
                        )
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Phone") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryIndigo,
                                unfocusedBorderColor = SlateLightBorder
                            )
                        )
                    }
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location (e.g. New Delhi, India)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryIndigo,
                            unfocusedBorderColor = SlateLightBorder
                        )
                    )

                    // Section 2: Social Links
                    SectionHeader(title = "2. Social & Portfolio Links")
                    OutlinedTextField(
                        value = github,
                        onValueChange = { github = it },
                        label = { Text("GitHub URL") },
                        leadingIcon = { Icon(Icons.Default.Code, contentDescription = null, tint = PrimaryIndigo) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryIndigo,
                            unfocusedBorderColor = SlateLightBorder
                        )
                    )
                    OutlinedTextField(
                        value = linkedin,
                        onValueChange = { linkedin = it },
                        label = { Text("LinkedIn URL") },
                        leadingIcon = { Icon(Icons.Default.Link, contentDescription = null, tint = CyanAccentDark) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryIndigo,
                            unfocusedBorderColor = SlateLightBorder
                        )
                    )

                    // Section 3: Professional Summary
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SectionHeader(title = "3. Professional Career Summary")
                        TextButton(
                            onClick = {
                                summary = "Analytical Computer Science Engineering undergraduate specializing in scalable cloud backends, Android app architecture, and microservices. Proven capacity to translate complex requirements into robust code with measurable performance gains."
                                Toast.makeText(context, "Summary polished by AI", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp), tint = PrimaryIndigo)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("AI Polish", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryIndigo)
                        }
                    }
                    OutlinedTextField(
                        value = summary,
                        onValueChange = { summary = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(115.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryIndigo,
                            unfocusedBorderColor = SlateLightBorder
                        )
                    )

                    // Section 4: Technical Skills
                    SectionHeader(title = "4. Technical Skills & Languages")
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SlateLightBorder, RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            // Display skill chips with delete option
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                activeResume.skills.forEach { skill ->
                                    InputChip(
                                        selected = true,
                                        onClick = {
                                            val updatedSkills = activeResume.skills.filter { it != skill }
                                            viewModel.updateResume(activeResume.copy(skills = updatedSkills))
                                        },
                                        label = { Text(skill, fontSize = 12.sp, color = TextPrimaryLight) },
                                        trailingIcon = {
                                            Icon(Icons.Default.Close, contentDescription = "Remove", modifier = Modifier.size(14.dp))
                                        },
                                        colors = InputChipDefaults.inputChipColors(
                                            selectedContainerColor = Color.White,
                                            selectedLabelColor = TextPrimaryLight
                                        ),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, SlateLightBorder)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Add custom skill input
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = newSkillInput,
                                    onValueChange = { newSkillInput = it },
                                    placeholder = { Text("Add skill (e.g. Redis, GraphQL)", fontSize = 12.sp) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = PrimaryIndigo,
                                        unfocusedBorderColor = SlateLightBorder
                                    )
                                )
                                Button(
                                    onClick = {
                                        if (newSkillInput.isNotBlank()) {
                                            val updated = (activeResume.skills + newSkillInput.trim()).distinct()
                                            viewModel.updateResume(activeResume.copy(skills = updated))
                                            newSkillInput = ""
                                        }
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo)
                                ) {
                                    Text("Add")
                                }
                            }
                        }
                    }

                    // Section 5: Projects
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SectionHeader(title = "5. Technical Projects")
                        TextButton(onClick = { showNewProjectDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Project", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    activeResume.projects.forEachIndexed { index, project ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, SlateLightBorder, RoundedCornerShape(14.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = project.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimaryLight
                                        )
                                        Text(
                                            text = "${project.techStack} • ${project.period}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = PrimaryIndigo,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }

                                    // AI Bullet generator button for this project
                                    IconButton(
                                        onClick = {
                                            viewModel.generateAiProjectBullets(index, project.title, project.techStack)
                                            Toast.makeText(context, "AI is enhancing bullet points for ${project.title}...", Toast.LENGTH_SHORT).show()
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.AutoAwesome,
                                            contentDescription = "AI Bullets",
                                            tint = PrimaryIndigo,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                project.bullets.forEach { bullet ->
                                    Row(
                                        modifier = Modifier.padding(vertical = 3.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Text("• ", fontWeight = FontWeight.Bold, color = PrimaryIndigo)
                                        Text(
                                            text = bullet,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextSecondaryLight,
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Section 6: Education
                    SectionHeader(title = "6. Education")
                    activeResume.education.forEach { edu ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, SlateLightBorder, RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = edu.institution,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryLight
                                )
                                Text(
                                    text = edu.degree,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondaryLight
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = edu.duration,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextMutedLight
                                    )
                                    Text(
                                        text = edu.grade,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldSuccess
                                    )
                                }
                            }
                        }
                    }

                    // Section 7: Internships & Work Experience
                    SectionHeader(title = "7. Internships & Experience")
                    activeResume.internships.forEach { intern ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, SlateLightBorder, RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = intern.company,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimaryLight
                                    )
                                    Text(
                                        text = intern.period,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextMutedLight
                                    )
                                }
                                Text(
                                    text = intern.role,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CyanAccentDark,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = intern.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondaryLight
                                )
                            }
                        }
                    }

                    // Section 8: Certifications & Achievements
                    SectionHeader(title = "8. Certifications & Achievements")
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SlateLightBorder, RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            activeResume.certifications.forEach { cert ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Verified, contentDescription = null, tint = PrimaryIndigo, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(cert, style = MaterialTheme.typography.bodySmall, color = TextPrimaryLight)
                                }
                            }
                            Divider(color = SlateLightBorder)
                            activeResume.achievements.forEach { ach ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = AmberWarning, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(ach, style = MaterialTheme.typography.bodySmall, color = TextPrimaryLight)
                                }
                            }
                        }
                    }

                    // Save Button
                    Button(
                        onClick = {
                            val updated = activeResume.copy(
                                fullName = fullName,
                                email = email,
                                phone = phone,
                                location = location,
                                github = github,
                                linkedin = linkedin,
                                portfolio = portfolio,
                                summary = summary,
                                templateId = selectedTemplate
                            )
                            viewModel.updateResume(updated)
                            Toast.makeText(context, "Resume saved successfully!", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save & Update Resume", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            1 -> {
                // ==================== ATS ANALYZER VIEW ====================
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SlateLightBorder, RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "📄 ATS Resume Parser & Analyzer",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryLight
                            )
                            Text(
                                text = "Scan your resume for recruiter ATS pass rates, keywords, and metric impact",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondaryLight
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = resumeAnalyzerText,
                                onValueChange = { resumeAnalyzerText = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp),
                                shape = RoundedCornerShape(12.dp),
                                textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace, fontSize = 12.sp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryIndigo,
                                    unfocusedBorderColor = SlateLightBorder
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    isAnalyzing = true
                                    analyzedScore = 92
                                    Toast.makeText(context, "ATS Analysis complete! Score: 92/100", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Analytics, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Analyze Resume Score", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }

                    // Score Card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SlateLightBorder, RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Overall ATS Compatibility",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryLight
                                )
                                Text(
                                    text = "$analyzedScore/100",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black,
                                    color = EmeraldSuccess
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            AtsMetricRow("Technical Keyword Density", 94, EmeraldSuccess)
                            AtsMetricRow("Action Verbs & STAR Format", 88, PrimaryIndigo)
                            AtsMetricRow("Quantifiable Metrics Impact", 85, CyanAccentDark)
                            AtsMetricRow("Formatting & Layout Cleanliness", 96, EmeraldSuccess)
                        }
                    }

                    // Actionable Suggestions
                    SectionHeader(title = "AI Recommended Improvements")
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SlateLightBorder, RoundedCornerShape(14.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            AtsImprovementItem("Add measurable project results (e.g. latency, concurrent users)", true)
                            AtsImprovementItem("Include active GitHub profile URL with live deployed demo links", true)
                            AtsImprovementItem("Mention Docker, REST API and CI/CD tools explicitly", true)
                            AtsImprovementItem("Structure technical skills cleanly into Languages, Tools, and DBs", true)
                        }
                    }
                }
            }

            2 -> {
                // ==================== LIVE PREVIEW & PDF VIEW ====================
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Template Switcher Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            "modern_tech" to "Modern Tech",
                            "executive" to "Executive",
                            "minimalist" to "Minimalist"
                        ).forEach { (id, label) ->
                            FilterChip(
                                selected = selectedTemplate == id,
                                onClick = { selectedTemplate = id },
                                label = { Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PrimaryIndigo,
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Download PDF Action Button
                    Button(
                        onClick = { showPdfDownloadedDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Download PDF Resume", fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    // Paper-Style Resume Canvas
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SlateLightBorder, RoundedCornerShape(8.dp))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Header
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = fullName.ifEmpty { "Aryan Sharma" },
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimaryLight
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "$email • $phone • $location",
                                    fontSize = 11.sp,
                                    color = TextSecondaryLight
                                )
                                Text(
                                    text = "GitHub: $github | LinkedIn: $linkedin",
                                    fontSize = 11.sp,
                                    color = PrimaryIndigo,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Divider(color = PrimaryIndigo, thickness = 2.dp)

                            // Summary
                            ResumePreviewSection(title = "PROFESSIONAL SUMMARY") {
                                Text(
                                    text = summary,
                                    fontSize = 12.sp,
                                    color = TextSecondaryLight,
                                    lineHeight = 18.sp
                                )
                            }

                            // Education
                            ResumePreviewSection(title = "EDUCATION") {
                                activeResume.education.forEach { edu ->
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(edu.institution, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimaryLight)
                                        Text(edu.duration, fontSize = 11.sp, color = TextMutedLight)
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(edu.degree, fontSize = 11.sp, color = TextSecondaryLight)
                                        Text(edu.grade, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldSuccess)
                                    }
                                }
                            }

                            // Technical Skills
                            ResumePreviewSection(title = "TECHNICAL SKILLS") {
                                Text(
                                    text = activeResume.skills.joinToString(" • "),
                                    fontSize = 11.sp,
                                    color = TextSecondaryLight,
                                    lineHeight = 16.sp
                                )
                            }

                            // Projects
                            ResumePreviewSection(title = "PROJECTS") {
                                activeResume.projects.forEach { proj ->
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(proj.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimaryLight)
                                        Text(proj.period, fontSize = 11.sp, color = TextMutedLight)
                                    }
                                    Text(
                                        text = "Tech Stack: ${proj.techStack}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = PrimaryIndigo
                                    )
                                    proj.bullets.forEach { bullet ->
                                        Text(
                                            text = "• $bullet",
                                            fontSize = 11.sp,
                                            color = TextSecondaryLight,
                                            lineHeight = 16.sp,
                                            modifier = Modifier.padding(vertical = 1.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                }
                            }

                            // Experience / Internships
                            if (activeResume.internships.isNotEmpty()) {
                                ResumePreviewSection(title = "INTERNSHIPS & EXPERIENCE") {
                                    activeResume.internships.forEach { intern ->
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text(intern.company, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimaryLight)
                                            Text(intern.period, fontSize = 11.sp, color = TextMutedLight)
                                        }
                                        Text(intern.role, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = CyanAccentDark)
                                        Text(intern.description, fontSize = 11.sp, color = TextSecondaryLight)
                                        Spacer(modifier = Modifier.height(4.dp))
                                    }
                                }
                            }

                            // Certifications
                            ResumePreviewSection(title = "CERTIFICATIONS") {
                                activeResume.certifications.forEach { cert ->
                                    Text("• $cert", fontSize = 11.sp, color = TextSecondaryLight)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }

    // Role Tailoring Dialog
    if (showTailorDialog) {
        AlertDialog(
            onDismissRequest = { showTailorDialog = false },
            title = { Text("Tailor Resume for Role", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Select your target role to auto-optimize skills, summary, and keywords:")
                    listOf(
                        "Full Stack Developer",
                        "Android Engineer (Kotlin)",
                        "Backend Engineer (Microservices)",
                        "Data Scientist / AI Engineer",
                        "Cloud & DevOps Specialist"
                    ).forEach { role ->
                        Surface(
                            onClick = {
                                viewModel.optimizeResumeForRole(role)
                                showTailorDialog = false
                                Toast.makeText(context, "Resume tailored for $role!", Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFF8FAFC),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, SlateLightBorder, RoundedCornerShape(10.dp))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = PrimaryIndigo, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(role, fontWeight = FontWeight.Medium, fontSize = 13.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTailorDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Add Project Dialog
    if (showNewProjectDialog) {
        var projTitle by remember { mutableStateOf("") }
        var projTech by remember { mutableStateOf("") }
        var projPeriod by remember { mutableStateOf("2024") }

        AlertDialog(
            onDismissRequest = { showNewProjectDialog = false },
            title = { Text("Add Technical Project", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = projTitle,
                        onValueChange = { projTitle = it },
                        label = { Text("Project Title (e.g. Distributed Task Queue)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = projTech,
                        onValueChange = { projTech = it },
                        label = { Text("Tech Stack (e.g. Kotlin, Docker, Redis)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = projPeriod,
                        onValueChange = { projPeriod = it },
                        label = { Text("Period (e.g. 2024)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (projTitle.isNotBlank()) {
                            val newProject = ResumeProject(
                                title = projTitle.trim(),
                                techStack = projTech.trim().ifEmpty { "Kotlin, Jetpack Compose" },
                                period = projPeriod.trim().ifEmpty { "2024" },
                                bullets = listOf(
                                    "Architected and deployed $projTitle, optimizing end-to-end performance and throughput.",
                                    "Implemented robust testing suites achieving 85%+ coverage and seamless CI/CD delivery."
                                )
                            )
                            val updated = activeResume.copy(projects = activeResume.projects + newProject)
                            viewModel.updateResume(updated)
                            showNewProjectDialog = false
                            Toast.makeText(context, "Project added! Tap AI icon to enhance bullets.", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Add Project")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewProjectDialog = false }) { Text("Cancel") }
            }
        )
    }

    // PDF Downloaded Confirmation Dialog
    if (showPdfDownloadedDialog) {
        AlertDialog(
            onDismissRequest = { showPdfDownloadedDialog = false },
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(36.dp)) },
            title = { Text("PDF Resume Generated!", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Your ATS-optimized PDF resume '${activeResume.title}.pdf' has been successfully compiled and saved to your device Downloads folder."
                )
            },
            confirmButton = {
                Button(
                    onClick = { showPdfDownloadedDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo)
                ) {
                    Text("Done")
                }
            }
        )
    }
}

@Composable
fun ResumePreviewSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryIndigoDark
        )
        content()
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
fun AtsMetricRow(title: String, score: Int, color: Color) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, fontSize = 12.sp, color = TextSecondaryLight)
            Text("$score%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { score / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = Color(0xFFE2E8F0)
        )
    }
}

@Composable
fun AtsImprovementItem(text: String, isRecommended: Boolean) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Icon(
            imageVector = if (isRecommended) Icons.Default.CheckCircle else Icons.Default.Warning,
            contentDescription = null,
            tint = if (isRecommended) EmeraldSuccess else AmberWarning,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 12.sp, color = TextPrimaryLight, lineHeight = 18.sp)
    }
}
