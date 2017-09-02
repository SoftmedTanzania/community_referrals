package org.ei.opensrp.client;

import com.loopj.android.http.HttpGet;

import org.apache.http.HttpResponse;
import java.io.IOException;
import org.ei.opensrp.util.HttpResponseUtil;

import java.io.IOException;
import java.io.InputStream;

import cz.msebera.android.httpclient.client.CredentialsProvider;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;

import static org.apache.http.HttpStatus.SC_OK;

public class GZipEncodingHttpClient {
    private HttpClient httpClient;

    public GZipEncodingHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

//    public InputStream fetchContent(HttpGet request) throws IOException {
//        if (!request.containsHeader("Accept-Encoding")) {
//            request.addHeader("Accept-Encoding", "gzip");
//        }
//
//        HttpResponse response = httpClient.execute(request);
//        if (response.getStatusLine().getStatusCode() != SC_OK) {
//            throw new IOException("Invalid status code: " + response.getStatusLine().getStatusCode());
//        }
//
//        return HttpResponseUtil.getResponseStream(response);
//    }
//
//    public HttpResponse execute(HttpGet request) throws IOException {
//        return httpClient.execute(request);
//    }
//
//    public CredentialsProvider getCredentialsProvider() {
//        return httpClient.getCredentialsProvider();
//    }
//
//    public HttpResponse postContent(HttpPost request) throws IOException {
//        HttpResponse response = httpClient.execute(request);
//        return response;
//    }
}
