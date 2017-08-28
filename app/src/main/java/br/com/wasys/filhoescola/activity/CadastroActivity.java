package br.com.wasys.filhoescola.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import br.com.wasys.filhoescola.FilhoNaEscolaApplication;
import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.service.DispositivoService;
import br.com.wasys.filhoescola.model.DispositivoModel;
import br.com.wasys.library.utils.FieldUtils;
import br.com.wasys.library.widget.edittext.DDDPhoneTextEdit;
import br.com.wasys.library.widget.edittext.DateTextEdit;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;

public class CadastroActivity extends BaseActivity {

    @BindView(R.id.edt_numero) DDDPhoneTextEdit edtDDDPhone;
    @BindView(R.id.edt_data_nascimento) DateTextEdit edtDataNascimento;
    @BindView(R.id.edt_nome_completo) EditText edtNomeCompleto;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, CadastroActivity.class);
        return intent;
    }

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
                    edtDDDPhone.requestFocus();
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

        if(FieldUtils.isValueIsNullOrEmpty(edtDDDPhone.getPhoneNumber())){
            showSnack(getString(R.string.msg_val_telefone));
            edtDDDPhone.setError(getString(R.string.msg_val_campo_obrigatorio));
            return;
        }
        dispositivoModel.nome = edtNomeCompleto.getText().toString();
        dispositivoModel.dataNascimento = edtDataNascimento.getDate();
        dispositivoModel.prefixo = edtDDDPhone.getPhoneNumber().substring(0,2);
        dispositivoModel.numero = edtDDDPhone.getPhoneNumber().substring(2);

        DispositivoService business = new DispositivoService();
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
                        FilhoNaEscolaApplication.setDispositivoLogado(dispositivoModel1);
                        Intent intent = AguardeSMSActivity.newIntent(CadastroActivity.this, dispositivoModel1);
                        startActivity(intent);
                        finish();
                    }
                });

    }
}
