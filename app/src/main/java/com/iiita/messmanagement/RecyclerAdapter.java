package com.iiita.messmanagement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private final int COUNTDOWN_RUNNING_TIME = 500;
    private List<Report> reportList;
    private Animation animationUp;
    private Animation animationDown;
    private Context context;


    public RecyclerAdapter(List<Report> reportList, Context context) {
        this.reportList = reportList;
        this.context = context;
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
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            int isDown = 0;

            @Override
            public void onClick(View view) {
                holder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!holder.checkBox.isChecked()) {
                            holder.editText.setVisibility(View.GONE);
                            holder.imageView.setImageResource(R.drawable.ic_arrow_drop_down);
                        }
                    }
                });
                if (holder.checkBox.isChecked()) {
                    report.setChecked(true);
                    report.setAnswer(holder.editText.getText().toString());
                    holder.editText.setVisibility(View.VISIBLE);
                    holder.imageView.setImageResource(R.drawable.ic_arrow_drop_up);
                    isDown++;
//                    holder.editText.startAnimation(animationDown);
                } else {
                    holder.editText.setVisibility(View.GONE);
                    holder.imageView.setImageResource(R.drawable.ic_arrow_drop_down);
                    Toast.makeText(context, "Check the item", Toast.LENGTH_SHORT).show();
//                    holder.editText.startAnimation(animationUp);
                }
                if (isDown % 2 == 0) {
                    holder.editText.setVisibility(View.GONE);
                    holder.imageView.setImageResource(R.drawable.ic_arrow_drop_down);
                    isDown = 0;
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
