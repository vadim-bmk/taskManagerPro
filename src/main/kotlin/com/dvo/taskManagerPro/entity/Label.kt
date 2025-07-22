package com.dvo.taskManagerPro.entity

import jakarta.persistence.*

@Entity(name = "labels")
@Table(name = "labels")
data class Label(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    var name: String,

    @ManyToMany(mappedBy = "labels")
    var tasks: MutableList<Task>?
)
