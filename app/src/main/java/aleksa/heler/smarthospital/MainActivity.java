package aleksa.heler.smarthospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/// TODO: add notifications when new devices are discovered in DeviceBinder.java

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    EditText usernameET;
    EditText passwordET;
    Button login_submit;

    private aleksa.heler.smarthospital.fragments.DeviceBinder mService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // References to input fields
        usernameET = findViewById(R.id.login_username_input);
        passwordET = findViewById(R.id.login_password_input);
        login_submit = findViewById(R.id.login_submit_button);

        // Setting callback listener
        findViewById(R.id.register_button).setOnClickListener(this);
        findViewById(R.id.login_button).setOnClickListener(this);
        login_submit.setOnClickListener(this);

        // Create bind service for getting device list
        if(!bindService(new Intent(MainActivity.this, DeviceService.class), this, Context.BIND_AUTO_CREATE))
            Toast.makeText(MainActivity.this, "Bind failed", Toast.LENGTH_LONG).show();
    }

    // Handling click events for all buttons
    @Override
    public void onClick(View v) {
        // Open activity for creating new user
        if(v.getId() == R.id.register_button){
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        }
        // Show input fields for logging in
        else if(v.getId() == R.id.login_button){
            usernameET.setVisibility(View.VISIBLE);
            passwordET.setVisibility(View.VISIBLE);
            login_submit.setVisibility(View.VISIBLE);
        }
        // Open login activity with given username
        else if(v.getId() == R.id.login_submit_button){
            String username = usernameET.getText().toString();
            startActivity(new Intent(MainActivity.this, LoginActivity.class).putExtra("username", username));
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Toast.makeText(MainActivity.this, "Service connected", Toast.LENGTH_LONG).show();
        mService  = DeviceBinder.Stub.asInterface(service);
        try{
            mService.getDevices();
        } catch (RemoteException e){
            Log.d("DEVICEBINDER", "Error in function onServiceConnected in MainActivity.java");
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d("DEVICEBINDER", "Service  by error!");
        mService = null;
    }

}