package inf311.grupo1.projetopratico;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.android.material.search.SearchBar;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Tela_leads extends Toolbar_activity
{

    private List<Contato> contatos;

    private List<Contato> all_contatos;
    private HashMap<Integer,Contato> cont_dict;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_leads);


        all_contatos = new ArrayList<Contato>();
        contatos = new ArrayList<Contato>();
        cont_dict = new HashMap<Integer,Contato>();

        Contato Joao_silva = new Contato("Joao silva","joaosilva@gmail.com",
                "38922285","Joao Silva pai","Matricula imediata","5 ano","Escola 1",new Date());

        Contato Ana_silva = new Contato("Ana silva","anasilva@gmail.com",
                "38913223","Joao Silva pai","Matricula imediata","8 ano","Escola 1",new Date());

        Contato Joao_android = new Contato("Joao Android II","joaoandroid2@gmail.com",
                "38923237","Joao Android","Matricula imediata","23 ano","Escola 2",new Date());

        /*contatos.add(Joao_silva);
        contatos.add(Ana_silva);
        contatos.add(Joao_android);
        all_contatos.add(Joao_silva);
        all_contatos.add(Ana_silva);
        all_contatos.add(Joao_android);*/

        var app= (App_main) getApplication();

        var cdms = app.get_leads();
        all_contatos =new ArrayList<Contato>(cdms);
        contatos = cdms;


        for(int a=0;a<contatos.size();a++)
        {
            add_lead_card(contatos.get(a));

        }

        add_spacer();

        SearchView sv = (SearchView) findViewById(R.id.lead_search_bar);

        sv.setIconified(false);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Aqui você pode lidar com o envio da pesquisa
                text_changed(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Aqui você executa a função toda vez que o texto mudar


                if(newText.matches(""))
                {
                    text_changed(newText);
                    return true;
                }

                return false;
            }
        });








    }

    public void add_lead_card(Contato cont)
    {
        int dp_16 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        int dp_12 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());
        int dp_8 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        int dp_4 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());

        String st_name = cont.nome;
        String st_alert = "Atribuido: ";
        String st_escola_serie = cont.escola + " • " + cont.serie;
        String interesse = cont.interesse;
        Date now = new Date();
        var last_cont_time = DateUtils.getRelativeTimeSpanString(cont.ultimo_contato.getTime(),now.getTime(),0);

        LinearLayout ln = findViewById(R.id.lead_scroll_linear2);


        TextView name = new TextView(this);
        name.setId(View.generateViewId());
        RelativeLayout.LayoutParams nm_para = new RelativeLayout.LayoutParams(CardView.LayoutParams.MATCH_PARENT,CardView.LayoutParams.WRAP_CONTENT);
        nm_para.topMargin=dp_4;
        name.setGravity(Gravity.START);
        name.setLayoutParams(nm_para);
        name.setText(st_name );
        name.setTypeface(name.getTypeface(), Typeface.BOLD);

        TextView atrib = new TextView(this);
        atrib.setId(View.generateViewId());
        RelativeLayout.LayoutParams al_para = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        al_para.topMargin=dp_4;
        atrib.setGravity(Gravity.END);
        atrib.setLayoutParams(al_para);
        atrib.setText(st_alert);

        TextView esc_serie = new TextView(this);
        esc_serie.setId(View.generateViewId());
        RelativeLayout.LayoutParams es_para = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        es_para.topMargin=dp_4;
        es_para.addRule(RelativeLayout.BELOW,name.getId());
        esc_serie.setGravity(Gravity.START);
        esc_serie.setLayoutParams(es_para);
        esc_serie.setText(st_escola_serie);

        TextView inter = new TextView(this);
        inter.setId(View.generateViewId());
        RelativeLayout.LayoutParams in_para = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        in_para.addRule(RelativeLayout.BELOW,esc_serie.getId());
        inter.setLayoutParams(in_para);
        inter.setText(interesse);

        TextView last_cont = new TextView(this);
        last_cont.setId(View.generateViewId());
        RelativeLayout.LayoutParams ls_para = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        ls_para.addRule(RelativeLayout.BELOW,esc_serie.getId());
        ls_para.addRule(RelativeLayout.END_OF,inter.getId());
        ls_para.setMargins(dp_8,0,0,0);
        last_cont.setLayoutParams(ls_para);
        last_cont.setText(last_cont_time);


        RelativeLayout rl = new RelativeLayout(this);
        rl.setId(View.generateViewId());
        RelativeLayout.LayoutParams rl_para = new RelativeLayout.LayoutParams(CardView.LayoutParams.WRAP_CONTENT,CardView.LayoutParams.WRAP_CONTENT);
        rl.setLayoutParams(rl_para);
        rl.setPadding(dp_16, dp_16, dp_16, dp_16);





        CardView cv = new CardView(this);
        cv.setId(View.generateViewId());
        CardView.LayoutParams cvl = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT,CardView.LayoutParams.WRAP_CONTENT);
        cvl.setMargins(dp_8,dp_4,dp_8,dp_12);
        cv.setCardElevation(dp_4);
        cv.setRadius(dp_8);

        cv.setLayoutParams(cvl);


        rl.addView(name);
        rl.addView(atrib);
        rl.addView(esc_serie);
        rl.addView(inter);
        rl.addView(last_cont);


        cv.addView(rl);
        ln.addView(cv);

        cont_dict.put(cv.getId(),cont);
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                to_details(v);
            }
        });
    }

    public void to_details(View v)
    {
        Contato c = cont_dict.get(v.getId());

        Intent i = new Intent(getBaseContext(), TelaDetalhesLead.class);
        i.putExtra("contato",c);


        startActivity(i);




    }


    public void clear_cards()
    {
        LinearLayout ln = findViewById(R.id.lead_scroll_linear2);

        cont_dict.clear();
        contatos.clear();

        ln.removeAllViews();
    }

    public void text_changed(String tg)
    {
        clear_cards();


        tg=tg.toLowerCase();


        Log.w("myApp",all_contatos.toString());






        for(int a=0;a<all_contatos.size();a++)
        {
             if(tg.matches("") || all_contatos.get(a).nome.toLowerCase().contains(tg))
             {
                     contatos.add(all_contatos.get(a));
                     add_lead_card(all_contatos.get(a));
             }
        }
    }
    public void add_spacer()
    {

        int dp_50 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        LinearLayout ln = findViewById(R.id.lead_scroll_linear2);



        View v = new View(this);

        RelativeLayout.LayoutParams v_para = new RelativeLayout.LayoutParams(CardView.LayoutParams.MATCH_PARENT,dp_50);

        v.setLayoutParams(v_para);


        ln.addView(v);
    }



}
