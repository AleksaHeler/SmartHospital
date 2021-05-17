package aleksa.heler.smarthospital;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
        TextView textViewOverlay = convertView.findViewById(R.id.listTxtOverlay);
        ImageView imageView = convertView.findViewById(R.id.listImg);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch sw = convertView.findViewById(R.id.listSw);
        textView.setText(listItem.getText());
        textViewOverlay.setText(R.string.neaktivan_label);
        imageView.setImageDrawable(listItem.getImage());
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton sw, boolean isChecked) {
                if(isChecked){
                    textViewOverlay.setVisibility(View.INVISIBLE);
                }else{
                    textViewOverlay.setVisibility(View.VISIBLE);
                }
            }
        });
        sw.setChecked(listItem.isActive());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEVICE", "Switching to another activity");
                context.startActivity(new Intent(context, DeviceActivity.class).putExtra("itemPosition", position));
            }
        });

        return convertView;
    }
}
