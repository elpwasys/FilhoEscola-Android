package br.com.wasys.filhoescola.realm;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bruno on 04/07/17.
 */

public class Mensagem extends RealmObject {
    @PrimaryKey
    private Long id;
    private Date data;
    private String assunto;
    private String conteudo;
    private String botaoLink;
    private String botaoTexto;
    private Escola escola;
    private Funcionario funcionario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getBotaoLink() {
        return botaoLink;
    }

    public void setBotaoLink(String botaoLink) {
        this.botaoLink = botaoLink;
    }

    public String getBotaoTexto() {
        return botaoTexto;
    }

    public void setBotaoTexto(String botaoTexto) {
        this.botaoTexto = botaoTexto;
    }

    public Escola getEscola() {
        return escola;
    }

    public void setEscola(Escola escola) {
        this.escola = escola;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
}