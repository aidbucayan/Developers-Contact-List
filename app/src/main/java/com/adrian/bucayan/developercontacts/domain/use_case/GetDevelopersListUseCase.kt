package com.adrian.bucayan.developercontacts.domain.use_case

import android.content.Context
import com.adrian.bucayan.developercontacts.R
import com.adrian.bucayan.developercontacts.common.Resource
import com.adrian.bucayan.developercontacts.data.dto.toDeveloper
import com.adrian.bucayan.developercontacts.domain.model.Developer
import com.adrian.bucayan.developercontacts.domain.repository.DeveloperContactsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetDevelopersListUseCase @Inject constructor(private val repository: DeveloperContactsRepository,
                                                   @ApplicationContext private val appCtx: Context) {

    operator fun invoke(): Flow<Resource<List<Developer>>> = flow {
        try {
            emit(Resource.Loading<List<Developer>>())
            val getSearchedMovie = repository.getDevelopersList().map { it.toDeveloper() }
            emit(Resource.Success<List<Developer>>(getSearchedMovie))
        } catch(e: HttpException) {
            emit(Resource.Error<List<Developer>>(appCtx.getString(R.string.oops_something_went_wrong)))
        } catch(e: IOException) {
            emit(Resource.Error<List<Developer>>(appCtx.getString(R.string.error_couldnt_reach_server)))
        }
    }

}