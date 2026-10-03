package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SkillszDao {

    // Institutes
    @Query("SELECT * FROM institutes")
    fun getAllInstitutes(): Flow<List<InstituteRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInstitutes(institutes: List<InstituteRecordEntity>)

    // Users
    @Query("SELECT * FROM users WHERE rollNumber = :roll AND instituteId = :instId LIMIT 1")
    suspend fun getUserByRollAndInstitute(roll: String, instId: String): UserAccountEntity?

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: String): UserAccountEntity?

    @Query("SELECT * FROM users WHERE instituteId = :instituteId")
    fun getUsersByInstitute(instituteId: String): Flow<List<UserAccountEntity>>

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserAccountEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserAccountEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserAccountEntity)

    @Update
    suspend fun updateUser(user: UserAccountEntity)

    // Notes
    @Query("SELECT * FROM notes WHERE instituteId = :instituteId OR instituteId = 'GLOBAL' ORDER BY subject ASC, unit ASC")
    fun getNotesByInstitute(instituteId: String): Flow<List<NoteRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: List<NoteRecordEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteRecordEntity)

    @Query("UPDATE notes SET isBookmarked = :bookmarked WHERE id = :noteId")
    suspend fun setBookmark(noteId: String, bookmarked: Boolean)

    // Jobs & Internships
    @Query("SELECT * FROM jobs WHERE instituteId = :instituteId OR instituteId = 'GLOBAL' OR instituteId = ''")
    fun getJobsForInstitute(instituteId: String): Flow<List<JobRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJobs(jobs: List<JobRecordEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: JobRecordEntity)

    // Applications
    @Query("SELECT * FROM applications WHERE studentId = :studentId ORDER BY appliedDate DESC")
    fun getApplicationsByStudent(studentId: String): Flow<List<ApplicationRecordEntity>>

    @Query("SELECT * FROM applications WHERE instituteId = :instituteId ORDER BY appliedDate DESC")
    fun getApplicationsByInstitute(instituteId: String): Flow<List<ApplicationRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(app: ApplicationRecordEntity)

    @Query("UPDATE applications SET status = :status WHERE id = :id")
    suspend fun updateApplicationStatus(id: String, status: String)

    // Resumes
    @Query("SELECT * FROM resumes WHERE studentId = :studentId")
    fun getResumesByStudent(studentId: String): Flow<List<ResumeRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResume(resume: ResumeRecordEntity)

    // Test Results
    @Query("SELECT * FROM test_results WHERE studentId = :studentId ORDER BY completedDate DESC")
    fun getTestResultsByStudent(studentId: String): Flow<List<TestResultRecordEntity>>

    @Query("SELECT * FROM test_results ORDER BY completedDate DESC")
    fun getAllTestResults(): Flow<List<TestResultRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestResult(result: TestResultRecordEntity)
}
