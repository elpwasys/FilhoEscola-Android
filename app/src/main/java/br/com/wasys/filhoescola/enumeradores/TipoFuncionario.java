package br.com.wasys.filhoescola.enumeradores;

/**
 * Created by bruno on 05/07/17.
 */

public enum TipoFuncionario {

    PRINCIPAL("PRINCIPAL"),
    PROFESSOR("PROFESSOR"),
    SECRETARIA("SECRETARIA");

    private String tipo;

    TipoFuncionario(String tipo){
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return tipo;
    }

    public static TipoFuncionario getTipo(String tipo) {
        if(tipo.equals("PRINCIPAL")){
            return PRINCIPAL;
        }else if(tipo.equals("PROFESSOR")){
            return PROFESSOR;
        }else {
            return SECRETARIA;
        }

    }

}
