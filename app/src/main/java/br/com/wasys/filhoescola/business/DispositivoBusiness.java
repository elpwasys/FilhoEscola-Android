package br.com.wasys.filhoescola.business;

import android.content.Context;

import br.com.wasys.filhoescola.endpoint.DispositivoEndpoint;
import br.com.wasys.filhoescola.endpoint.Endpoint;
import br.com.wasys.filhoescola.model.DispositivoModel;
import io.realm.Realm;
import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by bruno on 09/07/17.
 */

public class DispositivoBusiness extends Business {

    public DispositivoBusiness(Context context) {
        super(context);
    }

    public Observable<DispositivoModel> confirmar(DispositivoModel dispositivoModel) {
        return Observable.create(new AuthenticateHandler(dispositivoModel));
    }

    private class AuthenticateHandler implements Observable.OnSubscribe<DispositivoModel> {
        private DispositivoModel dispositivoModel;
        public AuthenticateHandler(DispositivoModel dispositivoModel) {
            this.dispositivoModel = dispositivoModel;
        }
        @Override
        public void call(Subscriber<? super DispositivoModel> subscriber) {
            try {
                DispositivoEndpoint endpoint = Endpoint.create(DispositivoEndpoint.class);
                Call<DispositivoModel> call = endpoint.confirmar(dispositivoModel);
                DispositivoModel  model = Endpoint.execute(call);
                subscriber.onNext(model);
                subscriber.onCompleted();
            } catch (Throwable e) {
                subscriber.onError(e);
            } finally {
            }
        }
    }
}
