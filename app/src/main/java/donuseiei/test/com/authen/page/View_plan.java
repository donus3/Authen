package donuseiei.test.com.authen.page;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import donuseiei.test.com.authen.Adapter.PlanViewAdapter;
import donuseiei.test.com.authen.HTTPConnector;
import donuseiei.test.com.authen.ListItemPlan;
import donuseiei.test.com.authen.Plan;
import donuseiei.test.com.authen.R;

public class View_plan extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "password";

    // TODO: Rename and change types of parameters
    private String id;
    private String password;
    private View view_plan;
    private List<Plan> listVM;
    private RequestParams params;
    private Spinner dropdown;
    private List<String> ips;

    public View_plan() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listVM = new ArrayList<>();
        ips = new ArrayList<>();
        if (getArguments() != null) {
            id = getArguments().getString(ARG_PARAM1);
            password = getArguments().getString(ARG_PARAM2);
            params = new RequestParams();
            params.put("password", password);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view_plan =  inflater.inflate(R.layout.fragment_view_plan, container, false);
        getVM(params, "plan/" + id + "/");
        return view_plan;
    }

    /*send http to get plan that available*/
    public void getVM(RequestParams params,String url) {
        HTTPConnector.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = "";
                for (int index = 0; index < responseBody.length; index++) {
                    response += (char) responseBody[index];
                }
                try {
                    if(!listVM.isEmpty()) {
                        listVM.removeAll(listVM);
                    }
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
                            listVM.add(c);
                        }
                    }
                    CreateView();
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

    public void CreateView(){
        dropdown = (Spinner) view_plan.findViewById(R.id.spinner_vp);
        if(!ips.isEmpty()){
            ips.removeAll(ips);
        }
        for (Plan vm : listVM) {
            ips.add(vm.getIp());
        }
        ArrayAdapter<String> adapter_spinner = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, ips);
        dropdown.setAdapter(adapter_spinner);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ListView lv = (ListView)view_plan.findViewById(R.id.listPlanView);
                List<ListItemPlan> itemsVM = new ArrayList<>();
                Plan p = listVM.get(position);
                if(!itemsVM.isEmpty()){
                    itemsVM.removeAll(itemsVM);
                }
                else {
                    itemsVM.add(new ListItemPlan("Cloud Provider", p.getProv()));
                    //itemsVM.add(new ListItemPlan("IP Address", p.getIp()));
                    itemsVM.add(new ListItemPlan("CPU", p.getCpu()));
                    itemsVM.add(new ListItemPlan("Memory", p.getMemory()));
                    itemsVM.add(new ListItemPlan("Network", p.getMemory()));
                    itemsVM.add(new ListItemPlan("Storage", p.getStorage()));
                    itemsVM.add(new ListItemPlan("Mountly Rate", p.getMounthlyrate()));
                }
                PlanViewAdapter adapter = new PlanViewAdapter(getContext(),android.R.layout.simple_expandable_list_item_2,itemsVM);
                lv.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}