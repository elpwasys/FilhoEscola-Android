package br.com.wasys.filhoescola.utils;

import android.content.Context;
import android.os.Build;
import android.webkit.MimeTypeMap;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import br.com.wasys.filhoescola.FilhoNaEscolaApplication;
import br.com.wasys.library.enumerator.DeviceHeader;
import br.com.wasys.library.utils.AndroidUtils;
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
            return new WebResourceResponse(
                    getMimeType(url), // set content-type
                    response.header("content-encoding", "utf-8"),
                    response.body().byteStream()
            );
        } catch (IOException e) {
            return null;
        }
    }

    public String getMimeType(String url) {
        String type = null;
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
            }
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
}
