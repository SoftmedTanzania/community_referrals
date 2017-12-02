package com.softmed.uzazisalama.Repository;

import android.content.ContentValues;

import static org.ei.opensrp.commonregistry.CommonRepository.DETAILS_COLUMN;
import static org.ei.opensrp.commonregistry.CommonRepository.ID_COLUMN;
import static org.ei.opensrp.commonregistry.CommonRepository.Relational_ID;

/**
 * Created by Kency on 14/09/2017.
 */

public class CustomChildRepository {

    public static final String GENDER = "GENDER";
    public static final String STATUS = "STATUS";
    public static final String PROBLEM = "PROBLEM";
    public static final String WEIGHT = "WEIGHT";
    public static final String CREATEDBY = "CreatedBy";
    public static final String MODIFYBY = "ModifyBy";



    public ContentValues createValuesFor(ChildPersonObject childPersonObject) {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, childPersonObject.getId());
        values.put(Relational_ID, childPersonObject.getId());
        values.put(GENDER, childPersonObject.getGENDER());
        values.put(WEIGHT, childPersonObject.getWEIGHT());
        values.put(PROBLEM, childPersonObject.getPROBLEM());
        values.put(STATUS, childPersonObject.getSTATUS());
        values.put(CREATEDBY, childPersonObject.getCreatedBy());
        values.put(MODIFYBY, childPersonObject.getModifyBy());
        values.put(DETAILS_COLUMN, childPersonObject.getDetails());
        return values;
    }

}
