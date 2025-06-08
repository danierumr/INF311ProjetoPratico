package inf311.grupo1.projetopratico;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TelaFunil extends AppCompatActivity {

    String intervalo = "";
    Button btnSemanal, btnMensal, btnTrimestral;

    @Override
    protected void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.tela_funil);

        btnSemanal = findViewById(R.id.btn_semanal);
        btnMensal = findViewById(R.id.btn_mensal);
        btnTrimestral = findViewById(R.id.btn_trimestral);

        intervalo = "Mensal";
        update();
    }

    public void mudar_intervalo(View view) {
        Button btn = (Button) view;
        intervalo = btn.getText().toString();
        update();
    }

    private void update() {
    }
}