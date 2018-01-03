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
    private static final String CHILD_SQL = "CREATE TABLE ClientReferral(id VARCHAR PRIMARY KEY, relationalid VARCHAR, first_name VARCHAR, middle_name VARCHAR, surname VARCHAR, community_based_hiv_service VARCHAR, ctc_number VARCHAR, referral_date VARCHAR, facility_id VARCHAR, referral_reason VARCHAR, referral_service_id VARCHAR, referral_status VARCHAR, is_valid VARCHAR, details VARCHAR)";
    public static final String CLIENT_REFERRAL = "client_referral";
    public static final String ID_COLUMN = "id";
    public static final String Relational_ID = "relationalid";
    public static final String fName = "first_name";
    public static final String mName = "middle_name";
    public static final String lName = "surname";
    public static final String CBHS = "community_based_hiv_service";
    public static final String CTCNumber = "ctc_number";
    public static final String ReferralDate = "referral_date";
    public static final String ReferralFacility = "facility_id";
    public static final String ReferralReason = "referral_reason";
    public static final String Service = "referral_service_id";
    public static final String Status = "referral_status";
    public static final String IS_VALID = "is_valid";
    public static final String DETAILS_COLUMN = "details";
    public static final String[] CLIENT_REFERRAL_TABLE_COLUMNS = {ID_COLUMN, Relational_ID, fName, mName, lName, CBHS, CTCNumber, ReferralDate, ReferralFacility, ReferralReason, Service, Status, IS_VALID,DETAILS_COLUMN};
    

    @Override
    protected void onCreate(SQLiteDatabase database) {
        database.execSQL(CHILD_SQL);
    }

    public void add(ClientReferral clientReferral) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.insert(CLIENT_REFERRAL, null, createValuesFor(clientReferral));
    }

    public void update(ClientReferral clientReferral) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.update(CLIENT_REFERRAL, createValuesFor(clientReferral), ID_COLUMN + " = ?", new String[]{clientReferral.getId()});
    }

    public List<ClientReferral> all() {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(CLIENT_REFERRAL, CLIENT_REFERRAL_TABLE_COLUMNS, null, null, null, null, null);
        return readAll(cursor);
    }

    public ClientReferral find(String caseId) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(CLIENT_REFERRAL, CLIENT_REFERRAL_TABLE_COLUMNS, ID_COLUMN + " = ?", new String[]{caseId}, null, null, null, null);
        List<ClientReferral> children = readAll(cursor);

        if (children.isEmpty()) {
            return null;
        }
        return children.get(0);
    }

    public List<ClientReferral> findClientReferralByCaseIds(String... caseIds) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.rawQuery(String.format("SELECT * FROM %s WHERE %s IN (%s)", CLIENT_REFERRAL, ID_COLUMN, insertPlaceholdersForInClause(caseIds.length)), caseIds);
        return readAll(cursor);
    }
    public ClientReferral findByServiceStatus(String name) {

        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(CLIENT_REFERRAL, CLIENT_REFERRAL_TABLE_COLUMNS, Status + " = ?", new String[]{name}, null, null, null, null);
        List<ClientReferral> children = readAll(cursor);

        if (children.isEmpty()) {
            return null;
        }
        return children.get(0);
    }
    public void updateStatus(String caseId, String name ){
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Status, name);
        database.update(CLIENT_REFERRAL, values, ID_COLUMN + " = ?", new String[]{caseId});
    }

    public List<ClientReferral> findByServiceCaseId(String caseId) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(CLIENT_REFERRAL, CLIENT_REFERRAL_TABLE_COLUMNS, ID_COLUMN + " = ?", new String[]{caseId}, null, null, null, null);
        return readAll(cursor);
    }

    public long count() {
        return longForQuery(masterRepository.getReadableDatabase(), "SELECT COUNT(1) FROM " + CLIENT_REFERRAL, new String[0]);
    }

    public long unsuccesfulcount() {
        return longForQuery(masterRepository.getReadableDatabase(), "SELECT COUNT(1) FROM " + CLIENT_REFERRAL
               , new String[0]);
    }
    public long succesfulcount() {
        return longForQuery(masterRepository.getReadableDatabase(), "SELECT COUNT(1) FROM " + CLIENT_REFERRAL
                + " WHERE " + Status + " = '1' and is_valid = 'true'", new String[0]);
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


    public ContentValues createValuesFor(ClientReferral clientReferralPersonObject) {

        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, clientReferralPersonObject.getId());
        values.put(Relational_ID, clientReferralPersonObject.getRelationalid());
        values.put(fName, clientReferralPersonObject.getFirst_name());
        values.put(mName, clientReferralPersonObject.getMiddle_name());
        values.put(lName, clientReferralPersonObject.getSurname());
        values.put(CBHS, clientReferralPersonObject.getCommunity_based_hiv_service());
        values.put(CTCNumber, clientReferralPersonObject.getCtc_number());
        values.put(ReferralDate, clientReferralPersonObject.getReferral_date());
        values.put(ReferralFacility, clientReferralPersonObject.getFacility_id());
        values.put(ReferralReason, clientReferralPersonObject.getReferral_reason());
        values.put(Service, clientReferralPersonObject.getReferral_service_id());
        values.put(Status, clientReferralPersonObject.getStatus());
        values.put(IS_VALID, clientReferralPersonObject.getIs_valid());
        values.put(DETAILS_COLUMN, new Gson().toJson(clientReferralPersonObject));
        return values;
    }
    private List<ClientReferral> readAll(Cursor cursor) {
        cursor.moveToFirst();
        List<ClientReferral> referralServicesListDataModel = new ArrayList<ClientReferral>();
        while (!cursor.isAfterLast()) {
            referralServicesListDataModel.add(new ClientReferral(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getLong(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13))

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
                getColumnValueByAlias(cursor, CLIENT_REFERRAL, ID_COLUMN),
                getColumnValueByAlias(cursor, CLIENT_REFERRAL, Relational_ID),
                getColumnValueByAlias(cursor, CLIENT_REFERRAL, fName),
                getColumnValueByAlias(cursor, CLIENT_REFERRAL, mName),
                getColumnValueByAlias(cursor, CLIENT_REFERRAL, lName),
                getColumnValueByAlias(cursor, CLIENT_REFERRAL, CBHS),
                getColumnValueByAlias(cursor, CLIENT_REFERRAL, CTCNumber),
                Long.parseLong(getColumnValueByAlias(cursor, CLIENT_REFERRAL, ReferralDate)),
                getColumnValueByAlias(cursor, CLIENT_REFERRAL, ReferralFacility),
                getColumnValueByAlias(cursor, CLIENT_REFERRAL, ReferralReason),
                getColumnValueByAlias(cursor, CLIENT_REFERRAL, Service),
                getColumnValueByAlias(cursor, CLIENT_REFERRAL, Status),
                getColumnValueByAlias(cursor, CLIENT_REFERRAL, IS_VALID),
                getColumnValueByAlias(cursor, CLIENT_REFERRAL, DETAILS_COLUMN));

    }

    private String getColumnValueByAlias(Cursor cursor, String table, String column) {
        return cursor.getString(cursor.getColumnIndex(table + column));
    }

    private String insertPlaceholdersForInClause(int length) {
        return repeat("?", ",", length);
    }


    public void delete(String childId) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.delete(CLIENT_REFERRAL, ID_COLUMN + "= ?", new String[]{childId});
    }
}
