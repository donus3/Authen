package donuseiei.test.com.authen.page;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import donuseiei.test.com.authen.HTTPConnector;
import donuseiei.test.com.authen.R;

public class Report_page extends DialogFragment{

    // TODO: Rename and change types of parameters
    private String id;
    private String password;
    private String cloudProv;
    private String ip;
    private Button btn_start;
    private Button btn_end;
    private RequestParams params;
    private TextView v_cpu;
    private TextView v_mem;
    private TextView v_str;
    private TextView v_net;
    private ProgressDialog progressDialog;
    private String peakCPU;
    private String peakMem;
    private String peakStr;
    private String peakNet;
    private BarGraphSeries<DataPoint> CPUseries = new BarGraphSeries<>();
    private BarGraphSeries<DataPoint> Memseries = new BarGraphSeries<>();
    private BarGraphSeries<DataPoint> Strseries = new BarGraphSeries<>();
    private BarGraphSeries<DataPoint> Netseries = new BarGraphSeries<>();
    private View rootView;
    private boolean set_start;
    private boolean set_end;
    private Date date_start;
    private Date date_end;

    public Report_page() {
        // Required empty public constructor
    }
    private DatePickerDialog.OnDateSetListener datePickerListener_start = new DatePickerDialog.OnDateSetListener() {
        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
            date_start = new Date(selectedYear-1900,selectedMonth,selectedDay);
            set_start = true;
            params.put("start", (selectedMonth+1)+" "+selectedYear);
            btn_start.setText("Start date : "+(selectedMonth+1)+"/"+selectedYear);
        }
    };
    private DatePickerDialog.OnDateSetListener datePickerListener_end = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
            date_end = new Date(selectedYear-1900,selectedMonth,selectedDay);
            set_end = true;
            params.put("end", (selectedMonth+1)+" "+selectedYear);
            btn_end.setText("End date : " + (selectedMonth+1) + "/" + selectedYear);
        }
    };
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_report_page, container, false);
        set_end = false;
        set_start = false;
        btn_start = (Button)rootView.findViewById(R.id.btn_start_date);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialogWithoutDateField(datePickerListener_start).show();
            }
        });
        btn_end = (Button)rootView.findViewById(R.id.btn_end_date);
        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialogWithoutDateField(datePickerListener_end).show();
            }
        });

        ((Button)rootView.findViewById(R.id.btn_get_report)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataPoint[] d = {};
                CPUseries.resetData(d);
                Memseries.resetData(d);
                Strseries.resetData(d);
                Netseries.resetData(d);
                if(set_start&&set_end) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(Calendar.getInstance().getTime());
                    calendar.add(Calendar.MONTH, 1);
                    if(date_end.before(calendar.getTime())) {
                        if (date_start.before(date_end)) {
                            progressDialog = ProgressDialog.show(getActivity(), "In progress", "Loading");
                            updateData(params);
                        } else
                            Toast.makeText(getActivity(), "The Start Date must come before The  End Date", Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(getActivity(), "The End Date must set at least this month", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getActivity(), "Please Select Start Date And End Date", Toast.LENGTH_LONG).show();
            }
        });
        v_cpu = (TextView)rootView.findViewById(R.id.report_text_cpu);
        v_mem = (TextView)rootView.findViewById(R.id.report_text_mem);
        v_str = (TextView)rootView.findViewById(R.id.report_text_storage);
        v_net = (TextView)rootView.findViewById(R.id.report_text_net);

        GraphView graph_cpu = (GraphView) rootView.findViewById(R.id.report_cpu);
        init(graph_cpu,CPUseries);

        GraphView graph_mem = (GraphView) rootView.findViewById(R.id.report_mem);
        init(graph_mem,Memseries);

        GraphView graph_storage = (GraphView) rootView.findViewById(R.id.report_storage);
        init(graph_storage, Strseries);

        GraphView graph_net = (GraphView) rootView.findViewById(R.id.report_net);
        init(graph_net, Netseries);

        return rootView;
    }

    private DatePickerDialog createDialogWithoutDateField(DatePickerDialog.OnDateSetListener d) {
        DatePickerDialog dpd = new DatePickerDialog(getContext(), d, 2015, 1, 1);
        try {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        Log.i("test", datePickerField.getName());
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
        catch (Exception ex) {

        }
        return dpd;
    }

    public void init(GraphView graph,BarGraphSeries<DataPoint> list){

        graph.addSeries(list);
/*        String[] s = new String[31];
        for (int i = 0; i < 31 ; i++){
            s[i] = ""+i+1;
        }*/
        //StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(31);
        //staticLabelsFormatter.setHorizontalLabels(s);
        //staticLabelsFormatter.setVerticalLabels(new String[]{"0%", "50%", "100%"});
        //graph.getGridLabelRenderer().setHorizontalAxisTitle("time");
        //graph.getGridLabelRenderer().setNumHorizontalLabels(3);
       /* StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[]{"60 seconds", "now"});
        staticLabelsFormatter.setVerticalLabels(new String[]{"0%", "50%","100%"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(100);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);
        graph.getViewport().setYAxisBoundsManual(true);*/
    }
    public void setGraph(JSONArray j,BarGraphSeries<DataPoint> b) throws JSONException {
        for(int i = 0; i< j.length();i++){
            double d = j.getDouble(i);
            if(d<0)
                d=0;
            b.setSpacing(1);
            b.appendData(new DataPoint(i+1,d),true,31);
        }
    }
    public void updateData(RequestParams params) {
        HTTPConnector.get("report/" + id + "/", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = "";
                for (int index = 0; index < responseBody.length; index++) {
                    response += (char) responseBody[index];
                }
                if (!response.isEmpty()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject json = jsonArray.getJSONObject(0);
                        JSONArray cpulist = json.getJSONArray("cpus");
                        setGraph(cpulist,CPUseries);
                        JSONArray memlist = json.getJSONArray("mems");
                        setGraph(memlist,Memseries);
                        JSONArray strlist = json.getJSONArray("storage");
                        setGraph(strlist,Strseries);
                        JSONArray netlist = json.getJSONArray("networks");
                        setGraph(netlist,Netseries);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("JSONObject","JSONObject Error");
                    }
                } else
                    Toast.makeText(getActivity(), "null response", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Error Code " + statusCode, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
