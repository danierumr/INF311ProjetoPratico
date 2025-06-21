package inf311.grupo1.projetopratico.utils;

public class AppConstants {
    
    // Séries disponíveis para cadastro de leads
    public static final String[] SERIES_DISPONVEIS = {
        "1º Ano", "2º Ano", "3º Ano", "4º Ano", "5º Ano", "6º Ano", 
        "7º Ano", "8º Ano", "9º Ano", "1º EM", "2º EM", "3º EM"
    };
    
    // Tipos de interesse disponíveis
    public static final String[] TIPOS_INTERESSE = {
        "Selecione", "Matrícula imediata", "Conhecer a escola", 
        "Informações", "Valores", "Bolsa de estudos"
    };
    
    // Status possíveis dos leads
    public static final String[] STATUS_LEADS = {
        "Novo", "Contatado", "Interessado", "Agendado", "Visitou", "Matriculado"
    };
    
    // Prioridades dos leads
    public static final String[] PRIORIDADES = {
        "Baixa", "Média", "Alta", "Urgente"
    };
    
    // Estágios do funil de vendas
    public static final String[] ESTAGIOS_FUNIL = {
        "Prospecção", "Qualificação", "Proposta", "Negociação", "Fechamento"
    };
    
    // Cores padrão para gráficos
    public static final String[] CORES_GRAFICOS = {
        "#14b8a6", "#F44336", "#2196F3", "#FF9800", "#E91E63", 
        "#9C27B0", "#673AB7", "#3F51B5", "#009688", "#4CAF50"
    };
    
    // Intervalos de tempo para análises
    public static final String INTERVALO_SEMANAL = "semanal";
    public static final String INTERVALO_MENSAL = "mensal";
    public static final String INTERVALO_TRIMESTRAL = "trimestral";
    
    // Limites de paginação
    public static final int LIMITE_LEADS_DASHBOARD = 5;
    public static final int LIMITE_LEADS_POR_PAGINA = 20;
    
    // Chaves para Bundle/Intent
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_USER_UID = "user_uid";
    public static final String KEY_IS_ADMIN = "is_admin";
    public static final String KEY_CONTATO = "contato";
    
    // Mensagens de erro padrão
    public static final String MSG_ERRO_CARREGAR_DADOS = "Erro ao carregar dados";
    public static final String MSG_ERRO_SALVAR = "Erro ao salvar informações";
    public static final String MSG_ERRO_CONEXAO = "Erro de conexão";
    public static final String MSG_SUCESSO_SALVAR = "Informações salvas com sucesso";
    
    // Configurações de validação
    public static final int MIN_TELEFONE_LENGTH = 10;
    public static final int MAX_TELEFONE_LENGTH = 15;
    public static final int MIN_NOME_LENGTH = 2;
    public static final int MAX_OBSERVACOES_LENGTH = 500;
    
    private AppConstants() {
        // Classe utilitária - não deve ser instanciada
    }
} 