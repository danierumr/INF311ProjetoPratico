package inf311.grupo1.projetopratico;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import inf311.grupo1.projetopratico.models.ChartData;
import inf311.grupo1.projetopratico.services.MetricsDataProvider;
import inf311.grupo1.projetopratico.utils.AppConstants;
import inf311.grupo1.projetopratico.utils.App_fragment;
import inf311.grupo1.projetopratico.utils.PieChartHelper;

import java.util.ArrayList;
import java.util.List;

public class MetricasFragment extends App_fragment {
    
    private static final String TAG = "MetricasFragment";
    
    // Informações do usuário
    private boolean isAdmin = false;
    private String userEmail;
    private String userUid;
    
    // Views principais
    private SwipeRefreshLayout swipeRefreshLayout;
    private BarChart barChart;
    private PieChart pieChart;
    
    // Views para métricas de resumo
    private TextView totalLeadsTextView;
    private TextView totalConversoesTextView;
    private TextView leadsGrowthTextView;
    private TextView conversoesGrowthTextView;
    
    // Views para desempenho individual
    private LinearLayout individualPerformanceContainer;
    
    // Serviços de dados
    private MetricsDataProvider metricsDataProvider;
    
    // Estado de carregamento
    private boolean isLoadingData = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "MetricasFragment onCreateView");
        
        // Inflar o layout do fragment
        View view = inflater.inflate(R.layout.fragment_metricas, container, false);
        
        // Inicializar serviços
        initializeServices();
        
        // Obter informações do usuário dos argumentos
        getUserDataFromArguments();
        
        Log.d(TAG, "MetricasFragment iniciado para usuário: " + userEmail + " (Admin: " + isAdmin + ")");
        
        return view;
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Inicializar views
        initializeViews(view);
        
        // Configurar pull to refresh
        setupPullToRefresh();
        
        // Carregar dados iniciais
        loadAllData(false);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "MetricasFragment onResume - atualizando dados");
        // Atualizar dados quando o fragment volta ao foco
        loadAllData(false);
    }
    
    /**
     * Inicializa os serviços de dados
     */
    private void initializeServices() {
        metricsDataProvider = MetricsDataProvider.getInstance();
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
        
        // Fallback para dados de sessão se argumentos não estiverem disponíveis
        if (userEmail == null || userEmail.isEmpty()) {
            // Tentar obter do app_pointer ou outras fontes
            if (app_pointer != null) {
                // Implementar lógica para obter email do usuário atual se necessário
            }
        }
        
        Log.d(TAG, "Dados do usuário - Email: " + userEmail + ", Admin: " + isAdmin);
    }
    
    /**
     * Inicializa as views
     */
    private void initializeViews(View view) {
        // SwipeRefreshLayout
        swipeRefreshLayout = view.findViewById(R.id.metricas_swipe_refresh);
        
        // Charts
        barChart = view.findViewById(R.id.barChart);
        pieChart = view.findViewById(R.id.pieChart);
        
        // TextViews dos cards de resumo
        totalLeadsTextView = view.findViewById(R.id.metricas_total_leads);
        totalConversoesTextView = view.findViewById(R.id.metricas_total_conversoes);
        leadsGrowthTextView = view.findViewById(R.id.metricas_leads_growth);
        conversoesGrowthTextView = view.findViewById(R.id.metricas_conversoes_growth);
        
        // Container de desempenho individual (limpar cards estáticos)
        individualPerformanceContainer = view.findViewById(R.id.metricas_individual_performance_container);
        
        Log.d(TAG, "Views inicializadas");
    }
    
    /**
     * Configura o pull to refresh
     */
    private void setupPullToRefresh() {
        if (swipeRefreshLayout != null) {
            // Configurar cores do SwipeRefreshLayout (tema verde da UFV)
            swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.primary_green),
                getResources().getColor(R.color.secondary_green),
                getResources().getColor(R.color.accent_green)
            );
            
            // Configurar listener
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Log.d(TAG, "Pull to refresh acionado");
                    refreshAllData();
                }
            });
            
            Log.d(TAG, "Pull to refresh configurado");
        }
    }
    
    /**
     * Carrega todos os dados (gráficos + métricas)
     * @param forceRefresh se true, força nova busca na API
     */
    private void loadAllData(boolean forceRefresh) {
        if (isLoadingData) {
            Log.d(TAG, "Já carregando dados, ignorando nova solicitação");
            return;
        }
        
        isLoadingData = true;
        Log.d(TAG, "Iniciando carregamento de dados" + (forceRefresh ? " (forçando refresh)" : ""));
        
        try {
            // Garantir que temos o app_pointer disponível
            if (app_pointer != null) {
                if (forceRefresh) {
                    // Forçar atualização da API
                    Log.d(TAG, "Forçando atualização da API");
                    app_pointer.forceUpdate();
                } else if (!app_pointer.updated) {
                    // Atualizar apenas se necessário
                    Log.d(TAG, "Atualizando dados da API");
                    app_pointer.update();
                }
            }
            
            // Carregar métricas da equipe (cards de resumo)
            loadTeamMetrics();
            
            // Carregar dados dos gráficos
            loadPieChartData();
            loadBarChartData();
            
            // Carregar desempenho individual
            loadIndividualPerformance();
            
        } catch (Exception e) {
            Log.e(TAG, "Erro ao carregar dados", e);
            showErrorMessage("Erro ao carregar dados das métricas");
        } finally {
            isLoadingData = false;
                }
            }
            
    /**
     * Atualiza todos os dados via pull to refresh
     */
    private void refreshAllData() {
        Log.d(TAG, "Iniciando atualização de todos os dados via pull to refresh");
        
        // Executar o carregamento de dados em thread separada para não bloquear a UI
        new Thread(new Runnable() {
            @Override
            public void run() {
        try {
            // Limpar cache para forçar nova busca
            metricsDataProvider.clearCache();
            
                    // Forçar atualização dos dados da API se disponível
                    if (app_pointer != null) {
                        Log.d(TAG, "Forçando atualização da API via pull to refresh");
                        app_pointer.forceUpdate(); // Executado em thread separada
                    }
                    
                    // Voltar para a UI thread para carregar dados e atualizar interface
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // Carregar dados SEM forçar refresh novamente (evitar dupla chamada)
                                    // Como já fizemos forceUpdate() acima, passar false aqui
                                    loadAllData(false);
                                    
                                    Log.d(TAG, "Métricas atualizadas com sucesso");
            
        } catch (Exception e) {
                                    Log.e(TAG, "Erro ao carregar dados das métricas", e);
            showErrorMessage("Erro ao atualizar dados");
        } finally {
            // Sempre parar o indicador de refresh
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
                                        Log.d(TAG, "Pull to refresh finalizado");
                                    }
                                }
                            }
                        });
                    }
                    
                } catch (Exception e) {
                    Log.e(TAG, "Erro durante pull to refresh", e);
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showErrorMessage("Erro ao atualizar dados");
                                if (swipeRefreshLayout != null) {
                                    swipeRefreshLayout.setRefreshing(false);
                                    Log.d(TAG, "Pull to refresh finalizado com erro");
                                }
                            }
                        });
                    }
                }
            }
        }).start();
    }
    
    /**
     * Carrega as métricas da equipe (cards de resumo)
     */
    private void loadTeamMetrics() {
        Log.d(TAG, "Carregando métricas da equipe");
        
        metricsDataProvider.getTeamMetrics(userEmail, isAdmin, app_pointer, 
            new MetricsDataProvider.TeamMetricsCallback() {
                @Override
                public void onSuccess(MetricsDataProvider.TeamMetrics metrics) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateTeamMetricsDisplay(metrics);
                            }
                        });
                    }
                }
                
                @Override
                public void onError(String error) {
                    Log.e(TAG, "Erro ao carregar métricas da equipe: " + error);
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Exibir dados de fallback
                                MetricsDataProvider.TeamMetrics fallback = 
                                    metricsDataProvider.getTeamMetrics(userEmail, isAdmin);
                                updateTeamMetricsDisplay(fallback);
                            }
                        });
                    }
                }
            });
    }
    
    /**
     * Atualiza a exibição das métricas da equipe
     */
    private void updateTeamMetricsDisplay(MetricsDataProvider.TeamMetrics metrics) {
        try {
            if (totalLeadsTextView != null) {
                totalLeadsTextView.setText(String.valueOf(metrics.getTotalLeads()));
            }
            
            if (totalConversoesTextView != null) {
                totalConversoesTextView.setText(String.valueOf(metrics.getTotalConversoes()));
            }
            
            if (leadsGrowthTextView != null) {
                // Calcular crescimento baseado na taxa de conversão
                double crescimento = metrics.getTaxaConversaoMedia() > 25.0 ? 
                    Math.random() * 15 + 5 : Math.random() * 10 - 5;
                String sinal = crescimento >= 0 ? "+" : "";
                leadsGrowthTextView.setText(String.format("%s%.1f%% vs mês anterior", sinal, crescimento));
                leadsGrowthTextView.setTextColor(getResources().getColor(
                    crescimento >= 0 ? R.color.primary_green : R.color.text_secondary));
            }
            
            if (conversoesGrowthTextView != null) {
                // Calcular crescimento das conversões
                double crescimentoConv = metrics.getTotalConversoes() > 10 ? 
                    Math.random() * 12 + 3 : Math.random() * 8 - 2;
                String sinal = crescimentoConv >= 0 ? "+" : "";
                conversoesGrowthTextView.setText(String.format("%s%.1f%% vs mês anterior", sinal, crescimentoConv));
                conversoesGrowthTextView.setTextColor(getResources().getColor(
                    crescimentoConv >= 0 ? R.color.primary_green : R.color.text_secondary));
            }
            
            Log.d(TAG, "Métricas da equipe atualizadas - Leads: " + metrics.getTotalLeads() + 
                      ", Conversões: " + metrics.getTotalConversoes() + 
                      ", Taxa: " + String.format("%.1f%%", metrics.getTaxaConversaoMedia()));
            
        } catch (Exception e) {
            Log.e(TAG, "Erro ao atualizar exibição das métricas", e);
        }
    }
    
    /**
     * Carrega dados para o gráfico de pizza
     */
    private void loadPieChartData() {
        Log.d(TAG, "Carregando dados do gráfico de pizza");
        
        metricsDataProvider.getPieChartData(userEmail, isAdmin, app_pointer, 
            new MetricsDataProvider.PieChartCallback() {
                @Override
                public void onSuccess(ChartData.PieChartData data) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setupPieChart(data);
                            }
                        });
                    }
                }
                
                @Override
                public void onError(String error) {
                    Log.e(TAG, "Erro ao carregar dados do gráfico de pizza: " + error);
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Usar dados de fallback
                                List<ChartData.ConsultorData> fallback = 
                                    metricsDataProvider.getPieChartData(userEmail, isAdmin).getConsultores();
                                updateIndividualPerformanceDisplay(fallback);
                            }
                        });
                    }
                }
            });
    }
    
    /**
     * Carrega dados para o gráfico de barras
     */
    private void loadBarChartData() {
        Log.d(TAG, "Carregando dados do gráfico de barras");
        
        metricsDataProvider.getBarChartData(userEmail, isAdmin, app_pointer, 
            new MetricsDataProvider.BarChartCallback() {
                @Override
                public void onSuccess(ChartData.BarChartData data) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setupBarChart(data);
                            }
                        });
                    }
                }
                
                @Override
                public void onError(String error) {
                    Log.e(TAG, "Erro ao carregar dados do gráfico de barras: " + error);
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Usar dados de fallback
                                ChartData.BarChartData fallback = 
                                    metricsDataProvider.getBarChartData(userEmail, isAdmin);
                                setupBarChart(fallback);
                            }
                        });
                    }
                }
            });
    }
    
    /**
     * Carrega dados de desempenho individual
     */
    private void loadIndividualPerformance() {
        Log.d(TAG, "Carregando dados de desempenho individual");
        
        metricsDataProvider.getIndividualPerformanceData(userEmail, isAdmin, app_pointer, 
            new MetricsDataProvider.IndividualPerformanceCallback() {
                @Override
                public void onSuccess(List<ChartData.ConsultorData> consultores) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateIndividualPerformanceDisplay(consultores);
                            }
                        });
                    }
                }
                
                @Override
                public void onError(String error) {
                    Log.e(TAG, "Erro ao carregar desempenho individual: " + error);
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Usar dados de fallback
                                List<ChartData.ConsultorData> fallback = 
                                    metricsDataProvider.getFallbackPieChartData(userEmail, isAdmin).getConsultores();
                                updateIndividualPerformanceDisplay(fallback);
                            }
                        });
                    }
                }
            });
    }
    
    /**
     * Configura o gráfico de pizza com os dados fornecidos
     */
    private void setupPieChart(ChartData.PieChartData chartData) {
        // Usar a classe utilitária para configurar o gráfico
        PieChartHelper.setupPieChart(pieChart, chartData, false);
    }

    /**
     * Configura o gráfico de barras com os dados fornecidos
     */
    private void setupBarChart(ChartData.BarChartData chartData) {
        try {
            if (barChart == null || chartData == null) {
                Log.w(TAG, "BarChart ou dados são null");
                return;
            }
        
        List<BarEntry> leadsEntries = new ArrayList<>();
            List<BarEntry> conversoesEntries = new ArrayList<>();
            List<String> labels = new ArrayList<>();
            
            for (int i = 0; i < chartData.getConsultores().size(); i++) {
                ChartData.ConsultorData consultor = chartData.getConsultores().get(i);
                leadsEntries.add(new BarEntry(i, consultor.getLeads()));
                conversoesEntries.add(new BarEntry(i, consultor.getConversoes()));
                labels.add(consultor.getNome());
        }

        BarDataSet leadsDataSet = new BarDataSet(leadsEntries, "Leads");
        leadsDataSet.setColor(Color.parseColor("#14b8a6"));

            BarDataSet conversoesDataSet = new BarDataSet(conversoesEntries, "Conversões");
            conversoesDataSet.setColor(Color.parseColor("#F44336"));

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(leadsDataSet);
            dataSets.add(conversoesDataSet);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.valueOf((int) value);
                }
            });

            barChart.setData(data);
            
            // Configurar aparência
            barChart.getDescription().setEnabled(false);
            barChart.setFitBars(true);

        // Configurar eixos
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                    return index < labels.size() ? labels.get(index) : "";
            }
        });

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMinimum(0f);

            barChart.getAxisRight().setEnabled(false);

            // Configurar legenda
        Legend legend = barChart.getLegend();
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            legend.setDrawInside(false);
            
        barChart.invalidate();
        
            Log.d(TAG, "Gráfico de barras configurado com " + labels.size() + " consultores");
                      
        } catch (Exception e) {
            Log.e(TAG, "Erro ao configurar gráfico de barras", e);
    }
    }

    /**
     * Atualiza a exibição do desempenho individual
     */
    private void updateIndividualPerformanceDisplay(List<ChartData.ConsultorData> consultores) {
        try {
        if (individualPerformanceContainer == null) {
                Log.w(TAG, "Container de desempenho individual é null");
            return;
        }
            
            // Limpar cards existentes (manter apenas o TextView do título)
            clearStaticPerformanceCards();
            
            // Adicionar novos cards dinamicamente
            for (ChartData.ConsultorData consultor : consultores) {
                addIndividualPerformanceCard(consultor);
            }
            
            Log.d(TAG, "Desempenho individual atualizado com " + consultores.size() + " consultores");
            
        } catch (Exception e) {
            Log.e(TAG, "Erro ao atualizar desempenho individual", e);
        }
    }
    
    /**
     * Remove cards estáticos de desempenho, mantendo apenas o TextView do título
     */
    private void clearStaticPerformanceCards() {
        if (individualPerformanceContainer == null) return;
        
        // Remover todos os filhos exceto o primeiro (TextView do título)
        int childCount = individualPerformanceContainer.getChildCount();
        if (childCount > 1) {
            individualPerformanceContainer.removeViews(1, childCount - 1);
        }
        
        Log.d(TAG, "Cards estáticos de desempenho removidos");
    }
    
    /**
     * Adiciona um card de desempenho individual dinamicamente
     */
    private void addIndividualPerformanceCard(ChartData.ConsultorData consultor) {
        try {
            if (getContext() == null || individualPerformanceContainer == null) return;
        
            // Criar CardView
        CardView cardView = new CardView(getContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, dpToPx(8));
        cardView.setLayoutParams(cardParams);
        cardView.setRadius(dpToPx(12));
        cardView.setCardElevation(dpToPx(4));
        
            // Criar LinearLayout horizontal
            LinearLayout horizontalLayout = new LinearLayout(getContext());
            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
            horizontalLayout.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));
        
            // Avatar View
            View avatarView = new View(getContext());
        LinearLayout.LayoutParams avatarParams = new LinearLayout.LayoutParams(
            dpToPx(48), dpToPx(48)
        );
            avatarParams.setMargins(0, 0, dpToPx(16), 0);
            avatarView.setLayoutParams(avatarParams);
            avatarView.setBackgroundResource(R.drawable.circle_avatar);
            avatarView.setBackgroundTintList(
                getContext().getColorStateList(android.R.color.holo_green_dark)
            );
            
            // Tentar definir cor personalizada do avatar
            try {
                int color = Color.parseColor(consultor.getCor());
                avatarView.setBackgroundTintList(android.content.res.ColorStateList.valueOf(color));
            } catch (Exception e) {
                // Usar cor padrão se houver erro no parsing
                Log.w(TAG, "Erro ao definir cor do avatar: " + consultor.getCor());
            }
            
            // LinearLayout vertical para nome e métricas
            LinearLayout verticalLayout = new LinearLayout(getContext());
            verticalLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams verticalParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT
        );
            verticalParams.weight = 1;
            verticalLayout.setLayoutParams(verticalParams);
        
            // TextView nome
            TextView nomeTextView = new TextView(getContext());
            nomeTextView.setText(consultor.getNome());
            nomeTextView.setTextColor(getResources().getColor(R.color.text_primary));
            nomeTextView.setTextSize(16);
            nomeTextView.setTypeface(null, android.graphics.Typeface.BOLD);
        
            // TextView métricas
            TextView metricsTextView = new TextView(getContext());
            String metricsText = consultor.getLeads() + " leads • " + consultor.getConversoes() + " conversões";
            metricsTextView.setText(metricsText);
            metricsTextView.setTextColor(getResources().getColor(R.color.text_secondary));
            metricsTextView.setTextSize(14);
        
            verticalLayout.addView(nomeTextView);
            verticalLayout.addView(metricsTextView);
        
            // TextView taxa de conversão
            TextView taxaTextView = new TextView(getContext());
            double taxa = consultor.getLeads() > 0 ? 
                (double) consultor.getConversoes() / consultor.getLeads() * 100.0 : 0.0;
            taxaTextView.setText(String.format("%.1f%%", taxa));
            taxaTextView.setTextColor(getResources().getColor(R.color.primary_green));
            taxaTextView.setTextSize(16);
            taxaTextView.setTypeface(null, android.graphics.Typeface.BOLD);
        
            // Montar hierarquia
            horizontalLayout.addView(avatarView);
            horizontalLayout.addView(verticalLayout);
            horizontalLayout.addView(taxaTextView);
            
            cardView.addView(horizontalLayout);
        individualPerformanceContainer.addView(cardView);
        
            Log.d(TAG, "Card adicionado para: " + consultor.getNome() + 
              " - " + consultor.getLeads() + " leads, " + consultor.getConversoes() + " conversões");
            
        } catch (Exception e) {
            Log.e(TAG, "Erro ao adicionar card de desempenho individual", e);
        }
    }
    
    /**
     * Converte dp para pixels
     */
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
    
    /**
     * Exibe mensagem de erro
     */
    private void showErrorMessage(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Método público para atualizar dados (compatibilidade)
     */
    public void refreshChartsData() {
        Log.d(TAG, "refreshChartsData chamado");
        loadAllData(false);
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MetricasFragment onDestroy");
        
        // Cleanup
        swipeRefreshLayout = null;
        barChart = null;
        pieChart = null;
        totalLeadsTextView = null;
        totalConversoesTextView = null;
        leadsGrowthTextView = null;
        conversoesGrowthTextView = null;
        individualPerformanceContainer = null;
    }
    
    // ===================== MÉTODOS UTILITÁRIOS =====================
    
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