package br.com.wasys.filhoescola.firebase;


import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import br.com.wasys.filhoescola.business.DispositivoBusiness;
import br.com.wasys.filhoescola.model.DispositivoModel;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by bruno on 19/08/17.
 */

public class FilhoFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "FirebaseIIDService";
    private Looper mLooper;
    private HandlerThread mHandlerThread;

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }
    private void sendRegistrationToServer(String token) {

        DispositivoBusiness business = new DispositivoBusiness(this);
        Observable<DispositivoModel> observable = business.push(token);
        prepare(observable)
                .subscribe(new Subscriber<DispositivoModel>() {
                    @Override
                    public void onStart(){
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                    @Override
                    public void onNext(DispositivoModel dispositivoModel1) {
                        Log.d("DispositivoModel", dispositivoModel1.toString());

                    }
                });
    }
    public Observable prepare(Observable observable) {
        return observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.from(getLooper()));
    }
    protected Looper getLooper() {
        if (mLooper == null) {
            if (mHandlerThread == null) {
                mHandlerThread = new HandlerThread(TAG, Process.THREAD_PRIORITY_BACKGROUND);
                mHandlerThread.start();
            }
            mLooper = mHandlerThread.getLooper();
        }
        return mLooper;
    }
}