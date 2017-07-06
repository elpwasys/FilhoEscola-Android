package br.com.wasys.filhoescola.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import br.com.wasys.filhoescola.R;
import br.com.wasys.library.activity.AppActivity;

public class SplashScreenActivity extends BaseActivity {

    static final String[] PHONE = { Manifest.permission.READ_PHONE_STATE };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if(!checkedSelfPermission(PHONE)){
            ActivityCompat.requestPermissions(this,PHONE,500);
        }else{
            startActivity(new Intent(this,CadastroActivity.class));
            finish();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 500: {
                if (!grantedResults(grantResults)) {
                    showSnack(getString(R.string.msg_liberar_permissao));
                }else{

                }
                return;
            }

        }
    }

}
