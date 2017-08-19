package br.com.wasys.filhoescola.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.BinderThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.activity.BaseActivity;
import br.com.wasys.filhoescola.realm.Aluno;
import br.com.wasys.filhoescola.realm.Mensagem;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * Created by bruno on 14/07/17.
 */

public class AlunoAdapter extends RealmRecyclerViewAdapter<Aluno, AlunoAdapter.ViewHolder> implements View.OnClickListener {

    public BaseActivity activity;
    private static ItemClickListener itemClickListener;

    public AlunoAdapter(RealmResults<Aluno> data, BaseActivity activity) {
        super(data, true);
        setHasStableIds(true);
        this.activity = activity;
    }


    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_aluno_mensagem, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Aluno obj = getItem(position);
        holder.imgAluno.setTag(obj.getId());
        holder.txtNomeAluno.setTag(obj.getId());
        holder.lilNumeroMensagens.setTag(obj.getId());
        holder.txtNumeroMensagens.setTag(obj.getId());
        holder.imgAluno.setOnClickListener(this);
        holder.txtNomeAluno.setOnClickListener(this);
        holder.lilNumeroMensagens.setOnClickListener(this);
        holder.txtNumeroMensagens.setOnClickListener(this);

        holder.txtNomeAluno.setText(obj.getNome());
        int mensagensNaoLida = 0;
        for (Mensagem mensagem : obj.getMensagens()) {
            if(!mensagem.getLida()){
                mensagensNaoLida++;
            }
        }
        if(mensagensNaoLida == 0){
            holder.lilNumeroMensagens.setVisibility(View.GONE);
        }else {
            holder.lilNumeroMensagens.setVisibility(View.VISIBLE);
            holder.txtNumeroMensagens.setText(Integer.toString(mensagensNaoLida));
        }
    }
    @Override
    public void onClick(View v) {

        if(itemClickListener != null) {
            itemClickListener.onItemClick((Long) v.getTag());
        }
    }

    @Override
    public long getItemId(int index) {
        return getItem(index).getId();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imgAluno) ImageView imgAluno;
        @BindView(R.id.txtNomeAluno) TextView txtNomeAluno;
        @BindView(R.id.txtNumeroMensagens) TextView txtNumeroMensagens;
        @BindView(R.id.lilNumeroMensagens) LinearLayout lilNumeroMensagens;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    public interface ItemClickListener {

        void onItemClick(Long position);
    }

}
