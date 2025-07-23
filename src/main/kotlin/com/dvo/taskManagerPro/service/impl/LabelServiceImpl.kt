package com.dvo.taskManagerPro.service.impl

import com.dvo.taskManagerPro.entity.Label
import com.dvo.taskManagerPro.exception.EntityExistsException
import com.dvo.taskManagerPro.exception.EntityNotFoundException
import com.dvo.taskManagerPro.mapper.LabelMapper
import com.dvo.taskManagerPro.repository.LabelRepository
import com.dvo.taskManagerPro.service.LabelService
import com.dvo.taskManagerPro.web.model.request.UpsertLabelRequest
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class LabelServiceImpl(
    private val labelRepository: LabelRepository,
    private val labelMapper: LabelMapper
) : LabelService {
    private val log = LoggerFactory.getLogger(LabelServiceImpl::class.java)

    override fun findAll(pageable: Pageable): Page<Label> {
        log.info("Call findAll in LabelServiceImpl with pageable: {}", pageable)

        return labelRepository.findAll(pageable)
    }

    override fun findById(id: Long): Label {
        log.info("Call findById in LabelServiceImpl with ID: {}", id)

        return labelRepository.findById(id)
            .orElseThrow {
                EntityNotFoundException("Label with ID: $id not found")
            }
    }

    @Transactional
    override fun create(label: Label): Label {
        log.info("Call create in LabelServiceImpl with label: {}", label)

        if (labelRepository.existsByName(label.name)) {
            throw EntityExistsException("Label with name: ${label.name} already exists")
        }

        return labelRepository.save(label)
    }

    @Transactional
    override fun update(label: UpsertLabelRequest, id: Long): Label {
        log.info("Call update in LabelServiceImpl with ID: {} and label: {}", id, label)

        val existedLabel = labelRepository.findById(id)
            .orElseThrow {
                EntityNotFoundException("Label with ID: $id not found")
            }

        if (existedLabel.name != label.name && labelRepository.existsByName(label.name)) {
            throw EntityExistsException("Label with name: ${label.name} already exists")
        }

        labelMapper.updateRequestToLabel(label, existedLabel)

        return labelRepository.save(existedLabel)
    }

    @Transactional
    override fun deleteById(id: Long) {
        log.info("Call deleteById in LabelServiceImpl with ID: {}", id)

        labelRepository.deleteById(id)
    }

    override fun findLabelsByTaskId(taskId: Long): List<Label> {
        log.info("Call findLabelsByTaskId in LabelServiceImpl with taskId: {}", taskId)

        return labelRepository.findAllByTasks_Id(taskId)
    }


}