package br.com.wasys.filhoescola.model;

import java.io.Serializable;

import br.com.wasys.filhoescola.enumeradores.TipoFuncionario;

/**
 * Created by bruno on 13/07/17.
 */

public class FuncionarioModel implements Serializable {

    public Long id;
    public String nome;
    public String email;
    public TipoFuncionario tipo;
}
