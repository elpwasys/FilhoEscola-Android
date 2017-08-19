package firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.activity.MensagensAlunoActivity;
import br.com.wasys.filhoescola.activity.SplashScreenActivity;
import br.com.wasys.filhoescola.business.MensagemBusiness;
import br.com.wasys.filhoescola.enumeradores.Assunto;
import br.com.wasys.filhoescola.realm.Mensagem;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by bruno on 19/08/17.
 */

public class FilhoFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMService";
    private Looper mLooper;
    private HandlerThread mHandlerThread;

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            MensagemBusiness business = new MensagemBusiness(this);
            Observable<Long> observable = business.getMensagem(remoteMessage.getData().get("id"));
            prepare(observable)
                    .subscribe(new Subscriber<Long>() {
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
                        public void onNext(Long id) {
                            Log.d("MensagemId",id+"");
                            Intent intent = new Intent(getBaseContext(), MensagensAlunoActivity.class);
                            intent.putExtra("idMensagem",id);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent,
                                    PendingIntent.FLAG_ONE_SHOT);

                            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getBaseContext())
                                    .setSmallIcon(Assunto.getAssunto(remoteMessage.getData().get("type")).getImagem())
                                    .setContentText(remoteMessage.getNotification().getBody())
                                    .setContentTitle(remoteMessage.getNotification().getTitle())
                                    .setAutoCancel(true)
                                    .setSound(defaultSoundUri)
                                    .setContentIntent(pendingIntent);

                            NotificationManager notificationManager =
                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                            notificationManager.notify(0, notificationBuilder.build());
                        }
                    });


            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

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