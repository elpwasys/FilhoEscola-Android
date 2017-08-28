package br.com.wasys.filhoescola.model;

import java.io.Serializable;

import br.com.wasys.filhoescola.realm.Imagem;

/**
 * Created by bruno on 13/07/17.
 */

public class ImagemModel implements Serializable {

    public Long id;
    public String caminho;

    public static ImagemModel from(Imagem entity) {
        if (entity == null) {
            return null;
        }
        ImagemModel model = new ImagemModel();
        model.id = entity.getId();
        model.caminho = entity.getCaminho();
        return model;
    }
}
