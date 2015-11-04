package donuseiei.test.com.authen;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import donuseiei.test.com.authen.page.AllDash_page;
import donuseiei.test.com.authen.page.Bill_page;
import donuseiei.test.com.authen.page.ChangePlane_page;
import donuseiei.test.com.authen.page.Dash_page;
import donuseiei.test.com.authen.page.EachDash_page;
import donuseiei.test.com.authen.page.Login_page;
import donuseiei.test.com.authen.page.Mail_page;
import donuseiei.test.com.authen.page.Plan_page;
import donuseiei.test.com.authen.page.Profile_page;
import donuseiei.test.com.authen.page.Registe_page;
import donuseiei.test.com.authen.page.Report_page;

public class MainActivity extends AppCompatActivity {

    private RadioButton dash;
    private RadioButton plan;
    private RadioButton bill;
    private RadioButton report;
    private RadioButton mail;
    private RadioButton tog;
    private final int oldColor = Color.rgb(33, 150, 243); //tab color before clicked
    private final int clickColor = Color.rgb(19,90,145);
    private String id;
    private String password;
    private String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get id from login
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        password = bundle.getString("password");
        info = bundle.getString("info");
        //construct tab button
        dash = (RadioButton)findViewById(R.id.btnDash);
        plan = (RadioButton)findViewById(R.id.btnPlan);
        bill = (RadioButton)findViewById(R.id.btnBill);
        report = (RadioButton)findViewById(R.id.btnReport);
        mail = (RadioButton)findViewById(R.id.btnMail);
        //default
        tog = dash;
        toggle(dash);
        replacePage(new EachDash_page());
        //check button menu clicked
        onClick();
    }
    /*
    * check tab clicked and change page
    * */
    private void onClick(){
        dash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(dash);
                replacePage(new EachDash_page());
            }
        });
        plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(plan);
                replacePage(new Plan_page());
            }
        });
        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(bill);
                replacePage(new Bill_page());
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(report);
                replacePage(new Report_page());
            }
        });
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(mail);
                replacePage(new Mail_page());
            }
        });
    }
    /*
    * change page
    * */
    public void replacePage(Fragment f){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_ac, f);
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("password",password);
        bundle.putString("info",info);
        // set Fragmentclass Arguments
        f.setArguments(bundle);
        transaction.commit();
    }
    /*
    * set color background when tab clicked
    * */
    public void toggle(RadioButton radioButton){
        tog.setBackgroundColor(oldColor);
        radioButton.setBackgroundColor(clickColor);
        tog = radioButton;
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
                tog.setBackgroundColor(oldColor);
                replacePage(new Profile_page());
                return true;
            case R.id.home:
                tog.setBackgroundColor(oldColor);
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
