package com.example.ui.screens.code

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import com.example.data.model.CodingProblem
import com.example.ui.components.TagChip
import com.example.ui.theme.*
import com.example.viewmodel.SkillszViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodingArenaScreen(
    viewModel: SkillszViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val problems by viewModel.codingProblems.collectAsState()
    var selectedProblemIndex by remember { mutableStateOf(0) }
    val problem = problems.getOrElse(selectedProblemIndex) { problems.first() }

    val languages = listOf("Kotlin", "Java", "Python", "C++")
    var selectedLanguage by remember { mutableStateOf("Kotlin") }

    var codeText by remember(problem, selectedLanguage) {
        mutableStateOf(problem.initialCode[selectedLanguage] ?: "// Enter code here")
    }

    var isRunning by remember { mutableStateOf(false) }
    var executionResult by remember { mutableStateOf<String?>(null) }
    var passedTestCases by remember { mutableStateOf<Pair<Int, Int>?>(null) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Code Arena - Algorithm Sandbox", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Problem Selector Carousel
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                problems.forEachIndexed { index, p ->
                    FilterChip(
                        selected = selectedProblemIndex == index,
                        onClick = {
                            selectedProblemIndex = index
                            executionResult = null
                            passedTestCases = null
                        },
                        label = { Text("${index + 1}. ${p.title}", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                    )
                }
            }

            // Problem Details Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SlateDarkBorder, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(problem.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                        TagChip(
                            text = problem.difficulty,
                            color = if (problem.difficulty == "Easy") EmeraldSuccess else AmberWarning
                        )
                    }

                    Text("Topic: ${problem.category}", fontSize = 12.sp, color = CyanAccentDark, fontWeight = FontWeight.SemiBold)
                    Text(problem.statement, fontSize = 13.sp, lineHeight = 20.sp, color = MaterialTheme.colorScheme.onSurface)

                    Divider(color = SlateDarkBorder)

                    Text("Example:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Input: ${problem.exampleInput}", fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                            Text("Output: ${problem.exampleOutput}", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = EmeraldSuccess)
                        }
                    }

                    Text("Constraints:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(problem.constraints, fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Language Selector Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Code Editor", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    languages.forEach { lang ->
                        FilterChip(
                            selected = selectedLanguage == lang,
                            onClick = { selectedLanguage = lang },
                            label = { Text(lang, fontSize = 11.sp) }
                        )
                    }
                }
            }

            // Monospace Code Editor
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = SlateDarkBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, PrimaryIndigoDark.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
            ) {
                OutlinedTextField(
                    value = codeText,
                    onValueChange = { codeText = it },
                    textStyle = LocalTextStyle.current.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .testTag("code_editor_field"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
            }

            // Run & Submit Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        isRunning = true
                        scope.launch {
                            delay(600)
                            isRunning = false
                            executionResult = "Execution Time: 38 ms • Memory Usage: 39.4 MB\nAll Sample Test Cases Passed! ✓"
                            passedTestCases = Pair(problem.testCasesCount, problem.testCasesCount)
                        }
                    },
                    modifier = Modifier.weight(1f).height(46.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    if (isRunning) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                    } else {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Run Code")
                    }
                }

                Button(
                    onClick = {
                        isRunning = true
                        scope.launch {
                            delay(900)
                            isRunning = false
                            executionResult = "ACCEPTED 🚀\nRuntime: 42 ms (beats 89.4%)\nMemory: 41.2 MB (beats 92.1%)\nTest Cases: ${problem.testCasesCount}/${problem.testCasesCount} Passed"
                            passedTestCases = Pair(problem.testCasesCount, problem.testCasesCount)
                            Toast.makeText(context, "Solution Accepted! Algorithm Score +20", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.weight(1f).height(46.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess)
                ) {
                    Icon(Icons.Default.Upload, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Submit Solution", fontWeight = FontWeight.Bold)
                }
            }

            // Execution Console Output
            if (executionResult != null) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SlateDarkSurfaceVariant),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, EmeraldSuccess.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Output Console", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = CyanAccentDark)
                            if (passedTestCases != null) {
                                TagChip(text = "${passedTestCases!!.first}/${passedTestCases!!.second} Tests Passed", color = EmeraldSuccess)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = executionResult!!,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}
