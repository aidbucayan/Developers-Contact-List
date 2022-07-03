package com.adrian.bucayan.developercontacts.data.remote

import com.adrian.bucayan.developercontacts.data.dto.DeveloperDto
import com.adrian.bucayan.developercontacts.data.dto.StatusResponseDto
import com.adrian.bucayan.developercontacts.domain.request.DeveloperRequest
import retrofit2.http.*

interface DeveloperContactsApi {

    @GET("my/api/getDevelopers")
    suspend fun getDevelopers(): List<DeveloperDto>

    @POST("my/api/addDeveloper")
    suspend fun addDevelopers(@Body developerRequest: DeveloperRequest): StatusResponseDto

    @PUT("my/api/editDeveloper")
    suspend fun editDeveloper(@Body developerRequest: DeveloperRequest): StatusResponseDto

    @POST("my/api/deleteDevelopers")
    suspend fun deleteDeveloper(@Body developerRequest: DeveloperRequest): StatusResponseDto

}