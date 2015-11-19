package donuseiei.test.com.authen.page;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import donuseiei.test.com.authen.Adapter.ChangePlanViewAdapter;
import donuseiei.test.com.authen.HTTPConnector;
import donuseiei.test.com.authen.ListItemChangePlan;
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
    private Spinner spinnerplan;
    private List<String> ips;
    private List<String> planName;
    private Button btnChange;
    private ProgressDialog progressDialog;
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
            cloundProv = getArguments().getString("cloudName");
            ip = getArguments().getString("ip");
        }
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
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you need to Change Your Plan?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RequestParams paramsUpdate = new RequestParams();
                        paramsUpdate.put("password", password);
                        paramsUpdate.put("ip", ip);
                        paramsUpdate.put("plan", indexPlan+1);
                        Log.i("indexplan",""+(indexPlan+1));
                        paramsUpdate.put("cloudProv", cloundProv);
                        updatePlan(paramsUpdate);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        RequestParams param = new RequestParams();
        param.put("cloudProv",cloundProv);
        getPlan(param);
        return view;
    }

    public void CreatePlanAvailable(final Plan plan){
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
                Plan plan_list = listPlan.get(position);
                if (!itemsVM.isEmpty()) {
                    itemsVM.removeAll(itemsVM);
                } else {
                    itemsVM.add(new ListItemChangePlan("CPU", plan_list.getCpu()+" GHz",
                            "(current: "+plan.getCpu()+" GHz)", R.drawable.cpu));
                    itemsVM.add(new ListItemChangePlan("Memory", plan_list.getMemory()+" GB",
                            "(current: "+plan.getMemory()+" GB)", R.drawable.ram));
                    itemsVM.add(new ListItemChangePlan("Network", plan_list.getMemory()+" GB",
                            "(current: "+plan.getMemory()+" GB)", R.drawable.storage));
                    itemsVM.add(new ListItemChangePlan("Storage", plan_list.getStorage()+" GB",
                            "(current: "+plan.getStorage()+" GB)", R.drawable.network));
                    itemsVM.add(new ListItemChangePlan("Mountly Rate", plan_list.getMounthlyrate()+" USD",
                            "(current: "+plan.getMounthlyrate()+" USD)", R.drawable.monthly_rate));
                }
                System.out.println("item plan" + itemsVM.toString());
                ChangePlanViewAdapter adapter = new ChangePlanViewAdapter(getContext(),
                        android.R.layout.simple_expandable_list_item_2, itemsVM);
                lv.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        progressDialog.dismiss();
    }

    /*send http to get plan that available*/
    public void getPlan(RequestParams params) {
        progressDialog = ProgressDialog.show(getActivity(), "in progress", "loading");
        progressDialog.setCanceledOnTouchOutside(true);
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
                    if(!response.isEmpty()) {
                        Log.i("All plan",response);
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() != 0) {
                            for (int index = 0; index < jsonArray.length(); index++) {
                                JSONObject json = new JSONObject(jsonArray.get(index).toString());
                                Plan p = new Plan(json.getString("cloudProv"),
                                        null,//json.getString("ip"),
                                        json.getString("monthlyRate"),
                                        json.getString("cpu"),
                                        json.getString("mem"),
                                        json.getString("network"),
                                        json.getString("storage"));
                                listPlan.add(p);
                            }
                            RequestParams param = new RequestParams();
                            param.put("cloudProv", cloundProv);
                            param.put("password", password);
                            param.put("vmIP", ip);
                            getVM(param);
                        } else {
                            Toast.makeText(getActivity(), "No Any Fucking Plan , Go to ur school", Toast.LENGTH_LONG);
                        }
                    }
                    else
                        Toast.makeText(getActivity(), "null response", Toast.LENGTH_LONG).show();

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
            public void onSuccess(int j, Header[] headers, byte[] bytes) {
                String response = "";
                for (int index = 0; index < bytes.length; index++) {
                    response += (char) bytes[index];
                }
                try {
                    if(!response.isEmpty()) {
/*                        Log.i("All VM",response);
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() != 0) {
                            for (int index = 0; index < jsonArray.length(); index++) {
                                JSONObject json = new JSONObject(jsonArray.get(index).toString());
                                Plan p = new Plan(json.getString("cloudProv"),
                                        null,//json.getString("ip"),
                                        json.getString("monthlyRate"),
                                        json.getString("cpu"),
                                        json.getString("mem"),
                                        json.getString("network"),
                                        json.getString("storage"));
                                CreatePlanAvailable(p);
                            }
                        } else {
                            Toast.makeText(getActivity(), "No Any Fucking Plan , Go to ur school", Toast.LENGTH_LONG);
                        }*/
                        JSONObject json = new JSONObject(response);
                        Plan p = new Plan(json.getString("cloudProv"),
                                null,//json.getString("ip"),
                                json.getString("monthlyRate"),
                                json.getString("cpu"),
                                json.getString("mem"),
                                json.getString("network"),
                                json.getString("storage"));
                        CreatePlanAvailable(p);
                    }
                    else
                        Toast.makeText(getActivity(), "null response", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(getActivity(), "Error code : " + statusCode, Toast.LENGTH_LONG).show();
            }
        });
    }
    public void updatePlan(RequestParams params){
        HTTPConnector.get("update/plan/" + id + "/", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = "";
                for (int index = 0; index < responseBody.length; index++) {
                    response += (char) responseBody[index];
                }
                if(response.equals("true")) {
                    Toast.makeText(getActivity(), "Change plan was success!", Toast.LENGTH_LONG).show();
                    Log.i("update plan", response);
                    RequestParams param = new RequestParams();
                    param.put("cloudProv", cloundProv);
                    getPlan(param);
                }
                else{
                    Toast.makeText(getActivity(), "Do it again!", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "Error code " + statusCode, Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}
