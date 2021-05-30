package aleksa.heler.smarthospital.listadapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import aleksa.heler.smarthospital.DeviceActivity;
import aleksa.heler.smarthospital.R;
import aleksa.heler.smarthospital.classes.SmartDevice;
import aleksa.heler.smarthospital.helpers.DeviceDBHelper;
import aleksa.heler.smarthospital.helpers.HttpHelper;

import static java.lang.Integer.parseInt;

//////////////////////////////////////////////////////////////////////////////////////////////////
///////// Promeni stanje uredjaja na serveru
//////////////////////////////////////////////////////////////////////////////////////////////////

public class AdminListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<SmartDevice> adminList;

    private static String SERVER_URL_POST_STATE = "http://192.168.0.100:8080/api/device/";

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

    public void clear(){
        adminList.clear();
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
        imageView.setImageDrawable(context.getDrawable(parseInt(listItem.getImage())));
        if(listItem.getActive().toUpperCase().equals("ON")){
            textViewOverlay.setVisibility(View.INVISIBLE);
        }else{
            textViewOverlay.setVisibility(View.VISIBLE);
        }
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            //////////////////////////////////////////////////////////////////////////////////////////////////
            ///////// Klikom na 'switch' uredjaja se menja njegovo stanje na serveru
            //////////////////////////////////////////////////////////////////////////////////////////////////
            @Override
            public void onCheckedChanged(CompoundButton sw, boolean isChecked) {
                HttpHelper httpHelper = new HttpHelper();
                DeviceDBHelper deviceDBHelper = new DeviceDBHelper(context);

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
                            listItem.setActive(state);
                            JSONObject jsonObject = httpHelper.getJSONObjectFromPostURL(SERVER_URL_POST_STATE + listItem.getId() + "/" + state);
                            deviceDBHelper.deleteDevice(listItem.getId());  // Delete and again add the device to change its data
                            deviceDBHelper.insert(listItem);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        sw.setChecked(listItem.getActive().toUpperCase().equals("ON"));

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
