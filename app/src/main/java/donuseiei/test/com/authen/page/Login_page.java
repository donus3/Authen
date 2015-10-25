package donuseiei.test.com.authen.page;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import donuseiei.test.com.authen.HTTPConnector;
import donuseiei.test.com.authen.MainActivity;
import donuseiei.test.com.authen.R;


public class Login_page extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private  ProgressDialog prgDialog;
    private  Button submit;
    private  Button register;
    private  EditText edit_email;
    private  EditText edit_pass;
    private String username;
    // Get Password Edit View Value
    private String password;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static Login_page newInstance(String param1, String param2) {
        Login_page fragment = new Login_page();
        Bundle args = new Bundle();
        //args.putString(name_var1, param1);
        //args.putString(name_var2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Login_page() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(name_var1);
            //mParam2 = getArguments().getString(name_var2);
        }
        prgDialog = new ProgressDialog(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login_page, container, false);
        submit = (Button)v.findViewById(R.id.submit_login);
        register = (Button)v.findViewById(R.id.btn_register);
        edit_email = (EditText)v.findViewById(R.id.username_login);
        edit_pass = (EditText)v.findViewById(R.id.password_login);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new Registe_page()).commit();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });
        return v;
    }
    public void login(View view){
        // Get Email Edit View Value
        username = edit_email.getText().toString();
        // Get Password Edit View Value
        password = edit_pass.getText().toString();
        // Instantiate Http Request Param Object
        RequestParams params = new RequestParams();
        params.put("password", password);
        // Invoke RESTful Web Service with Http parameters
        if(isOnline())
            get(params);
        else
            Toast.makeText(getActivity(), "Please Connect the internet", Toast.LENGTH_LONG).show();
    }

    public void get(RequestParams params) {
        HTTPConnector.get("login/" + username + "/", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = "";
                for (int index = 0; index < responseBody.length; index++) {
                    response += (char) responseBody[index];
                }
                Log.i("res", response);
                try {
                    JSONObject json = new JSONObject(response);
                    if (!response.isEmpty()) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("id", json.getString("userId"));
                        intent.putExtra("password", json.getString("password"));
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "Email or Password may wrong", Toast.LENGTH_LONG).show();
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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}

