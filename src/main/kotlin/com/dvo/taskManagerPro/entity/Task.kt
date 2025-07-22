package com.dvo.taskManagerPro.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(name = "tasks")
@Table(name = "tasks")
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    var title: String,
    var description: String? = null,
    @Column(nullable = false, name = "created_at")
    val createdAt: LocalDateTime,

    @Column(name = "due_date")
    var dueDate: LocalDate,

    @Enumerated(EnumType.STRING)
    var status: TaskStatus,

    @Enumerated(EnumType.STRING)
    var priority: TaskPriority,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var assignedTo: User?,

    @ManyToOne(fetch = FetchType.LAZY)
    var project: Project,

    @OneToMany(mappedBy = "task", cascade = [CascadeType.ALL], orphanRemoval = true)
    val comments: List<Comment>?,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "task_labels",
        joinColumns = [JoinColumn(name = "task_id")],
        inverseJoinColumns = [JoinColumn(name = "label_id")]
    )
    var labels: MutableList<Label> = mutableListOf()
)
