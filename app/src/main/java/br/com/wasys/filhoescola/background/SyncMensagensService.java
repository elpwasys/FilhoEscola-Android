package br.com.wasys.filhoescola.background;

import android.app.Service;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import br.com.wasys.filhoescola.service.MensagemService;
import br.com.wasys.filhoescola.enumeradores.StatusMensagemSincronizacao;
import br.com.wasys.filhoescola.model.SuccessModel;
import br.com.wasys.filhoescola.realm.Mensagem;
import io.realm.Realm;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class SyncMensagensService extends Service {

    private static final String TAG = "SyncMensagensService";
    private Looper mLooper;
    private HandlerThread mHandlerThread;

    public SyncMensagensService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return onBind(intent);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {
        enviaMensagensLidas();
        return START_STICKY;
    }


    public void enviaMensagensLidas(){
        Realm realm = Realm.getDefaultInstance();
        Mensagem mensagens = realm.where(Mensagem.class).equalTo("lida",true).equalTo("status",StatusMensagemSincronizacao.AGUARDANDO.toString()).findFirst();
        if(mensagens != null) {
            realm.beginTransaction();
            mensagens.setStatus(StatusMensagemSincronizacao.ENVIADO);
            realm.commitTransaction();
            MensagemService business = new MensagemService();
            Observable<SuccessModel> observable = business.getSync(mensagens.getId());
            prepare(observable)
                    .subscribe(new Subscriber<SuccessModel>() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(SuccessModel bol) {
                            Log.d("MsgSync", bol.success.toString());
                            enviaMensagensLidas();
                        }
                    });
        }
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
