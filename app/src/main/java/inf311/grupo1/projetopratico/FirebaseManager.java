package inf311.grupo1.projetopratico;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Classe utilitária para gerenciar operações do Firebase
 * Centraliza a lógica de autenticação e outras operações Firebase
 */
public class FirebaseManager {
    
    private static final String TAG = "FirebaseManager";
    private static FirebaseManager instance;
    private FirebaseAuth mAuth;
    
    // Interfaces para callbacks
    public interface AuthCallback {
        void onSuccess(FirebaseUser user);
        void onError(String errorMessage);
    }
    
    public interface UserCheckCallback {
        void onUserLoggedIn(FirebaseUser user);
        void onUserNotLoggedIn();
    }
    
    /**
     * Construtor privado para implementar Singleton
     */
    private FirebaseManager() {
        mAuth = FirebaseAuth.getInstance();
    }
    
    /**
     * Retorna a instância única do FirebaseManager (Singleton)
     */
    public static synchronized FirebaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseManager();
        }
        return instance;
    }
    
    /**
     * Retorna a instância do FirebaseAuth
     */
    public FirebaseAuth getAuth() {
        return mAuth;
    }
    
    /**
     * Retorna o usuário atualmente logado
     */
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }
    
    /**
     * Verifica se há um usuário logado
     */
    public boolean isUserLoggedIn() {
        return getCurrentUser() != null;
    }
    
    /**
     * Realiza login com email e senha
     */
    public void signInWithEmail(String email, String password, AuthCallback callback) {
        Log.d(TAG, "Tentando fazer login com email: " + email);
        
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Login bem-sucedido");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (callback != null) {
                                callback.onSuccess(user);
                            }
                        } else {
                            Log.w(TAG, "Falha no login", task.getException());
                            String errorMessage = getErrorMessage(task.getException());
                            if (callback != null) {
                                callback.onError(errorMessage);
                            }
                        }
                    }
                });
    }
    
    /**
     * Realiza cadastro com email e senha
     */
    public void createUserWithEmail(String email, String password, AuthCallback callback) {
        Log.d(TAG, "Tentando criar usuário com email: " + email);
        
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Usuário criado com sucesso");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (callback != null) {
                                callback.onSuccess(user);
                            }
                        } else {
                            Log.w(TAG, "Falha na criação do usuário", task.getException());
                            String errorMessage = getErrorMessage(task.getException());
                            if (callback != null) {
                                callback.onError(errorMessage);
                            }
                        }
                    }
                });
    }
    
    /**
     * Realiza logout
     */
    public void signOut() {
        Log.d(TAG, "Fazendo logout do usuário");
        mAuth.signOut();
    }
    
    /**
     * Envia email de redefinição de senha
     */
    public void sendPasswordResetEmail(String email, AuthCallback callback) {
        Log.d(TAG, "Enviando email de redefinição de senha para: " + email);
        
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email de redefinição enviado com sucesso");
                            if (callback != null) {
                                callback.onSuccess(null);
                            }
                        } else {
                            Log.w(TAG, "Falha ao enviar email de redefinição", task.getException());
                            String errorMessage = getErrorMessage(task.getException());
                            if (callback != null) {
                                callback.onError(errorMessage);
                            }
                        }
                    }
                });
    }
    
    /**
     * Verifica o status do usuário atual
     */
    public void checkCurrentUser(UserCheckCallback callback) {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            Log.d(TAG, "Usuário já está logado: " + currentUser.getEmail());
            if (callback != null) {
                callback.onUserLoggedIn(currentUser);
            }
        } else {
            Log.d(TAG, "Nenhum usuário logado");
            if (callback != null) {
                callback.onUserNotLoggedIn();
            }
        }
    }
    
    /**
     * Converte exceções do Firebase em mensagens de erro amigáveis
     */
    private String getErrorMessage(Exception exception) {
        if (exception == null) {
            return "Erro desconhecido";
        }
        
        String errorCode = exception.getMessage();
        if (errorCode == null) {
            return "Erro de autenticação";
        }
        
        // Mapear códigos de erro para mensagens em português
        if (errorCode.contains("invalid-email")) {
            return "Email inválido";
        } else if (errorCode.contains("user-disabled")) {
            return "Usuário desabilitado";
        } else if (errorCode.contains("user-not-found")) {
            return "Usuário não encontrado";
        } else if (errorCode.contains("wrong-password")) {
            return "Senha incorreta";
        } else if (errorCode.contains("invalid-credential")) {
            return "Credenciais inválidas";
        } else if (errorCode.contains("email-already-in-use")) {
            return "Este email já está em uso";
        } else if (errorCode.contains("weak-password")) {
            return "Senha muito fraca. Use pelo menos 6 caracteres";
        } else if (errorCode.contains("network-request-failed")) {
            return "Erro de conexão. Verifique sua internet";
        } else if (errorCode.contains("too-many-requests")) {
            return "Muitas tentativas. Tente novamente mais tarde";
        } else {
            return "Erro de autenticação: " + errorCode;
        }
    }
    
    /**
     * Obtém informações do usuário atual
     */
    public String getCurrentUserEmail() {
        FirebaseUser user = getCurrentUser();
        return user != null ? user.getEmail() : null;
    }
    
    /**
     * Obtém o UID do usuário atual
     */
    public String getCurrentUserUid() {
        FirebaseUser user = getCurrentUser();
        return user != null ? user.getUid() : null;
    }
    
    /**
     * Verifica se o email do usuário foi verificado
     */
    public boolean isEmailVerified() {
        FirebaseUser user = getCurrentUser();
        return user != null && user.isEmailVerified();
    }
} 