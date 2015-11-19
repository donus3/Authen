package donuseiei.test.com.authen.page;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import donuseiei.test.com.authen.Adapter.BillAdapter;
import donuseiei.test.com.authen.Bill;
import donuseiei.test.com.authen.HTTPConnector;
import donuseiei.test.com.authen.R;

public class Bill_page extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "password";
    TextView t;
    // TODO: Rename and change types of parameters
    private String id;
    private String password;
    private String cloudProv;
    private String ip;
    private View bill_page;
    private RequestParams params;

    public Bill_page() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString("id");
            password = getArguments().getString("password");
            cloudProv = getArguments().getString("info").split(" : ")[0];
            ip = getArguments().getString("info").split(" : ")[1];
            params = new RequestParams();
            params.put("password",password);
            params.put("vmIP",ip);
            params.put("cloudProv",cloudProv);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bill_page =  inflater.inflate(R.layout.fragment_bill_page, container, false);
        getBillPage(params, "bill/vm/" + id + "/");
        return bill_page;
    }

    public void getBillPage(RequestParams params,String url){
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "In progress", "Loading");
        HTTPConnector.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = "";
                for (int index = 0; index < responseBody.length; index++) {
                    response += (char) responseBody[index];
                }
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray json_arr = json.getJSONArray("plans");
                    Bill bill = new Bill(
                            json_arr.getJSONObject(0).getString("cloudProv"),
                            json.getString("timestamp"),
                            json_arr.getJSONObject(0).getString("monthlyRate"),
                            //json.getString("arrears"),
                            "0",
                            json_arr.getJSONObject(0).getString("cpu"),
                            json_arr.getJSONObject(0).getString("mem"),
                            json_arr.getJSONObject(0).getString("network"),
                            json_arr.getJSONObject(0).getString("storage"));
                    CreateView(bill);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "Error Code " + statusCode, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    public void CreateView(Bill b){
        TextView t = (TextView)bill_page.findViewById(R.id.bill_cpu);
        t.setText(b.getCpu()+" GHz");
        t = (TextView)bill_page.findViewById(R.id.bill_mem);
        t.setText(b.getMem()+" GB");
        t = (TextView)bill_page.findViewById(R.id.bill_network);
        t.setText(b.getNetwork()+" GB");
        t = (TextView)bill_page.findViewById(R.id.bill_storage);
        t.setText(b.getStorage()+" GB");
        t = (TextView)bill_page.findViewById(R.id.rate);
        t.setText("$"+b.getRate());
        t = (TextView)bill_page.findViewById(R.id.arrears);
        t.setText("$"+b.getArrears());
        t = (TextView)bill_page.findViewById(R.id.total);
        t.setText("$"+b.getTotal());
        t = (TextView)bill_page.findViewById(R.id.cloud_bill);
        t.setText(b.getName()+" ");
        t = (TextView)bill_page.findViewById(R.id.month_bill);
        t.setText(b.getMonth());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
