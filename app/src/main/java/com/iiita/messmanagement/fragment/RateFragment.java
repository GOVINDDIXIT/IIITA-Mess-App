package com.iiita.messmanagement.fragment;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.iiita.messmanagement.R;
import com.iiita.messmanagement.RecyclerAdapter;
import com.iiita.messmanagement.Report;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RateFragment extends Fragment {
    private List<Report> reportList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerAdapter mAdapter;
    private boolean sendData = false;
    private View viewSave;
    private ArrayList<String> listAnswer = new ArrayList<>();
    private ArrayList<Boolean> listCheck = new ArrayList<>();
    private FloatingActionButton floatingActionButton;
    public RateFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate, container, false);
        viewSave = view;

        recyclerView = view.findViewById(R.id.recycler_view);
        mAdapter = new RecyclerAdapter(reportList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        prepareReportData();
        recyclerView.setAdapter(mAdapter);

        floatingActionButton = view.findViewById(R.id.upload);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean allChecked = false;
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    View view1 = recyclerView.getChildAt(i);
                    CheckBox checkBox = (CheckBox) view1.findViewById(R.id.checkBox_id);
                    EditText editText = (EditText) view1.findViewById(R.id.et_answer);
                    if (checkBox.isChecked()) {
                        listAnswer.add(editText.getText().toString() + "");
                        listCheck.add(true);
                        allChecked = true;
                    } else {
//                        Toast.makeText(getContext(), "Checks Remaining", Toast.LENGTH_SHORT).show();
                        listAnswer.add("");
                        listCheck.add(false);
                        allChecked = false;
                    }
                }
                showDialog(allChecked);

            }
        });
        return view;
    }

    private void sendDataToDatabase(ArrayList<String> listAnswer, ArrayList<Boolean> listCheck) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < listAnswer.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            String comment = listAnswer.get(i);
            boolean check = listCheck.get(i);
            try {
                jsonObject.put("id", i);
                jsonObject.put("comment", comment);
                jsonObject.put("check", check);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("student", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SendPostRequest sendPostRequest = new SendPostRequest(jsonObject);
        String responseCode = sendPostRequest.doInBackground();
        if (responseCode == null) {
            //TODO
            Toast.makeText(getContext(), "Report cannot be Send", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Report Successfully Sent", Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.GONE);
            floatingActionButton.setVisibility(View.GONE);
            LinearLayout linearLayout = viewSave.findViewById(R.id.id_done_for_today);
            linearLayout.setVisibility(View.GONE);
        }
    }


    private void showDialog(boolean allChecked) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        if (!allChecked) {
            builder.setTitle("Submit  Report")
                    .setMessage("All checks not done. Are you sure you want to submit?")
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

    }

    private void doOnTrue() {
        sendData = true;
        if (sendData == true) {
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

}
