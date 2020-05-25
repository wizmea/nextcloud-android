package com.owncloud.android.lib

import android.accounts.Account
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Handler
import androidx.browser.customtabs.CustomTabsClient.getPackageName
import com.owncloud.android.lib.common.OwnCloudAccount
import com.owncloud.android.lib.common.OwnCloudClient
import com.owncloud.android.lib.common.OwnCloudClientFactory
import com.owncloud.android.lib.common.SingleSessionManager
import com.owncloud.android.lib.common.authentication.OwnCloudCredentialsFactory
import com.owncloud.android.lib.common.network.OnDatatransferProgressListener
import com.owncloud.android.lib.common.operations.OnRemoteOperationListener
import com.owncloud.android.lib.resources.files.CreateRemoteFolderOperation
import com.owncloud.android.lib.resources.files.FileUtils
import com.owncloud.android.lib.resources.files.ReadRemoteFolderOperation
import com.owncloud.android.lib.resources.files.UploadRemoteFileOperation
import timber.log.Timber
import java.io.File

fun getUserAgent(): String? {
    val appString: String = "ownCloud Sample Client"
    val version = "1.0"
    // Mozilla/5.0 (Android) ownCloud-android/1.7.0
    return String.format(appString, version)
}

private fun client(context: Context? = null): OwnCloudClient {
    val serverUri = Uri.parse("https://nextcloud.opensi.co")
    SingleSessionManager.setUserAgent(getUserAgent())
    var client = OwnCloudClient(serverUri)
    if (context != null) {
        client = OwnCloudClientFactory.createOwnCloudClient(serverUri, context, true);
        //client.account = OwnCloudAccount(Uri.parse("https://nextcloud.opensi.co"),OwnCloudCredentialsFactory.newBasicCredentials("arcadius.tchokpodo", "azertyuiop"))
    }
    client.clearCredentials()
    client.credentials = OwnCloudCredentialsFactory.newBasicCredentials("arcadius.tchokpodo", "azertyuiop")
    return client
}



fun startReadRootFolder(context: Context, listener: OnRemoteOperationListener, mHandler: Handler) {
    val refreshOperation = ReadRemoteFolderOperation(FileUtils.PATH_SEPARATOR + "arcadius/")
    // root folder
    refreshOperation.execute(client(context), listener, mHandler)
}

/**
 * @param: newFolderPath --> /comite/souscommite/
 */
fun createCloudFolder(newFolderPath: String,listener: OnRemoteOperationListener, mHandler: Handler) {
    val createOperation = CreateRemoteFolderOperation(newFolderPath, true)
    createOperation.execute(client(), listener, mHandler)
}

/**
 * @param: remotePath --> /comite/souscommite/communication1.pdf
 */
fun uploadFileOnCloud(fileToUpload: File, remotePath: String, mimeType: String, listener: OnRemoteOperationListener, mHandler: Handler, progressListener: OnDatatransferProgressListener) {
    val timeStampLong = fileToUpload.lastModified() / 1000
    val timeStamp = timeStampLong.toString()
    val uploadOperation = UploadRemoteFileOperation(fileToUpload.absolutePath, remotePath, mimeType, timeStamp)
    uploadOperation.addDatatransferProgressListener(progressListener)
    uploadOperation.execute(client(), listener, mHandler)
}