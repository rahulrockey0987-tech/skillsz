package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import com.example.data.model.CurrentUser
import com.example.data.model.NoteType
import com.example.data.model.UserRole
import com.example.ui.components.MetricStatCard
import com.example.ui.components.SectionHeader
import com.example.ui.components.TagChip
import com.example.ui.theme.*
import com.example.viewmodel.SkillszViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleAdminPanelScreen(
    viewModel: SkillszViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val context = LocalContext.current

    val role = currentUser?.role ?: UserRole.INSTITUTE_ADMIN

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (role) {
                            UserRole.INSTITUTE_ADMIN -> "Institute Admin Console"
                            UserRole.FACULTY -> "Faculty Academic Portal"
                            UserRole.SUPER_ADMIN -> "SKILLSZ Super Admin HQ"
                            else -> "Admin Management"
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                },
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
        when (role) {
            UserRole.INSTITUTE_ADMIN -> InstituteAdminView(viewModel, modifier = Modifier.padding(padding))
            UserRole.FACULTY -> FacultyView(viewModel, modifier = Modifier.padding(padding))
            UserRole.SUPER_ADMIN -> SuperAdminView(viewModel, modifier = Modifier.padding(padding))
            UserRole.STUDENT -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Role Switch Required", fontWeight = FontWeight.Bold)
                    Text("Use the role switcher at top to switch to Institute Admin or Faculty demo.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = { viewModel.quickLoginAs(UserRole.INSTITUTE_ADMIN) }) {
                        Text("Switch to Institute Admin")
                    }
                }
            }
        }
    }
}

@Composable
fun InstituteAdminView(viewModel: SkillszViewModel, modifier: Modifier = Modifier) {
    val students by viewModel.instituteStudents.collectAsState()
    val context = LocalContext.current

    var showAddStudentDialog by remember { mutableStateOf(false) }
    var showPostJobDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Isolation Status Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SlateDarkSurfaceVariant),
            modifier = Modifier.fillMaxWidth().border(1.dp, CyanAccentDark.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(Icons.Default.Security, contentDescription = null, tint = CyanAccentDark)
                Column {
                    Text("Institute Isolation: ACTIVE", fontWeight = FontWeight.Black, fontSize = 13.sp, color = CyanAccentDark)
                    Text("Data strictly scoped to Indian Institute of Technology Delhi (iitd)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        // Metrics Grid
        SectionHeader(title = "Institute Placement & Academic KPI")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricStatCard("Total Students", "4,200", "Registered by Admin", Icons.Default.Group, PrimaryIndigoDark, {}, Modifier.weight(1f))
            MetricStatCard("Active Today", "3,140", "94% Attendance", Icons.Default.Bolt, CyanAccentDark, {}, Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricStatCard("Avg Test Score", "76.4%", "Diagnostic Benchmark", Icons.Default.BarChart, EmeraldSuccess, {}, Modifier.weight(1f))
            MetricStatCard("Resume Done", "89%", "ATS Verified", Icons.Default.Description, VioletPurple, {}, Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricStatCard("Job Openings", "14", "Campus Placement Drives", Icons.Default.Work, AmberWarning, {}, Modifier.weight(1f))
            MetricStatCard("Placement Rate", "64%", "Target: 100% by 2026", Icons.Default.Stars, EmeraldSuccess, {}, Modifier.weight(1f))
        }

        // Quick Admin Actions
        SectionHeader(title = "Administrative Actions")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { showAddStudentDialog = true },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                modifier = Modifier.weight(1f).testTag("admin_add_student_btn")
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Add Student", fontSize = 12.sp)
            }

            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "Mock CSV Batch: 150 student accounts created with temporary credentials!", Toast.LENGTH_LONG).show()
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.FileUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Import CSV", fontSize = 12.sp)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { showPostJobDialog = true },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.PostAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Post Drive Job", fontSize = 12.sp)
            }

            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "Temporary password reset tokens dispatched to 12 students via institute email.", Toast.LENGTH_SHORT).show()
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.LockReset, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Batch Reset Pass", fontSize = 12.sp)
            }
        }

        // Student Roster Sample
        SectionHeader(
            title = "Enrolled Students Roster",
            subtitle = "Managed by Institute Placement & Registrar Cell"
        )

        val sampleStudents = listOf(
            Triple("CSE22041", "Aryan Sharma", "B.Tech CSE • Sem 5 • Sec A"),
            Triple("CSE22042", "Rohan Mehta", "B.Tech CSE • Sem 5 • Sec A"),
            Triple("CSE22043", "Ananya Verma", "B.Tech CSE • Sem 5 • Sec B"),
            Triple("ECE22019", "Sneha Iyer", "B.Tech ECE • Sem 5 • Sec B")
        )

        sampleStudents.forEach { (roll, name, dept) ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth().border(1.dp, SlateDarkBorder, RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("$roll • $dept", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    TagChip(text = "Active", color = EmeraldSuccess)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    if (showAddStudentDialog) {
        var roll by remember { mutableStateOf("") }
        var name by remember { mutableStateOf("") }
        var branch by remember { mutableStateOf("Computer Science & Engineering") }
        var sec by remember { mutableStateOf("A") }

        AlertDialog(
            onDismissRequest = { showAddStudentDialog = false },
            title = { Text("Provision Student Account") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = roll, onValueChange = { roll = it }, label = { Text("Roll Number / Student ID") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = branch, onValueChange = { branch = it }, label = { Text("Branch / Department") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = sec, onValueChange = { sec = it }, label = { Text("Section") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (roll.isNotBlank() && name.isNotBlank()) {
                            viewModel.adminCreateStudent(roll, name, branch, "3rd Year", sec)
                            showAddStudentDialog = false
                            Toast.makeText(context, "Student account $roll created successfully!", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Provision Account")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddStudentDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showPostJobDialog) {
        var title by remember { mutableStateOf("") }
        var company by remember { mutableStateOf("") }
        var stipend by remember { mutableStateOf("₹30 LPA") }
        var skills by remember { mutableStateOf("Java, Kotlin, DSA") }

        AlertDialog(
            onDismissRequest = { showPostJobDialog = false },
            title = { Text("Publish Campus Placement Drive") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Job Title") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = company, onValueChange = { company = it }, label = { Text("Recruiter Company") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = stipend, onValueChange = { stipend = it }, label = { Text("Package / Stipend") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = skills, onValueChange = { skills = it }, label = { Text("Required Skills (csv)") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank() && company.isNotBlank()) {
                            viewModel.adminPublishJob(title, company, stipend, false, skills)
                            showPostJobDialog = false
                            Toast.makeText(context, "Drive published to students!", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Publish to Students")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPostJobDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun FacultyView(viewModel: SkillszViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var showUploadNoteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth().border(1.dp, SlateDarkBorder, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Dr. Priya Raman", fontWeight = FontWeight.Black, fontSize = 16.sp)
                Text("Professor • Dept of Computer Science & Engineering", fontSize = 12.sp, color = CyanAccentDark)
                Text("Assigned Classes: B.Tech CSE 3rd Year (Sem 5) Sec A & B", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        SectionHeader(title = "Faculty Quick Tools")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { showUploadNoteDialog = true },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Upload Notes", fontSize = 12.sp)
            }

            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "Quiz created and assigned to CSE 3rd Year!", Toast.LENGTH_SHORT).show()
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Publish Quiz", fontSize = 12.sp)
            }
        }

        SectionHeader(title = "Assigned Subject Modules")

        listOf(
            "Artificial Intelligence" to "5 Units • 48 Students Enrolled",
            "Machine Learning" to "5 Units • 52 Students Enrolled",
            "Distributed Systems Lab" to "12 Lab Exercises Completed"
        ).forEach { (sub, stats) ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth().border(1.dp, SlateDarkBorder, RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(sub, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(stats, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    TagChip(text = "Active Course", color = PrimaryIndigoDark)
                }
            }
        }
    }

    if (showUploadNoteDialog) {
        var subject by remember { mutableStateOf("Artificial Intelligence") }
        var unit by remember { mutableStateOf("Unit 3") }
        var title by remember { mutableStateOf("") }
        var summary by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showUploadNoteDialog = false },
            title = { Text("Upload Department Notes") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = subject, onValueChange = { subject = it }, label = { Text("Subject") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = unit, onValueChange = { unit = it }, label = { Text("Unit") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Module Title") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = summary, onValueChange = { summary = it }, label = { Text("Syllabus Summary") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            viewModel.facultyUploadNote(subject, unit, title, NoteType.PDF, summary)
                            showUploadNoteDialog = false
                            Toast.makeText(context, "Note published to student learning portal!", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Publish to Students")
                }
            },
            dismissButton = {
                TextButton(onClick = { showUploadNoteDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun SuperAdminView(viewModel: SkillszViewModel, modifier: Modifier = Modifier) {
    val institutes by viewModel.institutes.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SlateDarkSurfaceVariant),
            modifier = Modifier.fillMaxWidth().border(1.dp, VioletPurple.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("SKILLSZ Platform Multi-Tenant SuperAdmin", fontWeight = FontWeight.Black, fontSize = 15.sp, color = VioletPurple)
                Text("Global infrastructure overview across all affiliated technical universities", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        SectionHeader(title = "Global Platform Statistics")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricStatCard("Institutes", "${institutes.size.coerceAtLeast(4)}", "Active Campuses", Icons.Default.AccountBalance, PrimaryIndigoDark, {}, Modifier.weight(1f))
            MetricStatCard("Total Students", "14,000+", "Pan-India Enrolled", Icons.Default.People, CyanAccentDark, {}, Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricStatCard("AI Queries", "285,420", "Career Coaching", Icons.Default.AutoAwesome, AmberWarning, {}, Modifier.weight(1f))
            MetricStatCard("Placement Rate", "88.2%", "Tier 1 Drives", Icons.Default.Verified, EmeraldSuccess, {}, Modifier.weight(1f))
        }

        SectionHeader(title = "Registered Institutes (Tenant Isolation)")

        val list = if (institutes.isNotEmpty()) institutes else listOf(
            com.example.data.model.Institute("iitd", "TECH-IITD", "Indian Institute of Technology Delhi", "New Delhi", 4200),
            com.example.data.model.Institute("nitt", "NIT-TRICHY", "National Institute of Technology Trichy", "Trichy", 3800),
            com.example.data.model.Institute("bitm", "BIT-MESRA", "Birla Institute of Technology", "Ranchi", 3100),
            com.example.data.model.Institute("mit", "MIT-TECH", "MIT College of Engineering", "Pune", 2900)
        )

        list.forEach { inst ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth().border(1.dp, SlateDarkBorder, RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(inst.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("${inst.code} • ${inst.city} • ${inst.studentCount} Students", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    TagChip(text = "Healthy", color = EmeraldSuccess)
                }
            }
        }
    }
}
