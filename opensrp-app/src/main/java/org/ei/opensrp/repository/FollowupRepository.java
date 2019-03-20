package org.ei.opensrp.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;

import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.ei.opensrp.domain.Followup;

import java.util.ArrayList;
import java.util.List;

import static net.sqlcipher.DatabaseUtils.longForQuery;
import static org.apache.commons.lang3.StringUtils.repeat;

public class FollowupRepository extends DrishtiRepository {
    private static final String CHILD_SQL = "CREATE TABLE followup(id VARCHAR PRIMARY KEY, " +
            "relationalid VARCHAR, " +
            "client_id VARCHAR, " +
            "referral_id VARCHAR," +
            "referral_feedback_id VARCHAR, " +
            "other_notes VARCHAR, " +
            "followup_date VARCHAR, " +
            "details VARCHAR)";
    public static final String TABLE_NAME = "followup";
    public static final String ID_COLUMN = "id";
    public static final String RELATIONAL_ID = "relationalid";
    public static final String CLIENT_ID = "client_id";
    public static final String REFERRAL_ID = "referral_id";
    public static final String REFERRAL_FEEDBACK_ID = "referral_feedback_id";
    public static final String OTHER_NOTES = "other_notes";
    public static final String FOLLOWUP_DATE = "followup_date";
    public static final String DETAILS_COLUMN = "details";
    public static final String[] CLIENT_REFERRAL_TABLE_COLUMNS = {ID_COLUMN, RELATIONAL_ID, CLIENT_ID, REFERRAL_ID,REFERRAL_FEEDBACK_ID,OTHER_NOTES,FOLLOWUP_DATE,DETAILS_COLUMN};
    

    @Override
    protected void onCreate(SQLiteDatabase database) {
        database.execSQL(CHILD_SQL);
    }

    public void add(Followup followup) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.insert(FollowupRepository.TABLE_NAME, null, createValuesFor(followup));
    }

    public void update(Followup followup) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.update(TABLE_NAME, createValuesFor(followup), ID_COLUMN + " = ?", new String[]{followup.getId()});
    }

    public List<Followup> all() {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, CLIENT_REFERRAL_TABLE_COLUMNS, null, null, null, null, null);
        return readAll(cursor);
    }

    public Followup find(String caseId) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, CLIENT_REFERRAL_TABLE_COLUMNS, ID_COLUMN + " = ?", new String[]{caseId}, null, null, null, null);
        List<Followup> children = readAll(cursor);

        if (children.isEmpty()) {
            return null;
        }
        return children.get(0);
    }

    public List<Followup> findByClientId(String caseId) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, CLIENT_REFERRAL_TABLE_COLUMNS, CLIENT_ID + " = ?", new String[]{caseId}, null, null, null, null);
        List<Followup> children = readAll(cursor);


        return children;
    }


    public List<Followup> findClientReferralByCaseIds(String... caseIds) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.rawQuery(String.format("SELECT * FROM %s WHERE %s IN (%s)", TABLE_NAME, ID_COLUMN, insertPlaceholdersForInClause(caseIds.length)), caseIds);
        return readAll(cursor);
    }
   
    public List<Followup> findByServiceCaseId(String caseId) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, CLIENT_REFERRAL_TABLE_COLUMNS, ID_COLUMN + " = ?", new String[]{caseId}, null, null, null, null);
        return readAll(cursor);
    }

    public long count() {
        return longForQuery(masterRepository.getReadableDatabase(), "SELECT COUNT(1) FROM " + TABLE_NAME, new String[0]);
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


    public ContentValues createValuesFor(Followup followup) {

        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, followup.getId());
        values.put(RELATIONAL_ID, followup.getRelationalid());
        values.put(CLIENT_ID, followup.getClient_id());
        values.put(REFERRAL_ID, followup.getReferral_id());
        values.put(REFERRAL_FEEDBACK_ID, followup.getReferral_feedback_id());
        values.put(OTHER_NOTES, followup.getOther_notes());
        values.put(DETAILS_COLUMN, new Gson().toJson(followup));
        return values;
    }


    private List<Followup> readAll(Cursor cursor) {
        cursor.moveToFirst();
        List<Followup> referralServicesListDataModel = new ArrayList<Followup>();
        while (!cursor.isAfterLast()) {
            referralServicesListDataModel.add(new Followup(cursor.getString(0),
                    cursor.getString(1),
                    cursor.getLong(2),
                    cursor.getLong(3),
                    cursor.getLong(4),
                    cursor.getString(5),
                    cursor.getLong(6),
                    cursor.getString(7))
            );
            cursor.moveToNext();
        }
        cursor.close();
        return referralServicesListDataModel;
    }

    private List<Followup> readAllChildren(Cursor cursor) {
        cursor.moveToFirst();
        List<Followup> children = new ArrayList<Followup>();
        while (!cursor.isAfterLast()) {
            children.add(serviceFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return children;
    }

    private Followup serviceFromCursor(Cursor cursor) {
        return new Followup(
                getColumnValueByAlias(cursor, TABLE_NAME, ID_COLUMN),
                getColumnValueByAlias(cursor, TABLE_NAME, RELATIONAL_ID),
                Long.parseLong(getColumnValueByAlias(cursor, TABLE_NAME, CLIENT_ID)),
                Long.parseLong(getColumnValueByAlias(cursor, TABLE_NAME, REFERRAL_ID)),
                Long.parseLong(getColumnValueByAlias(cursor, TABLE_NAME, REFERRAL_FEEDBACK_ID)),
                getColumnValueByAlias(cursor, TABLE_NAME, OTHER_NOTES),
                Long.parseLong(getColumnValueByAlias(cursor, TABLE_NAME, FOLLOWUP_DATE)),
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
