package br.com.wasys.filhoescola.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.node.BooleanNode;

import org.w3c.dom.Text;

import br.com.wasys.filhoescola.FilhoNaEscolaApplication;
import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.activity.AguardeSMSActivity;
import br.com.wasys.filhoescola.activity.BaseActivity;
import br.com.wasys.filhoescola.activity.CadastroActivity;
import br.com.wasys.filhoescola.adapter.MensagemAdapter;
import br.com.wasys.filhoescola.business.DispositivoBusiness;
import br.com.wasys.filhoescola.business.MensagemBusiness;
import br.com.wasys.filhoescola.enumeradores.TipoVisualizacao;
import br.com.wasys.filhoescola.model.DispositivoModel;
import br.com.wasys.filhoescola.realm.Mensagem;
import br.com.wasys.library.activity.AppActivity;
import br.com.wasys.library.utils.DateUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;


public class VisualizacaoFragment extends Fragment {

    private static final String TIPO_VISUALIZACAO = "visualizacao";

    private TipoVisualizacao tipoVisualizacao;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.txtEmpty) TextView txtEmpty;

    private MensagemAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;


    public static VisualizacaoFragment newInstance(TipoVisualizacao tipoVisualizacao) {
        VisualizacaoFragment fragment = new VisualizacaoFragment();
        Bundle args = new Bundle();
        args.putInt(TIPO_VISUALIZACAO, tipoVisualizacao.toInt());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tipoVisualizacao = TipoVisualizacao.getTipo(getArguments().getInt(TIPO_VISUALIZACAO));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visualizacao, container, false);
        ButterKnife.bind(this, view);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        if(tipoVisualizacao == TipoVisualizacao.DIARIA) {
            MensagemBusiness business = new MensagemBusiness(getActivity());
            Observable<Boolean> observable = business.buscar();
            ((AppActivity) getActivity()).prepare(observable)
                    .subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onStart() {
                            ((AppActivity) getActivity()).showProgress();
                        }

                        @Override
                        public void onCompleted() {
                            ((AppActivity) getActivity()).hideProgress();
                        }

                        @Override
                        public void onError(Throwable e) {
                            ((AppActivity) getActivity()).hideProgress();
                            e.printStackTrace();
                            ((BaseActivity) getActivity()).showSnack(e.getMessage());
                        }

                        @Override
                        public void onNext(Boolean bol) {
                            montaLista();
                        }
                    });
        }else{
            montaLista();
        }


        return view;
    }

    @UiThread
    public void montaLista(){
        RealmResults<Mensagem> mensagens = Realm.getDefaultInstance().where(Mensagem.class).findAll();
        switch (tipoVisualizacao){
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
        if(mensagens.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
        }else {
            adapter = new MensagemAdapter(mensagens, (BaseActivity) getActivity());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
            txtEmpty.setVisibility(View.GONE);
        }

    }
}
