package donuseiei.test.com.authen.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import donuseiei.test.com.authen.ListItemChangePlan;
import donuseiei.test.com.authen.R;

public class ChangePlanViewAdapter extends ArrayAdapter<ListItemChangePlan>{
    private final LayoutInflater mInflater;

    public ChangePlanViewAdapter(Context context, int resource, List<ListItemChangePlan> itemsVM) {
        super(context, resource);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setData(itemsVM);
    }

    public void setData(List<ListItemChangePlan> data) {
        clear();
        if (data != null) {
            for (ListItemChangePlan appEntry : data) {
                add(appEntry);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.changeplanviewlist, parent, false);
        } else {
            view = convertView;
        }
        ListItemChangePlan item = getItem(position);
        ((TextView)view.findViewById(R.id.changeElementTitle)).setText(item.getTitle());
        ((TextView)view.findViewById(R.id.changeElementDetailNew)).setText(item.getDetailNew());
        ((TextView)view.findViewById(R.id.changeElementDetailOld)).setText(item.getDetailOld());
        ((ImageView)view.findViewById(R.id.image_view_changeplan)).setImageResource(item.getIcon());
        return view;
    }
}
