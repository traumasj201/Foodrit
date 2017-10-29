package app.cap.foodreet.SignIn;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.AuthService;
import com.kakao.auth.network.response.AccessTokenInfoResponse;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.auth.Session;
import com.kakao.auth.ISessionCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.auth.AuthType;
import com.kakao.util.helper.log.Logger;

import java.security.MessageDigest;

import app.cap.foodreet.Foodreet;
import app.cap.foodreet.Main2Activity;
import app.cap.foodreet.MainActivity;
import app.cap.foodreet.R;
import app.cap.foodreet.SignUp.ForgotAccoutActivity;
import app.cap.foodreet.SignUp.SignUpActivity;

public class SignInActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private static final String TAG = SignInActivity.class.getSimpleName();
    private static final int REQUEST_KAKAO_LOGIN = 1000;
    public static final String INTENT_EXTRA_ACCESS_TOKEN = "access_token";
    private TextView btnNewAccount, btnForgotAccount;
    private EditText etEmail, etPassword;
    private Button btnLogIn;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mDatabaseRef;
    private GoogleApiClient mGoogleApiClient;
    private static AuthType auth_type = AuthType.KAKAO_TALK;
    private final ISessionCallback callback = new SessionCallback();
    private Session session;
    private static final String mOwner = "owner";
    private static final String mUser = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //getAppKeyHash();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabaseRef.keepSynced(true);
        etEmail = (EditText)findViewById(R.id.xetEmail);
        etPassword = (EditText)findViewById(R.id.xetPassword);
        btnLogIn = (Button)findViewById(R.id.xbtnLogIn);
        btnNewAccount = (TextView)findViewById(R.id.xbtnNewAccount);
        btnForgotAccount = (TextView)findViewById(R.id.xbtnForgotAccount);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        session = Session.getCurrentSession();
        session.addCallback(callback);
        session.checkAndImplicitOpen();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() { //영업자, 일반 사용자 구분 필요
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    mDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String role = dataSnapshot.child("role").getValue(String.class);
                            if (role!=null&&role.equals(mOwner)){
                                startActivity(new Intent(SignInActivity.this, Main2Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finish();
                            }else if(role!=null&&role.equals(mUser)){
                                startActivity(new Intent(SignInActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finish();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
        };
        btnLogIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                doLogIn();
            }
        });
        btnNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(SignInActivity.this, SignUpActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
            }
        });
        btnForgotAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, ForgotAccoutActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
 }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //로그인 activity를 이용하여
        //sdk에서 필요로 하는 activity를 띄우기 때문에 해당 결과를 세션에도 넘겨줘서 처리할 수 있도록 Session#handleActivityResult를 호출
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void successLogin() {
        requestMe();
    }

    //--kako Login--
    //영업자, 일반 사용자 구분 필요(DB에서 uid 값 가져오기)
  private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            Log.d(TAG, "onSessionOpened()"); //올바른 엑세스 토큰을 가지고 있는 경우
            successLogin();
        }
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e(TAG, "onSessionOpenFailed()");
            if (exception != null) { //Exception 발생
                Toast.makeText(getApplicationContext(), exception.getMessage()+getString(R.string.default_error),Toast.LENGTH_SHORT).show();
                Log.e(TAG, "exception: " + exception.getMessage());
            }
            finish();
        }
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }

            @Override
            public void onSuccess(UserProfile userProfile) { //userid, Role 값 필요
                try {
                    String accessToken = Session.getCurrentSession().getAccessToken();
                    Log.d(TAG, "accessToken: " + accessToken);
                }catch (Exception e){
                    e.printStackTrace();
                }
                String email = userProfile.getEmail();
                Logger.d("UserProfile : " + userProfile+""+email);
                String user_id = mFirebaseAuth.getCurrentUser().getUid();
                DatabaseReference userDbRef = mDatabaseRef.child(user_id);
                Foodreet foodreet = (Foodreet)getApplicationContext();
                if (userDbRef.child("email").equals(email)&&userDbRef.child("role").equals(mOwner)) {
                    startActivity(new Intent(SignInActivity.this, Main2Activity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                }else if (userDbRef.child("email").equals(email)&&userDbRef.child("role").equals(mUser)){
                    startActivity(new Intent(SignInActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                }else if(foodreet.getRoleType().equals(mOwner)&&foodreet!=null){
                    userDbRef.child("email").setValue(email);
                    userDbRef.child("u_id").setValue(user_id);
                    userDbRef.child("role").setValue(mOwner);
                }else if(foodreet.getRoleType().equals(mUser)&&foodreet!=null){
                    userDbRef.child("email").setValue(email);
                    userDbRef.child("u_id").setValue(user_id);
                    userDbRef.child("role").setValue(mUser);
                }
            }
            @Override
            public void onNotSignedUp() {
                requestSignUp();
            }
        });
    }
    //카카오톡 가입 요청
    private void requestSignUp() {
        UserManagement.requestSignup(new ApiResponseCallback<Long>() {
            @Override
            public void onNotSignedUp() {
            }
            @Override
            public void onSuccess(Long result) {
                requestMe();
            }
            @Override
            public void onFailure(ErrorResult errorResult) {
                final String message = "UsermgmtResponseCallback : failure : " + errorResult;
                Logger.w(message);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }
        }, null);
    }

    private void requestAccessTokenInfo() {
        AuthService.requestAccessTokenInfo(new ApiResponseCallback<AccessTokenInfoResponse>() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }
            @Override
            public void onNotSignedUp() {
                // not happened
            }
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e("failed to get access token info. msg=" + errorResult);
            }
            @Override
            public void onSuccess(AccessTokenInfoResponse accessTokenInfoResponse) {
                long userId = accessTokenInfoResponse.getUserId();
                Logger.d("this access token is for userId=" + userId);
                long expiresInMilis = accessTokenInfoResponse.getExpiresInMillis();
                Logger.d("this access token expires after " + expiresInMilis + " milliseconds.");
            }
        });
    }

    //--Email Login--
    private void doLogIn() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        checkUserExists();
                    }
                    else{
                        Toast.makeText(SignInActivity.this, getString(R.string.incorrect_id), Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private void checkUserExists() {

        final String user_id = mFirebaseAuth.getCurrentUser().getUid();

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user_id)) {
                    progressBar.setVisibility(View.GONE);
                    String role = dataSnapshot.child("role").getValue(String.class);
                    if (role!=null&&role.equals(mUser)) {
                        startActivity(new Intent(SignInActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }
                    else if(role!=null&&role.equals(mOwner)){
                        startActivity(new Intent(SignInActivity.this, Main2Activity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),getString(R.string.default_error),Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignInActivity.this, getString(R.string.incorrect_et), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.d("Hash key:", "*****"+something+"*****");
            }
        } catch (Exception e){
            Log.e("name not found", e.toString());
        }
    }
}
