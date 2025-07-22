package com.dvo.taskManagerPro.entity

import jakarta.persistence.*

@Entity(name = "users")
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    var username: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var roleType: RoleType = RoleType.ROLE_EMPLOYEE,

    @Column(nullable = false)
    var password: String,

    @OneToMany(mappedBy = "assignedTo", cascade = [CascadeType.ALL], orphanRemoval = true)
    val tasks: List<Task>?,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_project",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "project_id")]
    )
    val projects: List<Project>?
)
