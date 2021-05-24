package aleksa.heler.smarthospital.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import aleksa.heler.smarthospital.R;
import aleksa.heler.smarthospital.listadapters.UserListAdapter;
import aleksa.heler.smarthospital.classes.Appointment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "username";

    private String username;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param username Username.
     * @return A new instance of fragment UserFragment.
     */
    public static UserFragment newInstance(String username) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user, container, false);

        //TextView tv = v.findViewById(R.id.hello_blank_fragment);
        //tv.setText(username);

        UserListAdapter adapter = new UserListAdapter(this.getContext());

        /* TODO:
        * Dodati elemente na osnovu podataka iz tabele "appointment"
        * Obratiti paznju da se prikazu samo oni pregledi koji imaju ID isti kao korisnik
        */
        adapter.addElement(new Appointment("12.05.2021", "pregled 1"));
        adapter.addElement(new Appointment("24.05.2021", "pregled 2"));
        adapter.addElement(new Appointment("13.06.2021", "pregled 3"));
        adapter.addElement(new Appointment("07.07.2021", "pregled 4"));

        ListView listView = v.findViewById(R.id.list);
        TextView textView = v.findViewById(R.id.emptyView);
        listView.setEmptyView(textView);
        listView.setAdapter(adapter);

        return v;
    }
}