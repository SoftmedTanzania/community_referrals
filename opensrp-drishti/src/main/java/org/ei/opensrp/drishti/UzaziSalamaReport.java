package org.ei.opensrp.drishti;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ei.opensrp.drishti.util.DatesHelper;

import org.ei.opensrp.commonregistry.CommonRepository;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class UzaziSalamaReport extends AppCompatActivity implements java.io.Serializable  {
//    private PregnantMom pregnantMom,mother;
//    private PncMother pncMom;
//    private PncPersonObject pncMomObject;
//    private Child child;
    private List<Object> motherList;
    private final static String TAG = UzaziSalamaReport.class.getSimpleName();
    private Gson gson = new Gson();
    private String risk,delivery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table);

        String gsonMoms = getIntent().getStringExtra("moms");
        String type = getIntent().getStringExtra("type");
        risk = getIntent().getStringExtra("risk");
        delivery = getIntent().getStringExtra("delivery");

//        if (type.equals("anc")){
//            motherList = gson.fromJson(gsonMoms, new TypeToken<List<PregnantMom>>() {
//            }.getType());
//            Log.d("UzaziSalamaReport", "list count = " + motherList.size());
//
//            fillTableAncMother();
//        } else {
//            motherList = gson.fromJson(gsonMoms, new TypeToken<List<PncPersonObject>>() {
//            }.getType());
//            Log.d("UzaziSalamaReport", "list count = " + motherList.size());
//
//            fillTablePncMother();
//        }


    }


//    public void fillTableAncMother() {
//        int rowCount = motherList.size();
//        Log.d("Fill Table", "rowCount = " + rowCount);
//        Log.d("Fill Table", "mom = " + gson.toJson(motherList));
//
//        TableLayout table = (TableLayout) this.findViewById(R.id.tablelayout);
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View tableheader;
//        if(risk.equalsIgnoreCase("yes")) {
//
//            View key = inflater.inflate(R.layout.key, null, false);
//            table.addView(key);
//            tableheader = inflater.inflate(R.layout.table_anc_isrisk_header, null, false);
//            table.addView(tableheader);
//            for (int i = 0; i < rowCount; i++) {
//
//                fillRowAncIsRiskMother(table, i);
//            }
//        } else{
//            tableheader = inflater.inflate(R.layout.table_anc_header, null, false);
//            table.addView(tableheader);
//            for (int i = 0; i < rowCount; i++) {
//
//                fillRowAncMother(table, i);
//            }
//        }
//
//
//
//
//
//    }
//
//    public void fillTablePncMother() {
//        int rowCount = motherList.size();
//        Log.d("Fill Table", "rowCount = " + rowCount);
//        Log.d("Fill Table", "mom = " + gson.toJson(motherList));
//
//        TableLayout table = (TableLayout) this.findViewById(R.id.tablelayout);
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View tableheader = inflater.inflate(R.layout.table_pnc_header, null, false);
//        table.addView(tableheader);
//        for (int i = 0; i < rowCount; i++) {
//
//            fillRowPncMother(table, i);
//        }
//    }
//
//    public void fillRowAncMother(TableLayout table, int noRow) {
//        pregnantMom = (PregnantMom) motherList.get(noRow);
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View fullRow = inflater.inflate(R.layout.row_anc, null, false);
//        TextView nr = (TextView) fullRow.findViewById(R.id.visitorSN);
//        nr.setText(String.valueOf(noRow + 1));
//
//        TextView Name = (TextView) fullRow.findViewById(R.id.visitorMothersName);
//        Name.setText(pregnantMom.getName());
//
//        TextView phone = (TextView) fullRow.findViewById(R.id.visitorMobile);
//        phone.setText(pregnantMom.getPhone());
//
//        // automate follow up number
//        long lnmp = pregnantMom.getDateLNMP();
//        long firstVisit = DatesHelper.calculate1stVisitFromLNMP(lnmp);
//        long secondVisit = DatesHelper.calculate2ndVisitFromLNMP(lnmp);
//        long thirdVisit = DatesHelper.calculate3rdVisitFromLNMP(lnmp);
//        long fourthVisit = DatesHelper.calculate4thVisitFromLNMP(lnmp);
//        ImageView app1 = (ImageView) fullRow.findViewById(R.id.visitorApp1);
//        ImageView app2 = (ImageView) fullRow.findViewById(R.id.visitorApp2);
//        ImageView app3 = (ImageView) fullRow.findViewById(R.id.visitorApp3);
//        ImageView app4 = (ImageView) fullRow.findViewById(R.id.visitorApp4);
//
//        if(pregnantMom.isAncAppointment1()){
//            app1.setImageResource(R.drawable.ic_check_black_24dp);
//        }else if(System.currentTimeMillis()>firstVisit && !pregnantMom.isAncAppointment1() ){
//            app1.setImageResource(R.drawable.ic_clear_black_24dp);
//        }else{
//            app1.setImageResource(R.drawable.ic_remove_black_24dp);
//        }
//
//        if(pregnantMom.isAncAppointment2()){
//            app2.setImageResource(R.drawable.ic_check_black_24dp);
//        }else if(System.currentTimeMillis()>secondVisit && !pregnantMom.isAncAppointment2() ){
//            app2.setImageResource(R.drawable.ic_clear_black_24dp);
//        }else{
//            app2.setImageResource(R.drawable.ic_remove_black_24dp);
//        }
//
//        if(pregnantMom.isAncAppointment3()){
//            app3.setImageResource(R.drawable.ic_check_black_24dp);
//        }else if(System.currentTimeMillis()>thirdVisit && !pregnantMom.isAncAppointment3() ){
//            app3.setImageResource(R.drawable.ic_clear_black_24dp);
//        }else{
//            app3.setImageResource(R.drawable.ic_remove_black_24dp);
//        }
//
//        if(pregnantMom.isAncAppointment4()){
//            app4.setImageResource(R.drawable.ic_check_black_24dp);
//        }else if(System.currentTimeMillis()>fourthVisit && !pregnantMom.isAncAppointment4() ){
//            app4.setImageResource(R.drawable.ic_clear_black_24dp);
//        }else{
//            app4.setImageResource(R.drawable.ic_remove_black_24dp);
//        }
//
//
//
//        TextView location = (TextView) fullRow.findViewById(R.id.visitorVillage);
//        location.setText(pregnantMom.getPhysicalAddress());
//        TextView risk = (TextView) fullRow.findViewById(R.id.visitorRisk);
//        if(pregnantMom.isOnRisk()){
//            risk.setText("high");
//            risk.setTextColor(getResources().getColor(R.color.alert_urgent_red));
//        }
//        else{
//            risk.setText("moderate");
//            risk.setTextColor(getResources().getColor(R.color.alert_in_progress_blue));
//        }
//
//
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
//        TextView edd = (TextView) fullRow.findViewById(R.id.visitorEDD);
//        edd.setText(dateFormat.format(pregnantMom.getEdd()));
//
//
//
//        table.addView(fullRow);
//    }
//
//    public void fillRowAncIsRiskMother(TableLayout table, int noRow) {
//        pregnantMom = (PregnantMom) motherList.get(noRow);
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        View fullRow = inflater.inflate(R.layout.row_anc_isrisk, null, false);
//        TextView nr = (TextView) fullRow.findViewById(R.id.visitorSN);
//        nr.setText(String.valueOf(noRow + 1));
//
//        TextView Name = (TextView) fullRow.findViewById(R.id.visitorMothersName);
//        Name.setText(pregnantMom.getName());
//
//        TextView phone = (TextView) fullRow.findViewById(R.id.visitorMobile);
//        phone.setText(pregnantMom.getPhone());
//
//        // automate follow up number
//
//        TextView location = (TextView) fullRow.findViewById(R.id.visitorVillage);
//        location.setText(pregnantMom.getPhysicalAddress());
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
//        TextView edd = (TextView) fullRow.findViewById(R.id.visitorEDD);
//        edd.setText(dateFormat.format(pregnantMom.getEdd()));
//
//
//
//        table.addView(fullRow);
//    }
//
//    public void fillRowPncMother(TableLayout table, int noRow) {
//
//        pncMomObject = (PncPersonObject) motherList.get(noRow);
//        pncMom = gson.fromJson(pncMomObject.getDetails(), PncMother.class);
//        Log.d(TAG, "resultList2 " + gson.toJson(pncMomObject));
//        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM uzazi_salama_child WHERE id = '"+pncMomObject.getChildCaseId()+"'");
//        org.ei.opensrp.Context context = org.ei.opensrp.Context.getInstance().updateApplicationContext(getApplicationContext());
//        CommonRepository commonRepository = context.commonrepository("uzazi_salama_child");
//        Cursor cursor = commonRepository.RawCustomQueryForAdapter(queryBuilder.toString());
//        child = getChildData(cursor);
//
//        queryBuilder = new StringBuilder("SELECT * FROM wazazi_salama_mother WHERE id = '"+pncMomObject.getMotherCaseId()+"'");
//        CommonRepository motherRepository = context.commonrepository("wazazi_salama_mother");
//        cursor = motherRepository.RawCustomQueryForAdapter(queryBuilder.toString());
//        mother = getMothersData(cursor);
//
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View fullRow = inflater.inflate(R.layout.row_pnc, null, false);
//
//        TextView nr = (TextView) fullRow.findViewById(R.id.visitorSN);
//        nr.setText(String.valueOf(noRow + 1));
//
//        TextView Name = (TextView) fullRow.findViewById(R.id.visitorMothersName);
//        Name.setText(mother.getName());
//        Log.d(TAG, "mother details afterwards = " + gson.toJson(mother));
//        Log.d(TAG, "child details afterwards = " + gson.toJson(child));
//
//        TextView age = (TextView) fullRow.findViewById(R.id.visitorAge);
//        age.setText(String.valueOf(mother.getAge()));
//
//        TextView location = (TextView) fullRow.findViewById(R.id.visitorVillage);
//        location.setText(mother.getPhysicalAddress());
//
//        TextView risk = (TextView) fullRow.findViewById(R.id.visitorRisk);
//        if(mother.isOnRisk()){
//            risk.setText("high");
//            risk.setTextColor(getResources().getColor(R.color.alert_urgent_red));
//        }
//        else{
//            risk.setText("moderate");
//            risk.setTextColor(getResources().getColor(R.color.alert_in_progress_blue));
//        }
//
//        TextView childStatus = (TextView) fullRow.findViewById(R.id.visitorChildStatus);
//        childStatus.setText(child.getStatus());
//
//        TextView placeOfBirth = (TextView) fullRow.findViewById(R.id.visitorMotherStatus);
//        placeOfBirth.setText(pncMom.getMother_status());
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
//        TextView edd = (TextView) fullRow.findViewById(R.id.visitorDD);
//        edd.setText(dateFormat.format(pncMom.getDeliveryDate()));
//
//
//        table.addView(fullRow);
//    }
//
//    private Child getChildData(Cursor cursor){
//        Child child = new Child();
//        try {
//            if (cursor.moveToFirst()) {
//                while (!cursor.isAfterLast()) {
//                    // get anc mothers from query result and add them to list
//                    String details = cursor.getString(cursor.getColumnIndex("details"));
//                    child = gson.fromJson(details, Child.class);
//                    Log.d(TAG, "column details = " + details);
//                    Log.d(TAG, "child details = " + gson.toJson(child));
//
//                    cursor.moveToNext();
//                }
//            }
//        } catch (Exception e) {
//            Log.d(TAG, "error: " + e.getMessage());
//            return null;
//
//        } finally {
//            cursor.close();
//        }
//        return child;
//    }
//
//    private PregnantMom getMothersData(Cursor cursor){
//        PregnantMom mom = new PregnantMom();
//        try {
//            if (cursor.moveToFirst()) {
//                while (!cursor.isAfterLast()) {
//                    // get anc mothers from query result and add them to list
//                    String details = cursor.getString(cursor.getColumnIndex("details"));
//                    mom = gson.fromJson(details,PregnantMom.class);
//                    Log.d(TAG, "column details = " + details);
//                    Log.d(TAG, "mom details = " + gson.toJson(mom));
//
//                    cursor.moveToNext();
//                }
//            }
//        } catch (Exception e) {
//            Log.d(TAG, "error: " + e.getMessage());
//            return null;
//
//        } finally {
//            cursor.close();
//        }
//        return mom;
//    }
}
