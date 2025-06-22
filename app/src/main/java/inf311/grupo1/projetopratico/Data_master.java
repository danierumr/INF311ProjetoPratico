package inf311.grupo1.projetopratico;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Data_master

{

    public static String user_id="85";

    public static String origem = "7";
    public static String token = "b502570a7d57926038efe5c95a234b18";

    public static  boolean admin = false;




    public static String do_api_call(@NonNull HashMap<String, Object> params, String url) throws IOException
    {
        StringBuilder sbParams = new StringBuilder();
        int i = 0;
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


        URL urlObj = new URL(url);
        //Log.w("myapp","Api call...");
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
        Log.d("test", "result from server: " + result.toString());

        return  result.toString();
    }


    public static ArrayList<Contato> get_id_contacts(JSONObject obj) throws org.json.JSONException
    {

        var users =obj.getJSONObject("dados").getJSONArray("dados");
        ArrayList<Contato> conts = new ArrayList<Contato>();

        for(int a=0;a<users.length();a++)
        {

            var ob = users.getJSONObject(a);

                /*if(ob.getString("nome").equals("Joao android III"))
                {
                     Log.w("myApp",ob.toString());
                }

                else{
                    continue;
                }*/







            var ob_custom = ob.getJSONObject("camposPersonalizados");

            if(ob_custom.isNull("campopersonalizado_3_compl_cont")){continue;}


            var ob_array = ob_custom.
                    getJSONArray("campopersonalizado_3_compl_cont");



            boolean user = false;

            for(int b=0;b< ob_array.length();b++)
            {
                if(Objects.equals(ob_array.getString(b), Data_master.user_id))
                {
                    user=true;
                    break;
                }
            }

            if(user)
            {
                Contato c = new Contato(users.getJSONObject(a));
                conts.add(c);
            }

        }




        return  conts;
    }

    public static ArrayList<Contato> get_leads()
    {
        //{"success":true,"dados":{"qtdTotal":21,"dados":[{"id":"69","pessoaprincipal_id":"69","codigo":null,"cidade_id":null,"endereco":null,"cep":null,"numero":null,"bairro":null,"sexo_id":null,"cor_id":null,"nome":"Teste","nomeSocial":null,"telefone":null,"email":"teste@teste.com","origem":"7","grauinstrucao_id":null,"profissao":null,"escolaorigem":"teste","anoformacao":null,"outrasdeficiencias":null,"aluno":"-1","exaluno":"-1","canhoto":"-1","origemNome":"Teste","criadoEm":"2025-06-10 10:11:15","cpf":null,"dataNascimento":null,"idade":null,"desinscreveu":"-1","usuario_id":null,"campopersonalizado_1_compl_cont":null,"campopersonalizado_2_compl_cont":null,"campopersonalizado_3_compl_cont":null,"camposPersonalizados":{"campopersonalizado_1_compl_cont":null,"campopersonalizado_2_compl_cont":null,"campopersonalizado_3_compl_cont":null}}
        ArrayList<Contato> conts = new ArrayList<Contato>();

        String url = "";

        HashMap<String, Object> params = new HashMap<>();
        params.put("origem", origem);
        params.put("token", token);
        String ret = "";

        try {
            ret = do_api_call(params,"https://crmufvgrupo1.apprubeus.com.br/api/Pessoa/listarPessoas");

            Log.w("myApp",ret);
        }
        catch (IOException c)
        {
            return  null;
        }


        JSONObject obj = null;

        try
        {
            obj = new JSONObject(ret);
            conts = get_id_contacts(obj);
        }

        catch (org.json.JSONException j) {
            Log.w("myApp",j.toString());
            return null;
        }

        if(!conts.isEmpty())
        {
            for(Contato c:conts)
            {
                try {
                    params.put("id",c.id);
                    ret = do_api_call(params,"https://crmufvgrupo1.apprubeus.com.br/api/Contato/listarOportunidades");

                    try
                    {
                        JSONObject objo = new JSONObject(ret);

                        if(objo.getBoolean("success"))
                        {
                            c.interesse = objo.getJSONArray("dados").getJSONObject(0).getString("etapaNome");
                        }


                    }

                    catch (org.json.JSONException jxe)
                    {

                    }

                    Log.w("myApp",ret);
                }
                catch (IOException cx)
                {

                }
            }
        }






        return  conts;


    }


    public static  int get_processos()
    {



        int[] idx = new  int[]{26};

        HashMap<String, Object> params = new HashMap<>();
        params.put("origem", origem);
        params.put("token", token);
        params.put("id", 26);
        String ret = "";





        var conts = get_leads();

        int[] ar = new int[conts.size()];






        for (int a=0;a<ar.length;a++)
        {
            ar[a] = conts.get(a).id;
        }




        for(int a=0;a<ar.length;a++)
        {
            try {
                params.put("id",ar[a]);
                ret = do_api_call(params,"https://crmufvgrupo1.apprubeus.com.br/api/Contato/listarOportunidades");

                Log.w("myApp",ret);
            }
            catch (IOException c)
            {

            }
        }




        return  0;
    }



}
