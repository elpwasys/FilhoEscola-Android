package br.com.wasys.filhoescola.enumeradores;

/**
 * Created by bruno on 04/07/17.
 */

public enum TipoVisualizacao {

    DIARIA(1),
    SEMANAL(2),
    MENSAL(3);

    private int tipo;

    TipoVisualizacao(int tipo){
        this.tipo = tipo;
    }

    public int toInt() {
        return tipo;
    }

    public static TipoVisualizacao getTipo(int tipo) {
        switch (tipo){
            case 1:
                return DIARIA;
            case 2:
                return SEMANAL;
            case 3:
                return MENSAL;
            default:
                return DIARIA;
        }
    }
}
