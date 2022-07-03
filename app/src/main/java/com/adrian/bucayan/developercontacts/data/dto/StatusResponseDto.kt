package com.adrian.bucayan.developercontacts.data.dto

import com.adrian.bucayan.developercontacts.domain.model.StatusResponse
import com.fasterxml.jackson.annotation.JsonProperty

data class StatusResponseDto(

    @JsonProperty("status")
    var status: String?,
)

fun StatusResponseDto.toAddDeveloper(): StatusResponse {
    return  StatusResponse(
        status = status
    )
}

