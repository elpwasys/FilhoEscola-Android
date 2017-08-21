package br.com.wasys.filhoescola.adapter;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import br.com.wasys.filhoescola.BuildConfig;
import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.activity.BaseActivity;
import br.com.wasys.filhoescola.enumeradores.Assunto;
import br.com.wasys.filhoescola.realm.Mensagem;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * Created by bruno on 14/07/17.
 */

public class MensagemAdapter extends RealmRecyclerViewAdapter<Mensagem, MensagemAdapter.ViewHolder> implements View.OnClickListener{

    public BaseActivity activity;
    private ItemClickListener itemClickListener;

    public MensagemAdapter(RealmResults<Mensagem> data, BaseActivity activity) {
        super(data, true);
        setHasStableIds(true);
        this.activity = activity;
    }
    public MensagemAdapter(RealmList<Mensagem> data, BaseActivity activity) {
        super(data, true);
        setHasStableIds(true);
        this.activity = activity;
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Mensagem obj = getItem(position);
        Picasso.with(activity)
                .load(BuildConfig.BASE_URL+BuildConfig.BASE_CONTEXT_FILE+obj.getEscola().getImagem().getCaminho())
                .into(holder.imgEscola);
        holder.txtMensagem.setText(obj.getConteudo());
        holder.txtAssunto.setText(Assunto.getAssunto(obj.getAssunto()).toString());
        holder.imgAssunto.setImageResource(Assunto.getAssunto(obj.getAssunto()).getImagem());

        if(!obj.getLida()){
            holder.txtMensagem.setTypeface(null, Typeface.BOLD);
        }

        holder.imgEscola.setOnClickListener(this);
        holder.txtMensagem.setOnClickListener(this);
        holder.txtAssunto.setOnClickListener(this);
        holder.imgAssunto.setOnClickListener(this);
        holder.linearLayout.setOnClickListener(this);
        holder.linearLayout.setTag(obj.getId());
        holder.imgEscola.setTag(obj.getId());
        holder.txtMensagem.setTag(obj.getId());
        holder.txtAssunto.setTag(obj.getId());
        holder.imgAssunto.setTag(obj.getId());

    }

    @Override
    public long getItemId(int index) {
        return getItem(index).getId();
    }

    @Override
    public void onClick(View v) {

        if(itemClickListener != null) {
            itemClickListener.onItemClick((Long) v.getTag());
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imgEscola) ImageView imgEscola;
        @BindView(R.id.imgAssunto) ImageView imgAssunto;
        @BindView(R.id.txtAssunto) TextView txtAssunto;
        @BindView(R.id.txtMensagem) TextView txtMensagem;
        @BindView(R.id.layout) LinearLayout linearLayout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    public interface ItemClickListener {

        void onItemClick(Long position);
    }

}
