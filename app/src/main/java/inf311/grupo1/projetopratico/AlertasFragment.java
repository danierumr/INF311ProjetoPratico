package inf311.grupo1.projetopratico;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class AlertasFragment extends Fragment {
    
    private static final String TAG = "AlertasFragment";
    
    // Informações do usuário
    private boolean isAdmin = false;
    private String userEmail;
    private String userUid;
    
    // Botões de filtro
    private Button btnTodas;
    private Button btnAlertas;
    private Button btnOportunidades;
    
    // Estado atual do filtro
    private String filtroAtual = "todas";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "AlertasFragment onCreateView");
        
        View view = inflater.inflate(R.layout.fragment_alertas, container, false);
        
        getUserDataFromArguments();
        
        Log.d(TAG, "AlertasFragment iniciado para usuário: " + userEmail);
        
        return view;
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initializeViews(view);
        setupListeners();
        mudarFiltroAlerta("todas");
    }
    
    /**
     * Obtém dados do usuário dos argumentos do fragment
     */
    private void getUserDataFromArguments() {
        Bundle args = getArguments();
        if (args != null) {
            isAdmin = args.getBoolean("is_admin", false);
            userEmail = args.getString("user_email");
            userUid = args.getString("user_uid");
        }
        
        Log.d(TAG, "Dados do usuário - Email: " + userEmail + ", Admin: " + isAdmin);
    }
    
    /**
     * Inicializa as views
     */
    private void initializeViews(View view) {
        btnTodas = view.findViewById(R.id.alertas_btn_todas);
        btnAlertas = view.findViewById(R.id.alertas_btn_alertas);
        btnOportunidades = view.findViewById(R.id.alertas_btn_oportunidades);
        
        Log.d(TAG, "Views inicializadas");
    }
    
    /**
     * Configura os listeners dos botões
     */
    private void setupListeners() {
        btnTodas.setOnClickListener(v -> mudarFiltroAlerta("todas"));
        btnAlertas.setOnClickListener(v -> mudarFiltroAlerta("alertas"));
        btnOportunidades.setOnClickListener(v -> mudarFiltroAlerta("oportunidades"));
    }
    
    /**
     * Muda o filtro de alertas
     * @param novoFiltro O novo filtro a ser aplicado
     */
    public void mudarFiltroAlerta(String novoFiltro) {
        Log.d(TAG, "Mudando filtro de '" + filtroAtual + "' para '" + novoFiltro + "'");
        
        filtroAtual = novoFiltro;
        
        resetarBotoes();
        
        switch (novoFiltro) {
            case "todas":
                ativarBotao(btnTodas);
                Log.d(TAG, "Filtro 'Todas' ativado");
                break;
            case "alertas":
                ativarBotao(btnAlertas);
                Log.d(TAG, "Filtro 'Alertas' ativado");
                break;
            case "oportunidades":
                ativarBotao(btnOportunidades);
                Log.d(TAG, "Filtro 'Oportunidades' ativado");
                break;
            default:
                Log.w(TAG, "Filtro desconhecido: " + novoFiltro);
                ativarBotao(btnTodas);
                filtroAtual = "todas";
                break;
        }
        
        filtrarAlertas(novoFiltro);
    }
    
    /**
     * Reseta todos os botões para o estado inativo
     */
    private void resetarBotoes() {
        desativarBotao(btnTodas);
        desativarBotao(btnAlertas);
        desativarBotao(btnOportunidades);
    }
    
    /**
     * Ativa um botão específico
     */
    private void ativarBotao(Button botao) {
        if (botao != null) {
            botao.setTextColor(Color.WHITE);
        }
    }
    
    /**
     * Desativa um botão específico
     */
    private void desativarBotao(Button botao) {
        if (botao != null) {
            botao.setTextColor(getResources().getColor(R.color.text_primary));
        }
    }
    
    /**
     * Filtra os alertas baseado no filtro selecionado
     */
    private void filtrarAlertas(String filtro) {
        Log.d(TAG, "Aplicando filtro: " + filtro);
        
        switch (filtro) {
            case "todas":
                break;
            case "alertas":
                break;
            case "oportunidades":
                break;
        }
    }
    
    /**
     * Getter para o filtro atual
     */
    public String getFiltroAtual() {
        return filtroAtual;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "AlertasFragment destruído");
    }
    
    /**
     * Getters para informações do usuário (compatibilidade)
     */
    public String getCurrentUserEmail() {
        return userEmail;
    }
    
    public String getCurrentUserUid() {
        return userUid;
    }
    
    public boolean isCurrentUserAdmin() {
        return isAdmin;
    }
} 