package donuseiei.test.com.authen.page;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import donuseiei.test.com.authen.Adapter.PlanViewAdapter;
import donuseiei.test.com.authen.HTTPConnector;
import donuseiei.test.com.authen.ListItemPlan;
import donuseiei.test.com.authen.Plan;
import donuseiei.test.com.authen.R;


public class EachDash_page extends Fragment {

    private final Handler mHandler = new Handler();
    private Runnable mTimer;
    private LineGraphSeries<DataPoint> mSeries_cpu;
    private LineGraphSeries<DataPoint> mSeries_mem;
    private LineGraphSeries<DataPoint> mSeries_storage;
    private LineGraphSeries<DataPoint> mSeries_net;
    private double graph2LastXValue = 5d;
    private TextView v_cpu;
    private TextView v_mem;
    private TextView v_str;
    private TextView v_net;
    private String id;
    private String password;
    private RequestParams params;
    private List<String> list_name;
    private View rootView;
    private JSONObject update_json;
    private JSONArray update_json_arr;
    private double cpu = 0;
    private double mem = 0;
    private double str = 0;
    private double net = 0;
    private String name;
    private String ip;
    private GraphView graph_cpu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString("id");
            password = getArguments().getString("password");
            name = getArguments().getString("info").split(" : ")[0];
            ip = getArguments().getString("info").split(" : ")[1];
            params = new RequestParams();
            params.put("password",password);
        }
        list_name = new ArrayList<>();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_each_dash_page, container, false);

        v_cpu = (TextView)rootView.findViewById(R.id.text_cpu);
        v_mem = (TextView)rootView.findViewById(R.id.text_mem);
        v_str = (TextView)rootView.findViewById(R.id.text_storage);
        v_net = (TextView)rootView.findViewById(R.id.text_net);

        graph_cpu = (GraphView) rootView.findViewById(R.id.graph_cpu);
        mSeries_cpu = new LineGraphSeries<>();
        init(graph_cpu,mSeries_cpu);

        GraphView graph_mem = (GraphView) rootView.findViewById(R.id.graph_mem);
        mSeries_mem = new LineGraphSeries<>();
        init(graph_mem,mSeries_mem);

        GraphView graph_storage = (GraphView) rootView.findViewById(R.id.graph_storage);
        mSeries_storage = new LineGraphSeries<>();
        init(graph_storage,mSeries_storage);

        GraphView graph_net = (GraphView) rootView.findViewById(R.id.graph_net);
        mSeries_net = new LineGraphSeries<>();
        init(graph_net,mSeries_net);

        //getData(params);

        return rootView;
    }
    public void init(GraphView graph,LineGraphSeries<DataPoint> list){
        graph.addSeries(list);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(100);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);
        graph.getViewport().setYAxisBoundsManual(true);
    }
    @Override
    public void onResume() {
        super.onResume();
        mTimer = new Runnable() {
            @Override
            public void run() {
                updateData(params,name,ip);
                graph2LastXValue += 1d;
                Log.i("value",name + ip + " " + cpu+" "+mem+" "+str+" "+net);
                mSeries_cpu.appendData(new DataPoint(graph2LastXValue, cpu), true, 100);
                mSeries_mem.appendData(new DataPoint(graph2LastXValue, mem), true, 100);
                mSeries_storage.appendData(new DataPoint(graph2LastXValue, str), true, 100);
                mSeries_net.appendData(new DataPoint(graph2LastXValue, net), true, 100);
                v_cpu.setText("CPU : " + cpu + "%");
                v_mem.setText("Memory : "+mem+"%");
                v_str.setText("Storage : "+str+"%");
                v_net.setText("Network : " + net + "%");

                mHandler.postDelayed(this, 1000);
            }
        };
        mHandler.postDelayed(mTimer, 1000);
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(mTimer);
        super.onPause();
    }

    public void updateData(RequestParams params,final String name,final String ip){
        HTTPConnector.get("/dashboard/" + id + "/", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = "";
                for (int index = 0; index < responseBody.length; index++) {
                    response += (char) responseBody[index];
                }
                try {
                    update_json = new JSONObject(response);
                    update_json_arr = update_json.getJSONArray("clouds");
                    System.out.println("json arr : " + update_json_arr.toString());
                    for (int i = 0; i < update_json_arr.length(); i++) {
                        if(update_json_arr.getJSONObject(i).getString("cloudName").equals(name)) {
                            JSONArray j_arr = update_json_arr.getJSONObject(i).getJSONArray("vms");
                            for (int j = 0; j < j_arr.length(); j++) {
                                if(j_arr.getJSONObject(j).getString("vmIP").equals(ip)) {
                                    cpu = Double.parseDouble(j_arr.getJSONObject(j).getString("Cpu"));
                                    mem = Double.parseDouble(j_arr.getJSONObject(j).getString("Mem"));
                                    str = Double.parseDouble(j_arr.getJSONObject(j).getString("Storage"));
                                    net = Double.parseDouble(j_arr.getJSONObject(j).getString("Network"));
                                    break;
                                }
                            }
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "Error Code " + statusCode, Toast.LENGTH_LONG).show();
            }
        });
    }
}