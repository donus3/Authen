package donuseiei.test.com.authen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import donuseiei.test.com.authen.page.Bill_page;
import donuseiei.test.com.authen.page.EachDash_page;
import donuseiei.test.com.authen.page.Host_Report_page;
import donuseiei.test.com.authen.page.Mail_page;
import donuseiei.test.com.authen.page.Plan_page;
import donuseiei.test.com.authen.page.Profile_page;

public class MainActivity extends AppCompatActivity {

    private String id;
    private String password;
    private String info;
    private FragmentTabHost mTabHost;
    private Bundle bundle_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get id from login
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        password = bundle.getString("password");
        info = bundle.getString("info");
        bundle_send = new Bundle();
        bundle_send.putString("id", id);
        bundle_send.putString("password", password);
        bundle_send.putString("info", info);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.tab_main);
        mTabHost.addTab(mTabHost.newTabSpec("dashBoard").setIndicator("", getResources().getDrawable(R.drawable.dashboard)),
                EachDash_page.class, bundle_send);
        mTabHost.addTab(mTabHost.newTabSpec("Plan").setIndicator("", getResources().getDrawable(R.drawable.plan)),
                Plan_page.class, bundle_send);
        mTabHost.addTab(mTabHost.newTabSpec("Bill").setIndicator("", getResources().getDrawable(R.drawable.bill)),
                Bill_page.class, bundle_send);
        mTabHost.addTab(mTabHost.newTabSpec("Report").setIndicator("", getResources().getDrawable(R.drawable.report)),
                Host_Report_page.class, bundle_send);
        mTabHost.addTab(mTabHost.newTabSpec("Mail").setIndicator("", getResources().getDrawable(R.drawable.mail)),
                Mail_page.class, bundle_send);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.profile:
                Intent intent_profile = new Intent(this,Profile_page.class);
                intent_profile.putExtra("idPassCloudProvIP",bundle_send);
                startActivity(intent_profile);
                return true;
            case R.id.home:
                Intent intent = new Intent(this,CloudListActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("password",password);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}