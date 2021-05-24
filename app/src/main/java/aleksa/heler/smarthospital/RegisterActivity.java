package aleksa.heler.smarthospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import aleksa.heler.smarthospital.classes.User;
import aleksa.heler.smarthospital.helpers.UserDBHelper;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextView printTV;
    EditText nameET;
    EditText surnameET;
    EditText passwordET;
    RadioGroup genderRG;
    RadioButton genderRB;
    CalendarView dateOfBirthCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        printTV = findViewById(R.id.print_label);
        nameET = findViewById(R.id.name_input);
        surnameET = findViewById(R.id.lastname_input);
        passwordET = findViewById(R.id.password_input);
        genderRG = findViewById(R.id.gender_select);
        dateOfBirthCV = findViewById(R.id.caledar_select);

        findViewById(R.id.submit_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.submit_button){
            // Get name, surname and password
            String name = nameET.getText().toString();
            String surname = surnameET.getText().toString();
            String password = passwordET.getText().toString();

            // If strings are empty dont do anything
            if(name.isEmpty() || surname.isEmpty() || password.isEmpty())
                return;

            // Print generated username
            String username = name + "." + surname;
            printTV.setText("Username is: " + username);
            printTV.setVisibility(View.VISIBLE);

            // Get gender from radio button select
            int selectedId=genderRG.getCheckedRadioButtonId();
            genderRB=(RadioButton)findViewById(selectedId);
            String gender = genderRB.getText().toString();

            // Get date of birth from calendar
            long dateOfBirthLong = dateOfBirthCV.getDate(); // Get selected date in milliseconds
            String dateOfBirth = "" + dateOfBirthLong;

            UserDBHelper userDBHelper = new UserDBHelper(getApplicationContext());

            /// TODO: Check if the given username already exists

            // Try to generate unique ID by checking if this one already exists
            String id = "";
            int i = 0;
            do{
                id = "" + new Random().nextInt(1000000);
                if(i == 100){
                    Toast.makeText(RegisterActivity.this, "ERROR: Could not generate unique ID for user", Toast.LENGTH_LONG).show();
                    return;
                }
                i++;
            } while(userDBHelper.readUser(id) != null);

            // Add new user to database
            userDBHelper.insert(new User(id, name, surname, gender, dateOfBirth, password));

            // Print success message
            Toast.makeText(RegisterActivity.this, "Successfully created new user with username: " + username, Toast.LENGTH_LONG).show();

            // Automatically log the user in
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class).putExtra("username", username));
        }
    }
}