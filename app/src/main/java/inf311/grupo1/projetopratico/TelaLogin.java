package inf311.grupo1.projetopratico;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TelaLogin extends AppCompatActivity
{

    private  boolean is_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button btn2 = (Button) findViewById(R.id.user_btn);
        //btn2.setBackgroundColor(getResources().getColor(R.color.white));
        //TODO fazer o sistema visual de toogle dos botões

        is_admin=false;
    }

    public void set_as_user(View v)
    {
        is_admin=false;
    }

    public void set_as_admin(View v)
    {
        is_admin=true;
    }
}
