package com.frlib.basic.mobleinfo

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import com.frlib.basic.mobleinfo.entity.PackageInfoEntity
import com.frlib.utils.EncryptUtil
import com.frlib.utils.SysUtil
import com.frlib.utils.ext.invalid

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 24/02/2021 20:55
 * @desc app信息
 */
object AppInfoHelper {

    fun packageInfo(context: Context): PackageInfoEntity {
        val packageManager = context.packageManager
        val applicationInfo = context.applicationInfo
        val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
        val signatures = signatures(context, packageManager)
        val packageSign = if (signatures != null) signatures?.get(0) else ""
        return PackageInfoEntity(
            packageManager.getApplicationLabel(applicationInfo).toString(),
            packageInfo.firstInstallTime,
            packageInfo.lastUpdateTime,
            packageInfo.packageName,
            packageSign.invalid(),
            packageInfo.versionName,
            if (SysUtil.isAndroid9()) packageInfo.longVersionCode else packageInfo.versionCode.toLong(),
            applicationInfo.targetSdkVersion,
            if (SysUtil.isAndroid7()) applicationInfo.minSdkVersion else 0,
            applicationInfo.loadIcon(packageManager)
        )
    }

    private fun signatures(context: Context, packageManager: PackageManager): List<String?>? {
        return if (SysUtil.isAndroid9()) {
            val packageInfo =
                packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            if (packageInfo?.signingInfo != null) {
                if (packageInfo.signingInfo.hasMultipleSigners()) {
                    signatureDigest(packageInfo.signingInfo.apkContentsSigners)
                } else {
                    signatureDigest(packageInfo.signingInfo.signingCertificateHistory)
                }
            } else {
                null
            }
        } else {
            val packageInfo = packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
            if (packageInfo != null && packageInfo.signatures.isNotEmpty()) {
                signatureDigest(packageInfo.signatures)
            } else {
                null
            }
        }
    }

    private fun signatureDigest(sigList: Array<Signature>): List<String?> {
        val signaturesList: MutableList<String?> = ArrayList()
        for (signature in sigList) {
            signaturesList.add(EncryptUtil.encryptMD5ToString(signature.toByteArray()))
        }
        return signaturesList
    }
}