package donuseiei.test.com.authen.page;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import donuseiei.test.com.authen.R;

public class Host_Report_page extends Fragment {
    private String id;
    private String password;
    private String name;
    private String ip;
    private FragmentTabHost mTabHost;

    public Host_Report_page() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString("id");
            password = getArguments().getString("password");
            name = getArguments().getString("info").split(" : ")[0];
            ip = getArguments().getString("info").split(" : ")[1];
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        bundle.putString("password",password);
        bundle.putString("cloudName",name);
        bundle.putString("ip",ip);

        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.tabHost_dash);
        mTabHost.addTab(mTabHost.newTabSpec("report").setIndicator("Report"),
                Report_page.class, bundle);
        mTabHost.addTab(mTabHost.newTabSpec("predict").setIndicator("Prediction"),
                Prediction_page.class, bundle);
        return mTabHost;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
