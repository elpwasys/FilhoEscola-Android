package br.com.wasys.filhoescola.model;

import java.io.Serializable;

import br.com.wasys.filhoescola.enumeradores.TipoFuncionario;
import br.com.wasys.filhoescola.realm.Funcionario;

/**
 * Created by bruno on 13/07/17.
 */

public class FuncionarioModel implements Serializable {

    public Long id;
    public String nome;
    public String email;
    public TipoFuncionario tipo;

    public static FuncionarioModel from(Funcionario entity) {
        if (entity == null) {
            return null;
        }
        FuncionarioModel model = new FuncionarioModel();
        model.id = entity.getId();
        model.nome = entity.getNome();
        model.email = entity.getEmail();
        model.tipo = TipoFuncionario.getTipo(entity.getTipo());
        return model;
    }
}
