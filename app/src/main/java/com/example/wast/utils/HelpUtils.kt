package com.example.wast.utils

import android.app.Application
import android.os.Environment
import android.util.Log
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.ExecuteCallback
import com.arthenica.mobileffmpeg.FFmpeg
import com.example.wast.api.models.SccI18nInfoLabel
import com.example.wast.api.models.StreamInfo
import org.apache.commons.codec.digest.Md5Crypt
import java.io.File
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
        val langs = mutableListOf("sk", "cs", "en")
        var title: String? = null
        langs.forEach { lang ->
            title = movieInfoList.find { info -> info.title != null && info.lang.equals(lang) }?.title
            if (!title.isNullOrEmpty()) {
                return title as String
            }
        }
        return ""
    }

    fun generateFileWithSound(application: Application, link: String): String {
        //        val info: MediaInformation = FFprobe.getMediaInformation(link)
        val path: String = application.getExternalFilesDir(Environment.DIRECTORY_MOVIES).toString() + "/webshare.mp4"
        val file = File(path)
        file.delete()
        FFmpeg.cancel();
        FFmpeg.executeAsync("-i " + link + " -acodec mp3 -vcodec copy " + path, object : ExecuteCallback {
            override fun apply(executionId: Long, returnCode: Int) {
                if (returnCode == Config.RETURN_CODE_SUCCESS) {
                    Log.i(Config.TAG, "Async command execution completed successfully.");
                } else if (returnCode == Config.RETURN_CODE_CANCEL) {
                    Log.i(Config.TAG, "Async command execution cancelled by user.");
                } else {
                    Log.i(Config.TAG, String.format("Async command execution failed with returnCode=%d.", returnCode));
                }
            }
        })
        return path
        //iba pre nefunkcne codec-y napr EAC3, HEVC ...
        //subor treba z lokalneho serveru poslať castu (bude fungovat aj ked bude transcodenuta iba cast?)
        //vratit by sa mala cesta suboru na lokalnom servery aby ho vedel cast prehrat
    }

    fun getSortedStreams(streams: List<StreamInfo>): List<StreamInfo> {
        val languageHashMap: HashMap<String, Int> = hashMapOf(
            "sk" to 0,
            "cs" to 1,
            "en" to 2,
        )

        val sortedList = streams.sortedWith(compareBy({ languageHashMap.getOrDefault(it.audio?.get(0)?.language, 4) }, { it.size!!.toBigInteger() }))
        return sortedList
    }

}