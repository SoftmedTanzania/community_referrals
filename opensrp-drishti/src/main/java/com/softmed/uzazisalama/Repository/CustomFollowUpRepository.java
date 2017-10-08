package com.softmed.uzazisalama.Repository;

import android.content.ContentValues;

import static org.ei.opensrp.commonregistry.CommonRepository.DETAILS_COLUMN;
import static org.ei.opensrp.commonregistry.CommonRepository.ID_COLUMN;
import static org.ei.opensrp.commonregistry.CommonRepository.Relational_ID;

/**
 * Created by Kency on 14/09/2017.
 */

public class CustomFollowUpRepository {

    public static final String FOLLOW_UP_DATA = "FOLLOW_UP_DATA";
    public static final String REPORT_DATE = "REPORT_DATE";
    public static final String IS_ON_RISK = "IS_ON_RISK";
    public static final String FACILITY_ID = "FACILITY_ID";
    public static final String MOTHERS_ID = "MOTHERS_ID";
    public static final String CREATEDBY = "CreatedBy";
    public static final String MODIFYBY = "ModifyBy";



    public ContentValues createValuesFor(FollowUpReportObject followUpReportObject) {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, followUpReportObject.getId());
        values.put(Relational_ID, followUpReportObject.getRelationalId());
        values.put(MOTHERS_ID, followUpReportObject.getMOTHERS_ID());
        values.put(FOLLOW_UP_DATA, followUpReportObject.getREPORT_DATE());
        values.put(REPORT_DATE, followUpReportObject.getREPORT_DATE());
        values.put(IS_ON_RISK, followUpReportObject.getIS_ON_RISK());
        values.put(FACILITY_ID, followUpReportObject.getFACILITY_ID());
        values.put(CREATEDBY, followUpReportObject.getCreatedBy());
        values.put(MODIFYBY, followUpReportObject.getCreatedBy());
        values.put(DETAILS_COLUMN, followUpReportObject.getDetails());
        return values;
    }

}
