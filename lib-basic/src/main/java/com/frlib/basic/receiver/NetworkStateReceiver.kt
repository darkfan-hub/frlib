package com.frlib.basic.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.text.style.TtsSpan
import com.frlib.utils.network.NetworkUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 14/06/2021 23:43
 * @desc 网络状态receiver
 */
class NetworkStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (ConnectivityManager.EXTRA_NO_CONNECTIVITY == intent.action) {
            CoroutineScope(Dispatchers.IO).launch {
                val networkType = NetworkUtil.networkType(context)
                Timber.i(networkType.getValue())
            }
        }
    }
}