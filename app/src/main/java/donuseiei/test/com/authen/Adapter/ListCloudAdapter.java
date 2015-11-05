package donuseiei.test.com.authen.Adapter;

import android.content.Context;
<<<<<<< HEAD
import android.graphics.Color;
=======
>>>>>>> ac89bd87c6d8c7c42aefaaa46abb4bb9c6b07e20
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
<<<<<<< HEAD
    private final int color1 = Color.rgb(0,128,255);
    private final int color2 = Color.rgb(0,204,104);
    private final int color3 = Color.rgb(240,139,37);
    private final int color4 = Color.rgb(255,51,51);
=======
>>>>>>> ac89bd87c6d8c7c42aefaaa46abb4bb9c6b07e20

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
<<<<<<< HEAD
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
        if(position%4 == 0){
            t.setBackgroundColor(color1);
        }
        else if(position%4 == 1){
            t.setBackgroundColor(color2);
        }
        else if(position%4 == 2){
            t.setBackgroundColor(color3);
        }
        else {
            t.setBackgroundColor(color4);
        }
        //((TextView)view.findViewById(R.id.btn_cloud)).setText(item.getName());
=======
        if (convertView == null) {
            view = mInflater.inflate(R.layout.list_cloud, parent, false);
        } else {
            view = convertView;
        }
        ListCloud item = getItem(position);
        ((TextView)view.findViewById(R.id.btn_cloud)).setText(item.getName());
>>>>>>> ac89bd87c6d8c7c42aefaaa46abb4bb9c6b07e20
        return view;
    }
}
