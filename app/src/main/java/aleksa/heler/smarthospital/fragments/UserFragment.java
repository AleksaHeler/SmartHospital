package aleksa.heler.smarthospital.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import aleksa.heler.smarthospital.LoginActivity;
import aleksa.heler.smarthospital.MainActivity;
import aleksa.heler.smarthospital.NewAppointment;
import aleksa.heler.smarthospital.R;
import aleksa.heler.smarthospital.listadapters.UserListAdapter;
import aleksa.heler.smarthospital.classes.Appointment;

/// TODO: treba ucitati preglede iz baze podataka

public class UserFragment extends Fragment {

    private String username;

    // Required empty public constructor
    public UserFragment() { }

    // Use this factory method to create a new instance of this fragment using the provided parameters
    public static UserFragment newInstance(String username) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString("username");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user, container, false);

        UserListAdapter adapter = new UserListAdapter(this.getContext());

        /* TODO:
        * Dodati elemente na osnovu podataka iz tabele "appointment"
        * Obratiti paznju da se prikazu samo oni pregledi koji imaju ID isti kao korisnik
        */
        adapter.addElement(new Appointment("0","12.05.2021", "Zubar"));
        adapter.addElement(new Appointment("0","24.05.2021", "Lekar opste prakse"));
        adapter.addElement(new Appointment("0","13.06.2021", "Terapija"));
        adapter.addElement(new Appointment("0","07.07.2021", "Previjanje"));

        ListView listView = v.findViewById(R.id.list);
        TextView textView = v.findViewById(R.id.emptyView);
        listView.setEmptyView(textView);
        listView.setAdapter(adapter);

        Button newAppointmentButton = v.findViewById(R.id.new_appointment);
        newAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NewAppointment.class));
            }
        });

        return v;
    }
}