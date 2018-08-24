package com.iiita.messmanagement.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iiita.messmanagement.R;
import com.iiita.messmanagement.RecyclerAdapter;
import com.iiita.messmanagement.Report;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RateFragment extends Fragment {
    private List<Report> reportList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerAdapter mAdapter;
    private boolean sendData = false;
    private View viewSave;
    private ArrayList<String> listAnswer = new ArrayList<>();
    private ArrayList<Boolean> listCheck = new ArrayList<>();
    private ArrayList<String> listQuestion = new ArrayList<>();
    private FloatingActionButton floatingActionButton;
    private TextView textView;
    private ProgressDialog pDialog;
    private LinearLayout linearLayout;
    private String responseCode = "";
    private SharedPreferences pref;

    public RateFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate, container, false);
        viewSave = view;
        linearLayout = viewSave.findViewById(R.id.id_done_for_today);
        recyclerView = view.findViewById(R.id.recycler_view);
        mAdapter = new RecyclerAdapter(reportList, getContext());
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        prepareReportData();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemViewCacheSize(reportList.size());
        textView = view.findViewById(R.id.json);

        pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        String date = pref.getString("doneToday", "");
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String todaysDate = df.format(c);
        floatingActionButton = view.findViewById(R.id.upload);
        if (todaysDate.equalsIgnoreCase(date)) {
            floatingActionButton.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            floatingActionButton.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean allChecked = false;
                listAnswer.clear();
                listCheck.clear();
                listQuestion.clear();
                for (int i = 0; i < mAdapter.getReportList().size(); i++) {
                    Report report = mAdapter.getReportList().get(i);
                    boolean isCheck = report.isChecked();
                    if (isCheck) {
                        listAnswer.add(i, report.getAnswer());
                        listCheck.add(i, true);
                        allChecked = true;
                    } else {
                        listAnswer.add("");
                        listCheck.add(false);
                        allChecked = false;
                    }
                    listQuestion.add(i, report.getQuestion());
                }
                showDialog(allChecked);

            }
        });
        return view;
    }

    private void sendDataToDatabase
            (ArrayList<String> listAnswer, ArrayList<Boolean> listCheck) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < listAnswer.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            String comment = listAnswer.get(i);
            String que = listQuestion.get(i);
            boolean check = listCheck.get(i);
            try {
                jsonObject.put("ques_type", "DAILY");
                jsonObject.put("ques", que);
                jsonObject.put("comment", comment);
                jsonObject.put("check", check);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("questions", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendDataToDatabase(jsonObject.toString());

        if (responseCode.equals("201") || responseCode.equals("500")) {
            Toast.makeText(getContext(), "Report cannot be Send", Toast.LENGTH_SHORT).show();
        } else {
            if (responseCode.equals("500"))
                Toast.makeText(getContext(), "Report Already Sent", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "Report Successfully Sent", Toast.LENGTH_SHORT).show();
            floatingActionButton.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            SharedPreferences.Editor editor = pref.edit();
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c);

            editor.putString("doneToday", formattedDate);
            editor.apply();
        }
    }


    private void showDialog(boolean allChecked) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        String statement = "All checks not done. Are you sure you want to submit?";
        if (allChecked)
            statement = "All check done. Press yes to submit report";
        builder.setTitle("Submit  Report")
                .setMessage(statement)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        doOnTrue();
                    }


                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }

    private void doOnTrue() {
        sendData = true;
        if (sendData) {
            sendDataToDatabase(listAnswer, listCheck);
        }

    }

    private void prepareReportData() {
        reportList.add(new Report(getString(R.string.ques1), "", false));
        reportList.add(new Report(getString(R.string.ques2), "", false));
        reportList.add(new Report(getString(R.string.ques3), "", false));
        reportList.add(new Report(getString(R.string.ques4), "", false));
        reportList.add(new Report(getString(R.string.ques5), "", false));
        reportList.add(new Report(getString(R.string.ques6), "", false));
        reportList.add(new Report(getString(R.string.ques7), "", false));
        reportList.add(new Report(getString(R.string.ques8), "", false));
        reportList.add(new Report(getString(R.string.ques9), "", false));
        reportList.add(new Report(getString(R.string.ques10), "", false));
        reportList.add(new Report(getString(R.string.ques11), "", false));
        reportList.add(new Report(getString(R.string.ques12), "", false));
        reportList.add(new Report(getString(R.string.ques13), "", false));
        reportList.add(new Report(getString(R.string.ques14), "", false));
        reportList.add(new Report(getString(R.string.ques15), "", false));
        reportList.add(new Report(getString(R.string.ques16), "", false));
        reportList.add(new Report(getString(R.string.ques17), "", false));
        reportList.add(new Report(getString(R.string.ques18), "", false));
    }

    public void sendDataToDatabase(String json) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String URL = "http://172.19.15.74:8000/api/v1/review/";
        final String requestBody = json;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                responseCode = response;
                if (responseCode == "403") {//
                    floatingActionButton.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    SharedPreferences.Editor editor = pref.edit();
                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);

                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(c);
                    editor.putString("doneToday", formattedDate);
                    editor.apply();
                }
                Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("TOKEN", "abcd");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("TOKEN", "abcd");
                return headers;
            }

        };

        requestQueue.add(stringRequest);

    }
}