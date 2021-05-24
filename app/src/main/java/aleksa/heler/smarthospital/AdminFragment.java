package aleksa.heler.smarthospital;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminFragment extends Fragment {

    private HttpHelper httpHelper;

    private static int SERVER_STATUS_OK = 200;
    private static String SERVER_URL_POST = "http://192.168.0.17:8080/api/device";
    private static String SERVER_URL_GET = "http://192.168.0.17:8080/api/devices";
    private static String SERVER_URL_DELETE = "http://192.168.0.17:8080/api/device/";

    // Required empty public constructor
    public AdminFragment() { }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AdminFragment.
     */
    public static AdminFragment newInstance() {
        AdminFragment fragment = new AdminFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpHelper = new HttpHelper();
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_admin, container, false);

        AdminListAdapter adapter = new AdminListAdapter(this.getContext());

        /// TODO: add new activity: "new device" to add new devices and put this code there
        //////////////////////////////////////////////////////////////////////////////////////////////////
        ///////// Generisi nove uredjaje kad se ucita ovaj fragment - TESTING ONLY
        //////////////////////////////////////////////////////////////////////////////////////////////////
        /*for(int i = 0; i < 6; i++) {
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Priprema podataka uredjaja
                        String name = "Uredjaj " + finalI;
                        String type = "Aktuator";
                        String id = "" + new Random().nextInt(100);

                        // Kreiranje JSON-a koji cemo poslati
                        JSONObject jsonSend = new JSONObject();
                        jsonSend.put("name", name);
                        jsonSend.put("id", id);
                        jsonSend.put("state", "on");

                        // Slanje json-a na server i ispis da li je uspelo
                        int response = httpHelper.postJSONObjectFromURL(SERVER_URL_POST, jsonSend);
                        /// TODO: Move this to a special button or something
                        //int response = 0;
                        //for(int i = 0; i < 1000; i++){
                        //    httpHelper.httpDelete(SERVER_URL_DELETE + i);
                        //}

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (response == SERVER_STATUS_OK) {
                                    Toast.makeText(getContext(), "POST Success", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(), "POST Error: " + response, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }*/

        //////////////////////////////////////////////////////////////////////////////////////////////////
        ///////// Dobavi sve uredjaje sa servera i dodaj ih u listu da se prikazu
        //////////////////////////////////////////////////////////////////////////////////////////////////
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Dobavi niz sa servera i prodji kroz njega
                    JSONArray jsonArray = httpHelper.getJSONArrayFromURL(SERVER_URL_GET);
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        // Parsiranje podataka
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("id");
                        String name = object.getString("name");
                        String state = object.getString("state");
                        boolean bool_state = state.toUpperCase().equals("ON");

                        // Dodaj u listu
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.addElement(new SmartDevice(id, name, bool_state, getResources().getDrawable(R.drawable.test_tube)));
                            }
                        });
                    }
                    if(jsonArray.length() == 0){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "There are no available devices!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        ListView listView =  (ListView) v.findViewById(R.id.list);
        TextView textView = v.findViewById(R.id.emptyView);
        listView.setEmptyView(textView);
        listView.setAdapter(adapter);

        return v;
    }
}