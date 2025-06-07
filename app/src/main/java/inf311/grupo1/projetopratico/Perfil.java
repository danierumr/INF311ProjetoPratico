package inf311.grupo1.projetopratico;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class Perfil extends AppCompatActivity {

    private CircleImageView ivAvatar;
    private ImageView ivCamera;
    private TextView tvNome;
    private TextView tvCargo;

    private TextView tvTotalLeads;
    private TextView tvConvertidos;
    private TextView tvTaxaConversao;
    private TextView tvEsteMes;

    private TextView tvEmail;
    private TextView tvTelefone;
    private TextView tvCargoInfo;

    private LinearLayout llConfigEquipe;
    private LinearLayout llConfigConta;
    private LinearLayout llAjudaSuporte;
    private LinearLayout llSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        initViews();
        setupClickListeners();
        loadUserData();
    }

    private void initViews() {
        ivAvatar = findViewById(R.id.iv_avatar);
        ivCamera = findViewById(R.id.iv_camera);
        tvNome = findViewById(R.id.tv_nome);
        tvCargo = findViewById(R.id.tv_cargo);

        tvTotalLeads = findViewById(R.id.tv_total_leads);
        tvConvertidos = findViewById(R.id.tv_convertidos);
        tvTaxaConversao = findViewById(R.id.tv_taxa_conversao);
        tvEsteMes = findViewById(R.id.tv_este_mes);

        tvEmail = findViewById(R.id.tv_email);
        tvTelefone = findViewById(R.id.tv_telefone);
        tvCargoInfo = findViewById(R.id.tv_cargo_info);

        llConfigEquipe = findViewById(R.id.ll_config_equipe);
        llConfigConta = findViewById(R.id.ll_config_conta);
        llAjudaSuporte = findViewById(R.id.ll_ajuda_suporte);
        llSair = findViewById(R.id.ll_sair);
    }

    private void setupClickListeners() {
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // implementar a funcionalidade de trocar foto
                // abrir galeria ou câmera
                Toast.makeText(Perfil.this, "Funcionalidade de trocar foto", Toast.LENGTH_SHORT).show();
            }
        });

        llConfigEquipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Perfil.this, "Configurações da Equipe", Toast.LENGTH_SHORT).show();
                // Intent intent = new Intent(Perfil.this, ConfiguracoesEquipeActivity.class);
                // startActivity(intent);
            }
        });

        llConfigConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Perfil.this, "Configurações da Conta", Toast.LENGTH_SHORT).show();
                // Intent intent = new Intent(Perfil.this, ConfiguracoesContaActivity.class);
                // startActivity(intent);
            }
        });

        llAjudaSuporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Perfil.this, "Ajuda e Suporte", Toast.LENGTH_SHORT).show();
                // Intent intent = new Intent(Perfil.this, AjudaSuporteActivity.class);
                // startActivity(intent);
            }
        });

        llSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fazer logout
                showLogoutDialog();
            }
        });
    }

    private void loadUserData() {
        // carregar dados do usuário de um arquovo ou api
        // ex:
        // tvNome.setText("Nome do Usuário");
        // tvEmail.setText("email@exemplo.com");
        // tvTelefone.setText("(11) 99999-9999");
        // tvCargo.setText("Cargo do Usuário");
        // tvCargoInfo.setText("Cargo do Usuário");

        // tvTotalLeads.setText("48");
        // tvConvertidos.setText("12");
        // tvTaxaConversao.setText("25%");
        // tvEsteMes.setText("8");
    }

    private void showLogoutDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Sair")
                .setMessage("Tem certeza que deseja sair?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    // fazer logout
                    // limpar sessão
                    // voltrar para o login
                    Toast.makeText(Perfil.this, "Logout realizado", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}