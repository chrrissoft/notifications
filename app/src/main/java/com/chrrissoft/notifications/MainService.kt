package com.chrrissoft.notifications

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
