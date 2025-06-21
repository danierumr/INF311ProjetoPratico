package inf311.grupo1.projetopratico.services;

import inf311.grupo1.projetopratico.Contato;
import inf311.grupo1.projetopratico.models.DashboardMetrics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DashboardDataProvider {
    
    private static DashboardDataProvider instance;
    
    private DashboardDataProvider() {}
    
    public static DashboardDataProvider getInstance() {
        if (instance == null) {
            instance = new DashboardDataProvider();
        }
        return instance;
    }
    
    /**
     * Obtém as métricas do dashboard para um usuário específico
     * Futuramente este método fará uma chamada à API
     */
    public DashboardMetrics getDashboardMetrics(String userEmail, boolean isAdmin) {
        // Simula dados dinâmicos - futuramente virá da API
        if (isAdmin) {
            return new DashboardMetrics(
                156,  // total leads
                23,   // leads novos
                89,   // leads contatados  
                44,   // leads convertidos
                28.2, // taxa conversão
                12,   // atividades hoje
                8     // visitas agendadas
            );
        } else {
            return new DashboardMetrics(
                48,   // total leads
                8,    // leads novos
                28,   // leads contatados
                12,   // leads convertidos
                25.0, // taxa conversão
                5,    // atividades hoje
                3     // visitas agendadas
            );
        }
    }
    
    /**
     * Obtém os leads recentes para exibir no dashboard
     * Futuramente este método fará uma chamada à API
     */
    public List<Contato> getLeadsRecentes(String userEmail, boolean isAdmin, int limite) {
        List<Contato> contatos = new ArrayList<>();
        
        // Simula dados dinâmicos - futuramente virá da API
        if (isAdmin) {
            // Admin vê leads de todos os consultores
            contatos.add(new Contato("Maria Silva", "maria.silva@gmail.com",
                    "31987654321", "José Silva", "Matrícula imediata", "3º EM", 
                    "Colégio Exemplo", new Date()));
            
            contatos.add(new Contato("Pedro Santos", "pedro.santos@gmail.com",
                    "31987654322", "Ana Santos", "Conhecer a escola", "1º EM", 
                    "Escola ABC", new Date()));
            
            contatos.add(new Contato("Carla Oliveira", "carla.oliveira@gmail.com",
                    "31987654323", "Roberto Oliveira", "Informações", "2º EM", 
                    "Instituto XYZ", new Date()));
        } else {
            // Consultor vê apenas seus leads
            contatos.add(new Contato("João Silva", "joaosilva@gmail.com",
                    "38922285", "João Silva pai", "Matrícula imediata", "5º ano", 
                    "Escola 1", new Date()));

            contatos.add(new Contato("Ana Silva", "anasilva@gmail.com",
                    "38913223", "João Silva pai", "Matrícula imediata", "8º ano", 
                    "Escola 1", new Date()));

            contatos.add(new Contato("João Android II", "joaoandroid2@gmail.com",
                    "38923237", "João Android", "Matrícula imediata", "2º EM", 
                    "Escola 2", new Date()));
        }
        
        // Limitar o número de resultados
        if (contatos.size() > limite) {
            return contatos.subList(0, limite);
        }
        
        return contatos;
    }
    
    /**
     * Obtém estatísticas rápidas para cards do dashboard
     * Futuramente este método fará uma chamada à API
     */
    public DashboardQuickStats getQuickStats(String userEmail, boolean isAdmin) {
        if (isAdmin) {
            return new DashboardQuickStats(
                156, // total leads
                44,  // conversões
                12,  // atividades hoje
                "↑ 12%", // crescimento mensal
                "Excelente performance da equipe!"
            );
        } else {
            return new DashboardQuickStats(
                48,  // total leads
                12,  // conversões
                5,   // atividades hoje
                "↑ 8%", // crescimento mensal
                "Você está 20% acima da meta!"
            );
        }
    }
    
    /**
     * Classe auxiliar para estatísticas rápidas
     */
    public static class DashboardQuickStats {
        private int totalLeads;
        private int conversoes;
        private int atividadesHoje;
        private String crescimentoMensal;
        private String mensagemMotivacional;
        
        public DashboardQuickStats(int totalLeads, int conversoes, int atividadesHoje, 
                                 String crescimentoMensal, String mensagemMotivacional) {
            this.totalLeads = totalLeads;
            this.conversoes = conversoes;
            this.atividadesHoje = atividadesHoje;
            this.crescimentoMensal = crescimentoMensal;
            this.mensagemMotivacional = mensagemMotivacional;
        }
        
        // Getters
        public int getTotalLeads() { return totalLeads; }
        public int getConversoes() { return conversoes; }
        public int getAtividadesHoje() { return atividadesHoje; }
        public String getCrescimentoMensal() { return crescimentoMensal; }
        public String getMensagemMotivacional() { return mensagemMotivacional; }
    }
} 