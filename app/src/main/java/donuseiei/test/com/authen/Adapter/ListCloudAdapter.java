package donuseiei.test.com.authen.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import donuseiei.test.com.authen.ListCloud;
import donuseiei.test.com.authen.R;


public class ListCloudAdapter extends ArrayAdapter<ListCloud> {
    private LayoutInflater mInflater;
    private final int color1 = Color.rgb(17,102,28);
    private final int color2 = Color.rgb(159,2,159);
    private final int color3 = Color.rgb(237,99,16);
    private final int color4 = Color.rgb(168,0,12);
    private final int color5 = Color.rgb(247,185,24);
    private final int color6 = Color.rgb(14,29,102);

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
        TextView t;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.list_cloud, parent, false);
            t = (TextView)view.findViewById(R.id.btn_cloud);
        } else {
            view = convertView;
            t = (TextView)view.findViewById(R.id.btn_cloud);
        }
        ListCloud item = getItem(position);
        t.setText(item.getName());
        if(position%6 == 0){
            t.setBackgroundColor(color1);
        }
        else if(position%6 == 1){
            t.setBackgroundColor(color2);
        }
        else if(position%6 == 2){
            t.setBackgroundColor(color3);
        }
        else if(position%6 == 3){
            t.setBackgroundColor(color4);
        }
        else if(position%6 == 4){
            t.setBackgroundColor(color5);
        }
        else {
            t.setBackgroundColor(color6);
        }
        return view;
    }
}
