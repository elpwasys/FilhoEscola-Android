package br.com.wasys.filhoescola.model;

import java.io.Serializable;

/**
 * Created by bruno on 13/07/17.
 */

public class EscolaModel implements Serializable {

    public Long id;
    public String nome;
    public String logo;
    public ImagemModel imagem;

}
