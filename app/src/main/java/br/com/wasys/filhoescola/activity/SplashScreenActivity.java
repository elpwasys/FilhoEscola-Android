package br.com.wasys.filhoescola.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.commons.lang3.StringUtils;

import br.com.wasys.filhoescola.FilhoNaEscolaApplication;
import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.business.DispositivoBusiness;
import br.com.wasys.filhoescola.enumeradores.StatusDispositivo;
import br.com.wasys.filhoescola.model.DispositivoModel;
import rx.Observable;
import rx.Subscriber;

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
            String token = FirebaseInstanceId.getInstance().getToken();
            if(token != null && !token.isEmpty()) {
                DispositivoBusiness business = new DispositivoBusiness(this);
                Observable<DispositivoModel> observable = business.push(token);
                prepare(observable)
                        .subscribe(new Subscriber<DispositivoModel>() {
                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                home();
                            }

                            @Override
                            public void onNext(DispositivoModel dispositivoModel1) {
                                Log.d("DispositivoModel", dispositivoModel1.toString());
                                home();
                            }
                        });
            }else {
                home();
            }
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
