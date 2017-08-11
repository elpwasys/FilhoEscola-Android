package br.com.wasys.filhoescola.utils;

import android.content.Context;
import android.os.Build;
import android.preference.PreferenceActivity;
import android.util.Base64;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Map;
import java.util.Set;

import br.com.wasys.filhoescola.FilhoNaEscolaApplication;
import br.com.wasys.filhoescola.realm.Cache;
import br.com.wasys.library.enumerator.DeviceHeader;
import br.com.wasys.library.utils.AndroidUtils;
import io.realm.Realm;
import okhttp3.Headers;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by bruno on 16/07/17.
 */

public class WebViewClient extends android.webkit.WebViewClient {
    public Context context;
    private Cache cache;

    public WebViewClient(Context context) {
        this.context = context;
    }


    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        try {
            Realm realm = Realm.getDefaultInstance();

            cache = Realm.getDefaultInstance().where(Cache.class).like("url", url).findFirst();

            if(cache == null) {
                if(getMimeType(url) == null) {
                    Log.d("request",url);
                    return super.shouldInterceptRequest(view,url);
                }else{

                    OkHttpClient httpClient = new OkHttpClient.Builder().build();
                    Request request = new Request.Builder()
                            .url(url.trim())
                            .addHeader(DeviceHeader.DEVICE_SO.key, "Android")
                            .addHeader(DeviceHeader.DEVICE_SO_VERSION.key, Build.VERSION.RELEASE)
                            .addHeader(DeviceHeader.DEVICE_MODEL.key, Build.MODEL)
                            .addHeader(DeviceHeader.DEVICE_IMEI.key, AndroidUtils.getIMEI(context))
                            .addHeader(DeviceHeader.DEVICE_WIDTH.key, String.valueOf(AndroidUtils.getWidthPixels(context)))
                            .addHeader(DeviceHeader.DEVICE_HEIGHT.key, String.valueOf(AndroidUtils.getHeightPixels(context)))
                            .addHeader(DeviceHeader.DEVICE_APP_VERSION.key, String.valueOf(AndroidUtils.getVersionCode(context)))
                            .addHeader(DeviceHeader.DEVICE_TOKEN.key, FilhoNaEscolaApplication.getAuthorization())
                            .build();
                    Response response = httpClient.newCall(request).execute();
                    cache = new Cache();
                    cache.setData(response.body().bytes());
                    cache.setUrl(url);
                    cache.setEnconding(response.header("content-encoding", "utf-8"));
                    cache.setMimeType(getMimeType(url));

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            try {
                                Cache cacheRealm = realm.createObject(Cache.class);
                                cacheRealm.setData(cache.getData());
                                cacheRealm.setEnconding(cache.getEnconding());
                                cacheRealm.setMimeType(cache.getMimeType());
                                cacheRealm.setUrl(cache.getUrl());
                                Log.d("request-cache-save",cache.getUrl() );
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
            Log.d("request-cache",url);
            return new WebResourceResponse(
                    cache.getMimeType(),
                    cache.getEnconding(),
                    new ByteArrayInputStream(cache.getData())
            );

        } catch (IOException e) {
            return null;
        }
    }

    public String getMimeType(String url) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            if (extension.equals("js")) {
                return "text/javascript";
            } else if (extension.equals("woff")) {
                return "application/font-woff";
            } else if (extension.equals("woff2")) {
                return "application/font-woff2";
            } else if (extension.equals("ttf")) {
                return "application/x-font-ttf";
            } else if (extension.equals("eot")) {
                return "application/vnd.ms-fontobject";
            } else if (extension.equals("svg")) {
                return "image/svg+xml";
            } else if (extension.equals("css")) {
                return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            } else if (extension.contains("png")){
                return "image/png";
            } else if (extension.contains("jpg")){
                return "image/jpeg";
            }else {
                return null;
            }
        }else {
            return null;
        }
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        injectScriptFile(view, "device.js");
    }

    private void injectScriptFile(WebView view, String scriptFile) {
        InputStream input;
        try {
            input = context.getAssets().open(scriptFile);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            input.close();

            // String-ify the script byte-array using BASE64 encoding !!!
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            view.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var script = document.createElement('script');" +
                    "script.type = 'text/javascript';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "script.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(script)" +
                    "})()");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
