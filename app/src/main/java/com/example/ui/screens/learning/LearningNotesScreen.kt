package com.example.ui.screens.learning

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import com.example.data.model.AcademicNote
import com.example.data.model.NoteType
import com.example.ui.components.TagChip
import com.example.ui.theme.*
import com.example.viewmodel.SkillszViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningNotesScreen(
    viewModel: SkillszViewModel,
    onAskAIAboutTopic: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val notes by viewModel.notes.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val context = LocalContext.current

    var selectedSubject by remember { mutableStateOf("All") }
    var selectedUnit by remember { mutableStateOf("All") }
    var selectedNoteForReading by remember { mutableStateOf<AcademicNote?>(null) }

    val subjects = listOf("All", "Artificial Intelligence", "Machine Learning", "Data Structures", "DBMS", "Computer Networks", "Operating Systems")
    val units = listOf("All", "Unit 1", "Unit 2", "Unit 3", "Unit 4", "Unit 5")

    val filteredNotes = notes.filter {
        (selectedSubject == "All" || it.subject.equals(selectedSubject, ignoreCase = true)) &&
        (selectedUnit == "All" || it.unit.equals(selectedUnit, ignoreCase = true))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Institute & Curriculum Breadcrumb Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = PrimaryIndigoDark,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Curriculum: ${currentUser?.course ?: "B.Tech"} ${currentUser?.branch ?: "CSE"}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = "${currentUser?.year ?: "3rd Year"} • ${currentUser?.semester ?: "5th Semester"} • Section ${currentUser?.section ?: "A"}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            // Subject Filter Bar
            Text(
                text = "Select Subject",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                subjects.forEach { subj ->
                    FilterChip(
                        selected = selectedSubject == subj,
                        onClick = { selectedSubject = subj },
                        label = { Text(subj, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                    )
                }
            }

            // Unit Filter Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                units.forEach { u ->
                    FilterChip(
                        selected = selectedUnit == u,
                        onClick = { selectedUnit = u },
                        label = { Text(u, fontSize = 11.sp) }
                    )
                }
            }

            // Count
            Text(
                text = "${filteredNotes.size} Study Modules Available",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // Notes List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(filteredNotes, key = { it.id }) { note ->
                    AcademicNoteCard(
                        note = note,
                        onRead = { selectedNoteForReading = note },
                        onBookmark = {
                            viewModel.toggleBookmark(note.id, note.isBookmarked)
                            Toast.makeText(context, if (note.isBookmarked) "Bookmark removed" else "Bookmarked to revision list!", Toast.LENGTH_SHORT).show()
                        },
                        onDownload = {
                            Toast.makeText(context, "Downloading ${note.title} (${note.type.label})...", Toast.LENGTH_SHORT).show()
                        },
                        onAskAI = {
                            onAskAIAboutTopic("Explain the key concepts and exam formulas for: ${note.subject} - ${note.title}")
                        }
                    )
                }
            }
        }
    }

    // Note Reading Dialog / BottomSheet
    if (selectedNoteForReading != null) {
        val note = selectedNoteForReading!!
        AlertDialog(
            onDismissRequest = { selectedNoteForReading = null },
            title = {
                Column {
                    Text(note.title, fontWeight = FontWeight.Black, fontSize = 18.sp)
                    Text("${note.subject} • ${note.unit}", fontSize = 12.sp, color = CyanAccentDark)
                }
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Format: ${note.type.label}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryIndigoDark)
                            Text("Downloads: ${note.downloads} students accessed", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Text("Syllabus Summary & Key Takeaways:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(note.summary, fontSize = 13.sp, lineHeight = 20.sp, color = MaterialTheme.colorScheme.onSurface)

                    Text("Exam Quick Revision Checklist:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(
                        "1. Definitions and mathematical formulations.\n2. Comparison table of algorithmic complexities.\n3. Common university semester exam question patterns.\n4. Real-world industry applications.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAskAIAboutTopic("Explain this topic in detail: ${note.title}")
                        selectedNoteForReading = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ask AI to Explain")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedNoteForReading = null }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun AcademicNoteCard(
    note: AcademicNote,
    onRead: () -> Unit,
    onBookmark: () -> Unit,
    onDownload: () -> Unit,
    onAskAI: () -> Unit
) {
    val typeColor = when (note.type) {
        NoteType.PDF -> RoseError
        NoteType.VIDEO -> PrimaryIndigoDark
        NoteType.CODE -> CyanAccentDark
        NoteType.DOC -> VioletPurple
        NoteType.LINK -> AmberWarning
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SlateDarkBorder, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Subject & Bookmark Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TagChip(text = note.subject, color = PrimaryIndigoDark)
                    TagChip(text = note.unit, color = CyanAccentDark)
                }

                IconButton(
                    onClick = onBookmark,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (note.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (note.isBookmarked) AmberWarning else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = note.summary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(typeColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = note.type.name.take(3),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black,
                            color = typeColor
                        )
                    }
                    Text(
                        text = note.type.label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = typeColor
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IconButton(onClick = onAskAI, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "Ask AI", tint = CyanAccentDark, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onDownload, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Default.Download, contentDescription = "Download", tint = PrimaryIndigoDark, modifier = Modifier.size(18.dp))
                    }
                    Button(
                        onClick = onRead,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Read Notes", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
