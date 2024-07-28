package com.ucsm.uniseek.appuniseek;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ucsm.uniseek.R;

public class LoginauthActivity extends AppCompatActivity {
    private static final String TAG = "LoginauthActivity"; // Declarar TAG
    Button login, register, lostPassword;
    EditText email, password;
    ImageButton passwordVisibilityButton;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loginauth);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setup();
    }

    private void setup() {
        login = findViewById(R.id.loginButton);
        register = findViewById(R.id.registerButton);
        lostPassword = findViewById(R.id.contraseñalost); // Añadir la referencia al botón "contraseñalost"
        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);
        passwordVisibilityButton = findViewById(R.id.passwordVisibilityButton);

        passwordVisibilityButton.setOnClickListener(v -> {
            if (isPasswordVisible) {
                // Ocultar contraseña
                password.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordVisibilityButton.setImageResource(R.drawable.ic_visibility); // Cambiar ícono
            } else {
                // Mostrar contraseña
                password.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                passwordVisibilityButton.setImageResource(R.drawable.ic_visibility_off); // Cambiar ícono
            }
            // Mover el cursor al final del texto
            password.setSelection(password.getText().length());
            isPasswordVisible = !isPasswordVisible;
        });

        register.setOnClickListener(v -> {
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();
            if (!emailText.isEmpty() && !passwordText.isEmpty()) {
                mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Registro exitoso
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        user.sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(LoginauthActivity.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Log.e(TAG, "sendEmailVerification", task.getException());
                                                            Toast.makeText(LoginauthActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                } else {
                                    // Si el registro falla, mostrar un mensaje al usuario.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    String errorMessage = "Authentication failed.";
                                    if (task.getException() != null && task.getException().getMessage().contains("The email address is badly formatted")) {
                                        errorMessage = "Invalid email format.";
                                    } else if (task.getException() != null && task.getException().getMessage().contains("The email address is not allowed")) {
                                        errorMessage = "Email domain not allowed.";
                                    }
                                    Toast.makeText(LoginauthActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Inicio de sesión de usuario
        login.setOnClickListener(v -> {
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();
            if (!emailText.isEmpty() && !passwordText.isEmpty()) {
                mAuth.signInWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Inicio de sesión exitoso
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null && user.isEmailVerified()) {
                                        // Redirigir a FirstUniseekActivity
                                        Intent intent = new Intent(LoginauthActivity.this, FirstUniseekActivity.class);
                                        intent.putExtra("email", user.getEmail());
                                        startActivity(intent);
                                        finish(); // Opcional: Cierra la actividad actual
                                    } else if (user != null) {
                                        Toast.makeText(LoginauthActivity.this, "Please verify your email before logging in.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // Si el inicio de sesión falla, mostrar un mensaje al usuario.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginauthActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Redireccionar a LostPasswordActivity
        lostPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginauthActivity.this, LostPasswordActivity.class);
            startActivity(intent);
        });
    }
}



