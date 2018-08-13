package com.nativo.juan.citaprevia.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.nativo.juan.citaprevia.model.IssueType;

/**
 * Adaptador de tipos de PQRS
 */

public class IssueTypeAdapter extends ArrayAdapter<IssueType> {

    private final Context mContext;
    private List<IssueType> mIssueTypes;

    public IssueTypeAdapter(@NonNull Context context, @NonNull List<IssueType> issueTypes){
        super(context, 0, issueTypes);
        mContext = context;
        mIssueTypes = issueTypes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        IssueType issueType = mIssueTypes.get(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        } else {
            view = convertView;
        }

        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(issueType.getName());

        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return mIssueTypes.size();
    }

    @Nullable
    @Override
    public IssueType getItem(int position) {
        return mIssueTypes.get(position);
    }

    public void replaceData(List<IssueType> issueTypes) {
        mIssueTypes = issueTypes;
        notifyDataSetChanged();
    }
}
