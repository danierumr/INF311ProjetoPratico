package inf311.grupo1.projetopratico;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.TextView;

import java.util.Date;

public class TelaDetalhesLead extends Toolbar_activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_detalhes_lead);

        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        // StrictMode.setThreadPolicy(policy);

        Intent intent = getIntent();
        Contato c = intent .getParcelableExtra("contato");

        TextView nome = findViewById(R.id.detalhes_lead_name);
        nome.setText(c.nome);



        TextView escola_serie = findViewById(R.id.detalhes_lead_escola);

        escola_serie.setText(c.escola + " • " + c.serie);


        TextView responsavel = findViewById(R.id.detalhes_lead_resp_edit);
        responsavel.setText(c.responsavel);

        TextView email = findViewById(R.id.detalhes_lead_email_edit);
        email .setText(c.email);

        TextView telefone = findViewById(R.id.detalhes_lead_tel_edit);
        telefone.setText(c.telefone);

        TextView interesse = findViewById(R.id.detalhes_lead_interesse_edit);
        interesse.setText(c.interesse);

        TextView ult_cont = findViewById(R.id.detalhes_lead_ult_cont_edit);


        Date now = new Date();
        ult_cont.setText(DateUtils.getRelativeTimeSpanString(c.ultimo_contato.getTime(),now.getTime(),0));






    }
}
