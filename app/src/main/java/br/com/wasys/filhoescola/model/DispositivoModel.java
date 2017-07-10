package br.com.wasys.filhoescola.model;

import java.io.Serializable;
import java.util.Date;

import br.com.wasys.filhoescola.enumeradores.StatusDispositivo;

/**
 * Created by bruno on 05/07/17.
 */

public class DispositivoModel implements Serializable{

    public String uuid;
    public String nome;
    public String token;
    public String numero;
    public String prefixo;
    public Date dataNascimento;
    public StatusDispositivo status;

}
