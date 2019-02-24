package org.ei.opensrp.repository;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;

import net.sqlcipher.database.SQLiteDatabase;

import org.ei.opensrp.domain.Client;
import org.opensrp.api.constants.Gender;

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
            "care_taker_name VARCHAR, " +
            "care_taker_phone_number VARCHAR, " +
            "care_taker_relationship VARCHAR, " +
            "details VARCHAR)";
    public static final String CLIENT = "client";
    public static final String ID_COLUMN = "id";
    public static final String Relational_ID = "relationalid";
    public static final String ClientId = "client_id";
    public static final String fName = "first_name";
    public static final String gender = "gender";
    public static final String mName = "middle_name";
    public static final String Surname = "surname";
    public static final String dob = "date_of_birth";
    public static final String CBHS = "community_based_hiv_service";
    public static final String CTCNumber = "ctc_number";
    public static final String ReferralFacility = "facility_id";
    public static final String CareTakerName = "care_taker_name";
    public static final String CareTakerPhoneNumber = "care_taker_phone_number";
    public static final String CareTakerRelationship = "care_taker_relationship";
    public static final String IS_VALID = "is_valid";
    public static final String DETAILS_COLUMN = "details";
    public static final String[] CLIENT_TABLE_COLUMNS = {ID_COLUMN, Relational_ID,ClientId, fName, mName, Surname,dob, CBHS, CTCNumber,gender, ReferralFacility,CareTakerName,CareTakerPhoneNumber,CareTakerRelationship,IS_VALID,DETAILS_COLUMN};
    

    @Override
    protected void onCreate(SQLiteDatabase database) {
        database.execSQL(CHILD_SQL);
    }

    public void add(Client client) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.insert(CLIENT, null, createValuesFor(client));
    }

    public void update(Client client) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.update(CLIENT, createValuesFor(client), ID_COLUMN + " = ?", new String[]{client.getId()});
    }

    public List<Client> all() {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(CLIENT, CLIENT_TABLE_COLUMNS, null, null, null, null, null);
        return readAll(cursor);
    }

    public Client find(String caseId) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(CLIENT, CLIENT_TABLE_COLUMNS, ID_COLUMN + " = ?", new String[]{caseId}, null, null, null, null);
        List<Client> children = readAll(cursor);

        if (children.isEmpty()) {
            return null;
        }
        return children.get(0);
    }

    public List<Client> findClientReferralByCaseIds(String... caseIds) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.rawQuery(String.format("SELECT * FROM %s WHERE %s IN (%s)", CLIENT, ID_COLUMN, insertPlaceholdersForInClause(caseIds.length)), caseIds);
        return readAll(cursor);
    }

    public long count() {
        return longForQuery(masterRepository.getReadableDatabase(), "SELECT COUNT(1) FROM " + CLIENT, new String[0]);
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
        values.put(dob, client.getDate_of_birth());
        values.put(DETAILS_COLUMN, new Gson().toJson(client));
        values.put(CBHS, client.getCommunity_based_hiv_service());
        values.put(CTCNumber, client.getCtc_number());
        values.put(ReferralFacility, client.getFacility_id());
        values.put(IS_VALID, client.getIs_valid());
        values.put(CareTakerName, client.getCare_taker_name());
        values.put(CareTakerPhoneNumber, client.getCare_taker_phone_number());
        values.put(CareTakerRelationship, client.getCare_taker_relationship());
        values.put(ReferralFacility, client.getFacility_id());
        values.put(gender, client.getGender());
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
                    cursor.getString(15),
                    cursor.getString(16))

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

    private Client serviceFromCursor(Cursor cursor) {
        return new Client(
                getColumnValueByAlias(cursor, CLIENT, ID_COLUMN),
                getColumnValueByAlias(cursor, CLIENT, Relational_ID),
                getColumnValueByAlias(cursor, CLIENT, ClientId),
                getColumnValueByAlias(cursor, CLIENT, fName),
                getColumnValueByAlias(cursor, CLIENT, mName),
                getColumnValueByAlias(cursor, CLIENT, Surname),
                Long.parseLong(getColumnValueByAlias(cursor, CLIENT, dob)),
                getColumnValueByAlias(cursor, CLIENT, gender),
                getColumnValueByAlias(cursor, CLIENT, CBHS),
                getColumnValueByAlias(cursor, CLIENT, CTCNumber),
                getColumnValueByAlias(cursor, CLIENT, ReferralFacility),
                getColumnValueByAlias(cursor, CLIENT, IS_VALID),
                getColumnValueByAlias(cursor, CLIENT, CareTakerName),
                getColumnValueByAlias(cursor, CLIENT, CareTakerPhoneNumber),
                getColumnValueByAlias(cursor, CLIENT, CareTakerRelationship),
                getColumnValueByAlias(cursor, CLIENT, DETAILS_COLUMN));

    }

    private String getColumnValueByAlias(Cursor cursor, String table, String column) {
        return cursor.getString(cursor.getColumnIndex(table + column));
    }

    private String insertPlaceholdersForInClause(int length) {
        return repeat("?", ",", length);
    }


    public void delete(String childId) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.delete(CLIENT, ID_COLUMN + "= ?", new String[]{childId});
    }
}
