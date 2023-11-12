package dev.kevalkanapriya.umbrellaacademy.presentation.home

import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.kevalkanapriya.umbrellaacademy.domain.model.QnA
import dev.kevalkanapriya.util.ObserveAsEvents
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel
import java.io.FileInputStream
import java.io.FileNotFoundException


@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {

    val context = LocalContext.current
    
    ObserveAsEvents(flow = viewModel.channelFlow) { event ->
        when(event) {
            is HomeEvent.MakeApiCall -> {

            }

            is HomeEvent.ReceiveDataApi -> {

            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Spacer(modifier = Modifier.height(50.dp))

        Button(onClick = {
            context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }) {
            Text(text = "Start KeyLogger Service")
        }

        val filePath = "${context.filesDir}/reminder/1.json"

        Button(onClick = {

            val existingData: List<QnA> = try {
                val q = FileInputStream(filePath).bufferedReader().use { it.readText() }
                Json.decodeFromString(q)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                emptyList()
            }

            Log.d("HomeScreen", "$existingData")


        }) {

            Text(text = "Read $filePath")
        }

    }
}