package com.frlib.utils.network

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 17/06/2021 17:03
 * @desc 网络状态改变监听
 */
interface INetworkStateChangeListener {

    fun onConnected(networkType: NetworkType)

    fun onDisconnected()
}