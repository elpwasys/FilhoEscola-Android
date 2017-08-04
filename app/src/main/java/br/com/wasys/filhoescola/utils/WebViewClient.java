package br.com.wasys.filhoescola.utils;

import android.content.Context;
import android.os.Build;
import android.util.Base64;
import android.webkit.MimeTypeMap;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import br.com.wasys.filhoescola.FilhoNaEscolaApplication;
import br.com.wasys.filhoescola.realm.Cache;
import br.com.wasys.library.enumerator.DeviceHeader;
import br.com.wasys.library.utils.AndroidUtils;
import io.realm.Realm;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by bruno on 16/07/17.
 */

public class WebViewClient extends android.webkit.WebViewClient {
    public Context context;

    public WebViewClient(Context context) {
        this.context = context;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        try {
            Realm realm = Realm.getDefaultInstance();

            Cache cache;

            cache = Realm.getDefaultInstance().where(Cache.class).like("url", url).findFirst();

            if(cache == null) {

                OkHttpClient httpClient = new OkHttpClient();
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

                realm.beginTransaction();
                cache = realm.createObject(Cache.class);
                cache.setData(response.body().bytes());
                cache.setUrl(url);
                cache.setEnconding(response.header("content-encoding", "utf-8"));
                cache.setMimeType(response.header("Content-Type").replace(";charset=UTF-8", ""));
                realm.commitTransaction();
            }

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
            } else if (extension.contains(".xhtml")){
                return "application/xhtml+xml";
            } else {
                return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
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
