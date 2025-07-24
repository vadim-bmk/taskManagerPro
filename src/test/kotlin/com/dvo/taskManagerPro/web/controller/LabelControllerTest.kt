package com.dvo.taskManagerPro.web.controller

import com.dvo.taskManagerPro.TaskManagerProApplication
import com.dvo.taskManagerPro.configuration.SecurityConfiguration
import com.dvo.taskManagerPro.entity.Label
import com.dvo.taskManagerPro.mapper.LabelMapper
import com.dvo.taskManagerPro.security.UserDetailsServiceImpl
import com.dvo.taskManagerPro.service.LabelService
import com.dvo.taskManagerPro.service.UserService
import com.dvo.taskManagerPro.web.model.request.PaginationRequest
import com.dvo.taskManagerPro.web.model.request.UpsertLabelRequest
import com.dvo.taskManagerPro.web.model.response.LabelResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(LabelController::class)
@Import(SecurityConfiguration::class, UserDetailsServiceImpl::class)
@ContextConfiguration(classes = [TaskManagerProApplication::class])
class LabelControllerTest {
    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var labelService: LabelService

    @MockBean
    private lateinit var labelMapper: LabelMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var label: Label
    private lateinit var labelResponse: LabelResponse
    private val url = "/api/labels"

    @BeforeEach
    fun setUp() {
        label = Label(
            id = 1L,
            name = "name",
            tasks = mutableListOf()
        )

        labelResponse = LabelResponse(
            id = 1L,
            name = "name",
            tasks = mutableListOf()
        )
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test findAll`() {
        val labels = listOf(label)
        val page = PageImpl(labels, PageRequest.of(0, 10), labels.size.toLong())
        val request = PaginationRequest(
            pageNumber = 0,
            pageSize = 10
        )
        whenever(labelService.findAll(PageRequest.of(0, 10))).thenReturn(page)
        whenever(labelMapper.labelToResponse(label)).thenReturn(labelResponse)

        mockMvc.perform(
            get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data[0].id").value("1"))

        verify(labelService).findAll(PageRequest.of(0, 10))
        verify(labelMapper).labelToResponse(label)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test findById`() {
        whenever(labelService.findById(1L)).thenReturn(label)
        whenever(labelMapper.labelToResponse(label)).thenReturn(labelResponse)

        mockMvc.perform(get("$url/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value("1"))

        verify(labelService).findById(1L)
        verify(labelMapper).labelToResponse(label)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test create`() {
        val request = UpsertLabelRequest(
            name = "name"
        )

        whenever(labelMapper.requestToLabel(request)).thenReturn(label)
        whenever(labelService.create(label)).thenReturn(label)
        whenever(labelMapper.labelToResponse(label)).thenReturn(labelResponse)

        mockMvc.perform(
            post("$url/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value("1"))

        verify(labelMapper).requestToLabel(request)
        verify(labelService).create(label)
        verify(labelMapper).labelToResponse(label)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test update`() {
        val request = UpsertLabelRequest(name = "name")
        whenever(labelService.update(request, 1L)).thenReturn(label)
        whenever(labelMapper.labelToResponse(label)).thenReturn(labelResponse)

        mockMvc.perform(
            put("$url/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value("1"))

        verify(labelService).update(request, 1L)
        verify(labelMapper).labelToResponse(label)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test deleteById`() {
        doNothing().whenever(labelService).deleteById(1L)

        mockMvc.perform(delete("$url/1"))
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    fun `test findLabelsByTaskId`() {
        whenever(labelService.findLabelsByTaskId(1L)).thenReturn(listOf(label))
        whenever(labelMapper.labelToResponse(label)).thenReturn(labelResponse)

        mockMvc.perform(get("$url/task/1"))
            .andExpect(status().isOk)

        verify(labelService).findLabelsByTaskId(1L)
        verify(labelMapper).labelToResponse(label)
    }
}