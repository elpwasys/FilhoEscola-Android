package br.com.wasys.filhoescola.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;

import java.util.List;

import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.model.AlunoModel;
import br.com.wasys.filhoescola.model.MensagemModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bruno on 14/07/17.
 */

public class AlunoAdapter extends RecyclerView.Adapter<AlunoAdapter.ViewHolder> implements View.OnClickListener {

    private List<AlunoModel> mDataSet;
    private OnItemClickListener mOnItemClickListener;

    public AlunoAdapter(List<AlunoModel> mDataSet) {
        this.mDataSet = mDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_aluno_mensagem, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mView.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AlunoModel obj = mDataSet.get(position);
        holder.mPosition = position;
        holder.mView.setTag(holder);
        holder.txtNomeAluno.setText(obj.nome);
        int mensagensNaoLida = 0;
        if (CollectionUtils.isNotEmpty(obj.mensagens)) {
            for (MensagemModel mensagem : obj.mensagens) {
                if (BooleanUtils.isNotTrue(mensagem.lida)){
                    mensagensNaoLida++;
                }
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
    public int getItemCount() {
        return CollectionUtils.size(mDataSet);
    }

    @Override
    public void onClick(View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder != null) {
            AlunoModel model = mDataSet.get(holder.mPosition);
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

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private int mPosition;

        @BindView(R.id.imgAluno) ImageView imgAluno;
        @BindView(R.id.txtNomeAluno) TextView txtNomeAluno;
        @BindView(R.id.txtNumeroMensagens) TextView txtNumeroMensagens;
        @BindView(R.id.lilNumeroMensagens) LinearLayout lilNumeroMensagens;

        ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int index, AlunoModel model);
    }
}
