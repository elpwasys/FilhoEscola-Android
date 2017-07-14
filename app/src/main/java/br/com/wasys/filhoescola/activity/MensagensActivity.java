package br.com.wasys.filhoescola.activity;

import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;

import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.adapter.MensagensPagerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MensagensActivity extends BaseActivity {


    private MensagensPagerAdapter mensagensPagerAdapter;

    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.navigation_view) NavigationView navigationView;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        mensagensPagerAdapter = new MensagensPagerAdapter(getBaseContext(),getSupportFragmentManager());

        viewPager.setAdapter(mensagensPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

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
                    case R.id.item_ajuda:
                        ajuda();
                        break;
                    case R.id.item_mensagem:
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
