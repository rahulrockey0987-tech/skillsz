package com.example.ui.screens.tests

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AssessmentQuestion
import com.example.data.model.AssessmentTest
import com.example.data.model.TestCategory
import com.example.data.model.TestResultData
import com.example.ui.components.TagChip
import com.example.ui.theme.*
import com.example.viewmodel.SkillszViewModel
import kotlinx.coroutines.delay

@Composable
fun TestsAndAssessmentsScreen(
    viewModel: SkillszViewModel,
    modifier: Modifier = Modifier
) {
    val assessmentTests by viewModel.assessmentTests.collectAsState()
    val testResults by viewModel.testResults.collectAsState()

    var activeTestTaking by remember { mutableStateOf<AssessmentTest?>(null) }
    var activeTestResult by remember { mutableStateOf<TestResultData?>(null) }

    when {
        activeTestTaking != null -> {
            TestTakingView(
                test = activeTestTaking!!,
                onCancel = { activeTestTaking = null },
                onSubmitTest = { result ->
                    viewModel.saveTestResult(result)
                    activeTestTaking = null
                    activeTestResult = result
                }
            )
        }
        activeTestResult != null -> {
            TestResultReportView(
                result = activeTestResult!!,
                onClose = { activeTestResult = null }
            )
        }
        else -> {
            // Main Tests Catalog & History View
            TestsCatalogView(
                tests = assessmentTests,
                history = testResults,
                onStartTest = { activeTestTaking = it },
                onViewResult = { activeTestResult = it },
                modifier = modifier
            )
        }
    }
}

@Composable
fun TestsCatalogView(
    tests: List<AssessmentTest>,
    history: List<TestResultData>,
    onStartTest: (AssessmentTest) -> Unit,
    onViewResult: (TestResultData) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<TestCategory?>(null) }
    val categories = TestCategory.values()

    val filteredTests = if (selectedCategory == null) tests else tests.filter { it.category == selectedCategory }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Assessments & Diagnostic Tests",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Benchmark your technical skills with timed campus placement simulations",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Categories Chip Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { selectedCategory = null },
                label = { Text("All Categories", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            categories.forEach { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat.label, fontWeight = FontWeight.SemiBold, fontSize = 12.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            // Available Tests Section
            item {
                Text(
                    text = "Scheduled Tests (${filteredTests.size})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            items(filteredTests, key = { it.id }) { test ->
                TestCard(test = test, onStart = { onStartTest(test) })
            }

            // Completed Tests Section
            if (history.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Completed Assessment History",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                items(history, key = { it.id }) { result ->
                    Card(
                        onClick = { onViewResult(result) },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SlateDarkBorder, RoundedCornerShape(14.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = result.testTitle,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Completed: ${result.completedDate} • Time: ${result.timeTakenSeconds / 60}m",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = EmeraldSuccess.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "${result.percentage}%",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = EmeraldSuccess,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TestCard(test: AssessmentTest, onStart: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SlateDarkBorder, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TagChip(text = test.category.label, color = PrimaryIndigoDark)
                Text(
                    text = "Passing: ${test.passingScore}%",
                    fontSize = 11.sp,
                    color = CyanAccentDark,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = test.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.Timer, contentDescription = null, tint = AmberWarning, modifier = Modifier.size(16.dp))
                    Text("${test.durationMinutes} mins", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.HelpOutline, contentDescription = null, tint = CyanAccentDark, modifier = Modifier.size(16.dp))
                    Text("${test.totalQuestions} questions", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.BarChart, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                    Text(test.difficulty, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onStart,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("start_test_button")
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Start Test Simulation", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun TestTakingView(
    test: AssessmentTest,
    onCancel: () -> Unit,
    onSubmitTest: (TestResultData) -> Unit
) {
    var currentIndex by remember { mutableStateOf(0) }
    val userAnswers = remember { mutableStateMapOf<Int, Int>() }
    var secondsLeft by remember { mutableStateOf(test.durationMinutes * 60) }

    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000L)
            secondsLeft--
        }
    }

    val currentQ = test.questions[currentIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        // Top Timer & Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = test.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                Text(
                    text = "Question ${currentIndex + 1} of ${test.questions.size}",
                    fontSize = 12.sp,
                    color = CyanAccentDark,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Countdown Pill
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (secondsLeft < 180) RoseError.copy(alpha = 0.2f) else SlateDarkSurfaceVariant
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.Timer,
                        contentDescription = null,
                        tint = if (secondsLeft < 180) RoseError else AmberWarning,
                        modifier = Modifier.size(16.dp)
                    )
                    val mins = secondsLeft / 60
                    val secs = secondsLeft % 60
                    Text(
                        text = String.format("%02d:%02d", mins, secs),
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        color = if (secondsLeft < 180) RoseError else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LinearProgressIndicator(
            progress = { (currentIndex + 1) / test.questions.size.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = PrimaryIndigoDark,
            trackColor = SlateDarkSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Question Box
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(1.dp, SlateDarkBorder, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TagChip(text = currentQ.topic, color = PrimaryIndigoDark)
                    Text("1 Mark", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Text(
                    text = currentQ.questionText,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 22.sp
                )

                if (currentQ.codeSnippet != null) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SlateDarkBackground,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = currentQ.codeSnippet,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = CyanAccentDark,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                Divider(color = SlateDarkBorder)

                // Options
                currentQ.options.forEachIndexed { optIndex, optionText ->
                    val isSelected = userAnswers[currentIndex] == optIndex
                    Surface(
                        onClick = { userAnswers[currentIndex] = optIndex },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) PrimaryIndigo.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                if (isSelected) PrimaryIndigoDark else Color.Transparent,
                                RoundedCornerShape(12.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) PrimaryIndigo else SlateDarkBorder),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = ('A'.code + optIndex).toChar().toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                            }
                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Navigation Footer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { if (currentIndex > 0) currentIndex-- },
                enabled = currentIndex > 0,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Previous")
            }

            if (currentIndex + 1 < test.questions.size) {
                Button(
                    onClick = { currentIndex++ },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo)
                ) {
                    Text("Next Question")
                }
            } else {
                Button(
                    onClick = {
                        // Compute score
                        var correct = 0
                        test.questions.forEachIndexed { idx, q ->
                            if (userAnswers[idx] == q.correctOptionIndex) {
                                correct++
                            }
                        }
                        val pct = ((correct / test.questions.size.toFloat()) * 100).toInt()
                        val result = TestResultData(
                            id = "tr_${System.currentTimeMillis()}",
                            testId = test.id,
                            testTitle = test.title,
                            score = correct * 10,
                            totalScore = test.questions.size * 10,
                            percentage = pct,
                            correctCount = correct,
                            wrongCount = test.questions.size - correct,
                            timeTakenSeconds = (test.durationMinutes * 60) - secondsLeft,
                            completedDate = "Today",
                            weakAreas = if (pct < 80) listOf("Red-Black Trees", "Concurrency Control") else emptyList(),
                            recommendedTopics = listOf("Review Unit 2 Notes", "Solve 3 Tree Problems in Code Arena")
                        )
                        onSubmitTest(result)
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess)
                ) {
                    Text("Submit Test", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun TestResultReportView(result: TestResultData, onClose: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(if (result.percentage >= 60) EmeraldSuccess.copy(alpha = 0.15f) else RoseError.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (result.percentage >= 60) Icons.Default.CheckCircle else Icons.Default.Cancel,
                        contentDescription = null,
                        tint = if (result.percentage >= 60) EmeraldSuccess else RoseError,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = result.testTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = if (result.percentage >= 60) "Passed - Assessment Benchmark Met! 🎯" else "Needs Improvement - Below Passing Threshold",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (result.percentage >= 60) EmeraldSuccess else RoseError
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Percentage Banner
                Text(
                    text = "${result.percentage}%",
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Black,
                    color = if (result.percentage >= 60) EmeraldSuccess else RoseError
                )

                Text(
                    text = "Score: ${result.score} / ${result.totalScore}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Stats breakdown
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ResultStatPill("Correct", "${result.correctCount}", EmeraldSuccess, Modifier.weight(1f))
                    ResultStatPill("Wrong", "${result.wrongCount}", RoseError, Modifier.weight(1f))
                    ResultStatPill("Time Taken", "${result.timeTakenSeconds / 60}m ${result.timeTakenSeconds % 60}s", CyanAccentDark, Modifier.weight(1f))
                }
            }
        }

        // Weak Areas and Recommended Topics
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SlateDarkBorder, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "🎯 Topic Performance & Weak Areas",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = CyanAccentDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (result.weakAreas.isNotEmpty()) {
                    result.weakAreas.forEach { area ->
                        Text("• Weak Area Identified: $area", fontSize = 12.sp, color = AmberWarning)
                    }
                } else {
                    Text("• Strong across all tested topics!", fontSize = 12.sp, color = EmeraldSuccess)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Recommended Study Action:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                result.recommendedTopics.forEach { topic ->
                    Text("→ $topic", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Button(
            onClick = onClose,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("Back to Assessments Catalog", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ResultStatPill(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Black, color = color)
        }
    }
}
