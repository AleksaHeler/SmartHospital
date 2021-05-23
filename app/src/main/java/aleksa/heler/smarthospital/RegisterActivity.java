package aleksa.heler.smarthospital;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextView ispisTV;
    EditText imeET;
    EditText prezimeET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ispisTV = findViewById(R.id.print_label);
        imeET = findViewById(R.id.name_input);
        prezimeET = findViewById(R.id.lastname_input);

        findViewById(R.id.submit_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.submit_button){
            String i = imeET.getText().toString();
            String p = prezimeET.getText().toString();
            if(i.isEmpty() || p.isEmpty())
                return;
            String s = "Username je:\n";
            s += i + "." + p;
            ispisTV.setText(s);
            ispisTV.setVisibility(View.VISIBLE);

            /* TODO:
            * Unutar ove tabele "user" treba da se čuvaju svi korisnici koji se registruju kroz ovu formu.
            * Voditi računa da se za svakog korisnika generiše jedinstveni ID pri pohranjivanju u tabelu baze kako bi se obezbedili jedinstveni unosi.
            * Pri prijavljivanju korisnika, obaviti proveru da li taj korisnik postoji u bazi podataka
            * Zatim obaviti prijavu samo ukoliko su podaci za prijavu validni
            * U suprotnom slučaju izbaciti poruku greške
            */
        }
    }
}