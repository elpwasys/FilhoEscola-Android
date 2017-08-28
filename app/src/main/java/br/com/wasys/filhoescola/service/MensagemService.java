package br.com.wasys.filhoescola.service;

import android.util.Log;

import java.util.List;

import br.com.wasys.filhoescola.endpoint.Endpoint;
import br.com.wasys.filhoescola.endpoint.MensagemEndpoint;
import br.com.wasys.filhoescola.enumeradores.StatusMensagemSincronizacao;
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

public class MensagemService extends Service {

    public static void read(Long id) throws Throwable {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            RealmQuery<Mensagem> query = realm.where(Mensagem.class)
                    .equalTo("id", id);
            Mensagem mensagem = query.findFirst();
            mensagem.setLida(true);
            realm.commitTransaction();
        } catch (Throwable e) {
            if (realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            throw e;
        } finally {
            realm.close();
        }
    }

    public static MensagemModel get(Long id) throws Throwable {
        Realm realm = Realm.getDefaultInstance();
        try {
            RealmQuery<Mensagem> query = realm.where(Mensagem.class)
                    .equalTo("id", id);
            Mensagem mensagem = query.findFirst();
            return MensagemModel.from(mensagem);
        } finally {
            realm.close();
        }
    }

    public static List<MensagemModel> findByAluno(Long id) throws Throwable {
        Realm realm = Realm.getDefaultInstance();
        try {
            RealmQuery<Mensagem> query = realm.where(Mensagem.class)
                    .equalTo("alunos.id", id);
            RealmResults<Mensagem> results = query.findAll();
            return MensagemModel.from(results);
        } finally {
            realm.close();
        }
    }

    public Observable<Boolean> buscar() {
        return Observable.create(new BuscaMensagemHandler());
    }

    private class BuscaMensagemHandler implements Observable.OnSubscribe<Boolean> {

        public BuscaMensagemHandler() {
        }
        @Override
        public void call(Subscriber<? super Boolean> subscriber) {
            Realm realm = Realm.getDefaultInstance();
            try {
                MensagemEndpoint endpoint = Endpoint.create(MensagemEndpoint.class);
                Call<List<MensagemModel>> call = endpoint.buscar();
                List<MensagemModel> models = Endpoint.execute(call);
                for (MensagemModel model : models) {
                    Log.d("Mensagem","Aluno="+model.aluno.nome+" Mensagem="+model.conteudo);
                    realm.beginTransaction();
                    Aluno aluno = realm.where(Aluno.class).equalTo("id",model.aluno.id).findFirst();
                    if(aluno == null){
                        aluno = realm.createObject(Aluno.class,model.aluno.id);
                        aluno.createFrom(model.aluno);
                    }
                    Escola escola = realm.where(Escola.class).equalTo("id",model.escola.id).findFirst();
                    if(escola == null){
                        escola = realm.createObject(Escola.class,model.escola.id);
                        escola.createFrom(realm,model.escola);
                    }
                    Funcionario funcionario = realm.where(Funcionario.class).equalTo("id",model.funcionario.id).findFirst();
                    if(funcionario == null){
                        funcionario = realm.createObject(Funcionario.class,model.funcionario.id);
                        funcionario.createFrom(model.funcionario);
                    }
                    Mensagem mensagem  = realm.where(Mensagem.class).equalTo("id",model.id).findFirst();
                    if(mensagem == null){
                        mensagem = realm.createObject(Mensagem.class,model.id);
                        mensagem.createFrom(model);
                        mensagem.setLida(false);
                        mensagem.setStatus(StatusMensagemSincronizacao.AGUARDANDO);
                    }

                    mensagem.setEscola(escola);
                    mensagem.setFuncionario(funcionario);

                    if(!mensagem.getAlunos().contains(aluno)) {
                        mensagem.getAlunos().add(aluno);
                    }

                    aluno.setEscola(escola);
                    if(!aluno.getMensagens().contains(mensagem)) {
                        aluno.getMensagens().add(mensagem);
                    }

                    funcionario.setEscola(escola);
                    if(!funcionario.getMensagens().contains(mensagem)) {
                        funcionario.getMensagens().add(mensagem);
                    }

                    if(!escola.getMensagens().contains(mensagem)) {
                        escola.getMensagens().add(mensagem);
                    }

                    if(!escola.getAlunos().contains(aluno)){
                        escola.getAlunos().add(aluno);
                    }

                    if(!escola.getFuncionarios().contains(funcionario)){
                        escola.getFuncionarios().add(funcionario);
                    }
                    realm.commitTransaction();
                }
                subscriber.onNext(true);
                subscriber.onCompleted();
            } catch (Throwable e) {
                if (realm.isInTransaction()) {
                    realm.cancelTransaction();
                }
                subscriber.onError(e);
            } finally {
                realm.close();
            }
        }
    }

    public Observable<Long> getMensagem(String id) {
        return Observable.create(new BuscaMensagemIdHandler(id));
    }

    private class BuscaMensagemIdHandler implements Observable.OnSubscribe<Long> {

        private String id;

        public BuscaMensagemIdHandler(String id) {
            this.id = id;
        }
        @Override
        public void call(Subscriber<? super Long> subscriber) {
            Realm realm = Realm.getDefaultInstance();
            try {
                MensagemEndpoint endpoint = Endpoint.create(MensagemEndpoint.class);
                Call<MensagemModel> call = endpoint.getMensagem(id);
                MensagemModel model = Endpoint.execute(call);
                realm.beginTransaction();
                Aluno aluno = realm.where(Aluno.class).equalTo("id",model.aluno.id).findFirst();
                if(aluno == null){
                    aluno = realm.createObject(Aluno.class,model.aluno.id);
                    aluno.createFrom(model.aluno);
                }
                Escola escola = realm.where(Escola.class).equalTo("id",model.escola.id).findFirst();
                if(escola == null){
                    escola = realm.createObject(Escola.class,model.escola.id);
                    escola.createFrom(realm,model.escola);
                }
                Funcionario funcionario = realm.where(Funcionario.class).equalTo("id",model.funcionario.id).findFirst();
                if(funcionario == null){
                    funcionario = realm.createObject(Funcionario.class,model.funcionario.id);
                    funcionario.createFrom(model.funcionario);
                }
                Mensagem mensagem  = realm.where(Mensagem.class).equalTo("id",model.id).findFirst();
                if(mensagem == null){
                    mensagem = realm.createObject(Mensagem.class,model.id);
                    mensagem.createFrom(model);
                    mensagem.setLida(false);
                    mensagem.setStatus(StatusMensagemSincronizacao.AGUARDANDO);
                }

                mensagem.setEscola(escola);
                mensagem.setFuncionario(funcionario);

                if(!mensagem.getAlunos().contains(aluno)) {
                    mensagem.getAlunos().add(aluno);
                }

                aluno.setEscola(escola);
                if(!aluno.getMensagens().contains(mensagem)) {
                    aluno.getMensagens().add(mensagem);
                }

                funcionario.setEscola(escola);
                if(!funcionario.getMensagens().contains(mensagem)) {
                    funcionario.getMensagens().add(mensagem);
                }

                if(!escola.getMensagens().contains(mensagem)) {
                    escola.getMensagens().add(mensagem);
                }

                if(!escola.getAlunos().contains(aluno)){
                    escola.getAlunos().add(aluno);
                }

                if(!escola.getFuncionarios().contains(funcionario)){
                    escola.getFuncionarios().add(funcionario);
                }
                realm.commitTransaction();
                subscriber.onNext(mensagem.getId());
                subscriber.onCompleted();
            } catch (Throwable e) {
                if (realm.isInTransaction()) {
                    realm.cancelTransaction();
                }
                subscriber.onError(e);
            } finally {
                realm.close();
            }
        }
    }

    public Observable<SuccessModel> getSync(Long id) {
        return Observable.create(new SyncIdHandler(id));
    }

    private class SyncIdHandler implements Observable.OnSubscribe<SuccessModel> {

        private Long id;

        public SyncIdHandler(Long id) {
            this.id = id;
        }
        @Override
        public void call(Subscriber<? super SuccessModel> subscriber) {
            try {
                MensagemEndpoint endpoint = Endpoint.create(MensagemEndpoint.class);
                Call<SuccessModel> call = endpoint.sincronizar(id);
                SuccessModel model = Endpoint.execute(call);
                subscriber.onNext(model);
                subscriber.onCompleted();
            } catch (Throwable e) {
                subscriber.onError(e);
            } finally {
            }
        }
    }

    public static class Async {

        public static Observable<Boolean> read(final Long id) {
            return Observable.create(new Observable.OnSubscribe<Boolean>() {
                @Override
                public void call(Subscriber<? super Boolean> subscriber) {
                    try {
                        MensagemService.read(id);
                        subscriber.onNext(true);
                        subscriber.onCompleted();
                    } catch (Throwable e) {
                        subscriber.onError(e);
                    }
                }
            });
        }

        public static Observable<MensagemModel> get(final Long id) {
            return Observable.create(new Observable.OnSubscribe<MensagemModel>() {
                @Override
                public void call(Subscriber<? super MensagemModel> subscriber) {
                    try {
                        MensagemModel model = MensagemService.get(id);
                        subscriber.onNext(model);
                        subscriber.onCompleted();
                    } catch (Throwable e) {
                        subscriber.onError(e);
                    }
                }
            });
        }


        public static Observable<List<MensagemModel>> findByAluno(final Long id) {
            return Observable.create(new Observable.OnSubscribe<List<MensagemModel>>() {
                @Override
                public void call(Subscriber<? super List<MensagemModel>> subscriber) {
                    try {
                        List<MensagemModel> models = MensagemService.findByAluno(id);
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
