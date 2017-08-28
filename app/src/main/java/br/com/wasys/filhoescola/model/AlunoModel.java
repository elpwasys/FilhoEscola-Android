package br.com.wasys.filhoescola.model;

import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.wasys.filhoescola.realm.Aluno;
import io.realm.RealmResults;

/**
 * Created by bruno on 13/07/17.
 */

public class AlunoModel implements Serializable {

    public Long id;
    public String foto;
    public String nome;
    public String nomeMae;
    public Date dataNascimento;
    public List<MensagemModel> mensagens;

    public static AlunoModel from(Aluno entity) {
        if (entity == null) {
            return null;
        }
        AlunoModel model = new AlunoModel();
        model.id = entity.getId();
        model.nome = entity.getNome();
        model.nomeMae = entity.getNomeMae();
        model.mensagens = MensagemModel.from(entity.getMensagens());
        return model;
    }

    public static List<AlunoModel> from(RealmResults<Aluno> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return null;
        }
        List<AlunoModel> models = new ArrayList<>();
        for (Aluno entity : entities) {
            AlunoModel model = AlunoModel.from(entity);
            models.add(model);
        }
        return models;
    }
}
