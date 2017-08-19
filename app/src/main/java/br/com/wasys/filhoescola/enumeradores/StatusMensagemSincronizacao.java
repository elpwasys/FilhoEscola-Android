package br.com.wasys.filhoescola.enumeradores;

/**
 * Created by bruno on 05/07/17.
 */

public enum StatusMensagemSincronizacao {

    AGUARDANDO("AGUARDANDO"),
    ENVIANDO("ENVIANDO"),
    ENVIADO("ENVIADO");

    private String status;

    StatusMensagemSincronizacao(String status){
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }

    public static StatusMensagemSincronizacao getStatus(String status) {
        if(status.equals("AGUARDANDO")){
            return AGUARDANDO;
        }else if(status.equals("ENVIANDO")){
            return ENVIANDO;
        }else {
            return ENVIADO;
        }

    }

}
