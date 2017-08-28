package br.com.wasys.filhoescola.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;

import java.util.List;

import br.com.wasys.filhoescola.BuildConfig;
import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.enumeradores.Assunto;
import br.com.wasys.filhoescola.model.MensagemModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bruno on 14/07/17.
 */

public class MensagemAdapter extends RecyclerView.Adapter<MensagemAdapter.ViewHolder> implements View.OnClickListener {

    private List<MensagemModel> mDataSet;
    private OnItemClickListener mOnItemClickListener;

    public MensagemAdapter(List<MensagemModel> mDataSet) {
        this.mDataSet = mDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_mensagem, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mView.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MensagemModel model = mDataSet.get(position);
        Context context = holder.mView.getContext();
        holder.mPosition = position;
        holder.mView.setTag(holder);
        Picasso.with(context)
                .load(BuildConfig.URL_BASE + "/" + model.escola.logo.caminho)
                .into(holder.imgEscola);
        holder.txtMensagem.setText(model.conteudo);
        holder.txtAssunto.setText(model.assunto.toString());
        holder.imgAssunto.setImageResource(model.assunto.getImagem());
        if (BooleanUtils.isNotTrue(model.lida)){
            holder.txtMensagem.setTypeface(null, Typeface.BOLD);
        }
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.size(mDataSet);
    }

    @Override
    public void onClick(View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder != null) {
            MensagemModel model = mDataSet.get(holder.mPosition);
            if (view == holder.mView) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(holder.mPosition, model);
                }
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private int mPosition;

        @BindView(R.id.imgEscola) ImageView imgEscola;
        @BindView(R.id.imgAssunto) ImageView imgAssunto;
        @BindView(R.id.txtAssunto) TextView txtAssunto;
        @BindView(R.id.txtMensagem) TextView txtMensagem;
        @BindView(R.id.layout) LinearLayout linearLayout;

        ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int index, MensagemModel model);
    }
}