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

        btnSemanal = findViewById(R.id.funil_btn_semanal);
        btnMensal = findViewById(R.id.funil_btn_mensal);
        btnTrimestral = findViewById(R.id.funil_btn_trimestral);

        intervalo = "Mensal";
        update();
    }

    public void mudar_intervalo(View view) {
        Button btnSemanal = findViewById(R.id.funil_btn_semanal);
        Button btnMensal = findViewById(R.id.funil_btn_mensal);
        Button btnTrimestral = findViewById(R.id.funil_btn_trimestral);

        int activeBackground = R.drawable.period_selector_active;
        int inactiveBackground = R.drawable.period_selector_inactive;

        int activeTextColor = getResources().getColor(android.R.color.white);
        int inactiveTextColor = getResources().getColor(R.color.text_primary);

        // Resetar estilos de todos os botões
        btnSemanal.setBackgroundResource(inactiveBackground);
        btnMensal.setBackgroundResource(inactiveBackground);
        btnTrimestral.setBackgroundResource(inactiveBackground);

        btnSemanal.setTextColor(inactiveTextColor);
        btnMensal.setTextColor(inactiveTextColor);
        btnTrimestral.setTextColor(inactiveTextColor);

        // Ativar o botão clicado
        Button clicked = (Button) view;
        clicked.setBackgroundResource(activeBackground);
        clicked.setTextColor(activeTextColor);

        intervalo = clicked.getText().toString();
        update();
    }

    private void update() {
    }
}