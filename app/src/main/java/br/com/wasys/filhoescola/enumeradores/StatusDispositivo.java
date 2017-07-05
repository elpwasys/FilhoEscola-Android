package br.com.wasys.filhoescola.enumeradores;

/**
 * Created by bruno on 05/07/17.
 */

public enum StatusDispositivo {

    VERIFICADO("VERIFICADO"),
    NAO_VERIFICADO("NAO_VERIFICADO");

    private String status;

    StatusDispositivo(String status){
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }

    public static StatusDispositivo getStatus(String status) {
        if(status.equals("VERIFICADO")){
            return VERIFICADO;
        }else{
            return NAO_VERIFICADO;
        }

    }

}
