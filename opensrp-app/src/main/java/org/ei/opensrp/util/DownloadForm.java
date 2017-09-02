package org.ei.opensrp.util;

import android.util.Log;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.HttpStatus;
import org.ei.opensrp.domain.DownloadStatus;
import org.ei.opensrp.domain.Response;
import org.ei.opensrp.domain.ResponseStatus;
import org.ei.opensrp.service.FormPathService;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Dimas Ciputra on 3/21/15.
 */
public class DownloadForm {
    private static int status;
    private static File f;
    public static Response<DownloadStatus> DownloadFromURL(String downloadURL,
                                                           String fileName,
                                                           final SyncHttpClient httpClient)
    {

        try {

            File dir = new File(FormPathService.sdcardPathDownload);

            if(!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, fileName);

            long startTime = System.currentTimeMillis();
            Log.d("DownloadFormService", "download begin");
            Log.d("DownloadFormService", "download url: " + downloadURL.toString());
            Log.d("DownloadFormService", "download file name: " + fileName);


            httpClient.get(downloadURL, new FileAsyncHttpResponseHandler(file) {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                    status = statusCode;
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, File file) {
                    status = statusCode;
                    f=file;
                }
            });

            /* expect HTTP 200 OK */
            if (status != HttpStatus.SC_OK) {
                Log.d("DownloadFormService", "Server returned HTTP " + status);
                return new Response<DownloadStatus>(ResponseStatus.failure, DownloadStatus.failedDownloaded);
            }

            if(f == null || !f.exists()) {
                return new Response<DownloadStatus>(ResponseStatus.success, DownloadStatus.nothingDownloaded);
            }


        } catch (Exception e) {
            Log.d("DownloadFormService", "download error : " + e);
            return new Response<DownloadStatus>(ResponseStatus.success, DownloadStatus.failedDownloaded);
        }

        return new Response<DownloadStatus>(ResponseStatus.success, DownloadStatus.downloaded);
    }

}