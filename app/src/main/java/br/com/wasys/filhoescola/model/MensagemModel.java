package br.com.wasys.filhoescola.model;

import java.io.Serializable;
import java.util.Date;

import br.com.wasys.filhoescola.enumeradores.Assunto;
import br.com.wasys.filhoescola.enumeradores.StatusDispositivo;

/**
 * Created by bruno on 05/07/17.
 */

public class MensagemModel implements Serializable{

    public Long id;
    public Date data;
    public String conteudo;
    public String botaoLink;
    public String botaoTexto;
    public Assunto assunto;

    public AlunoModel aluno;
    public EscolaModel escola;
    public FuncionarioModel funcionario;

}
