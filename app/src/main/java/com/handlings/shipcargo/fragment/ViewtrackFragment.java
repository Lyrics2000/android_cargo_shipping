package com.handlings.shipcargo.fragment;

import static com.handlings.shipcargo.Contants.CARGOID;
import static com.handlings.shipcargo.Contants.TRACKINFO;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.handlings.shipcargo.Contants;
import com.handlings.shipcargo.R;
import com.handlings.shipcargo.SessionManager;
import com.handlings.shipcargo.adapter.HistoryRecyclerAdapter;
import com.handlings.shipcargo.adapter.TrackInfoAdapter;
import com.handlings.shipcargo.models.TrackModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewtrackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewtrackFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Bundle bundle;
    private RecyclerView trackRecycler;
    LinearLayoutManager  linearLayoutManager;
    TrackInfoAdapter trackInfoAdapter;
    private ProgressBar progressRecyclerTrack;


    SessionManager sessionManager;
    ProgressBar progressBarSubmit;

    public ViewtrackFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewtrackFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewtrackFragment newInstance(String param1, String param2) {
        ViewtrackFragment fragment = new ViewtrackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_viewtrack, container, false);
        progressRecyclerTrack =  root.findViewById(R.id.progressRecyclerTrack);
        trackRecycler =  root.findViewById(R.id.trackRecycler);
        sessionManager =  new SessionManager(getContext());

        bundle = getArguments();

        int cargo_id = bundle.getInt(CARGOID);

        setCargoId(cargo_id);
//        Toast.makeText(getContext(),String.valueOf("cargo id" + cargo_id),Toast.LENGTH_SHORT).show();


        return  root;
    }

    private void setCargoId(int cargo_id) {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("id", String.valueOf(cargo_id))
                .build();
      Request  request =  new Request.Builder()
                .header("Authorization","Token "+sessionManager.getUserDetails().get(Contants.TOKEN))
                .url(TRACKINFO)
                .post(formBody)
                .build();

        Call k = client.newCall(request);
        k.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"An error occured",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                String ju =  response.body().string();

                ArrayList<TrackModel> trackModels = new ArrayList<>();


                try {
                    JSONObject jsonObject =  new JSONObject(ju);
                    JSONArray jsonArray =  jsonObject.getJSONArray("total");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        int id = jsonObject1.getInt("id");
                        String description = jsonObject1.getString("description");
                        trackModels.add(new TrackModel(id,description));
                    }

                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressRecyclerTrack.setVisibility(View.GONE);
                            linearLayoutManager =  new LinearLayoutManager(getContext());
                            trackRecycler.setLayoutManager(linearLayoutManager);
                            trackInfoAdapter = new TrackInfoAdapter(trackModels,getContext());
                            trackRecycler.setAdapter(trackInfoAdapter);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                if(response.code() == 200){
//
//
//                }

            }
        });
    }
}