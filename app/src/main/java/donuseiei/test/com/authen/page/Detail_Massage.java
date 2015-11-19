package donuseiei.test.com.authen.page;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import donuseiei.test.com.authen.R;


public class Detail_Massage extends Fragment {

    private String title;
    private String detail;
    private String id;
    private String password;
    private String cloudProv;
    private String ip;
    public Detail_Massage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
            detail = getArguments().getString("detail");
            id = getArguments().getString("id");
            password = getArguments().getString("password");
            cloudProv = getArguments().getString("cloudProv");
            ip = getArguments().getString("ip");
            Log.i("Detail mail",title+" "+detail);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail__massage, container, false);
        ((TextView)v.findViewById(R.id.detail_title_msg)).setText(title);
        ((TextView)v.findViewById(R.id.detail_detail_msg)).setText(detail);
        ((Button)v.findViewById(R.id.btn_back_msg)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack ("mail", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack("mail", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack("mail", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
