package org.ei.opensrp.service;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.HttpStatus;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.AbstractVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.ei.opensrp.DristhiConfiguration;
import org.ei.opensrp.R;
import org.ei.opensrp.domain.DownloadStatus;
import org.ei.opensrp.domain.LoginResponse;
import org.ei.opensrp.domain.ProfileImage;
import org.ei.opensrp.domain.Response;
import org.ei.opensrp.domain.ResponseStatus;
import org.ei.opensrp.repository.AllSettings;
import org.ei.opensrp.repository.AllSharedPreferences;
import org.ei.opensrp.util.DownloadForm;

import java.io.File;
import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

import static org.ei.opensrp.domain.LoginResponse.NO_INTERNET_CONNECTIVITY;
import static org.ei.opensrp.domain.LoginResponse.SUCCESS;
import static org.ei.opensrp.domain.LoginResponse.UNAUTHORIZED;
import static org.ei.opensrp.domain.LoginResponse.UNKNOWN_RESPONSE;
import static org.ei.opensrp.util.Log.logError;
import static org.ei.opensrp.util.Log.logWarn;

public class HTTPAgent {
    private static final String TAG=HTTPAgent.class.getCanonicalName();
    private Context context;
    private AllSettings settings;
    private AllSharedPreferences allSharedPreferences;
    private DristhiConfiguration configuration;
    SyncHttpClient httpClient = new SyncHttpClient();


    public HTTPAgent(Context context, AllSettings settings, AllSharedPreferences allSharedPreferences, DristhiConfiguration configuration) {
        this.context = context;
        this.settings = settings;
        this.allSharedPreferences = allSharedPreferences;
        this.configuration = configuration;

        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, 30000);
        HttpConnectionParams.setSoTimeout(basicHttpParams, 60000);

        httpClient.setTimeout(60000);
    }

    private String responseContent;
    public Response<String> fetch(String requestURLPath) {
        Log.d(TAG,"url "+requestURLPath);
        responseContent=null;
        try {
            setCredentials(allSharedPreferences.fetchRegisteredANM(), settings.fetchANMPassword());
            Log.d(TAG,"username - "+allSharedPreferences.fetchRegisteredANM());
            Log.d(TAG,"password -  "+settings.fetchANMPassword());
            httpClient.get(requestURLPath, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    throwable.printStackTrace();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    responseContent = responseString;
                }
            });


            return new Response<String>(ResponseStatus.success, responseContent);
        } catch (Exception e) {
            return new Response<String>(ResponseStatus.failure, null);
        }
    }

    private ResponseStatus r;
    public Response<String> post(String postURLPath, String jsonPayload) {
        try {
            responseContent = null;
            setCredentials(allSharedPreferences.fetchRegisteredANM(), settings.fetchANMPassword());


            ByteArrayEntity entity = new ByteArrayEntity(jsonPayload.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            httpClient.post(context, postURLPath, entity, "application/json",
                    new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            throwable.printStackTrace();
                            r = ResponseStatus.failure;
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            responseContent = responseString;
                            r = ResponseStatus.success;

                        }
                    });



            return new Response<String>(r, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<String>(ResponseStatus.failure, null);
        }
    }


    private int status;
    public LoginResponse urlCanBeAccessWithGivenCredentials(String requestURL, String userName, String password) {
        Log.d(TAG,"username = "+userName);
        Log.d(TAG,"password = "+password);
        httpClient.setBasicAuth(userName,password);
        responseContent=null;

        try {
            requestURL=requestURL.replaceAll("\\s+","");

            httpClient.get(requestURL, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    status=statusCode;
                    throwable.printStackTrace();
                    Log.d(TAG,"response string = "+responseString);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    status=statusCode;
                    responseContent = responseString;

                }
            });



            if (status == HttpStatus.SC_OK) {
                return SUCCESS.withPayload(responseContent);
            } else if (status == HttpStatus.SC_UNAUTHORIZED) {
                logError("Invalid credentials for: " + userName + " using " + requestURL);
                return UNAUTHORIZED;
            } else {
                logError("Bad response from Dristhi. Status code:  " + status + " username: " + userName + " using " + requestURL);
                return UNKNOWN_RESPONSE;
            }
        }catch (Exception e) {
            logError("Failed to check credentials of: " + userName + " using " + requestURL + ". Error: " + e.toString());
            return NO_INTERNET_CONNECTIVITY;
        }
    }

    public DownloadStatus downloadFromUrl(String url, String filename) {
        setCredentials(allSharedPreferences.fetchRegisteredANM(), settings.fetchANMPassword());
        Response<DownloadStatus> status = DownloadForm.DownloadFromURL(url, filename, httpClient);
        return status.payload();
    }




    private void setCredentials(String userName, String password) {
        Log.d(TAG,"username = "+userName);
        Log.d(TAG,"password = "+password);
        httpClient.setBasicAuth(userName,password);
    }

    private SocketFactory sslSocketFactoryWithopensrpCertificate() {
        try {
            KeyStore trustedKeystore = KeyStore.getInstance("BKS");
            InputStream inputStream = context.getResources().openRawResource(R.raw.dristhi_client);
            try {
                trustedKeystore.load(inputStream, "phone red pen".toCharArray());
            } finally {
                inputStream.close();
            }
            SSLSocketFactory socketFactory = new SSLSocketFactory(trustedKeystore);
            final X509HostnameVerifier oldVerifier = socketFactory.getHostnameVerifier();
            socketFactory.setHostnameVerifier(new AbstractVerifier() {
                @Override
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                    for (String cn : cns) {
                        if (!configuration.shouldVerifyCertificate() || host.equals(cn)) {
                            return;
                        }
                    }
                    oldVerifier.verify(host, cns, subjectAlts);
                }
            });
            return socketFactory;
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
    public Response<String> fetchWithCredentials(String uri, String username, String password) {
        setCredentials(username, password);
        return fetch(uri);
    }


    private String responseS= null;
    private int code;
    public String httpImagePost(String url,ProfileImage image){


        try {
            File uploadFile = new File(image.getFilepath());
            if(uploadFile.exists()) {
                setCredentials(allSharedPreferences.fetchRegisteredANM(), settings.fetchANMPassword());



                RequestParams params = new RequestParams();
                try {
                    params.put("file", uploadFile);
                    params.put("anm-id", image.getAnmId());
                    params.put("entity-id", image.getEntityID());
                    params.put("content-type", image.getContenttype() != null ? image.getContenttype() : "jpeg");
                    params.put("file-category", image.getFilecategory() != null ? image.getFilecategory() : "profilepic");

                } catch(Exception e){
                    e.printStackTrace();
                }

                httpClient.post(url, params, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        code = statusCode;
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        responseS=responseString;
                        code = statusCode;
                    }
                });


                Log.v("file to upload", "" + uploadFile.length());

                Log.v("response so many", responseS);
                int RESPONSE_OK = 200;
                int RESPONSE_OK_ = 201;

                if (code != RESPONSE_OK_ && code!= RESPONSE_OK) {
                }
            }
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
        return responseS;
    }
}
