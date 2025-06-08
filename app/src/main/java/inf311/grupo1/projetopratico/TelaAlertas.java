package inf311.grupo1.projetopratico;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TelaAlertas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.tela_alertas);
    }

    public void mudar_filtro_alerta(View view) {
        Button btnTodas = findViewById(R.id.alertas_btn_todas);
        Button btnAlertas = findViewById(R.id.alertas_btn_alertas);
        Button btnOportunidades = findViewById(R.id.alertas_btn_oportunidades);

        int activeBackground = R.drawable.period_selector_active;
        int inactiveBackground = R.drawable.period_selector_inactive;

        int activeTextColor = getResources().getColor(android.R.color.white);
        int inactiveTextColor = getResources().getColor(R.color.text_primary);

        // Resetar todos para inativos
        btnTodas.setBackgroundResource(inactiveBackground);
        btnAlertas.setBackgroundResource(inactiveBackground);
        btnOportunidades.setBackgroundResource(inactiveBackground);

        btnTodas.setTextColor(inactiveTextColor);
        btnAlertas.setTextColor(inactiveTextColor);
        btnOportunidades.setTextColor(inactiveTextColor);

        // Ativar o botão clicado
        Button clicked = (Button) view;
        clicked.setBackgroundResource(activeBackground);
        clicked.setTextColor(activeTextColor);

    }

}
