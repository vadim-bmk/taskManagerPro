package com.dvo.taskManagerPro.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "projects")
@Table(name = "projects")
data class Project(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    var name: String,
    var description: String?,

    @Column(name = "created_at")
    var createdAt: LocalDateTime,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_project",
        joinColumns = [JoinColumn(name = "project_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    var users: MutableList<User> = mutableListOf(),

    @OneToMany(mappedBy = "project", cascade = [CascadeType.ALL], orphanRemoval = true)
    val tasks: List<Task>
)
