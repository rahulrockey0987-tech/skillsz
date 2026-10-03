package com.example.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import com.example.data.model.UserRole
import com.example.ui.components.SkillszTopBar
import com.example.ui.screens.admin.RoleAdminPanelScreen
import com.example.ui.screens.ai.AIChatScreen
import com.example.ui.screens.ai.MockInterviewScreen
import com.example.ui.screens.auth.LoginScreen
import com.example.ui.screens.code.CodingArenaScreen
import com.example.ui.screens.dashboard.StudentDashboardScreen
import com.example.ui.screens.jobs.JobsAndInternshipsScreen
import com.example.ui.screens.learning.LearningNotesScreen
import com.example.ui.screens.notifications.NotificationsScreen
import com.example.ui.screens.profile.StudentProfileScreen
import com.example.ui.screens.projects.ProjectsScreen
import com.example.ui.screens.resume.ResumeBuilderScreen
import com.example.ui.screens.roadmap.CareerRoadmapScreen
import com.example.ui.screens.tests.TestsAndAssessmentsScreen
import com.example.ui.theme.*
import com.example.viewmodel.SkillszViewModel

@Composable
fun SkillszMainScaffold(
    viewModel: SkillszViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val context = LocalContext.current

    if (currentUser == null) {
        LoginScreen(viewModel = viewModel)
        return
    }

    var selectedMainTab by remember { mutableStateOf(MainDestination.DASHBOARD) }
    var activeSubDestination by remember { mutableStateOf<SubDestination?>(null) }
    var showRoleSwitchDialog by remember { mutableStateOf(false) }

    // Intercept back button when inside a secondary sub-screen
    BackHandler(enabled = activeSubDestination != null) {
        activeSubDestination = null
    }

    Scaffold(
        topBar = {
            if (activeSubDestination == null) {
                SkillszTopBar(
                    currentUser = currentUser,
                    unreadNotifications = notifications.size,
                    onNotificationsClick = { activeSubDestination = SubDestination.NOTIFICATIONS },
                    onRoleSwitchClick = { showRoleSwitchDialog = true },
                    onLogoutClick = {
                        viewModel.logout()
                        Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        },
        bottomBar = {
            if (activeSubDestination == null) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp,
                    windowInsets = WindowInsets.navigationBars,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    MainDestination.values().forEach { destination ->
                        val isSelected = selectedMainTab == destination
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { selectedMainTab = destination },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                                    contentDescription = destination.title
                                )
                            },
                            label = {
                                Text(
                                    text = destination.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = PrimaryIndigoDark,
                                selectedTextColor = PrimaryIndigoDark,
                                indicatorColor = PrimaryIndigo.copy(alpha = 0.15f),
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                activeSubDestination != null -> {
                    when (activeSubDestination!!) {
                        SubDestination.TESTS -> {
                            TestsAndAssessmentsScreen(viewModel = viewModel)
                        }
                        SubDestination.CODE_ARENA -> {
                            CodingArenaScreen(
                                viewModel = viewModel,
                                onBack = { activeSubDestination = null }
                            )
                        }
                        SubDestination.ROADMAP -> {
                            CareerRoadmapScreen(
                                viewModel = viewModel,
                                onBack = { activeSubDestination = null }
                            )
                        }
                        SubDestination.PROJECTS -> {
                            ProjectsScreen(
                                viewModel = viewModel,
                                onBack = { activeSubDestination = null }
                            )
                        }
                        SubDestination.PROFILE -> {
                            StudentProfileScreen(
                                viewModel = viewModel,
                                onNavigateToResume = {
                                    activeSubDestination = null
                                    selectedMainTab = MainDestination.RESUME
                                }
                            )
                        }
                        SubDestination.NOTIFICATIONS -> {
                            NotificationsScreen(
                                viewModel = viewModel,
                                onBack = { activeSubDestination = null }
                            )
                        }
                        SubDestination.MOCK_INTERVIEW -> {
                            MockInterviewScreen(
                                viewModel = viewModel,
                                onBack = { activeSubDestination = null }
                            )
                        }
                        SubDestination.ADMIN_PANEL -> {
                            RoleAdminPanelScreen(
                                viewModel = viewModel,
                                onBack = { activeSubDestination = null }
                            )
                        }
                    }
                }
                else -> {
                    when (selectedMainTab) {
                        MainDestination.DASHBOARD -> {
                            StudentDashboardScreen(
                                viewModel = viewModel,
                                onNavigateToAi = { selectedMainTab = MainDestination.AI },
                                onNavigateToResume = { selectedMainTab = MainDestination.RESUME },
                                onNavigateToJobs = { selectedMainTab = MainDestination.JOBS },
                                onNavigateToInternships = { selectedMainTab = MainDestination.JOBS },
                                onNavigateToNotes = { selectedMainTab = MainDestination.LEARNING },
                                onNavigateToTests = { activeSubDestination = SubDestination.TESTS },
                                onNavigateToCodeArena = { activeSubDestination = SubDestination.CODE_ARENA },
                                onNavigateToMockInterview = { activeSubDestination = SubDestination.MOCK_INTERVIEW },
                                onNavigateToRoadmap = { activeSubDestination = SubDestination.ROADMAP },
                                onNavigateToProjects = { activeSubDestination = SubDestination.PROJECTS }
                            )
                        }
                        MainDestination.AI -> {
                            AIChatScreen(
                                viewModel = viewModel,
                                onNavigateToResume = { selectedMainTab = MainDestination.RESUME },
                                onNavigateToInterview = { activeSubDestination = SubDestination.MOCK_INTERVIEW }
                            )
                        }
                        MainDestination.RESUME -> {
                            ResumeBuilderScreen(viewModel = viewModel)
                        }
                        MainDestination.JOBS -> {
                            JobsAndInternshipsScreen(viewModel = viewModel)
                        }
                        MainDestination.LEARNING -> {
                            LearningNotesScreen(
                                viewModel = viewModel,
                                onAskAIAboutTopic = { query ->
                                    selectedMainTab = MainDestination.AI
                                    viewModel.sendAIMessage(query)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Role Switch Dialog
    if (showRoleSwitchDialog) {
        AlertDialog(
            onDismissRequest = { showRoleSwitchDialog = false },
            title = {
                Text(
                    text = "Switch Platform Role",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Instant 1-tap demo credentials for evaluating role-based isolated views:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    RoleSelectButton("Student (Aryan Sharma • IITD)", UserRole.STUDENT, currentUser?.role) {
                        viewModel.quickLoginAs(UserRole.STUDENT)
                        activeSubDestination = null
                        showRoleSwitchDialog = false
                        Toast.makeText(context, "Switched to Student: Aryan Sharma (IITD)", Toast.LENGTH_SHORT).show()
                    }

                    RoleSelectButton("Faculty (Dr. Priya Raman • CSE)", UserRole.FACULTY, currentUser?.role) {
                        viewModel.quickLoginAs(UserRole.FACULTY)
                        activeSubDestination = SubDestination.ADMIN_PANEL
                        showRoleSwitchDialog = false
                        Toast.makeText(context, "Switched to Faculty: Dr. Priya Raman", Toast.LENGTH_SHORT).show()
                    }

                    RoleSelectButton("Institute Admin (Prof. Rajesh Verma)", UserRole.INSTITUTE_ADMIN, currentUser?.role) {
                        viewModel.quickLoginAs(UserRole.INSTITUTE_ADMIN)
                        activeSubDestination = SubDestination.ADMIN_PANEL
                        showRoleSwitchDialog = false
                        Toast.makeText(context, "Switched to Institute Admin: IIT Delhi", Toast.LENGTH_SHORT).show()
                    }

                    RoleSelectButton("Super Admin (SKILLSZ HQ)", UserRole.SUPER_ADMIN, currentUser?.role) {
                        viewModel.quickLoginAs(UserRole.SUPER_ADMIN)
                        activeSubDestination = SubDestination.ADMIN_PANEL
                        showRoleSwitchDialog = false
                        Toast.makeText(context, "Switched to Super Admin (Global Tenant Monitor)", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showRoleSwitchDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun RoleSelectButton(
    title: String,
    role: UserRole,
    currentRole: UserRole?,
    onClick: () -> Unit
) {
    val isSelected = currentRole == role
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) PrimaryIndigo.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) CyanAccentDark else MaterialTheme.colorScheme.onSurface
            )
            if (isSelected) {
                Icon(Icons.Default.Check, contentDescription = null, tint = CyanAccentDark, modifier = Modifier.size(16.dp))
            }
        }
    }
}
