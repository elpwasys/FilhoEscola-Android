package br.com.wasys.filhoescola.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;

import br.com.wasys.filhoescola.FilhoNaEscolaApplication;
import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.enumeradores.TipoPagina;
import br.com.wasys.library.activity.AppActivity;

/**
 * Created by bruno on 05/07/17.
 */

public class BaseActivity extends AppActivity {

    public void showSnack(String mensagem){

        Snackbar.make(findViewById(android.R.id.content),mensagem, Snackbar.LENGTH_LONG)
                .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    public void home(){
        openWeb(TipoPagina.INICIO);
    }
    public void meuCadastro(){
        openWeb(TipoPagina.MEUCADASTRO);
    }
    public void mensagem(){
        startActivity(new Intent(this,MensagensActivity.class));
        finish();
    }
    public void configurar(){
        openWeb(TipoPagina.CONFIGURAR);
    }
    public void ajuda(){
        openWeb(TipoPagina.AJUDA);
    }
    public void sair(){
        FilhoNaEscolaApplication.setDispositivoLogado(null);
        startActivity(new Intent(this,CadastroActivity.class));
        finish();
    }

    public void openWeb(TipoPagina tipoPagina){
        Intent intent = new Intent(this,WebActivity.class);
        intent.putExtra("tipo", tipoPagina);
        startActivity(intent);
        finish();
    }
}
