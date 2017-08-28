package br.com.wasys.filhoescola.service;

import android.util.Log;

import java.util.List;

import br.com.wasys.filhoescola.endpoint.Endpoint;
import br.com.wasys.filhoescola.endpoint.MensagemEndpoint;
import br.com.wasys.filhoescola.enumeradores.StatusMensagemSincronizacao;
import br.com.wasys.filhoescola.model.AlunoModel;
import br.com.wasys.filhoescola.model.MensagemModel;
import br.com.wasys.filhoescola.model.SuccessModel;
import br.com.wasys.filhoescola.realm.Aluno;
import br.com.wasys.filhoescola.realm.Escola;
import br.com.wasys.filhoescola.realm.Funcionario;
import br.com.wasys.filhoescola.realm.Mensagem;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by bruno on 09/07/17.
 */

public class AlunoService extends Service {

    public static AlunoModel get(Long id) throws Throwable {
        Realm realm = Realm.getDefaultInstance();
        try {
            RealmQuery<Aluno> query = realm.where(Aluno.class)
                    .equalTo("id", id);
            Aluno aluno = query.findFirst();
            return AlunoModel.from(aluno);
        } finally {
            realm.close();
        }
    }

    public static List<AlunoModel> listar() throws Throwable {
        Realm realm = Realm.getDefaultInstance();
        try {
            RealmResults<Aluno> results = realm.where(Aluno.class).findAll();
            return AlunoModel.from(results);
        } finally {
            realm.close();
        }
    }

    public static class Async {

        public static Observable<AlunoModel> get(final Long id) {
            return Observable.create(new Observable.OnSubscribe<AlunoModel>() {
                @Override
                public void call(Subscriber<? super AlunoModel> subscriber) {
                    try {
                        AlunoModel model = AlunoService.get(id);
                        subscriber.onNext(model);
                        subscriber.onCompleted();
                    } catch (Throwable e) {
                        subscriber.onError(e);
                    }
                }
            });
        }

        public static Observable<List<AlunoModel>> listar() {
            return Observable.create(new Observable.OnSubscribe<List<AlunoModel>>() {
                @Override
                public void call(Subscriber<? super List<AlunoModel>> subscriber) {
                    try {
                        List<AlunoModel> models = AlunoService.listar();
                        subscriber.onNext(models);
                        subscriber.onCompleted();
                    } catch (Throwable e) {
                        subscriber.onError(e);
                    }
                }
            });
        }
    }
}
