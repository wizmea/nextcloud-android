/* ownCloud Android Library is available under MIT license
 *   Copyright (C) 2020 ownCloud GmbH.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *   EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *   NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 *   BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 *   ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *   CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 *
 */

package com.owncloud.android.lib.sampleclient;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.owncloud.android.lib.common.OwnCloudAccount;
import com.owncloud.android.lib.common.OwnCloudClient;
import com.owncloud.android.lib.common.OwnCloudClientFactory;
import com.owncloud.android.lib.common.SingleSessionManager;
import com.owncloud.android.lib.common.authentication.OwnCloudCredentialsFactory;
import com.owncloud.android.lib.common.network.OnDatatransferProgressListener;
import com.owncloud.android.lib.common.operations.OnRemoteOperationListener;
import com.owncloud.android.lib.common.operations.RemoteOperation;
import com.owncloud.android.lib.common.operations.RemoteOperationResult;
import com.owncloud.android.lib.resources.files.CreateRemoteFolderOperation;
import com.owncloud.android.lib.resources.files.DownloadRemoteFileOperation;
import com.owncloud.android.lib.resources.files.FileUtils;
import com.owncloud.android.lib.resources.files.ReadRemoteFolderOperation;
import com.owncloud.android.lib.resources.files.RemoteFile;
import com.owncloud.android.lib.resources.files.RemoveRemoteFileOperation;
import com.owncloud.android.lib.resources.files.UploadRemoteFileOperation;
import info.hannes.timber.DebugTree;
import timber.log.Timber;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.owncloud.android.lib.ManagerKt.createCloudFolder;
import static com.owncloud.android.lib.ManagerKt.startReadRootFolder;
import static com.owncloud.android.lib.ManagerKt.uploadFileOnCloud;

public class MainActivity extends Activity {

    private Handler mHandler;
    private OwnCloudClient mClient, client;
    private FilesArrayAdapter mFilesAdapter;
    private View mFrame;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findViewById(R.id.button_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Read", Toast.LENGTH_LONG).show();
                startReadRootFolder(MainActivity.this, (caller, result) -> {
                    Timber.tag("hello").e(result.getException());
                    Timber.tag("hello").e(result.getHttpPhrase());
                    Timber.tag("hello").e(result.getCode().name());
                    if (result.getData() != null) {
                        List<RemoteFile> files = (List<RemoteFile>) result.getData();
                        Timber.tag("hello").e(result.getData().toString());
                        Timber.tag("hello").e(files.get(0).getRemotePath());
                    }

                }, new Handler());
            }
        });

        findViewById(R.id.button_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testCreateAndUpload();
            }
        });
    }

    private void testCreateAndUpload() {
        Toast.makeText(this,"Launch", Toast.LENGTH_SHORT).show();


        createCloudFolder("/arcadiusTest/", (caller, result) -> {
            Timber.tag("hello").e(result.getHttpPhrase());
        }, new Handler());
        createCloudFolder("/arcadiusTest/communications/", (caller, result) -> {

        }, new Handler());
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard,"/Download/1532.pdf");
        uploadFileOnCloud(file,"/arcadius/fichier4.pdf","application/pdf",(caller, result) -> {

        }, new Handler(), (read, transferred, percent, absolutePath) -> {

        });
    }

}