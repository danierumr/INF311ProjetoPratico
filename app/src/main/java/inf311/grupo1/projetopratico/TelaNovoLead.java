package inf311.grupo1.projetopratico;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.HashMap;

public class TelaNovoLead extends AppCompatActivity
{


    private  String[] series;
    private  String[] interests ;
    private  String[] est_funil ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_novo_lead);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

         series = new String[]{"1 Ano", "2 Ano", "3 Ano", "4 Ano", "5 Ano", "6 Ano", "7 Ano", "8 Ano", "9 Ano",
        "1 EM", "2 EM", "3 EM"};

        interests =  new String[]{"Selecione","Matricula imediata","Conhecer a escola","Informações",
        "Valores","Bolsa de estudos"};

        est_funil =  new String[]{"Selecione","Novos Leads","Contatados","Interessados","Agendados",
        "Visitaram","Matriculados"};



        ArrayAdapter<String> ad_sr = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                series
        );

        ArrayAdapter<String> ad_in = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                interests
        );

        ArrayAdapter<String> ad_fn = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                est_funil
        );


        Spinner serie = (Spinner) findViewById(R.id.serie_spinner);
        Spinner sp_interests = (Spinner) findViewById(R.id.spinner_interesse);
        Spinner funil = (Spinner) findViewById(R.id.spinner_funil);

        serie.setAdapter(ad_sr);
        sp_interests.setAdapter(ad_in);
        funil.setAdapter(ad_fn);




    }


    public void register_lead(View v) throws IOException
    {

        var nma = ( (TextView)findViewById(R.id.edittext_name) ).getText().toString();

        if(nma.matches(""))
        {
            return;
        }

        var nme = ( (TextView)findViewById(R.id.edittext_escola) ).getText().toString();

        if(nme.matches(""))
        {
            return;
        }

        int serie_inx = ((Spinner)findViewById(R.id.serie_spinner)).getSelectedItemPosition();
        var serie = series[serie_inx];

        if(serie.matches(""))
        {
            return;
        }

        var nmr = ( (TextView)findViewById(R.id.edittext_nome_resp) ).getText().toString();

        if(nmr.matches(""))
        {
            return;
        }

        var email = ( (TextView)findViewById(R.id.edittext_email) ).getText().toString();

        if(email.matches(""))
        {
            return;
        }

        var tel = ( (TextView)findViewById(R.id.edittext_tel) ).getText().toString();

        if(tel.matches(""))
        {
            return;
        }

        int inte_inx = ((Spinner)findViewById(R.id.spinner_interesse)).getSelectedItemPosition();

        if(inte_inx==0)
        {
            return;
        }

        int funil_inx =  ((Spinner)findViewById(R.id.spinner_funil)).getSelectedItemPosition();

        if(funil_inx==0)
        {
            return;
        }

        var obs = ( (TextView)findViewById(R.id.edittext_obser) ).getText().toString();





        HashMap<String, String> params = new HashMap<>();
        params.put("origem", "7");
        params.put("token", "b502570a7d57926038efe5c95a234b18");

        params.put("nome", nma);
        params.put("emailPrincipal", email);
        params.put("telefonePrincipal", tel);
        params.put("escolaOrigem",nme);

        //b502570a7d57926038efe5c95a234b18


        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0) {
                    sbParams.append("&");
                }
                sbParams.append(key).append("=").append(URLEncoder.encode(params.get(key), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }

        String url = "https://crmufvgrupo1.apprubeus.com.br/api/Contato/cadastro";
        URL urlObj = new URL(url);
        Log.w("myAPP","Mandando o request");
        HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();

        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.connect();

        String paramsString = sbParams.toString();
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(paramsString);
        wr.flush();
        wr.close();

        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        Log.w("myAPP","Final do request");


    }


}
