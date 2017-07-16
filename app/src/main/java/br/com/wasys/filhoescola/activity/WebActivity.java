package br.com.wasys.filhoescola.activity;

import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import br.com.wasys.filhoescola.BuildConfig;
import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.endpoint.Endpoint;
import br.com.wasys.filhoescola.enumeradores.TipoFuncionario;
import br.com.wasys.filhoescola.enumeradores.TipoPagina;
import br.com.wasys.filhoescola.utils.WebViewClient;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WebActivity extends BaseActivity {

    @BindView(R.id.web) WebView webView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.navigation_view) NavigationView navigationView;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;

    public static final String BASE_URL = BuildConfig.BASE_URL + BuildConfig.BASE_CONTEXT_MOBILE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        webView.setWebViewClient(new WebViewClient(getApplicationContext()));
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setVerticalScrollBarEnabled(true);
        webView.setScrollbarFadingEnabled(true);
        webView.setVerticalScrollbarOverlay(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(false);
        webView.getSettings().setSaveFormData(false);

        TipoPagina tipo = (TipoPagina) getIntent().getSerializableExtra("tipo");

        switch (tipo){
            case INICIO:
                webView.loadUrl(BASE_URL+"aluno/inicio.xhtml");
                break;
            case MEUCADASTRO:
                webView.loadUrl(BASE_URL+"aluno/meucadastro.xhtml");
                break;
            case CONFIGURAR:
                webView.loadUrl(BASE_URL+"aluno/configuracao.xhtml");
                break;
            case AJUDA:
                webView.loadUrl(BASE_URL+"aluno/ajuda.xhtml");
                break;
            default:
                webView.loadUrl(BASE_URL+"aluno/configuracao.xhtml");
        }
        initNavigationDrawer();
    }
    public void initNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){
                    case R.id.home:
                        home();
                        break;
                    case R.id.item_meu_cadastro:
                        meuCadastro();
                        break;
                    case R.id.item_configurar:
                        configurar();
                        break;
                    case R.id.item_mensagem:
                        mensagem();
                        break;
                    case R.id.item_ajuda:
                        ajuda();
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
        TextView tv_email = (TextView)header.findViewById(R.id.tv_email);
        tv_email.setText("Nome Usuario Logado");

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close){

            @Override
            public void onDrawerClosed(View v){
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
}
