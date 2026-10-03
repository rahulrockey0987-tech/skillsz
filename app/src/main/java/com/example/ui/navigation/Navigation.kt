package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class MainDestination(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    DASHBOARD("dashboard", "Home", Icons.Filled.Dashboard, Icons.Outlined.Dashboard),
    AI("ai_chat", "AI Career", Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome),
    RESUME("resume_builder", "Resume", Icons.Filled.Description, Icons.Outlined.Description),
    JOBS("jobs_internships", "Jobs", Icons.Filled.Work, Icons.Outlined.WorkOutline),
    LEARNING("learning_notes", "Learn", Icons.Filled.MenuBook, Icons.Outlined.MenuBook)
}

enum class SubDestination(val route: String, val title: String) {
    TESTS("tests", "Tests & Assessments"),
    CODE_ARENA("code_arena", "Code Practice"),
    ROADMAP("roadmap", "Career Roadmap"),
    PROJECTS("projects", "Projects"),
    PROFILE("profile", "Profile"),
    NOTIFICATIONS("notifications", "Notifications"),
    MOCK_INTERVIEW("mock_interview", "Mock Interview"),
    ADMIN_PANEL("admin_panel", "Admin Panel")
}
