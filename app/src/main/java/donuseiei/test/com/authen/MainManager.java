package donuseiei.test.com.authen;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import donuseiei.test.com.authen.page.Login_page;
import donuseiei.test.com.authen.page.Registe_page;

public class MainManager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_manager);
        Login_page fragment_login = new Login_page();
        FragmentTransaction transaction_plan = getSupportFragmentManager().beginTransaction();
        transaction_plan.replace(R.id.container, fragment_login);
        transaction_plan.commit();
    }
}
