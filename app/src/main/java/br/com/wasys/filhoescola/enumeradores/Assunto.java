package br.com.wasys.filhoescola.enumeradores;

/**
 * Created by bruno on 05/07/17.
 */

public enum Assunto {

    PROVA("PROVA"),
    MENSAGEM("MENSAGEM"),
    ATIVIDADE("ATIVIDADE"),
    INFORMACAO("INFORMACAO");

    private String assunto;

    Assunto(String assunto){
        this.assunto = assunto;
    }

    @Override
    public String toString() {
        return assunto;
    }

    public static Assunto getAssunto(String assunto) {
        if(assunto.equals("PROVA")){
            return PROVA;
        }else if(assunto.equals("MENSAGEM")){
            return MENSAGEM;
        }else if(assunto.equals("ATIVIDADE")){
            return ATIVIDADE;
        }else {
            return INFORMACAO;
        }

    }

}
