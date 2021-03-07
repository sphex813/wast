package com.example.wast.cast

import android.app.Application
import android.util.Log
import com.example.wast.api.models.SccData
import com.example.wast.datastore.LocalStorage
import com.example.wast.datastore.PreferenceKeys
import com.example.wast.utils.HelpUtils
import com.google.android.gms.cast.*
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.google.android.gms.cast.framework.media.RemoteMediaClient.ProgressListener
import com.google.android.gms.common.api.ResultCallbacks
import com.google.android.gms.common.api.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class CastComponent : KoinComponent {
    private val app: Application by inject()
    private val localStorage: LocalStorage by inject()
    private lateinit var castContext: CastContext
    private var castPlayListener: CastPlayListener? = null

    fun setCastContext(cast: CastContext) {
        castContext = cast
        castContext.sessionManager.addSessionManagerListener(SessionListener())
    }

    private fun loadRemoteMedia(
        position: Int,
        parentMedia: SccData?,
        media: SccData,
        link: String,
    ) {
        if (castContext.sessionManager.currentCastSession == null) {
            return
        }

        val mediaQueueItem: MediaQueueItem = MediaQueueItem.Builder(buildMediaInfo(parentMedia, media, link)).build()

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
            .setMediaInfo(buildMediaInfo(parentMedia, media, link))
            .setAutoplay(true)
            .setCurrentTime(position.toLong()).build())
            .setResultCallback(object : ResultCallbacks<RemoteMediaClient.MediaChannelResult>() {
                override fun onSuccess(p0: RemoteMediaClient.MediaChannelResult) {
                    CoroutineScope(Dispatchers.IO).launch {
                        localStorage.addToList(PreferenceKeys.WATCHED, media._id, castPlayListener!!::addToWatched)
                    }
                    val title = remoteMediaClient.mediaInfo.metadata.getString(MediaMetadata.KEY_TITLE) ?: ""
                    val subTitle = remoteMediaClient.mediaInfo.metadata.getString(MediaMetadata.KEY_SUBTITLE) ?: ""
                    val image: String = (remoteMediaClient.mediaInfo.customData?.get("image") ?: "") as String
                    castPlayListener?.setPlayedMedia(title, subTitle, image);

                    Log.d("cast", "on Success")
                }

                override fun onFailure(p0: Status) {
                    Log.d("cast", "on Failure")
                }
            })
    }

    private fun buildMediaInfo(parentData: SccData?, mediaData: SccData, link: String): MediaInfo? {
//        val link: String = generateFileWithSound(app, link)
        val movieMetadata = setMovieMetaData(parentData, mediaData)

        val customMediaData = JSONObject().put("image", HelpUtils.getMovieImageLink(
            let {
                if (parentData !== null) {
                    parentData._source.i18n_info_labels
                } else {
                    mediaData._source.i18n_info_labels
                }
            }
        ))

        return MediaInfo.Builder(link)
            .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
            .setContentType("videos/mp4") //TODO pridat do Constants
            .setMetadata(movieMetadata)
            .setCustomData(customMediaData)
            .setStreamDuration(MediaInfo.UNKNOWN_DURATION)
            .build()
    }

    private fun setMovieMetaData(parentData: SccData?, mediaData: SccData): MediaMetadata {
        val movieMetadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE)
        if (parentData != null) {
            movieMetadata.putString(MediaMetadata.KEY_TITLE, HelpUtils.getTitle(parentData._source.info_labels.originaltitle,
                parentData._source.i18n_info_labels))
            movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, HelpUtils.getTitle(mediaData._source.info_labels.originaltitle,
                (mediaData._source.i18n_info_labels)))
        } else {
            movieMetadata.putString(MediaMetadata.KEY_TITLE, HelpUtils.getTitle(mediaData._source.info_labels.originaltitle,
                mediaData._source.i18n_info_labels))
        }

        return movieMetadata
    }

    fun play(
        parentMedia: SccData?,
        media: SccData,
        link: String,
        position: Int = 0,
    ) = loadRemoteMedia(position, parentMedia, media, link)

    fun registerCastSuccessListener(castPlayListener: CastPlayListener) {
        this.castPlayListener = castPlayListener
    }

    fun registerCastProgressListener(progressListener: ProgressListener) {
        castContext.sessionManager?.currentCastSession?.getRemoteMediaClient()?.addProgressListener(progressListener, 1000)
    }

    fun unregisterCastProgressListener(progressListener: ProgressListener) {
        castContext.sessionManager?.currentCastSession?.getRemoteMediaClient()?.removeProgressListener(progressListener)
    }

    fun getState(): Boolean {
        return castContext.sessionManager?.currentCastSession?.getRemoteMediaClient()?.isPlaying ?: false
    }

    fun pause() {
        castContext.sessionManager?.currentCastSession?.getRemoteMediaClient()?.pause()
    }

    fun play() {
        castContext.sessionManager?.currentCastSession?.getRemoteMediaClient()?.play()
    }

    fun seekToPosition(value: Int?) {
        if (value !== null) {
            castContext.sessionManager?.currentCastSession?.getRemoteMediaClient()
                ?.seek(MediaSeekOptions.Builder().setPosition(value.toLong()).build())
        }
    }
}