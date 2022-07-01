package com.adrian.bucayan.developercontacts.data.dto

import com.adrian.bucayan.developercontacts.domain.model.Developer
import com.fasterxml.jackson.annotation.JsonProperty

data class DeveloperDto(

    @JsonProperty("Photo")
    var Photo: String?,

    @JsonProperty("Name")
    var Name: String?,

    @JsonProperty("Email")
    var Email: String?,

    @JsonProperty("PhoneNumber")
    var PhoneNumber: String?,

    @JsonProperty("CompanyName")
    var CompanyName: String?
)

fun DeveloperDto.toDeveloper(): Developer {
    return  Developer(
        photo = Photo,
        name = Name,
        email = Email,
        phoneNumber = PhoneNumber,
        companyName = CompanyName
    )
}

