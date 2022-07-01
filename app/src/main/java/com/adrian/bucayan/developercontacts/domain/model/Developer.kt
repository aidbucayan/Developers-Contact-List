package com.adrian.bucayan.developercontacts.domain.model

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import kotlinx.parcelize.Parcelize

@JsonIgnoreProperties(ignoreUnknown = true)
@Parcelize
data class Developer(
    var photo: String?,
    var name: String?,
    var email: String?,
    var phoneNumber: String?,
    var companyName: String?
): Parcelable {

}
