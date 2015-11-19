package donuseiei.test.com.authen;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

public class AddingCloud_page extends Activity {
    private EditText cloud_provider;
    private EditText cloud_id;
    private EditText cloud_password;
    private Button btn_add;
    private Button btn_cancel;
    private String id;
    private String password;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_cloud_page);

        bundle = getIntent().getExtras().getBundle("idPassCloudProvIP");
        if(bundle!=null){
            id = bundle.getString("id");
            password = bundle.getString("password");
        }
        cloud_id = (EditText)findViewById(R.id.add_cloud_id);
        cloud_password = (EditText)findViewById(R.id.add_cloud_password);
        cloud_provider = (EditText)findViewById(R.id.add_cloudProv);

        btn_add = (Button)findViewById(R.id.btn_add_addpage);
        btn_cancel = (Button)findViewById(R.id.btn_cancel_addpage);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddingCloud_page.this,CloudListActivity.class);
                intent.putExtra("idPassCloudProvIP", bundle);
                startActivity(intent);
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams();
                params.put("password",password);
                params.put("cloudProv",cloud_provider.getText().toString());
                params.put("cloudUsername",cloud_id.getText().toString());
                params.put("cloudPassword",cloud_password.getText().toString());
                addCloudAccount(params);
            }
        });
    }
    public void addCloudAccount(RequestParams params){
        HTTPConnector.get("/plan/addCloudAccount/"+id+"/", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Intent intent = new Intent(AddingCloud_page.this,CloudListActivity.class);
                intent.putExtra("idPassCloudProvIP", bundle);
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
