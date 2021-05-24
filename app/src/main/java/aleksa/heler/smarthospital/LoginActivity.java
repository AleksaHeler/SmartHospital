package aleksa.heler.smarthospital;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import aleksa.heler.smarthospital.fragments.AdminFragment;
import aleksa.heler.smarthospital.fragments.UserFragment;

public class LoginActivity extends AppCompatActivity {

    private UserFragment userFragment;
    private AdminFragment adminFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String username = getIntent().getExtras().getString("username");

        if(username.equals("admin")) {
            adminFragment = AdminFragment.newInstance();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_placeholder, adminFragment, "adminFragment")
                    .commit();
        }
        else{
            userFragment = UserFragment.newInstance(username);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_placeholder, userFragment, "userFragment")
                    .commit();
        }
    }
}