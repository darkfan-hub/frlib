package com.frlib.basic.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.frlib.basic.app.AppManager
import com.frlib.basic.app.IAppComponent
import timber.log.Timber

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 26/05/2021 16:09
 * @desc fragment 生命周期回调
 */
class FragmentLifecycle(
    private val appComponent: IAppComponent
) : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        super.onFragmentAttached(fm, f, context)
        Timber.i("${f.javaClass.simpleName} Attached!")
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        super.onFragmentCreated(fm, f, savedInstanceState)
        Timber.i("${f.javaClass.simpleName} Created!")
        if (f is IFragment && !f.isAdded) {
            (f as IFragment).setupActivityComponent(appComponent)

            AppManager.addNetworkStateChangeListener(f.networkChangeListener())
        }
    }

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState)
        Timber.i("${f.javaClass.simpleName} ViewCreated!")
    }

    override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        super.onFragmentActivityCreated(fm, f, savedInstanceState)
        Timber.i("${f.javaClass.simpleName} ActivityCreated!")
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        super.onFragmentStarted(fm, f)
        Timber.i("${f.javaClass.simpleName} Started!")
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        super.onFragmentResumed(fm, f)
        Timber.i("${f.javaClass.simpleName} Resumed!")
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        super.onFragmentPaused(fm, f)
        Timber.i("${f.javaClass.simpleName} Paused!")
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        super.onFragmentStopped(fm, f)
        Timber.i("${f.javaClass.simpleName} Stopped!")
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        super.onFragmentDetached(fm, f)
        Timber.i("${f.javaClass.simpleName} Detached!")
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentDestroyed(fm, f)
        Timber.i("${f.javaClass.simpleName} Destroyed!")
        if (f is IFragment) {
            AppManager.removeNetworkStateChangeListener(f.networkChangeListener())
        }
    }
}