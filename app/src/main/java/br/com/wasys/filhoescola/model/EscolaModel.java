package br.com.wasys.filhoescola.model;

import java.io.Serializable;
import java.nio.ByteBuffer;

import br.com.wasys.filhoescola.realm.Escola;

/**
 * Created by bruno on 13/07/17.
 */

public class EscolaModel implements Serializable {

    public Long id;
    public String nome;
    public ImagemModel logo;

    public static EscolaModel from(Escola entity) {
        if (entity == null) {
            return null;
        }
        EscolaModel model = new EscolaModel();
        model.id = entity.getId();
        model.nome = entity.getNome();
        model.logo = ImagemModel.from(entity.getImagem());
        return model;
    }
}
