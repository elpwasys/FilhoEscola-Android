package br.com.wasys.filhoescola.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import br.com.wasys.filhoescola.FilhoNaEscolaApplication;
import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.enumeradores.StatusDispositivo;

public class SplashScreenActivity extends BaseActivity {

    static final String[] PHONE = { Manifest.permission.READ_PHONE_STATE };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if(!checkedSelfPermission(PHONE)){
            ActivityCompat.requestPermissions(this,PHONE,500);
            return;
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(FilhoNaEscolaApplication.getDispositivoLogado() != null && FilhoNaEscolaApplication.getDispositivoLogado().status == StatusDispositivo.VERIFICADO){
            home();
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
                    startActivity(new Intent(this,CadastroActivity.class));
                    finish();
                }
                return;
            }

        }
    }

}
