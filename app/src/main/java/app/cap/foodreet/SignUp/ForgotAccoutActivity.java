package app.cap.foodreet.SignUp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import app.cap.foodreet.R;

public class ForgotAccoutActivity extends AppCompatActivity {

    private EditText inputEmail;
    private Button btnReset;
    private FirebaseAuth mFirebaseAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_accout);
        inputEmail = (EditText) findViewById(R.id.email);
        btnReset = (Button)findViewById(R.id.btn_reset_password);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);
        mFirebaseAuth = FirebaseAuth.getInstance();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), getString(R.string.incorrect_et), Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                mFirebaseAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotAccoutActivity.this, getString(R.string.sent_password), Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(ForgotAccoutActivity.this, getString(R.string.error_sent_password), Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                }
        });
    }
}
