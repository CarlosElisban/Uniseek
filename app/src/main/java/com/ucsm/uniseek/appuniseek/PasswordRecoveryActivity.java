package com.ucsm.uniseek.appuniseek;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.ucsm.uniseek.R;

public class PasswordRecoveryActivity extends AppCompatActivity {
    private static final String TAG = "PasswordRecoveryActivity";
    EditText emailLost;
    Button resetPassword, volver;

    FirebaseAuth mAuthrset = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lost_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setup();
    }

    private void setup() {
        resetPassword = findViewById(R.id.ResetPassword);
        emailLost = findViewById(R.id.emailLostEditText);
        volver = findViewById(R.id.volver);

        resetPassword.setOnClickListener(v -> {
            String emailText = emailLost.getText().toString();
            if (!emailText.isEmpty()) {
                mAuthrset.sendPasswordResetEmail(emailText)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(PasswordRecoveryActivity.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e(TAG, "sendPasswordResetEmail", task.getException());
                                    Toast.makeText(PasswordRecoveryActivity.this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(PasswordRecoveryActivity.this, "Please enter your email address.", Toast.LENGTH_SHORT).show();
            }
        });

        volver.setOnClickListener(v -> {
            Intent intent = new Intent(PasswordRecoveryActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
