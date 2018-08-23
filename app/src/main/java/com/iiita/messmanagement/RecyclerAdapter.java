package com.iiita.messmanagement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private List<Report> reportList;
    private Context context;

    public RecyclerAdapter(List<Report> reportList, Context context) {
        this.reportList = reportList;
        this.context = context;
    }

    public List<Report> getReportList() {
        return reportList;
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerAdapter.MyViewHolder holder, final int position) {
        final Report report = reportList.get(position);
        holder.question.setText(report.getQuestion());
        if (report.isChecked()) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked())
                    report.setChecked(true);
                else
                    report.setChecked(false);
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                holder.editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        report.setAnswer(holder.editText.getText().toString());
                    }
                });
                if (holder.editText.getVisibility() == View.VISIBLE) {
                    holder.editText.setVisibility(View.GONE);
                } else {
                    holder.editText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView question;
        public CheckBox checkBox;
        public EditText editText;
        private LinearLayout linearLayout;
        private ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            question = (TextView) view.findViewById(R.id.tv_question);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox_id);
            editText = (EditText) view.findViewById(R.id.et_answer);
            imageView = (ImageView) view.findViewById(R.id.id_arrow);
        }
    }


}
