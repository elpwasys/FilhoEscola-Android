package br.com.wasys.filhoescola.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import br.com.wasys.filhoescola.FilhoNaEscolaApplication;
import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.enumeradores.StatusDispositivo;
import br.com.wasys.filhoescola.enumeradores.TipoPagina;
import br.com.wasys.filhoescola.model.DispositivoModel;

public class SplashScreenActivity extends BaseActivity {

    static final String[] PHONE = { Manifest.permission.READ_PHONE_STATE };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!checkedSelfPermission(PHONE)){
            ActivityCompat.requestPermissions(this,PHONE, 500);
            return;
        }
        navegar();
    }

    private void navegar() {
        Intent intent = CadastroActivity.newIntent(this);
        DispositivoModel dispositivoModel = FilhoNaEscolaApplication.getDispositivoLogado();
        if (dispositivoModel != null) {
            intent = AguardeSMSActivity.newIntent(this, dispositivoModel);
            if (StatusDispositivo.VERIFICADO.equals(dispositivoModel.status)) {
                intent = WebActivity.newIntent(this, TipoPagina.INICIO);
            }
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 500: {
                if (!grantedResults(grantResults)) {
                    showSnack(getString(R.string.msg_liberar_permissao));
                }else{
                    startActivity(new Intent(this, CadastroActivity.class));
                    finish();
                }
                return;
            }
        }
    }
}
