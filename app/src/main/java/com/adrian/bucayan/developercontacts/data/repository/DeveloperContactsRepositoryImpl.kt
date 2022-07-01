package com.adrian.bucayan.developercontacts.data.repository

import com.adrian.bucayan.developercontacts.data.dto.AddDeveloperDto
import com.adrian.bucayan.developercontacts.data.dto.DeveloperDto
import com.adrian.bucayan.developercontacts.data.remote.DeveloperContactsApi
import com.adrian.bucayan.developercontacts.domain.repository.DeveloperContactsRepository

class DeveloperContactsRepositoryImpl(
    private val api: DeveloperContactsApi
) : DeveloperContactsRepository {

    override suspend fun getDevelopersList(): List<DeveloperDto> {
        return api.getDevelopers()
    }

    override suspend fun addDeveloper(): AddDeveloperDto {
        return api.addDevelopers()
    }

}