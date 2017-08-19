package firebase;


import android.content.Intent;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import br.com.wasys.filhoescola.FilhoNaEscolaApplication;
import br.com.wasys.filhoescola.activity.AguardeSMSActivity;
import br.com.wasys.filhoescola.activity.CadastroActivity;
import br.com.wasys.filhoescola.business.DispositivoBusiness;
import br.com.wasys.filhoescola.model.DispositivoModel;
import br.com.wasys.library.widget.Progress;
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
        // TODO: Implement this method to send token to your app server.
        DispositivoBusiness business = new DispositivoBusiness(this);
        Observable<Boolean> observable = business.push(token);
        prepare(observable)
                .subscribe(new Subscriber<Boolean>() {
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
                    public void onNext(Boolean dispositivoModel1) {

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