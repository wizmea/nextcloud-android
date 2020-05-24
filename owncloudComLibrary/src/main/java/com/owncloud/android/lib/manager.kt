package com.owncloud.android.lib

import android.net.Uri
import android.os.Handler
import com.owncloud.android.lib.common.OwnCloudClient
import com.owncloud.android.lib.common.authentication.OwnCloudCredentialsFactory
import com.owncloud.android.lib.common.network.OnDatatransferProgressListener
import com.owncloud.android.lib.common.operations.OnRemoteOperationListener
import com.owncloud.android.lib.resources.files.CreateRemoteFolderOperation
import com.owncloud.android.lib.resources.files.FileUtils
import com.owncloud.android.lib.resources.files.ReadRemoteFolderOperation
import com.owncloud.android.lib.resources.files.UploadRemoteFileOperation
import java.io.File

private fun client(): OwnCloudClient {
    val serverUri = Uri.parse("https://nextcloud.opensi.co")
    val client = OwnCloudClient(serverUri)
    client.clearCredentials()
    client.credentials = OwnCloudCredentialsFactory.newBasicCredentials("arcadius.tchokpodo", "azertyuiop")
    return client
}



fun startReadRootFolder(listener: OnRemoteOperationListener, mHandler: Handler) {
    val refreshOperation = ReadRemoteFolderOperation(FileUtils.PATH_SEPARATOR + "testeur/")
    // root folder
    refreshOperation.execute(client(), listener, mHandler)
}

/**
 * @param: newFolderPath --> /comite/souscommite/
 */
fun createCloudFolder(newFolderPath: String,listener: OnRemoteOperationListener, mHandler: Handler) {
    val createOperation = CreateRemoteFolderOperation(newFolderPath, false)
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