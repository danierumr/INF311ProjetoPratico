package inf311.grupo1.projetopratico.services;

import inf311.grupo1.projetopratico.models.ChartData;

import java.util.ArrayList;
import java.util.List;

public class MetricsDataProvider {
    
    private static MetricsDataProvider instance;
    
    private MetricsDataProvider() {}
    
    public static MetricsDataProvider getInstance() {
        if (instance == null) {
            instance = new MetricsDataProvider();
        }
        return instance;
    }
    
    /**
     * Obtém dados para o gráfico de pizza (distribuição de leads por consultor)
     * Futuramente este método fará uma chamada à API
     */
    public ChartData.PieChartData getPieChartData(String userEmail, boolean isAdmin) {
        List<ChartData.ConsultorData> consultores = new ArrayList<>();
        
        // Simula dados dinâmicos - futuramente virá da API
        if (isAdmin) {
            // Admin vê dados de todos os consultores
            consultores.add(new ChartData.ConsultorData("Ana", 26, 8, "#14b8a6"));
            consultores.add(new ChartData.ConsultorData("Carlos", 19, 5, "#F44336"));
            consultores.add(new ChartData.ConsultorData("Juliana", 22, 7, "#2196F3"));
            consultores.add(new ChartData.ConsultorData("Roberto", 17, 4, "#FF9800"));
            consultores.add(new ChartData.ConsultorData("Mariana", 16, 3, "#E91E63"));
        } else {
            // Consultor vê apenas seus próprios dados
            String nomeConsultor = getNomeFromEmail(userEmail);
            consultores.add(new ChartData.ConsultorData(nomeConsultor, 48, 12, "#14b8a6"));
        }
        
        return new ChartData.PieChartData(consultores);
    }
    
    /**
     * Obtém dados para o gráfico de barras (leads vs conversões)
     * Futuramente este método fará uma chamada à API
     */
    public ChartData.BarChartData getBarChartData(String userEmail, boolean isAdmin) {
        List<ChartData.ConsultorData> consultores = new ArrayList<>();
        
        // Simula dados dinâmicos - futuramente virá da API
        if (isAdmin) {
            // Admin vê dados de todos os consultores
            consultores.add(new ChartData.ConsultorData("Ana", 42, 12, "#14b8a6"));
            consultores.add(new ChartData.ConsultorData("Carlos", 38, 8, "#F44336"));
            consultores.add(new ChartData.ConsultorData("Juliana", 45, 10, "#2196F3"));
            consultores.add(new ChartData.ConsultorData("Roberto", 35, 7, "#FF9800"));
            consultores.add(new ChartData.ConsultorData("Mariana", 40, 5, "#E91E63"));
        } else {
            // Consultor vê apenas seus próprios dados
            String nomeConsultor = getNomeFromEmail(userEmail);
            consultores.add(new ChartData.ConsultorData(nomeConsultor, 48, 12, "#14b8a6"));
        }
        
        return new ChartData.BarChartData(consultores);
    }
    
    /**
     * Obtém dados de desempenho individual dos consultores
     * Futuramente este método fará uma chamada à API
     */
    public List<ChartData.ConsultorData> getIndividualPerformanceData(String userEmail, boolean isAdmin) {
        List<ChartData.ConsultorData> consultores = new ArrayList<>();
        
        // Simula dados dinâmicos - futuramente virá da API
        if (isAdmin) {
            // Admin vê desempenho de todos os consultores
            consultores.add(new ChartData.ConsultorData("Ana Silva", 42, 12, "#14b8a6"));
            consultores.add(new ChartData.ConsultorData("Carlos Santos", 38, 8, "#F44336"));
            consultores.add(new ChartData.ConsultorData("Juliana Costa", 45, 10, "#2196F3"));
            consultores.add(new ChartData.ConsultorData("Roberto Lima", 35, 7, "#FF9800"));
            consultores.add(new ChartData.ConsultorData("Mariana Souza", 40, 5, "#E91E63"));
        } else {
            // Consultor vê apenas seu próprio desempenho
            String nomeConsultor = getNomeCompletoFromEmail(userEmail);
            consultores.add(new ChartData.ConsultorData(nomeConsultor, 48, 12, "#14b8a6"));
        }
        
        return consultores;
    }
    
    /**
     * Obtém métricas consolidadas da equipe
     * Futuramente este método fará uma chamada à API
     */
    public TeamMetrics getTeamMetrics(String userEmail, boolean isAdmin) {
        if (isAdmin) {
            return new TeamMetrics(
                200,  // total leads equipe
                42,   // total conversões
                21.0, // taxa conversão média
                5,    // consultores ativos
                156,  // leads este mês
                12.5  // crescimento percentual
            );
        } else {
            // Consultor vê apenas suas métricas individuais
            return new TeamMetrics(
                48,   // seus leads
                12,   // suas conversões
                25.0, // sua taxa conversão
                1,    // apenas ele
                8,    // leads este mês
                8.3   // seu crescimento
            );
        }
    }
    
    /**
     * Obtém dados históricos para análise de tendências
     * Futuramente este método fará uma chamada à API
     */
    public List<MonthlyData> getMonthlyTrends(String userEmail, boolean isAdmin, int meses) {
        List<MonthlyData> dados = new ArrayList<>();
        
        // Simula dados dos últimos meses
        if (isAdmin) {
            dados.add(new MonthlyData("Jan", 145, 28));
            dados.add(new MonthlyData("Fev", 158, 35));
            dados.add(new MonthlyData("Mar", 142, 31));
            dados.add(new MonthlyData("Abr", 167, 42));
            dados.add(new MonthlyData("Mai", 156, 38));
        } else {
            dados.add(new MonthlyData("Jan", 42, 8));
            dados.add(new MonthlyData("Fev", 48, 12));
            dados.add(new MonthlyData("Mar", 45, 10));
            dados.add(new MonthlyData("Abr", 52, 14));
            dados.add(new MonthlyData("Mai", 48, 12));
        }
        
        return dados.subList(Math.max(0, dados.size() - meses), dados.size());
    }
    
    /**
     * Extrai nome do consultor a partir do email
     */
    private String getNomeFromEmail(String email) {
        if (email == null) return "Consultor";
        
        String nome = email.split("@")[0];
        return nome.substring(0, 1).toUpperCase() + nome.substring(1);
    }
    
    /**
     * Extrai nome completo do consultor a partir do email
     */
    private String getNomeCompletoFromEmail(String email) {
        if (email == null) return "Consultor Atual";
        
        String nome = email.split("@")[0];
        return nome.substring(0, 1).toUpperCase() + nome.substring(1);
    }
    
    /**
     * Classe para métricas da equipe
     */
    public static class TeamMetrics {
        private int totalLeads;
        private int totalConversoes;
        private double taxaConversaoMedia;
        private int consultoresAtivos;
        private int leadsEsteMes;
        private double crescimentoPercentual;
        
        public TeamMetrics(int totalLeads, int totalConversoes, double taxaConversaoMedia,
                          int consultoresAtivos, int leadsEsteMes, double crescimentoPercentual) {
            this.totalLeads = totalLeads;
            this.totalConversoes = totalConversoes;
            this.taxaConversaoMedia = taxaConversaoMedia;
            this.consultoresAtivos = consultoresAtivos;
            this.leadsEsteMes = leadsEsteMes;
            this.crescimentoPercentual = crescimentoPercentual;
        }
        
        // Getters
        public int getTotalLeads() { return totalLeads; }
        public int getTotalConversoes() { return totalConversoes; }
        public double getTaxaConversaoMedia() { return taxaConversaoMedia; }
        public int getConsultoresAtivos() { return consultoresAtivos; }
        public int getLeadsEsteMes() { return leadsEsteMes; }
        public double getCrescimentoPercentual() { return crescimentoPercentual; }
    }
    
    /**
     * Classe para dados mensais
     */
    public static class MonthlyData {
        private String mes;
        private int leads;
        private int conversoes;
        
        public MonthlyData(String mes, int leads, int conversoes) {
            this.mes = mes;
            this.leads = leads;
            this.conversoes = conversoes;
        }
        
        // Getters
        public String getMes() { return mes; }
        public int getLeads() { return leads; }
        public int getConversoes() { return conversoes; }
        public double getTaxaConversao() {
            return leads > 0 ? (double) conversoes / leads * 100 : 0.0;
        }
    }
} 