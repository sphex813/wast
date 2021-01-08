package com.example.wast.api.models

data class SccResponse(
    val data: List<SccData>?,
    val pagination: SccPagination?,
    val totalCount: Int?,
)

data class SccData(
    val _id: String,
    val _score: String?,
    val _source: SccSource,
)

data class SccPagination(
    val from: Int?,
    val limit: Int?,
    val next: String?,
    val page: Int?,
    val pageCount: Int?,
    val prev: String?,
)

data class SccSource(
    val available_streams: SccStreams?,
    val cast: List<SccCast>?,
    val children_count: Int,
    val i18n_info_labels: List<SccI18nInfoLabel>,
    val info_labels: SccInfoLabel,
    val play_count: Int?,
    val ratings: SccRatings?,
    val services: SccServices?,
    val stream_info: SccStreamInfo?,
    val videos: List<SccVideo>?,
)

data class SccCast(
    val name: String?,
)

data class SccStreams(
    val count: Int?,
    val languages: SccLanguage?,
)

data class SccLanguage(
    val audio: SccLanguages?,
    val subtitles: SccLanguages?,
)

data class SccLanguages(
    val map: List<String>?,
)

data class SccRating(
    val rating: Float?,
    val votes: Int?,
)

data class SccRatings(
    val csfd: SccRating?,
    val imdb: SccRating?,
    val slug: SccRating?,
    val tmdb: SccRating?,
    val trakt: SccRating?,
    val revenue: Int?,
)

data class SccServices(
    val csfd: String?,
    val imdb: String?,
    val slug: String?,
    val tmdb: String?,
    val trakt: String?,
    val trakt_with_type: String?,
)

data class SccVideo(
    val lang: String?,
    val type: String?,
    val url: String?,
)

data class SccAudio(
    val channels: Int?,
    val codec: String?,
    val language: String?,
)

data class SccSubtitles(
    val language: String?,
    val forced: Boolean?,
)

data class SccStreamInfo(
    val audio: SccAudio?,
    val subtitles: SccSubtitles?,
    val videos: List<SccVideoInfo>?,
)

data class SccArt(
    val poster: String?,
)

data class SccVideoInfo(
    val aspect: Float?,
    val codec: String?,
    val duration: Float?,
    val hdr: Boolean?,
    val height: Float?,
    val width: Float?,
)

data class SccI18nInfoLabel(
    val art: SccArt?,
    val lang: String?,
    val parent_titles: List<String>?,
    val plot: String?,
    val title: String?,
    val trailer: String?,
)

data class SccInfoLabel(
    val dateadded: String?,
    val director: List<String>?,
    val duration: Int?,
    val episode: Int?,
    val genre: List<String>?,
    val mediatype: String?,
    val originaltitle: String?,
    val premiered: String?,
    val season: Int?,
    val studio: List<String>?,
    val trailer: String?,
    val writer: List<String>?,
    val year: Int?,
)

data class StreamInfo(
    val audio: List<SccAudio>?,
    val ident: String,
    val media: String?,
    val name: String?,
    val provider: String?,
    val size: String?,
    val subtitles: List<SccSubtitles>?,
    val video: List<SccVideoInfo>?,
    val _id: String
)
