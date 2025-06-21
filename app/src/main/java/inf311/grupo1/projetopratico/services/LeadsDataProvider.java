package inf311.grupo1.projetopratico.services;

import inf311.grupo1.projetopratico.Contato;
import inf311.grupo1.projetopratico.utils.AppConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LeadsDataProvider {
    
    private static LeadsDataProvider instance;
    
    private LeadsDataProvider() {}
    
    public static LeadsDataProvider getInstance() {
        if (instance == null) {
            instance = new LeadsDataProvider();
        }
        return instance;
    }
    
    /**
     * Obtém todos os leads do usuário
     * Futuramente este método fará uma chamada à API
     */
    public List<Contato> getAllLeads(String userEmail, boolean isAdmin) {
        List<Contato> leads = new ArrayList<>();
        
        // Simula dados dinâmicos - futuramente virá da API
        if (isAdmin) {
            // Admin vê leads de todos os consultores
            leads.addAll(getAdminLeads());
        } else {
            // Consultor vê apenas seus leads
            leads.addAll(getConsultorLeads(userEmail));
        }
        
        return leads;
    }
    
    /**
     * Obtém leads filtrados por status
     * Futuramente este método fará uma chamada à API
     */
    public List<Contato> getLeadsByStatus(String userEmail, boolean isAdmin, String status) {
        List<Contato> todosLeads = getAllLeads(userEmail, isAdmin);
        List<Contato> leadsFiltrados = new ArrayList<>();
        
        for (Contato lead : todosLeads) {
            if (matchesStatus(lead, status)) {
                leadsFiltrados.add(lead);
            }
        }
        
        return leadsFiltrados;
    }
    
    /**
     * Busca leads por nome ou email
     * Futuramente este método fará uma chamada à API
     */
    public List<Contato> searchLeads(String userEmail, boolean isAdmin, String query) {
        List<Contato> todosLeads = getAllLeads(userEmail, isAdmin);
        List<Contato> resultados = new ArrayList<>();
        
        String queryLower = query.toLowerCase();
        
        for (Contato lead : todosLeads) {
            if (lead.nome.toLowerCase().contains(queryLower) ||
                lead.email.toLowerCase().contains(queryLower) ||
                lead.responsavel.toLowerCase().contains(queryLower)) {
                resultados.add(lead);
            }
        }
        
        return resultados;
    }
    
    /**
     * Obtém estatísticas dos leads
     * Futuramente este método fará uma chamada à API
     */
    public LeadsStats getLeadsStats(String userEmail, boolean isAdmin) {
        List<Contato> leads = getAllLeads(userEmail, isAdmin);
        
        int novos = 0, contatados = 0, interessados = 0, agendados = 0, 
            visitaram = 0, matriculados = 0;
        
        for (Contato lead : leads) {
            switch (getLeadStatus(lead)) {
                case "novo": novos++; break;
                case "contatado": contatados++; break;
                case "interessado": interessados++; break;
                case "agendado": agendados++; break;
                case "visitou": visitaram++; break;
                case "matriculado": matriculados++; break;
            }
        }
        
        return new LeadsStats(leads.size(), novos, contatados, interessados, 
                             agendados, visitaram, matriculados);
    }
    
    /**
     * Dados de leads para admin (todos os consultores)
     */
    private List<Contato> getAdminLeads() {
        List<Contato> leads = new ArrayList<>();
        
        // Leads do Consultor Ana
        leads.add(new Contato("Maria Silva", "maria.silva@gmail.com",
                "31987654321", "José Silva", "Matrícula imediata", "3º EM", 
                "Colégio Exemplo", new Date()));
        
        leads.add(new Contato("Pedro Santos", "pedro.santos@gmail.com",  
                "31987654322", "Ana Santos", "Conhecer a escola", "1º EM",
                "Escola ABC", new Date()));
        
        // Leads do Consultor Carlos
        leads.add(new Contato("Carla Oliveira", "carla.oliveira@gmail.com",
                "31987654323", "Roberto Oliveira", "Informações", "2º EM",
                "Instituto XYZ", new Date()));
        
        leads.add(new Contato("Rafael Costa", "rafael.costa@gmail.com",
                "31987654324", "Lucia Costa", "Valores", "9º ano",
                "Escola Central", new Date()));
        
        // Leads da Consultora Juliana  
        leads.add(new Contato("Fernanda Lima", "fernanda.lima@gmail.com",
                "31987654325", "Carlos Lima", "Bolsa de estudos", "1º EM",
                "Colégio Futuro", new Date()));
        
        leads.add(new Contato("Gabriel Rocha", "gabriel.rocha@gmail.com",
                "31987654326", "Maria Rocha", "Matrícula imediata", "2º EM",
                "Instituto Saber", new Date()));
        
        return leads;
    }
    
    /**
     * Dados de leads para consultor específico
     */
    private List<Contato> getConsultorLeads(String userEmail) {
        List<Contato> leads = new ArrayList<>();
        
        // Simula leads específicos do consultor baseado no email
        leads.add(new Contato("João Silva", "joaosilva@gmail.com",
                "38922285", "João Silva pai", "Matrícula imediata", "5º ano", 
                "Escola 1", new Date()));

        leads.add(new Contato("Ana Silva", "anasilva@gmail.com",
                "38913223", "João Silva pai", "Matrícula imediata", "8º ano", 
                "Escola 1", new Date()));

        leads.add(new Contato("João Android II", "joaoandroid2@gmail.com",
                "38923237", "João Android", "Matrícula imediata", "2º EM", 
                "Escola 2", new Date()));
        
        // Adicionar mais leads simulados
        leads.add(new Contato("Beatriz Santos", "beatriz.santos@gmail.com",
                "31998877665", "Ricardo Santos", "Conhecer a escola", "6º ano",
                "Colégio Novo Horizonte", new Date()));
        
        leads.add(new Contato("Marcos Oliveira", "marcos.oliveira@gmail.com",
                "31998877666", "Sandra Oliveira", "Informações", "7º ano",
                "Escola Progresso", new Date()));
        
        return leads;
    }
    
    /**
     * Verifica se um lead corresponde ao status especificado
     */
    private boolean matchesStatus(Contato lead, String status) {
        String leadStatus = getLeadStatus(lead);
        
        switch (status.toLowerCase()) {
            case "todos": return true;
            case "novos": return leadStatus.equals("novo");
            case "contatados": return leadStatus.equals("contatado");
            case "interessados": return leadStatus.equals("interessado");
            case "agendados": return leadStatus.equals("agendado");
            case "visitaram": return leadStatus.equals("visitou");
            case "matriculados": return leadStatus.equals("matriculado");
            default: return false;
        }
    }
    
    /**
     * Determina o status do lead baseado nos dados
     */
    private String getLeadStatus(Contato lead) {
        // Simula lógica de determinação de status
        // Futuramente este dado virá diretamente da API
        
        if (lead.interesse.contains("Matrícula imediata")) {
            return "interessado";
        } else if (lead.interesse.contains("Conhecer")) {
            return "novo";
        } else if (lead.interesse.contains("Informações")) {
            return "contatado";
        } else if (lead.interesse.contains("Valores")) {
            return "agendado";
        } else if (lead.interesse.contains("Bolsa")) {
            return "visitou";
        } else {
            return "novo";
        }
    }
    
    /**
     * Classe para estatísticas dos leads
     */
    public static class LeadsStats {
        private int total;
        private int novos;
        private int contatados;
        private int interessados;
        private int agendados;
        private int visitaram;
        private int matriculados;
        
        public LeadsStats(int total, int novos, int contatados, int interessados,
                         int agendados, int visitaram, int matriculados) {
            this.total = total;
            this.novos = novos;
            this.contatados = contatados;
            this.interessados = interessados;
            this.agendados = agendados;
            this.visitaram = visitaram;
            this.matriculados = matriculados;
        }
        
        // Getters
        public int getTotal() { return total; }
        public int getNovos() { return novos; }
        public int getContatados() { return contatados; }
        public int getInteressados() { return interessados; }
        public int getAgendados() { return agendados; }
        public int getVisitaram() { return visitaram; }
        public int getMatriculados() { return matriculados; }
    }
    
    /**
     * Obtém as opções de séries disponíveis
     */
    public String[] getSeries() {
        return AppConstants.SERIES_DISPONVEIS;
    }
    
    /**
     * Obtém os tipos de interesse disponíveis
     */
    public String[] getInterests() {
        return AppConstants.TIPOS_INTERESSE;
    }
    
    /**
     * Adiciona um novo lead
     * Futuramente este método fará uma chamada à API
     */
    public boolean adicionarLead(Contato novoLead, String userEmail, boolean isAdmin) {
        try {
            // TODO: Implementar chamada à API para salvar o lead
            // Por enquanto, simula o salvamento
            
            // Validar dados do lead
            if (novoLead == null || novoLead.nome == null || novoLead.nome.trim().isEmpty()) {
                return false;
            }
            
            // Simular salvamento bem-sucedido
            // Em produção, aqui seria feita a chamada à API
            return true;
            
        } catch (Exception e) {
            // Log do erro e retorno false em caso de falha
            return false;
        }
    }
} 