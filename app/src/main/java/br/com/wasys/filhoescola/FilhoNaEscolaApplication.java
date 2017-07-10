package br.com.wasys.filhoescola;

import br.com.wasys.filhoescola.model.DispositivoModel;
import br.com.wasys.library.Application;
import br.com.wasys.library.utils.PreferencesUtils;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by bruno on 05/07/17.
 */

public class FilhoNaEscolaApplication extends Application {

    private static final String AUTHORIZATION_PREFERENCES_KEY = FilhoNaEscolaApplication.class.getName() + ".authorization";
    private static final String DISPOSITIVO_PREFERENCES_KEY = FilhoNaEscolaApplication.class.getName() + ".dispositivo";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getContext());
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("data.realm")
                .build();
        Realm.setDefaultConfiguration(configuration);
    }

    public static String getAuthorization() {
        return PreferencesUtils.get(AUTHORIZATION_PREFERENCES_KEY);
    }

    public static void setAuthorization(String authorization) {
        PreferencesUtils.put(AUTHORIZATION_PREFERENCES_KEY, authorization);
    }

    public static DispositivoModel getDispositivoLogado() {
        return PreferencesUtils.getJSON(DispositivoModel.class,DISPOSITIVO_PREFERENCES_KEY);
    }

    public static void setDispositivoLogado(DispositivoModel dispositivoModel) {
        PreferencesUtils.putJSON(DISPOSITIVO_PREFERENCES_KEY, dispositivoModel);
    }

}
