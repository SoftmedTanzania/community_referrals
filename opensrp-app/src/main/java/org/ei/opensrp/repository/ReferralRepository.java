package org.ei.opensrp.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;

import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.ei.opensrp.domain.Referral;

import java.util.ArrayList;
import java.util.List;

import static net.sqlcipher.DatabaseUtils.longForQuery;
import static org.apache.commons.lang3.StringUtils.repeat;

public class ReferralRepository extends DrishtiRepository {
    private static final String CHILD_SQL = "CREATE TABLE referral(id VARCHAR PRIMARY KEY, " +
            "relationalid VARCHAR, " +
            "client_id VARCHAR, " +
            "referral_date VARCHAR," +
            "appointment_date VARCHAR, " +
            "facility_id VARCHAR, " +
            "referral_reason VARCHAR, " +
            "referral_service_id VARCHAR, " +
            "referral_status VARCHAR, " +
            "is_emergency VARCHAR, " +
            "is_valid VARCHAR, " +
            "indicator_ids VARCHAR, " +
            "other_notes VARCHAR, " +
            "services_given_to_patient VARCHAR, " +
            "referral_type INTEGER, " +
            "service_provider_uuid VARCHAR, " +
            "referral_feedback_id VARCHAR, " +
            "referral_uuid VARCHAR, " +
            "test_results VARCHAR, " +
            "details VARCHAR)";
    public static final String TABLE_NAME = "referral";
    public static final String ID_COLUMN = "id";
    public static final String RELATIONAL_ID = "relationalid";
    public static final String CLIENT_ID = "client_id";
    public static final String ReferralDate = "referral_date";
    public static final String ReferralFacility = "facility_id";
    public static final String ReferralReason = "referral_reason";
    public static final String Service = "referral_service_id";
    public static final String ReferralStatus = "referral_status";
    public static final String IsEmergency = "is_emergency";
    public static final String AppointmentDate = "appointment_date";
    public static final String IS_VALID = "is_valid";
    public static final String INDICATOR_IDS = "indicator_ids";
    public static final String OTHER_NOTES = "other_notes";
    public static final String SERVICES_GIVEN_TO_PATIENTS = "services_given_to_patient";
    public static final String REFERRAL_TYPE = "referral_type";
    public static final String SERVICE_PROVIDER_UUID = "service_provider_uuid";
    public static final String REFERRAL_FEEDBACK_ID = "referral_feedback_id";
    public static final String REFERRAL_UUID = "referral_uuid";
    public static final String TEST_RESULTS = "test_results";
    public static final String DETAILS_COLUMN = "details";
    public static final String[] CLIENT_REFERRAL_TABLE_COLUMNS = {ID_COLUMN, RELATIONAL_ID, CLIENT_ID, ReferralDate,AppointmentDate, ReferralFacility, ReferralReason, Service, ReferralStatus, IsEmergency,IS_VALID,INDICATOR_IDS,OTHER_NOTES,SERVICES_GIVEN_TO_PATIENTS,REFERRAL_TYPE, SERVICE_PROVIDER_UUID, REFERRAL_FEEDBACK_ID,REFERRAL_UUID,TEST_RESULTS,DETAILS_COLUMN};
    

    @Override
    protected void onCreate(SQLiteDatabase database) {
        database.execSQL(CHILD_SQL);
    }

    public void add(Referral referral) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.insert(ReferralRepository.TABLE_NAME, null, createValuesFor(referral));
    }

    public void update(Referral referral) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.update(TABLE_NAME, createValuesFor(referral), ID_COLUMN + " = ?", new String[]{referral.getId()});
    }

    public List<Referral> all() {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, CLIENT_REFERRAL_TABLE_COLUMNS, null, null, null, null, null);
        return readAll(cursor);
    }

    public Referral find(String caseId) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, CLIENT_REFERRAL_TABLE_COLUMNS, ID_COLUMN + " = ?", new String[]{caseId}, null, null, null, null);
        List<Referral> children = readAll(cursor);

        if (children.isEmpty()) {
            return null;
        }
        return children.get(0);
    }

    public List<Referral> findByClientId(String caseId) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, CLIENT_REFERRAL_TABLE_COLUMNS, CLIENT_ID + " = ?", new String[]{caseId}, null, null, null, null);
        List<Referral> children = readAll(cursor);


        return children;
    }


    public List<Referral> findClientReferralByCaseIds(String... caseIds) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.rawQuery(String.format("SELECT * FROM %s WHERE %s IN (%s)", TABLE_NAME, ID_COLUMN, insertPlaceholdersForInClause(caseIds.length)), caseIds);
        return readAll(cursor);
    }
    public Referral findByServiceStatus(String name) {

        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, CLIENT_REFERRAL_TABLE_COLUMNS, ReferralStatus + " = ?", new String[]{name}, null, null, null, null);
        List<Referral> children = readAll(cursor);

        if (children.isEmpty()) {
            return null;
        }
        return children.get(0);
    }
    public void updateStatus(String caseId, String name ){
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ReferralStatus, name);
        database.update(TABLE_NAME, values, ID_COLUMN + " = ?", new String[]{caseId});
    }

    public List<Referral> findByServiceCaseId(String caseId) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, CLIENT_REFERRAL_TABLE_COLUMNS, ID_COLUMN + " = ?", new String[]{caseId}, null, null, null, null);
        return readAll(cursor);
    }

    public long count() {
        return longForQuery(masterRepository.getReadableDatabase(), "SELECT COUNT(1) FROM " + TABLE_NAME, new String[0]);
    }

    public long unsuccesfulcount() {
        return longForQuery(masterRepository.getReadableDatabase(), "SELECT COUNT(1) FROM " + TABLE_NAME
                        + " WHERE " + ReferralStatus + " = ? ",
                new String[]{"0"});
    }
    public long succesfulcount() {
        return longForQuery(masterRepository.getReadableDatabase(), "SELECT COUNT(1) FROM " + TABLE_NAME
                        + " WHERE " + ReferralStatus + " = ? ",
                new String[]{"1"});
    }
    private String tableColumnsForQuery(String tableName, String[] tableColumns) {
        return StringUtils.join(prepend(tableColumns, tableName), ", ");
    }

    private String[] prepend(String[] input, String tableName) {
        int length = input.length;
        String[] output = new String[length];
        for (int index = 0; index < length; index++) {
            output[index] = tableName + "." + input[index] + " as " + tableName + input[index];
        }
        return output;
    }


    public ContentValues createValuesFor(Referral referral) {

        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, referral.getId());
        values.put(RELATIONAL_ID, referral.getRelationalid());
        values.put(CLIENT_ID, referral.getClient_id());
        values.put(ReferralDate, referral.getReferral_date());
        values.put(AppointmentDate, referral.getAppointment_date());
        values.put(ReferralFacility, referral.getFacility_id());
        values.put(ReferralReason, referral.getReferral_reason());
        values.put(Service, referral.getReferral_service_id());
        values.put(ReferralStatus, referral.getReferral_status());
        values.put(IS_VALID, referral.getIs_valid());
        values.put(IsEmergency, referral.getIs_emergency());
        values.put(INDICATOR_IDS, referral.getIndicator_ids());
        values.put(OTHER_NOTES, referral.getOther_notes());
        values.put(REFERRAL_TYPE, referral.getReferral_type());
        values.put(SERVICES_GIVEN_TO_PATIENTS, referral.getServices_given_to_patient());
        values.put(REFERRAL_FEEDBACK_ID, referral.getReferral_feedback_id());
        values.put(REFERRAL_UUID, referral.getReferral_uuid());
        values.put(TEST_RESULTS, referral.getTest_results());
        values.put(DETAILS_COLUMN, new Gson().toJson(referral));
        return values;
    }

    public ContentValues createValuesUpdateValues(Referral referral) {

        ContentValues values = new ContentValues();
        values.put(ReferralStatus, referral.getReferral_status());
        values.put(DETAILS_COLUMN, new Gson().toJson(referral));
        return values;
    }


    private List<Referral> readAll(Cursor cursor) {
        cursor.moveToFirst();
        List<Referral> referralServicesListDataModel = new ArrayList<Referral>();
        while (!cursor.isAfterLast()) {
            referralServicesListDataModel.add(new Referral(cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getLong(3),
                    cursor.getLong(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getString(10),
                    cursor.getString(11),
                    cursor.getString(12),
                    cursor.getString(13),
                    cursor.getInt(14),
                    cursor.getString(15),
                    cursor.getString(16),
                    cursor.getString(17),
                    cursor.getString(18),
                    cursor.getString(19))
            );
            cursor.moveToNext();
        }
        cursor.close();
        return referralServicesListDataModel;
    }

    private List<Referral> readAllChildren(Cursor cursor) {
        cursor.moveToFirst();
        List<Referral> children = new ArrayList<Referral>();
        while (!cursor.isAfterLast()) {
            children.add(serviceFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return children;
    }

    private Referral serviceFromCursor(Cursor cursor) {
        return new Referral(
                getColumnValueByAlias(cursor, TABLE_NAME, ID_COLUMN),
                getColumnValueByAlias(cursor, TABLE_NAME, RELATIONAL_ID),
                getColumnValueByAlias(cursor, TABLE_NAME, CLIENT_ID),
                Long.parseLong(getColumnValueByAlias(cursor, TABLE_NAME, ReferralDate)),
                Long.parseLong(getColumnValueByAlias(cursor, TABLE_NAME, AppointmentDate)),
                getColumnValueByAlias(cursor, TABLE_NAME, ReferralFacility),
                getColumnValueByAlias(cursor, TABLE_NAME, ReferralReason),
                getColumnValueByAlias(cursor, TABLE_NAME, Service),
                getColumnValueByAlias(cursor, TABLE_NAME, ReferralStatus),
                getColumnValueByAlias(cursor, TABLE_NAME, IsEmergency),
                getColumnValueByAlias(cursor, TABLE_NAME, IS_VALID),
                getColumnValueByAlias(cursor, TABLE_NAME, INDICATOR_IDS),
                getColumnValueByAlias(cursor, TABLE_NAME, OTHER_NOTES),
                getColumnValueByAlias(cursor, TABLE_NAME, SERVICES_GIVEN_TO_PATIENTS),
                Integer.parseInt(getColumnValueByAlias(cursor, TABLE_NAME, REFERRAL_TYPE)),
                getColumnValueByAlias(cursor, TABLE_NAME, SERVICES_GIVEN_TO_PATIENTS),
                getColumnValueByAlias(cursor, TABLE_NAME, REFERRAL_FEEDBACK_ID),
                getColumnValueByAlias(cursor, TABLE_NAME, REFERRAL_UUID),
                getColumnValueByAlias(cursor, TABLE_NAME, TEST_RESULTS),
                getColumnValueByAlias(cursor, TABLE_NAME, DETAILS_COLUMN));

    }

    private String getColumnValueByAlias(Cursor cursor, String table, String column) {
        return cursor.getString(cursor.getColumnIndex(table + column));
    }

    private String insertPlaceholdersForInClause(int length) {
        return repeat("?", ",", length);
    }


    public void delete(String childId) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.delete(TABLE_NAME, ID_COLUMN + "= ?", new String[]{childId});
    }

    public Cursor RawQuery(String query) {
        Log.i(getClass().getName(), query);
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }
}
