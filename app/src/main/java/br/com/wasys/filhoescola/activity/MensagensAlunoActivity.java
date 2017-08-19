package br.com.wasys.filhoescola.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.BinderThread;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Text;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.wasys.filhoescola.FilhoNaEscolaApplication;
import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.adapter.AlunoAdapter;
import br.com.wasys.filhoescola.adapter.MensagemAdapter;
import br.com.wasys.filhoescola.business.MensagemBusiness;
import br.com.wasys.filhoescola.enumeradores.Assunto;
import br.com.wasys.filhoescola.realm.Aluno;
import br.com.wasys.filhoescola.realm.Mensagem;
import br.com.wasys.filhoescola.utils.EventDecorator;
import br.com.wasys.library.utils.DateUtils;
import br.com.wasys.library.widget.AlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;

public class MensagensAlunoActivity extends BaseActivity implements OnDateSelectedListener {


    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.calendarView) MaterialCalendarView calendarView;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    private Long idAluno;
    private Long idMensagem;

    private MensagemHeadersAdapter adapter;

    private LinearLayoutManager layoutManager;

    private CalendarMode modoCalendario = CalendarMode.MONTHS;

    private RealmResults<Mensagem> mensagens;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem_aluno);

        idAluno = getIntent().getLongExtra("idAluno",0);

        if(idAluno == 0){
            idMensagem = getIntent().getLongExtra("idMensagem",0);
            idAluno = Realm.getDefaultInstance().where(Mensagem.class).equalTo("id",idMensagem).findFirst().getAluno().getId();
        }

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        calendarView.state().edit().setCalendarDisplayMode(modoCalendario)
                .commit();

        calendarView.setOnDateChangedListener(this);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == 0){
                    if(recyclerView.computeVerticalScrollOffset() > 0 && modoCalendario == CalendarMode.MONTHS) {
                        modoCalendario = CalendarMode.WEEKS;
                        calendarView.state().edit().setCalendarDisplayMode(modoCalendario).commit();
                    }
                    else if(recyclerView.computeVerticalScrollOffset() <= 50 && modoCalendario == CalendarMode.WEEKS) {
                        modoCalendario = CalendarMode.MONTHS;
                        calendarView.state().edit().setCalendarDisplayMode(modoCalendario).commit();
                    }
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = layoutManager.findFirstVisibleItemPosition();
                Mensagem mensagem = mensagens.get(position);
                if(calendarView.getSelectedDate() == null || !calendarView.getSelectedDate().getDate().equals(mensagem.getData())) {
                    for(CalendarDay daySelected : calendarView.getSelectedDates()) {
                        calendarView.setDateSelected(daySelected, false);
                    }
                    calendarView.setDateSelected(mensagem.getData(), true);
                    calendarView.setCurrentDate(mensagem.getData());
                }
            }
        });



        montaLista();
    }
    public void montaLista(){
        Aluno aluno = Realm.getDefaultInstance().where(Aluno.class).equalTo("id",idAluno).findFirst();
        mensagens = Realm.getDefaultInstance().where(Mensagem.class).equalTo("aluno.id",aluno.getId()).findAllSorted("data");
        getSupportActionBar().setTitle(aluno.getNome());
        adapter = new MensagemHeadersAdapter(mensagens, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Collection<Date> type2 = new ArrayList<Date>();
        for (Mensagem mensagem : aluno.getMensagens()) {
            type2.add(mensagem.getData());
        }

        calendarView.addDecorator(new EventDecorator(Color.RED,type2));

        recyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(adapter));
        adapter.setOnItemClickListener(new MensagemAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Long position) {
                final Mensagem obj = Realm.getDefaultInstance().where(Mensagem.class).equalTo("id",position).findFirst();
                final Dialog dialog = new Dialog(MensagensAlunoActivity.this);
                dialog.setContentView(R.layout.view_mensagem);
                ImageView imgFechar = (ImageView) dialog.findViewById(R.id.imgFechar);
                TextView txtNomeAluno = (TextView) dialog.findViewById(R.id.txtNomeAluno);
                TextView txtEscola = (TextView) dialog.findViewById(R.id.txtEscola);
                ImageView imgAssunto = (ImageView) dialog.findViewById(R.id.imgAssunto);
                TextView txtAssunto = (TextView) dialog.findViewById(R.id.txtAssunto);
                TextView txtMensagem = (TextView) dialog.findViewById(R.id.txtMensagem);
                Button btnAcao = (Button) dialog.findViewById(R.id.btnAcao);


                txtNomeAluno.setText(obj.getAluno().getNome());
                txtEscola.setText(obj.getEscola().getNome());
                imgAssunto.setImageResource(Assunto.getAssunto(obj.getAssunto()).getImagem());
                txtAssunto.setText(Assunto.getAssunto(obj.getAssunto()).toString());
                txtMensagem.setText(obj.getConteudo());

                btnAcao.setText(obj.getBotaoTexto());
                btnAcao.setTag(obj.getBotaoLink());
                btnAcao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getBotaoLink()));
                            MensagensAlunoActivity.this.startActivity(intent);
                        }catch (Exception e){
                            e.printStackTrace();
                            showSnack("Ocorreu um erro ao abrir o link, entre em contato com o administrador");
                        }
                    }
                });

                imgFechar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnAcao.setVisibility(obj.getBotaoTexto() != null && StringUtils.isNotEmpty(obj.getBotaoTexto()) ? View.VISIBLE : View.GONE);

                dialog.show();
            }
        });
        if(idMensagem != 0){
            adapter.getItemClickListener().onItemClick(idMensagem);
        }

    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        for(int x=0 ; x != mensagens.size(); x++){
            if(mensagens.get(x).getData().equals(date.getDate())){
                layoutManager.scrollToPosition(x);
                if(modoCalendario == CalendarMode.MONTHS) {
                    modoCalendario = CalendarMode.WEEKS;
                    calendarView.state().edit().setCalendarDisplayMode(modoCalendario).commit();
                }
                break;
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MensagensAlunoActivity.this,MensagensActivity.class);
        startActivity(intent);
        finish();
    }

    private class MensagemHeadersAdapter extends MensagemAdapter
            implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
        public MensagemHeadersAdapter(RealmResults<Mensagem> data, BaseActivity activity) {
            super(data, activity);
        }

        public MensagemHeadersAdapter(RealmList<Mensagem> data, BaseActivity activity) {
            super(data, activity);
        }

        @Override
        public long getHeaderId(int position) {
            final Mensagem obj = getItem(position);
            return obj.getData().getTime();
        }

        @Override
        public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_header_mensagem, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
            final Mensagem obj = getItem(position);
            View view = holder.itemView;
            TextView textView = (TextView) view.findViewById(R.id.txtTitulo);
            textView.setText(DateUtils.format(obj.getData(),"dd/MM/yyyy"));
        }
    }
}
