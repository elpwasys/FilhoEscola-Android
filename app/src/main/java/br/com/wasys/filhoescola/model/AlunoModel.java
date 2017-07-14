package br.com.wasys.filhoescola.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by bruno on 13/07/17.
 */

public class AlunoModel implements Serializable {

    public Long id;
    public String nome;
    public String nomeMae;
    public Date dataNascimento;
    public String foto;
    public List<MensagemModel> mensagens;
}
