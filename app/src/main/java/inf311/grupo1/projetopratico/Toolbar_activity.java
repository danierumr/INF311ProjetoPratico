package inf311.grupo1.projetopratico;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Toolbar_activity extends AppCompatActivity
{

    Toolbar tb;
    public Data_master dm;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        tb = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(tb);

        tb.setElevation(25.0f);

        dm = new Data_master();
    }

    public void novo_lead(View v)
    {
        Intent a = new Intent(getBaseContext(), TelaNovoLead.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(a);
    }

    public void leads(View v)
    {
        Intent a = new Intent(getBaseContext(), Tela_leads.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(a);
    }

    public void dashboard(View v)
    {
        Intent a = new Intent(getBaseContext(), TelaDashboard.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(a);
    }

    public void profile(View v)
    {
        Intent it = new Intent(this, Perfil.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(it);
    }

    public void alertas(View v) {
        Intent it = new Intent(this, TelaAlertas.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(it);
    }

}
