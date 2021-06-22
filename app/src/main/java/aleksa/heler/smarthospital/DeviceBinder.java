package aleksa.heler.smarthospital;

import android.app.Application;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import aleksa.heler.smarthospital.helpers.DeviceDBHelper;
import aleksa.heler.smarthospital.helpers.HttpHelper;

public class DeviceBinder extends aleksa.heler.smarthospital.fragments.DeviceBinder.Stub {
    HttpHelper httpHelper;
    Context currentContext;
    String GET_URL = "http://192.168.0.100:8080" + "/api/devices/";
    private GetCaller caller;

    @Override
    public void getDevices() throws RemoteException {
        caller = new GetCaller();
        caller.start();
    }

    public void stop() {
        caller.stop();
    }

    private class GetCaller implements Runnable {

        private Handler handler = null;
        private static final long PERIOD = 10000L; // TODO: make this 60 seconds instead of 10
        private boolean run = true;

        public void start(){
            // Stefan je odradio upis u bazu ovde, pitati njega kako je uspeo
            handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(this, PERIOD);
        }

        public void stop(){
            run = false;
            handler.removeCallbacks(this);
        }

        @Override
        public void run() {

            if(!run) return;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        httpHelper = new HttpHelper();
                        JSONArray arr = httpHelper.getJSONArrayFromURL(GET_URL);
                        if(arr != null) {
                            Log.d("DEVICEBINDER", "Service found " + arr.length() + " devices:");
                            for (int i = 0; i < arr.length(); i++) {

                                JSONObject object = arr.getJSONObject(i);
                                String name = object.getString("name");
                                String id = object.getString("id");
                                String state = object.getString("state");

                                Log.d("DEVICEBINDER", "    Device: "+ name + ", ID: " + id + ", State: " + state);
                                /// TODO: Ovde ide logika dodavanja novih uredjaja kad se pojave
                                /// TODO: PITANJE: Kako ovde dodati DBHelper???
                                /// Tj kada u ovom nizu postoji uredjaj koji se ne nalazi u lokalnoj bazi
                                /// Takodje treba poslati notification da se dodao novi uredjaj
                                //if(deviceDBHelper.readDevice(id) == null){
                                //    Log.d("DEVICEBINDER", "[Server -> DB] Adding a device to local DB...");
                                //    deviceDBHelper.insert(device);
                                //}
                            }
                        }
                    }
                    catch (JSONException e)
                    {
                        Log.d("DEVICEBINDER", "Device list is empty");

//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(), "Lista uređaja je prazna!", Toast.LENGTH_LONG).show();
//                    }
//                });
                        e.printStackTrace();
                    } catch (Exception e) {
                        Log.d("DEVICEBINDER", "[Error in DeviceBinder.java -> run(), binder thread] {" + e.toString() + "}");
                        e.printStackTrace();
                    }
                }
            }).start();

            handler.postDelayed(this, PERIOD);
        }
    }
}
