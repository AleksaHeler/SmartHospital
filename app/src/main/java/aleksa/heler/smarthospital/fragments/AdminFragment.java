package aleksa.heler.smarthospital.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.Random;

import aleksa.heler.smarthospital.LoginActivity;
import aleksa.heler.smarthospital.MainActivity;
import aleksa.heler.smarthospital.NewDevice;
import aleksa.heler.smarthospital.helpers.DeviceDBHelper;
import aleksa.heler.smarthospital.listadapters.AdminListAdapter;
import aleksa.heler.smarthospital.R;
import aleksa.heler.smarthospital.classes.SmartDevice;
import aleksa.heler.smarthospital.helpers.HttpHelper;

//////////////////////////////////////////////////////////////////////////////////////////////////
///////// Dobavlja sve uredjaje sa servera i dodaj ih u lokalnu SQL bazu
///////// Dobavlja sve uredjaje iz lokalne SQL baze i dodaj ih u listu
//////////////////////////////////////////////////////////////////////////////////////////////////

public class AdminFragment extends Fragment {

    private HttpHelper httpHelper;
    private DeviceDBHelper deviceDBHelper;
    private AdminListAdapter adapter;

    private static final int SERVER_STATUS_OK = 200;
    private static final String SERVER_URL_POST = "http://192.168.0.100:8080/api/device/";
    private static final String SERVER_URL_GET = "http://192.168.0.100:8080/api/devices/";

    private int devicesOnServer = 0;

    // Required empty public constructor
    public AdminFragment() { }

    // Use this factory method to create a new instance of this fragment
    public static AdminFragment newInstance() {
        return new AdminFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpHelper = new HttpHelper();
        deviceDBHelper = new DeviceDBHelper(getContext());
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_admin, container, false);

        adapter = new AdminListAdapter(getContext());

        ListView listView = (ListView) v.findViewById(R.id.list);
        TextView textView = v.findViewById(R.id.emptyView);
        listView.setEmptyView(textView);
        listView.setAdapter(adapter);

        v.findViewById(R.id.new_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NewDevice.class));
            }
        });

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();

        //////////////////////////////////////////////////////////////////////////////////////////////////
        ///////// Dobavi sve uredjaje sa servera i dodaj ih u lokalnu SQL bazu
        //////////////////////////////////////////////////////////////////////////////////////////////////
        final Thread serverToDB = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Prvo ocisti lokalnu bazu
                    SmartDevice[] devices = deviceDBHelper.readDevices();
                    if(devices != null){
                        for(int i = 0; i < devices.length; i++)
                            deviceDBHelper.deleteDevice(devices[i].getId());
                    }

                    // Dobavi niz sa servera i prodji kroz njega
                    JSONArray jsonArray = httpHelper.getJSONArrayFromURL(SERVER_URL_GET);
                    if(jsonArray == null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("MYTESTING", "[Server -> DB] There are no available devices");
                                Toast.makeText(getContext(), "[Server -> DB] There are no available devices!", Toast.LENGTH_LONG).show();
                                return;
                            }
                        });
                    }
                    devicesOnServer = jsonArray.length();
                    //Log.d("MYTESTING", "[Server -> DB] Found " +  jsonArray.length() + " devices on server!");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        // Parsiranje podataka
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("id");
                        String name = object.getString("name");
                        String type = "actuator";
                        //String type = object.getString("type");
                        String state = object.getString("state");

                        // Dodaj u bazu podataka
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SmartDevice device = new SmartDevice(id, name, type, state, "" + R.drawable.flask);
                                if(deviceDBHelper.readDevice(id) == null){
                                    Log.d("MYTESTING", "[Server -> DB] Adding a device to local DB...");
                                    deviceDBHelper.insert(device);
                                }
                            }
                        });
                    }
                }
                catch (FileNotFoundException fnfe){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("MYTESTING", "[Server -> DB] File not found error: " + fnfe.toString());
                            Toast.makeText(getContext(), "[Server -> DB] File not found: " + fnfe.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                catch (Exception e) {
                    Log.d("MYTESTING", "[Server -> DB] Error getting data from server and adding to DB: " + e.toString());
                    e.printStackTrace();
                }
            }
        });
        serverToDB.start();

        //////////////////////////////////////////////////////////////////////////////////////////////////
        ///////// Dobavi sve uredjaje iz lokalne SQL baze i dodaj ih u listu
        //////////////////////////////////////////////////////////////////////////////////////////////////
        Thread dbToList = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Wait for this thread to finish
                    serverToDB.join();

                    // Dobavi niz iz baze podataka i prodji kroz njega
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SmartDevice[] devices = deviceDBHelper.readDevices();
                            if (devices == null) {
                                Log.d("MYTESTING", "[DB -> List] There are no available devices!");
                                Toast.makeText(getContext(), "[DB -> List] There are no available devices!", Toast.LENGTH_LONG).show();
                                return;
                            }

                            adapter.clear();
                            Log.d("MYTESTING", "[DB -> List] Cleared adapter!");

                            try {
                                // Ako se razlikuje broj uredjaja na serveru i u lokalnoj bazi, osvezi bazu
                                Log.d("MYTESTING", "[DB -> List] FOUND " + devicesOnServer + " DEVICES ON SERVER");
                                Log.d("MYTESTING", "[DB -> List] FOUND " + devices.length + " DEVICES IN DB");
                                if (devicesOnServer != devices.length) {
                                    Log.d("MYTESTING", "[DB -> List] ERROR - number of devices in database is different from server");
                                }

                                Log.d("MYTESTING", "[DB -> List] Found " + devices.length + " devices in DB!");
                                for (int i = 0; i < devices.length; i++) {
                                    // Add to list
                                    // For every device, add to list adapter
                                    Log.d("MYTESTING", "[DB -> List] Added another device to list adapter!");
                                    adapter.addElement(devices[i]);
                                }
                                if (devices.length == 0) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("MYTESTING", "[DB -> List] There are no available devices!");
                                            Toast.makeText(getContext(), "[DB -> List] There are no available devices!", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            } catch (Exception e){
                                Log.d("MYTESTING", "[DB -> List] Error: " + e.toString());
                            }
                        }
                    });
                }catch (Exception e) {
                    Log.d("MYTESTING", "[DB -> List] Error adding devices to list adapter: " + e.toString());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "[DB -> List] Error adding devices to list adapter: " + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        });
        dbToList.start();
    }
}