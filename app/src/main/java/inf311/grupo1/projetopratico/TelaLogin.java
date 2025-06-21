package inf311.grupo1.projetopratico;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

public class TelaLogin extends AppCompatActivity {

    private static final String TAG = "TelaLogin";
    
    // Firebase Manager
    private FirebaseManager firebaseManager;
    
    // UI Elements
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnAdmin;
    private Button btnUser;
    
    // User type
    private String userType = "consultor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_login);

        // Inicializar Firebase Manager
        firebaseManager = FirebaseManager.getInstance();
        
        // Inicializar elementos da UI
        initializeUI();
        
        // Configurar listeners
        setupListeners();
        
        Log.d(TAG, "TelaLogin inicializada com Firebase Auth");
    }

    @Override
    public void onStart() {
        super.onStart();
        // Verificar se o usuário já está logado usando FirebaseManager
        firebaseManager.checkCurrentUser(new FirebaseManager.UserCheckCallback() {
            @Override
            public void onUserLoggedIn(FirebaseUser user) {
                Log.d(TAG, "Usuário já está logado: " + user.getEmail());
                navigateToDashboard();
            }

            @Override
            public void onUserNotLoggedIn() {
                Log.d(TAG, "Nenhum usuário logado");
                // Usuário não está logado, continuar na tela de login
            }
        });
    }

    /**
     * Inicializa todos os elementos da interface do usuário
     */
    private void initializeUI() {
        etEmail = findViewById(R.id.email_edit);
        etPassword = findViewById(R.id.password_edit);
        btnLogin = findViewById(R.id.login_button);
        btnAdmin = findViewById(R.id.admin_btn);
        btnUser = findViewById(R.id.user_btn);
        
        // Configurar estado inicial dos botões
        setUserTypeButtonsState();
    }

    /**
     * Configura os listeners para os elementos da UI
     */
    private void setupListeners() {
        // Listener para o botão de login
        if (btnLogin != null) {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performLogin();
                }
            });
        }
    }

    /**
     * Método chamado quando o botão "Administrador" é clicado
     */
    public void set_as_admin(View v) {
        userType = "administrador";
        setUserTypeButtonsState();
        Log.d(TAG, "Tipo de usuário definido como: Administrador");
    }

    /**
     * Método chamado quando o botão "Consultor" é clicado
     */
    public void set_as_user(View v) {
        userType = "consultor";
        setUserTypeButtonsState();
        Log.d(TAG, "Tipo de usuário definido como: Usuário");
    }

    /**
     * Atualiza o estado visual dos botões de tipo de usuário
     */
    private void setUserTypeButtonsState() {
        if (btnAdmin != null && btnUser != null) {
            if (userType.equals("administrador")) {
                btnAdmin.setSelected(true);
                btnUser.setSelected(false);
            } else {
                btnAdmin.setSelected(false);
                btnUser.setSelected(true);
            }
        }
    }

    /**
     * Realiza o processo de login com Firebase usando FirebaseManager
     */
    private void performLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validar campos
        if (!validateInputs(email, password)) {
            return;
        }

        // Mostrar loading (desabilitar botão)
        setLoadingState(true);

        // Fazer login usando FirebaseManager
        firebaseManager.signInWithEmail(email, password, new FirebaseManager.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                setLoadingState(false);
                Log.d(TAG, "Login realizado com sucesso para: " + user.getEmail());
                Toast.makeText(TelaLogin.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                navigateToDashboard();
            }

            @Override
            public void onError(String errorMessage) {
                setLoadingState(false);
                Log.e(TAG, "Erro no login: " + errorMessage);
                Toast.makeText(TelaLogin.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Valida os campos de entrada
     */
    private boolean validateInputs(String email, String password) {
        // Validar email
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email é obrigatório");
            etEmail.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Digite um email válido");
            etEmail.requestFocus();
            return false;
        }

        // Validar senha
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Senha é obrigatória");
            etPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            etPassword.setError("Senha deve ter pelo menos 6 caracteres");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Gerencia o estado de loading da interface
     */
    private void setLoadingState(boolean isLoading) {
        if (btnLogin != null) {
            btnLogin.setEnabled(!isLoading);
            btnLogin.setText(isLoading ? "Entrando..." : "Entrar");
        }
        
        if (etEmail != null) {
            etEmail.setEnabled(!isLoading);
        }
        
        if (etPassword != null) {
            etPassword.setEnabled(!isLoading);
        }
        
        if (btnAdmin != null) {
            btnAdmin.setEnabled(!isLoading);
        }
        
        if (btnUser != null) {
            btnUser.setEnabled(!isLoading);
        }
    }

    /**
     * Navega para a tela do dashboard usando a estrutura de fragments
     */
    private void navigateToDashboard() {
        Intent intent = new Intent(this, MainActivityNova.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        
        // Determinar se é admin baseado no tipo de usuário selecionado
        boolean isAdmin = userType.equals("administrador");
        intent.putExtra("is_admin", isAdmin);
        
        // Passar informações do usuário logado
        String userEmail = firebaseManager.getCurrentUserEmail();
        String userUid = firebaseManager.getCurrentUserUid();
        
        intent.putExtra("user_email", userEmail);
        intent.putExtra("user_uid", userUid);
        
        Log.d(TAG, "Navegando para MainActivityNova (estrutura de fragments) - Usuário: " + userEmail + ", Admin: " + isAdmin);
        
        startActivity(intent);
        finish(); // Fechar a tela de login
    }

    /**
     * Método para compatibilidade com o layout existente
     */
    public void to_dashboard(View v) {
        performLogin();
    }
}
