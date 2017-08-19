package br.com.wasys.filhoescola.model;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Created by bruno on 13/07/17.
 */

public class EscolaModel implements Serializable {

    public Long id;
    public String nome;
    public ImagemModel logo;

}
