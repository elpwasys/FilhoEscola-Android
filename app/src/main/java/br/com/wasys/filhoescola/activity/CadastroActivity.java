package br.com.wasys.filhoescola.activity;

import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import br.com.wasys.filhoescola.FilhoNaEscolaApplication;
import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.business.DispositivoBusiness;
import br.com.wasys.filhoescola.endpoint.DispositivoEndpoint;
import br.com.wasys.filhoescola.model.DispositivoModel;
import br.com.wasys.library.utils.FieldUtils;
import br.com.wasys.library.widget.edittext.DDDTextEdit;
import br.com.wasys.library.widget.edittext.DateTextEdit;
import br.com.wasys.library.widget.edittext.PhoneTextEdit;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;

public class CadastroActivity extends BaseActivity {

    @BindView(R.id.edt_ddd) DDDTextEdit edtDDD;
    @BindView(R.id.edt_numero) PhoneTextEdit edtNumero;
    @BindView(R.id.edt_data_nascimento) DateTextEdit edtDataNascimento;
    @BindView(R.id.edt_nome_completo) EditText edtNomeCompleto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        ButterKnife.bind(this);

        setTitle(R.string.cadastro);

        edtDataNascimento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(FieldUtils.removerEspeciaisEspaco(s.toString()).length() == 8){
                    edtDDD.requestFocus();
                }
            }
        });


        edtDDD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(FieldUtils.removerEspeciaisEspaco(s.toString()).length() == 2){
                    edtNumero.requestFocus();
                }
            }
        });
    }

    @OnClick(R.id.btn_enviar)
    public void enviar() {

        final DispositivoModel dispositivoModel = new DispositivoModel();

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        if(telephonyManager.getDeviceId() == null){
            dispositivoModel.uuid = "123123123123123";
        }else {
            dispositivoModel.uuid = telephonyManager.getDeviceId();
        }

        if(FieldUtils.isValueIsNullOrEmpty(edtNomeCompleto)){
            showSnack(getString(R.string.msg_val_nome));
            edtNomeCompleto.setError(getString(R.string.msg_val_campo_obrigatorio));
            return;
        }

        if(edtDataNascimento.getDate() == null){
            showSnack(getString(R.string.msg_val_data));
            edtDataNascimento.setError(getString(R.string.msg_val_campo_obrigatorio));
            return;
        }

        if(FieldUtils.isValueIsNullOrEmpty(edtDDD.getPhoneNumber())){
            showSnack(getString(R.string.msg_val_ddd));
            edtDDD.setError(getString(R.string.msg_val_campo_obrigatorio));
            return;
        }

        if(FieldUtils.isValueIsNullOrEmpty(edtNumero.getPhoneNumber())){
            showSnack(getString(R.string.msg_val_telefone));
            edtNumero.setError(getString(R.string.msg_val_campo_obrigatorio));
            return;
        }
        dispositivoModel.nome = edtNomeCompleto.getText().toString();
        dispositivoModel.dataNascimento = edtDataNascimento.getDate();
        dispositivoModel.prefixo = edtDDD.getPhoneNumber();
        dispositivoModel.numero = edtNumero.getPhoneNumber();

        DispositivoBusiness business = new DispositivoBusiness(this);
        Observable<DispositivoModel> observable = business.confirmar(dispositivoModel);
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
                        Intent intent = new Intent(CadastroActivity.this,AguardeSMSActivity.class);
                        intent.putExtra("dispositivo",dispositivoModel1);
                        startActivity(intent);
                        finish();
                        FilhoNaEscolaApplication.setDispositivoLogado(dispositivoModel1);
                    }
                });



    }
}
