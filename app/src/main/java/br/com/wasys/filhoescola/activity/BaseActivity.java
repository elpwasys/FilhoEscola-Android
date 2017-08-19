package br.com.wasys.filhoescola.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.wasys.filhoescola.BuildConfig;
import br.com.wasys.filhoescola.FilhoNaEscolaApplication;
import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.enumeradores.TipoPagina;
import br.com.wasys.filhoescola.enumeradores.TypeMessage;
import br.com.wasys.filhoescola.model.MessageModel;
import br.com.wasys.filhoescola.realm.Cache;
import br.com.wasys.library.activity.AppActivity;
import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by bruno on 05/07/17.
 */

public class BaseActivity extends AppActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showSnack(String mensagem) {

        Snackbar.make(findViewById(android.R.id.content), mensagem, Snackbar.LENGTH_LONG)
                .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }
    public void showSnack(MessageModel mensagem) {

        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), mensagem.getContent(), Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(R.color.preto));
        switch (mensagem.getType()){
            case INFO:
            case SUCCESS:
                sbView.setBackgroundColor(getResources().getColor(R.color.verde));
                break;
            case ERROR:
                sbView.setBackgroundColor(getResources().getColor(R.color.vermelho));
                break;
            case WARNING:
                sbView.setBackgroundColor(getResources().getColor(R.color.amarelo));
                break;

        }
        snackbar.show();

    }

    public void home() {
        openWeb(TipoPagina.INICIO);
    }

    public void meuCadastro() {
        openWeb(TipoPagina.MEUCADASTRO);
    }

    public void mensagem() {
        startActivity(new Intent(this, MensagensActivity.class));
        finish();
    }

    public void configurar() {
        openWeb(TipoPagina.CONFIGURAR);
    }

    public void ajuda() {
        openWeb(TipoPagina.AJUDA);
    }

    public void sair() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        FilhoNaEscolaApplication.setDispositivoLogado(null);
        startActivity(new Intent(this, CadastroActivity.class));
        finish();
    }

    public void limparCache() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<Cache> results = realm.where(Cache.class).findAll();
        results.deleteAllFromRealm();
        realm.commitTransaction();
        showSnack("Cache limpo");
    }

    public void openWeb(TipoPagina tipoPagina) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("tipo", tipoPagina);
        startActivity(intent);
        finish();
    }

    public String uploadImage(Bitmap image) throws IOException {

        OkHttpClient client = new OkHttpClient();

        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpeg");

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss'.jpeg'");
        String imageName = format.format(new Date());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 70, bos);

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", imageName, RequestBody.create(MEDIA_TYPE_PNG, bos.toByteArray()))
                .build();

        Request request = new Request.Builder().url(BuildConfig.BASE_URL + BuildConfig.BASE_CONTEXT_REST+ "/file/upload")
                .post(requestBody).build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        return response.body().string();

    }
}
