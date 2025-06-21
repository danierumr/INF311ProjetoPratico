package inf311.grupo1.projetopratico;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import de.hdodenhof.circleimageview.CircleImageView;

import inf311.grupo1.projetopratico.models.UserProfile;
import inf311.grupo1.projetopratico.models.UserMetrics;
import inf311.grupo1.projetopratico.services.UserProfileService;
import inf311.grupo1.projetopratico.utils.AppConstants;

public class PerfilFragment extends Fragment {
    
    private static final String TAG = "PerfilFragment";
    
    private boolean isAdmin = false;
    private String userEmail;
    private String userUid;
    
    // Referência ao FirebaseManager
    private FirebaseManager firebaseManager;
    
    // Serviços de dados
    private UserProfileService userProfileService;
    
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "PerfilFragment onCreateView");
        
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        
        // Inicializar serviços
        initializeServices();
        
        // Obter dados do usuário dos argumentos
        getUserDataFromArguments();
        
        Log.d(TAG, "PerfilFragment iniciado para usuário: " + userEmail);
        
        return view;
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupClickListeners();
        loadUserData();
    }
    
    /**
     * Inicializa os serviços
     */
    private void initializeServices() {
        firebaseManager = FirebaseManager.getInstance();
        userProfileService = UserProfileService.getInstance();
        Log.d(TAG, "Serviços inicializados");
    }
    
    /**
     * Obtém dados do usuário dos argumentos do fragment
     */
    private void getUserDataFromArguments() {
        Bundle args = getArguments();
        if (args != null) {
            isAdmin = args.getBoolean(AppConstants.KEY_IS_ADMIN, false);
            userEmail = args.getString(AppConstants.KEY_USER_EMAIL);
            userUid = args.getString(AppConstants.KEY_USER_UID);
        }
        
        Log.d(TAG, "Dados do usuário - Email: " + userEmail + ", Admin: " + isAdmin);
    }
    
    /**
     * Inicializa os elementos da UI
     */
    private void initViews(View view) {
        // Avatar e informações básicas
        ivAvatar = view.findViewById(R.id.iv_avatar);
        ivCamera = view.findViewById(R.id.iv_camera);
        tvNome = view.findViewById(R.id.tv_nome);
        tvCargo = view.findViewById(R.id.tv_cargo);
        
        // Métricas de desempenho
        tvTotalLeads = view.findViewById(R.id.tv_total_leads);
        tvConvertidos = view.findViewById(R.id.tv_convertidos);
        tvTaxaConversao = view.findViewById(R.id.tv_taxa_conversao);
        tvEsteMes = view.findViewById(R.id.tv_este_mes);
        
        // Informações pessoais
        tvEmail = view.findViewById(R.id.tv_email);
        tvTelefone = view.findViewById(R.id.tv_telefone);
        tvCargoInfo = view.findViewById(R.id.tv_cargo_info);
        
        // Opções de configuração
        llConfigEquipe = view.findViewById(R.id.ll_config_equipe);
        llConfigConta = view.findViewById(R.id.ll_config_conta);
        llAjudaSuporte = view.findViewById(R.id.ll_ajuda_suporte);
        llSair = view.findViewById(R.id.ll_sair);
        
        Log.d(TAG, "UI inicializada");
    }
    
    /**
     * Configura os listeners dos elementos clicáveis
     */
    private void setupClickListeners() {
        if (ivCamera != null) {
            ivCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCameraClick();
                }
            });
        }
        
        if (llConfigEquipe != null) {
            llConfigEquipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onConfigEquipeClick();
                }
            });
        }
        
        if (llConfigConta != null) {
            llConfigConta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onConfigContaClick();
                }
            });
        }
        
        if (llAjudaSuporte != null) {
            llAjudaSuporte.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAjudaSuporteClick();
                }
            });
        }
        
        if (llSair != null) {
            llSair.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSairClick();
                }
            });
        }
        
        Log.d(TAG, "Listeners configurados");
    }
    
    /**
     * Carrega os dados do usuário usando o serviço
     */
    private void loadUserData() {
        Log.d(TAG, "Carregando dados do usuário");
        
        try {
            // Obter perfil completo do usuário
            UserProfile userProfile = userProfileService.getUserProfile(userUid, userEmail, isAdmin);
            
            // Atualizar informações básicas
            updateBasicInfo(userProfile);
            
            // Atualizar métricas
            updateMetrics(userProfile.getMetricas());
            
            // Obter estatísticas detalhadas
            loadDetailedStats();
            
            Log.d(TAG, "Dados do usuário carregados com sucesso");
            
        } catch (Exception e) {
            Log.e(TAG, "Erro ao carregar dados do usuário", e);
            showError("Erro ao carregar perfil");
            
            // Fallback para dados básicos
            loadBasicUserData();
        }
    }
    
    /**
     * Atualiza informações básicas do perfil
     */
    private void updateBasicInfo(UserProfile userProfile) {
        if (userProfile == null) return;
        
        if (tvNome != null) tvNome.setText(userProfile.getNomeExibicao());
        if (tvCargo != null) tvCargo.setText(userProfile.getCargoFormatado());
        if (tvEmail != null) tvEmail.setText(userProfile.getEmail());
        if (tvTelefone != null) tvTelefone.setText(userProfile.getTelefone());
        if (tvCargoInfo != null) tvCargoInfo.setText(userProfile.getCargoFormatado());
        
        Log.d(TAG, "Informações básicas atualizadas para: " + userProfile.getNomeExibicao());
    }
    
    /**
     * Atualiza as métricas de desempenho
     */
    private void updateMetrics(UserMetrics metricas) {
        if (metricas == null) return;
        
        if (tvTotalLeads != null) tvTotalLeads.setText(String.valueOf(metricas.getTotalLeads()));
        if (tvConvertidos != null) tvConvertidos.setText(String.valueOf(metricas.getConvertidos()));
        if (tvTaxaConversao != null) tvTaxaConversao.setText(metricas.getTaxaConversaoFormatted());
        if (tvEsteMes != null) tvEsteMes.setText(String.valueOf(metricas.getEsteMes()));
        
        Log.d(TAG, "Métricas atualizadas - Total: " + metricas.getTotalLeads() + 
                  ", Conversões: " + metricas.getConvertidos());
    }
    
    /**
     * Carrega estatísticas detalhadas do usuário
     */
    private void loadDetailedStats() {
        try {
            UserProfileService.UserDetailedStats stats = 
                userProfileService.getUserDetailedStats(userEmail, isAdmin);
            
            // TODO: Implementar exibição das estatísticas detalhadas na interface
            // Por exemplo, adicionar cards ou seções extras com essas informações
            
            Log.d(TAG, "Estatísticas detalhadas - Leads ativos: " + stats.getLeadsAtivos() +
                      ", Média leads/dia: " + stats.getMediaLeadsPorDia() +
                      ", Dias consecutivos: " + stats.getDiasConsecutivosAtivo());
                      
        } catch (Exception e) {
            Log.e(TAG, "Erro ao carregar estatísticas detalhadas", e);
        }
    }
    
    /**
     * Carrega dados básicos como fallback
     */
    private void loadBasicUserData() {
        Log.d(TAG, "Carregando dados básicos como fallback");
        
        if (userEmail != null) {
            if (tvEmail != null) tvEmail.setText(userEmail);
            
            String nome = userEmail.split("@")[0];
            nome = nome.substring(0, 1).toUpperCase() + nome.substring(1);
            if (tvNome != null) tvNome.setText(nome);
        }
        
        String cargo = isAdmin ? "Administrador" : "Consultor de Vendas";
        if (tvCargo != null) tvCargo.setText(cargo);
        if (tvCargoInfo != null) tvCargoInfo.setText(cargo);
        
        // Valores padrão para métricas
        if (tvTotalLeads != null) tvTotalLeads.setText("--");
        if (tvConvertidos != null) tvConvertidos.setText("--");
        if (tvTaxaConversao != null) tvTaxaConversao.setText("--");
        if (tvEsteMes != null) tvEsteMes.setText("--");
        if (tvTelefone != null) tvTelefone.setText("(--) ----------");
        
        Log.d(TAG, "Dados básicos carregados");
    }
    
    /**
     * Atualiza as métricas do usuário (método público para compatibilidade)
     */
    public void updateMetricas(int totalLeads, int convertidos, String taxaConversao, int esteMes) {
        Log.d(TAG, "Atualizando métricas: " + totalLeads + " leads, " + convertidos + " conversões");
        
        if (tvTotalLeads != null) tvTotalLeads.setText(String.valueOf(totalLeads));
        if (tvConvertidos != null) tvConvertidos.setText(String.valueOf(convertidos));
        if (tvTaxaConversao != null) tvTaxaConversao.setText(taxaConversao);
        if (tvEsteMes != null) tvEsteMes.setText(String.valueOf(esteMes));
    }
    
    /**
     * Atualiza informações pessoais do usuário (método público para compatibilidade)
     */
    public void updateUserInfo(String nome, String email, String telefone, String cargo) {
        Log.d(TAG, "Atualizando informações do usuário: " + nome);
        
        if (tvNome != null) tvNome.setText(nome);
        if (tvEmail != null) tvEmail.setText(email);
        if (tvTelefone != null) tvTelefone.setText(telefone);
        if (tvCargo != null) tvCargo.setText(cargo);
        if (tvCargoInfo != null) tvCargoInfo.setText(cargo);
    }
    
    /**
     * Recarrega os dados do perfil
     */
    public void refreshProfile() {
        Log.d(TAG, "Recarregando dados do perfil");
        loadUserData();
    }
    
    /**
     * Exibe mensagem de erro
     */
    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Handler para clique na câmera (trocar foto)
     */
    private void onCameraClick() {
        Log.d(TAG, "Clique na câmera - trocar foto");
        
        if (getContext() != null) {
            Toast.makeText(getContext(), "Funcionalidade de trocar foto", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Handler para configurações da equipe
     */
    private void onConfigEquipeClick() {
        Log.d(TAG, "Clique em configurações da equipe");
        
        if (getContext() != null) {
            if (isAdmin) {
                Toast.makeText(getContext(), "Configurações da Equipe", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Acesso restrito a administradores", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    /**
     * Handler para configurações da conta
     */
    private void onConfigContaClick() {
        Log.d(TAG, "Clique em configurações da conta");
        
        if (getContext() != null) {
            Toast.makeText(getContext(), "Configurações da Conta", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Handler para ajuda e suporte
     */
    private void onAjudaSuporteClick() {
        Log.d(TAG, "Clique em ajuda e suporte");
        
        if (getContext() != null) {
            Toast.makeText(getContext(), "Ajuda e Suporte", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Handler para sair (logout)
     */
    private void onSairClick() {
        Log.d(TAG, "Clique em sair");
        
        showLogoutDialog();
    }
    
    /**
     * Exibe dialog de confirmação de logout
     */
    private void showLogoutDialog() {
        if (getContext() == null) return;
        
        new AlertDialog.Builder(getContext())
                .setTitle("Sair")
                .setMessage("Tem certeza que deseja sair?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    performLogout();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
    
    /**
     * Executa o logout do usuário
     */
    private void performLogout() {
        Log.d(TAG, "Executando logout do Firebase");
        
        // Fazer logout do Firebase
        if (firebaseManager != null) {
            firebaseManager.signOut();
            Log.d(TAG, "Logout do Firebase realizado");
        }
        
        // Mostrar mensagem de confirmação
        if (getContext() != null) {
            Toast.makeText(getContext(), "Logout realizado com sucesso", Toast.LENGTH_SHORT).show();
        }
        
        // Redirecionar para tela de login
        redirectToLogin();
    }
    
    /**
     * Redireciona para a tela de login e limpa o stack de activities
     */
    private void redirectToLogin() {
        if (getContext() != null) {
            Intent intent = new Intent(getContext(), TelaLogin.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            
            // Finalizar a activity atual
            if (getActivity() != null) {
                getActivity().finish();
            }
            
            Log.d(TAG, "Redirecionamento para TelaLogin executado");
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "PerfilFragment destruído");
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