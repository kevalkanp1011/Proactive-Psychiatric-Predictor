package dev.kevalkanapriya.umbrellaacademy.work.status

import androidx.work.WorkInfo
import androidx.work.WorkManager


/**
 * [SyncStatusMonitor] backed by [WorkInfo] from [WorkManager]
 */
//class WorkManagerSyncStatusMonitor(
//    context: Context
//) : SyncStatusMonitor {
//    override val isSyncing: Flow<Boolean> =
//        Transformations.map(
//            WorkManager.getInstance(context).getWorkInfosForUniqueWorkLiveData(SyncWorkName),
//            MutableList<WorkInfo>::anyRunning
//        )
//            .asFlow()
//            .conflate()
//}
//
//private val List<WorkInfo>.anyRunning get() = any { it.state == State.RUNNING }