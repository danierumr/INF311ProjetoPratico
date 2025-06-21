package inf311.grupo1.projetopratico;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import inf311.grupo1.projetopratico.services.LeadsDataProvider;
import inf311.grupo1.projetopratico.utils.AppConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class LeadsFragment extends Fragment {
    
    private static final String TAG = "LeadsFragment";
    
    private List<Contato> contatos;
    private List<Contato> all_contatos;
    private HashMap<Integer, Contato> cont_dict;
    
    // Informações do usuário
    private boolean isAdmin = false;
    private String userEmail;
    private String userUid;
    
    // UI Elements
    private SearchView searchView;
    private FloatingActionButton fabAddLead;
    private LinearLayout leadsContainer;
    
    // Filter chips
    private Chip chipTodos, chipNovos, chipContatados, chipInteressados, 
                chipAgendados, chipVisitaram, chipMatriculados;
    
    // Estado de cadastro ativo
    private static boolean isCadastroAtivo = false;
    
    // Serviços de dados
    private LeadsDataProvider leadsDataProvider;
    
    // Filtro atual
    private String currentFilter = "todos";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "LeadsFragment onCreateView");
        
        // Inflar o layout do fragment
        View view = inflater.inflate(R.layout.fragment_leads, container, false);
        
        // Inicializar serviços
        initializeServices();
        
        // Obter informações do usuário dos argumentos
        getUserDataFromArguments();
        
        Log.d(TAG, "LeadsFragment iniciado para usuário: " + userEmail);
        
        return view;
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Inicializar dados
        initializeData();
        
        // Inicializar UI
        initializeUI(view);
        
        // Configurar listeners
        setupListeners();
        
        // Carregar leads
        loadLeads();
    }
    
    /**
     * Inicializa os serviços de dados
     */
    private void initializeServices() {
        leadsDataProvider = LeadsDataProvider.getInstance();
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
     * Inicializa as estruturas de dados
     */
    private void initializeData() {
        all_contatos = new ArrayList<>();
        contatos = new ArrayList<>();
        cont_dict = new HashMap<>();
        
        Log.d(TAG, "Estruturas de dados inicializadas");
    }
    
    /**
     * Inicializa os elementos da UI
     */
    private void initializeUI(View view) {
        searchView = view.findViewById(R.id.lead_search_bar);
        fabAddLead = view.findViewById(R.id.fab_add_lead);
        leadsContainer = view.findViewById(R.id.lead_scroll_linear2);
        
        // Inicializar chips de filtro
        chipTodos = view.findViewById(R.id.leads_filter1);
        chipNovos = view.findViewById(R.id.leads_filter2);
        chipContatados = view.findViewById(R.id.leads_filter3);
        chipInteressados = view.findViewById(R.id.leads_filter4);
        chipAgendados = view.findViewById(R.id.leads_filter5);
        chipVisitaram = view.findViewById(R.id.leads_filter6);
        chipMatriculados = view.findViewById(R.id.leads_filter7);
        
        Log.d(TAG, "UI inicializada");
    }
    
    /**
     * Configura os listeners dos elementos
     */
    private void setupListeners() {
        // Search listener
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    filterLeads(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (newText.isEmpty()) {
                        filterLeads("");
                    }
                    return false;
                }
            });
        }
        
        // FAB listener
        if (fabAddLead != null) {
            fabAddLead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToNovoLead();
                }
            });
        }
        
        // Filter chips listeners
        setupFilterChips();
        
        Log.d(TAG, "Listeners configurados");
    }
    
    /**
     * Configura os chips de filtro
     */
    private void setupFilterChips() {
        View.OnClickListener filterListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFilterClick((Chip) v);
            }
        };
        
        if (chipTodos != null) chipTodos.setOnClickListener(filterListener);
        if (chipNovos != null) chipNovos.setOnClickListener(filterListener);
        if (chipContatados != null) chipContatados.setOnClickListener(filterListener);
        if (chipInteressados != null) chipInteressados.setOnClickListener(filterListener);
        if (chipAgendados != null) chipAgendados.setOnClickListener(filterListener);
        if (chipVisitaram != null) chipVisitaram.setOnClickListener(filterListener);
        if (chipMatriculados != null) chipMatriculados.setOnClickListener(filterListener);
        
        Log.d(TAG, "Chips de filtro configurados");
    }
    
    /**
     * Manipula clique nos chips de filtro
     */
    private void handleFilterClick(Chip clickedChip) {
        resetAllChips();
        clickedChip.setChecked(true);
        
        String filtro = clickedChip.getText().toString().toLowerCase();
        applyFilter(filtro);
        
        Log.d(TAG, "Filtro aplicado: " + filtro);
    }
    
    /**
     * Carrega e exibe os leads usando o provedor de dados
     */
    private void loadLeads() {
        try {
            // Obter leads do provedor de dados
            if ("todos".equals(currentFilter)) {
                all_contatos = leadsDataProvider.getAllLeads(userEmail, isAdmin);
                contatos = new ArrayList<>(all_contatos);
            } else {
                contatos = leadsDataProvider.getLeadsByStatus(userEmail, isAdmin, currentFilter);
                if (all_contatos.isEmpty()) {
                    all_contatos = leadsDataProvider.getAllLeads(userEmail, isAdmin);
                }
            }
            
            // Limpar e recriar cards
            clearCards();
            
            for (Contato contato : contatos) {
                add_lead_card(contato);
            }
            
            Log.d(TAG, "Leads carregados: " + contatos.size() + " (filtro: " + currentFilter + ")");
            
        } catch (Exception e) {
            Log.e(TAG, "Erro ao carregar leads", e);
            showError("Erro ao carregar leads");
            
            // Fallback para lista vazia
            contatos = new ArrayList<>();
            all_contatos = new ArrayList<>();
            clearCards();
        }
    }
    
    /**
     * Aplica filtro baseado no chip selecionado
     */
    private void applyFilter(String filtro) {
        currentFilter = filtro;
        loadLeads();
        
        Log.d(TAG, "Filtro aplicado: " + filtro);
    }
    
    /**
     * Filtra leads por texto de busca
     */
    public void filterLeads(String query) {
        try {
            if (query == null || query.trim().isEmpty()) {
                // Se não há busca, aplicar filtro atual
                loadLeads();
                return;
            }
            
            // Buscar leads usando o provedor
            List<Contato> resultados = leadsDataProvider.searchLeads(userEmail, isAdmin, query.trim());
            
            // Atualizar lista de contatos
            contatos = resultados;
            
            // Limpar e recriar cards
            clearCards();
            
            for (Contato contato : contatos) {
                add_lead_card(contato);
            }
            
            Log.d(TAG, "Busca realizada: '" + query + "' - " + resultados.size() + " resultados");
            
        } catch (Exception e) {
            Log.e(TAG, "Erro ao filtrar leads", e);
            showError("Erro ao buscar leads");
        }
    }
    
    /**
     * Atualiza os dados dos leads
     */
    public void refreshLeads() {
        Log.d(TAG, "Atualizando dados dos leads");
        currentFilter = "todos";
        resetAllChips();
        if (chipTodos != null) {
            chipTodos.setChecked(true);
        }
        loadLeads();
    }
    
    /**
     * Obtém estatísticas dos leads
     */
    public void loadLeadsStats() {
        try {
            LeadsDataProvider.LeadsStats stats = leadsDataProvider.getLeadsStats(userEmail, isAdmin);
            
            // TODO: Implementar exibição das estatísticas na interface
            // Por exemplo, atualizar TextViews com os valores das estatísticas
            
            Log.d(TAG, "Estatísticas carregadas - Total: " + stats.getTotal() +
                      ", Novos: " + stats.getNovos() +
                      ", Interessados: " + stats.getInteressados() +
                      ", Matriculados: " + stats.getMatriculados());
                      
        } catch (Exception e) {
            Log.e(TAG, "Erro ao carregar estatísticas dos leads", e);
        }
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
     * Navega para a tela de novo lead
     */
    private void navigateToNovoLead() {
        if (getActivity() instanceof MainActivityNova) {
            MainActivityNova mainActivity = (MainActivityNova) getActivity();
            mainActivity.novo_lead(null); // Usar o método da MainActivity
            Log.d(TAG, "Navegando para novo lead");
        }
    }
    
    /**
     * Controla o estado do FAB baseado no cadastro ativo
     */
    private void setFabEnabled(boolean enabled) {
        if (fabAddLead != null) {
            fabAddLead.setEnabled(enabled);
            fabAddLead.setAlpha(enabled ? 1.0f : 0.5f);
        }
    }
    
    /**
     * Define se o cadastro está ativo
     */
    public static void setCadastroAtivo(boolean ativo) {
        isCadastroAtivo = ativo;
    }
    
    /**
     * Verifica se o cadastro está ativo
     */
    public static boolean isCadastroAtivo() {
        return isCadastroAtivo;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        
        if (isCadastroAtivo()) {
            setFabEnabled(false);
            Log.d(TAG, "FAB desabilitado - cadastro ativo");
        } else {
            setFabEnabled(true);
            Log.d(TAG, "FAB habilitado");
        }
    }

    /**
     * Adiciona um card de lead na interface
     */
    public void add_lead_card(Contato cont) {
        if (getContext() == null || leadsContainer == null) {
            Log.e(TAG, "Context ou leadsContainer é null");
            return;
        }
        
        int dp_16 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        int dp_12 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());
        int dp_8 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        int dp_4 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());

        String st_name = cont.nome;
        String st_email = cont.email;
        String st_phone = cont.telefone;
        String st_interest = cont.interesse;
        String st_time = DateUtils.getRelativeTimeSpanString(cont.ultimo_contato.getTime()).toString();

        CardView cv = new CardView(getContext());
        cv.setId(View.generateViewId());
        CardView.LayoutParams cvl = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
        cvl.setMargins(0, 0, 0, dp_12);
        cv.setCardElevation(dp_4);
        cv.setRadius(dp_8);
        cv.setLayoutParams(cvl);

        RelativeLayout rl = new RelativeLayout(getContext());
        rl.setId(View.generateViewId());
        rl.setPadding(dp_16, dp_16, dp_16, dp_16);

        TextView name = new TextView(getContext());
        name.setId(View.generateViewId());
        RelativeLayout.LayoutParams nameParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        name.setLayoutParams(nameParams);
        name.setText(st_name);
        name.setTypeface(name.getTypeface(), Typeface.BOLD);
        name.setTextSize(16);

        TextView time = new TextView(getContext());
        time.setId(View.generateViewId());
        RelativeLayout.LayoutParams timeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        timeParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        time.setLayoutParams(timeParams);
        time.setText(st_time);
        time.setTextSize(12);

        TextView email = new TextView(getContext());
        email.setId(View.generateViewId());
        RelativeLayout.LayoutParams emailParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        emailParams.addRule(RelativeLayout.BELOW, name.getId());
        emailParams.topMargin = dp_4;
        email.setLayoutParams(emailParams);
        email.setText(st_email);
        email.setTextSize(14);

        TextView phone = new TextView(getContext());
        phone.setId(View.generateViewId());
        RelativeLayout.LayoutParams phoneParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        phoneParams.addRule(RelativeLayout.BELOW, email.getId());
        phoneParams.topMargin = dp_4;
        phone.setLayoutParams(phoneParams);
        phone.setText(st_phone);
        phone.setTextSize(14);

        TextView interest = new TextView(getContext());
        interest.setId(View.generateViewId());
        RelativeLayout.LayoutParams interestParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        interestParams.addRule(RelativeLayout.BELOW, phone.getId());
        interestParams.topMargin = dp_8;
        interest.setLayoutParams(interestParams);
        interest.setText(st_interest);
        interest.setTypeface(interest.getTypeface(), Typeface.BOLD);
        interest.setTextSize(14);

        rl.addView(name);
        rl.addView(time);
        rl.addView(email);
        rl.addView(phone);
        rl.addView(interest);

        cv.addView(rl);
        leadsContainer.addView(cv);

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
     * Remove todos os cards da interface
     */
    public void clearCards() {
        if (leadsContainer != null) {
            leadsContainer.removeAllViews();
            cont_dict.clear();
            Log.d(TAG, "Cards limpos");
        }
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

    /**
     * Reseta todos os chips para estado não selecionado
     */
    private void resetAllChips() {
        if (chipTodos != null) chipTodos.setChecked(false);
        if (chipNovos != null) chipNovos.setChecked(false);
        if (chipContatados != null) chipContatados.setChecked(false);
        if (chipInteressados != null) chipInteressados.setChecked(false);
        if (chipAgendados != null) chipAgendados.setChecked(false);
        if (chipVisitaram != null) chipVisitaram.setChecked(false);
        if (chipMatriculados != null) chipMatriculados.setChecked(false);
    }
} 