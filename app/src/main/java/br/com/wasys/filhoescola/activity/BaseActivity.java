package br.com.wasys.filhoescola.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;

import br.com.wasys.filhoescola.R;
import br.com.wasys.library.activity.AppActivity;

/**
 * Created by bruno on 05/07/17.
 */

public class BaseActivity extends AppActivity {

    protected void showSnack(String mensagem){

        Snackbar.make(findViewById(android.R.id.content),mensagem, Snackbar.LENGTH_LONG)
                .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    protected void home(){
        showSnack(getString(R.string.inicio));
    }
    protected void meuCadastro(){
        showSnack(getString(R.string.meu_cadastro));
    }
    protected void mensagem(){
        startActivity(new Intent(this,MensagensActivity.class));
        finish();
    }
    protected void configurar(){
        showSnack(getString(R.string.configurar));
    }
    protected void ajuda(){
        showSnack(getString(R.string.ajuda));
    }
    protected void sair(){
        finish();
    }
}
