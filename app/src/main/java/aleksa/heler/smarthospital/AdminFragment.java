package aleksa.heler.smarthospital;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminFragment extends Fragment {

    public AdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminFragment newInstance() {
        AdminFragment fragment = new AdminFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_admin, container, false);

        AdminListAdapter adapter = new AdminListAdapter(this.getContext());

        adapter.addElement(new AdminListModel(getResources().getDrawable(R.drawable.blood_test), "description 1"));
        adapter.addElement(new AdminListModel(getResources().getDrawable(R.drawable.palette), "description 2"));
        adapter.addElement(new AdminListModel(getResources().getDrawable(R.drawable.flask), "description 3"));
        adapter.addElement(new AdminListModel(getResources().getDrawable(R.drawable.test_tube), "description 4"));

        ListView listView =  (ListView) v.findViewById(R.id.list);
        TextView textView = v.findViewById(R.id.emptyView);
        listView.setEmptyView(textView);
        listView.setAdapter(adapter);
        // TODO: dodati callback za switch na uredjaju (set active)
        // TODO: ne poziva se ovaj callback
        /*Log.d("DEVICE", "Adding setOnItemClickCallback");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("DEVICE", "Switching to another activity");
                startActivity(new Intent(getContext(), DeviceActivity.class));
            }
        });*/

        return v;
    }
}