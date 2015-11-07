package donuseiei.test.com.authen.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import donuseiei.test.com.authen.ListItemPlan;
import donuseiei.test.com.authen.R;

public class PlanViewAdapter extends ArrayAdapter<ListItemPlan> {

    private final LayoutInflater mInflater;

    public PlanViewAdapter(Context context, int resource, List<ListItemPlan> itemsVM) {
        super(context, resource);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setData(itemsVM);
    }

    public void setData(List<ListItemPlan> data) {
        clear();
        if (data != null) {
            for (ListItemPlan appEntry : data) {
                add(appEntry);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.planviewlist, parent, false);
        } else {
            view = convertView;
        }

        ListItemPlan item = getItem(position);
        ((TextView)view.findViewById(R.id.elementTitle)).setText(item.getTitle());
        ((TextView)view.findViewById(R.id.elementDetail)).setText(item.getDetail());
        ((ImageView)view.findViewById(R.id.image_view_plan)).setImageResource(item.getIcon());

        return view;
    }
}
