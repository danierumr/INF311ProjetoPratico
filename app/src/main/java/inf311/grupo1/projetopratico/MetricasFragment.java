package inf311.grupo1.projetopratico;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

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

import java.util.ArrayList;
import java.util.List;

public class MetricasFragment extends Fragment {
    
    private static final String TAG = "MetricasFragment";
    
    // Informações do usuário
    private boolean isAdmin = false;
    private String userEmail;
    private String userUid;
    
    // Charts
    private BarChart barChart;
    private PieChart pieChart;
    
    // Views para desempenho individual
    private LinearLayout individualPerformanceContainer;
    
    // Serviços de dados
    private MetricsDataProvider metricsDataProvider;

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
        
        Log.d(TAG, "MetricasFragment iniciado para usuário: " + userEmail);
        
        return view;
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Inicializar charts e views
        initializeViews(view);
        
        // Carregar dados e configurar charts
        loadMetricsData();
        
        // Carregar desempenho individual
        loadIndividualPerformance();
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
        
        Log.d(TAG, "Dados do usuário - Email: " + userEmail + ", Admin: " + isAdmin);
    }
    
    /**
     * Inicializa os charts e views
     */
    private void initializeViews(View view) {
        barChart = view.findViewById(R.id.barChart);
        pieChart = view.findViewById(R.id.pieChart);
        
        // Encontrar o container onde iremos adicionar os cards de desempenho individual
        // Vamos procurar pelo último LinearLayout que contém os cards estáticos
        findIndividualPerformanceContainer(view);
        
        Log.d(TAG, "Views inicializadas");
    }
    
    /**
     * Encontra o container de desempenho individual para substituir conteúdo estático
     */
    private void findIndividualPerformanceContainer(View rootView) {
        // Buscar diretamente pelo ViewGroup raiz e navegar pela hierarquia
        if (rootView instanceof ViewGroup) {
            ViewGroup rootGroup = (ViewGroup) rootView;
            individualPerformanceContainer = findContainerRecursively(rootGroup);
        }
        
        Log.d(TAG, "Container de desempenho individual " + 
              (individualPerformanceContainer != null ? "encontrado" : "não encontrado"));
    }
    
    /**
     * Busca recursivamente pelo container que contém os cards de desempenho individual
     */
    private LinearLayout findContainerRecursively(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            
            if (child instanceof LinearLayout) {
                LinearLayout linearChild = (LinearLayout) child;
                
                // Verificar se este LinearLayout contém cards de desempenho
                // Procurar por um que tenha um TextView com "Desempenho Individual" e cards
                if (hasIndividualPerformanceContent(linearChild)) {
                    return linearChild;
                }
            }
            
            // Buscar recursivamente nos filhos
            if (child instanceof ViewGroup) {
                LinearLayout found = findContainerRecursively((ViewGroup) child);
                if (found != null) {
                    return found;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Verifica se um LinearLayout contém conteúdo de desempenho individual
     */
    private boolean hasIndividualPerformanceContent(LinearLayout layout) {
        if (layout.getChildCount() < 2) return false;
        
        // Verificar se o primeiro filho é um TextView com "Desempenho Individual"
        View firstChild = layout.getChildAt(0);
        if (firstChild instanceof TextView) {
            TextView textView = (TextView) firstChild;
            String text = textView.getText().toString();
            if (text.contains("Desempenho Individual")) {
                // Verificar se há cards (CardViews) como filhos
                for (int i = 1; i < layout.getChildCount(); i++) {
                    if (layout.getChildAt(i) instanceof CardView) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * Carrega os dados das métricas e configura os gráficos
     */
    private void loadMetricsData() {
        try {
            // Carregar dados para o gráfico de pizza
            loadPieChartData();
            
            // Carregar dados para o gráfico de barras
            loadBarChartData();
            
            Log.d(TAG, "Dados das métricas carregados com sucesso");
        } catch (Exception e) {
            Log.e(TAG, "Erro ao carregar dados das métricas", e);
        }
    }
    
    /**
     * Carrega e configura os dados do gráfico de pizza
     */
    private void loadPieChartData() {
        try {
            ChartData.PieChartData pieData = metricsDataProvider.getPieChartData(userEmail, isAdmin);
            setupPieChart(pieData);
            
            Log.d(TAG, "Dados do gráfico de pizza carregados");
        } catch (Exception e) {
            Log.e(TAG, "Erro ao carregar dados do gráfico de pizza", e);
        }
    }
    
    /**
     * Carrega e configura os dados do gráfico de barras
     */
    private void loadBarChartData() {
        try {
            ChartData.BarChartData barData = metricsDataProvider.getBarChartData(userEmail, isAdmin);
            setupBarChart(barData);
            
            Log.d(TAG, "Dados do gráfico de barras carregados");
        } catch (Exception e) {
            Log.e(TAG, "Erro ao carregar dados do gráfico de barras", e);
        }
    }
    
    /**
     * Configura o gráfico de pizza com dados dinâmicos
     */
    private void setupPieChart(ChartData.PieChartData chartData) {
        if (pieChart == null || chartData == null) return;
        
        List<PieEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        
        // Converter dados do provedor para o formato do gráfico
        for (ChartData.ConsultorData consultor : chartData.getConsultores()) {
            entries.add(new PieEntry(consultor.getLeads(), consultor.getNome()));
            colors.add(Color.parseColor(consultor.getCor()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Distribuição de Leads");
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12f);
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(5f);

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });

        pieChart.setData(pieData);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleRadius(45f);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(50);

        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);

        pieChart.setTouchEnabled(true);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        pieChart.animateY(1000);
        pieChart.invalidate();
        
        Log.d(TAG, "Gráfico de pizza configurado com " + entries.size() + " entradas");
    }

    /**
     * Configura o gráfico de barras com dados dinâmicos
     */
    private void setupBarChart(ChartData.BarChartData chartData) {
        if (barChart == null || chartData == null) return;
        
        List<BarEntry> leadsEntries = new ArrayList<>();
        List<BarEntry> conversionsEntries = new ArrayList<>();
        List<String> consultorNames = new ArrayList<>();

        // Converter dados do provedor para o formato do gráfico
        int index = 0;
        for (ChartData.ConsultorData consultor : chartData.getConsultores()) {
            leadsEntries.add(new BarEntry(index, consultor.getLeads()));
            conversionsEntries.add(new BarEntry(index, consultor.getConversoes()));
            consultorNames.add(consultor.getNome());
            index++;
        }

        BarDataSet leadsDataSet = new BarDataSet(leadsEntries, "Leads");
        leadsDataSet.setColor(Color.parseColor("#14b8a6"));
        leadsDataSet.setValueTextColor(Color.BLACK);
        leadsDataSet.setValueTextSize(10f);

        BarDataSet conversionsDataSet = new BarDataSet(conversionsEntries, "Conversões");
        conversionsDataSet.setColor(Color.parseColor("#F44336"));
        conversionsDataSet.setValueTextColor(Color.BLACK);
        conversionsDataSet.setValueTextSize(10f);

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(leadsDataSet);
        dataSets.add(conversionsDataSet);

        BarData barData = new BarData(dataSets);
        barData.setBarWidth(0.35f);

        barChart.setData(barData);

        // Configurar eixos
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < consultorNames.size()) {
                    return consultorNames.get(index);
                }
                return "";
            }
        });

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);

        Legend legend = barChart.getLegend();
        legend.setEnabled(true);

        // Configurar agrupamento de barras
        float groupSpace = 0.3f;
        float barSpace = 0.05f;
        float barWidth = 0.3f;
        
        barData.setBarWidth(barWidth);
        barChart.groupBars(0, groupSpace, barSpace);

        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.setPinchZoom(false);

        barChart.animateY(1000);
        barChart.invalidate();
        
        Log.d(TAG, "Gráfico de barras configurado com " + leadsEntries.size() + " entradas");
    }
    
    /**
     * Atualiza os dados dos gráficos
     */
    public void refreshChartsData() {
        Log.d(TAG, "Atualizando dados dos gráficos");
        loadMetricsData();
        loadIndividualPerformance();
    }
    
    /**
     * Obtém métricas consolidadas da equipe
     */
    public void loadTeamMetrics() {
        try {
            MetricsDataProvider.TeamMetrics teamMetrics = 
                metricsDataProvider.getTeamMetrics(userEmail, isAdmin);
            
            // TODO: Implementar exibição das métricas da equipe na interface
            // Por exemplo, atualizar TextViews com os valores das métricas
            
            Log.d(TAG, "Métricas da equipe - Total leads: " + teamMetrics.getTotalLeads() +
                      ", Conversões: " + teamMetrics.getTotalConversoes() +
                      ", Taxa média: " + teamMetrics.getTaxaConversaoMedia() + "%");
                      
        } catch (Exception e) {
            Log.e(TAG, "Erro ao carregar métricas da equipe", e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        
        // Limpar referências dos charts
        if (barChart != null) {
            barChart.clear();
        }
        if (pieChart != null) {
            pieChart.clear();
        }
        
        Log.d(TAG, "MetricasFragment destruído");
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

    /**
     * Carrega e exibe o desempenho individual da equipe
     */
    private void loadIndividualPerformance() {
        if (individualPerformanceContainer == null) {
            Log.w(TAG, "Container de desempenho individual não encontrado");
            return;
        }
        
        try {
            // Obter dados de desempenho individual
            List<ChartData.ConsultorData> consultores = 
                metricsDataProvider.getIndividualPerformanceData(userEmail, isAdmin);
            
            // Limpar cards estáticos existentes (manter apenas o título)
            clearStaticPerformanceCards();
            
            // Adicionar cards dinâmicos
            for (ChartData.ConsultorData consultor : consultores) {
                addIndividualPerformanceCard(consultor);
            }
            
            Log.d(TAG, "Desempenho individual carregado para " + consultores.size() + " consultores");
            
        } catch (Exception e) {
            Log.e(TAG, "Erro ao carregar desempenho individual", e);
        }
    }
    
    /**
     * Remove os cards estáticos de desempenho, mantendo apenas o título
     */
    private void clearStaticPerformanceCards() {
        if (individualPerformanceContainer == null) return;
        
        // Manter apenas o primeiro child (título) e remover os cards estáticos
        int childCount = individualPerformanceContainer.getChildCount();
        for (int i = childCount - 1; i > 0; i--) {
            View child = individualPerformanceContainer.getChildAt(i);
            if (child instanceof CardView) {
                individualPerformanceContainer.removeViewAt(i);
            }
        }
        
        Log.d(TAG, "Cards estáticos de desempenho removidos");
    }
    
    /**
     * Adiciona um card de desempenho individual para um consultor
     */
    private void addIndividualPerformanceCard(ChartData.ConsultorData consultor) {
        if (individualPerformanceContainer == null || getContext() == null) return;
        
        // Criar o CardView
        CardView cardView = new CardView(getContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, dpToPx(8));
        cardView.setLayoutParams(cardParams);
        cardView.setRadius(dpToPx(12));
        cardView.setCardElevation(dpToPx(4));
        
        // Criar o LinearLayout principal (horizontal)
        LinearLayout mainLayout = new LinearLayout(getContext());
        mainLayout.setOrientation(LinearLayout.HORIZONTAL);
        mainLayout.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));
        
        // Criar o avatar (View circular)
        View avatar = new View(getContext());
        LinearLayout.LayoutParams avatarParams = new LinearLayout.LayoutParams(
            dpToPx(48), dpToPx(48)
        );
        avatarParams.setMarginEnd(dpToPx(16));
        avatar.setLayoutParams(avatarParams);
        avatar.setBackgroundResource(R.drawable.circle_avatar);
        avatar.getBackground().setTint(Color.parseColor(consultor.getCor()));
        
        // Criar o LinearLayout para nome e métricas (vertical)
        LinearLayout infoLayout = new LinearLayout(getContext());
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(
            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
        );
        infoLayout.setLayoutParams(infoParams);
        
        // TextView para o nome
        TextView nameText = new TextView(getContext());
        nameText.setText(consultor.getNome());
        nameText.setTextColor(getResources().getColor(R.color.text_primary, null));
        nameText.setTextSize(16);
        nameText.setTypeface(null, android.graphics.Typeface.BOLD);
        
        // TextView para as métricas
        TextView metricsText = new TextView(getContext());
        String metricsString = consultor.getLeads() + " leads • " + 
                              consultor.getConversoes() + " conversões";
        metricsText.setText(metricsString);
        metricsText.setTextColor(getResources().getColor(R.color.text_secondary, null));
        metricsText.setTextSize(14);
        
        // Adicionar nome e métricas ao layout de informações
        infoLayout.addView(nameText);
        infoLayout.addView(metricsText);
        
        // TextView para a taxa de conversão
        TextView rateText = new TextView(getContext());
        float taxaConversao = consultor.getConversoes() > 0 && consultor.getLeads() > 0 ?
            (float) consultor.getConversoes() / consultor.getLeads() * 100 : 0;
        rateText.setText(String.format("%.1f%%", taxaConversao));
        rateText.setTextColor(getResources().getColor(R.color.primary_green, null));
        rateText.setTextSize(16);
        rateText.setTypeface(null, android.graphics.Typeface.BOLD);
        
        // Montar o layout principal
        mainLayout.addView(avatar);
        mainLayout.addView(infoLayout);
        mainLayout.addView(rateText);
        
        // Adicionar o layout ao CardView
        cardView.addView(mainLayout);
        
        // Adicionar o CardView ao container
        individualPerformanceContainer.addView(cardView);
        
        Log.d(TAG, "Card adicionado para " + consultor.getNome() + 
              " - " + consultor.getLeads() + " leads, " + consultor.getConversoes() + " conversões");
    }
    
    /**
     * Converte dp para pixels
     */
    private int dpToPx(int dp) {
        if (getContext() == null) return dp;
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
} 