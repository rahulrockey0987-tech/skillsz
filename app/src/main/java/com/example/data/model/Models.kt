package com.example.data.model

enum class UserRole {
    STUDENT,
    FACULTY,
    INSTITUTE_ADMIN,
    SUPER_ADMIN
}

data class CurrentUser(
    val id: String,
    val instituteId: String,
    val rollNumber: String,
    val name: String,
    val email: String,
    val role: UserRole,
    val course: String,
    val branch: String,
    val year: String,
    val semester: String,
    val section: String,
    val careerGoal: String,
    val profileCompletion: Int = 78,
    val gpa: Double = 8.65,
    val skills: List<String> = listOf("Kotlin", "Jetpack Compose", "Java", "Python", "Data Structures", "React", "Node.js", "Docker", "SQL", "Git", "System Design", "REST APIs")
)

data class Institute(
    val id: String,
    val code: String,
    val name: String,
    val city: String,
    val studentCount: Int,
    val courses: List<String> = listOf("B.Tech", "M.Tech", "Diploma", "BCA", "MCA")
)

enum class JobType(val label: String) {
    FULL_TIME("Full-Time"),
    PART_TIME("Part-Time"),
    INTERNSHIP("Internship"),
    REMOTE("Remote"),
    FRESHER("Fresher")
}

enum class ApplicationStatus(val label: String) {
    APPLIED("Applied"),
    UNDER_REVIEW("Under Review"),
    SHORTLISTED("Shortlisted"),
    INTERVIEW("Interview"),
    SELECTED("Selected"),
    REJECTED("Rejected")
}

data class JobPosting(
    val id: String,
    val instituteId: String, // empty string if global
    val title: String,
    val company: String,
    val location: String,
    val type: JobType,
    val isInternship: Boolean,
    val stipendOrSalary: String,
    val deadline: String,
    val requiredSkills: List<String>,
    val description: String,
    val isCampusDrive: Boolean = false,
    val experience: String = "0-1 Years"
)

data class JobApplication(
    val id: String,
    val jobId: String,
    val jobTitle: String,
    val company: String,
    val studentId: String,
    val instituteId: String,
    val appliedDate: String,
    val status: ApplicationStatus,
    val resumeName: String,
    val statusTimeline: List<TimelineEvent>
)

data class TimelineEvent(
    val title: String,
    val date: String,
    val completed: Boolean
)

enum class NoteType(val label: String) {
    PDF("PDF Document"),
    VIDEO("Video Lecture"),
    DOC("Lecture Notes"),
    CODE("Code Cheatsheet"),
    LINK("Interactive Resource")
}

data class AcademicNote(
    val id: String,
    val instituteId: String,
    val course: String,
    val branch: String,
    val year: String,
    val semester: String,
    val subject: String,
    val unit: String,
    val title: String,
    val type: NoteType,
    val fileUrl: String,
    val summary: String,
    val downloads: Int = 142,
    val isBookmarked: Boolean = false
)

enum class TestCategory(val label: String) {
    TECHNICAL("Technical"),
    APTITUDE("Aptitude"),
    PROGRAMMING("Programming"),
    REASONING("Reasoning"),
    ENGLISH("English"),
    INTERVIEW_PREP("Interview Prep")
}

enum class QuestionType {
    MCQ,
    TRUE_FALSE,
    MULTIPLE_ANSWER
}

data class AssessmentQuestion(
    val id: String,
    val questionText: String,
    val codeSnippet: String? = null,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String,
    val topic: String
)

data class AssessmentTest(
    val id: String,
    val instituteId: String,
    val title: String,
    val category: TestCategory,
    val durationMinutes: Int,
    val totalQuestions: Int,
    val passingScore: Int,
    val difficulty: String = "Intermediate",
    val questions: List<AssessmentQuestion>
)

data class TestResultData(
    val id: String,
    val testId: String,
    val testTitle: String,
    val score: Int,
    val totalScore: Int,
    val percentage: Int,
    val correctCount: Int,
    val wrongCount: Int,
    val timeTakenSeconds: Int,
    val completedDate: String,
    val weakAreas: List<String>,
    val recommendedTopics: List<String>
)

data class ResumeInternship(
    val company: String,
    val role: String,
    val period: String,
    val description: String
)

data class ResumeData(
    val id: String,
    val title: String = "Full Stack Engineer Resume",
    val templateId: String = "modern_tech",
    val fullName: String = "Aryan Sharma",
    val email: String = "aryan.cse@skillsz.edu",
    val phone: String = "+91 98765 43210",
    val location: String = "New Delhi, India",
    val github: String = "github.com/aryansharma-dev",
    val linkedin: String = "linkedin.com/in/aryansharma",
    val portfolio: String = "aryansharma.dev",
    val summary: String = "Motivated Computer Science Engineering student skilled in modern full-stack development, Kotlin, Jetpack Compose, and distributed systems. Built production microservices with 99.9% uptime and high-performance mobile architectures.",
    val education: List<EducationItem> = listOf(
        EducationItem("Indian Institute of Technology Delhi", "B.Tech in Computer Science and Engineering", "2022 - 2026", "CGPA: 8.65/10.0")
    ),
    val skills: List<String> = listOf("Kotlin", "Jetpack Compose", "Java", "Python", "Data Structures", "React", "Node.js", "Docker", "PostgreSQL", "REST APIs", "Git", "CI/CD"),
    val projects: List<ResumeProject> = listOf(
        ResumeProject(
            title = "SKILLSZ Student Hub",
            techStack = "Kotlin, Jetpack Compose, Room, Coroutines",
            period = "2024",
            bullets = listOf(
                "Engineered multi-tenant Android platform supporting 4 roles with offline Room persistence.",
                "Optimized SQLite database indexing, reducing local query latency by 45%.",
                "Implemented AI Career Assistant using reactive Kotlin StateFlow for real-time recommendations."
            )
        ),
        ResumeProject(
            title = "Cloud Microservices API Gateway",
            techStack = "Java, Spring Boot, Docker, Redis",
            period = "2023",
            bullets = listOf(
                "Designed rate-limiting gateway handling 15,000+ requests per minute with token bucket algorithm.",
                "Containerized 6 microservices with Docker Compose and automated testing via GitHub Actions."
            )
        )
    ),
    val internships: List<ResumeInternship> = listOf(
        ResumeInternship(
            company = "FinTech Labs India",
            role = "Software Engineering Intern",
            period = "May 2024 - Jul 2024",
            description = "Developed async payment processing endpoints in Spring Boot and Docker. Refactored query execution plans improving throughput by 30%."
        )
    ),
    val certifications: List<String> = listOf(
        "Google Associate Android Developer Certified (2024)",
        "AWS Certified Cloud Practitioner (2023)",
        "Meta Front-End Developer Specialization (2023)"
    ),
    val achievements: List<String> = listOf(
        "Ranked Top 2% in Smart India Hackathon among 10,000+ teams nationwide.",
        "Winner of Institute Intra-College Coding Championship 2024.",
        "Published technical article on High Performance Android Memory Architecture."
    ),
    val languages: List<String> = listOf("English (Fluent)", "Hindi (Native)"),
    val atsScore: Int = 84
)

data class EducationItem(
    val institution: String,
    val degree: String,
    val duration: String,
    val grade: String
)

data class ResumeProject(
    val title: String,
    val techStack: String,
    val period: String,
    val bullets: List<String>
)

data class CodingProblem(
    val id: String,
    val title: String,
    val difficulty: String, // Easy, Medium, Hard
    val category: String, // Arrays, Strings, Trees, DP, Graphs
    val statement: String,
    val inputDescription: String,
    val outputDescription: String,
    val exampleInput: String,
    val exampleOutput: String,
    val constraints: String,
    val initialCode: Map<String, String>, // Language to template
    val testCasesCount: Int = 5
)

data class CareerRoadmapNode(
    val id: String,
    val phase: String, // Beginner, Fundamentals, Intermediate, Projects, Advanced, Interview Prep, Job Ready
    val title: String,
    val description: String,
    val skillsCovered: List<String>,
    val completionPercentage: Int,
    val isCompleted: Boolean = false,
    val isCurrent: Boolean = false
)

data class StudentProject(
    val id: String,
    val title: String,
    val description: String,
    val techStack: List<String>,
    val githubUrl: String,
    val liveDemoUrl: String,
    val stars: Int = 12,
    val category: String = "Full Stack"
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val category: String, // Job, Test, Academic, Application
    val timeAgo: String,
    val isRead: Boolean = false
)

data class AIChatMessage(
    val id: String,
    val isUser: Boolean,
    val message: String,
    val timestamp: String,
    val suggestedActions: List<String> = emptyList()
)
