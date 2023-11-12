package dev.kevalkanapriya.umbrellaacademy.work.workers

import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.kevalkanapriya.umbrellaacademy.R
import dev.kevalkanapriya.umbrellaacademy.domain.model.QnA
import dev.kevalkanapriya.umbrellaacademy.notification.NotificationConstant
import dev.kevalkanapriya.umbrellaacademy.notification.reminderQnANotification
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class QnAReminderWorker constructor(
    private val notificationManager: NotificationManager,
    private val context: Context,
    workerParams: WorkerParameters,
): CoroutineWorker(context, workerParams), KoinComponent {

    private val sharedPreferencesKey = "lastIndex"
    private val batchSize = 10
    private var count = 1
    override suspend fun doWork(): Result {
        //reminder user to open the app and answer the questionnaire

        Log.d("QnAReminderWorker", "started")
        processRecords(count)

        notificationManager.notify(NotificationConstant.QnANotificationId, reminderQnANotification(context))
        count++

        return Result.success()
    }

    private fun processRecords(count: Int) {
        // Load the records from your JSON files
        val allRecords = loadAllRecords()

        // Get the last processed record index
        val lastProcessedIndex = getLastProcessedIndex()

        // Filter the next batch of records
        val recordsToProcess = filterNextBatch(allRecords, lastProcessedIndex)

        // Update the last processed index
        updateLastProcessedIndex(lastProcessedIndex + batchSize)

        // Implement logic to process the filtered records
        process(recordsToProcess, count)
    }

    private fun loadAllRecords(): List<QnA> {
        // Load all records from your JSON files or other data source
        // Return a list of records
        val questionnaireJson = context.resources.openRawResource(R.raw.questionnaire)
            .readBytes()
            .decodeToString()

        Log.d("QnAReminderWorker", "$questionnaireJson")
        val obj = Json.decodeFromString<List<QnA>>(questionnaireJson)
        Log.d("QnAReminderWorker", "${obj}")

        return Json.decodeFromString(questionnaireJson)
    }

    private fun filterNextBatch(records: List<QnA>, startIndex: Int): List<QnA> {
        // Filter the next batch of records based on the startIndex
        val endIndex = minOf(startIndex + batchSize, records.size)
        return records.subList(startIndex, endIndex)
    }

    private fun getLastProcessedIndex(): Int {
        // Retrieve the last processed index from SharedPreferences
        return get<SharedPreferences>().getInt(sharedPreferencesKey, 0)
    }

    private fun updateLastProcessedIndex(index: Int) {
        // Update the last processed index in SharedPreferences
        get<SharedPreferences>().edit()
            .putInt(sharedPreferencesKey, index)
            .apply()
    }

    private fun process(records: List<QnA>, count: Int) {
        // Implement your logic to process the records

        val directoryName = "reminder"

        val directory = File(context.filesDir, directoryName)

        if (!directory.exists()) {
            // If not, create the directory
            if (directory.mkdir()) {
                Log.d("DirectoryCreation", "Directory created successfully")
            } else {
                Log.e("DirectoryCreation", "Failed to create directory")
            }
        } else {
            // Directory already exists
            Log.d("DirectoryCreation", "Directory already exists")
        }

        val filePath = "${context.filesDir}/$directoryName/${count}.json"

        Log.d("QnAReminderWorker", "path $filePath")
        val recordString = Json.encodeToString(records)
        try {
            FileOutputStream(filePath).use { it.write(recordString.toByteArray()) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}