package br.com.wasys.filhoescola.business;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.wasys.filhoescola.endpoint.DispositivoEndpoint;
import br.com.wasys.filhoescola.endpoint.Endpoint;
import br.com.wasys.filhoescola.endpoint.MensagemEndpoint;
import br.com.wasys.filhoescola.enumeradores.StatusMensagemSincronizacao;
import br.com.wasys.filhoescola.enumeradores.TipoVisualizacao;
import br.com.wasys.filhoescola.model.DispositivoModel;
import br.com.wasys.filhoescola.model.MensagemModel;
import br.com.wasys.filhoescola.model.SuccessModel;
import br.com.wasys.filhoescola.realm.Aluno;
import br.com.wasys.filhoescola.realm.Escola;
import br.com.wasys.filhoescola.realm.Funcionario;
import br.com.wasys.filhoescola.realm.Mensagem;
import br.com.wasys.library.utils.DateUtils;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by bruno on 09/07/17.
 */

public class MensagemBusiness extends Business {

    public MensagemBusiness(Context context) {
        super(context);
    }

    public Observable<Boolean> buscar() {
        return Observable.create(new BuscaMensagemHandler());
    }

    private class BuscaMensagemHandler implements Observable.OnSubscribe<Boolean> {


        public BuscaMensagemHandler() {
        }
        @Override
        public void call(Subscriber<? super Boolean> subscriber) {
            try {
                MensagemEndpoint endpoint = Endpoint.create(MensagemEndpoint.class);
                Call<List<MensagemModel>> call = endpoint.buscar();
                List<MensagemModel> models = Endpoint.execute(call);
                Realm realm = Realm.getDefaultInstance();
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
                subscriber.onError(e);
            } finally {
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
            try {
                MensagemEndpoint endpoint = Endpoint.create(MensagemEndpoint.class);
                Call<MensagemModel> call = endpoint.getMensagem(id);
                MensagemModel model = Endpoint.execute(call);
                Realm realm = Realm.getDefaultInstance();
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
                subscriber.onError(e);
            } finally {
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

}
