package donuseiei.test.com.authen.page;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import donuseiei.test.com.authen.HTTPConnector;
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
    private double cpu = 0;
    private double mem = 0;
    private double str = 0;
    private double net = 0;
    private String rcpu;
    private String rmem;
    private String rstr;
    private String rnet;
    private String name;
    private String ip;
    private ProgressDialog progressDialog;

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
            params.put("vmIP",ip);
            params.put("cloudProv",name);
        }
        list_name = new ArrayList<>();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_each_dash_page, container, false);

        progressDialog = ProgressDialog.show(getActivity(), "In progress", "Loading");
        progressDialog.setCanceledOnTouchOutside(true);

        v_cpu = (TextView)rootView.findViewById(R.id.text_cpu);
        v_mem = (TextView)rootView.findViewById(R.id.text_mem);
        v_str = (TextView)rootView.findViewById(R.id.text_storage);
        v_net = (TextView)rootView.findViewById(R.id.text_net);

        GraphView graph_cpu = (GraphView) rootView.findViewById(R.id.graph_cpu);
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
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[]{"60 seconds", "now"});
        staticLabelsFormatter.setVerticalLabels(new String[]{"0%", "50%","100%"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
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
                updateData(params);
                graph2LastXValue += 1d;
                mSeries_cpu.appendData(new DataPoint(graph2LastXValue, cpu), true, 100);
                mSeries_mem.appendData(new DataPoint(graph2LastXValue, mem), true, 100);
                mSeries_storage.appendData(new DataPoint(graph2LastXValue, str), true, 100);
                mSeries_net.appendData(new DataPoint(graph2LastXValue, net), true, 100);
                v_cpu.setText("CPU : " + cpu + "% (from " + rcpu + " GHz)");
                v_mem.setText("Memory : "+mem+"% (from "+rmem + " GB)");
                v_str.setText("Storage : "+str+"% (from "+rstr + " GB)");
                v_net.setText("Network : " + net + "% (from "+rnet + " GB)");

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

    public void updateData(RequestParams params){
        HTTPConnector.get("/dashboard/" + id + "/", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = "";
                for (int index = 0; index < responseBody.length; index++) {
                    response += (char) responseBody[index];
                }
                try {
                    if(!response.isEmpty()) {
                        JSONObject json = new JSONObject(response);
                        cpu = Math.round(Double.parseDouble(json.getString("Cpu")));
                        mem = Math.round(Double.parseDouble(json.getString("Mem")));
                        str = Math.round(Double.parseDouble(json.getString("Storage")));
                        net = Math.round(Double.parseDouble(json.getString("Network")));
                        rcpu = json.getString("rCpu");
                        rmem  = json.getString("rMem");
                        rstr = json.getString("rStorage");
                        rnet = json.getString("rNetwork");
                        progressDialog.dismiss();
                    }
                    else
                        Toast.makeText(getActivity(), "null response", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Error Code " + statusCode, Toast.LENGTH_LONG).show();
            }
        });
    }
}