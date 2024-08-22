package dev.kevalkanapriya.umbrellaacademy.data.remote

import dev.kevalkanapriya.umbrellaacademy.data.remote.dto.PostRequest
import dev.kevalkanapriya.umbrellaacademy.data.remote.dto.PostResponse
import dev.kevalkanapriya.util.HttpRoutes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType


class PostServiceImpl(
    private val client: HttpClient
): PostService {
    override suspend fun getPosts(): List<PostResponse> {
        return try {
            client.get { url(HttpRoutes.POSTS) }.body()
        } catch (e: RedirectResponseException) {
            // 3xx - responses
            /** Yönlendirme ile ilgili hataları döndermek için kullanılır. */
            /** Örneğin Proxy kullanılması gerekiyorsa ona ait hatayı dönderir veya taşınma işlemi varsa onu dönderir */
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ClientRequestException) {
            // 4xx - responses
            /** İstemci hatalarını döndermek için kullanıyoruz. Yanlış request atılırsa veya ödeme istenirse veya izin verilmeyen bir method kullanılırsa */
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ServerResponseException) {
            // 5xx - responses
            /** Sunucu hatalarını döndermek için kullandık. Örneğin istek attığımız sunucu cevap vermezse 500 hata kodlarından biri döner. */
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch (e: Exception) {
            /** Genel dönecek hatlar için de normal Exception sınıfını kullanabiliriz. */
            println("Error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun createPost(postRequest: PostRequest): PostResponse? {
        return try {
            client.post {
                url(HttpRoutes.POSTS)
                contentType(ContentType.Application.Json)
                setBody(postRequest)
            }.body()

        } catch (e: RedirectResponseException) {
            // 3xx - responses
            /** Yönlendirme ile ilgili hataları döndermek için kullanılır. */
            /** Örneğin Proxy kullanılması gerekiyorsa ona ait hatayı dönderir veya taşınma işlemi varsa onu dönderir */
            println("Error: ${e.response.status.description}")
            null
        } catch (e: ClientRequestException) {
            // 4xx - responses
            /** İstemci hatalarını döndermek için kullanıyoruz. Yanlış request atılırsa veya ödeme istenirse veya izin verilmeyen bir method kullanılırsa */
            println("Error: ${e.response.status.description}")
            null
        } catch (e: ServerResponseException) {
            // 5xx - responses
            /** Sunucu hatalarını döndermek için kullandık. Örneğin istek attığımız sunucu cevap vermezse 500 hata kodlarından biri döner. */
            println("Error: ${e.response.status.description}")
            null
        } catch (e: Exception) {
            /** Genel dönecek hatlar için de normal Exception sınıfını kullanabiliriz. */
            println("Error: ${e.message}")
            null
        }
    }

   
}
