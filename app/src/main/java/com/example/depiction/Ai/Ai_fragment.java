package com.example.depiction.Ai;
import static com.android.volley.VolleyLog.TAG;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.depiction.R;
import com.example.depiction.WallpaperAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ai_fragment extends Fragment {

    EditText imageSearch;
    Button genrateImagesBtn;
    RecyclerView rvImageAi;
    private WallpaperAdapter imageAdapter;
    private  ArrayList<String> imageUrls;

    String base_url ="https://api.openai.com/v1/images/generations";
    String API_KEY ="sk-proj-q6HYdVsGhXJw6QTsmAQ1T3BlbkFJuQ0UxehAB0toxmj3fTSu";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_ai_fragment, container, false);


        imageSearch = view.findViewById(R.id.etTextAi);
        genrateImagesBtn = view.findViewById(R.id.btnGenerate);
        rvImageAi = view.findViewById(R.id.rvImagesAi);


        imageUrls = new ArrayList<>();
        imageAdapter = new WallpaperAdapter(imageUrls,getContext());
        rvImageAi.setLayoutManager(new LinearLayoutManager(getContext()));
        rvImageAi.setAdapter(imageAdapter);


        genrateImagesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prompt = imageSearch.getText().toString().trim();
                if (!prompt.isEmpty()) {
                    generateImages(prompt);
                } else {
                    Toast.makeText(getContext(), "Please enter text to generate images", Toast.LENGTH_SHORT).show();
                }
            }
        });





        return view;
    }

    private void generateImages(String prompt)  {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "dall-e-3");
            jsonBody.put("prompt", prompt);
            jsonBody.put("n", 1);
            jsonBody.put("size", "1024x1024");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, base_url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            imageUrls.clear();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject imageObject = data.getJSONObject(i);
                                String imageUrl = imageObject.getString("url");
                                imageUrls.add(imageUrl);
                            }
                            imageAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            String errorString = new String(response.data);
                            Log.e(TAG, "Error: " + errorString);
                        }
                        Toast.makeText(getContext(), "API call failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + API_KEY);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }
}