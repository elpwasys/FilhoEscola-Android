package br.com.wasys.filhoescola.business;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.wasys.filhoescola.endpoint.DispositivoEndpoint;
import br.com.wasys.filhoescola.endpoint.Endpoint;
import br.com.wasys.filhoescola.endpoint.MensagemEndpoint;
import br.com.wasys.filhoescola.enumeradores.TipoVisualizacao;
import br.com.wasys.filhoescola.model.DispositivoModel;
import br.com.wasys.filhoescola.model.MensagemModel;
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

    public Observable<RealmResults<Mensagem>> diaria() {
        return Observable.create(new BuscaMensagemHandler(TipoVisualizacao.DIARIA,true));
    }

    public Observable<RealmResults<Mensagem>> semanal() {
        return Observable.create(new BuscaMensagemHandler(TipoVisualizacao.SEMANAL,false));
    }

    public Observable<RealmResults<Mensagem>> mensal() {
        return Observable.create(new BuscaMensagemHandler(TipoVisualizacao.MENSAL,false));
    }

    private class BuscaMensagemHandler implements Observable.OnSubscribe<RealmResults<Mensagem>> {
        private TipoVisualizacao tipo;
        private boolean sincroniza;

        public BuscaMensagemHandler(TipoVisualizacao tipo,boolean sincroniza) {
            this.tipo = tipo;
            this.sincroniza = sincroniza;
        }
        @Override
        public void call(Subscriber<? super RealmResults<Mensagem>> subscriber) {
            try {
                if(sincroniza){
                    sincronizar();
                }
                RealmResults<Mensagem> mensagens = null;
                switch (tipo){
                    case DIARIA:{
                        mensagens = Realm.getDefaultInstance().where(Mensagem.class).between("data", DateUtils.getFirstTimeOfDay(), DateUtils.getLastTimeOfDay()).findAll();
                        break;
                    }
                    case SEMANAL:{
                        mensagens = Realm.getDefaultInstance().where(Mensagem.class).between("data", DateUtils.getWeekMinDay(), DateUtils.getWeekMaxDay()).findAll();
                        break;
                    }
                    case MENSAL:{
                        mensagens = Realm.getDefaultInstance().where(Mensagem.class).between("data", DateUtils.getActualMinDay(), DateUtils.getActualMaxDay()).findAll();
                        break;
                    }
                }
                subscriber.onNext(mensagens);
                subscriber.onCompleted();
            } catch (Throwable e) {
                subscriber.onError(e);
            } finally {
            }
        }
    }

   public void sincronizar() throws Exception{
       MensagemEndpoint endpoint = Endpoint.create(MensagemEndpoint.class);
       Call<List<MensagemModel>> call = endpoint.buscar();
       List<MensagemModel> models = Endpoint.execute(call);
       Realm realm = Realm.getDefaultInstance();
       for (MensagemModel model : models) {
           realm.beginTransaction();
           Aluno aluno = realm.where(Aluno.class).equalTo("id",model.aluno.id).findFirst();
           if(aluno == null){
               aluno = realm.createObject(Aluno.class);
               aluno.createFrom(model.aluno);
           }
           Escola escola = realm.where(Escola.class).equalTo("id",model.escola.id).findFirst();
           if(escola == null){
               escola = realm.createObject(Escola.class);
               escola.createFrom(realm,model.escola);
           }
           Funcionario funcionario = realm.where(Funcionario.class).equalTo("id",model.funcionario.id).findFirst();
           if(funcionario == null){
               funcionario = realm.createObject(Funcionario.class);
               funcionario.createFrom(model.funcionario);
           }

           Mensagem mensagem = realm.createObject(Mensagem.class);
           mensagem.createFrom(model);

           mensagem.setEscola(escola);
           mensagem.setFuncionario(funcionario);

           aluno.setEscola(escola);
           aluno.getMensagens().add(mensagem);

           funcionario.setEscola(escola);
           funcionario.getMensagens().add(mensagem);

           escola.getMensagens().add(mensagem);

           if(!escola.getAlunos().contains(aluno)){
               escola.getAlunos().add(aluno);
           }

           if(!escola.getFuncionarios().contains(funcionario)){
               escola.getFuncionarios().add(funcionario);
           }
           realm.commitTransaction();
       }
   }
}
