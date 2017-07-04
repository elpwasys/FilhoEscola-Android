package br.com.wasys.filhoescola.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.activity.HomeActivity;
import br.com.wasys.filhoescola.enumeradores.TipoVisualizacao;
import br.com.wasys.filhoescola.fragment.VisualizacaoFragment;

/**
 * Created by bruno on 04/07/17.
 */

public class HomePagerAdapter extends FragmentPagerAdapter {

    public Context context;

    public HomePagerAdapter(Context context,FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (TipoVisualizacao.getTipo(position)){
            case DIARIA:
                return VisualizacaoFragment.newInstance(TipoVisualizacao.DIARIA);
            case SEMANAL:
                return VisualizacaoFragment.newInstance(TipoVisualizacao.SEMANAL);
            case MENSAL:
                return VisualizacaoFragment.newInstance(TipoVisualizacao.MENSAL);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.diaria);
            case 1:
                return context.getString(R.string.semanal);
            case 2:
                return context.getString(R.string.mensal);
        }
        return null;
    }
}
