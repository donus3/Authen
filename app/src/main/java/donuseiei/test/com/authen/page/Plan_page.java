package donuseiei.test.com.authen.page;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import donuseiei.test.com.authen.R;

public class Plan_page extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "password";
    // TODO: Rename and change types of parameters
    private String id;
    private String password;
    private FragmentTabHost mTabHost;

    // TODO: Rename and change types and number of parameters
    public static Plan_page newInstance(String param1, String param2) {
        Plan_page fragment = new Plan_page();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Plan_page() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_PARAM1);
            password = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        bundle.putString("password",password);
        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.tabHost_plan);
        mTabHost.addTab(mTabHost.newTabSpec("currentPlan").setIndicator("Current Plan"),
                View_plan.class, bundle);
        mTabHost.addTab(mTabHost.newTabSpec("changePlan").setIndicator("Change Plan"),
                ChangePlane_page.class, bundle);
        return mTabHost;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
