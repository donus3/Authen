package donuseiei.test.com.authen.page;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import donuseiei.test.com.authen.AddingCloud_page;
import donuseiei.test.com.authen.CloudListActivity;
import donuseiei.test.com.authen.HTTPConnector;
import donuseiei.test.com.authen.Profile;
import donuseiei.test.com.authen.R;

public class Profile_page extends AppCompatActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "password";

    // TODO: Rename and change types of parameters
    private String id;
    private String password;
    private RequestParams params;
    private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_page);
        bundle = getIntent().getExtras().getBundle("idPassCloudProvIP");
        if (bundle != null) {
            id = bundle.getString("id");
            password = bundle.getString("password");
            params = new RequestParams();
            params.put("password",password);
            getDetailPlan(params, "request/profile/user/" + id + "/");
        }
        Button button = (Button)findViewById(R.id.btn_edit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_page.this, Edit_page.class);
                intent.putExtra("idPassCloudProvIP", bundle);
                startActivity(intent);
            }
        });
        Button button_add = (Button)findViewById(R.id.btn_addcloud);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_page.this, AddingCloud_page.class);
                intent.putExtra("idPassCloudProvIP",bundle);
                startActivity(intent);
            }
        });
        logout();
    }

    public void getDetailPlan(RequestParams params,String url) {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "In progress", "Loading");
        HTTPConnector.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = "";
                for (int index = 0; index < responseBody.length; index++) {
                    response += (char) responseBody[index];
                }
                try {
                    if (!response.isEmpty()) {
                        JSONObject json = new JSONObject(response);
                        Profile profile = new Profile(
                                json.getString("name"),
                                json.getString("email"));
                        CreateView(profile);
                    } else
                        Toast.makeText(Profile_page.this, "null response", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                Toast.makeText(Profile_page.this, "Error Code " + statusCode, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void CreateView(Profile p){
        TextView tname = (TextView)findViewById(R.id.sUsername);
        tname.setText(p.getUsername());
        TextView temail = (TextView)findViewById(R.id.sEmail);
        temail.setText(p.getEmail());
    }

    public void logout(){
        Button btn_logout = (Button)findViewById(R.id.btn_logout);
        Button btn_edit = (Button)findViewById(R.id.btn_edit);
        btn_logout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.main_home:
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
