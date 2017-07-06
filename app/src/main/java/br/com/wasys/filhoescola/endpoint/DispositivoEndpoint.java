package br.com.wasys.filhoescola.endpoint;

import br.com.wasys.filhoescola.model.DispositivoModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by pascke on 02/08/16.
 */
public interface DispositivoEndpoint {

    @POST("dispositivo/confirmar")
    Call<DispositivoModel> confirmar(@Body DispositivoModel dispositivoModel);

    @GET("dispositivo/reenviar")
    Call<DispositivoModel> reenviar(@Body String prefixo, @Body String numero);

    @GET("dispositivo/verificar")
    Call<DispositivoModel> verificar(@Body String prefixo, @Body String numero,@Body String codigo);
}
