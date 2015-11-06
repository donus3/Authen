package donuseiei.test.com.authen.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import donuseiei.test.com.authen.ListBill;
import donuseiei.test.com.authen.R;

/**
 * Created by Pai on 11/6/2015.
 */
public class BillAdapter extends ArrayAdapter<ListBill> {
    private LayoutInflater mInflater;

    public BillAdapter(Context context, int resource,List<ListBill> items) {
        super(context, resource);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setData(items);
    }
    public void setData(List<ListBill> data) {
        clear();
        if (data != null) {
            for (ListBill appEntry : data) {
                add(appEntry);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.bill_list, parent, false);
        } else {
            view = convertView;
        }
        ListBill item = getItem(position);
        ((TextView)view.findViewById(R.id.service)).setText(item.getService());
        ((TextView)view.findViewById(R.id.price)).setText(item.getPrice());
        return view;
    }
}
