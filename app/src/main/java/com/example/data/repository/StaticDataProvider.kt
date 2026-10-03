package com.example.data.repository

import com.example.data.model.*

object StaticDataProvider {

    fun getCodingProblems(): List<CodingProblem> = listOf(
        CodingProblem(
            id = "prob_two_sum",
            title = "Two Sum",
            difficulty = "Easy",
            category = "Arrays & Hashing",
            statement = "Given an array of integers `nums` and an integer `target`, return indices of the two numbers such that they add up to `target`. You may assume that each input would have exactly one solution, and you may not use the same element twice.",
            inputDescription = "nums = [2,7,11,15], target = 9",
            outputDescription = "[0,1]",
            exampleInput = "[2, 7, 11, 15], 9",
            exampleOutput = "[0, 1] (because nums[0] + nums[1] == 9)",
            constraints = "2 <= nums.length <= 10^4\n-10^9 <= nums[i] <= 10^9\nOnly one valid answer exists.",
            initialCode = mapOf(
                "Kotlin" to """class Solution {
    fun twoSum(nums: IntArray, target: Int): IntArray {
        val map = HashMap<Int, Int>()
        for (i in nums.indices) {
            val complement = target - nums[i]
            if (map.containsKey(complement)) {
                return intArrayOf(map[complement]!!, i)
            }
            map[nums[i]] = i
        }
        return intArrayOf()
    }
}""",
                "Java" to """class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[] { map.get(complement), i };
            }
            map.put(nums[i], i);
        }
        return new int[]{};
    }
}""",
                "Python" to """class Solution:
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        seen = {}
        for i, n in enumerate(nums):
            diff = target - n
            if diff in seen:
                return [seen[diff], i]
            seen[n] = i
        return []""",
                "C++" to """class Solution {
public:
    vector<int> twoSum(vector<int>& nums, int target) {
        unordered_map<int, int> mp;
        for (int i = 0; i < nums.size(); i++) {
            int comp = target - nums[i];
            if (mp.count(comp)) return {mp[comp], i};
            mp[nums[i]] = i;
        }
        return {};
    }
};"""
            )
        ),
        CodingProblem(
            id = "prob_valid_parens",
            title = "Valid Parentheses",
            difficulty = "Easy",
            category = "Stack",
            statement = "Given a string `s` containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.\nAn input string is valid if open brackets are closed by the same type of brackets in the correct order.",
            inputDescription = "s = '()[]{}'",
            outputDescription = "true",
            exampleInput = "\"()[]{}\"",
            exampleOutput = "true",
            constraints = "1 <= s.length <= 10^4\ns consists of parentheses only '()[]{}'.",
            initialCode = mapOf(
                "Kotlin" to """class Solution {
    fun isValid(s: String): Boolean {
        val stack = ArrayDeque<Char>()
        for (c in s) {
            when (c) {
                '(', '{', '[' -> stack.addFirst(c)
                ')' -> if (stack.isEmpty() || stack.removeFirst() != '(') return false
                '}' -> if (stack.isEmpty() || stack.removeFirst() != '{') return false
                ']' -> if (stack.isEmpty() || stack.removeFirst() != '[') return false
            }
        }
        return stack.isEmpty()
    }
}""",
                "Java" to """class Solution {
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (c == '(') stack.push(')');
            else if (c == '{') stack.push('}');
            else if (c == '[') stack.push(']');
            else if (stack.isEmpty() || stack.pop() != c) return false;
        }
        return stack.isEmpty();
    }
}""",
                "Python" to """class Solution:
    def isValid(self, s: str) -> bool:
        stack = []
        mapping = {")": "(", "}": "{", "]": "["}
        for char in s:
            if char in mapping:
                top = stack.pop() if stack else '#'
                if mapping[char] != top:
                    return False
            else:
                stack.append(char)
        return not stack"""
            )
        ),
        CodingProblem(
            id = "prob_reverse_ll",
            title = "Reverse Linked List",
            difficulty = "Easy",
            category = "Linked Lists",
            statement = "Given the head of a singly linked list, reverse the list, and return the reversed list.\nCould you implement it both iteratively and recursively?",
            inputDescription = "head = [1,2,3,4,5]",
            outputDescription = "[5,4,3,2,1]",
            exampleInput = "[1,2,3,4,5]",
            exampleOutput = "[5,4,3,2,1]",
            constraints = "Number of nodes is [0, 5000].\n-5000 <= Node.val <= 5000",
            initialCode = mapOf(
                "Kotlin" to """/**
 * Example:
 * var li = ListNode(5)
 * var v = li.`val`
 * Definition for singly-linked list.
 * class ListNode(var `val`: Int) {
 *     var next: ListNode? = null
 * }
 */
class Solution {
    fun reverseList(head: ListNode?): ListNode? {
        var prev: ListNode? = null
        var curr = head
        while (curr != null) {
            val nextNode = curr.next
            curr.next = prev
            prev = curr
            curr = nextNode
        }
        return prev
    }
}""",
                "Java" to """class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode nextTemp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nextTemp;
        }
        return prev;
    }
}"""
            )
        ),
        CodingProblem(
            id = "prob_longest_substr",
            title = "Longest Substring Without Repeating Characters",
            difficulty = "Medium",
            category = "Sliding Window",
            statement = "Given a string `s`, find the length of the longest substring without duplicate characters.",
            inputDescription = "s = 'abcabcbb'",
            outputDescription = "3 (the answer is 'abc')",
            exampleInput = "\"abcabcbb\"",
            exampleOutput = "3",
            constraints = "0 <= s.length <= 5 * 10^4\ns consists of English letters, digits, symbols and spaces.",
            initialCode = mapOf(
                "Kotlin" to """class Solution {
    fun lengthOfLongestSubstring(s: String): Int {
        var maxLen = 0
        var left = 0
        val seen = HashMap<Char, Int>()
        for (right in s.indices) {
            val c = s[right]
            if (seen.containsKey(c)) {
                left = maxOf(left, seen[c]!! + 1)
            }
            seen[c] = right
            maxLen = maxOf(maxLen, right - left + 1)
        }
        return maxLen
    }
}""",
                "Python" to """class Solution:
    def lengthOfLongestSubstring(self, s: str) -> int:
        char_map = {}
        left = 0
        max_len = 0
        for right, char in enumerate(s):
            if char in char_map:
                left = max(left, char_map[char] + 1)
            char_map[char] = right
            max_len = max(max_len, right - left + 1)
        return max_len"""
            )
        )
    )

    fun getAssessmentTests(): List<AssessmentTest> = listOf(
        AssessmentTest(
            id = "test_cse_core",
            instituteId = "iitd",
            title = "CSE Core Engineering Diagnostic",
            category = TestCategory.TECHNICAL,
            durationMinutes = 15,
            totalQuestions = 6,
            passingScore = 60,
            difficulty = "Intermediate",
            questions = listOf(
                AssessmentQuestion(
                    id = "q1",
                    questionText = "Which data structure is primarily used to implement LRU (Least Recently Used) cache with O(1) get and put operations?",
                    options = listOf("Hash Map with Doubly Linked List", "Binary Search Tree", "Array with Min Heap", "Single Queue"),
                    correctOptionIndex = 0,
                    explanation = "A Hash Map provides O(1) key lookup, while a Doubly Linked List enables O(1) removal and re-insertion at the head/tail for recency.",
                    topic = "Data Structures"
                ),
                AssessmentQuestion(
                    id = "q2",
                    questionText = "What is the worst-case time complexity of searching in a properly balanced Red-Black Tree?",
                    options = listOf("O(1)", "O(log N)", "O(N)", "O(N log N)"),
                    correctOptionIndex = 1,
                    explanation = "Red-Black trees ensure the maximum depth is no more than 2 * log2(N + 1), guaranteeing O(log N) lookup.",
                    topic = "Data Structures"
                ),
                AssessmentQuestion(
                    id = "q3",
                    questionText = "In Relational DBMS, which normal form eliminates transitive dependency?",
                    options = listOf("1NF (First Normal Form)", "2NF (Second Normal Form)", "3NF (Third Normal Form)", "BCNF"),
                    correctOptionIndex = 2,
                    explanation = "3NF eliminates transitive dependencies (where a non-prime attribute depends on another non-prime attribute).",
                    topic = "DBMS"
                ),
                AssessmentQuestion(
                    id = "q4",
                    questionText = "What mechanism in modern OS avoids priority inversion when a low-priority thread holds a lock needed by a high-priority thread?",
                    options = listOf("Priority Ceiling / Priority Inheritance", "Round Robin Scheduling", "Multi-Level Feedback Queues", "Aging"),
                    correctOptionIndex = 0,
                    explanation = "Priority Inheritance temporarily raises the priority of the lock-holder to match that of the waiting high-priority thread.",
                    topic = "Operating Systems"
                ),
                AssessmentQuestion(
                    id = "q5",
                    questionText = "In TCP/IP networking, what does SYN-ACK represent during connection establishment?",
                    options = listOf("Step 1 from client", "Step 2 server acknowledgment & synchronization", "Step 3 final handshake confirmation", "Connection tear-down"),
                    correctOptionIndex = 1,
                    explanation = "SYN-ACK is sent by the receiving server to acknowledge the client's SYN and synchronize its own initial sequence number.",
                    topic = "Computer Networks"
                ),
                AssessmentQuestion(
                    id = "q6",
                    questionText = "What is the result of evaluating this in Java/Kotlin: `(14 >> 2) + (1 << 3)`?",
                    options = listOf("11", "12", "14", "8"),
                    correctOptionIndex = 0,
                    explanation = "14 >> 2 is 3 (integer division by 4). 1 << 3 is 8. 3 + 8 = 11.",
                    topic = "Programming"
                )
            )
        ),
        AssessmentTest(
            id = "test_aptitude",
            instituteId = "iitd",
            title = "Campus Placement Aptitude Benchmark",
            category = TestCategory.APTITUDE,
            durationMinutes = 12,
            totalQuestions = 5,
            passingScore = 70,
            difficulty = "Easy - Medium",
            questions = listOf(
                AssessmentQuestion(
                    id = "aq1",
                    questionText = "A train running at 72 km/h crosses a 200m platform in 22 seconds. What is the length of the train?",
                    options = listOf("240 m", "200 m", "180 m", "220 m"),
                    correctOptionIndex = 0,
                    explanation = "Speed = 72 * (5/18) = 20 m/s. Distance = Speed * Time = 20 * 22 = 440m. Train length = 440 - 200 = 240m.",
                    topic = "Speed, Time & Distance"
                ),
                AssessmentQuestion(
                    id = "aq2",
                    questionText = "If A can complete a project in 12 days and B in 24 days, how many days will they take together?",
                    options = listOf("8 days", "6 days", "10 days", "7.5 days"),
                    correctOptionIndex = 0,
                    explanation = "1/12 + 1/24 = 3/24 = 1/8. Together they finish in 8 days.",
                    topic = "Time & Work"
                ),
                AssessmentQuestion(
                    id = "aq3",
                    questionText = "Find the missing number in the sequence: 4, 9, 25, 49, 121, ?",
                    options = listOf("144", "169", "196", "225"),
                    correctOptionIndex = 1,
                    explanation = "These are squares of consecutive prime numbers: 2^2, 3^2, 5^2, 7^2, 11^2, 13^2 = 169.",
                    topic = "Number Series"
                ),
                AssessmentQuestion(
                    id = "aq4",
                    questionText = "In a code language, SYSTEM is written as SYSMET. How is FRACTION written?",
                    options = listOf("FRACNOIT", "CARFNOIT", "FRANTOIC", "CARFNOIT"),
                    correctOptionIndex = 0,
                    explanation = "The first half (FRAC) is kept or transformed in chunks; here SYSTEM (SYS-TEM -> SYS-MET) reverses the second half: FRACTION -> FRAC-NOIT.",
                    topic = "Coding-Decoding"
                ),
                AssessmentQuestion(
                    id = "aq5",
                    questionText = "A shopkeeper marks an article 30% above cost and offers a 10% discount. What is the actual profit percentage?",
                    options = listOf("17%", "20%", "15%", "18%"),
                    correctOptionIndex = 0,
                    explanation = "Cost = 100, Marked = 130, Selling = 130 * 0.9 = 117. Profit = 17%.",
                    topic = "Profit & Loss"
                )
            )
        )
    )

    fun getCareerRoadmap(role: String): List<CareerRoadmapNode> = when {
        role.contains("AI", ignoreCase = true) || role.contains("Machine", ignoreCase = true) -> listOf(
            CareerRoadmapNode("ai_1", "Beginner", "Python & Math Fundamentals", "Linear algebra, multivariate calculus, probability distributions, NumPy, and Pandas manipulation.", listOf("Python", "NumPy", "Pandas", "Linear Algebra"), 100, isCompleted = true),
            CareerRoadmapNode("ai_2", "Fundamentals", "Classic Machine Learning", "Scikit-Learn, regression, decision trees, random forests, clustering, gradient boosting (XGBoost).", listOf("Scikit-Learn", "Feature Engineering", "Model Evaluation"), 90, isCompleted = true),
            CareerRoadmapNode("ai_3", "Intermediate", "Deep Learning & PyTorch", "Neural network backpropagation, CNNs for computer vision, RNNs, LSTMs, and PyTorch tensors.", listOf("PyTorch", "CNNs", "Optimization", "Tensors"), 65, isCompleted = false, isCurrent = true),
            CareerRoadmapNode("ai_4", "Projects", "End-to-End LLM & RAG Application", "Vector databases (Chroma/Pinecone), LangChain, retrieval augmented generation, and embeddings.", listOf("Vector DBs", "RAG", "Embeddings", "FastAPI"), 20, isCompleted = false),
            CareerRoadmapNode("ai_5", "Advanced", "Model Fine-Tuning & Distillation", "LoRA/QLoRA parameter-efficient fine tuning, quantization with ONNX/GGUF, and agentic workflows.", listOf("LoRA", "Quantization", "vLLM", "Agentic Systems"), 0, isCompleted = false),
            CareerRoadmapNode("ai_6", "Interview Prep", "ML System Design & Coding", "Designing recommendation feeds, latency optimization, bias-variance tradeoffs, LeetCode ML algorithms.", listOf("ML System Design", "Algorithms", "Metrics"), 0, isCompleted = false),
            CareerRoadmapNode("ai_7", "Job Ready", "Production MLOps & Portfolio", "MLflow tracking, Dockerized deployment on Kubernetes, GitHub portfolio with live model demos.", listOf("MLflow", "Docker", "CI/CD", "Portfolio"), 0, isCompleted = false)
        )
        else -> listOf(
            CareerRoadmapNode("fs_1", "Beginner", "Computer Science & Programming Basics", "Data structures, OOPs principles in Java/Kotlin/Python, memory management, and Git version control.", listOf("Data Structures", "Algorithms", "Git", "OOPs"), 100, isCompleted = true),
            CareerRoadmapNode("fs_2", "Fundamentals", "Web & Mobile Core Technologies", "Modern JavaScript/TypeScript, React state model, Jetpack Compose layouts, HTML5/CSS3 semantics.", listOf("TypeScript", "React", "Jetpack Compose", "Tailwind"), 85, isCompleted = true),
            CareerRoadmapNode("fs_3", "Intermediate", "Backend Architecture & Databases", "RESTful API design, Node.js/Spring Boot, PostgreSQL, indexing, transaction ACID, and Room ORM.", listOf("Node.js", "PostgreSQL", "Room DB", "REST APIs"), 70, isCompleted = false, isCurrent = true),
            CareerRoadmapNode("fs_4", "Projects", "Full Stack Production Projects", "Develop multi-tenant SaaS or student hub with authentication, caching (Redis), and responsive UI.", listOf("Full Stack", "JWT Auth", "Redis", "State Management"), 40, isCompleted = false),
            CareerRoadmapNode("fs_5", "Advanced", "Distributed Systems & Cloud", "Microservices communication, Docker containerization, AWS S3/EC2, message queues (Kafka/RabbitMQ).", listOf("Docker", "AWS", "Kafka", "CI/CD"), 15, isCompleted = false),
            CareerRoadmapNode("fs_6", "Interview Prep", "DSA Mastery & System Design", "Graph algorithms, dynamic programming, scaling web systems, caching strategies, mock interviews.", listOf("LeetCode DSA", "System Design", "Behavioral"), 20, isCompleted = false),
            CareerRoadmapNode("fs_7", "Job Ready", "ATS Resume & Campus Drives", "Verified technical portfolio, ATS-optimized resume, campus interview practice, and offer negotiation.", listOf("ATS Resume", "Live Demos", "Job Applications"), 10, isCompleted = false)
        )
    }

    fun getStudentProjects(): List<StudentProject> = listOf(
        StudentProject(
            id = "p1",
            title = "SKILLSZ Student Career Platform",
            description = "All-in-one multi-tenant Android platform for technical institutes offering learning materials, resume builder, AI career coach, and application tracking.",
            techStack = listOf("Kotlin", "Jetpack Compose", "Room", "Coroutines", "Material 3"),
            githubUrl = "https://github.com/aryansharma-dev/skillsz-android",
            liveDemoUrl = "https://skillsz.app",
            stars = 28,
            category = "Mobile & Full Stack"
        ),
        StudentProject(
            id = "p2",
            title = "Distributed High-Throughput Rate Limiter",
            description = "Token-bucket rate limiter service handling 25k requests/sec built with Go, Redis cluster, and Docker with sub-millisecond p99 latency.",
            techStack = listOf("Go", "Redis", "Docker", "Prometheus"),
            githubUrl = "https://github.com/aryansharma-dev/redis-rate-limiter",
            liveDemoUrl = "https://demo.ratelimit.dev",
            stars = 42,
            category = "Backend & Systems"
        ),
        StudentProject(
            id = "p3",
            title = "AI Code Reviewer & AST Analyzer",
            description = "Static analysis tool combined with LLM semantic code review to spot memory leaks, anti-patterns, and security vulnerabilities in pull requests.",
            techStack = listOf("Python", "FastAPI", "Tree-sitter", "Docker"),
            githubUrl = "https://github.com/aryansharma-dev/ai-code-auditor",
            liveDemoUrl = "https://auditor.aryansharma.dev",
            stars = 19,
            category = "AI / Developer Tools"
        )
    )

    fun getMockInterviewQuestions(category: String): List<String> = when (category) {
        "Technical" -> listOf(
            "Can you explain the internal working of a HashMap and how hash collisions are resolved in Java 8+?",
            "What is the difference between TCP and UDP, and in what scenarios would you choose UDP over TCP?",
            "Explain Database Indexing. Why does a B+ Tree outperform a Binary Search Tree for disk storage?",
            "What are Coroutines in Kotlin, and how do structured concurrency and dispatchers prevent memory leaks?"
        )
        "Coding" -> listOf(
            "How would you detect a cycle in a singly linked list in O(N) time and O(1) space?",
            "Explain how the Sliding Window algorithm reduces time complexity from O(N^2) to O(N) in substring problems.",
            "Design an algorithm to find the median of a stream of integers in real-time."
        )
        "Behavioral" -> listOf(
            "Tell me about a challenging technical project you built. What was the toughest roadblock and how did you resolve it?",
            "Describe a situation where you had a disagreement with a team member about architecture or code style. How did you handle it?",
            "Where do you see yourself in 3 years in terms of engineering depth and technical leadership?"
        )
        else -> listOf(
            "Tell me about yourself, your educational background, and why you chose Computer Science Engineering.",
            "What motivates you to apply for our campus placement drive?",
            "How do you prioritize learning new frameworks while maintaining high academic performance?"
        )
    }

    fun generateAIResponse(prompt: String, student: CurrentUser): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("resume") -> """### 📄 Resume Optimization for ${student.careerGoal}

Based on your profile as a **${student.year} ${student.branch}** student targeting **${student.careerGoal}**:

**1. Quantify Project Impact**
Instead of *"Built an Android app"*, write:
> *"Architected an offline-first Android application using Jetpack Compose and Room DB, achieving 45% faster local query performance and supporting 4,000+ active institute users."*

**2. Key Technical Keywords to Include:**
- Core: `Kotlin`, `Coroutines`, `Jetpack Compose`, `Room ORM`, `Clean Architecture (MVVM)`
- Backend / Systems: `REST APIs`, `PostgreSQL`, `Docker`, `Git CI/CD`

**3. Action Item:**
Head to the **Resume Builder** tab and tap **"Generate Resume with AI"** to apply these bullet points to your live draft!"""

            lower.contains("skill gap") || lower.contains("gap") -> """### 🎯 Skill Gap Analysis for ${student.name}

Target Goal: **${student.careerGoal}**

**Strengths:**
✅ Strong foundation in Kotlin, Java, and Data Structures
✅ Good academic standing (CGPA: ${student.gpa}/10.0)

**Identified Gaps for Top Tier Hiring:**
1. **System Design Fundamentals:** Practice designing rate limiters, URL shorteners, and cache invalidation.
2. **Containerization & CI/CD:** Add Docker and GitHub Actions workflows to your pinned projects.
3. **Advanced Concurrency:** Study Kotlin Flow backpressure, shared flows, and state flows under high contention.

**Recommended Step:**
Check out the **Career Roadmap** tab to track your progress through Intermediate & Advanced milestones."""

            lower.contains("interview") -> """### 🎙️ Interview Preparation Blueprint

For technical rounds in **${student.branch}**:

**1. Data Structures & Algorithms (DSA):**
- Practice Top 50 LeetCode patterns (Sliding Window, Two Pointers, BFS/DFS, Top 'K' Elements).
- Always talk through your thought process before writing code!

**2. CS Fundamentals Checklist:**
- **OS:** Threads vs Processes, Mutex vs Semaphore, Virtual Memory.
- **DBMS:** ACID properties, Indexing (B+ Trees), Normalization (1NF to 3NF).
- **Networks:** TCP 3-way handshake, DNS resolution path, HTTPS TLS handshake.

**3. Mock Interview Practice:**
Tap the **Mock Interview** button in the AI menu to test yourself with live feedback on technical accuracy and communication!"""

            lower.contains("study plan") || lower.contains("plan") -> """### 📅 4-Week Technical Study Plan

**Week 1: Core Algorithms & Problem Solving**
- Solve 3 Medium problems daily in the **Coding Arena** (Arrays, Strings, HashMaps).
- Review time & space complexities.

**Week 2: Framework & System Architecture**
- Deep dive into Kotlin Coroutines, Flow, and Jetpack Compose lifecycle.
- Study Unit 3 & 4 of Operating Systems in the **Notes** section.

**Week 3: Portfolio Project Polish**
- Add clean README, architectural diagram, and Dockerfile to your GitHub repo.
- Run your resume through the **Resume Analyzer** (aim for 85+ ATS score).

**Week 4: Mock Tests & Campus Drives**
- Complete 2 timed assessments in the **Tests** section.
- Apply to 3 active campus placement drives in the **Jobs** tab."""

            lower.contains("shortlist") || lower.contains("rejected") -> """### 🔍 Why You Might Not Be Getting Shortlisted & How to Fix It

Based on campus recruiter screening patterns for **${student.branch}**:

**1. The 6-Second ATS Filter:**
- **Problem:** Many applicants list generic summaries without numbers.
- **Fix:** Include metrics on your top 2 projects (e.g. *"reduced API latency by 35%"*, *"processed 10,000 requests/min"*).

**2. Missing Key Industry Stack:**
- For **${student.careerGoal}**, ensure you explicitly list:
  `REST APIs`, `Docker`, `Git`, `PostgreSQL/SQL`, `Unit Testing`.

**3. GitHub & Live Demos:**
- Recruiters skip resumes where links are broken or repos have only README placeholders. Pin your top 2 active repositories with deployed links.

**4. Action Step:**
Use the **ATS Analyzer** in the Resume tab to check your keyword match percentage against real job descriptions!"""

            lower.contains("what skill") || lower.contains("skills to learn") || lower.contains("software development") -> """### 🚀 Essential Skills for Software Development (${student.careerGoal})

Here is the industry-standard competency matrix for **2026 technical hiring**:

**1. Core Computer Science Fundamentals:**
- **DSA:** Arrays, Hash Tables, Trees, Graphs, Dynamic Programming (Solve 150+ problems).
- **Core Systems:** OS (Concurrency & Memory), DBMS (ACID, B+ Trees), Computer Networks (TCP/IP, HTTP/3).

**2. Modern Architecture & Frameworks:**
- **Mobile/Front-End:** Kotlin, Jetpack Compose, React, TypeScript.
- **Backend/Cloud:** Node.js / Java Spring Boot, PostgreSQL, Docker, Redis.
- **APIs:** RESTful endpoints, WebSockets, gRPC.

**3. Development Practices:**
- Git branch management, Docker containerization, CI/CD with GitHub Actions.

**Next Step:** Head to the **Career Roadmap** tab to track your completion of each phase!"""

            lower.contains("java") -> """### ☕ Java & JVM Interview Mastery Guide

Key questions asked in Tier-1 technical rounds:

**1. JVM Internal Architecture:**
- **Memory Areas:** Heap (Young/Old gen), Metaspace, Stack frames.
- **Garbage Collection:** G1GC vs ZGC phases, Stop-the-world pauses.

**2. Collections & Concurrency:**
- **HashMap:** How hash collisions convert from LinkedList to Red-Black Tree when bucket size >= 8.
- **ConcurrentHashMap:** Segment locking (Java 7) vs CAS with synchronized bin nodes (Java 8+).
- **Volatile vs Synchronized:** Memory visibility, instruction reordering, and happens-before guarantees.

**3. Java 17/21 Modern Features:**
- Virtual Threads (Project Loom), Record classes, Pattern matching for switch.

**Practice:** You can test these concepts right now in the **Assessments** tab!"""

            lower.contains("explain") || lower.contains("topic") || lower.contains("dbms") || lower.contains("os") -> """### 📚 Concept Deep Dive: ${prompt.replace("explain", "", true).replace("this topic", "", true).trim().capitalize()}

Here is the core technical explanation:

**1. Architectural Concept:**
- In technical computing, efficient resource allocation is driven by space-time tradeoffs.
- For disk and database systems, **B+ Trees** store keys in internal nodes while leaves are doubly-linked for fast range queries.
- In operating systems, **Virtual Memory** abstracts physical RAM using Translation Lookaside Buffers (TLB) and page tables with LRU eviction.

**2. Industry Application:**
- Production databases (PostgreSQL, MySQL InnoDB) rely on WAL (Write-Ahead Logging) to guarantee ACID Durability during power loss.

**3. Exam / Interview Tip:**
- Always draw the state diagram (e.g. 3-way TCP handshake or process life cycle) when answering university or technical interview questions!"""

            lower.contains("hi") || lower.contains("hello") || lower.contains("hey") -> """Hello **${student.name}**! 👋 

I'm **SKILLSZ AI**, your engineering career and academic coach.

Here are quick actions you can try:
- **"Improve my resume"** — Generate ATS bullets with metrics
- **"Why am I not getting shortlisted?"** — Recruiter screening checklist
- **"What skills should I learn for software development?"** — Career matrix
- **"Prepare for interview"** — Question breakdowns & mock rounds
- **"Explain [any topic]"** — Academic syllabus revision

How can I help you today?"""

            lower.contains("project") -> """### 💡 Recommended Projects for ${student.careerGoal}

Here are 3 high-impact project ideas tailored for your background:

1. **Enterprise Distributed Rate Limiter & Token Bucket Gateway**
   - *Stack:* Kotlin / Java, Redis, Docker, Spring Boot
   - *Why recruiters love it:* Demonstrates deep concurrency, low latency, and distributed systems understanding.

2. **Autonomous Multi-Agent Task Orchestrator**
   - *Stack:* Python, FastAPI, Vector DB, LLM Function Calling
   - *Why recruiters love it:* Shows you can build production AI workflows beyond simple chat wrappers.

3. **Offline-First Collaborative Canvas or Markdown Hub**
   - *Stack:* Jetpack Compose, WebSockets, Room, CRDTs (Conflict-free replicated data)
   - *Why recruiters love it:* High UI complexity and advanced state synchronization."""

            lower.contains("internship") || lower.contains("apply") -> """### 💼 Internship Advice for 3rd Year Students

1. **Target Companies Currently Hiring:**
   - Check the **Internships** tab: Razorpay, Microsoft Research, and CRED have active listings for 3rd year engineering students.
2. **Campus Placement Advantage:**
   - Your institute (${student.course} ${student.branch}) has 2 exclusive placement drives open now with Google and PhonePe.
3. **Application Tip:**
   - Tailor your resume summary for each role. For backend roles, emphasize REST APIs and SQL; for Android roles, emphasize Jetpack Compose and Coroutines."""

            else -> """Hello **${student.name}**! I am **SKILLSZ AI**, your personalized career & academic mentor.

I have full visibility into your profile:
- **Institute:** Indian Institute of Technology Delhi
- **Academic:** ${student.course} ${student.branch} (${student.year})
- **Goal:** ${student.careerGoal}
- **Current CGPA:** ${student.gpa}

I can help you:
- **Analyze and optimize your resume** for ATS algorithms
- **Identify technical skill gaps** and create daily study schedules
- **Practice mock interviews** with instant scoring
- **Find top job & internship opportunities** matching your skill set

What would you like to focus on right now?"""
        }
    }
}
