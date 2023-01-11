package com.nikitavenediktov.sportapp.Views.Main;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nikitavenediktov.sportapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Locale;

public class FactsFragment extends Fragment {
    TextView facts_tv;
    Button reload_btn;
    ProgressBar load_bar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);
        
        View rootView = inflater.inflate(R.layout.facts_tab, container, false);

        facts_tv = (TextView) rootView.findViewById(R.id.facts_tv);
        reload_btn = (Button) rootView.findViewById(R.id.reload_btn);
        load_bar = (ProgressBar) rootView.findViewById(R.id.loading_bar);

        FactsTask factsTask = new FactsTask();
        factsTask.execute();

        reload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FactsTask factsTask = new FactsTask();
                factsTask.execute();
            }
        }
        );
        return rootView;
    }

    private void hideView(View v) {
        v.setFocusable(false);
        v.setVisibility(View.INVISIBLE);
    }

    private void showView(View v) {
        v.setFocusable(true);
        v.setVisibility(View.VISIBLE);
    }

    class FactsTask extends AsyncTask<Void, Void, String> {
        String url = "https://opentdb.com/api.php?amount=10&category=21&type=boolean";

        @Override
        protected String doInBackground(Void... voids) {
            final String[] res = new String[1];

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @SuppressLint("DefaultLocale")
                        @Override
                        public void onResponse(String response)
                        {
                            synchronized (requestQueue) {
                                res[0] = "";

                                try
                                {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    JSONArray facts = jsonResponse.getJSONArray("results");
                                    int counter = 1;
                                    for (int i = 0; i < facts.length(); ++i)
                                    {
                                        JSONObject fact = facts.getJSONObject(i);
                                        if (fact.getBoolean("correct_answer"))
                                        {
                                            res[0] +=
                                                    String.format("%d. %s\n", counter++, fact.getString("question"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), getString(R.string.parsing_error), Toast.LENGTH_LONG)
                                            .show();
                                }

                                requestQueue.notify();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), getString(R.string.network_error), Toast.LENGTH_LONG)
                            .show();
                    synchronized (requestQueue) {
                        requestQueue.notify();
                    }
                }
            });

            requestQueue.add(stringRequest);

            synchronized (requestQueue)
            {
                try {
                    requestQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), getString(R.string.network_error), Toast.LENGTH_LONG)
                            .show();
                }

                return res[0];
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showView(load_bar);
            hideView(facts_tv);
            hideView(reload_btn);
        }


        @Override
        protected void onPostExecute(String facts) {
            super.onPostExecute(facts);

            facts_tv.setText(facts);
            showView(facts_tv);
            showView(reload_btn);
            hideView(load_bar);
        }

    }
}
