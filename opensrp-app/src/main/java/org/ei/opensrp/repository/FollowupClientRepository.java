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

public class FollowupClientRepository extends DrishtiRepository {
    private static final String CHILD_SQL = "CREATE TABLE followup_client(id VARCHAR PRIMARY KEY, relationalid VARCHAR, " +
            "first_name VARCHAR, middle_name VARCHAR, surname VARCHAR,gender VARCHAR," +
            "phone_number VARCHAR, community_based_hiv_service VARCHAR,map_cue VARCHAR, ward VARCHAR,ctc_number VARCHAR, care_taker_name VARCHAR," +
            "care_taker_phone_number VARCHAR,care_taker_relationship VARCHAR, visit_date VARCHAR,date_of_birth VARCHAR,referral_date VARCHAR, " +
            "facility_id VARCHAR, referral_reason VARCHAR, service_provider_uiid VARCHAR, referral_status VARCHAR, is_valid VARCHAR, details VARCHAR)";
    public static final String CLIENT_FOLLOWUP = "followup_client";
    public static final String ID_COLUMN = "id";
    public static final String Relational_ID = "relationalid";
    public static final String FNAME = "first_name";
    public static final String GENDER = "gender";
    public static final String MNAME = "middle_name";
    public static final String LNAME = "surname";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String CBHS = "community_based_hiv_service";
    public static final String CARE_TAKER_NAME = "care_taker_name";
    public static final String CARE_TAKER_PHONE_NUMBER = "care_taker_phone_number";
    public static final String CARE_TAKER_RELATIONSHIP = "care_taker_relationship";
    public static final String VISIT_DATE = "visit_date";
    public static final String REFERRAL_DATE = "referral_date";
    public static final String DATE_OF_BIRTH = "date_of_birth";
    public static final String MAP_CUE = "map_cue";
    public static final String WARD = "ward";
    public static final String CTCNumber = "ctc_number";
    public static final String FACILITY_ID = "facility_id";
    public static final String REFERRAL_REASON = "referral_reason";
    public static final String REFERRAL_STATUS = "referral_status";
    public static final String SERVICE_PROVIDER_UUID = "service_provider_uiid";
    public static final String IS_VALID = "is_valid";
    public static final String DETAILS_COLUMN = "details";
    public static final String[] CLIENT_FOLLOWUP_TABLE_COLUMNS = {ID_COLUMN, Relational_ID, FNAME,
            MNAME, LNAME, CBHS, CTCNumber, GENDER, PHONE_NUMBER, CARE_TAKER_NAME, FACILITY_ID,MAP_CUE,WARD,
            CARE_TAKER_PHONE_NUMBER,CARE_TAKER_RELATIONSHIP,VISIT_DATE,REFERRAL_DATE,REFERRAL_STATUS,DATE_OF_BIRTH,REFERRAL_REASON,
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
        values.put(FNAME, clientFollowup.getFirst_name());
        values.put(MNAME, clientFollowup.getMiddle_name());
        values.put(LNAME, clientFollowup.getSurname());
        values.put(GENDER, clientFollowup.getGender());
        values.put(CBHS, clientFollowup.getCommunity_based_hiv_service());
        values.put(CTCNumber, clientFollowup.getCtc_number());
        values.put(PHONE_NUMBER, clientFollowup.getPhone_number());
        values.put(MAP_CUE, clientFollowup.getMap_cue());
        values.put(WARD, clientFollowup.getWard());
        values.put(CBHS, clientFollowup.getCommunity_based_hiv_service());
        values.put(CARE_TAKER_NAME, clientFollowup.getCare_taker_name());
        values.put(CARE_TAKER_PHONE_NUMBER, clientFollowup.getCare_taker_name_phone_number());
        values.put(CARE_TAKER_RELATIONSHIP, clientFollowup.getCare_taker_relationship());
        values.put(VISIT_DATE, clientFollowup.getVisit_date());
        values.put(REFERRAL_DATE, clientFollowup.getReferral_date());
        values.put(DATE_OF_BIRTH, clientFollowup.getDate_of_birth());
        values.put(FACILITY_ID, clientFollowup.getFacility_id());
        values.put(REFERRAL_REASON, clientFollowup.getReferral_reason());
        values.put(REFERRAL_STATUS, clientFollowup.getReferral_status());
        values.put(SERVICE_PROVIDER_UUID, clientFollowup.getService_provider_uiid());
        values.put(IS_VALID, clientFollowup.getIs_valid());

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
        clientFollowup.setFirst_name( getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, FNAME));
        clientFollowup.setMiddle_name( getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, MNAME));
        clientFollowup.setSurname( getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, LNAME));
        clientFollowup.setGender( getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, GENDER));
        clientFollowup.setPhone_number( getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, PHONE_NUMBER));
        clientFollowup.setCommunity_based_hiv_service( getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, CBHS));
        clientFollowup.setMap_cue( getColumnValueByAlias(cursor, CLIENT_FOLLOWUP,MAP_CUE ));
        clientFollowup.setWard( getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, WARD));
        clientFollowup.setReferral_reason( getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, REFERRAL_REASON));
        clientFollowup.setCare_taker_name( getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, CARE_TAKER_NAME));
        clientFollowup.setCare_taker_name_phone_number( getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, CARE_TAKER_PHONE_NUMBER));
        clientFollowup.setCare_taker_relationship( getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, CARE_TAKER_RELATIONSHIP));
        clientFollowup.setCtc_number( getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, CTCNumber));
        clientFollowup.setFacility_id( getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, FACILITY_ID));
        clientFollowup.setReferral_status( getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, REFERRAL_STATUS));
        clientFollowup.setService_provider_uiid( getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, SERVICE_PROVIDER_UUID));



        clientFollowup.setReferral_date(Long.valueOf(getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, REFERRAL_DATE)));
        clientFollowup.setVisit_date(Long.valueOf(getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, VISIT_DATE)));
        clientFollowup.setDate_of_birth(Long.valueOf(getColumnValueByAlias(cursor, CLIENT_FOLLOWUP, DATE_OF_BIRTH)));

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
