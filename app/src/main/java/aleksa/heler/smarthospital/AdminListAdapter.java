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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class AdminListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<SmartDevice> adminList;

    private static String SERVER_URL_POST_STATE = "http://192.168.0.17:8080/api/device/";

    public AdminListAdapter(Context context) {
        this.context = context;
        adminList = new ArrayList<SmartDevice>();
    }

    public void addElement(SmartDevice element){
        adminList.add(element);
        notifyDataSetChanged();
    }

    public void removeElement(SmartDevice element){
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

        SmartDevice listItem = (SmartDevice) getItem(position);
        TextView textView = convertView.findViewById(R.id.listTxt);
        TextView textViewOverlay = convertView.findViewById(R.id.listTxtOverlay);
        ImageView imageView = convertView.findViewById(R.id.listImg);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch sw = convertView.findViewById(R.id.listSw);
        textView.setText(listItem.getName());
        textViewOverlay.setText(R.string.neaktivan_label);
        imageView.setImageDrawable(listItem.getImage());
        if(listItem.isActive()){
            textViewOverlay.setVisibility(View.INVISIBLE);
        }else{
            textViewOverlay.setVisibility(View.VISIBLE);
        }
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            //////////////////////////////////////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////////////////////////////////////
            /////////// OVDE PROMENITI STANJE UREDJAJA NA SERVERU ////////////////////////////////////////////
            /////////////////////Promena stanja uređaja///////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////////////////////////////////////
            @Override
            public void onCheckedChanged(CompoundButton sw, boolean isChecked) {
                HttpHelper httpHelper = new HttpHelper();

                if(isChecked){
                    textViewOverlay.setVisibility(View.INVISIBLE);
                }else{
                    textViewOverlay.setVisibility(View.VISIBLE);
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SmartDevice listItem = (SmartDevice) getItem(position);
                            String state = isChecked ? "on" : "off";
                            JSONObject jsonObject = httpHelper.getJSONObjectFromPostURL(SERVER_URL_POST_STATE + listItem.getId() + "/" + state);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        sw.setChecked(listItem.isActive());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmartDevice listItem = (SmartDevice) getItem(position);
                context.startActivity(new Intent(context, DeviceActivity.class).putExtra("id", listItem.getId()));
            }
        });

        return convertView;
    }
}
