package br.com.wasys.filhoescola;

import com.google.firebase.FirebaseApp;

import org.apache.commons.lang3.StringUtils;

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
        FirebaseApp.initializeApp(getApplicationContext());
    }

    public static String getAuthorization() {
        return PreferencesUtils.get(AUTHORIZATION_PREFERENCES_KEY);
    }

    public static void setAuthorization(String authorization) {
        if (StringUtils.isBlank(authorization)) {
            PreferencesUtils.remove(AUTHORIZATION_PREFERENCES_KEY);
        } else {
            PreferencesUtils.put(AUTHORIZATION_PREFERENCES_KEY, authorization);
        }
    }

    public static DispositivoModel getDispositivoLogado() {
        return PreferencesUtils.getJSON(DispositivoModel.class,DISPOSITIVO_PREFERENCES_KEY);
    }

    public static void setDispositivoLogado(DispositivoModel dispositivoModel) {
        if (dispositivoModel == null) {
            PreferencesUtils.remove(DISPOSITIVO_PREFERENCES_KEY);
        } else {
            PreferencesUtils.putJSON(DISPOSITIVO_PREFERENCES_KEY, dispositivoModel);
        }
    }
}
