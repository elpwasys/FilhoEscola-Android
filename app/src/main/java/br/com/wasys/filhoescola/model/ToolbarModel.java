package br.com.wasys.filhoescola.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bruno on 12/08/17.
 */

public class ToolbarModel implements Serializable {
    private List<ButtonModel> buttons;

    public List<ButtonModel> getButtons() {
        return buttons;
    }

    public void setButtons(List<ButtonModel> buttons) {
        this.buttons = buttons;
    }
}
