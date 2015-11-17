package donuseiei.test.com.authen.page;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
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
    private String cloudProv;
    private String ip;
    private View view_plan;
    private RequestParams params;

    public View_plan() {
        // Required empty public constructor
    }

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
            params.put("cloudProv",cloudProv);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view_plan =  inflater.inflate(R.layout.fragment_view_plan, container, false);
        getDetailPlan(params, "plan/" + id + "/");
        return view_plan;
    }

    /*send http to get plan that available*/
    public void getDetailPlan(RequestParams params,String url) {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "In progress", "Loading");
        HTTPConnector.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = "";
                for (int index = 0; index < responseBody.length; index++) {
                    response += (char) responseBody[index];
                }
                try {
                    if(!response.isEmpty()) {
                        JSONObject json = new JSONObject(response);
                        Plan plan = new Plan(
                                json.getString("cloudProv"),
                                null,//json.getString("ip"),
                                json.getString("monthlyRate"),
                                json.getString("cpu"),
                                json.getString("mem"),
                                json.getString("network"),
                                json.getString("storage"));
                        CreateView(plan);
                    }
                    else
                        Toast.makeText(getActivity(), "null response", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Error Code " + statusCode, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void CreateView(Plan p){
        ListView lv = (ListView)view_plan.findViewById(R.id.listPlanView);
        List<ListItemPlan> itemsVM = new ArrayList<>();
        if(!itemsVM.isEmpty()){
            itemsVM.removeAll(itemsVM);
        }
        else {
            itemsVM.add(new ListItemPlan("Cloud Provider", p.getProv(),R.drawable.cloud));
            //itemsVM.add(new ListItemPlan("IP Address", p.getIp()));
            itemsVM.add(new ListItemPlan("CPU", p.getCpu()+" GHz", R.drawable.cpu));
            itemsVM.add(new ListItemPlan("Memory", p.getMemory()+" GB", R.drawable.ram));
            itemsVM.add(new ListItemPlan("Network", p.getMemory()+" GB", R.drawable.network));
            itemsVM.add(new ListItemPlan("Storage", p.getStorage()+" GB", R.drawable.storage));
            itemsVM.add(new ListItemPlan("Mountly Rate", p.getMounthlyrate()+" USD", R.drawable.monthly_rate));
        }
        PlanViewAdapter adapter = new PlanViewAdapter(getContext(),android.R.layout.simple_expandable_list_item_2,itemsVM);
        lv.setAdapter(adapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}