package inf311.grupo1.projetopratico.services;

import inf311.grupo1.projetopratico.models.UserMetrics;
import inf311.grupo1.projetopratico.models.UserProfile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserProfileService {
    
    private static UserProfileService instance;
    
    private UserProfileService() {}
    
    public static UserProfileService getInstance() {
        if (instance == null) {
            instance = new UserProfileService();
        }
        return instance;
    }
    
    /**
     * Obtém o perfil completo do usuário
     * Futuramente este método fará uma chamada à API
     */
    public UserProfile getUserProfile(String userUid, String userEmail, boolean isAdmin) {
        // Simula dados dinâmicos - futuramente virá da API
        UserMetrics metricas = getUserMetrics(userEmail, isAdmin);
        
        return new UserProfile(
            userUid,
            extractNameFromEmail(userEmail),
            userEmail,
            generatePhoneNumber(userEmail), // Simula telefone baseado no email
            isAdmin ? "Administrador" : "Consultor de Vendas",
            isAdmin,
            null, // URL da foto - pode ser implementado com upload de imagem
            getCurrentDate(),
            metricas
        );
    }
    
    /**
     * Obtém as métricas específicas do usuário
     * Futuramente este método fará uma chamada à API
     */
    public UserMetrics getUserMetrics(String userEmail, boolean isAdmin) {
        // Simula dados dinâmicos baseados no tipo de usuário
        if (isAdmin) {
            return new UserMetrics(
                156, // total leads (soma da equipe)
                44,  // convertidos (soma da equipe)
                28.2, // taxa conversão
                23,  // este mês
                50,  // meta mensal
                22   // dias trabalhados
            );
        } else {
            return new UserMetrics(
                48,  // total leads individuais
                18,  // convertidos individuais
                25.0, // taxa conversão
                8,   // este mês
                15,  // meta mensal individual
                22   // dias trabalhados
            );
        }
    }
    
    /**
     * Atualiza as métricas do usuário
     * Futuramente este método fará uma chamada à API
     */
    public void updateUserMetrics(String userUid, UserMetrics novasMetricas) {
        // TODO: Implementar atualização via API
        // Por enquanto apenas simula o sucesso da operação
    }
    
    /**
     * Atualiza informações do perfil do usuário
     * Futuramente este método fará uma chamada à API
     */
    public void updateUserProfile(String userUid, String nome, String telefone) {
        // TODO: Implementar atualização via API
        // Por enquanto apenas simula o sucesso da operação
    }
    
    /**
     * Obtém estatísticas detalhadas do usuário para análise
     * Futuramente este método fará uma chamada à API
     */
    public UserDetailedStats getUserDetailedStats(String userEmail, boolean isAdmin) {
        if (isAdmin) {
            return new UserDetailedStats(
                156, // leads total
                89,  // leads ativos
                23,  // leads este mês
                44,  // conversões total
                12,  // conversões este mês
                28.2, // taxa conversão
                5.2,  // média leads por dia
                1.8,  // média conversões por dia
                12,   // dias consecutivos com atividade
                "Excelente performance!"
            );
        } else {
            return new UserDetailedStats(
                48,  // leads total
                28,  // leads ativos
                8,   // leads este mês
                18,  // conversões total
                3,   // conversões este mês
                25.0, // taxa conversão
                2.2,  // média leads por dia
                0.5,  // média conversões por dia
                8,    // dias consecutivos com atividade
                "Você está indo muito bem!"
            );
        }
    }
    
    /**
     * Extrai nome a partir do email
     */
    private String extractNameFromEmail(String email) {
        if (email == null) return "Usuário";
        
        String nome = email.split("@")[0];
        nome = nome.replace(".", " ");
        
        String[] partes = nome.split(" ");
        StringBuilder nomeFormatado = new StringBuilder();
        
        for (String parte : partes) {
            if (parte.length() > 0) {
                nomeFormatado.append(parte.substring(0, 1).toUpperCase())
                            .append(parte.substring(1).toLowerCase())
                            .append(" ");
            }
        }
        
        return nomeFormatado.toString().trim();
    }
    
    /**
     * Gera um número de telefone baseado no email (simulação)
     */
    private String generatePhoneNumber(String email) {
        if (email == null) return "(11) 99999-9999";
        
        // Simula geração de telefone baseado no hash do email
        int hash = Math.abs(email.hashCode());
        int ddd = 11 + (hash % 89); // DDDs de 11 a 99
        int numero = 90000000 + (hash % 9999999);
        
        return String.format("(%02d) %d-%04d", ddd, numero / 10000, numero % 10000);
    }
    
    /**
     * Obtém a data atual formatada
     */
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }
    
    /**
     * Classe para estatísticas detalhadas do usuário
     */
    public static class UserDetailedStats {
        private int leadsTotal;
        private int leadsAtivos;
        private int leadsEsteMes;
        private int conversoesTotal;
        private int conversoesEsteMes;
        private double taxaConversao;
        private double mediaLeadsPorDia;
        private double mediaConversoesPorDia;
        private int diasConsecutivosAtivo;
        private String mensagemMotivacional;
        
        public UserDetailedStats(int leadsTotal, int leadsAtivos, int leadsEsteMes,
                               int conversoesTotal, int conversoesEsteMes, double taxaConversao,
                               double mediaLeadsPorDia, double mediaConversoesPorDia,
                               int diasConsecutivosAtivo, String mensagemMotivacional) {
            this.leadsTotal = leadsTotal;
            this.leadsAtivos = leadsAtivos;
            this.leadsEsteMes = leadsEsteMes;
            this.conversoesTotal = conversoesTotal;
            this.conversoesEsteMes = conversoesEsteMes;
            this.taxaConversao = taxaConversao;
            this.mediaLeadsPorDia = mediaLeadsPorDia;
            this.mediaConversoesPorDia = mediaConversoesPorDia;
            this.diasConsecutivosAtivo = diasConsecutivosAtivo;
            this.mensagemMotivacional = mensagemMotivacional;
        }
        
        // Getters
        public int getLeadsTotal() { return leadsTotal; }
        public int getLeadsAtivos() { return leadsAtivos; }
        public int getLeadsEsteMes() { return leadsEsteMes; }
        public int getConversoesTotal() { return conversoesTotal; }
        public int getConversoesEsteMes() { return conversoesEsteMes; }
        public double getTaxaConversao() { return taxaConversao; }
        public double getMediaLeadsPorDia() { return mediaLeadsPorDia; }
        public double getMediaConversoesPorDia() { return mediaConversoesPorDia; }
        public int getDiasConsecutivosAtivo() { return diasConsecutivosAtivo; }
        public String getMensagemMotivacional() { return mensagemMotivacional; }
    }
} 