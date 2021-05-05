package aleksa.heler.smarthospital;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class AdminListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<AdminListModel> adminList;

    public AdminListAdapter(Context context) {
        this.context = context;
        adminList = new ArrayList<AdminListModel>();
    }

    public void addElement(AdminListModel element){
        adminList.add(element);
        notifyDataSetChanged();
    }

    public void removeElement(AdminListModel element){
        adminList.remove(element);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return adminList.size();
    }

    @Override
    public Object getItem(int position) {
        if(position < 0)
            return null;
        try {
            return adminList.get(position);
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
            convertView = inflater.inflate(R.layout.admin_list_row, null);
        }

        AdminListModel listItem = (AdminListModel) getItem(position);
        TextView textView = convertView.findViewById(R.id.listTxt);
        ImageView imageView = convertView.findViewById(R.id.listImg);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch sw = convertView.findViewById(R.id.listSw);
        textView.setText(listItem.getText());
        imageView.setImageDrawable(listItem.getImage());
        sw.setChecked(listItem.isActive());

        return convertView;
    }
}
