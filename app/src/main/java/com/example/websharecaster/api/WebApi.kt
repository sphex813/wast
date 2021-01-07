package com.example.websharecaster.api

import com.example.websharecaster.api.models.SccResponse
import com.example.websharecaster.api.models.StreamInfo
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface WebApi {
    companion object {
        val WEBSHARE_URL = "https://webshare.cz/api/"
        val SCC_URL_FILTER = "https://plugin.sc2.zone/api/media/filter/"
        val SCC_URL = "https://plugin.sc2.zone/api/media/"
    }

    @Xml
    @FormUrlEncoded
    @POST
    suspend fun salt(
        @Field("username_or_email") username: String,
        @Url url: String = WEBSHARE_URL + "salt/",
    ): Response<SaltResponse>

    @Xml
    @FormUrlEncoded
    @POST
    suspend fun login(
        @Field("username_or_email") username: String,
        @Field("password") password: String,
        @Field("keep_logged_in") loggedIn: Int = 0,
        @Url url: String = WEBSHARE_URL + "login/",
    ): Response<LoginResponse>

    @Xml
    @FormUrlEncoded
    @POST
    fun search(
        @Field("what") what: String,
        @Field("sort") sort: String? = null,
        @Field("limit") limit: Int = 30,
        @Field("offset") offset: Int = 0,
        @Field("category") category: String? = "video",
        @Url url: String = WEBSHARE_URL + "search/",
    ): Call<SearchResponse>

    @Xml
    @FormUrlEncoded
    @POST
    suspend fun fileLink(
        @Field("ident") ident: String,
        @Field("wst") wst: String,
        @Url url: String = WEBSHARE_URL + "file_link/",
    ): Response<FileLinkResponse>

    @Json
    @GET
    suspend fun newsDubbed(
        @Url url: String = SCC_URL_FILTER + "newsDubbed",
        @Query("type") type: String = "movie",
        @Query("sort") sort: String = "dateAdded",
        @Query("order") order: String = "desc",
        @Query("days") days: String = "365",
        @Query("lang") langCs: String = "cs",
        @Query("lang") langSk: String = "sk",
        @Query("access_token") access_token: String = "th2tdy0no8v1zoh1fs59",
    ): Response<SccResponse>

    @Json
    @GET
    suspend fun news(
        @Url url: String = SCC_URL_FILTER + "news",
        @Query("type") type: String = "movie",
        @Query("sort") sort: String = "dateAdded",
        @Query("days") days: String = "365",
        @Query("order") order: String = "desc",
        @Query("access_token") access_token: String = "th2tdy0no8v1zoh1fs59",
    ): Response<SccResponse>

    @Json
    @GET
    suspend fun all(
        @Url url: String = SCC_URL_FILTER + "all",
        @Query("type") type: String = "movie",
        @Query("sort") sort: String = "playCount",
        @Query("order") order: String = "desc",
        @Query("access_token") access_token: String = "th2tdy0no8v1zoh1fs59",
    ): Response<SccResponse>

    @Json
    @GET
    suspend fun getContentFromId(
        @Url url: String = SCC_URL_FILTER + "parent",
        @Query("value") value: String,
        @Query("sort") sort: String = "episode",
        @Query("access_token") access_token: String = "th2tdy0no8v1zoh1fs59",
    ): Response<SccResponse>

    @Json
    @GET
    suspend fun streams(
        @Url url: String = SCC_URL_FILTER + "stream_id" + "/streams",
        @Query("access_token") access_token: String = "th2tdy0no8v1zoh1fs59",
    ): Response<List<StreamInfo>>

    @Json
    @GET
    suspend fun searchScc(
        @Url url: String = SCC_URL_FILTER + "search",
        @Query("value") value: String,
        @Query("type") type: String = "*",
        @Query("sort") sort: String = "playCount",
        @Query("order") order: String = "desc",
        @Query("access_token") access_token: String = "th2tdy0no8v1zoh1fs59",
    ): Response<SccResponse>
}
