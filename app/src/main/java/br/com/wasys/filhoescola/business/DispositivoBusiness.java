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
        return Observable.create(new ConfirmarHandler(dispositivoModel));
    }

    public Observable<DispositivoModel> verificar(String prefixo,String numero,String codigo) {
        return Observable.create(new VerificarHandler(prefixo,numero,codigo));
    }

    private class ConfirmarHandler implements Observable.OnSubscribe<DispositivoModel> {
        private DispositivoModel dispositivoModel;
        public ConfirmarHandler(DispositivoModel dispositivoModel) {
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

    private class VerificarHandler implements Observable.OnSubscribe<DispositivoModel> {
        private String prefixo;
        private String numero;
        private String codigo;

        public VerificarHandler(String prefixo, String numero, String codigo) {
            this.prefixo = prefixo;
            this.numero = numero;
            this.codigo = codigo;
        }

        @Override
        public void call(Subscriber<? super DispositivoModel> subscriber) {
            try {
                DispositivoEndpoint endpoint = Endpoint.create(DispositivoEndpoint.class);
                Call<DispositivoModel> call = endpoint.verificar(prefixo,numero,codigo);
                DispositivoModel model = Endpoint.execute(call);
                subscriber.onNext(model);
                subscriber.onCompleted();
            } catch (Throwable e) {
                subscriber.onError(e);
            } finally {
            }
        }
    }
}
