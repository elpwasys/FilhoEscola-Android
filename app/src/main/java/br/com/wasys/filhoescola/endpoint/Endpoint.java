package br.com.wasys.filhoescola.endpoint;

import android.content.Context;
import android.os.Build;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import br.com.wasys.filhoescola.BuildConfig;

import br.com.wasys.filhoescola.FilhoNaEscolaApplication;
import br.com.wasys.filhoescola.R;
import br.com.wasys.library.enumerator.DeviceHeader;
import br.com.wasys.library.enumerator.HttpStatus;
import br.com.wasys.library.exception.EndpointException;
import br.com.wasys.library.http.Error;
import br.com.wasys.library.utils.AndroidUtils;
import retrofit2.Call;

/**
 * Created by pascke on 03/08/16.
 */
public class Endpoint {

    public static final String BASE_URL = BuildConfig.BASE_URL + BuildConfig.BASE_CONTEXT_REST;

    public static <T> T create(Class<T> clazz) {
        return br.com.wasys.library.http.Endpoint.create(clazz, BASE_URL, getHeaders());
    }

    public static <T> T execute(Call<T> call) throws EndpointException {
        Context context = FilhoNaEscolaApplication.getContext();
        if (!AndroidUtils.isNetworkAvailable(context)) {
            Error error = new Error(HttpStatus.NOT_APPLY, context.getString(R.string.network_not_available));
            throw new EndpointException(error);
        }
        return br.com.wasys.library.http.Endpoint.execute(call);
    }
    public static Map<String, String> getHeaders(){
        Context context = FilhoNaEscolaApplication.getContext();
        Map<String, String> headers = new HashMap<>();
        headers.put(DeviceHeader.DEVICE_SO.key, "Android");
        headers.put(DeviceHeader.DEVICE_SO_VERSION.key, Build.VERSION.RELEASE);
        headers.put(DeviceHeader.DEVICE_MODEL.key, Build.MODEL);
        headers.put(DeviceHeader.DEVICE_IMEI.key, AndroidUtils.getIMEI(context));
        headers.put(DeviceHeader.DEVICE_WIDTH.key, String.valueOf(AndroidUtils.getWidthPixels(context)));
        headers.put(DeviceHeader.DEVICE_HEIGHT.key, String.valueOf(AndroidUtils.getHeightPixels(context)));
        headers.put(DeviceHeader.DEVICE_APP_VERSION.key, String.valueOf(AndroidUtils.getVersionCode(context)));
        String authorization = FilhoNaEscolaApplication.getAuthorization();
        if (StringUtils.isNotBlank(authorization)) {
            headers.put(DeviceHeader.AUTHORIZATION.key, authorization);
        }
        return headers;
    }
}
