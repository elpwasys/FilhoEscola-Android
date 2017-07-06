package br.com.wasys.filhoescola.activity;

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
}
