package donuseiei.test.com.authen.page;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
import com.jjoe64.graphview.series.LineGraphSeries;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import donuseiei.test.com.authen.HTTPConnector;
import donuseiei.test.com.authen.R;
import donuseiei.test.com.authen.TimeDate;

public class Report_page extends DialogFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "password";

    // TODO: Rename and change types of parameters
    private String id;
    private String password;
    private String cloudProv;
    private String ip;
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
    public Report_page() {
        // Required empty public constructor
    }
    private DatePickerDialog.OnDateSetListener datePickerListener_start = new DatePickerDialog.OnDateSetListener() {
        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
            Log.i("set date1", selectedDay + " " + selectedMonth + " " + selectedYear);
            params.put("start", selectedMonth + " " + selectedYear);
            TextView t_start = (TextView)rootView.findViewById(R.id.text_start_date);
            t_start.setText("Start date : "+selectedMonth+"/"+selectedYear);
        }
    };
    private DatePickerDialog.OnDateSetListener datePickerListener_end = new DatePickerDialog.OnDateSetListener() {
        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
            Log.i("set date2", selectedDay + " " + selectedMonth + " " + selectedYear);
            params.put("start", selectedDay + " " + selectedYear);
            TextView t_end = (TextView)rootView.findViewById(R.id.text_end_date);
            t_end.setText("End date : "+selectedMonth + "/" + selectedYear);
            updateData(params);
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_PARAM1);
            password = getArguments().getString(ARG_PARAM2);
            cloudProv = getArguments().getString("cloudName");
            ip = getArguments().getString("ip");
            params = new RequestParams();
            params.put("password", password);
            params.put("vmIP",ip);
        }
        createDialogWithoutDateField(datePickerListener_start).show();
        createDialogWithoutDateField(datePickerListener_end).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_report_page, container, false);

       // progressDialog = ProgressDialog.show(getActivity(), "In progress", "Loading");

        v_cpu = (TextView)rootView.findViewById(R.id.report_text_cpu);
        v_mem = (TextView)rootView.findViewById(R.id.report_text_mem);
        v_str = (TextView)rootView.findViewById(R.id.report_text_storage);
        v_net = (TextView)rootView.findViewById(R.id.report_text_net);

        GraphView graph_cpu = (GraphView) rootView.findViewById(R.id.report_cpu);
        init(graph_cpu,CPUseries);

        GraphView graph_mem = (GraphView) rootView.findViewById(R.id.report_mem);
        init(graph_mem,Memseries);

        GraphView graph_storage = (GraphView) rootView.findViewById(R.id.report_storage);
        init(graph_storage,Strseries);

        GraphView graph_net = (GraphView) rootView.findViewById(R.id.report_net);
        init(graph_net, Netseries);

        Button btn_start = (Button)rootView.findViewById(R.id.btn_start_date);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialogWithoutDateField(datePickerListener_start).show();
            }
        });
        Button btn_end = (Button)rootView.findViewById(R.id.btn_end_date);
        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialogWithoutDateField(datePickerListener_end).show();
            }
        });
        //getData(params);

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
    public void addDataPoint(Double d) {

    }

    public void updateData(RequestParams params) {
        HTTPConnector.get("/dashboard/" + id + "/", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = "";
                for (int index = 0; index < responseBody.length; index++) {
                    response += (char) responseBody[index];
                }
                try {
                    if (!response.isEmpty()) {
                        JSONObject json = new JSONObject(response);
                        progressDialog.dismiss();
                    } else
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

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
