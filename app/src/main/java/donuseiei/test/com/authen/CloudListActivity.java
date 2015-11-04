
package donuseiei.test.com.authen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
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

public class CloudListActivity extends AppCompatActivity {
    private GridView gridView;
    private  List<ListCloud> list;
    private String id;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        password = bundle.getString("password");
        setContentView(R.layout.activity_cloud_list);
        gridView = (GridView)findViewById(R.id.gridView);
        list = new ArrayList<>();
        RequestParams params = new RequestParams();
        params.add("password",password);
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
                intent.putExtra("info",list.get(position).getName());
                startActivity(intent);
            }
        });
    }

    public void getData(RequestParams params){
        HTTPConnector.get("/dashboard/" + id + "/", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = "";
                for (int index = 0; index < responseBody.length; index++) {
                    response += (char) responseBody[index];
                }
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray json_arr = json.getJSONArray("clouds");
                    System.out.println("json arr : " + json_arr.length());
                    for (int i=0 ; i < json_arr.length();i++) {
                        JSONArray j_arr = json_arr.getJSONObject(i).getJSONArray("vms");
                        for(int j =0 ;j<j_arr.length();j++){
                            list.add(new ListCloud(json_arr.getJSONObject(i).getString("cloudName") +" : "+ j_arr.getJSONObject(j).getString("vmIP")));
                        }
                    }
                    doSomething();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(CloudListActivity.this, "Error Code " + statusCode, Toast.LENGTH_LONG).show();
            }
        });
    }

}
