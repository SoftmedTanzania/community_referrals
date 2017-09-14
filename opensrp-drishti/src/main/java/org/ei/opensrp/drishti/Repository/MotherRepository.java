package org.ei.opensrp.drishti.Repository;

import android.content.ContentValues;

import com.google.gson.Gson;

import net.sqlcipher.database.SQLiteDatabase;

import org.ei.opensrp.commonregistry.CommonFtsObject;
import org.ei.opensrp.commonregistry.CommonRepository;

/**
 * Created by Kency on 14/09/2017.
 */

public class MotherRepository extends CommonRepository {

    public static final String MOTHERS_FIRST_NAME = "MOTHERS_FIRST_NAME";
    public static final String MOTHERS_LAST_NAME = "MOTHERS_LAST_NAME";
    public static final String MOTHERS_SORTVALUE = "MOTHERS_SORTVALUE";
    public static final String EXPECTED_DELIVERY_DATE = "EXPECTED_DELIVERY_DATE";
    public static final String MOTHERS_LAST_MENSTRUATION_DATE = "MOTHERS_LAST_MENSTRUATION_DATE";
    public static final String FACILITY_ID = "FACILITY_ID";
    public static final String IS_PNC = "IS_PNC";
    public static final String IS_VALID = "IS_VALID";
    public static final String PNC_STATUS = "PNC_STATUS";
    public static final String MOTHERS_ID = "MOTHERS_ID";

    public MotherRepository(String tablename, String[] columns) {
        super(tablename, columns);
    }

    public MotherRepository(CommonFtsObject commonFtsObject, String tablename, String[] columns) {
        super(commonFtsObject, tablename, columns);
    }

    public void add(MotherPersonObject common) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.insert(TABLE_NAME, null, createValuesFor(common));
    }

    private ContentValues createValuesFor(MotherPersonObject common) {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, common.getCaseId());
        values.put(Relational_ID, common.getRelationalId());
        values.put(MOTHERS_FIRST_NAME, common.getMOTHERS_FIRST_NAME());
        values.put(MOTHERS_LAST_NAME, common.getMOTHERS_LAST_NAME());
        values.put(MOTHERS_ID, common.getMOTHERS_ID());
        values.put(MOTHERS_SORTVALUE, common.getMOTHERS_SORTVALUE());
        values.put(MOTHERS_LAST_MENSTRUATION_DATE, common.getMOTHERS_LAST_MENSTRUATION_DATE());
        values.put(EXPECTED_DELIVERY_DATE, common.getEXPECTED_DELIVERY_DATE());
        values.put(IS_PNC, common.getIS_PNC());
        values.put(PNC_STATUS, common.getPNC_STATUS());
        values.put(IS_VALID, common.getIS_VALID());
        values.put(FACILITY_ID, common.getFACILITY_ID());
        values.put(DETAILS_COLUMN, new Gson().toJson(common.getDetails()));
        return values;
    }

}
