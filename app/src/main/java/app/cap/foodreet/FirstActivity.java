package app.cap.foodreet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import app.cap.foodreet.SignIn.SignInActivity;

public class FirstActivity extends AppCompatActivity {

    private Button btnOwner;
    private Button btnUser;
    private static final String mOwner = "owner";
    private static final String mUser = "user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        final Foodreet foodreet = (Foodreet)getApplicationContext();
        btnOwner = (Button)findViewById(R.id.button_owner);
        btnUser = (Button)findViewById(R.id.button_user);

        btnOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getString(R.string.join_owner),Toast.LENGTH_SHORT).show();
                foodreet.setRoleType(mOwner);
                startActivity(new Intent(FirstActivity.this, SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getString(R.string.join_user), Toast.LENGTH_SHORT).show();
                foodreet.setRoleType(mUser);
                startActivity(new Intent(FirstActivity.this, SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }
}
