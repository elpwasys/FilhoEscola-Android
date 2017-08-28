package br.com.wasys.filhoescola.service;

import org.apache.commons.lang3.StringUtils;

import br.com.wasys.filhoescola.FilhoNaEscolaApplication;
import br.com.wasys.filhoescola.endpoint.DispositivoEndpoint;
import br.com.wasys.filhoescola.endpoint.Endpoint;
import br.com.wasys.filhoescola.model.DispositivoModel;
import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by bruno on 09/07/17.
 */

public class DispositivoService extends Service {

    public static DispositivoModel atualizar(String pushToken) throws Throwable {
        String authorization = FilhoNaEscolaApplication.getAuthorization();
        if (StringUtils.isBlank(authorization)) {
            return FilhoNaEscolaApplication.getDispositivoLogado();
        }
        DispositivoEndpoint endpoint = Endpoint.create(DispositivoEndpoint.class);
        Call<DispositivoModel> call = endpoint.push(pushToken);
        DispositivoModel dispositivoModel = Endpoint.execute(call);
        return dispositivoModel;
    }

    public Observable<DispositivoModel> confirmar(DispositivoModel dispositivoModel) {
        return Observable.create(new ConfirmarHandler(dispositivoModel));
    }

    public Observable<DispositivoModel> verificar(String prefixo,String numero,String codigo) {
        return Observable.create(new VerificarHandler(prefixo,numero,codigo));
    }

    public Observable<DispositivoModel> reenviar(String prefixo,String numero) {
        return Observable.create(new ReenviarHandler(prefixo,numero));
    }
    public Observable<DispositivoModel> push(String token) {
        return Observable.create(new PushHandler(token));
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

    private class ReenviarHandler implements Observable.OnSubscribe<DispositivoModel> {
        private String prefixo;
        private String numero;

        public ReenviarHandler(String prefixo, String numero) {
            this.prefixo = prefixo;
            this.numero = numero;
        }

        @Override
        public void call(Subscriber<? super DispositivoModel> subscriber) {
            try {
                DispositivoEndpoint endpoint = Endpoint.create(DispositivoEndpoint.class);
                Call<DispositivoModel> call = endpoint.reenviar(prefixo,numero);
                DispositivoModel model = Endpoint.execute(call);
                subscriber.onNext(model);
                subscriber.onCompleted();
            } catch (Throwable e) {
                subscriber.onError(e);
            } finally {
            }
        }
    }

    private class PushHandler implements Observable.OnSubscribe<DispositivoModel> {
        private String token;

        public PushHandler(String token) {
            this.token = token;
        }

        @Override
        public void call(Subscriber<? super DispositivoModel> subscriber) {
            try {
                DispositivoEndpoint endpoint = Endpoint.create(DispositivoEndpoint.class);
                Call<DispositivoModel> call = endpoint.push(token);
                DispositivoModel dispositivoModel = Endpoint.execute(call);
                subscriber.onNext(dispositivoModel);
                subscriber.onCompleted();
            } catch (Throwable e) {
                subscriber.onError(e);
            } finally {
            }
        }
    }

    public static class Async {

        public static Observable<DispositivoModel> atualizar(final String pushToken) {
            return Observable.create(new Observable.OnSubscribe<DispositivoModel>() {
                @Override
                public void call(Subscriber<? super DispositivoModel> subscriber) {
                    try {
                        DispositivoModel dispositivoModel = DispositivoService.atualizar(pushToken);
                        subscriber.onNext(dispositivoModel);
                        subscriber.onCompleted();
                    } catch (Throwable e) {
                        subscriber.onError(e);
                    }
                }
            });
        }
    }
}
