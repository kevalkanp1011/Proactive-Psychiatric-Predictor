package dev.kevalkanapriya.umbrellaacademy

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import dev.kevalkanapriya.umbrellaacademy.ui.theme.UmbrellaAcademyTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UmbrellaAcademyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val text = AccService.tokenLiveData.value

                    Column {
                        Button(onClick = {
                            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                        }) {
                            Text(text = "Start Acc Service")
                        }

                        Text(text = text?: "value isnot available")
                    }


                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UmbrellaAcademyTheme {
        Greeting("Android")
    }
}

class AccService: AccessibilityService() {


    private fun getEventText(event: AccessibilityEvent?): String {
        val sb = StringBuilder()
        if (event != null) {
            for (s in event.text) {
                sb.append(s)
            }
        }
        return sb.toString()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val l1 = getEventText(event)
        if (l1 != "") {
            Log.d("Acc Event", "$l1")
            tokenLiveData.postValue(l1)
        }

    }

    override fun onInterrupt() {
        Log.d("Acc Event", "[-] Interrupted !!! ")
        tokenLiveData.postValue("[-] Interrupted !!! ");
    }

    override fun onServiceConnected() {
        super.onServiceConnected()

        Log.d("Acc Event", "[+] Connected")
        tokenLiveData.postValue("[+] Connected")
        AccessibilityServiceInfo().apply {
            flags = AccessibilityServiceInfo.DEFAULT
            eventTypes = AccessibilityEvent.TYPES_ALL_MASK
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            serviceInfo = this
        }

    }

    companion object {
        val tokenLiveData = MutableLiveData<String>()
    }

}