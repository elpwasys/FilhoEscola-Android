package br.com.wasys.filhoescola.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.enumeradores.TipoVisualizacao;


public class VisualizacaoFragment extends Fragment {

    private static final String TIPO_VISUALIZACAO = "visualizacao";

    private TipoVisualizacao tipoVisualizacao;

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
        TextView textView = new TextView(getActivity());
        textView.setText(Integer.toString(tipoVisualizacao.toInt()));
        return textView;
    }
}
