package com.adrian.bucayan.developercontacts.domain.repository

import com.adrian.bucayan.developercontacts.data.dto.AddDeveloperDto
import com.adrian.bucayan.developercontacts.data.dto.DeveloperDto


interface DeveloperContactsRepository {

    suspend fun getDevelopersList(): List<DeveloperDto>

    suspend fun addDeveloper(): AddDeveloperDto


}