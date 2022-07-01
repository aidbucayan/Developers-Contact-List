package com.adrian.bucayan.developercontacts.data.remote

import com.adrian.bucayan.developercontacts.data.dto.AddDeveloperDto
import com.adrian.bucayan.developercontacts.data.dto.DeveloperDto
import retrofit2.http.*

interface DeveloperContactsApi {

    @GET("my/api/getDevelopers/v1")
    suspend fun getDevelopers(): List<DeveloperDto>

    @POST("my/api/addDeveloper")
    suspend fun addDevelopers(): AddDeveloperDto

}