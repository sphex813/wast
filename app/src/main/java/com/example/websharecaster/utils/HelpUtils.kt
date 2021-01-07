package com.example.websharecaster.utils

import android.app.Application
import com.example.websharecaster.api.models.SccI18nInfoLabel
import org.apache.commons.codec.digest.Md5Crypt
import org.koin.java.KoinJavaComponent.inject
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object HelpUtils {
    fun hash(algorithm: String, str: String) = MessageDigest
        .getInstance(algorithm)
        .digest(str.toByteArray(StandardCharsets.UTF_8))
        .map { String.format("%02x", it) }
        .joinToString("")

    fun getHashedPassword(pass: String, salt: String): String? {
        if (!salt.isEmpty()) {
            return hash("SHA-1",
                Md5Crypt.md5Crypt(pass.toByteArray(Charsets.UTF_8), "$1$" + salt).toString())
        }
        return null
    }

    fun getMovieLink(movieInfoList: List<SccI18nInfoLabel>): String {
        val movieInfo = movieInfoList.find { image -> image.art?.poster != null }
        var movieImage = movieInfo?.art?.poster + "?w180"
        if (!movieImage.startsWith("https:") || !movieImage.startsWith("https:")) {
            movieImage = "https:" + movieImage
        }
        return movieImage
    }

    fun getTitle(movieInfoList: List<SccI18nInfoLabel>): String {
        val infoLabel = movieInfoList.find { info -> info.title != null }
        return infoLabel?.title ?: ""
    }
}