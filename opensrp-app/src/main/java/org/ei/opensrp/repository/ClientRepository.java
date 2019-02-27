package org.ei.opensrp.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;

import net.sqlcipher.database.SQLiteDatabase;

import org.ei.opensrp.domain.Client;

import java.util.ArrayList;
import java.util.List;

import static net.sqlcipher.DatabaseUtils.longForQuery;
import static org.apache.commons.lang3.StringUtils.repeat;

public class ClientRepository extends DrishtiRepository {
    private static final String CHILD_SQL = "CREATE TABLE client(id VARCHAR PRIMARY KEY, " +
            "relationalid VARCHAR, " +
            "client_id VARCHAR, " +
            "first_name VARCHAR, " +
            "middle_name VARCHAR, " +
            "surname VARCHAR," +
            "date_of_birth VARCHAR," +
            "gender VARCHAR, " +
            "community_based_hiv_service VARCHAR, " +
            "ctc_number VARCHAR, " +
            "facility_id VARCHAR, " +
            "is_valid VARCHAR, " +
            "helper_name VARCHAR, " +
            "helper_phone_number VARCHAR, " +
            "status VARCHAR, " +
            "phone_number VARCHAR, " +
            "village VARCHAR, " +
            "village_leader VARCHAR, " +
            "ward VARCHAR, " +
            "details VARCHAR)";
    public static final String TABLE_NAME = "client";
    public static final String ID_COLUMN = "id";
    public static final String Relational_ID = "relationalid";
    public static final String ClientId = "client_id";
    public static final String fName = "first_name";
    public static final String gender = "gender";
    public static final String mName = "middle_name";
    public static final String Surname = "surname";
    public static final String DOB = "date_of_birth";
    public static final String CBHS = "community_based_hiv_service";
    public static final String CTC_NUMBER = "ctc_number";
    public static final String FACILITY_ID = "facility_id";
    public static final String HELPER_NAME = "helper_name";
    public static final String HELPER_PHONE_NUMBER = "helper_phone_number";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String IS_VALID = "is_valid";
    public static final String STATUS = "status";
    public static final String VILLAGE = "village";
    public static final String WARD = "ward";
    public static final String VILLAGE_LEADER = "village_leader";
    public static final String DETAILS_COLUMN = "details";
    public static final String[] CLIENT_TABLE_COLUMNS = {ID_COLUMN, Relational_ID,ClientId, fName, mName, Surname, DOB, CBHS, CTC_NUMBER,gender, FACILITY_ID, HELPER_NAME, HELPER_PHONE_NUMBER,IS_VALID, STATUS,PHONE_NUMBER,VILLAGE,VILLAGE_LEADER,WARD,DETAILS_COLUMN};
    

    @Override
    protected void onCreate(SQLiteDatabase database) {
        database.execSQL(CHILD_SQL);
    }

    public void add(Client client) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.insert(TABLE_NAME, null, createValuesFor(client));
    }

    public void update(Client client) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.update(TABLE_NAME, createValuesFor(client), ID_COLUMN + " = ?", new String[]{client.getId()});
    }

    public List<Client> all() {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, CLIENT_TABLE_COLUMNS, null, null, null, null, null);
        return readAll(cursor);
    }

    public Client find(String caseId) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, CLIENT_TABLE_COLUMNS, ID_COLUMN + " = ?", new String[]{caseId}, null, null, null, null);
        List<Client> children = readAll(cursor);

        if (children.isEmpty()) {
            return null;
        }
        return children.get(0);
    }

    public List<Client> findClientReferralByCaseIds(String... caseIds) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.rawQuery(String.format("SELECT * FROM %s WHERE %s IN (%s)", TABLE_NAME, ID_COLUMN, insertPlaceholdersForInClause(caseIds.length)), caseIds);
        return readAll(cursor);
    }

    public long count() {
        return longForQuery(masterRepository.getReadableDatabase(), "SELECT COUNT(1) FROM " + TABLE_NAME, new String[0]);
    }

    private String[] prepend(String[] input, String tableName) {
        int length = input.length;
        String[] output = new String[length];
        for (int index = 0; index < length; index++) {
            output[index] = tableName + "." + input[index] + " as " + tableName + input[index];
        }
        return output;
    }


    public ContentValues createValuesFor(Client client) {

        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, client.getId());
        values.put(Relational_ID, client.getRelationalid());
        values.put(ClientId, client.getClient_id());
        values.put(fName, client.getFirst_name());
        values.put(mName, client.getMiddle_name());
        values.put(Surname, client.getSurname());
        values.put(DOB, client.getDate_of_birth());
        values.put(DETAILS_COLUMN, new Gson().toJson(client));
        values.put(CBHS, client.getCommunity_based_hiv_service());
        values.put(CTC_NUMBER, client.getCtc_number());
        values.put(FACILITY_ID, client.getFacility_id());
        values.put(IS_VALID, client.getIs_valid());
        values.put(HELPER_NAME, client.getHelper_name());
        values.put(HELPER_PHONE_NUMBER, client.getHelper_phone_number());
        values.put(FACILITY_ID, client.getFacility_id());
        values.put(gender, client.getGender());
        values.put(VILLAGE, client.getVillage());
        values.put(VILLAGE_LEADER, client.getVillage_leader());
        values.put(PHONE_NUMBER, client.getPhone_number());
        values.put(WARD, client.getWard());
        return values;
    }


    private List<Client> readAll(Cursor cursor) {
        cursor.moveToFirst();
        List<Client> referralServicesListDataModel = new ArrayList<Client>();
        while (!cursor.isAfterLast()) {
            referralServicesListDataModel.add(new Client(cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getLong(6),
                    cursor.getString(7),
                    cursor.getString(8), cursor.getString(9),
                    cursor.getString(10),
                    cursor.getString(11),
                    cursor.getString(12),
                    cursor.getString(13),
                    cursor.getLong(14),
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

    private List<Client> readAllChildren(Cursor cursor) {
        cursor.moveToFirst();
        List<Client> children = new ArrayList<Client>();
        while (!cursor.isAfterLast()) {
            children.add(serviceFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return children;
    }

    public List<Client> RawCustomQueryForAdapter(String query) {
        Log.i(getClass().getName(), query);
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);

        List<Client> clients = new ArrayList<Client>();
        while (!cursor.isAfterLast()) {
            clients.add(new Client(cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getLong(6),
                    cursor.getString(7),
                    cursor.getString(8), cursor.getString(9),
                    cursor.getString(10),
                    cursor.getString(11),
                    cursor.getString(12),
                    cursor.getString(13),
                    cursor.getLong(14),
                    cursor.getString(15),
                    cursor.getString(16),
                    cursor.getString(17),
                    cursor.getString(18),
                    cursor.getString(19))

            );
            cursor.moveToNext();
        }
        cursor.close();

        return clients;
    }

    private Client serviceFromCursor(Cursor cursor) {
        return new Client(
                getColumnValueByAlias(cursor, TABLE_NAME, ID_COLUMN),
                getColumnValueByAlias(cursor, TABLE_NAME, Relational_ID),
                getColumnValueByAlias(cursor, TABLE_NAME, ClientId),
                getColumnValueByAlias(cursor, TABLE_NAME, fName),
                getColumnValueByAlias(cursor, TABLE_NAME, mName),
                getColumnValueByAlias(cursor, TABLE_NAME, Surname),
                Long.parseLong(getColumnValueByAlias(cursor, TABLE_NAME, DOB)),
                getColumnValueByAlias(cursor, TABLE_NAME, gender),
                getColumnValueByAlias(cursor, TABLE_NAME, CBHS),
                getColumnValueByAlias(cursor, TABLE_NAME, CTC_NUMBER),
                getColumnValueByAlias(cursor, TABLE_NAME, FACILITY_ID),
                getColumnValueByAlias(cursor, TABLE_NAME, IS_VALID),
                getColumnValueByAlias(cursor, TABLE_NAME, HELPER_NAME),
                getColumnValueByAlias(cursor, TABLE_NAME, HELPER_PHONE_NUMBER),
                Long.parseLong(getColumnValueByAlias(cursor, TABLE_NAME, STATUS)),
                getColumnValueByAlias(cursor, TABLE_NAME, PHONE_NUMBER),
                getColumnValueByAlias(cursor, TABLE_NAME, VILLAGE),
                getColumnValueByAlias(cursor, TABLE_NAME, VILLAGE_LEADER),
                getColumnValueByAlias(cursor, TABLE_NAME, WARD),
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
