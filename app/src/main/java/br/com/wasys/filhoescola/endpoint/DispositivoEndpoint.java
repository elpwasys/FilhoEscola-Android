package br.com.wasys.filhoescola.endpoint;

import br.com.wasys.filhoescola.model.DispositivoModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by pascke on 02/08/16.
 */
public interface DispositivoEndpoint {

    @POST("dispositivo/confirmar")
    Call<DispositivoModel> confirmar(@Body DispositivoModel dispositivoModel);
}
