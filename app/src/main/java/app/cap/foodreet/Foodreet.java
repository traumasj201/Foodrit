package app.cap.foodreet;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.kakao.util.helper.log.Logger;

/**
 * Created by clear on 2017-10-28.
 */

public class Foodreet extends Application {
    public String roleType;

    @SuppressLint("StaticFieldLeak")
    private static volatile Foodreet instance = null;

    private final KakaoAdapter adapter = new KakaoAdapter() {
        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Context getApplicationContext() {
                    return Foodreet.getGlobalApplicationContext();
                }
            };
        }
    };
    /**
     * singleton 애플리케이션 객체를 얻는다.
     * @return singleton 애플리케이션 객체
     */
    public static Foodreet getGlobalApplicationContext() {
        if(instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        try {
            KakaoSDK.init(adapter);
        } catch (KakaoSDK.AlreadyInitializedException e) {
            Logger.e(e);
        }
        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }
    /**
     * 애플리케이션 종료시 singleton 어플리케이션 객체 초기화한다.
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }

    public void setRoleType(String roleType){
        this.roleType = roleType;
    }
    public String getRoleType(){
        return roleType;
    }
}
