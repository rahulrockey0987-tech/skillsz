package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.GeminiAiService
import com.example.data.local.SkillszDatabase
import com.example.data.local.UserAccountEntity
import com.example.data.model.*
import com.example.data.repository.SkillszRepository
import com.example.data.repository.StaticDataProvider
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface LoginUiState {
    object Idle : LoginUiState
    object Loading : LoginUiState
    data class Success(val user: CurrentUser) : LoginUiState
    data class Error(val message: String) : LoginUiState
}

class SkillszViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SkillszRepository
    init {
        val db = SkillszDatabase.getDatabase(application)
        repository = SkillszRepository(db.skillszDao())
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    private val _currentUser = MutableStateFlow<CurrentUser?>(null)
    val currentUser: StateFlow<CurrentUser?> = _currentUser.asStateFlow()

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    val institutes: StateFlow<List<Institute>> = repository.getInstitutes()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Active Institute ID (defaults to "iitd" or current user's institute)
    private val activeInstituteId = _currentUser.map { it?.instituteId ?: "iitd" }

    val notes: StateFlow<List<AcademicNote>> = activeInstituteId.flatMapLatest { id ->
        repository.getNotes(id)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val jobs: StateFlow<List<JobPosting>> = activeInstituteId.flatMapLatest { id ->
        repository.getJobs(id)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val applications: StateFlow<List<JobApplication>> = _currentUser.flatMapLatest { user ->
        if (user != null) repository.getApplications(user.id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val testResults: StateFlow<List<TestResultData>> = _currentUser.flatMapLatest { user ->
        if (user != null) repository.getTestResults(user.id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val resumes: StateFlow<List<ResumeData>> = _currentUser.flatMapLatest { user ->
        if (user != null) repository.getResumes(user.id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _activeResume = MutableStateFlow(
        ResumeData(
            id = "res_draft",
            fullName = "Aryan Sharma",
            email = "aryan.cse@iitd.ac.in",
            phone = "+91 98765 43210",
            location = "New Delhi, India",
            summary = "Computer Science undergraduate passionate about full-stack engineering, high-performance Android architectures, and distributed systems. Experienced with Kotlin, Jetpack Compose, Docker, and PostgreSQL.",
            skills = listOf("Kotlin", "Jetpack Compose", "Java", "Python", "Data Structures", "React", "Node.js", "Docker", "PostgreSQL", "REST APIs", "Git"),
            atsScore = 84
        )
    )
    val activeResume: StateFlow<ResumeData> = _activeResume.asStateFlow()

    // Coding Arena & Tests
    val codingProblems = MutableStateFlow(StaticDataProvider.getCodingProblems())
    val assessmentTests = MutableStateFlow(StaticDataProvider.getAssessmentTests())
    val studentProjects = MutableStateFlow(StaticDataProvider.getStudentProjects())

    // AI Career Chat Messages
    private val _aiMessages = MutableStateFlow<List<AIChatMessage>>(
        listOf(
            AIChatMessage(
                id = "ai_welcome",
                isUser = false,
                message = "Welcome to **SKILLSZ AI**! I'm your dedicated engineering career mentor. How can I accelerate your learning, interview prep, or resume today?",
                timestamp = "Just now",
                suggestedActions = listOf(
                    "Improve my resume",
                    "Find skill gaps",
                    "Prepare for interview",
                    "Create study plan",
                    "Give me project ideas"
                )
            )
        )
    )
    val aiMessages: StateFlow<List<AIChatMessage>> = _aiMessages.asStateFlow()

    private val _isAiGenerating = MutableStateFlow(false)
    val isAiGenerating: StateFlow<Boolean> = _isAiGenerating.asStateFlow()

    private val _isResumeGenerating = MutableStateFlow(false)
    val isResumeGenerating: StateFlow<Boolean> = _isResumeGenerating.asStateFlow()

    // Notifications
    val notifications = MutableStateFlow<List<NotificationItem>>(
        listOf(
            NotificationItem("n1", "Campus Drive: Google", "Google campus recruitment drive opened for 2026 Batch CSE/IT.", "Job", "10m ago"),
            NotificationItem("n2", "Upcoming Test Scheduled", "CSE Core Engineering Diagnostic test is scheduled for tomorrow 10:00 AM.", "Test", "1h ago"),
            NotificationItem("n3", "New Academic Note Uploaded", "Dr. Priya Raman uploaded Unit 3 DBMS notes on Concurrency Control.", "Academic", "3h ago"),
            NotificationItem("n4", "Application Shortlisted!", "Razorpay shortlisted your application for Full Stack Intern!", "Application", "1d ago")
        )
    )

    // Institute Admin Students List
    val instituteStudents: StateFlow<List<UserAccountEntity>> = activeInstituteId.flatMapLatest { id ->
        repository.getAllStudentsForInstitute(id)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Auto-login to default Aryan demo account on first run for great UX
    init {
        quickLoginAs(UserRole.STUDENT)
    }

    fun login(roll: String, pass: String, instId: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading
            val entity = repository.login(roll, pass, instId)
            if (entity != null) {
                val user = CurrentUser(
                    id = entity.id,
                    instituteId = entity.instituteId,
                    rollNumber = entity.rollNumber,
                    name = entity.name,
                    email = entity.email,
                    role = UserRole.valueOf(entity.role),
                    course = entity.course,
                    branch = entity.branch,
                    year = entity.year,
                    semester = entity.semester,
                    section = entity.section,
                    careerGoal = entity.careerGoal,
                    profileCompletion = entity.profileCompletion,
                    gpa = entity.gpa
                )
                _currentUser.value = user
                _loginState.value = LoginUiState.Success(user)
            } else {
                _loginState.value = LoginUiState.Error("Invalid credentials or Institute ID. Try demo login buttons below.")
            }
        }
    }

    fun quickLoginAs(role: UserRole) {
        val user = when (role) {
            UserRole.STUDENT -> CurrentUser(
                id = "usr_aryan",
                instituteId = "iitd",
                rollNumber = "CSE22041",
                name = "Aryan Sharma",
                email = "aryan.cse@iitd.ac.in",
                role = UserRole.STUDENT,
                course = "B.Tech",
                branch = "Computer Science & Engineering",
                year = "3rd Year",
                semester = "5th Semester",
                section = "A",
                careerGoal = "Full Stack Developer",
                profileCompletion = 78,
                gpa = 8.65
            )
            UserRole.FACULTY -> CurrentUser(
                id = "usr_priya",
                instituteId = "iitd",
                rollNumber = "FAC801",
                name = "Dr. Priya Raman",
                email = "priya.raman@cse.iitd.ac.in",
                role = UserRole.FACULTY,
                course = "B.Tech CSE",
                branch = "Computer Science Dept",
                year = "Faculty",
                semester = "All",
                section = "Dept",
                careerGoal = "Faculty Mentorship",
                profileCompletion = 100,
                gpa = 10.0
            )
            UserRole.INSTITUTE_ADMIN -> CurrentUser(
                id = "usr_verma",
                instituteId = "iitd",
                rollNumber = "ADM101",
                name = "Prof. Rajesh Verma",
                email = "dean.career@iitd.ac.in",
                role = UserRole.INSTITUTE_ADMIN,
                course = "Institute Administration",
                branch = "Career & Placement Cell",
                year = "Admin",
                semester = "All",
                section = "HQ",
                careerGoal = "100% Campus Placement",
                profileCompletion = 100,
                gpa = 10.0
            )
            UserRole.SUPER_ADMIN -> CurrentUser(
                id = "usr_super",
                instituteId = "GLOBAL",
                rollNumber = "SUP001",
                name = "System SuperAdmin",
                email = "admin@skillsz.platform",
                role = UserRole.SUPER_ADMIN,
                course = "Platform Governance",
                branch = "Global Architecture",
                year = "Master",
                semester = "Global",
                section = "HQ",
                careerGoal = "Global Tech Education Scale",
                profileCompletion = 100,
                gpa = 10.0
            )
        }
        _currentUser.value = user
        _loginState.value = LoginUiState.Success(user)
    }

    fun logout() {
        _currentUser.value = null
        _loginState.value = LoginUiState.Idle
    }

    fun updateCareerGoal(newGoal: String) {
        val user = _currentUser.value ?: return
        _currentUser.value = user.copy(careerGoal = newGoal)
    }

    fun toggleBookmark(noteId: String, current: Boolean) {
        viewModelScope.launch {
            repository.toggleBookmark(noteId, current)
        }
    }

    fun applyForJob(job: JobPosting, resumeName: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.applyForJob(job, user, resumeName)
            // Add notification
            val newNotification = NotificationItem(
                id = "notif_${System.currentTimeMillis()}",
                title = "Application Submitted",
                message = "Successfully submitted your resume for ${job.title} at ${job.company}.",
                category = "Application",
                timeAgo = "Just now"
            )
            notifications.value = listOf(newNotification) + notifications.value
        }
    }

    fun sendAIMessage(userText: String) {
        if (userText.isBlank()) return
        val userMsg = AIChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            isUser = true,
            message = userText.trim(),
            timestamp = "Just now"
        )
        _aiMessages.value = _aiMessages.value + userMsg

        viewModelScope.launch {
            _isAiGenerating.value = true
            try {
                val current = _currentUser.value
                val aiReplyText = GeminiAiService.generateChatResponse(userText.trim(), current)
                val aiMsg = AIChatMessage(
                    id = "ai_${System.currentTimeMillis()}",
                    isUser = false,
                    message = aiReplyText,
                    timestamp = "Just now",
                    suggestedActions = listOf(
                        "Improve my resume",
                        "Find skill gaps",
                        "Prepare for interview",
                        "Practice Coding"
                    )
                )
                _aiMessages.value = _aiMessages.value + aiMsg
            } catch (e: Exception) {
                val fallbackMsg = AIChatMessage(
                    id = "ai_err_${System.currentTimeMillis()}",
                    isUser = false,
                    message = "Here is guidance on **${userText.trim()}**:\n\n1. Focus on core problem-solving (Arrays, Hash Maps, Trees).\n2. Keep your resume projects updated with quantifiable results.\n3. Explore relevant course notes and test prep modules in SKILLSZ.",
                    timestamp = "Just now"
                )
                _aiMessages.value = _aiMessages.value + fallbackMsg
            } finally {
                _isAiGenerating.value = false
            }
        }
    }

    fun updateResume(updated: ResumeData) {
        _activeResume.value = updated
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.saveResume(updated, user.id)
        }
    }

    fun generateAIResumeEnhancements() {
        viewModelScope.launch {
            _isResumeGenerating.value = true
            try {
                val current = _activeResume.value
                val user = _currentUser.value
                val targetRole = user?.careerGoal ?: "Software Engineer"
                
                // Real AI generation for summary
                val newSummary = GeminiAiService.generateResumeSummary(user, targetRole, current.skills)
                // Real AI skill recommendations
                val recommendedSkills = GeminiAiService.recommendSkillsForRole(targetRole, current.skills)
                val combinedSkills = (current.skills + recommendedSkills).distinct()
                
                // Enhance existing projects with AI bullets
                val enhancedProjects = current.projects.map { proj ->
                    val newBullets = GeminiAiService.generateProjectBullets(proj.title, proj.techStack, user)
                    proj.copy(bullets = newBullets)
                }

                val enhanced = current.copy(
                    summary = newSummary,
                    skills = combinedSkills,
                    projects = enhancedProjects,
                    atsScore = 95
                )
                _activeResume.value = enhanced
                user?.let { repository.saveResume(enhanced, it.id) }
            } finally {
                _isResumeGenerating.value = false
            }
        }
    }

    fun generateAiProjectBullets(projectIndex: Int, title: String, techStack: String) {
        viewModelScope.launch {
            _isResumeGenerating.value = true
            try {
                val bullets = GeminiAiService.generateProjectBullets(title, techStack, _currentUser.value)
                val current = _activeResume.value
                val updatedProjects = current.projects.toMutableList()
                if (projectIndex in updatedProjects.indices) {
                    updatedProjects[projectIndex] = updatedProjects[projectIndex].copy(bullets = bullets)
                    val updated = current.copy(projects = updatedProjects)
                    _activeResume.value = updated
                    _currentUser.value?.let { repository.saveResume(updated, it.id) }
                }
            } finally {
                _isResumeGenerating.value = false
            }
        }
    }

    fun recommendAndAddSkills() {
        viewModelScope.launch {
            _isResumeGenerating.value = true
            try {
                val goal = _currentUser.value?.careerGoal ?: "Full Stack Developer"
                val recs = GeminiAiService.recommendSkillsForRole(goal, _activeResume.value.skills)
                val updated = _activeResume.value.copy(
                    skills = (_activeResume.value.skills + recs).distinct(),
                    atsScore = minOf(96, _activeResume.value.atsScore + 8)
                )
                _activeResume.value = updated
                _currentUser.value?.let { repository.saveResume(updated, it.id) }
            } finally {
                _isResumeGenerating.value = false
            }
        }
    }

    fun optimizeResumeForRole(roleName: String) {
        viewModelScope.launch {
            _isResumeGenerating.value = true
            try {
                val optimized = GeminiAiService.optimizeResumeForJob(roleName, "", _activeResume.value)
                _activeResume.value = optimized
                _currentUser.value?.let { repository.saveResume(optimized, it.id) }
            } finally {
                _isResumeGenerating.value = false
            }
        }
    }

    fun saveTestResult(testResult: TestResultData) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.saveTestResult(testResult, user.id)
        }
    }

    fun adminCreateStudent(roll: String, name: String, branch: String, year: String, sec: String) {
        val instId = _currentUser.value?.instituteId ?: "iitd"
        viewModelScope.launch {
            val newStudent = UserAccountEntity(
                id = "usr_${System.currentTimeMillis()}",
                instituteId = instId,
                rollNumber = roll,
                name = name,
                email = "$roll@$instId.ac.in",
                role = "STUDENT",
                course = "B.Tech",
                branch = branch,
                year = year,
                semester = "5th Semester",
                section = sec,
                passwordHash = "123456",
                careerGoal = "Software Engineer",
                profileCompletion = 50,
                gpa = 8.0
            )
            repository.createStudent(newStudent)
        }
    }

    fun facultyUploadNote(subject: String, unit: String, title: String, type: NoteType, summary: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val note = AcademicNote(
                id = "note_${System.currentTimeMillis()}",
                instituteId = user.instituteId,
                course = "B.Tech",
                branch = "CSE",
                year = "3rd Year",
                semester = "5th Semester",
                subject = subject,
                unit = unit,
                title = title,
                type = type,
                fileUrl = "https://skillsz.edu/uploads/$title.pdf",
                summary = summary
            )
            repository.uploadNote(note)
        }
    }

    fun adminPublishJob(title: String, company: String, stipend: String, isInternship: Boolean, skills: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val job = JobPosting(
                id = "job_${System.currentTimeMillis()}",
                instituteId = user.instituteId,
                title = title,
                company = company,
                location = "Hybrid / On-Campus",
                type = if (isInternship) JobType.INTERNSHIP else JobType.FULL_TIME,
                isInternship = isInternship,
                stipendOrSalary = stipend,
                deadline = "30 Nov 2026",
                requiredSkills = skills.split(",").map { it.trim() },
                description = "Campus placement / internship opening published directly by Institute Placement Cell.",
                isCampusDrive = true,
                experience = "Pre-Final / Final Year"
            )
            repository.publishJob(job)
        }
    }
}
