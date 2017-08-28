package br.com.wasys.filhoescola.firebase;

import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import br.com.wasys.filhoescola.model.DispositivoModel;
import br.com.wasys.filhoescola.service.DispositivoService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by pascke on 24/08/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private Looper mLooper;
    private HandlerThread mHandlerThread;

    @Override
    public void onTokenRefresh() {
        String pushToken = FirebaseInstanceId.getInstance().getToken();
        Observable<DispositivoModel> observable = DispositivoService.Async.atualizar(pushToken);
        observable
                .subscribeOn(AndroidSchedulers.from(getLooper()))
                .subscribe();
    }

    private String getTag() {
        return getClass().getSimpleName();
    }

    private Looper getLooper() {
        if (mLooper == null) {
            if (mHandlerThread == null) {
                mHandlerThread = new HandlerThread(getTag(), Process.THREAD_PRIORITY_BACKGROUND);
                mHandlerThread.start();
            }
            mLooper = mHandlerThread.getLooper();
        }
        return mLooper;
    }

    @Override
    public void onDestroy() {
        if (mHandlerThread != null) {
            mHandlerThread.quit();
        }
        super.onDestroy();
    }
}
