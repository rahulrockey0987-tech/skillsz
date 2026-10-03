package com.example.data.repository

import com.example.data.local.*
import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SkillszRepository(private val dao: SkillszDao) {

    suspend fun seedInitialDataIfEmpty() = withContext(Dispatchers.IO) {
        val currentUsers = dao.getAllUsers().first()
        if (currentUsers.isNotEmpty()) return@withContext

        // Seed Institutes
        val institutes = listOf(
            InstituteRecordEntity("iitd", "TECH-IITD", "Indian Institute of Technology Delhi", "New Delhi", 4200),
            InstituteRecordEntity("nitt", "NIT-TRICHY", "National Institute of Technology Trichy", "Tiruchirappalli", 3800),
            InstituteRecordEntity("bitm", "BIT-MESRA", "Birla Institute of Technology", "Ranchi", 3100),
            InstituteRecordEntity("mit", "MIT-TECH", "MIT College of Engineering & Technology", "Pune", 2900)
        )
        dao.insertInstitutes(institutes)

        // Seed Users
        val users = listOf(
            UserAccountEntity(
                id = "usr_aryan",
                instituteId = "iitd",
                rollNumber = "CSE22041",
                name = "Aryan Sharma",
                email = "aryan.cse@iitd.ac.in",
                role = "STUDENT",
                course = "B.Tech",
                branch = "Computer Science and Engineering",
                year = "3rd Year",
                semester = "5th Semester",
                section = "A",
                passwordHash = "123456",
                careerGoal = "Full Stack Developer",
                profileCompletion = 78,
                gpa = 8.65
            ),
            UserAccountEntity(
                id = "usr_sneha",
                instituteId = "nitt",
                rollNumber = "ECE22019",
                name = "Sneha Iyer",
                email = "sneha.ece@nitt.edu",
                role = "STUDENT",
                course = "B.Tech",
                branch = "Electronics & Communication",
                year = "3rd Year",
                semester = "5th Semester",
                section = "B",
                passwordHash = "123456",
                careerGoal = "AI / ML Engineer",
                profileCompletion = 85,
                gpa = 9.12
            ),
            UserAccountEntity(
                id = "usr_priya",
                instituteId = "iitd",
                rollNumber = "FAC801",
                name = "Dr. Priya Raman",
                email = "priya.raman@cse.iitd.ac.in",
                role = "FACULTY",
                course = "B.Tech",
                branch = "Computer Science",
                year = "Faculty",
                semester = "All",
                section = "Dept CSE",
                passwordHash = "123456",
                careerGoal = "Academic Excellence",
                profileCompletion = 100,
                gpa = 10.0
            ),
            UserAccountEntity(
                id = "usr_verma",
                instituteId = "iitd",
                rollNumber = "ADM101",
                name = "Prof. Rajesh Verma",
                email = "dean.career@iitd.ac.in",
                role = "INSTITUTE_ADMIN",
                course = "Administration",
                branch = "Career & Placement Cell",
                year = "Admin",
                semester = "All",
                section = "Central Admin",
                passwordHash = "123456",
                careerGoal = "100% Student Placement",
                profileCompletion = 100,
                gpa = 10.0
            ),
            UserAccountEntity(
                id = "usr_super",
                instituteId = "GLOBAL",
                rollNumber = "SUP001",
                name = "System SuperAdmin",
                email = "admin@skillsz.platform",
                role = "SUPER_ADMIN",
                course = "Platform Governance",
                branch = "SKILLSZ HQ",
                year = "Master",
                semester = "Global",
                section = "HQ",
                passwordHash = "admin123",
                careerGoal = "Scale Technical Education",
                profileCompletion = 100,
                gpa = 10.0
            )
        )
        dao.insertUsers(users)

        // Seed Academic Notes
        val notes = listOf(
            NoteRecordEntity("n1", "iitd", "B.Tech", "CSE", "3rd Year", "5th Semester", "Artificial Intelligence", "Unit 1", "Introduction to AI & State Space Search", "PDF", "https://skillsz.edu/notes/ai_u1.pdf", "Search heuristics, A* algorithm, Uniform Cost Search, and Adversarial Games with Minimax."),
            NoteRecordEntity("n2", "iitd", "B.Tech", "CSE", "3rd Year", "5th Semester", "Artificial Intelligence", "Unit 2", "Knowledge Representation & First Order Logic", "DOC", "https://skillsz.edu/notes/ai_u2.doc", "Propositional logic, unification, resolution theorem proving, and semantic networks."),
            NoteRecordEntity("n3", "iitd", "B.Tech", "CSE", "3rd Year", "5th Semester", "Machine Learning", "Unit 1", "Supervised Learning & Regression Models", "VIDEO", "https://skillsz.edu/notes/ml_u1.mp4", "Cost functions, gradient descent, polynomial regression, overfitting, and regularization techniques."),
            NoteRecordEntity("n4", "iitd", "B.Tech", "CSE", "3rd Year", "5th Semester", "Machine Learning", "Unit 2", "Classification Algorithms & Decision Trees", "PDF", "https://skillsz.edu/notes/ml_u2.pdf", "Logistic regression, SVM with kernel trick, ID3/C4.5 decision tree entropy and pruning."),
            NoteRecordEntity("n5", "iitd", "B.Tech", "CSE", "3rd Year", "5th Semester", "Data Structures", "Unit 1", "Advanced Trees & Balanced Search Trees", "CODE", "https://skillsz.edu/notes/dsa_u1.kt", "AVL Tree rotations, Red-Black tree invariants, B-Trees for database indexing with Kotlin code."),
            NoteRecordEntity("n6", "iitd", "B.Tech", "CSE", "3rd Year", "5th Semester", "DBMS", "Unit 3", "Transaction Processing, ACID & Concurrency Control", "PDF", "https://skillsz.edu/notes/dbms_u3.pdf", "Two-Phase Locking (2PL), serializability, timestamp ordering, and distributed recovery protocols."),
            NoteRecordEntity("n7", "iitd", "B.Tech", "CSE", "3rd Year", "5th Semester", "Computer Networks", "Unit 2", "TCP/IP Transport Layer & Congestion Control", "PDF", "https://skillsz.edu/notes/cn_u2.pdf", "TCP 3-way handshake, sliding window, slow start, congestion avoidance, and UDP multiplexing."),
            NoteRecordEntity("n8", "iitd", "B.Tech", "CSE", "3rd Year", "5th Semester", "Operating Systems", "Unit 4", "Virtual Memory Management & Page Replacement", "PDF", "https://skillsz.edu/notes/os_u4.pdf", "TLB lookup, inverted page tables, LRU vs FIFO page replacement, and thrashing prevention.")
        )
        dao.insertNotes(notes)

        // Seed Jobs and Internships
        val jobs = listOf(
            JobRecordEntity(
                id = "j1",
                instituteId = "iitd",
                title = "Software Engineer (Campus Placement)",
                company = "Google",
                location = "Bangalore, India",
                type = "FULL_TIME",
                isInternship = false,
                stipendOrSalary = "₹32 - 42 LPA",
                deadline = "15 Nov 2026",
                requiredSkills = "Java, Kotlin, C++, Data Structures, Distributed Systems",
                description = "Build global cloud and Android consumer experiences. Direct on-campus recruitment drive for final & pre-final year students.",
                isCampusDrive = true,
                experience = "Fresher (2026 Batch)"
            ),
            JobRecordEntity(
                id = "j2",
                instituteId = "",
                title = "Full Stack Engineer Intern",
                company = "Razorpay",
                location = "Remote / Bangalore",
                type = "INTERNSHIP",
                isInternship = true,
                stipendOrSalary = "₹50,000 / month",
                deadline = "28 Oct 2026",
                requiredSkills = "React, Node.js, TypeScript, REST APIs, PostgreSQL",
                description = "Work with payments core infrastructure team to build seamless merchant onboarding flows and real-time ledger APIs.",
                isCampusDrive = false,
                experience = "Pre-final Year"
            ),
            JobRecordEntity(
                id = "j3",
                instituteId = "",
                title = "AI / ML Research Intern",
                company = "Microsoft Research",
                location = "Hyderabad, India",
                type = "INTERNSHIP",
                isInternship = true,
                stipendOrSalary = "₹65,000 / month",
                deadline = "10 Nov 2026",
                requiredSkills = "Python, PyTorch, LLMs, Transformers, Statistics",
                description = "Research lightweight model distillation and multimodal agentic reasoning architectures for enterprise systems.",
                isCampusDrive = false,
                experience = "3rd / 4th Year"
            ),
            JobRecordEntity(
                id = "j4",
                instituteId = "iitd",
                title = "Associate SDE - Android Platform",
                company = "PhonePe",
                location = "Pune / Bangalore",
                type = "FULL_TIME",
                isInternship = false,
                stipendOrSalary = "₹22 - 28 LPA",
                deadline = "20 Nov 2026",
                requiredSkills = "Kotlin, Jetpack Compose, Coroutines, Room, Architecture Components",
                description = "Scale consumer app with 500M+ installs. Focus on edge offline performance, payment reliability, and responsive UI.",
                isCampusDrive = true,
                experience = "Fresher"
            ),
            JobRecordEntity(
                id = "j5",
                instituteId = "",
                title = "Cloud & DevOps Intern",
                company = "CRED",
                location = "Bangalore (Hybrid)",
                type = "INTERNSHIP",
                isInternship = true,
                stipendOrSalary = "₹45,000 / month",
                deadline = "05 Nov 2026",
                requiredSkills = "Docker, Kubernetes, AWS, Terraform, Linux",
                description = "Help automate CI/CD release pipelines and monitor distributed Kubernetes clusters with Prometheus & Grafana.",
                isCampusDrive = false,
                experience = "3rd / 4th Year"
            ),
            JobRecordEntity(
                id = "j6",
                instituteId = "",
                title = "Cyber Security Analyst Intern",
                company = "Palo Alto Networks",
                location = "Remote",
                type = "INTERNSHIP",
                isInternship = true,
                stipendOrSalary = "₹40,000 / month",
                deadline = "18 Nov 2026",
                requiredSkills = "Network Security, OWASP Top 10, Penetration Testing, Python",
                description = "Participate in vulnerability assessments, security telemetry analysis, and automated threat hunting script creation.",
                isCampusDrive = false,
                experience = "Pre-final Year"
            )
        )
        dao.insertJobs(jobs)

        // Seed Student Applications
        val applications = listOf(
            ApplicationRecordEntity("app_1", "j1", "Software Engineer (Campus Placement)", "Google", "usr_aryan", "iitd", "28 Sep 2026", "SHORTLISTED", "Aryan_Resume_FullStack.pdf"),
            ApplicationRecordEntity("app_2", "j2", "Full Stack Engineer Intern", "Razorpay", "usr_aryan", "iitd", "01 Oct 2026", "INTERVIEW", "Aryan_Resume_FullStack.pdf"),
            ApplicationRecordEntity("app_3", "j4", "Associate SDE - Android Platform", "PhonePe", "usr_aryan", "iitd", "02 Oct 2026", "UNDER_REVIEW", "Aryan_Resume_Android.pdf")
        )
        applications.forEach { dao.insertApplication(it) }

        // Seed Sample Resume
        dao.insertResume(
            ResumeRecordEntity(
                id = "res_1",
                studentId = "usr_aryan",
                title = "Aryan Sharma - Full Stack Developer",
                templateId = "modern_tech",
                fullName = "Aryan Sharma",
                email = "aryan.cse@iitd.ac.in",
                phone = "+91 98765 43210",
                location = "New Delhi, India",
                github = "github.com/aryansharma-dev",
                linkedin = "linkedin.com/in/aryansharma-tech",
                summary = "Aspiring Full Stack Engineer and CSE undergraduate with strong foundations in algorithms, modern Android, Kotlin, and backend microservices. Passionate about architecting scalable systems and AI-powered educational tools.",
                skillsCsv = "Kotlin, Jetpack Compose, Java, Python, React, Node.js, Room, Coroutines, Docker, PostgreSQL, REST APIs, Git",
                atsScore = 84
            )
        )

        // Seed Test Results
        dao.insertTestResult(
            TestResultRecordEntity(
                id = "tr_1",
                testId = "test_dsa",
                testTitle = "Data Structures & Algorithms Diagnostic",
                studentId = "usr_aryan",
                score = 85,
                totalScore = 100,
                percentage = 85,
                correctCount = 17,
                wrongCount = 3,
                timeTakenSeconds = 1120,
                completedDate = "01 Oct 2026",
                weakAreasCsv = "Dynamic Programming, Graph Shortest Paths"
            )
        )
    }

    // Auth & Users
    suspend fun login(rollNumber: String, password: String, instituteId: String): UserAccountEntity? {
        val user = dao.getUserByRollAndInstitute(rollNumber.trim(), instituteId.trim()) ?: return null
        if (user.passwordHash == password.trim()) {
            return user
        }
        return null
    }

    fun getInstitutes(): Flow<List<Institute>> = dao.getAllInstitutes().map { list ->
        list.map { Institute(it.id, it.code, it.name, it.city, it.studentCount) }
    }

    fun getNotes(instituteId: String): Flow<List<AcademicNote>> = dao.getNotesByInstitute(instituteId).map { list ->
        list.map {
            AcademicNote(
                id = it.id,
                instituteId = it.instituteId,
                course = it.course,
                branch = it.branch,
                year = it.year,
                semester = it.semester,
                subject = it.subject,
                unit = it.unit,
                title = it.title,
                type = NoteType.valueOf(it.type),
                fileUrl = it.fileUrl,
                summary = it.summary,
                downloads = it.downloads,
                isBookmarked = it.isBookmarked
            )
        }
    }

    suspend fun toggleBookmark(noteId: String, current: Boolean) {
        dao.setBookmark(noteId, !current)
    }

    fun getJobs(instituteId: String): Flow<List<JobPosting>> = dao.getJobsForInstitute(instituteId).map { list ->
        list.map {
            JobPosting(
                id = it.id,
                instituteId = it.instituteId,
                title = it.title,
                company = it.company,
                location = it.location,
                type = JobType.valueOf(it.type),
                isInternship = it.isInternship,
                stipendOrSalary = it.stipendOrSalary,
                deadline = it.deadline,
                requiredSkills = it.requiredSkills.split(",").map { s -> s.trim() },
                description = it.description,
                isCampusDrive = it.isCampusDrive,
                experience = it.experience
            )
        }
    }

    fun getApplications(studentId: String): Flow<List<JobApplication>> = dao.getApplicationsByStudent(studentId).map { list ->
        list.map {
            val status = ApplicationStatus.valueOf(it.status)
            JobApplication(
                id = it.id,
                jobId = it.jobId,
                jobTitle = it.jobTitle,
                company = it.company,
                studentId = it.studentId,
                instituteId = it.instituteId,
                appliedDate = it.appliedDate,
                status = status,
                resumeName = it.resumeName,
                statusTimeline = generateTimeline(it.appliedDate, status)
            )
        }
    }

    private fun generateTimeline(appliedDate: String, status: ApplicationStatus): List<TimelineEvent> {
        val stages = listOf(
            ApplicationStatus.APPLIED to "Application Submitted",
            ApplicationStatus.UNDER_REVIEW to "Resume Screened by Recruiter",
            ApplicationStatus.SHORTLISTED to "Shortlisted for Technical Assessment",
            ApplicationStatus.INTERVIEW to "Technical & Behavioral Rounds",
            ApplicationStatus.SELECTED to "Offer Letter Issued"
        )
        val currentIndex = stages.indexOfFirst { it.first == status }.takeIf { it >= 0 } ?: 0
        return stages.mapIndexed { idx, pair ->
            TimelineEvent(
                title = pair.second,
                date = if (idx <= currentIndex) appliedDate else "Upcoming",
                completed = idx <= currentIndex
            )
        }
    }

    suspend fun applyForJob(job: JobPosting, student: CurrentUser, resumeName: String) {
        val app = ApplicationRecordEntity(
            id = "app_${System.currentTimeMillis()}",
            jobId = job.id,
            jobTitle = job.title,
            company = job.company,
            studentId = student.id,
            instituteId = student.instituteId,
            appliedDate = "Today",
            status = ApplicationStatus.APPLIED.name,
            resumeName = resumeName
        )
        dao.insertApplication(app)
    }

    fun getTestResults(studentId: String): Flow<List<TestResultData>> = dao.getTestResultsByStudent(studentId).map { list ->
        list.map {
            TestResultData(
                id = it.id,
                testId = it.testId,
                testTitle = it.testTitle,
                score = it.score,
                totalScore = it.totalScore,
                percentage = it.percentage,
                correctCount = it.correctCount,
                wrongCount = it.wrongCount,
                timeTakenSeconds = it.timeTakenSeconds,
                completedDate = it.completedDate,
                weakAreas = it.weakAreasCsv.split(",").map { s -> s.trim() }.filter { s -> s.isNotEmpty() },
                recommendedTopics = listOf("Review Heap & Tree Invariants", "Practice 2-Pointer Sliding Window", "Graph BFS / Dijkstra Algorithms")
            )
        }
    }

    suspend fun saveTestResult(result: TestResultData, studentId: String) {
        dao.insertTestResult(
            TestResultRecordEntity(
                id = result.id,
                testId = result.testId,
                testTitle = result.testTitle,
                studentId = studentId,
                score = result.score,
                totalScore = result.totalScore,
                percentage = result.percentage,
                correctCount = result.correctCount,
                wrongCount = result.wrongCount,
                timeTakenSeconds = result.timeTakenSeconds,
                completedDate = result.completedDate,
                weakAreasCsv = result.weakAreas.joinToString(",")
            )
        )
    }

    fun getResumes(studentId: String): Flow<List<ResumeData>> = dao.getResumesByStudent(studentId).map { list ->
        list.map {
            ResumeData(
                id = it.id,
                title = it.title,
                templateId = it.templateId,
                fullName = it.fullName,
                email = it.email,
                phone = it.phone,
                location = it.location,
                github = it.github,
                linkedin = it.linkedin,
                summary = it.summary,
                skills = it.skillsCsv.split(",").map { s -> s.trim() },
                atsScore = it.atsScore
            )
        }
    }

    suspend fun saveResume(resume: ResumeData, studentId: String) {
        dao.insertResume(
            ResumeRecordEntity(
                id = resume.id,
                studentId = studentId,
                title = resume.title,
                templateId = resume.templateId,
                fullName = resume.fullName,
                email = resume.email,
                phone = resume.phone,
                location = resume.location,
                github = resume.github,
                linkedin = resume.linkedin,
                summary = resume.summary,
                skillsCsv = resume.skills.joinToString(","),
                atsScore = resume.atsScore
            )
        )
    }

    fun getAllStudentsForInstitute(instituteId: String): Flow<List<UserAccountEntity>> =
        dao.getUsersByInstitute(instituteId)

    suspend fun createStudent(user: UserAccountEntity) {
        dao.insertUser(user)
    }

    suspend fun uploadNote(note: AcademicNote) {
        dao.insertNote(
            NoteRecordEntity(
                id = note.id,
                instituteId = note.instituteId,
                course = note.course,
                branch = note.branch,
                year = note.year,
                semester = note.semester,
                subject = note.subject,
                unit = note.unit,
                title = note.title,
                type = note.type.name,
                fileUrl = note.fileUrl,
                summary = note.summary
            )
        )
    }

    suspend fun publishJob(job: JobPosting) {
        dao.insertJob(
            JobRecordEntity(
                id = job.id,
                instituteId = job.instituteId,
                title = job.title,
                company = job.company,
                location = job.location,
                type = job.type.name,
                isInternship = job.isInternship,
                stipendOrSalary = job.stipendOrSalary,
                deadline = job.deadline,
                requiredSkills = job.requiredSkills.joinToString(","),
                description = job.description,
                isCampusDrive = job.isCampusDrive,
                experience = job.experience
            )
        )
    }
}
