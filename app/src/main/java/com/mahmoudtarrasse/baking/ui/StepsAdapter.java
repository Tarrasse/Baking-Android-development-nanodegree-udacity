package com.mahmoudtarrasse.baking.ui;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mahmoudtarrasse.baking.R;
import com.mahmoudtarrasse.baking.modules.Steps;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by mahmoud on 10/05/17.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {

    private JSONArray steps;
    private Context context;
    private ItemClickListener listener;
    public StepsAdapter(Context context, JSONArray steps) {
        this.steps = steps;
        this.context = context;
    }

    @Override
    public StepViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_list_item, parent, false);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClick(v, parent.indexOfChild(rootView));
            }
        });
        return new StepViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        Gson gson = new Gson();
        try {
            String stepJson = steps.get(position).toString();
            Steps step = gson.fromJson(stepJson, Steps.class);
            holder.mLongTextView.setText(step.getDescription());
            holder.mShortTextView.setText(step.getShortDescription());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (steps == null)
            return 0;
        return steps.length();
    }

    public static class StepViewHolder extends RecyclerView.ViewHolder {
        public TextView mShortTextView;
        public TextView mLongTextView;
        public StepViewHolder(View v) {
            super(v);
            mLongTextView = (TextView) v.findViewById(R.id.step_long_description);
            mShortTextView = (TextView) v.findViewById(R.id.step_short_description);
        }
    }

    public void switchData(JSONArray steps){
        this.steps = steps;
        notifyDataSetChanged();
    }

    public void setOnClickListner(ItemClickListener listner){
        this.listener = listner;
    }

    public interface ItemClickListener {
        public void OnItemClick(View v, int position);
    }
}
