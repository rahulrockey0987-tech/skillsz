package com.example.ui.screens.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.StaticDataProvider
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*
import com.example.viewmodel.SkillszViewModel

data class InterviewFeedback(
    val answerQuality: Int = 88,
    val technicalAccuracy: Int = 92,
    val communication: Int = 85,
    val structure: Int = 90,
    val missingPoints: String = "Could have mentioned edge cases with empty collections and lock contention under multi-threading.",
    val positiveHighlights: String = "Excellent clarity explaining hash collision bucket trees (red-black trees) in Java 8+."
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockInterviewScreen(
    viewModel: SkillszViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("Technical") }
    val categories = listOf("Technical", "Coding", "Behavioral", "HR")

    val questions = remember(selectedCategory) {
        StaticDataProvider.getMockInterviewQuestions(selectedCategory)
    }

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var answerText by remember { mutableStateOf("") }
    var isSubmittedAnswer by remember { mutableStateOf(false) }
    var currentFeedback by remember { mutableStateOf<InterviewFeedback?>(null) }
    var isInterviewCompleted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Mock Interview", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Category Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = {
                            selectedCategory = cat
                            currentQuestionIndex = 0
                            answerText = ""
                            isSubmittedAnswer = false
                            currentFeedback = null
                            isInterviewCompleted = false
                        },
                        label = { Text(cat, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (!isInterviewCompleted) {
                // Progress Bar & Question Counter
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Question ${currentQuestionIndex + 1} of ${questions.size}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = CyanAccentDark
                        )
                        Text(
                            text = "$selectedCategory Round",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    LinearProgressIndicator(
                        progress = { (currentQuestionIndex + 1) / questions.size.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = PrimaryIndigoDark,
                        trackColor = SlateDarkSurfaceVariant
                    )
                }

                // Question Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SlateDarkBorder, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryIndigo.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Psychology,
                                    contentDescription = null,
                                    tint = PrimaryIndigoDark,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                text = "Interviewer Question",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = questions[currentQuestionIndex],
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 24.sp
                        )
                    }
                }

                // Student Answer Input
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SlateDarkBorder, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Your Answer",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Type your comprehensive answer structure (STAR method or technical breakdown)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = answerText,
                            onValueChange = { answerText = it },
                            placeholder = {
                                Text(
                                    "Type your response here... (e.g. In Java 8+, HashMap uses an array of Node objects. When a bucket length exceeds 8 and total capacity >= 64, it converts the linked list into a Red-Black tree to maintain O(log N) lookup instead of O(N)...)"
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .testTag("interview_answer_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    // Quick insert starter template
                                    answerText = "In technical terms, this is handled through structured design patterns. For instance, HashMap resolves collisions via chaining; upon reaching the threshold (TREEIFY_THRESHOLD = 8), it transforms into a balanced tree. This ensures worst-case performance remains O(log N)."
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Load Sample Answer", fontSize = 11.sp)
                            }

                            Button(
                                onClick = {
                                    if (answerText.isNotBlank()) {
                                        isSubmittedAnswer = true
                                        currentFeedback = InterviewFeedback(
                                            answerQuality = 89,
                                            technicalAccuracy = 94,
                                            communication = 86,
                                            structure = 91,
                                            missingPoints = "You covered the main concept thoroughly! To achieve a 100% score, mention how memory overhead of TreeNode is balanced by UNTREEIFY_THRESHOLD (6).",
                                            positiveHighlights = "Spot on mentioning the threshold and O(log N) worst-case time guarantee."
                                        )
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Evaluate Answer", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // AI Feedback Card
                if (isSubmittedAnswer && currentFeedback != null) {
                    val fb = currentFeedback!!
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SlateDarkSurfaceVariant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, CyanAccentDark.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🤖 AI Evaluator Feedback",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = CyanAccentDark
                                )
                                Text(
                                    text = "Score: ${fb.answerQuality}/100",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = EmeraldSuccess
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ScorePill("Tech Accuracy", "${fb.technicalAccuracy}%", CyanAccentDark, Modifier.weight(1f))
                                ScorePill("Communication", "${fb.communication}%", VioletPurple, Modifier.weight(1f))
                                ScorePill("Structure", "${fb.structure}%", AmberWarning, Modifier.weight(1f))
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "✨ Strong Points:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldSuccess
                            )
                            Text(
                                text = fb.positiveHighlights,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                            )

                            Text(
                                text = "⚠️ Missing Points & Improvements:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberWarning
                            )
                            Text(
                                text = fb.missingPoints,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(top = 2.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    if (currentQuestionIndex + 1 < questions.size) {
                                        currentQuestionIndex++
                                        answerText = ""
                                        isSubmittedAnswer = false
                                        currentFeedback = null
                                    } else {
                                        isInterviewCompleted = true
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = CyanAccentDark),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = if (currentQuestionIndex + 1 < questions.size) "Next Question →" else "Generate Final Interview Report 🏆",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            } else {
                // Final Interview Report
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SlateDarkBorder, RoundedCornerShape(20.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(EmeraldSuccess.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(32.dp))
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Interview Completed!",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Overall Candidate Performance: Strong Hire ⭐",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldSuccess
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Stats
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ScorePill("Overall Score", "91/100", CyanAccentDark, Modifier.weight(1f))
                            ScorePill("Questions", "${questions.size}/${questions.size}", VioletPurple, Modifier.weight(1f))
                            ScorePill("Readiness", "Job Ready", EmeraldSuccess, Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "The candidate demonstrates solid grasp of core systems and data structures. Good structured communication; continue refining edge case testing and multi-threaded synchronization nuances.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                currentQuestionIndex = 0
                                answerText = ""
                                isSubmittedAnswer = false
                                currentFeedback = null
                                isInterviewCompleted = false
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Restart Another Round", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScorePill(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Black, color = color)
        }
    }
}
