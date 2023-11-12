package dev.kevalkanapriya.umbrellaacademy

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.work.BackoffPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dev.kevalkanapriya.umbrellaacademy.navigation.Screen
import dev.kevalkanapriya.umbrellaacademy.navigation.SetUpNavGraph
import dev.kevalkanapriya.umbrellaacademy.ui.theme.UmbrellaAcademyTheme
import dev.kevalkanapriya.umbrellaacademy.work.workers.AccelerometerNRWorker
import dev.kevalkanapriya.umbrellaacademy.work.workers.AccelerometerWorker
import dev.kevalkanapriya.umbrellaacademy.work.workers.KeyLoggerNRWorker
import dev.kevalkanapriya.umbrellaacademy.work.workers.QnAReminderWorker
import dev.kevalkanapriya.umbrellaacademy.work.workers.QnANRWorker
import java.util.concurrent.TimeUnit


const val MainActivityLog = "MainActivityLog"
const val AccServiceLog = "AccServiceLog"


class MainActivity : ComponentActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val workManager = WorkManager.getInstance(applicationContext)




        val requestQnA = PeriodicWorkRequestBuilder<QnANRWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            //.setInitialDelay(initialDelayInMillis(24, 0),TimeUnit.MILLISECONDS)
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.LINEAR,
                backoffDelay = 12,
                timeUnit = TimeUnit.SECONDS
            )
            .build()
        workManager.enqueueUniquePeriodicWork(
            "QnAWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            requestQnA
        )

        val requestKeyLogger = PeriodicWorkRequestBuilder<KeyLoggerNRWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            //.setInitialDelay(initialDelayInMillis(24, 0),TimeUnit.MILLISECONDS)
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.LINEAR,
                backoffDelay = 12,
                timeUnit = TimeUnit.SECONDS
            )
            .build()
        workManager.enqueueUniquePeriodicWork(
            "KeyLoggerWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            requestKeyLogger
        )

        val requestAccelerometer = PeriodicWorkRequestBuilder<AccelerometerWorker>(
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        )
            //.setInitialDelay(initialDelayInMillis(24, 0),TimeUnit.MILLISECONDS)
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.LINEAR,
                backoffDelay = 12,
                timeUnit = TimeUnit.SECONDS
            )
            .build()
        workManager.enqueueUniquePeriodicWork(
            "AccelerometerWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            requestAccelerometer
        )

        val requestAccelerometerNR = PeriodicWorkRequestBuilder<AccelerometerNRWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            //.setInitialDelay(initialDelayInMillis(24, 0),TimeUnit.MILLISECONDS)
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.LINEAR,
                backoffDelay = 12,
                timeUnit = TimeUnit.SECONDS
            )
            .build()
        workManager.enqueueUniquePeriodicWork(
            "AccelerometerNRWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            requestAccelerometerNR
        )

        val requestQnAReminder = PeriodicWorkRequestBuilder<QnAReminderWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            //.setInitialDelay(initialDelayInMillis(22, 30),TimeUnit.MILLISECONDS)
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.LINEAR,
                backoffDelay = 12,
                timeUnit = TimeUnit.SECONDS
            )
            .build()
        workManager.enqueueUniquePeriodicWork(
            "QnAReminder",
            ExistingPeriodicWorkPolicy.KEEP,
            requestQnAReminder
        )

        workManager.getWorkInfosForUniqueWorkLiveData("QnAReminder")
            .observe(this@MainActivity) { workInfo ->
                workInfo.forEach {
                    Log.d("MainActivity", "Work QnAReminder: ${it.state}")
                }
            }

        workManager.getWorkInfosForUniqueWorkLiveData("AccelerometerWorker")
            .observe(this@MainActivity) { workInfo ->
                workInfo.forEach {
                    Log.d("MainActivity", "Work AccelerometerWorker: ${it.state}")
                }
            }

        workManager.getWorkInfosForUniqueWorkLiveData("KeyLoggerWorker")
            .observe(this@MainActivity) { workInfo ->
                workInfo.forEach {
                    Log.d("MainActivity", "Work KeyLoggerWorker: ${it.state}")
                }
            }
        workManager.getWorkInfosForUniqueWorkLiveData("QnAWorker")
            .observe(this@MainActivity) { workInfo ->
                workInfo.forEach {
                    Log.d("MainActivity", "Work QnAWorker: ${it.state}")
                }
            }



        setContent {

            val navController = rememberNavController()

            UmbrellaAcademyTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    SetUpNavGraph(navController, Screen.Home.route)


                }
            }
        }
    }


}







