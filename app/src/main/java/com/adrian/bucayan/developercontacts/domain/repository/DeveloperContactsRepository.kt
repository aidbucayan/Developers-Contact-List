package com.adrian.bucayan.developercontacts.domain.repository

import com.adrian.bucayan.developercontacts.data.dto.DeveloperDto
import com.adrian.bucayan.developercontacts.data.dto.StatusResponseDto
import com.adrian.bucayan.developercontacts.domain.request.DeveloperRequest


interface DeveloperContactsRepository {

    suspend fun getDevelopersList(): List<DeveloperDto>

    suspend fun addDeveloper(developerRequest: DeveloperRequest): StatusResponseDto

    suspend fun editDeveloper(developerRequest: DeveloperRequest): StatusResponseDto

    suspend fun deleteDeveloper(developerRequest: DeveloperRequest): StatusResponseDto
}