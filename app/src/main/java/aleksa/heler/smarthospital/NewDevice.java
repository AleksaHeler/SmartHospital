package aleksa.heler.smarthospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.Random;

import aleksa.heler.smarthospital.classes.SmartDevice;
import aleksa.heler.smarthospital.helpers.DeviceDBHelper;
import aleksa.heler.smarthospital.helpers.HttpHelper;

public class NewDevice extends AppCompatActivity {

    private EditText nameET;
    private EditText typeET;
    private Button submitButton;
    private Button deleteAllButton;

    private DeviceDBHelper deviceDBHelper;
    private HttpHelper httpHelper;

    private static String SERVER_URL_GET = "http://192.168.0.17:8080/api/devices/";
    private static String SERVER_URL_POST = "http://192.168.0.17:8080/api/device/";
    private static String SERVER_URL_DELETE = "http://192.168.0.17:8080/api/device/";
    private static int SERVER_STATUS_OK = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_device);

        deviceDBHelper = new DeviceDBHelper(NewDevice.this);
        httpHelper = new HttpHelper();

        nameET = findViewById(R.id.new_device_name);
        typeET = findViewById(R.id.new_device_type);
        submitButton = findViewById(R.id.new_device_submit);
        deleteAllButton = findViewById(R.id.delete_all_devices);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /// TODO: Create new device and add it to the database
                // After that (if successful) return to previous activity (like back arrow) and display toast
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Priprema podataka uredjaja
                            String name = nameET.getText().toString();
                            String type = typeET.getText().toString();

                            // Try to generate unique ID by checking if this one already exists
                            String id = "";
                            int i = 0;
                            do{
                                id = "" + new Random().nextInt(1000000);
                                if(i == 100){
                                    Toast.makeText(NewDevice.this, "ERROR: Could not generate unique ID for device", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                i++;
                            } while(deviceDBHelper.readDevice(id) != null);

                            // Kreiranje JSON-a koji cemo poslati
                            JSONObject jsonSend = new JSONObject();
                            jsonSend.put("id", id);
                            jsonSend.put("name", name);
                            jsonSend.put("type", type);
                            jsonSend.put("state", "on");

                            // Dodaj korisnika u lokalnu bazu
                            deviceDBHelper.insert(new SmartDevice(id, name, type, "on", ""+R.drawable.flask));

                            // Slanje json-a na server i ispis da li je uspelo
                            int response = httpHelper.postJSONObjectFromURL(SERVER_URL_POST, jsonSend);

                            String finalId = id;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (response == SERVER_STATUS_OK) {
                                        Toast.makeText(NewDevice.this, "Created new device: " + name + ", with ID " + finalId, Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        Toast.makeText(NewDevice.this, "Error creating new device: " + response, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            Toast.makeText(NewDevice.this, "Error creating new device: " + e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        // Get all devices from server and delete them
        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Dobavi niz sa servera i prodji kroz njega
                            JSONArray jsonArray = httpHelper.getJSONArrayFromURL(SERVER_URL_GET);
                            Log.d("MYTESTING", "[Delete all] Found " + jsonArray.length() + " devices on server!");

                            if(jsonArray.length() == 0){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(NewDevice.this, "There are no devices to delete!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }else{
                                int cnt = 0;
                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    // Parsiranje podataka
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("id");
                                    httpHelper.httpDelete(SERVER_URL_DELETE + id);
                                    deviceDBHelper.deleteDevice(id);
                                    cnt++;
                                }
                                Log.d("MYTESTING", "[Delete all] Deleted " + cnt + " devices!");
                                int finalCnt = cnt;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(NewDevice.this, "Deleted " + finalCnt + " devices!", Toast.LENGTH_LONG).show();
                                    }
                                });
                                finish();
                            }
                        }
                        catch (FileNotFoundException fnfe){
                            Toast.makeText(NewDevice.this, "No devices left to be deleted", Toast.LENGTH_LONG).show();
                        }
                        catch (Exception e){
                            Log.d("MYTESTING", "[Delete all] Error deleting all device: " + e.toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(NewDevice.this, "Error deleting all device: " + e.toString(), Toast.LENGTH_LONG).show();
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}