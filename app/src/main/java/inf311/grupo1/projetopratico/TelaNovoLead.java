package inf311.grupo1.projetopratico;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

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
import java.util.Objects;

public class TelaNovoLead extends Toolbar_activity
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

        Gson gson = new Gson();

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
        Integer[] idx = new Integer[]{Data_master.user_id};
        String[] idxs = new String[]{Data_master.user_id.toString()};



        HashMap<String, String> params_ex = new HashMap<>();
        params_ex.put("campopersonalizado_1_compl_cont",nmr);
        //params_ex.put("campopersonalizado_3_compl_cont","[\""+Data_master.user_id.toString() +"\"]");
        params_ex.put("campopersonalizado_3_compl_cont",Data_master.user_id.toString());
        String ar = "[\""+Data_master.user_id.toString() +"\"]";

        int i = 0;
        StringBuilder sbex = new StringBuilder();


        for (String key : params_ex.keySet()) {
            try {
                if (i != 0) {
                    sbex.append("&");
                }
                sbex.append(key).append("=").append(URLEncoder.encode(params_ex.get(key), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }

        Log.w("myApp",sbex.toString());





        HashMap<String, Object> params = new HashMap<>();
        params.put("origem", "7");
        params.put("token", "b502570a7d57926038efe5c95a234b18");

        params.put("nome", nma);
        params.put("emailPrincipal", email);
        //params.put("telefonePrincipal", tel);
        //params.put("escolaOrigem",nme);



        StringBuilder sbq = new StringBuilder();
        //sbq.append("{\"campopersonalizado_1_compl_cont\":" + nmr +"," + "\"campopersonalizado_3_compl_cont\":" + "85}");

        JSONObject obj = new JSONObject();
        JSONObject obj2 = new JSONObject();
        JSONArray jsr = new JSONArray();
        try {

            obj.put("campopersonalizado_1_compl_cont",nmr);
            jsr.put(Data_master.user_id);
            obj.put("campopersonalizado_3_compl_cont",jsr);


            obj2.put("origem", "7");
            obj2.put("token", "b502570a7d57926038efe5c95a234b18");

            obj2.put("nome", nma);
            obj2.put("emailPrincipal", email);
            obj2.put("telefonePrincipal", tel);
            obj2.put("escolaOrigem",nme);
            obj2.put("camposPersonalizados",obj);

            Log.w("myApp",obj2.toString());

        }
        catch (org.json.JSONException j)
        {
             Log.w("myApp","Erro de json");
        }


        //params.put("camposPersonalizados",params_ex );


        params.put("camposPersonalizados[campopersonalizado_1_compl_cont]",nmr );
        params.put("camposPersonalizados[campopersonalizado_3_compl_cont][0]",Data_master.user_id.toString() );

        //b502570a7d57926038efe5c95a234b18

        Object o = new Object();




        StringBuilder sbParams = new StringBuilder();
        i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0) {
                    sbParams.append("&");
                }
                sbParams.append(key).append("=").append(URLEncoder.encode(params.get(key).toString(), "UTF-8"));
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



        Log.w("myApp",obj2.toString());
        String paramsString =  sbParams.toString();
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
        Log.w("myAPP",result.toString());


    }


}
