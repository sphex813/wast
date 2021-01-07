package com.example.websharecaster.cast

import android.util.Log
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.Session
import com.google.android.gms.cast.framework.SessionManagerListener

class SessionListener : SessionManagerListener<Session> {
    override fun onSessionStarting(p0: Session?) {
        Log.d("SessionListener", "onSessionStarting")
    }

    override fun onSessionStarted(p0: Session?, p1: String?) {
        Log.d("SessionListener", "onSessionStarted")
    }

    override fun onSessionStartFailed(p0: Session?, p1: Int) {
        Log.d("SessionListener", "onSessionStartFailed")
    }

    override fun onSessionEnding(p0: Session?) {
        Log.d("SessionListener", "onSessionEnding")
    }

    override fun onSessionEnded(p0: Session?, p1: Int) {
        Log.d("SessionListener", "onSessionEnded")
    }

    override fun onSessionResuming(p0: Session?, p1: String?) {
        Log.d("SessionListener", "onSessionResuming")
    }

    override fun onSessionResumed(p0: Session?, p1: Boolean) {
        Log.d("SessionListener", "onSessionResumed")
    }

    override fun onSessionResumeFailed(p0: Session?, p1: Int) {
        Log.d("SessionListener", "onSessionResumeFailed")
    }

    override fun onSessionSuspended(p0: Session?, p1: Int) {
        Log.d("SessionListener", "onSessionSuspended")
    }

}