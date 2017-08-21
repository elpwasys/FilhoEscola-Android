package br.com.wasys.filhoescola.enumeradores;

import br.com.wasys.filhoescola.R;

/**
 * Created by bruno on 05/07/17.
 */

public enum Assunto {

    PROVA("Prova", R.mipmap.ico_editar),
    MENSAGEM("Mensagem",R.mipmap.ico_email),
    ATIVIDADE("Atividade",R.mipmap.ico_documento),
    INFORMACAO("Informação",R.mipmap.ico_informacao);

    private String assunto;
    private int imagem;

    Assunto(String assunto, int imagem) {
        this.assunto = assunto;
        this.imagem = imagem;
    }

    @Override
    public String toString() {
        return assunto;
    }

    public int getImagem() {
        return imagem;
    }

    public static Assunto getAssunto(String assunto) {
        assunto = assunto.toUpperCase();
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
