package br.com.wasys.filhoescola.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bruno on 04/07/17.
 */

public class Escola extends RealmObject {
    @PrimaryKey
    private Long id;
    private String nome;
    private Imagem imagem;
    private RealmList<Aluno> alunos;
    private RealmList<Mensagem> mensagens;
    private RealmList<Funcionario> funcionarios;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Imagem getImagem() {
        return imagem;
    }

    public void setImagem(Imagem imagem) {
        this.imagem = imagem;
    }

    public RealmList<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(RealmList<Aluno> alunos) {
        this.alunos = alunos;
    }

    public RealmList<Mensagem> getMensagens() {
        return mensagens;
    }

    public void setMensagens(RealmList<Mensagem> mensagens) {
        this.mensagens = mensagens;
    }

    public RealmList<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(RealmList<Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }
}
