package inf311.grupo1.projetopratico;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TelaDashboard extends Toolbar_activity
{

    private List<Contato> contatos;
    private HashMap<Integer,Contato> cont_dict;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_dashboard);

        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
       // StrictMode.setThreadPolicy(policy);

        //@+id/dash_scroll_atencao



        contatos = new ArrayList<Contato>();
        cont_dict = new HashMap<Integer,Contato>();

        Contato Joao_silva = new Contato("Joao silva","joaosilva@gmail.com",
                "38922285","Joao Silva pai","Matricula imediata","5 ano","Escola 1",new Date());

        Contato Ana_silva = new Contato("Ana silva","anasilva@gmail.com",
                "38913223","Joao Silva pai","Matricula imediata","8 ano","Escola 1",new Date());

        Contato Joao_android = new Contato("Joao Android II","joaoandroid2@gmail.com",
                "38923237","Joao Android","Matricula imediata","23 ano","Escola 2",new Date());

        contatos.add(Joao_silva);
        contatos.add(Ana_silva);
        contatos.add(Joao_android);


        for(int a=0;a<contatos.size();a++)
        {
            add_lead_card(contatos.get(a));
        }






    }

    public void add_lead_card(Contato cont)
    {
        int dp_16 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        int dp_12 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());
        int dp_8 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        int dp_4 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());

        String st_name = cont.nome;
        String st_alert = "Urgente";
        String st_status = cont.interesse;
        String st_visita = "Agendar visita";

        LinearLayout ln = findViewById(R.id.dash_scroll_linear2);


        TextView name = new TextView(this);
        name.setId(View.generateViewId());
        RelativeLayout.LayoutParams nm_para = new RelativeLayout.LayoutParams(CardView.LayoutParams.MATCH_PARENT,CardView.LayoutParams.WRAP_CONTENT);
        nm_para.topMargin=dp_4;
        name.setGravity(Gravity.START);
        name.setLayoutParams(nm_para);
        name.setText(st_name );
        name.setTypeface(name.getTypeface(), Typeface.BOLD);

        TextView alert = new TextView(this);
        alert.setId(View.generateViewId());
        RelativeLayout.LayoutParams al_para = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        al_para.topMargin=dp_4;
        alert .setGravity(Gravity.END);
        alert .setLayoutParams(al_para);
        alert .setText(st_alert);


        TextView status_time = new TextView(this);
        status_time.setId(View.generateViewId());
        RelativeLayout.LayoutParams st_para = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        st_para.topMargin=dp_8;
        st_para.addRule(RelativeLayout.BELOW,name.getId());
        status_time.setLayoutParams(st_para);
        status_time.setText(st_status);
        status_time.setGravity(Gravity.START);

        TextView visita = new TextView(this);
        status_time.setId(View.generateViewId());
        RelativeLayout.LayoutParams vt_para = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        vt_para.topMargin=dp_8;
        vt_para.addRule(RelativeLayout.BELOW,alert.getId());
        visita.setLayoutParams(vt_para);
        visita.setText(st_visita);
        visita.setGravity(Gravity.END);







        RelativeLayout rl = new RelativeLayout(this);
        rl.setId(View.generateViewId());
        RelativeLayout.LayoutParams rl_para = new RelativeLayout.LayoutParams(CardView.LayoutParams.MATCH_PARENT,CardView.LayoutParams.WRAP_CONTENT);
        rl.setLayoutParams(rl_para);
        rl.setPadding(dp_16, dp_16, dp_16, dp_16);





        CardView cv = new CardView(this);
        cv.setId(View.generateViewId());
        CardView.LayoutParams cvl = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT,CardView.LayoutParams.WRAP_CONTENT);
        cvl.setMargins(0,0,0,dp_12);
        cv.setCardElevation(dp_4);
        cv.setRadius(dp_8);


        cv.setLayoutParams(cvl);


        rl.addView(name);
        rl.addView(alert);
        rl.addView(status_time);
        rl.addView(visita);

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


    public void metricasDaEquipe(View view) {
        Intent it = new Intent(this, MetricasDaEquipe.class);
        startActivity(it);
    }

    public void funil_cap(View view) {
        Intent it = new Intent(this, TelaFunil.class);
        startActivity(it);
    }
}
