package br.com.wasys.filhoescola.realm;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by bruno on 04/07/17.
 */

public class Aluno extends RealmObject {
    private Integer id;
    private String nome;
    private String nomeMae;
    private Date dataNascimento;
    private Escola escola;
    private RealmList<Mensagem> mensagens;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Escola getEscola() {
        return escola;
    }

    public void setEscola(Escola escola) {
        this.escola = escola;
    }

    public RealmList<Mensagem> getMensagens() {
        return mensagens;
    }

    public void setMensagens(RealmList<Mensagem> mensagens) {
        this.mensagens = mensagens;
    }
}
