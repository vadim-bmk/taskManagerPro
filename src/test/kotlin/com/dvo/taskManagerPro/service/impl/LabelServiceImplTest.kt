package com.dvo.taskManagerPro.service.impl

import com.dvo.taskManagerPro.entity.Label
import com.dvo.taskManagerPro.exception.EntityExistsException
import com.dvo.taskManagerPro.mapper.LabelMapper
import com.dvo.taskManagerPro.repository.LabelRepository
import com.dvo.taskManagerPro.web.model.request.UpsertLabelRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*
import kotlin.test.assertEquals

class LabelServiceImplTest {
    private lateinit var labelService: LabelServiceImpl
    private val labelRepository: LabelRepository = mock()
    private val labelMapper: LabelMapper = mock()

    private lateinit var label: Label

    @BeforeEach
    fun setUp() {
        labelService = LabelServiceImpl(labelRepository, labelMapper)

        label = Label(
            id = 1L,
            name = "label",
            tasks = mutableListOf()
        )
    }

    @Test
    fun `test findAll`() {
        val labelList = listOf(label)
        val page = PageImpl(labelList, PageRequest.of(0, 10), labelList.size.toLong())
        whenever(labelRepository.findAll(PageRequest.of(0, 10))).thenReturn(page)

        val result = labelService.findAll(PageRequest.of(0, 10))

        assertEquals(1, result.content.size)
        verify(labelRepository).findAll(PageRequest.of(0, 10))
    }

    @Test
    fun `test findById`() {
        whenever(labelRepository.findById(1L)).thenReturn(Optional.of(label))

        val result = labelService.findById(1L)

        assertEquals(label, result)
        verify(labelRepository).findById(1L)
    }

    @Test
    fun `test create`() {
        whenever(labelRepository.existsByName("label")).thenReturn(false)
        whenever(labelRepository.save(label)).thenReturn(label)

        val result = labelService.create(label)

        assertEquals(label, result)
        verify(labelRepository).existsByName("label")
        verify(labelRepository).save(label)
    }

    @Test
    fun `test create when label name already exists `() {
        whenever(labelRepository.existsByName("label")).thenReturn(true)

        assertThrows<EntityExistsException> {
            labelService.create(label)
        }
    }

    @Test
    fun `test update`() {
        val request = UpsertLabelRequest(
            name = "new label"
        )
        whenever(labelRepository.findById(1L)).thenReturn(Optional.of(label))
        whenever(labelRepository.existsByName("new label")).thenReturn(false)
        whenever(labelRepository.save(label)).thenReturn(label)

        val result = labelService.update(request, 1L)

        assertEquals(label, result)
    }

    @Test
    fun `test deleteById`() {
        labelService.deleteById(1L)

        verify(labelRepository).deleteById(1L)
    }

    @Test
    fun `test findLabelsByTaskId`() {
        whenever(labelRepository.findAllByTasks_Id(1L)).thenReturn(listOf(label))

        val result = labelService.findLabelsByTaskId(1L)

        assertEquals(1, result.size)
        verify(labelRepository).findAllByTasks_Id(1L)
    }
}