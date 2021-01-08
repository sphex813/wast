package com.example.wast.api

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class LoginResponse(
    @PropertyElement val token: String?,
    @PropertyElement val app_version: String?,
    @PropertyElement val status: String?,
    @PropertyElement val code: String?,
    @PropertyElement val message: String?,
)

@Xml
data class SaltResponse(
    @PropertyElement val salt: String?,
    @PropertyElement val app_version: String?,
    @PropertyElement val code: String?,
    @PropertyElement val status: String?,
    @PropertyElement val message: String?,
)

@Xml
data class SearchResponse(
    @PropertyElement val total: Int?,
    @Element val files: List<File>,

    @PropertyElement val app_version: String?,
    @PropertyElement val code: String?,
    @PropertyElement val status: String?,
    @PropertyElement val message: String?,
)

@Xml
data class FileLinkResponse(
    @PropertyElement val link: String?,

    @PropertyElement val app_version: String?,
    @PropertyElement val code: String?,
    @PropertyElement val status: String?,
    @PropertyElement val message: String?,
)

@Xml
data class File(
    @PropertyElement val ident: String,
    @PropertyElement val name: String?,
    @PropertyElement val type: String?,
    @PropertyElement val img: String?,
    @PropertyElement val stripe: String?,
    @PropertyElement val stripe_count: Int?,
    @PropertyElement val size: String?,
    @PropertyElement val queued: Int?,
    @PropertyElement val positive_votes: Int?,
    @PropertyElement val negative_votes: Int?,

    @PropertyElement val code: String?,
    @PropertyElement val status: String?,
    @PropertyElement val message: String?,
)
