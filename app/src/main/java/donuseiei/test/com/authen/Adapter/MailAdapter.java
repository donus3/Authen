package donuseiei.test.com.authen.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import donuseiei.test.com.authen.R;
import donuseiei.test.com.authen.page.Mail;


public class MailAdapter extends ArrayAdapter<Mail> {
    private final LayoutInflater mInflater;

    public MailAdapter(Context context, int resource, List<Mail> items) {
        super(context, resource);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setData(items);
    }

    public void setData(List<Mail> data) {
        clear();
        if (data != null) {
            for (Mail appEntry : data) {
                add(appEntry);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.message_layout, parent, false);
        } else {
            view = convertView;
        }
        Mail item = getItem(position);
        ((TextView)view.findViewById(R.id.Mail_title)).setText(item.getTitle());
        ((TextView)view.findViewById(R.id.Mail_detail)).setText(item.getMwssage());
        return view;
    }
}
