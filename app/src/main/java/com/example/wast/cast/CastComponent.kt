package com.example.wast.cast

import android.app.Application
import android.os.Environment
import android.util.Log
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS
import com.arthenica.mobileffmpeg.ExecuteCallback
import com.arthenica.mobileffmpeg.FFmpeg
import com.example.wast.api.models.SccData
import com.google.android.gms.cast.*
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.google.android.gms.common.api.ResultCallbacks
import com.google.android.gms.common.api.Status
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File


class CastComponent : KoinComponent {
    private val app: Application by inject()
    private lateinit var castContext: CastContext

    fun setCastContext(cast: CastContext) {
        castContext = cast
        castContext.sessionManager.addSessionManagerListener(SessionListener())
    }

    private fun loadRemoteMedia(
        position: Int,
        movie: SccData,
        link: String,
    ) {
        if (link.isNullOrEmpty()) {
            //TODO nenasiel sa link
            return
        }
        if (castContext.sessionManager.currentCastSession == null) {
            return
        }

        val mediaQueueItem: MediaQueueItem = MediaQueueItem.Builder(buildMediaInfo(movie, link)).build()

        val queueList: ArrayList<MediaQueueItem> = ArrayList()
        queueList.add(mediaQueueItem)
        var queueArray: Array<MediaQueueItem?>? = arrayOfNulls(queueList.size)
        queueArray = queueList.toArray(queueArray)

        val remoteMediaClient: RemoteMediaClient =
            castContext.sessionManager.currentCastSession.getRemoteMediaClient()
                ?: return
//        remoteMediaClient.queueLoad(mediaQueueItem, 0)
//        if (remoteMediaClient.mediaInfo != null) {
//            remoteMediaClient.queueInsertItems(queueArray, 0, null);
//            //TODO treba dat vediet ze sa pridal item do queue
//        } else {
        remoteMediaClient.load(MediaLoadRequestData.Builder()
            .setMediaInfo(buildMediaInfo(movie, link))
            .setAutoplay(true)
            .setCurrentTime(position.toLong()).build())
            .setResultCallback(object : ResultCallbacks<RemoteMediaClient.MediaChannelResult>() {
                override fun onSuccess(p0: RemoteMediaClient.MediaChannelResult) {
                    Log.d("cast", "on Success")
                }

                override fun onFailure(p0: Status) {
                    Log.d("cast", "on Failure")
                }
            })
    }
//    }


    private fun buildMediaInfo(movie: SccData, link: String): MediaInfo? {

        val frenchAudio = MediaTrack.Builder(1, MediaTrack.TYPE_AUDIO)
            .setName("French Audio")
            .setContentId("trk0001")
            .setLanguage("fr")
            .build()
//        val path: String = generateFileWithSound(link)

        val movieMetadata = setMovieMetaData(movie)

        return MediaInfo.Builder(link)
            .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
            .setContentType("videos/mp4") //TODO pridat do Constants
            .setMetadata(movieMetadata)
            .setStreamDuration(MediaInfo.UNKNOWN_DURATION)
            .build()


    }

    //TODO premiestnit do utils/service
    private fun generateFileWithSound(link: String): String {
        //        val info: MediaInformation = FFprobe.getMediaInformation(link)
        val path: String = app.getExternalFilesDir(Environment.DIRECTORY_MOVIES).toString() + "/webshare.mp4"
        val file = File(path)
        file.delete()
        FFmpeg.cancel();
        FFmpeg.executeAsync("-i " + link + " -acodec mp3 -vcodec copy " + path, object : ExecuteCallback {
            override fun apply(executionId: Long, returnCode: Int) {
                if (returnCode == RETURN_CODE_SUCCESS) {
                    Log.i(Config.TAG, "Async command execution completed successfully.");
                } else if (returnCode == RETURN_CODE_CANCEL) {
                    Log.i(Config.TAG, "Async command execution cancelled by user.");
                } else {
                    Log.i(Config.TAG, String.format("Async command execution failed with returnCode=%d.", returnCode));
                }
            }
        })
        return path
        //TODO subor treba z lokalneho serveru poslať castu (bude fungovat aj ked bude transcodenuta iba cast?)
        //TODO iba pre nefunkcne zvukove codec-y napr EAC3
    }

    private fun setMovieMetaData(movie: SccData): MediaMetadata {
        val movieMetadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE)
        movieMetadata.putString(MediaMetadata.KEY_TITLE, movie._source.info_labels.originaltitle)
        //TODO dorobit nazov casti pre serial
//        movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, movie._source.info_labels.originaltitle)
        //TODO obrazok asi nebude fungovat... v niektorych pripadoch pada cast receiver
//        movie._source.i18n_info_labels.get(0).art?.poster?.apply {
//            movieMetadata.addImage(WebImage(Uri.parse(this)))
//        }
        //TODO dorbit parent obrazok aj pre cast serialu
        //movieMetadata.addImage(WebImage(Uri.parse(movie._source?.i18n_info_labels?.get(0)?.art?.poster)))
        return movieMetadata
    }

    fun play(
        movie: SccData,
        link: String,
    ) = loadRemoteMedia(0, movie, link)

}