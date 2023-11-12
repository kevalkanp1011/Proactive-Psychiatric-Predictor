package dev.kevalkanapriya.umbrellaacademy.data.remote

import dev.kevalkanapriya.umbrellaacademy.data.remote.dto.ApiResponse
import dev.kevalkanapriya.util.HttpRoutes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import java.io.File

class ApiServiceImpl(
    private val client: HttpClient
): ApiService {
    override suspend fun sendAccelerometerData(file: File): Response {

        return try {
            val body = client.post {
                url(HttpRoutes.Api_Send_Accelerometer_Data)
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("accelerometer_data", file.readBytes(), Headers.build {
                                append(HttpHeaders.ContentType, "text/csv")
                                append(HttpHeaders.ContentDisposition, "filename=\"accelerometer_data.csv\"")
                            })
                        }
                    )
                )

                onUpload { bytesSentTotal, contentLength ->
                    println("Sent $bytesSentTotal bytes from $contentLength")
                }

            }.body<String>()

            ApiResponse.Success(data = body)

        } catch (e: Exception) {
            ApiResponse.Failed(message = e.message)
        }
    }

    override suspend fun sendKeyLoggerData(file: File): Response {
        return client.post {
            url(HttpRoutes.Api_Send_Keylogger_Data)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("keylogger_data", file.readBytes(), Headers.build {
                            append(HttpHeaders.ContentType, "text/csv")
                            append(HttpHeaders.ContentDisposition, "filename=\"keylogger_data.csv\"")
                        })
                    }
                )
            )

            onUpload { bytesSentTotal, contentLength ->
                println("Sent $bytesSentTotal bytes from $contentLength")
            }
        }.body()
    }

    override suspend fun sendQnAData(file: File): Response {
        return client.post {
            url(HttpRoutes.Api_Send_QnA_Data)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("qna_data", file.readBytes(), Headers.build {
                            append(HttpHeaders.ContentType, "text/csv")
                            append(HttpHeaders.ContentDisposition, "filename=\"qna_data.csv\"")
                        })
                    }
                )
            )

            onUpload { bytesSentTotal, contentLength ->
                println("Sent $bytesSentTotal bytes from $contentLength")
            }
        }.body()
    }
}