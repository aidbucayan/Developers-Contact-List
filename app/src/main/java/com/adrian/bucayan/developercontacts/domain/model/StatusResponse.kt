package com.adrian.bucayan.developercontacts.domain.model

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import kotlinx.parcelize.Parcelize

@JsonIgnoreProperties(ignoreUnknown = true)
@Parcelize
data class StatusResponse(
    var status: String?
): Parcelable {

}
