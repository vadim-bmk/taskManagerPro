package com.dvo.taskManagerPro.service.impl

import com.dvo.taskManagerPro.entity.Comment
import com.dvo.taskManagerPro.exception.EntityNotFoundException
import com.dvo.taskManagerPro.mapper.CommentMapper
import com.dvo.taskManagerPro.repository.CommentRepository
import com.dvo.taskManagerPro.repository.TaskRepository
import com.dvo.taskManagerPro.repository.UserRepository
import com.dvo.taskManagerPro.service.CommentService
import com.dvo.taskManagerPro.web.model.request.UpdateCommentRequest
import com.dvo.taskManagerPro.web.model.request.UpsertCommentRequest
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
    private val taskRepository: TaskRepository,
    private val commentMapper: CommentMapper
) : CommentService {
    private val log = LoggerFactory.getLogger(CommentServiceImpl::class.java)

    override fun findAll(pageable: Pageable): Page<Comment> {
        log.info("Call findAll in CommentServiceImpl with pageable: {}", pageable)

        return commentRepository.findAll(pageable)
    }

    override fun findById(id: Long): Comment {
        log.info("Call findById in CommentServiceImpl with ID: {}", id)

        return commentRepository.findById(id)
            .orElseThrow {
                EntityNotFoundException("Comment with ID: $id not found")
            }
    }

    @Transactional
    override fun create(comment: UpsertCommentRequest): Comment {
        log.info("Call create in CommentServiceImpl with comment: {}", comment)

        val user = userRepository.findByUsername(comment.username)
            .orElseThrow {
                EntityNotFoundException("User with username: ${comment.username} not found")
            }

        val task = taskRepository.findById(comment.taskId)
            .orElseThrow {
                EntityNotFoundException("Task with ID: ${comment.taskId} not found")
            }

        val newComment = commentMapper.requestToComment(comment, user, task)

        return commentRepository.save(newComment)
    }

    @Transactional
    override fun update(comment: UpdateCommentRequest, id: Long): Comment {
        log.info("Call update in CommentServiceImpl with ID: {} and comment: {}", id, comment)

        val user = comment.username?.let {
            userRepository.findByUsername(it)
                .orElseThrow {
                    EntityNotFoundException("User with username: ${comment.username} not found")
                }
        }

        val task = comment.taskId?.let {
            taskRepository.findById(it)
                .orElseThrow {
                    EntityNotFoundException("Task with ID: ${comment.taskId} not found")
                }
        }

        val existedComment = commentRepository.findById(id)
            .orElseThrow {
                EntityNotFoundException("Comment with ID: $id not found")
            }

        commentMapper.updateRequestToComment(comment, user, task, existedComment)

        return commentRepository.save(existedComment)
    }

    @Transactional
    override fun deleteById(id: Long) {
        log.info("Call deleteById in CommentServiceImpl with ID: {}", id)

        commentRepository.deleteById(id)
    }
}