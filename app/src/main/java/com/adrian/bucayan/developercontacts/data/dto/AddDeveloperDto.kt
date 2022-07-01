package com.adrian.bucayan.developercontacts.data.dto

import com.adrian.bucayan.developercontacts.domain.model.AddDeveloper
import com.fasterxml.jackson.annotation.JsonProperty

data class AddDeveloperDto(

    @JsonProperty("status")
    var status: String?,
)

fun AddDeveloperDto.toAddDeveloper(): AddDeveloper {
    return  AddDeveloper(
        status = status
    )
}

