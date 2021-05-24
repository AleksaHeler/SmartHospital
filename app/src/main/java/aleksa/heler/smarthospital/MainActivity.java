package aleksa.heler.smarthospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText usernameET;
    EditText passwordET;
    Button login_submit;

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
}