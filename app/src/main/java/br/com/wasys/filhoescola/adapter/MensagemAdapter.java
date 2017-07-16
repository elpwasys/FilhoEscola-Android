package br.com.wasys.filhoescola.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.realm.Mensagem;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * Created by bruno on 14/07/17.
 */

public class MensagemAdapter extends RealmRecyclerViewAdapter<Mensagem, MensagemAdapter.ViewHolder> {

    public Activity activity;

    public MensagemAdapter(RealmResults<Mensagem> data, Activity activity) {
        super(data, true);
        setHasStableIds(true);
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Mensagem obj = getItem(position);
        holder.txtNomeAluno.setText(obj.getAluno().getNome());
        holder.txtAtividade.setText(obj.getAssunto());
        holder.txtEscola.setText(obj.getEscola().getNome());
        holder.txtProfessor.setText(obj.getFuncionario().getNome());
        holder.txtMensagem.setText(obj.getConteudo());

        holder.btnAcao.setText(obj.getBotaoTexto());
        holder.btnAcao.setTag(obj.getBotaoLink());
        holder.btnAcao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getBotaoLink()));
                 activity.startActivity(intent);
            }
        });

        holder.btnAcao.setVisibility(obj.getBotaoTexto() != null && StringUtils.isNotEmpty(obj.getBotaoTexto()) ? View.VISIBLE : View.GONE);
    }

    @Override
    public long getItemId(int index) {
        return getItem(index).getId();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imgAluno) ImageView imgAluno;
        @BindView(R.id.txtNomeAluno) TextView txtNomeAluno;
        @BindView(R.id.txtEscola) TextView txtEscola;
        @BindView(R.id.txtProfessor) TextView txtProfessor;
        @BindView(R.id.txtAtividade) TextView txtAtividade;
        @BindView(R.id.txtMensagem) TextView txtMensagem;
        @BindView(R.id.btnAcao) Button btnAcao;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
