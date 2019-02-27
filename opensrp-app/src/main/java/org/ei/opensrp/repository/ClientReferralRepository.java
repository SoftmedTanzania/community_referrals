package org.ei.opensrp.repository;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;

import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.ei.opensrp.domain.ClientReferral;

import java.util.ArrayList;
import java.util.List;

import static net.sqlcipher.DatabaseUtils.longForQuery;
import static org.apache.commons.lang3.StringUtils.repeat;

public class ClientReferralRepository extends DrishtiRepository {
    private static final String CHILD_SQL = "CREATE TABLE client_referral(id VARCHAR PRIMARY KEY, " +
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
            "details VARCHAR)";
    public static final String TABLE_NAME = "client_referral";
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
    public static final String DETAILS_COLUMN = "details";
    public static final String[] CLIENT_REFERRAL_TABLE_COLUMNS = {ID_COLUMN, RELATIONAL_ID, CLIENT_ID, ReferralDate,AppointmentDate, ReferralFacility, ReferralReason, Service, ReferralStatus, IsEmergency,IS_VALID,DETAILS_COLUMN};
    

    @Override
    protected void onCreate(SQLiteDatabase database) {
        database.execSQL(CHILD_SQL);
    }

    public void add(ClientReferral clientReferral) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.insert(TABLE_NAME, null, createValuesFor(clientReferral));
    }

    public void update(ClientReferral clientReferral) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.update(TABLE_NAME, createValuesFor(clientReferral), ID_COLUMN + " = ?", new String[]{clientReferral.getId()});
    }

    public List<ClientReferral> all() {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, CLIENT_REFERRAL_TABLE_COLUMNS, null, null, null, null, null);
        return readAll(cursor);
    }

    public ClientReferral find(String caseId) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, CLIENT_REFERRAL_TABLE_COLUMNS, ID_COLUMN + " = ?", new String[]{caseId}, null, null, null, null);
        List<ClientReferral> children = readAll(cursor);

        if (children.isEmpty()) {
            return null;
        }
        return children.get(0);
    }

    public List<ClientReferral> findClientReferralByCaseIds(String... caseIds) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.rawQuery(String.format("SELECT * FROM %s WHERE %s IN (%s)", TABLE_NAME, ID_COLUMN, insertPlaceholdersForInClause(caseIds.length)), caseIds);
        return readAll(cursor);
    }
    public ClientReferral findByServiceStatus(String name) {

        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, CLIENT_REFERRAL_TABLE_COLUMNS, ReferralStatus + " = ?", new String[]{name}, null, null, null, null);
        List<ClientReferral> children = readAll(cursor);

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

    public List<ClientReferral> findByServiceCaseId(String caseId) {
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


    public ContentValues createValuesFor(ClientReferral clientReferral) {

        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, clientReferral.getId());
        values.put(RELATIONAL_ID, clientReferral.getRelationalid());
        values.put(CLIENT_ID, clientReferral.getClientId());
        values.put(ReferralDate, clientReferral.getReferral_date());
        values.put(AppointmentDate, clientReferral.getAppointment_date());
        values.put(ReferralFacility, clientReferral.getFacility_id());
        values.put(ReferralReason, clientReferral.getReferral_reason());
        values.put(Service, clientReferral.getReferral_service_id());
        values.put(ReferralStatus, clientReferral.getReferral_status());
        values.put(IS_VALID, clientReferral.getIs_valid());
        values.put(IsEmergency, clientReferral.getIs_emergency());
        values.put(DETAILS_COLUMN, new Gson().toJson(clientReferral));
        return values;
    }

    public ContentValues createValuesUpdateValues(ClientReferral clientReferral) {

        ContentValues values = new ContentValues();
        values.put(ReferralStatus, clientReferral.getReferral_status());
        values.put(DETAILS_COLUMN, new Gson().toJson(clientReferral));
        return values;
    }


    private List<ClientReferral> readAll(Cursor cursor) {
        cursor.moveToFirst();
        List<ClientReferral> referralServicesListDataModel = new ArrayList<ClientReferral>();
        while (!cursor.isAfterLast()) {
            referralServicesListDataModel.add(new ClientReferral(cursor.getString(0),
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
                    cursor.getString(11))
            );
            cursor.moveToNext();
        }
        cursor.close();
        return referralServicesListDataModel;
    }

    private List<ClientReferral> readAllChildren(Cursor cursor) {
        cursor.moveToFirst();
        List<ClientReferral> children = new ArrayList<ClientReferral>();
        while (!cursor.isAfterLast()) {
            children.add(serviceFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return children;
    }

    private ClientReferral serviceFromCursor(Cursor cursor) {
        return new ClientReferral(
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
}
