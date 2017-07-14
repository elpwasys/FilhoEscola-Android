package br.com.wasys.filhoescola.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.webkit.WebView;
import android.widget.EditText;

import br.com.wasys.filhoescola.FilhoNaEscolaApplication;
import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.business.DispositivoBusiness;
import br.com.wasys.filhoescola.model.DispositivoModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;

public class AguardeSMSActivity extends BaseActivity {

    @BindView(R.id.edt_digito_1) EditText edtDigito1;
    @BindView(R.id.edt_digito_2) EditText edtDigito2;
    @BindView(R.id.edt_digito_3) EditText edtDigito3;
    @BindView(R.id.edt_digito_4) EditText edtDigito4;
    @BindView(R.id.edt_digito_5) EditText edtDigito5;
    @BindView(R.id.edt_digito_6) EditText edtDigito6;

    DispositivoModel dispositivoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aguarde_sms);

        ButterKnife.bind(this);

        dispositivoModel = (DispositivoModel) getIntent().getSerializableExtra("dispositivo");

        if(dispositivoModel == null){
            showSnack(getString(R.string.msg_erro_cadastro));
            startActivity(new Intent(this,CadastroActivity.class));
            finish();
        }

        setTitle(R.string.confirmacao);

        edtDigito1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 1){
                    edtDigito2.requestFocus();
                }
            }
        });
        edtDigito2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 1){
                    edtDigito3.requestFocus();
                }
            }
        });

        edtDigito3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 1){
                    edtDigito4.requestFocus();
                }
            }
        });

        edtDigito4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 1){
                    edtDigito5.requestFocus();
                }
            }
        });

        edtDigito5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 1){
                    edtDigito6.requestFocus();
                }
            }
        });


    }

    @OnClick(R.id.btn_cadastrar)
    public void cadastrar() {
        StringBuilder codigo = new StringBuilder();
        codigo.append(edtDigito1.getText().toString());
        codigo.append(edtDigito2.getText().toString());
        codigo.append(edtDigito3.getText().toString());
        codigo.append(edtDigito4.getText().toString());
        codigo.append(edtDigito5.getText().toString());
        codigo.append(edtDigito6.getText().toString());

        DispositivoBusiness business = new DispositivoBusiness(this);
        Observable<DispositivoModel> observable = business.verificar(dispositivoModel.prefixo,dispositivoModel.numero,codigo.toString());
        prepare(observable)
                .subscribe(new Subscriber<DispositivoModel>() {
                    @Override
                    public void onStart() {
                        showProgress();
                    }
                    @Override
                    public void onCompleted() {
                        hideProgress();
                    }
                    @Override
                    public void onError(Throwable e) {
                        hideProgress();
                        e.printStackTrace();
                        showSnack(e.getMessage());
                    }
                    @Override
                    public void onNext(DispositivoModel dispositivoModel1) {
                        startActivity(new Intent(AguardeSMSActivity.this,WebActivity.class));
                        finish();
                        FilhoNaEscolaApplication.setDispositivoLogado(dispositivoModel1);
                    }
                });


    }
}
