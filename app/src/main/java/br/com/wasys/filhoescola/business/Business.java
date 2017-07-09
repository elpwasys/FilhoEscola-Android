package br.com.wasys.filhoescola.business;

import android.content.Context;

public abstract class Business {

    protected Context context;

    public Business(Context context) {
        this.context = context.getApplicationContext();
    }
}
