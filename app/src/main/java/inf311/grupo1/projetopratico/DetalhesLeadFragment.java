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

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.util.Date;

import inf311.grupo1.projetopratico.utils.App_fragment;

public class DetalhesLeadFragment extends App_fragment {
    
    private static final String TAG = "DetalhesLeadFragment";
    
    private boolean isAdmin = false;
    private String userEmail;
    private String userUid;
    
    private Contato contato;
    
    // UI Elements - Header
    private CardView btnQuickCall, btnQuickEmail;
    
    // UI Elements - Profile Card
    private TextView nomeTextView, escolaTextView, estagioTextView, prioridadeTextView;
    private TextView ultimoContatoTextView;
    private CardView statusChip;
    
    // UI Elements - Contact Card
    private TextView responsavelTextView, emailTextView, telefoneTextView, interesseTextView;
    private CardView btnEmailAction, btnPhoneAction;
    
    // UI Elements - Notes and Actions
    private TextView observacoesTextView;
    //private MaterialButton registrarAtividadeBtn;

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
        updateContactButtonsState();
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
        // Header buttons
        btnQuickCall = view.findViewById(R.id.btn_quick_call);
        btnQuickEmail = view.findViewById(R.id.btn_quick_email);
        
        // Profile card elements
        nomeTextView = view.findViewById(R.id.detalhes_lead_name);
        escolaTextView = view.findViewById(R.id.detalhes_lead_escola);
        estagioTextView = view.findViewById(R.id.detalhes_lead_estagio_edit);
        prioridadeTextView = view.findViewById(R.id.detalhes_lead_prioridade_edit);
        ultimoContatoTextView = view.findViewById(R.id.detalhes_lead_ult_cont_edit);
        statusChip = view.findViewById(R.id.status_chip);
        
        // Contact card elements
        responsavelTextView = view.findViewById(R.id.detalhes_lead_resp_edit);
        emailTextView = view.findViewById(R.id.detalhes_lead_email_edit);
        telefoneTextView = view.findViewById(R.id.detalhes_lead_tel_edit);
        interesseTextView = view.findViewById(R.id.detalhes_lead_interesse_edit);
        btnEmailAction = view.findViewById(R.id.btn_email_action);
        btnPhoneAction = view.findViewById(R.id.btn_phone_action);
        
        // Notes and actions
        observacoesTextView = view.findViewById(R.id.detalhes_lead_obser_body);
        //registrarAtividadeBtn = view.findViewById(R.id.detalhes_lead_btn);
        
        Log.d(TAG, "UI inicializada");
    }
    
    /**
     * Preenche os dados do contato na interface
     */
    private void preencherDados() {
        if (contato == null) return;
        
        try {
            // Profile information
            nomeTextView.setText(contato.nome != null ? contato.nome : "Nome não informado");
            
            String schoolInfo = "";
            if (contato.escola != null && !contato.escola.isEmpty()) {
                schoolInfo = contato.escola;
                if (contato.serie != null && !contato.serie.isEmpty()) {
                    schoolInfo += " • " + contato.serie;
                }
            } else {
                schoolInfo = "Escola não informada";
            }
            escolaTextView.setText(schoolInfo);
            
            // Status information
            String estagio = getEstagioLead();
            estagioTextView.setText(estagio);
            updateStatusChip(estagio);
            
            prioridadeTextView.setText(getPrioridadeLead());
            
            // Last contact
            if (contato.ultimo_contato != null) {
                CharSequence tempoRelativo = DateUtils.getRelativeTimeSpanString(
                        contato.ultimo_contato.getTime(), 
                        System.currentTimeMillis(), 
                        DateUtils.MINUTE_IN_MILLIS);
                ultimoContatoTextView.setText(tempoRelativo);
            } else {
                ultimoContatoTextView.setText("Sem contato registrado");
            }
            
            // Contact information
            responsavelTextView.setText(contato.responsavel != null ? contato.responsavel : "Não informado");
            emailTextView.setText(contato.email != null ? contato.email : "Email não informado");
            telefoneTextView.setText(contato.telefone != null ? contato.telefone : "Telefone não informado");
            interesseTextView.setText(contato.interesse != null ? contato.interesse : "Interesse não informado");
            
            // Notes
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
     * Atualiza a cor do chip de status baseado no estágio
     */
    private void updateStatusChip(String estagio) {
        if (statusChip == null || getContext() == null) return;
        
        int backgroundColor;
        if (estagio.contains("Novo")) {
            backgroundColor = ContextCompat.getColor(getContext(), R.color.text_secondary);
        } else if (estagio.contains("Potencial")) {
            backgroundColor = ContextCompat.getColor(getContext(), R.color.azul_barra);
        } else if (estagio.contains("Interessado")) {
            backgroundColor = ContextCompat.getColor(getContext(), R.color.lilas_barra);
        } else if (estagio.contains("Inscrito")) {
            backgroundColor = ContextCompat.getColor(getContext(), R.color.roxo_barra);
        } else if (estagio.contains("Confirmado")) {
            backgroundColor = ContextCompat.getColor(getContext(), R.color.magenta_barra);
        } else if (estagio.contains("Convocado")) {
            backgroundColor = ContextCompat.getColor(getContext(), R.color.warning_orange);
        } else if (estagio.contains("Matriculado")) {
            backgroundColor = ContextCompat.getColor(getContext(), R.color.success_green);
        } else {
            backgroundColor = ContextCompat.getColor(getContext(), R.color.primary_green);
        }
        
        statusChip.setCardBackgroundColor(backgroundColor);
    }
    
    /**
     * Atualiza o estado dos botões de contato baseado na disponibilidade dos dados
     */
    private void updateContactButtonsState() {
        if (contato == null) return;
        
        boolean hasValidEmail = isEmailValido(contato.email);
        boolean hasValidPhone = isTelefoneValido(contato.telefone);
        
        updateButtonState(btnQuickCall, hasValidPhone);
        updateButtonState(btnQuickEmail, hasValidEmail);
        
        updateButtonState(btnPhoneAction, hasValidPhone);
        updateButtonState(btnEmailAction, hasValidEmail);
        
        Log.d(TAG, "Estado dos botões atualizado - Email válido: " + hasValidEmail + ", Telefone válido: " + hasValidPhone);
    }
    
    /**
     * Atualiza o estado visual de um botão baseado na disponibilidade
     */
    private void updateButtonState(CardView button, boolean isEnabled) {
        if (button == null) return;
        
        button.setAlpha(isEnabled ? 1.0f : 0.5f);
        button.setClickable(true); // Manter clicável para mostrar feedback
    }
    
    /**
     * Configura os listeners dos elementos
     */
    private void setupListeners() {
        // Quick action buttons
        if (btnQuickCall != null) {
            btnQuickCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handlePhoneAction();
                }
            });
        }
        
        if (btnQuickEmail != null) {
            btnQuickEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleEmailAction();
                }
            });
        }
        
        // Contact card action buttons
        if (btnPhoneAction != null) {
            btnPhoneAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handlePhoneAction();
                }
            });
        }
        
        if (btnEmailAction != null) {
            btnEmailAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleEmailAction();
                }
            });
        }
        
        // Contact text fields (backward compatibility)
        if (emailTextView != null) {
            emailTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleEmailAction();
                }
            });
        }
        
        if (telefoneTextView != null) {
            telefoneTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handlePhoneAction();
                }
            });
        }
        
//        if (registrarAtividadeBtn != null) {
//            registrarAtividadeBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    registrarAtividade();
//                }
//            });
//        }
        
        Log.d(TAG, "Listeners configurados");
    }
    
    /**
     * Manipula ação de telefone
     */
    private void handlePhoneAction() {
        if (contato == null) return;
        
        if (isTelefoneValido(contato.telefone)) {
            ligarTelefone(contato.telefone);
        } else {
            Toast.makeText(getContext(), "Telefone não informado para " + contato.nome, Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Manipula ação de email
     */
    private void handleEmailAction() {
        if (contato == null) return;
        
        if (isEmailValido(contato.email)) {
            abrirEmail(contato.email);
        } else {
            Toast.makeText(getContext(), "Email não informado para " + contato.nome, Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Registra uma nova atividade para o lead
     */
//    private void registrarAtividade() {
//        Log.d(TAG, "Registrando atividade para lead: " + contato.nome);
//        if (contato != null && getActivity() instanceof MainActivityNova){
//            MainActivityNova mainActivity = (MainActivityNova) getActivity();
//            mainActivity.navigateToAcompanhamento(contato);
//            Log.d(TAG, "Navegando para acompanhamento do lead: " + contato.nome);
//        }
//    }
    
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
     * Verifica se o telefone é válido
     */
    private boolean isTelefoneValido(String telefone) {
        return telefone != null && 
               !telefone.trim().isEmpty() && 
               !telefone.equalsIgnoreCase("null") &&
               !telefone.equalsIgnoreCase("undefined") &&
               !telefone.equals("0") &&
               !telefone.trim().equals("-") &&
               !telefone.contains("não informado");
    }
    
    /**
     * Verifica se o email é válido
     */
    private boolean isEmailValido(String email) {
        return email != null && 
               !email.trim().isEmpty() && 
               !email.equalsIgnoreCase("null") &&
               !email.equalsIgnoreCase("undefined") &&
               !email.contains("não informado") &&
               email.contains("@");
    }
    
    /**
     * Retorna o estágio do lead baseado no interesse
     */
    private String getEstagioLead() {
        if (contato.interesse == null) return "🆕 Novo";
        
        String interesse = contato.interesse.toLowerCase();
        if (interesse.contains("potencial")) {
            return "🌱 Potencial";
        } else if (interesse.contains("interessado")) {
            return "✨ Interessado";
        } else if (interesse.contains("inscrito")) {
            return "📝 Inscrito";
        } else if (interesse.contains("confirmado")) {
            return "✅ Confirmado";
        } else if (interesse.contains("matriculado")) {
            return "🎯 Matriculado";
        } else if (interesse.contains("convocado")) {
            return "📢 Convocado";
        } else {
            return "🆕 Novo";
        }
    }
    
    /**
     * Retorna a prioridade do lead baseado no interesse e último contato
     */
    private String getPrioridadeLead() {
        if (contato.interesse == null) return "⚪ Normal";
        
        String interesse = contato.interesse.toLowerCase();
        
        if (contato.ultimo_contato != null) {
            long daysSinceContact = (System.currentTimeMillis() - contato.ultimo_contato.getTime()) / (1000 * 60 * 60 * 24);
            if (daysSinceContact <= 1) {
                return "🔴 Urgente"; // Contato muito recente
            }
        }
        
        // Verificar prioridade por interesse
        if (interesse.contains("potencial") || interesse.contains("inscrito")) {
            return "🔴 Urgente";
        } else if (interesse.contains("interessado") || interesse.contains("convocado")) {
            return "🟠 Alta";
        } else if (interesse.contains("matriculado")) {
            return "🟡 Média";
        } else {
            return "⚪ Normal";
        }
    }
   
    private String getObservacoesLead() {
        StringBuilder observacoes = new StringBuilder();
        
        observacoes.append("📊 Status atual: ").append(contato.interesse != null ? contato.interesse : "não definido").append("\n\n");
        
        if (contato.escola != null && !contato.escola.isEmpty()) {
            observacoes.append("🏫 Estudante da: ").append(contato.escola);
            if (contato.serie != null && !contato.serie.isEmpty()) {
                observacoes.append(" (").append(contato.serie).append(")");
            }
            observacoes.append("\n\n");
        }
        
        if (contato.responsavel != null && !contato.responsavel.isEmpty() && !contato.responsavel.equals(contato.nome)) {
            observacoes.append("👨‍👩‍👧‍👦 Responsável: ").append(contato.responsavel).append("\n\n");
        }
        
        if (contato.ultimo_contato != null) {
            observacoes.append("📞 Último contato realizado em ")
                      .append(DateUtils.formatDateTime(getContext(), 
                          contato.ultimo_contato.getTime(), 
                          DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME))
                      .append("\n\n");
        } else {
            observacoes.append("❌ Nenhum contato registrado até o momento\n\n");
        }
        
        String estagio = getEstagioLead();
        if (estagio.contains("Novo") || estagio.contains("Potencial")) {
            observacoes.append("💡 Dica: Realizar primeiro contato para avaliar interesse");
        } else if (estagio.contains("Interessado")) {
            observacoes.append("💡 Dica: Agendar visita à escola ou reunião presencial");
        } else if (estagio.contains("Inscrito") || estagio.contains("Confirmado")) {
            observacoes.append("💡 Dica: Acompanhar processo de matrícula");
        } else if (estagio.contains("Matriculado")) {
            observacoes.append("🎉 Parabéns! Lead convertido com sucesso");
        }
        
        return observacoes.toString();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "DetalhesLeadFragment destruído");
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