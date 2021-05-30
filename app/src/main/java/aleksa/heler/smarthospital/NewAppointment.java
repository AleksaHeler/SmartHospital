package aleksa.heler.smarthospital;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

//////////////////////////////////////////////////////////////////////////////////////////////////
///////// Dodaje novi pregled na server i lokalnu bazu
//////////////////////////////////////////////////////////////////////////////////////////////////

public class NewAppointment extends AppCompatActivity {

    private EditText usernameET;
    private EditText descriptionET;
    private CalendarView dateCV;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);

        usernameET = findViewById(R.id.new_appointment_username);
        descriptionET = findViewById(R.id.new_appointment_description);
        dateCV = findViewById(R.id.new_appointment_date);
        submitButton = findViewById(R.id.new_appointment_submit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /// TODO: Create new appointment and add it to the database (get id from username)
                // After that (if successful) return to previous activity (like back arrow) and display toast
            }
        });
    }
}