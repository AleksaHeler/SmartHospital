package aleksa.heler.smarthospital;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Appointment> userList;

    public UserListAdapter(Context context) {
        this.context = context;
        userList = new ArrayList<Appointment>();
    }

    public void addElement(Appointment element){
        userList.add(element);
        notifyDataSetChanged();
    }

    public void removeElement(Appointment element){
        userList.remove(element);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        if(position < 0)
            return null;
        try {
            return userList.get(position);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){ // Ako se prvi put kreira taj view (za recycling)
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.user_list_row, null);
        }

        Appointment listItem = (Appointment) getItem(position);
        TextView dateTextView = convertView.findViewById(R.id.dateTxt);
        TextView textView = convertView.findViewById(R.id.headingTxt);
        dateTextView.setText(listItem.getDate());
        textView.setText(listItem.getText());

        return convertView;
    }
}
