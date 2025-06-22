package inf311.grupo1.projetopratico;

import android.graphics.Typeface;    
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import inf311.grupo1.projetopratico.models.DashboardMetrics;
import inf311.grupo1.projetopratico.services.DashboardDataProvider;
import inf311.grupo1.projetopratico.utils.AppConstants;
import inf311.grupo1.projetopratico.utils.App_fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DashboardFragment extends App_fragment {

    private static final String TAG = "DashboardFragment";

    private List<Contato> contatos;
    private HashMap<Integer, Contato> cont_dict;
    
    // Informações do usuário
    private boolean isAdmin = false;
    private String userEmail;
    private String userUid;
    
    // Views
    private LinearLayout dashScrollLinear2;
    
    // Serviços de dados
    private DashboardDataProvider dashboardDataProvider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "DashboardFragment onCreateView");
        
        // Inflar o layout do fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        
        // Inicializar serviços
        initializeServices();
        
        // Obter informações do usuário dos argumentos
        getUserDataFromArguments();
        
        Log.d(TAG, "DashboardFragment iniciado para usuário: " + userEmail);
        
        return view;
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Inicializar views
        initializeViews(view);
        
        // Carregar dados do dashboard
        loadDashboardData();
        
        // Configurar interface
        setupUI();
        
        // Mostrar informações do usuário
        displayUserInfo();
        
        // Configurar listeners
        setupListeners(view);
    }
    
    /**
     * Inicializa os serviços de dados
     */
    private void initializeServices() {
        dashboardDataProvider = DashboardDataProvider.getInstance();
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
     * Inicializa as views do fragment
     */
    private void initializeViews(View view) {
        dashScrollLinear2 = view.findViewById(R.id.dash_scroll_linear2);
        Log.d(TAG, "Views inicializadas");
    }
    
    /**
     * Carrega os dados do dashboard usando o provedor de dados
     */
    private void loadDashboardData() {
        // Carregar métricas do dashboard
        loadDashboardMetrics();
        
        // Carregar leads recentes
        loadRecentLeads();
        
        Log.d(TAG, "Dados do dashboard carregados");
    }
    
    /**
     * Carrega as métricas do dashboard
     */
    private void loadDashboardMetrics() {
        try {
            DashboardMetrics metrics = dashboardDataProvider.getDashboardMetrics(userEmail, isAdmin);
            displayDashboardMetrics(metrics);
            
            Log.d(TAG, "Métricas carregadas - Total leads: " + metrics.getTotalLeads());
        } catch (Exception e) {
            Log.e(TAG, "Erro ao carregar métricas do dashboard", e);
            showError("Erro ao carregar métricas");
        }
    }
    
    /**
     * Carrega os leads recentes
     */
    private void loadRecentLeads() {
        try {
            contatos = dashboardDataProvider.getLeadsRecentes(userEmail, isAdmin, 
                                                            AppConstants.LIMITE_LEADS_DASHBOARD,
                    app_pointer);
            cont_dict = new HashMap<>();
            
            Log.d(TAG, "Leads recentes carregados: " + contatos.size() + " leads");
        } catch (Exception e) {
            Log.e(TAG, "Erro ao carregar leads recentes", e);
            showError("Erro ao carregar leads");
            contatos = new ArrayList<>(); // Fallback para lista vazia
        }
    }
    
    /**
     * Exibe as métricas do dashboard na interface
     */
    private void displayDashboardMetrics(DashboardMetrics metrics) {
        // TODO: Implementar atualização das views com as métricas
        // Aqui você atualizaria os TextViews dos cards com os valores das métricas
        // Por exemplo:
        // TextView tvTotalLeads = findViewById(R.id.tv_total_leads);
        // tvTotalLeads.setText(String.valueOf(metrics.getTotalLeads()));
        
        Log.d(TAG, "Métricas exibidas - Total: " + metrics.getTotalLeads() + 
                  ", Conversões: " + metrics.getLeadsConvertidos() + 
                  ", Taxa: " + metrics.getTaxaConversao() + "%");
    }

    /**
     * Configura a interface do usuário
     */
    private void setupUI() {
        // Limpar cards existentes
        if (dashScrollLinear2 != null) {
            dashScrollLinear2.removeAllViews();
        }
        
        // Adicionar cards de leads
        if (contatos != null) {
            for (Contato contato : contatos) {
                add_lead_card(contato);
            }
        }
        
        Log.d(TAG, "Interface configurada com " + (contatos != null ? contatos.size() : 0) + " leads");
    }

    /**
     * Mostra informações do usuário autenticado
     */
    private void displayUserInfo() {
        if (userEmail != null) {
            String welcomeMessage = "Bem-vindo, " + userEmail + 
                    (isAdmin ? " (Administrador)" : " (Consultor)");
            Toast.makeText(getContext(), welcomeMessage, Toast.LENGTH_LONG).show();
            
            Log.d(TAG, "Usuário logado - Email: " + userEmail + ", Admin: " + isAdmin);
        }
    }
    
    /**
     * Configura os listeners
     */
    private void setupListeners(View view) {
        // Botão Métricas da Equipe
        View btnMetricas = view.findViewById(R.id.dash_btn_metricas);
        if (btnMetricas != null) {
            btnMetricas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToMetricas();
                }
            });
        }
        
        // Botão Funil
        View btnFunil = view.findViewById(R.id.dash_btn_funil);
        if (btnFunil != null) {
            btnFunil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToFunil();
                }
            });
        }
        
        Log.d(TAG, "Listeners configurados");
    }

    /**
     * Adiciona um card de lead na interface
     */
    public void add_lead_card(Contato cont) {
        if (getContext() == null || dashScrollLinear2 == null) {
            Log.e(TAG, "Context ou dashScrollLinear2 é null");
            return;
        }
        
        int dp_16 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        int dp_12 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());
        int dp_8 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        int dp_4 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());

        String st_name = cont.nome;
        String st_alert = determineLeadPriority(cont);
        String st_status = cont.interesse;
        String st_visita = "Agendar visita";

        TextView name = new TextView(getContext());
        name.setId(View.generateViewId());
        RelativeLayout.LayoutParams nm_para = new RelativeLayout.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
        nm_para.topMargin = dp_4;
        name.setGravity(Gravity.START);
        name.setLayoutParams(nm_para);
        name.setText(st_name);
        name.setTypeface(name.getTypeface(), Typeface.BOLD);

        TextView alert = new TextView(getContext());
        alert.setId(View.generateViewId());
        RelativeLayout.LayoutParams al_para = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        al_para.topMargin = dp_4;
        alert.setGravity(Gravity.END);
        alert.setLayoutParams(al_para);
        alert.setText(st_alert);

        TextView status_time = new TextView(getContext());
        status_time.setId(View.generateViewId());
        RelativeLayout.LayoutParams st_para = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        st_para.topMargin = dp_8;
        st_para.addRule(RelativeLayout.BELOW, name.getId());
        status_time.setLayoutParams(st_para);
        status_time.setText(st_status);
        status_time.setGravity(Gravity.START);

        TextView visita = new TextView(getContext());
        visita.setId(View.generateViewId());
        RelativeLayout.LayoutParams vt_para = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        vt_para.topMargin = dp_8;
        vt_para.addRule(RelativeLayout.BELOW, alert.getId());
        visita.setLayoutParams(vt_para);
        visita.setText(st_visita);
        visita.setGravity(Gravity.END);

        RelativeLayout rl = new RelativeLayout(getContext());
        rl.setId(View.generateViewId());
        RelativeLayout.LayoutParams rl_para = new RelativeLayout.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
        rl.setLayoutParams(rl_para);
        rl.setPadding(dp_16, dp_16, dp_16, dp_16);

        CardView cv = new CardView(getContext());
        cv.setId(View.generateViewId());
        CardView.LayoutParams cvl = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
        cvl.setMargins(0, 0, 0, dp_12);
        cv.setCardElevation(dp_4);
        cv.setRadius(dp_8);
        cv.setLayoutParams(cvl);

        rl.addView(name);
        rl.addView(alert);
        rl.addView(status_time);
        rl.addView(visita);

        cv.addView(rl);
        dashScrollLinear2.addView(cv);

        cont_dict.put(cv.getId(), cont);
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                to_details(v);
            }
        });
        
        Log.d(TAG, "Card de lead adicionado: " + cont.nome);
    }
    
    /**
     * Determina a prioridade do lead baseado nos dados
     */
    private String determineLeadPriority(Contato lead) {
        // Lógica dinâmica para determinar prioridade
        if (lead.interesse.contains("Matrícula imediata")) {
            return "Urgente";
        } else if (lead.interesse.contains("Conhecer")) {
            return "Alta";
        } else if (lead.interesse.contains("Informações")) {
            return "Média";
        } else {
            return "Baixa";
        }
    }
    
    /**
     * Atualiza os dados do dashboard
     */
    public void refreshDashboardData() {
        Log.d(TAG, "Atualizando dados do dashboard");
        loadDashboardData();
        setupUI();
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
     * Navega para os detalhes do lead
     */
    public void to_details(View v) {
        Contato c = cont_dict.get(v.getId());
        if (c != null && getActivity() instanceof MainActivityNova) {
            MainActivityNova mainActivity = (MainActivityNova) getActivity();
            mainActivity.navigateToLeadDetails(c);
            Log.d(TAG, "Navegando para detalhes do lead: " + c.nome);
        }
    }

    /**
     * Navega para a tela de métricas
     */
    private void navigateToMetricas() {
        Log.d(TAG, "Navegando para Métricas");
        
        if (getActivity() instanceof MainActivityNova) {
            MainActivityNova mainActivity = (MainActivityNova) getActivity();
            mainActivity.navigateToMetricas();
        }
    }

    /**
     * Navega para a tela de funil
     */
    private void navigateToFunil() {
        Log.d(TAG, "Navegando para Funil");
        
        if (getActivity() instanceof MainActivityNova) {
            MainActivityNova mainActivity = (MainActivityNova) getActivity();
            mainActivity.navigateToFunil();
        }
    }

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