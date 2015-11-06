package donuseiei.test.com.authen.page;

import android.app.Activity;
import android.net.Uri;
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

import donuseiei.test.com.authen.Adapter.BillAdapter;
import donuseiei.test.com.authen.Bill;
import donuseiei.test.com.authen.HTTPConnector;
import donuseiei.test.com.authen.ListBill;
import donuseiei.test.com.authen.R;

public class Bill_page extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String id;
    private String password;
    private String cloudProv;
    private String ip;
    private View bill_page;
    private RequestParams params;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Bill_page.
     */
    // TODO: Rename and change types and number of parameters
    public static Bill_page newInstance(String param1, String param2) {
        Bill_page fragment = new Bill_page();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Bill_page() {
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
        bill_page =  inflater.inflate(R.layout.fragment_bill_page, container, false);
       /* getBillPage(params, "bill/" + id + "/");*/
        ////////////////////TEST//////////////////////
        Bill bill = new Bill("Pai","July","4500","200","4700");
        CreateView(bill);
        return bill_page;
    }

    public void getBillPage(RequestParams params,String url){
        HTTPConnector.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = "";
                for (int index = 0; index < responseBody.length; index++) {
                    response += (char) responseBody[index];
                }
                try {
                    JSONObject json = new JSONObject(response);
                    Bill bill = new Bill(
                            json.getString("name"),
                            json.getString("month"),
                            json.getString("sum"),
                            json.getString("arrears"),
                            json.getString("total"));
                    CreateView(bill);
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

    public void CreateView(Bill b){
        ListView lv = (ListView)bill_page.findViewById(R.id.listBill);
        List<ListBill> itemsBill = new ArrayList<>();
        if(!itemsBill.isEmpty()){
            itemsBill.removeAll(itemsBill);
        }
        else {
            itemsBill.add(new ListBill("GG","500"));
            itemsBill.add(new ListBill("GL","1500"));
            itemsBill.add(new ListBill("HF","2500"));
        }
        BillAdapter adapter = new BillAdapter(getContext(),android.R.layout.simple_expandable_list_item_2,itemsBill);
        lv.setAdapter(adapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
