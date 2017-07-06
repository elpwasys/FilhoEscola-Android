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

import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.model.DispositivoModel;
import br.com.wasys.library.utils.FieldUtils;
import br.com.wasys.library.widget.edittext.DDDTextEdit;
import br.com.wasys.library.widget.edittext.DateTextEdit;
import br.com.wasys.library.widget.edittext.PhoneTextEdit;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

        edtDDD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 3){
                    edtNumero.requestFocus();
                }
            }
        });
    }

    @OnClick(R.id.btn_enviar)
    public void enviar() {

        DispositivoModel dispositivoModel = new DispositivoModel();

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        dispositivoModel.uuid = telephonyManager.getDeviceId();

        if(FieldUtils.isValueIsNullOrEmpty(edtNomeCompleto)){
            showSnack(getString(R.string.msg_val_nome));
            edtDataNascimento.setError(getString(R.string.msg_val_campo_obrigatorio));
            return;
        }

        if(edtDataNascimento.getDate() == null){
            showSnack(getString(R.string.msg_val_data));
            edtDataNascimento.setError(getString(R.string.msg_val_campo_obrigatorio));
            return;
        }

        if(FieldUtils.isValueIsNullOrEmpty(edtDDD.getPhoneNumber())){
            showSnack(getString(R.string.msg_val_ddd));
            edtDataNascimento.setError(getString(R.string.msg_val_campo_obrigatorio));
            return;
        }

        if(FieldUtils.isValueIsNullOrEmpty(edtNumero.getPhoneNumber())){
            showSnack(getString(R.string.msg_val_telefone));
            edtDataNascimento.setError(getString(R.string.msg_val_campo_obrigatorio));
            return;
        }


        startActivity(new Intent(this,AguardeSMSActivity.class));
        finish();
    }
}
