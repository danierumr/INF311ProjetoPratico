package inf311.grupo1.projetopratico;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class FunilFragment extends Fragment {
    
    private static final String TAG = "FunilFragment";
    
    // Informações do usuário
    private boolean isAdmin = false;
    private String userEmail;
    private String userUid;
    
    // Botões de intervalo
    private Button btnSemanal;
    private Button btnMensal;
    private Button btnTrimestral;
    
    // Estado atual do intervalo
    private String intervaloAtual = "semanal";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "FunilFragment onCreateView");
        
        // Inflar o layout do fragment
        View view = inflater.inflate(R.layout.fragment_funil, container, false);
        
        // Obter informações do usuário dos argumentos
        getUserDataFromArguments();
        
        Log.d(TAG, "FunilFragment iniciado para usuário: " + userEmail);
        
        return view;
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Inicializar views
        initializeViews(view);
        
        // Configurar listeners
        setupListeners();
        
        // Configurar estado inicial
        mudarIntervalo("semanal");
        
        // Executar update inicial
        update();
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
        btnSemanal = view.findViewById(R.id.funil_btn_semanal);
        btnMensal = view.findViewById(R.id.funil_btn_mensal);
        btnTrimestral = view.findViewById(R.id.funil_btn_trimestral);
        
        Log.d(TAG, "Views inicializadas");
    }
    
    /**
     * Configura os listeners dos botões
     */
    private void setupListeners() {
        btnSemanal.setOnClickListener(v -> mudarIntervalo("semanal"));
        btnMensal.setOnClickListener(v -> mudarIntervalo("mensal"));
        btnTrimestral.setOnClickListener(v -> mudarIntervalo("trimestral"));
    }
    
    /**
     * Muda o intervalo de visualização - EXATAMENTE igual ao método original
     * @param novoIntervalo O novo intervalo a ser aplicado
     */
    public void mudarIntervalo(String novoIntervalo) {
        Log.d(TAG, "Mudando intervalo de '" + intervaloAtual + "' para '" + novoIntervalo + "'");
        
        intervaloAtual = novoIntervalo;
        
        // Reset all buttons to inactive state
        resetarBotoes();
        
        // Activate the selected button
        switch (novoIntervalo) {
            case "semanal":
                ativarBotao(btnSemanal);
                Log.d(TAG, "Intervalo 'Semanal' ativado");
                break;
            case "mensal":
                ativarBotao(btnMensal);
                Log.d(TAG, "Intervalo 'Mensal' ativado");
                break;
            case "trimestral":
                ativarBotao(btnTrimestral);
                Log.d(TAG, "Intervalo 'Trimestral' ativado");
                break;
            default:
                Log.w(TAG, "Intervalo desconhecido: " + novoIntervalo);
                ativarBotao(btnSemanal);
                intervaloAtual = "semanal";
                break;
        }
        
        // Atualizar dados baseado no novo intervalo
        update();
    }
    
    /**
     * Reseta todos os botões para o estado inativo
     */
    private void resetarBotoes() {
        desativarBotao(btnSemanal);
        desativarBotao(btnMensal);
        desativarBotao(btnTrimestral);
    }
    
    /**
     * Ativa um botão específico
     */
    private void ativarBotao(Button botao) {
        if (botao != null) {
            botao.setTextColor(Color.WHITE);
            // Aqui você pode definir o background ativo se necessário
            // botao.setBackgroundResource(R.drawable.period_selector_active);
        }
    }
    
    /**
     * Desativa um botão específico
     */
    private void desativarBotao(Button botao) {
        if (botao != null) {
            botao.setTextColor(getResources().getColor(R.color.text_primary));
            // Aqui você pode definir o background inativo se necessário
            // botao.setBackgroundResource(R.drawable.period_selector_inactive);
        }
    }
    
    /**
     * Atualiza os dados do funil - EXATAMENTE igual ao método original
     */
    public void update() {
        Log.d(TAG, "Atualizando dados do funil para intervalo: " + intervaloAtual);
        
        // Aqui você implementaria a lógica para atualizar os dados do funil
        // baseado no intervalo selecionado. Por exemplo:
        
        switch (intervaloAtual) {
            case "semanal":
                // Carregar dados semanais
                Log.d(TAG, "Carregando dados semanais do funil");
                break;
            case "mensal":
                // Carregar dados mensais
                Log.d(TAG, "Carregando dados mensais do funil");
                break;
            case "trimestral":
                // Carregar dados trimestrais
                Log.d(TAG, "Carregando dados trimestrais do funil");
                break;
        }
        
        // Aqui você faria chamadas à API ou atualizaria as views com novos dados
        // Por exemplo, atualizar as progress bars, números, análises, etc.
        
        // A implementação específica dependeria de como os dados são estruturados
        // e de onde vêm (API, banco local, etc.)
        
        Log.d(TAG, "Dados do funil atualizados com sucesso");
    }
    
    /**
     * Getter para o intervalo atual
     */
    public String getIntervaloAtual() {
        return intervaloAtual;
    }
    
    /**
     * Força uma atualização dos dados
     */
    public void forceUpdate() {
        Log.d(TAG, "Forçando atualização dos dados");
        update();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "FunilFragment destruído");
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