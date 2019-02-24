package org.ei.opensrp.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;
import net.sqlcipher.database.SQLiteDatabase;
import org.apache.commons.lang3.StringUtils;
import org.ei.opensrp.domain.ClientFollowup;
import org.ei.opensrp.domain.ClientReferral;
import java.util.ArrayList;
import java.util.List;
import static net.sqlcipher.DatabaseUtils.longForQuery;
import static org.apache.commons.lang3.StringUtils.repeat;

public class ClientFollowupRepository extends DrishtiRepository {
    private static final String CHILD_SQL = "CREATE TABLE followup_client(id VARCHAR PRIMARY KEY, relationalid VARCHAR, " +
            "referral_date VARCHAR, " +
            "visit_date VARCHAR," +
            "facility_id VARCHAR, " +
            "referral_reason VARCHAR, " +
            "referral_feedback VARCHAR," +
            "referral_status VARCHAR," +
            "service_provider_uiid VARCHAR, " +
            "is_valid VARCHAR, " +
            "details VARCHAR)";
    public static final String CLIENT_FOLLOWUP = "followup_client";
    public static final String ID_COLUMN = "id";
    public static final String Relational_ID = "relationalid";
    public static final String CLIENT_ID = "client_id";
    public static final String VISIT_DATE = "visit_date";
    public static final String REFERRAL_DATE = "referral_date";
    public static final String FACILITY_ID = "facility_id";
    public static final String REFERRAL_REASON = "referral_reason";
    public static final String REFERRAL_FEEDBACK = "referral_feedback";
    public static final String REFERRAL_STATUS = "referral_status";
    public static final String SERVICE_PROVIDER_UUID = "service_provider_uuid";
    public static final String IS_VALID = "is_valid";
    public static final String DETAILS_COLUMN = "details";
    public static final String[] CLIENT_FOLLOWUP_TABLE_COLUMNS = {ID_COLUMN, Relational_ID,  FACILITY_ID,
            VISIT_DATE,REFERRAL_DATE,REFERRAL_STATUS,REFERRAL_REASON,REFERRAL_FEEDBACK,
            SERVICE_PROVIDER_UUID,IS_VALID,DETAILS_COLUMN};
    

    @Override
    protected void onCreate(SQLiteDatabase database) {
        database.execSQL(CHILD_SQL);
    }

    public void add(ClientFollowup clientFollowup) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.insert(CLIENT_FOLLOWUP, null, createValuesFor(clientFollowup));
    }

    public void update(ClientFollowup clientFollowup) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.update(CLIENT_FOLLOWUP, createValuesFor(clientFollowup), ID_COLUMN + " = ?", new String[]{clientFollowup.getId()});
    }

    public List<ClientFollowup> all() {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(CLIENT_FOLLOWUP, CLIENT_FOLLOWUP_TABLE_COLUMNS, null, null, null, null, null);
        return readAll(cursor);
    }

    public ClientFollowup find(String caseId) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(CLIENT_FOLLOWUP, CLIENT_FOLLOWUP_TABLE_COLUMNS, ID_COLUMN + " = ?", new String[]{caseId}, null, null, null, null);
        List<ClientFollowup> children = readAll(cursor);

        if (children.isEmpty()) {
            return null;
        }
        return children.get(0);
    }

    public List<ClientFollowup> findClientReferralByCaseIds(String... caseIds) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.rawQuery(String.format("SELECT * FROM %s WHERE %s IN (%s)", CLIENT_FOLLOWUP, ID_COLUMN, insertPlaceholdersForInClause(caseIds.length)), caseIds);
        return readAll(cursor);
    }
    public ClientFollowup findByServiceStatus(String name) {

        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(CLIENT_FOLLOWUP, CLIENT_FOLLOWUP_TABLE_COLUMNS, REFERRAL_STATUS + " = ?", new String[]{name}, null, null, null, null);
        List<ClientFollowup> children = readAll(cursor);

        if (children.isEmpty()) {
            return null;
        }
        return children.get(0);
    }
    public void updateStatus(String caseId, String name ){
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(REFERRAL_STATUS, name);
        database.update(CLIENT_FOLLOWUP, values, ID_COLUMN + " = ?", new String[]{caseId});
    }

    public List<ClientFollowup> findByServiceCaseId(String caseId) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(CLIENT_FOLLOWUP, CLIENT_FOLLOWUP_TABLE_COLUMNS, ID_COLUMN + " = ?", new String[]{caseId}, null, null, null, null);
        return readAll(cursor);
    }

    public long count() {
        return longForQuery(masterRepository.getReadableDatabase(), "SELECT COUNT(1) FROM " + CLIENT_FOLLOWUP, new String[0]);
    }

    public long unsuccesfulcount() {
        return longForQuery(masterRepository.getReadableDatabase(), "SELECT COUNT(1) FROM " + CLIENT_FOLLOWUP
                        + " WHERE " + REFERRAL_STATUS + " = ? ",
                new String[]{"0"});
    }
    public long succesfulcount() {
        return longForQuery(masterRepository.getReadableDatabase(), "SELECT COUNT(1) FROM " + CLIENT_FOLLOWUP
                        + " WHERE " + REFERRAL_STATUS + " = ? ",
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


    public ContentValues createValuesFor(ClientFollowup clientFollowup) {

        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, clientFollowup.getId());
        values.put(Relational_ID, clientFollowup.getRelationalid());
        values.put(CLIENT_ID, clientFollowup.getClient_id());
        values.put(REFERRAL_DATE, clientFollowup.getReferral_date());
        values.put(VISIT_DATE, clientFollowup.getVisit_date());
        values.put(FACILITY_ID, clientFollowup.getFacility_id());
        values.put(REFERRAL_REASON, clientFollowup.getReferral_reason());
        values.put(REFERRAL_FEEDBACK, clientFollowup.getReferral_feedback());
        values.put(REFERRAL_STATUS, clientFollowup.getReferral_status());
        values.put(SERVICE_PROVIDER_UUID, clientFollowup.getService_provider_uuid());
        values.put(IS_VALID, clientFollowup.getIs_valid());;

        values.put(DETAILS_COLUMN, new Gson().toJson(clientFollowup));
        return values;
    }


    public ContentValues createValuesUpdateValues(ClientFollowup clientFollowup) {

        ContentValues values = new ContentValues();
        values.put(REFERRAL_STATUS, clientFollowup.getReferral_status());
        values.put(DETAILS_COLUMN, new Gson().toJson(clientFollowup));
        return values;
    }


    private List<ClientFollowup> readAll(Cursor cursor) {
        cursor.moveToFirst();
        List<ClientFollowup> referralServicesListDataModel = new ArrayList<ClientFollowup>();
        while (!cursor.isAfterLast()) {
            referralServicesListDataModel.add(clientFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return referralServicesListDataModel;
    }

    public Cursor RawCustomQueryForAdapter(String query) {
        Log.i(getClass().getName(), query);
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }



    private ClientFollowup clientFromCursor(Cursor cursor) {
        ClientFollowup clientFollowup = new ClientFollowup();
        clientFollowup.setId(getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, ID_COLUMN));
        clientFollowup.setClient_id( getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, CLIENT_ID));
        clientFollowup.setReferral_reason( getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, REFERRAL_REASON));
        clientFollowup.setFacility_id( getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, FACILITY_ID));
        clientFollowup.setReferral_status( getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, REFERRAL_STATUS));
        clientFollowup.setService_provider_uuid( getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, SERVICE_PROVIDER_UUID));
        clientFollowup.setReferral_date(Long.valueOf(getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, REFERRAL_DATE)));
        clientFollowup.setVisit_date(Long.valueOf(getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, VISIT_DATE)));
        clientFollowup.setReferral_feedback(getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, REFERRAL_FEEDBACK));
        clientFollowup.setIs_valid(getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, IS_VALID));

        return clientFollowup;
    }

    private String getColumnValueByAlias(Cursor cursor, String table, String column) {
        return cursor.getString(cursor.getColumnIndex(column));
    }

    private String insertPlaceholdersForInClause(int length) {
        return repeat("?", ",", length);
    }


    public void delete(String childId) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.delete(CLIENT_FOLLOWUP, ID_COLUMN + "= ?", new String[]{childId});
    }
}
