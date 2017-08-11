package br.com.wasys.filhoescola.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.wasys.filhoescola.BuildConfig;
import br.com.wasys.filhoescola.FilhoNaEscolaApplication;
import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.endpoint.Endpoint;
import br.com.wasys.filhoescola.enumeradores.TipoFuncionario;
import br.com.wasys.filhoescola.enumeradores.TipoPagina;
import br.com.wasys.filhoescola.model.DispositivoModel;
import br.com.wasys.filhoescola.model.MessageModel;
import br.com.wasys.filhoescola.utils.ImagePicker;
import br.com.wasys.filhoescola.utils.WebViewClient;
import br.com.wasys.library.enumerator.DeviceHeader;
import br.com.wasys.library.utils.AndroidUtils;
import br.com.wasys.library.utils.JacksonUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WebActivity extends BaseActivity {

    @BindView(R.id.web)
    WebView webView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    public static final String BASE_URL = BuildConfig.BASE_URL + BuildConfig.BASE_CONTEXT_MOBILE;

    private TipoPagina tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }

        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setVerticalScrollBarEnabled(true);
        webView.setScrollbarFadingEnabled(true);
        webView.setVerticalScrollbarOverlay(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.getSettings().setDomStorageEnabled(false);
        //webView.getSettings().setSaveFormData(false);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(false);
        webView.addJavascriptInterface(new WebAppInterface(this), "android");

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100)
                    hideProgress();
                else
                    showProgress();
            }

            public void onConsoleMessage(String message, int lineNumber, String sourceID) {
                Log.d("MyApplication", message + " -- From line "
                        + lineNumber + " of "
                        + sourceID);
            }
        });
        webView.setWebViewClient(new WebViewClient(getApplicationContext()));

        tipo = (TipoPagina) getIntent().getSerializableExtra("tipo");
        loadPage();

        initNavigationDrawer();
    }

    public void loadPage() {

        Map<String, String> map = new HashMap<String, String>();
        map.put(DeviceHeader.DEVICE_SO.key, "Android");
        map.put(DeviceHeader.DEVICE_SO_VERSION.key, Build.VERSION.RELEASE);
        map.put(DeviceHeader.DEVICE_MODEL.key, Build.MODEL);
        map.put(DeviceHeader.DEVICE_IMEI.key, AndroidUtils.getIMEI(this));
        map.put(DeviceHeader.DEVICE_WIDTH.key, String.valueOf(AndroidUtils.getWidthPixels(this)));
        map.put(DeviceHeader.DEVICE_HEIGHT.key, String.valueOf(AndroidUtils.getHeightPixels(this)));
        map.put(DeviceHeader.DEVICE_APP_VERSION.key, String.valueOf(AndroidUtils.getVersionCode(this)));
        map.put(DeviceHeader.DEVICE_TOKEN.key, FilhoNaEscolaApplication.getAuthorization());

        switch (tipo) {
            case INICIO:
                webView.loadUrl(BASE_URL + "aluno/configuracao.xhtml", map);
                break;
            case MEUCADASTRO:
                webView.loadUrl(BASE_URL + "meu-cadastro.xhtml", map);
                break;
            case CONFIGURAR:
                webView.loadUrl(BASE_URL + "aluno/configuracao.xhtml", map);
                break;
            case AJUDA:
                webView.loadUrl(BASE_URL + "ajuda.xhtml", map);
                break;
            default:
                webView.loadUrl(BASE_URL + "aluno/inicio.xhtml", map);
        }
    }

    public void initNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.home:
                        tipo = TipoPagina.INICIO;
                        loadPage();
                        break;
                    case R.id.item_meu_cadastro:
                        tipo = TipoPagina.MEUCADASTRO;
                        loadPage();
                        break;
                    case R.id.item_configurar:
                        tipo = TipoPagina.CONFIGURAR;
                        loadPage();
                        break;
                    case R.id.item_mensagem:
                        mensagem();
                        break;
                    case R.id.item_ajuda:
                        tipo = TipoPagina.AJUDA;
                        loadPage();
                        break;
                    case R.id.item_limpar:
                        limparCache();
                        break;
                    case R.id.item_sair:
                        sair();
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView) header.findViewById(R.id.tv_email);
        ImageView imagemView = (ImageView) header.findViewById(R.id.img_user);
        tv_email.setText(FilhoNaEscolaApplication.getDispositivoLogado().nome);
        Picasso.with(this)
                .load(BuildConfig.BASE_URL+BuildConfig.BASE_CONTEXT_FILE+FilhoNaEscolaApplication.getDispositivoLogado().imagemURI)
                .placeholder(R.mipmap.ico_usuario)
                .error(R.mipmap.ico_usuario)
                .into(imagemView);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 234:
                final Bitmap file = ImagePicker.getImageFromResult(this, resultCode, imageReturnedIntent);
                showProgress();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final String retorno = uploadImage(file);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject jsonObject = new JSONObject(retorno);
                                        hideProgress();
                                        webView.loadUrl("javascript:Device.onUpload(JSON.parse('" + jsonObject.toString() + "'))");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;
        }
    }

    public class WebAppInterface {
        BaseActivity activity;

        WebAppInterface(BaseActivity c) {
            activity = c;
        }

        @JavascriptInterface
        public void initialize(final String value) {
            Log.i("initialize", value);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(value);
                        activity.setTitle(jsonObject.getString("title"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

        @JavascriptInterface
        public void dataUpdate(final String value) {
            Log.i("dataUpdate", value);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        DispositivoModel dispositivoModel = JacksonUtils.getObjectMapper().readValue(value, DispositivoModel.class);
                        FilhoNaEscolaApplication.setDispositivoLogado(dispositivoModel);
                        initNavigationDrawer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        @JavascriptInterface
        public void imageUpload(String value) {
            Log.i("imageUpload", value);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent chooseImageIntent = ImagePicker.getPickImageIntent(activity);
                    startActivityForResult(chooseImageIntent, 234);
                }
            });

        }

        @JavascriptInterface
        public void imageOpen(String value) {
            Log.i("imageOpen", value);
        }

        @JavascriptInterface
        public void message(final String value) {
            Log.i("message", value);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        MessageModel messageModel = JacksonUtils.getObjectMapper().readValue(value, MessageModel.class);
                        activity.showSnack(messageModel);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @JavascriptInterface
        public void progressHide() {
            Log.i("progressHide", "");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideProgress();
                }
            });
        }

        @JavascriptInterface
        public void progressShow() {
            Log.i("progressShow", "");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showProgress();
                }
            });
        }
    }
}
