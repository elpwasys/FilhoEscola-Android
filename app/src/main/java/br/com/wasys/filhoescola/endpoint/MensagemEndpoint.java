package br.com.wasys.filhoescola.endpoint;

import java.util.List;

import br.com.wasys.filhoescola.model.DispositivoModel;
import br.com.wasys.filhoescola.model.MensagemModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by pascke on 02/08/16.
 */
public interface MensagemEndpoint {

    @POST("mensagem/buscar")
    Call<List<MensagemModel>> buscar();

    @GET("mensagem/{id}")
    Call<MensagemModel> getMensagem(@Path("id") String id);

    @POST("mensagem/sincronizar")
    Call<List<MensagemModel>> sincronizar();
}
