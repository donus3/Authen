package donuseiei.test.com.authen.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import donuseiei.test.com.authen.ListCloud;
import donuseiei.test.com.authen.R;


public class ListCloudAdapter extends ArrayAdapter<ListCloud> {
    private LayoutInflater mInflater;

    public ListCloudAdapter(Context context, int resource,List<ListCloud> items) {
        super(context, resource);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setData(items);
    }
    public void setData(List<ListCloud> data) {
        clear();
        if (data != null) {
            for (ListCloud appEntry : data) {
                add(appEntry);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.list_cloud, parent, false);
        } else {
            view = convertView;
        }
        ListCloud item = getItem(position);
        ((TextView)view.findViewById(R.id.btn_cloud)).setText(item.getName());
        return view;
    }
}
