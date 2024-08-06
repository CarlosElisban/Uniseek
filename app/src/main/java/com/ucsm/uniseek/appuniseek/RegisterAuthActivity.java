package com.ucsm.uniseek.appuniseek;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ucsm.uniseek.R;

public class RegisterAuthActivity extends AppCompatActivity {
    private static final String TAG = "RegisterAuthActivity";
    Button Register,volver;
    EditText email, password;
    ImageButton passwordVisibilityButtonReg;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    boolean isPasswordVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_auth);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setup();
    }
    private void setup() {
        Register = findViewById(R.id.Registrar);
        email = findViewById(R.id.emailEditTextReg);
        password = findViewById(R.id.passwordEditTextReg);
        volver = findViewById(R.id.volver);
        passwordVisibilityButtonReg = findViewById(R.id.passwordVisibilityButtonReg);

        passwordVisibilityButtonReg.setOnClickListener(v -> {
            if (isPasswordVisible) {
                // Ocultar contraseña
                password.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordVisibilityButtonReg.setImageResource(R.drawable.ic_visibility); // Cambiar ícono
            } else {
                // Mostrar contraseña
                password.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                passwordVisibilityButtonReg.setImageResource(R.drawable.ic_visibility_off); // Cambiar ícono
            }
            // Mover el cursor al final del texto
            password.setSelection(password.getText().length());
            isPasswordVisible = !isPasswordVisible;
        });

        Register.setOnClickListener(v -> {
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
                                                            Toast.makeText(RegisterAuthActivity.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Log.e(TAG, "sendEmailVerification", task.getException());
                                                            Toast.makeText(RegisterAuthActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(RegisterAuthActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        volver.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterAuthActivity.this, LoginauthActivity.class);
            startActivity(intent);
            finish();
        });
    }
}