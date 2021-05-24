package aleksa.heler.smarthospital;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import aleksa.heler.smarthospital.helpers.HttpHelper;

public class DeviceActivity extends AppCompatActivity {

    private HttpHelper httpHelper;
    private String id;

    private static String SERVER_URL_GET_INFO = "http://192.168.0.17:8080/api/device/"; // Na ovo dodati ID uredjaja koji treba da se dobavi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        httpHelper = new HttpHelper();
        id = getIntent().getExtras().getString("id");

        TextView tvID = findViewById(R.id.device_info_id);
        TextView tvName = findViewById(R.id.device_info_name);
        TextView tvState = findViewById(R.id.device_info_state);
        TextView tvType = findViewById(R.id.device_info_type);

        //////////////////////////////////////////////////////////////////////////////////////////////////
        ///////// Prikazi informacije samo jednog izabranog uredjaja (preko id)
        //////////////////////////////////////////////////////////////////////////////////////////////////
        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    JSONObject jsonObject = httpHelper.getJSONObjectFromURL(SERVER_URL_GET_INFO + id);
                    String id ="ID: " +  jsonObject.getString("id");
                    String name ="Name: " +  jsonObject.getString("name");
                    String state ="State: " +  jsonObject.getString("state");
                    String type ="Type: " +  jsonObject.getString("type");
                    /// TODO: add type display

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try
                            {
                                tvID.setText(id);
                                tvName.setText(name);
                                tvState.setText(state);
                                tvType.setText(type);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                catch (JSONException | IOException e)
                {
                    e.printStackTrace();
                }
            }

        }).start();
    }
}