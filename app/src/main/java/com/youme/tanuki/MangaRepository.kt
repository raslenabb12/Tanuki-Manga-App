package com.youme.tanuki

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.jsoup.Jsoup

class MangaRepository(
    private val client: OkHttpClient = OkHttpClient(),
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
) {
    private fun convertHtmlToPlainText(html: String?): String {
        return html?.let {
            Jsoup.parse(it).text()
        } ?: ""
    }
    suspend fun fetchMangaDetails(name: String): Result<MangaDetails?> = withContext(Dispatchers.IO) {
        try {
            val query = """
                query (${'$'}search: String) {
                  Media(search: ${'$'}search, type: MANGA ) {
                    id
                    title {
                      romaji
                      english
                      native
                    }
                    description
                    coverImage {
                      extraLarge
                      large
                      medium
                    }
                    
                    status
                    chapters
                    genres
                    averageScore
                    popularity
                    bannerImage
                    startDate {
      year
      month
      day
    } 
    endDate {
      year
      month
      day
    }
    characters {
      edges{
        node{
          name {
            full
          }
          image{
            medium
            large
          }
        }
        role
      }
      }
      recommendations{
      nodes{
        mediaRecommendation{
          id
          title {
            romaji
            english
          }
          countryOfOrigin
          coverImage {
            extraLarge
            large
            medium
          }
        }
        }
        }
      
                  }
                }
            """.trimIndent()
            val variables = mapOf("search" to name)
            val requestBody = JSONObject().apply {
                put("query", query)
                put("variables", JSONObject(variables))
            }.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("https://graphql.anilist.co")
                .post(requestBody)
                .build()
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            val jsonAdapter = moshi.adapter(MangaDetailResponse::class.java)
            val apiResponse = jsonAdapter.fromJson(responseBody)
            Result.success(apiResponse?.data?.Media?.copy( description = convertHtmlToPlainText(apiResponse.data?.Media?.description)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun searchMangas(query: String, page: Int = 1, perPage: Int = 10): Result<List<Manga>> = withContext(Dispatchers.IO) {
        try {
            val graphqlQuery = """
                query (${'$'}search: String, ${'$'}page: Int, ${'$'}perPage: Int) {
                  Page(page: ${'$'}page, perPage: ${'$'}perPage) {
                    media(search: ${'$'}search, type: MANGA) {
                      id
                      title {
                        romaji
                        english
                      }
                      coverImage {
                        medium
                      }
                      status
                      chapters
                    }
                  }
                }
            """.trimIndent()

            val variables = mapOf(
                "search" to query,
                "page" to page,
                "perPage" to perPage
            )
            val requestBody = JSONObject().apply {
                put("query", graphqlQuery)
                put("variables", JSONObject(variables))
            }.toString().toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url("https://graphql.anilist.co")
                .post(requestBody)
                .build()
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            val jsonAdapter = moshi.adapter(MangaSearchResponse::class.java)
            val apiResponse = jsonAdapter.fromJson(responseBody)
            Result.success(apiResponse?.data?.Page?.media ?: emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
data class MangaSearchResponse(
    val data: MangaSearchData?
)

data class MangaSearchData(
    val Page: MangaSearchPage?
)

data class MangaSearchPage(
    val media: List<Manga>?
)
class MangaDetailsViewModel(private val repository: MangaRepository) : ViewModel() {
    private val _mangaDetails = MutableStateFlow<MangaDetails?>(null)
    val mangaDetails: StateFlow<MangaDetails?> = _mangaDetails
    fun fetchMangaDetails(name: String) {
        viewModelScope.launch {
            val details = repository.fetchMangaDetails(name)
            _mangaDetails.value = details.getOrNull()
        }
    }
}