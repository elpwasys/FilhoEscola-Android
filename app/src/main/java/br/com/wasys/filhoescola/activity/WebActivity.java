package br.com.wasys.filhoescola.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.com.wasys.filhoescola.BuildConfig;
import br.com.wasys.filhoescola.FilhoNaEscolaApplication;
import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.enumeradores.TipoPagina;
import br.com.wasys.filhoescola.model.ButtonModel;
import br.com.wasys.filhoescola.model.DispositivoModel;
import br.com.wasys.filhoescola.model.MessageModel;
import br.com.wasys.filhoescola.model.ViewModel;
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
    @BindView(R.id.lilToolbar)
    LinearLayout lilToolbar;
    @BindView(R.id.txtToolbar)
    TextView txtToolbar;


    private TipoPagina tipo;

    public static Intent newIntent(Context context, TipoPagina tipo) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(TipoPagina.KEY, tipo);
        return intent;
    }

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setWebContentsDebuggingEnabled(true);
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

        tipo = (TipoPagina) getIntent().getSerializableExtra(TipoPagina.KEY);
        loadPage();

        initNavigationDrawer();


    }

    public void inicializacao(ViewModel viewModel){

        txtToolbar.setText(viewModel.getTitle());
        if(viewModel.getToolbar() != null && viewModel.getToolbar().getButtons() != null && !viewModel.getToolbar().getButtons().isEmpty()) {
            lilToolbar.removeAllViews();
            for (ButtonModel buttonModel : viewModel.getToolbar().getButtons()) {
                ImageView imagemView = new ImageView(this);
                Picasso.with(this)
                        .load(BuildConfig.URL_BASE + buttonModel.getSrc())
                        .placeholder(R.mipmap.ic_exit_to_app_black_24dp)
                        .error(R.mipmap.ic_exit_to_app_black_24dp)
                        .into(imagemView);

                imagemView.setTag(buttonModel.getId());
                imagemView.setOnClickListener(onClickListener);
                imagemView.setPadding(10,0,10,0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.RIGHT;
                imagemView.setLayoutParams(params);
                lilToolbar.addView(imagemView);
            }
        }
    }

    private ImageView.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("click","javascript:Device.onTapped(" + v.getTag() + ")");
            webView.loadUrl("javascript:Device.onTapped(" + v.getTag() + ")");
        }
    };

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
        Log.d("Token",FilhoNaEscolaApplication.getAuthorization());
        switch (tipo) {
            case INICIO:
                webView.loadUrl(BuildConfig.URL_MOBILE + "aluno/configuracao.xhtml", map);
                break;
            case MEUCADASTRO:
                webView.loadUrl(BuildConfig.URL_MOBILE + "meu-cadastro.xhtml", map);
                break;
            case CONFIGURAR:
                webView.loadUrl(BuildConfig.URL_MOBILE + "aluno/configuracao.xhtml", map);
                break;
            case AJUDA:
                webView.loadUrl(BuildConfig.URL_MOBILE + "ajuda.xhtml", map);
                break;
            default:
                webView.loadUrl(BuildConfig.URL_MOBILE + "aluno/configuracao.xhtml", map);
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
                .load(BuildConfig.URL_FILE + FilhoNaEscolaApplication.getDispositivoLogado().imagemURI)
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
                            if(file != null) {
                                final String retorno = uploadImage(file);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject jsonObject = new JSONObject(retorno);
                                            hideProgress();
                                            Log.d("click","javascript:Device.onUpload(JSON.parse('" + jsonObject.toString() + "'))");
                                            webView.loadUrl("javascript:Device.onUpload(JSON.parse('" + jsonObject.toString() + "'))");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideProgress();
                                    }
                                });

                            }
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
                    try {
                        inicializacao(JacksonUtils.getObjectMapper().readValue(value, ViewModel.class));
                    } catch (Exception e) {
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
