
package donuseiei.test.com.authen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import donuseiei.test.com.authen.Adapter.ListCloudAdapter;
import donuseiei.test.com.authen.page.Profile_page;

public class CloudListActivity extends AppCompatActivity {
    private GridView gridView;
    private  List<ListCloud> list;
    private String id;
    private String password;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        id = bundle.getString("id");
        password = bundle.getString("password");
        Log.i("id in main",id+" "+password);
        setContentView(R.layout.activity_cloud_list);
        gridView = (GridView)findViewById(R.id.gridView);
        list = new ArrayList<>();
        RequestParams params = new RequestParams();
        params.add("password", password);
        getData(params);
    }

    public void doSomething(){
        ListCloudAdapter adapter = new ListCloudAdapter(this,android.R.layout.simple_expandable_list_item_2,list);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id1) {
                Intent intent = new Intent(CloudListActivity.this, MainActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("password", password);
                intent.putExtra("info", list.get(position).getName());
                startActivity(intent);
            }
        });
    }

    public void getData(RequestParams params){
        final ProgressDialog progressDialog = ProgressDialog.show(this, "In progress", "Loading");
        HTTPConnector.get("/dashboard/" + id + "/", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = "";
                for (int index = 0; index < responseBody.length; index++) {
                    response += (char) responseBody[index];
                }
                try {
                    if (!response.isEmpty()) {
                        JSONArray json_arr = new JSONArray(response);
                        System.out.println("json arr : " + json_arr.length());
                        for (int i = 0; i < json_arr.length(); i++) {
                            JSONArray j_arr = json_arr.getJSONObject(i).getJSONArray("vms");
                            for (int j = 0; j < j_arr.length(); j++) {
                                list.add(new ListCloud(json_arr.getJSONObject(i).getString("cloudName") + " : " + j_arr.getJSONObject(j).getString("vmIP")));
                            }
                        }
                        doSomething();
                    } else
                        Toast.makeText(CloudListActivity.this, "response null", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                Toast.makeText(CloudListActivity.this, "Error Code " + statusCode, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cloudlist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.profile_cloudlistpage:
                try {
                    Intent intent_profile = new Intent(this,Profile_page.class);
                    intent_profile.putExtra("idPassCloudProvIP",bundle);
                    startActivity(intent_profile);
                }
                catch (Exception e){
                    System.out.println("error in cloudlist menu");
                }
                /*replacePage(new Profile_page());
                tog.setBackgroundColor(oldColor);*/
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
