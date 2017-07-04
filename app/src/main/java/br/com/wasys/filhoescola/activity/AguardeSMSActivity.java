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

public class AguardeSMSActivity extends AppCompatActivity {

    @BindView(R.id.edt_digito_1) EditText edtDigito1;
    @BindView(R.id.edt_digito_2) EditText edtDigito2;
    @BindView(R.id.edt_digito_3) EditText edtDigito3;
    @BindView(R.id.edt_digito_4) EditText edtDigito4;
    @BindView(R.id.edt_digito_5) EditText edtDigito5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aguarde_sms);

        ButterKnife.bind(this);

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


    }

    @OnClick(R.id.btn_cadastrar)
    public void cadastrar() {
        startActivity(new Intent(this,HomeActivity.class));
        finish();
    }
}
