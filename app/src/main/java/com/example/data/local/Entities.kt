package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserAccountEntity(
    @PrimaryKey val id: String,
    val instituteId: String,
    val rollNumber: String,
    val name: String,
    val email: String,
    val role: String,
    val course: String,
    val branch: String,
    val year: String,
    val semester: String,
    val section: String,
    val passwordHash: String,
    val careerGoal: String,
    val profileCompletion: Int = 78,
    val gpa: Double = 8.65
)

@Entity(tableName = "institutes")
data class InstituteRecordEntity(
    @PrimaryKey val id: String,
    val code: String,
    val name: String,
    val city: String,
    val studentCount: Int
)

@Entity(tableName = "notes")
data class NoteRecordEntity(
    @PrimaryKey val id: String,
    val instituteId: String,
    val course: String,
    val branch: String,
    val year: String,
    val semester: String,
    val subject: String,
    val unit: String,
    val title: String,
    val type: String,
    val fileUrl: String,
    val summary: String,
    val downloads: Int = 120,
    val isBookmarked: Boolean = false
)

@Entity(tableName = "jobs")
data class JobRecordEntity(
    @PrimaryKey val id: String,
    val instituteId: String,
    val title: String,
    val company: String,
    val location: String,
    val type: String,
    val isInternship: Boolean,
    val stipendOrSalary: String,
    val deadline: String,
    val requiredSkills: String, // comma separated
    val description: String,
    val isCampusDrive: Boolean = false,
    val experience: String = "Fresher"
)

@Entity(tableName = "applications")
data class ApplicationRecordEntity(
    @PrimaryKey val id: String,
    val jobId: String,
    val jobTitle: String,
    val company: String,
    val studentId: String,
    val instituteId: String,
    val appliedDate: String,
    val status: String,
    val resumeName: String
)

@Entity(tableName = "resumes")
data class ResumeRecordEntity(
    @PrimaryKey val id: String,
    val studentId: String,
    val title: String,
    val templateId: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val location: String,
    val github: String,
    val linkedin: String,
    val summary: String,
    val skillsCsv: String,
    val atsScore: Int
)

@Entity(tableName = "test_results")
data class TestResultRecordEntity(
    @PrimaryKey val id: String,
    val testId: String,
    val testTitle: String,
    val studentId: String,
    val score: Int,
    val totalScore: Int,
    val percentage: Int,
    val correctCount: Int,
    val wrongCount: Int,
    val timeTakenSeconds: Int,
    val completedDate: String,
    val weakAreasCsv: String
)
