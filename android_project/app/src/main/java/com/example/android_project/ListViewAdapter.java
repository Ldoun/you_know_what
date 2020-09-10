package com.example.android_project;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter implements View.OnClickListener{

    private TextView textView;
    private Button btnSha, btnDel;

    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    public ListViewAdapter(){

    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_row, parent, false);
        }

        textView = (TextView) convertView.findViewById(R.id.replayTextView);
        /*btnSha = convertView.findViewById(R.id.btnSha);
        btnDel = convertView.findViewById(R.id.btnDel);

        btnSha.setOnClickListener(this);
        btnDel.setOnClickListener(this);*/

        final ListViewItem listViewItem = listViewItemList.get(position);

        textView.setText(listViewItem.getText());

        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.linearLayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReplayDialog replayDialog = new ReplayDialog(view.getContext(),
                        listViewItemList.get(position).getText().toString(),
                        "08.06");
                replayDialog.setCancelable(true);
                replayDialog.getWindow().setGravity(Gravity.CENTER);
                replayDialog.show();
            }
        });
        return convertView;
    }

    public void addItem(String text) {
        ListViewItem item = new ListViewItem();
        item.setText(text);

        listViewItemList.add(item);
    }

    @Override
    public void onClick(View view) {
        /*switch (view.getId()){
            case R.id.btnSha:
                break;
            case R.id.btnDel:
                break;
        }*/
    }
}
