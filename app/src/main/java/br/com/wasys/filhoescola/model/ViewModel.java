package br.com.wasys.filhoescola.model;

import java.io.Serializable;

/**
 * Created by bruno on 12/08/17.
 */

public class ViewModel implements Serializable {
    private String title;
    private ToolbarModel toolbar;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ToolbarModel getToolbar() {
        return toolbar;
    }

    public void setToolbar(ToolbarModel toolbar) {
        this.toolbar = toolbar;
    }
}
