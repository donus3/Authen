package donuseiei.test.com.authen.page;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import donuseiei.test.com.authen.HTTPConnector;
import donuseiei.test.com.authen.Plan;
import donuseiei.test.com.authen.R;


public class Prediction_page extends Fragment {
    private TextView pre_cpu;
    private TextView pre_mem;
    private TextView pre_str;
    private TextView pre_net;
    private TextView cur_cpu;
    private TextView cur_mem;
    private TextView cur_str;
    private TextView cur_net;
    private View rootView;
    private String id;
    private String password;
    private String cloudProv;
    private String ip;
    private RequestParams params;


    public Prediction_page() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString("id");
            password = getArguments().getString("password");
            cloudProv = getArguments().getString("cloudName");
            ip = getArguments().getString("ip");
            params = new RequestParams();
            params.put("password", password);
            params.put("vmIP",ip);
            Calendar calendar = Calendar.getInstance();
            String date = ""+calendar.getTime().getMonth()+" "+calendar.getTime().getYear();
            params.put("start", date);
            params.put("end", date);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_prediction_page, container, false);
        pre_cpu = (TextView)rootView.findViewById(R.id.predict_cpu);
        pre_mem = (TextView)rootView.findViewById(R.id.predict_mem);
        pre_str = (TextView)rootView.findViewById(R.id.predict_str);
        pre_net = (TextView)rootView.findViewById(R.id.predict_net);

        cur_cpu = (TextView)rootView.findViewById(R.id.predict_current_cpu);
        cur_mem = (TextView)rootView.findViewById(R.id.predict_current_mem);
        cur_str = (TextView)rootView.findViewById(R.id.predict_current_str);
        cur_net = (TextView)rootView.findViewById(R.id.predict_current_net);
        getPrediction(params);
        return rootView;
    }

    public void getPrediction(RequestParams params){
        HTTPConnector.get("predict/"+id+"/", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = "";
                for (int index = 0; index < responseBody.length; index++) {
                    response += (char) responseBody[index];
                }
                if(!response.isEmpty()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        pre_cpu.setText(jsonObject.getString("cpus") + " GHz / ");
                        pre_mem.setText(jsonObject.getString("mems")+ " GB / ");
                        pre_str.setText(jsonObject.getString("storage")+ " GB / ");
                        pre_net.setText(jsonObject.getString("networks")+ " GB / ");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestParams params1 = new RequestParams();
                    params1.put("password", password);
                    params1.put("vmIP",ip);
                    params1.put("cloudProv",cloudProv);
                    getCurrent(params1);
                }
                else
                    Toast.makeText(getActivity(), "null response", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "Error code "+statusCode, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getCurrent(RequestParams params){
        HTTPConnector.get("plan/" + id + "/", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = "";
                for (int index = 0; index < responseBody.length; index++) {
                    response += (char) responseBody[index];
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    cur_cpu.setText(jsonObject.getString("cpu")+" GHz(current)");
                    cur_mem.setText(jsonObject.getString("mem")+" GB(current)");
                    cur_str.setText(jsonObject.getString("storage")+" GB(current)");
                    cur_net.setText(jsonObject.getString("network")+" GB(current)");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "Error code "+statusCode, Toast.LENGTH_LONG).show();
            }
        });
    }

}
