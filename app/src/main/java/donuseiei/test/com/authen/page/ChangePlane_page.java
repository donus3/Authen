package donuseiei.test.com.authen.page;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import donuseiei.test.com.authen.Adapter.ChangePlanViewAdapter;
import donuseiei.test.com.authen.Adapter.PlanViewAdapter;
import donuseiei.test.com.authen.HTTPConnector;
import donuseiei.test.com.authen.ListItemChangePlan;
import donuseiei.test.com.authen.ListItemPlan;
import donuseiei.test.com.authen.Plan;
import donuseiei.test.com.authen.R;

public class ChangePlane_page extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "password";
    // TODO: Rename and change types of parameters
    private String id;
    private String password;
    private String ip;
    private String cloundProv;
    private int indexPlan = 0;
    private ArrayList<Plan> listPlan;
    private ArrayList<Plan> listvm;
    private View view;
    private Spinner spinnerVM;
    private Spinner spinnerplan;
    private List<String> ips;
    private List<String> planName;
    private RequestParams paramsGetPlan;
    private Button btnChange;

    public ChangePlane_page() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ips = new ArrayList<>();
        planName = new ArrayList<>();
        listPlan = new ArrayList<>();
        listvm = new ArrayList<>();
        if (getArguments() != null) {
            id = getArguments().getString(ARG_PARAM1);
            password = getArguments().getString(ARG_PARAM2);
        }
        paramsGetPlan = new RequestParams();
        paramsGetPlan.put("password", password);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_change_plane_page, container, false);
        btnChange = (Button)view.findViewById(R.id.btn_change);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams paramsUpdate = new RequestParams();
                paramsUpdate.put("password",password);
                paramsUpdate.put("ip",ip);
                paramsUpdate.put("plan",indexPlan);
                paramsUpdate.put("cloudProv",cloundProv);
                updatePlan(paramsUpdate);
            }
        });
        getVM(paramsGetPlan);
        return view;
    }

    public void createView(){
        spinnerVM = (Spinner)view.findViewById(R.id.spinner_vp_change);
        if(!ips.isEmpty()){
            ips.removeAll(ips);
        }
        for(Plan item : listvm){
            ips.add(item.getIp());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,ips);
        spinnerVM.setAdapter(adapter);
        spinnerVM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getPlanByName(listvm.get(position).getProv(), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    public void CreatePlanAvailable(final int positionvm){
        spinnerplan = (Spinner)view.findViewById(R.id.spinner_vp_planAvailable);
        int index = 1;
        if(!planName.isEmpty()){
            planName.removeAll(planName);
        }
        for(Plan item:listPlan){
            planName.add(item.getProv()+index++);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,planName);
        spinnerplan.setAdapter(adapter);
        spinnerplan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                indexPlan = position;
                ListView lv = (ListView) view.findViewById(R.id.listChange);
                List<ListItemChangePlan> itemsVM = new ArrayList<>();
                Plan plan = listPlan.get(position);
                Plan vm = listvm.get(positionvm);
                ip = vm.getIp();
                cloundProv = vm.getProv();
                if (!itemsVM.isEmpty()) {
                    itemsVM.removeAll(itemsVM);
                } else {
                    //itemsVM.add(new ListItemChangePlan("Cloud Provider", plan.getProv(),vm.getProv()));
                    //itemsVM.add(new ListItemChangePlan("IP Address", plan.getIp(), vm.getIp()));
                    itemsVM.add(new ListItemChangePlan("CPU", plan.getCpu(), vm.getCpu()));
                    itemsVM.add(new ListItemChangePlan("Memory", plan.getMemory(), vm.getMemory()));
                    itemsVM.add(new ListItemChangePlan("Network", plan.getMemory(), vm.getMemory()));
                    itemsVM.add(new ListItemChangePlan("Storage", plan.getStorage(), vm.getStorage()));
                    itemsVM.add(new ListItemChangePlan("Mountly Rate", plan.getMounthlyrate(), vm.getMounthlyrate()));
                }
                System.out.println("item plan" + itemsVM.toString());
                ChangePlanViewAdapter adapter = new ChangePlanViewAdapter(getContext(), android.R.layout.simple_expandable_list_item_2, itemsVM);
                lv.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getPlanByName(String cloud,int position){
        RequestParams param = new RequestParams();
        if(cloud.equals("GOOGLE"))
            param.put("cloudProv",0);
        else if(cloud.equals("Amazon"))
            param.put("cloudProv",1);
        else if(cloud.equals("Azure"))
            param.put("cloudProv", 2);
        else if(cloud.equals("Digital Ocean"))
            param.put("cloudProv", 3);
        else if(cloud.equals("VMWare"))
            param.put("cloudProv", 4);
        else if(cloud.equals("Unknown"))
            param.put("cloudProv", 5);
        //get("plan/", param, listPlan); //make list plan
        System.out.println("prov : " + param.toString());
        getPlan(param, position);
    }

    /*send http to get plan that available*/
    public void getPlan(RequestParams params,final int position) {
        // Make RESTful webservice call using AsyncHttpClient object
        HTTPConnector.get("plan/", params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(getActivity(), "Error code : " + statusCode, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String response = "";
                for (int index = 0; index < bytes.length; index++) {
                    response += (char) bytes[index];
                }
                if (!listPlan.isEmpty()) {
                    listPlan.removeAll(listPlan);
                }
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() != 0) {
                        for (int index = 0; index < jsonArray.length(); index++) {
                            JSONObject json = new JSONObject(jsonArray.get(index).toString());
                            Plan p = new Plan(json.getString("cloudProv"),
                                    json.getString("ip"),
                                    json.getString("monthlyRate"),
                                    json.getString("cpu"),
                                    json.getString("mem"),
                                    json.getString("network"),
                                    json.getString("storage"));
                            listPlan.add(p);
                        }
                        CreatePlanAvailable(position);
                    } else {
                        Toast.makeText(getActivity(), "No Any Fucking Plan , Go to ur school", Toast.LENGTH_LONG);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*send http to get plan that available*/
    public void getVM(RequestParams params) {
        // Make RESTful webservice call using AsyncHttpClient object
        HTTPConnector.get("plan/" + id + "/", params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(getActivity(), "Error code : " + statusCode, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int j, Header[] headers, byte[] bytes) {
                String response = "";
                for (int index = 0; index < bytes.length; index++) {
                    response += (char) bytes[index];
                }
                if(!listvm.isEmpty()){
                    listvm.removeAll(listvm);
                }
                try {
                    JSONArray jsonarr = new JSONArray(response);
                    if (jsonarr.length() != 0) {
                        for (int index = 0; index < jsonarr.length(); index++) {
                            JSONObject json = new JSONObject(jsonarr.get(index).toString());
                            Plan c = new Plan(
                                    json.getString("cloudProv"),
                                    json.getString("ip"),
                                    json.getString("monthlyRate"),
                                    json.getString("cpu"),
                                    json.getString("mem"),
                                    json.getString("network"),
                                    json.getString("storage"));
                            listvm.add(c);
                        }
                        createView();
                    } else {
                        Toast.makeText(getActivity(), "No Any Fucking Plan , Go to ur school", Toast.LENGTH_LONG);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void updatePlan(RequestParams params){
        HTTPConnector.get("/update/plan/" + id + "/", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                getVM(paramsGetPlan);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "No Any Fucking Plan , Go to ur school", Toast.LENGTH_LONG);
            }
        });
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}
