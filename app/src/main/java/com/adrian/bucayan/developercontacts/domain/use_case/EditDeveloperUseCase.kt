package com.adrian.bucayan.developercontacts.domain.use_case

import android.content.Context
import com.adrian.bucayan.developercontacts.R
import com.adrian.bucayan.developercontacts.common.Resource
import com.adrian.bucayan.developercontacts.data.dto.toAddDeveloper
import com.adrian.bucayan.developercontacts.domain.model.StatusResponse
import com.adrian.bucayan.developercontacts.domain.repository.DeveloperContactsRepository
import com.adrian.bucayan.developercontacts.domain.request.DeveloperRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class EditDeveloperUseCase @Inject constructor(private val repository: DeveloperContactsRepository,
                                               @ApplicationContext private val appCtx: Context) {

    operator fun invoke(developerRequest: DeveloperRequest): Flow<Resource<StatusResponse>> = flow {
        try {
            emit(Resource.Loading<StatusResponse>())
            val getSearchedMovie = repository.editDeveloper(developerRequest).toAddDeveloper()
            emit(Resource.Success<StatusResponse>(getSearchedMovie))
        } catch(e: HttpException) {
            emit(Resource.Error<StatusResponse>(appCtx.getString(R.string.oops_something_went_wrong)))
        } catch(e: IOException) {
            emit(Resource.Error<StatusResponse>(appCtx.getString(R.string.error_couldnt_reach_server)))
        }
    }

}