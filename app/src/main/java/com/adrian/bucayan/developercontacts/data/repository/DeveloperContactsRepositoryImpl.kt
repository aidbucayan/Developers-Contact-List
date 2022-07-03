package com.adrian.bucayan.developercontacts.data.repository

import com.adrian.bucayan.developercontacts.data.dto.DeveloperDto
import com.adrian.bucayan.developercontacts.data.dto.StatusResponseDto
import com.adrian.bucayan.developercontacts.data.remote.DeveloperContactsApi
import com.adrian.bucayan.developercontacts.domain.repository.DeveloperContactsRepository
import com.adrian.bucayan.developercontacts.domain.request.DeveloperRequest

class DeveloperContactsRepositoryImpl(
    private val api: DeveloperContactsApi
) : DeveloperContactsRepository {

    override suspend fun getDevelopersList(): List<DeveloperDto> {
        return api.getDevelopers()
    }

    override suspend fun addDeveloper(developerRequest: DeveloperRequest): StatusResponseDto {
        return api.addDevelopers(developerRequest)
    }

    override suspend fun editDeveloper(developerRequest: DeveloperRequest): StatusResponseDto {
        return api.editDeveloper(developerRequest)
    }

    override suspend fun deleteDeveloper(developerRequest: DeveloperRequest): StatusResponseDto {
        return api.deleteDeveloper(developerRequest)
    }

}