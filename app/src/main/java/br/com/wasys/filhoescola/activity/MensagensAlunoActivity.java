package br.com.wasys.filhoescola.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import br.com.wasys.filhoescola.R;
import br.com.wasys.filhoescola.adapter.MensagemAdapter;
import br.com.wasys.filhoescola.background.SyncMensagensService;
import br.com.wasys.filhoescola.enumeradores.Assunto;
import br.com.wasys.filhoescola.model.AlunoModel;
import br.com.wasys.filhoescola.model.MensagemModel;
import br.com.wasys.filhoescola.service.AlunoService;
import br.com.wasys.filhoescola.service.MensagemService;
import br.com.wasys.filhoescola.utils.EventDecorator;
import br.com.wasys.library.utils.DateUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;

public class MensagensAlunoActivity extends BaseActivity implements OnDateSelectedListener, MensagemAdapter.OnItemClickListener {


    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.calendarView) MaterialCalendarView calendarView;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    private Long idAluno;

    private AlunoModel mAluno;
    private List<MensagemModel> mMensagens;

    private MensagemHeadersAdapter adapter;

    private LinearLayoutManager layoutManager;

    private CalendarMode modoCalendario = CalendarMode.MONTHS;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem_aluno);

        idAluno = getIntent().getLongExtra("idAluno",0);

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
                MensagemModel mensagem = mMensagens.get(position);
                if(calendarView.getSelectedDate() == null || !calendarView.getSelectedDate().getDate().equals(mensagem.data)) {
                    for(CalendarDay daySelected : calendarView.getSelectedDates()) {
                        calendarView.setDateSelected(daySelected, false);
                    }
                    calendarView.setDateSelected(mensagem.data, true);
                    calendarView.setCurrentDate(mensagem.data);
                }
            }
        });

        montaLista();
    }

    public void atualizar(AlunoModel model) {

        mAluno = model;
        mMensagens = mAluno.mensagens;

        if (CollectionUtils.isNotEmpty(mMensagens)) {

            Collections.sort(mMensagens, new Comparator<MensagemModel>() {
                @Override
                public int compare(MensagemModel o1, MensagemModel o2) {
                    return o1.data.compareTo(o2.data);
                }
            });

            getSupportActionBar().setTitle(mAluno.nome);
            adapter = new MensagemHeadersAdapter(mMensagens);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            Collection<Date> type2 = new ArrayList<Date>();
            for (MensagemModel mensagem : mMensagens) {
                type2.add(mensagem.data);
            }

            calendarView.removeDecorators();
            calendarView.addDecorator(new EventDecorator(Color.RED,type2));

            recyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(adapter));

            adapter.setOnItemClickListener(this);
        }
    }

    public void montaLista(){
        showProgress();
        Observable<AlunoModel> observable = AlunoService.Async.get(idAluno);
        prepare(observable).subscribe(new Action1<AlunoModel>() {
            @Override
            public void call(AlunoModel model) {
                hideProgress();
                atualizar(model);
            }
        });
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        for(int x=0 ; x != mMensagens.size(); x++){
            if(mMensagens.get(x).data.equals(date.getDate())){
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

    @Override
    public void onItemClick(final int index, final MensagemModel model) {

        final Dialog dialog = new Dialog(MensagensAlunoActivity.this);
        dialog.setContentView(R.layout.view_mensagem);
        ImageView imgFechar = (ImageView) dialog.findViewById(R.id.imgFechar);
        TextView txtNomeAluno = (TextView) dialog.findViewById(R.id.txtNomeAluno);
        TextView txtEscola = (TextView) dialog.findViewById(R.id.txtEscola);
        ImageView imgAssunto = (ImageView) dialog.findViewById(R.id.imgAssunto);
        TextView txtAssunto = (TextView) dialog.findViewById(R.id.txtAssunto);
        TextView txtMensagem = (TextView) dialog.findViewById(R.id.txtMensagem);
        Button btnAcao = (Button) dialog.findViewById(R.id.btnAcao);

        txtNomeAluno.setText(model.funcionario.nome);
        txtEscola.setText(model.escola.nome);
        imgAssunto.setImageResource(model.assunto.getImagem());
        txtAssunto.setText(model.assunto.toString());
        txtMensagem.setText(model.conteudo);

        btnAcao.setText(model.botaoTexto);
        btnAcao.setTag(model.botaoLink);
        btnAcao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String url = model.botaoLink;
                    if(!url.contains("http")){
                        url = "http://"+url;
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
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

        btnAcao.setVisibility(StringUtils.isNotEmpty(model.botaoTexto) ? View.VISIBLE : View.GONE);

        dialog.show();

        if (BooleanUtils.isNotTrue(model.lida)) {
            Observable<Boolean> observable = MensagemService.Async.read(model.id);
            prepare(observable).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean success) {
                    if (BooleanUtils.isTrue(success)) {
                        model.lida = true;
                        adapter.notifyItemChanged(index, null);
                        //startService(new Intent(MensagensAlunoActivity.this, SyncMensagensService.class));
                    }
                }
            });
        }
    }

    private class MensagemHeadersAdapter extends MensagemAdapter implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

        private List<MensagemModel> mDataSet;

        public MensagemHeadersAdapter(List<MensagemModel> dataSet) {
            super(dataSet);
            mDataSet = dataSet;
        }

        @Override
        public long getHeaderId(int position) {
            MensagemModel model = mDataSet.get(position);
            return model.data.getTime();
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
            MensagemModel model = mDataSet.get(position);
            View view = holder.itemView;
            TextView textView = (TextView) view.findViewById(R.id.txtTitulo);
            textView.setText(DateUtils.format(model.data, "dd/MM/yyyy"));
        }
    }
}
