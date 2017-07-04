package br.com.wasys.filhoescola.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import br.com.wasys.filhoescola.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CadastroActivity extends AppCompatActivity {

    @BindView(R.id.edt_ddd) EditText edtDDD;
    @BindView(R.id.edt_numero) EditText edtNumero;
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
        startActivity(new Intent(this,AguardeSMSActivity.class));
    }
}
