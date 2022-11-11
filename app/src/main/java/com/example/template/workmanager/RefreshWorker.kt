package com.example.template.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.template.data.repository.EventRepository

import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.HttpException

@HiltWorker
class RefreshWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val repository: EventRepository
) : CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "com.example.template.workmanager.RefreshWorker"
        const val MAX_ATTEMPT_COUNT = 3
    }
    override suspend fun doWork(): Result {

        try {
            repository.refresh()
        } catch (e: HttpException) {
            return if(runAttemptCount < MAX_ATTEMPT_COUNT){
                Result.retry()
            }else{
                Result.failure()
            }
        }

        return Result.success()
    }


}