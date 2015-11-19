package donuseiei.test.com.authen.page;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import donuseiei.test.com.authen.Adapter.MailAdapter;
import donuseiei.test.com.authen.HTTPConnector;
import donuseiei.test.com.authen.R;

public class Mail_page extends Fragment {

    // TODO: Rename and change types of parameters
    private String id;
    private String password;
    private String cloudProv;
    private String ip;
    private ProgressDialog progressDialog;
    private List<Mail> list_mail;
    private RequestParams params;
    private View rootView;
    public Mail_page() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString("id");
            password = getArguments().getString("password");
            cloudProv = getArguments().getString("info").split(" : ")[0];
            ip = getArguments().getString("info").split(" : ")[1];
            list_mail = new ArrayList<>();
            params = new RequestParams();
            params.put("vmIP",ip);
            params.put("password",password);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_mail_page, container, false);
        progressDialog = ProgressDialog.show(getActivity(), "In progress", "Loading");
        updateMessage(params);
        return rootView;
    }
    public void createView(){
        final ListView listView = (ListView)rootView.findViewById(R.id.list_mail);
        MailAdapter adapter = new MailAdapter(getContext(),android.R.layout.simple_expandable_list_item_2,list_mail);
        listView.setAdapter(adapter);
/*        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id1) {
                Detail_Massage ldf = new Detail_Massage ();
                Bundle bundle = new Bundle();
                bundle.putString("id",id);
                bundle.putString("password",password);
                bundle.putString("cloudProv",cloudProv);
                bundle.putString("ip",ip);
                bundle.putString("title",list_mail.get(position).getTitle());
                bundle.putString("detail", list_mail.get(position).getMwssage());
                ldf.setArguments(bundle);
                FragmentTransaction f = getFragmentManager().beginTransaction();
                f.replace(R.id.tab_main, ldf);
                f.addToBackStack("mail");
                f.commit();
            }
        });*/
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void updateMessage(RequestParams params) {
        HTTPConnector.get("message/", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = "";
                for (int index = 0; index < responseBody.length; index++) {
                    response += (char) responseBody[index];
                }
                try {
                    if(!list_mail.isEmpty()){
                        list_mail.removeAll(list_mail);
                    }
                    if (!response.isEmpty()) {
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i = 0; i < jsonArray.length();i++){
                            Mail mail = new Mail(jsonArray.getJSONObject(i).getString("topic"),
                                    jsonArray.getJSONObject(i).getString("detail"));
                            list_mail.add(mail);
                        }
                        progressDialog.dismiss();
                    } else
                        Toast.makeText(getActivity(), "null response", Toast.LENGTH_LONG).show();
                    createView();
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
