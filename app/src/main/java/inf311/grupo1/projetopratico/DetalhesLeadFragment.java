package inf311.grupo1.projetopratico;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Date;

import inf311.grupo1.projetopratico.utils.App_fragment;

public class DetalhesLeadFragment extends App_fragment {
    
    private static final String TAG = "DetalhesLeadFragment";
    
    private boolean isAdmin = false;
    private String userEmail;
    private String userUid;
    
    private Contato contato;
    
    private TextView nomeTextView, escolaTextView, estagioTextView, prioridadeTextView;
    private TextView responsavelTextView, emailTextView, telefoneTextView;
    private TextView interesseTextView, ultimoContatoTextView, observacoesTextView;
    private Button registrarAtividadeBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "DetalhesLeadFragment onCreateView");
        
        View view = inflater.inflate(R.layout.fragment_detalhes_lead, container, false);
        
        getUserDataFromArguments();
        
        Log.d(TAG, "DetalhesLeadFragment iniciado para usuário: " + userEmail);
        
        return view;
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        obterDadosArguments();
        initializeUI(view);
        preencherDados();
        setupListeners();
    }
    
    /**
     * Obtém dados do usuário dos argumentos do fragment
     */
    private void getUserDataFromArguments() {
        Bundle args = getArguments();
        if (args != null) {
            isAdmin = args.getBoolean("is_admin", false);
            userEmail = args.getString("user_email");
            userUid = args.getString("user_uid");
        }
        
        Log.d(TAG, "Dados do usuário - Email: " + userEmail + ", Admin: " + isAdmin);
    }
    
    /**
     * Obtém os dados do contato dos argumentos
     */
    private void obterDadosArguments() {
        Bundle args = getArguments();
        if (args != null) {
            contato = args.getParcelable("contato");
        }
        
        if (contato == null) {
            Log.e(TAG, "Contato não encontrado nos argumentos");
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "Erro ao carregar dados do lead", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
            return;
        }
        
        Log.d(TAG, "Carregando detalhes do lead: " + contato.nome);
    }
    
    /**
     * Inicializa os elementos da UI
     */
    private void initializeUI(View view) {
        nomeTextView = view.findViewById(R.id.detalhes_lead_name);
        escolaTextView = view.findViewById(R.id.detalhes_lead_escola);
        estagioTextView = view.findViewById(R.id.detalhes_lead_estagio_edit);
        prioridadeTextView = view.findViewById(R.id.detalhes_lead_prioridade_edit);
        
        responsavelTextView = view.findViewById(R.id.detalhes_lead_resp_edit);
        emailTextView = view.findViewById(R.id.detalhes_lead_email_edit);
        telefoneTextView = view.findViewById(R.id.detalhes_lead_tel_edit);
        interesseTextView = view.findViewById(R.id.detalhes_lead_interesse_edit);
        ultimoContatoTextView = view.findViewById(R.id.detalhes_lead_ult_cont_edit);
        
        observacoesTextView = view.findViewById(R.id.detalhes_lead_obser_body);
        
        registrarAtividadeBtn = view.findViewById(R.id.detalhes_lead_btn);
        
        Log.d(TAG, "UI inicializada");
    }
    
    /**
     * Preenche os dados do contato na interface
     */
    private void preencherDados() {
        if (contato == null) return;
        
        try {
            nomeTextView.setText(contato.nome);
            escolaTextView.setText(contato.escola + " • " + contato.serie);
            
            estagioTextView.setText(getEstagioLead());
            prioridadeTextView.setText(getPrioridadeLead());
            
            responsavelTextView.setText(contato.responsavel);
            emailTextView.setText(contato.email);
            telefoneTextView.setText(contato.telefone);
            interesseTextView.setText(contato.interesse);
            
            Date now = new Date();
            CharSequence tempoRelativo = DateUtils.getRelativeTimeSpanString(
                    contato.ultimo_contato.getTime(), 
                    now.getTime(), 
                    DateUtils.MINUTE_IN_MILLIS);
            ultimoContatoTextView.setText(tempoRelativo);
            
            String observacoes = getObservacoesLead();
            observacoesTextView.setText(observacoes);
            
            Log.d(TAG, "Dados preenchidos para lead: " + contato.nome);
            
        } catch (Exception e) {
            Log.e(TAG, "Erro ao preencher dados do lead", e);
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "Erro ao carregar informações do lead", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    /**
     * Configura os listeners dos elementos
     */
    private void setupListeners() {
        if (registrarAtividadeBtn != null) {
            registrarAtividadeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registrarAtividade();
                }
            });
        }
        
        if (emailTextView != null) {
            emailTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    abrirEmail(contato.email);
                }
            });
        }
        
        if (telefoneTextView != null) {
            telefoneTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ligarTelefone(contato.telefone);
                }
            });
        }
        
        Log.d(TAG, "Listeners configurados");
    }
    
    /**
     * Registra uma nova atividade para o lead
     */
    private void registrarAtividade() {
        Log.d(TAG, "Registrando atividade para lead: " + contato.nome);
        
        // TODO: Implementar tela de registro de atividade
        // Por enquanto, apenas mostra uma mensagem
        if (getActivity() != null) {
            Toast.makeText(getActivity(), "Funcionalidade de registro de atividade em desenvolvimento", 
                          Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * Abre o app de email para enviar email ao lead
     */
    private void abrirEmail(String email) {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contato - " + contato.nome);
            
            startActivity(Intent.createChooser(emailIntent, "Enviar email"));
            Log.d(TAG, "Abrindo app de email para: " + email);
            
        } catch (Exception e) {
            Log.e(TAG, "Erro ao abrir app de email", e);
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "Erro ao abrir app de email", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    /**
     * Abre o app de telefone para ligar para o lead
     */
    private void ligarTelefone(String telefone) {
        try {
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
            phoneIntent.setData(Uri.parse("tel:" + telefone));
            
            startActivity(phoneIntent);
            Log.d(TAG, "Abrindo discador para: " + telefone);
            
        } catch (Exception e) {
            Log.e(TAG, "Erro ao abrir discador", e);
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "Erro ao abrir discador", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    /**
     * Retorna o estágio do lead (simulado)
     * Em produção, isso viria da API
     */
    private String getEstagioLead() {
        // Lógica simulada baseada no interesse
        if (contato.interesse.toLowerCase().contains("imediata")) {
            return "Quente";
        } else if (contato.interesse.toLowerCase().contains("conhecer")) {
            return "Morno";
        } else {
            return "Novo";
        }
    }
    
    /**
     * Retorna a prioridade do lead (simulado)
     * Em produção, isso viria da API
     */
    private String getPrioridadeLead() {
        // Lógica simulada baseada no interesse
        if (contato.interesse.toLowerCase().contains("imediata")) {
            return "Alta";
        } else if (contato.interesse.toLowerCase().contains("valores") || 
                   contato.interesse.toLowerCase().contains("bolsa")) {
            return "Média";
        } else {
            return "Normal";
        }
    }
    
    /**
     * Retorna as observações do lead (simulado)
     * Em produção, isso viria da API
     */
    private String getObservacoesLead() {
        // Placeholder - em produção viria da API
        return "Lead interessado em " + contato.interesse.toLowerCase() + 
               ". Aluno da " + contato.escola + " cursando " + contato.serie + 
               ". Responsável: " + contato.responsavel + ".";
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "DetalhesLeadFragment destruído");
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