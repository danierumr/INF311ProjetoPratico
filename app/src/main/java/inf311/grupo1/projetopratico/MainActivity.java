package inf311.grupo1.projetopratico;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
         StrictMode.setThreadPolicy(policy);
    }

    public void call_api(View v) throws IOException {

        HashMap<String, String> params = new HashMap<>();
        params.put("origem", "7");
        params.put("token", "b502570a7d57926038efe5c95a234b18");

        params.put("nome", "Joao android");
        params.put("emailPrincipal", "joaoandroid@gmail.com");

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
        //Log.d("test", "result from server: " + result.toString());

        TextView t2 = (TextView) findViewById(R.id.text2);
        t2.setText(result.toString());



    }

    public void call_api2(View v) throws IOException
    {


        HashMap<String,String> sub_hash = new HashMap<>();
        sub_hash.put("processo","1");
        sub_hash.put("idPessoaCrm","31");
        sub_hash.put("codPessoa","31");



        StringBuilder sh = new StringBuilder();
        int i = 0;
        for (String key : sub_hash.keySet()) {
            try {
                if (i != 0) {
                    sh.append("&");
                }
                sh.append(key).append("=").append(URLEncoder.encode(sub_hash.get(key), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("origem", "7");
        params.put("token", "b502570a7d57926038efe5c95a234b18");

        params.put("contato", "Atividade teste");
        params.put("vencimento", "2027-03-17 18:00:00");
        params.put("tipo", "3");
        params.put("formaContato", "0");
        params.put( "dados", sh.toString());




        StringBuilder sbParams = new StringBuilder();
        i = 0;
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

        String url = "https://crmufvgrupo1.apprubeus.com.br/api/Agendamento/cadastroApi";
        URL urlObj = new URL(url);
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
        //Log.d("test", "result from server: " + result.toString());

        TextView t2 = (TextView) findViewById(R.id.text2);
        t2.setText(result.toString());

    }

    public void call_api3(View v) throws IOException
    {




        int i = 0;
        HashMap<String, String> params = new HashMap<>();
        params.put("origem", "7");
        params.put("token", "b502570a7d57926038efe5c95a234b18");







        StringBuilder sbParams = new StringBuilder();
        i = 0;
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

        String url = "https://crmufvgrupo1.apprubeus.com.br/api/Oportunidade/listarOportunidades";
        URL urlObj = new URL(url);
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
        //Log.d("test", "result from server: " + result.toString());

        TextView t2 = (TextView) findViewById(R.id.text2);
        t2.setText(result.toString());

    }

    public void call_api4(View v) throws IOException
    {




        int i = 0;
        HashMap<String, String> params = new HashMap<>();
        params.put("origem", "7");
        params.put("token", "b502570a7d57926038efe5c95a234b18");
        params.put("id", "0");







        StringBuilder sbParams = new StringBuilder();
        i = 0;
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

        String url = "https://crmufvgrupo1.apprubeus.com.br/api/Processo/listarEtapas";
        URL urlObj = new URL(url);
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
        //Log.d("test", "result from server: " + result.toString());

        TextView t2 = (TextView) findViewById(R.id.text2);
        t2.setText(result.toString());

    }

}
