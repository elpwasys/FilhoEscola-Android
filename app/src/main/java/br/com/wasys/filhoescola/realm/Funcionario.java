package br.com.wasys.filhoescola.realm;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by bruno on 04/07/17.
 */

public class Funcionario extends RealmObject {

    private Integer id;
    private String tipo;
    private String nome;
    private String email;
    private Escola escola;
    private RealmList<Mensagem> mensagens;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
