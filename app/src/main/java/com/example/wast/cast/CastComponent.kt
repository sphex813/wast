package com.example.wast.cast

import android.app.Application
import android.util.Log
import com.example.wast.api.models.SccData
import com.example.wast.datastore.LocalStorage
import com.example.wast.datastore.PreferenceKeys
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadRequestData
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.MediaQueueItem
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.google.android.gms.common.api.ResultCallbacks
import com.google.android.gms.common.api.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class CastComponent : KoinComponent {
    private val app: Application by inject()
    private val localStorage: LocalStorage by inject()
    private lateinit var castContext: CastContext
    private var castSuccessListener: CastSuccessListener? = null

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
                    CoroutineScope(Dispatchers.IO).launch {
                        localStorage.addToList(PreferenceKeys.WATCHED, movie._id, castSuccessListener!!::castSuccessfull)
                    }
                    Log.d("cast", "on Success")
                }

                override fun onFailure(p0: Status) {
                    Log.d("cast", "on Failure")
                }
            })
    }
//    }


    private fun buildMediaInfo(movie: SccData, link: String): MediaInfo? {
//        val link: String = generateFileWithSound(app, link)
        val movieMetadata = setMovieMetaData(movie)

        return MediaInfo.Builder(link)
            .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
            .setContentType("videos/mp4") //TODO pridat do Constants
            .setMetadata(movieMetadata)
            .setStreamDuration(MediaInfo.UNKNOWN_DURATION)
            .build()


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

    fun registerCastSuccessListener(castSuccessListener: CastSuccessListener) {
        this.castSuccessListener = castSuccessListener
    }

}