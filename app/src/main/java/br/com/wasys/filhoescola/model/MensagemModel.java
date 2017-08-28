package br.com.wasys.filhoescola.model;

import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.wasys.filhoescola.enumeradores.Assunto;
import br.com.wasys.filhoescola.enumeradores.StatusDispositivo;
import br.com.wasys.filhoescola.realm.Mensagem;
import br.com.wasys.library.utils.TypeUtils;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by bruno on 05/07/17.
 */

public class MensagemModel implements Serializable{

    public Long id;
    public Date data;
    public Assunto assunto;

    public String conteudo;
    public String botaoLink;
    public String botaoTexto;

    public Boolean lida;

    public AlunoModel aluno;
    public EscolaModel escola;
    public FuncionarioModel funcionario;

    public static List<MensagemModel> from(RealmList<Mensagem> results) {
        if (CollectionUtils.isEmpty(results)) {
            return null;
        }
        List<MensagemModel> models = new ArrayList<>();
        for (Mensagem result : results) {
            MensagemModel model = MensagemModel.from(result);
            models.add(model);
        }
        return models;
    }

    public static List<MensagemModel> from(RealmResults<Mensagem> results) {
        if (CollectionUtils.isEmpty(results)) {
            return null;
        }
        List<MensagemModel> models = new ArrayList<>();
        for (Mensagem result : results) {
            MensagemModel model = MensagemModel.from(result);
            models.add(model);
        }
        return models;
    }

    public static MensagemModel from(Mensagem entity) {
        if (entity == null) {
            return null;
        }
        MensagemModel model = new MensagemModel();
        model.id = entity.getId();
        model.lida = entity.getLida();
        model.data = entity.getData();
        model.assunto = Assunto.getAssunto(entity.getAssunto());
        model.conteudo = entity.getConteudo();
        model.botaoLink = entity.getBotaoLink();
        model.botaoTexto = entity.getBotaoTexto();
        model.escola = EscolaModel.from(entity.getEscola());
        model.funcionario = FuncionarioModel.from(entity.getFuncionario());
        return model;
    }
}