package com.softmed.uzazisalama;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.softmed.uzazisalama.DataModels.PncMother;
import com.softmed.uzazisalama.DataModels.PregnantMom;

import org.ei.opensrp.drishti.R;

import java.util.List;

public class MotherPncReport extends AppCompatActivity {
    private PregnantMom pregnantMom;
    private List<Object> motherList;

    private Gson gson = new Gson();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table);

        String gsonMoms = getIntent().getStringExtra("moms");
        String type = getIntent().getStringExtra("type");

        if (type.equals("anc"))
            motherList = gson.fromJson(gsonMoms, new TypeToken<List<PregnantMom>>() {
            }.getType());

        else {
            motherList = gson.fromJson(gsonMoms, new TypeToken<List<PncMother>>() {
            }.getType());
        }

        Log.d("MotherPncReport", "list count = " + motherList.size());

        fillTable();
    }


    public void fillTable() {
        int rowCount = motherList.size();
        Log.d("Fill Table", "rowCount = " + rowCount);
        TableLayout table = (TableLayout) this.findViewById(R.id.tablelayout);
        for (int i = 0; i < rowCount; i++) {

            fillRow2(table, i);
        }
    }


    public void fillRow2(TableLayout table, int noRow) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View fullRow = inflater.inflate(R.layout.row, null, false);
        TextView nr = (TextView) fullRow.findViewById(R.id.visitorSN);
        nr.setText(noRow + 1);
        TextView Name = (TextView) fullRow.findViewById(R.id.visitorMothersName);
        Name.setText(pregnantMom.getName());

        TextView lName = (TextView) fullRow.findViewById(R.id.visitorMobile);
        lName.setText(pregnantMom.getPhone());
        table.addView(fullRow);
    }
}
