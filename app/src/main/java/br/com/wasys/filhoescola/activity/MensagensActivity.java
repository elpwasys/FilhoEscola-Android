package br.com.wasys.filhoescola.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.wasys.filhoescola.BuildConfig;
import br.com.wasys.filhoescola.FilhoNaEscolaApplication;
import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.adapter.AlunoAdapter;
import br.com.wasys.filhoescola.adapter.MensagemAdapter;
import br.com.wasys.filhoescola.adapter.MensagensPagerAdapter;
import br.com.wasys.filhoescola.business.MensagemBusiness;
import br.com.wasys.filhoescola.realm.Aluno;
import br.com.wasys.library.activity.AppActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;

public class MensagensActivity extends BaseActivity {


    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.navigation_view) NavigationView navigationView;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.txtEmpty) TextView txtEmpty;

    private AlunoAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        MensagemBusiness business = new MensagemBusiness(this);
        Observable<Boolean> observable = business.buscar();
        prepare(observable)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onStart() {
                        showProgress();
                    }

                    @Override
                    public void onCompleted() {
                        hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgress();
                        e.printStackTrace();
                        showSnack(e.getMessage());
                        montaLista();
                    }

                    @Override
                    public void onNext(Boolean bol) {
                        montaLista();
                    }
                });

        initNavigationDrawer();



    }
    public void montaLista(){
        RealmResults<Aluno> alunos = Realm.getDefaultInstance().where(Aluno.class).findAll();
        if(alunos.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
        }else {
            adapter = new AlunoAdapter(alunos, this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
            txtEmpty.setVisibility(View.GONE);
            adapter.setOnItemClickListener(new AlunoAdapter.ItemClickListener() {
                @Override
                public void onItemClick(Long position) {
                    Intent intent = new Intent(MensagensActivity.this,MensagensAlunoActivity.class);
                    intent.putExtra("idAluno",position);
                    startActivity(intent);
                }
            });
        }
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
        TextView tv_email = (TextView)header.findViewById(R.id.tv_email);
        ImageView imagemView = (ImageView) header.findViewById(R.id.img_user);
        tv_email.setText(FilhoNaEscolaApplication.getDispositivoLogado().nome);
        Picasso.with(this)
                .load(BuildConfig.BASE_URL+BuildConfig.BASE_CONTEXT_FILE+FilhoNaEscolaApplication.getDispositivoLogado().imagemURI)
                .placeholder(R.mipmap.ico_usuario)
                .error(R.mipmap.ico_usuario)
                .into(imagemView);
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
