package com.example.ui.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CurrentUser
import com.example.ui.components.MetricStatCard
import com.example.ui.components.QuickActionButton
import com.example.ui.components.SectionHeader
import com.example.ui.components.TagChip
import com.example.ui.theme.*
import com.example.viewmodel.SkillszViewModel

@Composable
fun StudentDashboardScreen(
    viewModel: SkillszViewModel,
    onNavigateToAi: () -> Unit,
    onNavigateToResume: () -> Unit,
    onNavigateToJobs: () -> Unit,
    onNavigateToInternships: () -> Unit,
    onNavigateToNotes: () -> Unit,
    onNavigateToTests: () -> Unit,
    onNavigateToCodeArena: () -> Unit,
    onNavigateToMockInterview: () -> Unit,
    onNavigateToRoadmap: () -> Unit,
    onNavigateToProjects: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val applications by viewModel.applications.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val resumes by viewModel.resumes.collectAsState()
    val activeResume by viewModel.activeResume.collectAsState()

    var showGoalDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Header & Profile Completion Ring
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SlateDarkBorder, RoundedCornerShape(20.dp))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Welcome, ${currentUser?.name ?: "Student"} 👋",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${currentUser?.rollNumber ?: "CSE22041"} • ${currentUser?.branch ?: "Computer Science"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Profile Completion Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(PrimaryIndigo.copy(alpha = 0.15f))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Profile Completion",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${currentUser?.profileCompletion ?: 78}%",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = CyanAccentDark
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { (currentUser?.profileCompletion ?: 78) / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = CyanAccentDark,
                    trackColor = SlateDarkSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Target Career Goal Pill
                Surface(
                    onClick = { showGoalDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.TrackChanges,
                                contentDescription = null,
                                tint = PrimaryIndigoDark,
                                modifier = Modifier.size(20.dp)
                            )
                            Column {
                                Text(
                                    text = "Target Career Goal",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = currentUser?.careerGoal ?: "Full Stack Developer",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Change Goal",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Hero Banner Card with Generated Illustration
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SlateDarkSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SlateDarkBorder, RoundedCornerShape(20.dp))
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.skillsz_hero_banner),
                        contentDescription = "Skillsz Hero Art",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, SlateDarkSurface.copy(alpha = 0.95f))
                                )
                            )
                    )
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🚀 Accelerate Your Engineering Career",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Practice algorithmic challenges, generate ATS-friendly resumes, access verified department notes, and crack campus placement drives.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onNavigateToAi,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                            modifier = Modifier.height(38.dp)
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Ask SKILLSZ AI", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onNavigateToRoadmap,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.height(38.dp)
                        ) {
                            Text("View Roadmap", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Quick Actions Section
        SectionHeader(title = "Quick Actions")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionButton(
                label = "AI Chat",
                icon = Icons.Default.SmartToy,
                color = CyanAccentDark,
                onClick = onNavigateToAi,
                modifier = Modifier.weight(1f)
            )
            QuickActionButton(
                label = "Build Resume",
                icon = Icons.Default.Description,
                color = PrimaryIndigoDark,
                onClick = onNavigateToResume,
                modifier = Modifier.weight(1f)
            )
            QuickActionButton(
                label = "Find Jobs",
                icon = Icons.Default.Work,
                color = EmeraldSuccess,
                onClick = onNavigateToJobs,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionButton(
                label = "Internships",
                icon = Icons.Default.School,
                color = AmberWarning,
                onClick = onNavigateToInternships,
                modifier = Modifier.weight(1f)
            )
            QuickActionButton(
                label = "Notes",
                icon = Icons.Default.MenuBook,
                color = VioletPurple,
                onClick = onNavigateToNotes,
                modifier = Modifier.weight(1f)
            )
            QuickActionButton(
                label = "Tests",
                icon = Icons.Default.Assignment,
                color = RoseError,
                onClick = onNavigateToTests,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionButton(
                label = "Code Arena",
                icon = Icons.Default.Code,
                color = CyanAccentDark,
                onClick = onNavigateToCodeArena,
                modifier = Modifier.weight(1f)
            )
            QuickActionButton(
                label = "Mock Interview",
                icon = Icons.Default.RecordVoiceOver,
                color = PrimaryIndigoDark,
                onClick = onNavigateToMockInterview,
                modifier = Modifier.weight(1f)
            )
            QuickActionButton(
                label = "Projects",
                icon = Icons.Default.Lightbulb,
                color = AmberWarning,
                onClick = onNavigateToProjects,
                modifier = Modifier.weight(1f)
            )
        }

        // Dashboard Metric Cards Grid
        SectionHeader(
            title = "Career & Academic Overview",
            subtitle = "Live metrics from your institute profile"
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricStatCard(
                title = "Resume",
                value = "${activeResume.atsScore}%",
                subtitle = "ATS Ready",
                icon = Icons.Default.Description,
                accentColor = PrimaryIndigoDark,
                onClick = onNavigateToResume,
                modifier = Modifier.weight(1f)
            )
            MetricStatCard(
                title = "Skills",
                value = "${currentUser?.skills?.size ?: 12}",
                subtitle = "Skills Verified",
                icon = Icons.Default.Psychology,
                accentColor = CyanAccentDark,
                onClick = onNavigateToResume,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricStatCard(
                title = "Applications",
                value = "${applications.size}",
                subtitle = "Active in pipeline",
                icon = Icons.Default.WorkHistory,
                accentColor = EmeraldSuccess,
                onClick = onNavigateToJobs,
                modifier = Modifier.weight(1f)
            )
            MetricStatCard(
                title = "Internships",
                value = "5",
                subtitle = "Recommended",
                icon = Icons.Default.Stars,
                accentColor = AmberWarning,
                onClick = onNavigateToInternships,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricStatCard(
                title = "Upcoming Tests",
                value = "2",
                subtitle = "Scheduled by Dept",
                icon = Icons.Default.Quiz,
                accentColor = RoseError,
                onClick = onNavigateToTests,
                modifier = Modifier.weight(1f)
            )
            MetricStatCard(
                title = "Learning Progress",
                value = "68%",
                subtitle = "Semester Syllabus",
                icon = Icons.Default.AutoStories,
                accentColor = VioletPurple,
                onClick = onNavigateToNotes,
                modifier = Modifier.weight(1f)
            )
        }

        // Recommended for You
        SectionHeader(
            title = "Recommended for You",
            subtitle = "Tailored for ${currentUser?.careerGoal ?: "Full Stack"}"
        )

        // Item 1: React Internship
        RecommendationCard(
            title = "React & Node.js Developer Intern",
            category = "INTERNSHIP",
            organization = "Razorpay • Bangalore / Remote",
            tag = "₹50,000 / mo",
            icon = Icons.Default.Work,
            accentColor = EmeraldSuccess,
            onClick = onNavigateToInternships
        )

        // Item 2: Python / ML Course Notes
        RecommendationCard(
            title = "Machine Learning & Neural Networks",
            category = "ACADEMIC NOTES",
            organization = "Unit 1 & 2 • Dr. Priya Raman",
            tag = "Sem 5 Syllabus",
            icon = Icons.Default.MenuBook,
            accentColor = VioletPurple,
            onClick = onNavigateToNotes
        )

        // Item 3: Core Assessment
        RecommendationCard(
            title = "CSE Core Engineering Diagnostic",
            category = "ASSESSMENT",
            organization = "Data Structures, OS & Networks",
            tag = "15 Mins • 6 Qs",
            icon = Icons.Default.Assignment,
            accentColor = RoseError,
            onClick = onNavigateToTests
        )

        // Item 4: Build a Portfolio Project
        RecommendationCard(
            title = "Build a Distributed Rate Limiter Project",
            category = "PROJECT RECOMMENDATION",
            organization = "Redis, Docker, Microservices",
            tag = "High ATS Value",
            icon = Icons.Default.Lightbulb,
            accentColor = AmberWarning,
            onClick = onNavigateToProjects
        )

        Spacer(modifier = Modifier.height(16.dp))
    }

    if (showGoalDialog) {
        val goals = listOf(
            "Full Stack Developer",
            "AI / ML Engineer",
            "Android Developer",
            "Cloud & DevOps Engineer",
            "Data Scientist",
            "Cyber Security Engineer"
        )
        AlertDialog(
            onDismissRequest = { showGoalDialog = false },
            title = { Text("Select Target Career Goal") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    goals.forEach { goal ->
                        Surface(
                            onClick = {
                                viewModel.updateCareerGoal(goal)
                                showGoalDialog = false
                            },
                            shape = RoundedCornerShape(10.dp),
                            color = if (currentUser?.careerGoal == goal) PrimaryIndigo.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 14.dp, vertical = 12.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = goal,
                                    fontWeight = if (currentUser?.careerGoal == goal) FontWeight.Bold else FontWeight.Normal,
                                    color = if (currentUser?.careerGoal == goal) CyanAccentDark else MaterialTheme.colorScheme.onSurface
                                )
                                if (currentUser?.careerGoal == goal) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = CyanAccentDark)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showGoalDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun RecommendationCard(
    title: String,
    category: String,
    organization: String,
    tag: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SlateDarkBorder, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = category,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                    Text(
                        text = tag,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = organization,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
