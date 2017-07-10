package br.com.wasys.filhoescola.endpoint;

import br.com.wasys.filhoescola.model.DispositivoModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by pascke on 02/08/16.
 */
public interface DispositivoEndpoint {

    @POST("dispositivo/confirmar")
    Call<DispositivoModel> confirmar(@Body DispositivoModel dispositivoModel);

    @GET("dispositivo/reenviar/{prefixo}/{numero}")
    Call<DispositivoModel> reenviar(@Path("prefixo") String prefixo, @Path("numero") String numero);

    @GET("dispositivo/verificar/{prefixo}/{numero}/{codigo}")
    Call<DispositivoModel> verificar(@Path("prefixo") String prefixo, @Path("numero") String numero,@Path("codigo") String codigo);
}
