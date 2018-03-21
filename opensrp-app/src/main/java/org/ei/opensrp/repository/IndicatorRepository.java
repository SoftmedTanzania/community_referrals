package org.ei.opensrp.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.ei.opensrp.domain.Indicator;
import org.ei.opensrp.domain.Indicator;

import java.util.ArrayList;
import java.util.List;

import static net.sqlcipher.DatabaseUtils.longForQuery;
import static org.apache.commons.lang3.StringUtils.repeat;

public class IndicatorRepository extends DrishtiRepository {
    private static final String TAG = IndicatorRepository.class.getSimpleName();
    private static final String TABLE_NAME = "indicator";
    private static final String CHILD_SQL = "CREATE TABLE indicator(" +
            "referralIndicatorId INTEGER NOT NULL," +
            "indicatorName VARCHAR," +
            "isActive VARCHAR," +
            "referralServiceIndicatorId INTEGER NOT NULL," +
            "PRIMARY KEY (referralIndicatorId, referralServiceIndicatorId)" +
            ")";
    public static final String INDICATOR = "indicator";
    private static final String REFERRAL_SERVICE_INDICATOR_ID = "referralServiceIndicatorId";
    private static final String INDICATOR_NAME = "indicatorName";
    private static final String REFERRAL_INDICATOR_ID = "referralIndicatorId";
    private static final String IS_ACTIVE = "isActive";
    public static final String[] REFERRAL_SERVICE_TABLE_COLUMNS = {REFERRAL_SERVICE_INDICATOR_ID, REFERRAL_INDICATOR_ID, IS_ACTIVE, INDICATOR_NAME};
    public static final String NOT_CLOSED = "false";

    @Override
    protected void onCreate(SQLiteDatabase database) {
        database.execSQL(CHILD_SQL);
        Log.d(TAG,"Indicator repository created successfully");
    }

    public void add(Indicator referralServiceDataModel) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.insert(INDICATOR, null, createValuesFor(referralServiceDataModel));
        Log.d(TAG,"data base created successfully");
    }

    public void update(Indicator referralServiceDataModel) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.update(INDICATOR, createValuesFor(referralServiceDataModel), REFERRAL_SERVICE_INDICATOR_ID + " = ?", new String[]{referralServiceDataModel.getReferralIndicatorId()});
    }

    public List<Indicator> all() {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(INDICATOR, REFERRAL_SERVICE_TABLE_COLUMNS, null, null, null, null, null);
        return readAll(cursor);
    }

    public Indicator find(String caseId) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(INDICATOR, REFERRAL_SERVICE_TABLE_COLUMNS, REFERRAL_INDICATOR_ID + " = ?", new String[]{caseId}, null, null, null, null);
        List<Indicator> children = readAll(cursor);

        if (children.isEmpty()) {
            return null;
        }
        return children.get(0);
    }

    public List<Indicator> findServiceByCaseIds(String... caseIds) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.rawQuery(String.format("SELECT * FROM %s WHERE %s IN (%s)", INDICATOR, REFERRAL_SERVICE_INDICATOR_ID, insertPlaceholdersForInClause(caseIds.length)), caseIds);
        return readAll(cursor);
    }

    public void updateName(String caseId, String name ){
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(INDICATOR_NAME, name);
        database.update(INDICATOR, values, REFERRAL_SERVICE_INDICATOR_ID + " = ?", new String[]{caseId});
    }

    public Indicator findByServiceName(String name) {

        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(INDICATOR, REFERRAL_SERVICE_TABLE_COLUMNS, INDICATOR_NAME + " = ?", new String[]{name}, null, null, null, null);
        List<Indicator> children = readAll(cursor);

        if (children.isEmpty()) {
            return null;
        }
        return children.get(0);
    }

    public long count() {
        return longForQuery(masterRepository.getReadableDatabase(), "SELECT COUNT(1) FROM " + INDICATOR, new String[0]);
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



    public ContentValues createValuesFor(Indicator indicator) {
        ContentValues values = new ContentValues();
        values.put(REFERRAL_SERVICE_INDICATOR_ID, indicator.getReferralServiceIndicatorId());
        values.put(INDICATOR_NAME, indicator.getIndicatorName());
        values.put(REFERRAL_INDICATOR_ID, indicator.getReferralIndicatorId());
        values.put(IS_ACTIVE, indicator.getIsActive());
        Log.d(TAG,"values"+values);
        return values;
    }

    private List<Indicator> readAll(Cursor cursor) {
        cursor.moveToFirst();
        List<Indicator> indicator = new ArrayList<Indicator>();
        while (!cursor.isAfterLast()) {
          
            indicator.add(new Indicator(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
                    
            cursor.moveToNext();
        }
        cursor.close();
        return indicator;
    }

    private List<Indicator> readAllChildren(Cursor cursor) {
        cursor.moveToFirst();
        List<Indicator> children = new ArrayList<Indicator>();
        while (!cursor.isAfterLast()) {
            children.add(serviceFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return children;
    }

    private Indicator serviceFromCursor(Cursor cursor) {
        return new Indicator(
                getColumnValueByAlias(cursor, INDICATOR, REFERRAL_SERVICE_INDICATOR_ID),
                getColumnValueByAlias(cursor, INDICATOR, REFERRAL_INDICATOR_ID),
                getColumnValueByAlias(cursor, INDICATOR, INDICATOR_NAME),
                getColumnValueByAlias(cursor, INDICATOR, IS_ACTIVE));
               
    }

    private String getColumnValueByAlias(Cursor cursor, String table, String column) {
        return cursor.getString(cursor.getColumnIndex(table + column));
    }

    private String insertPlaceholdersForInClause(int length) {
        return repeat("?", ",", length);
    }


    public void delete(String childId) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.delete(INDICATOR, REFERRAL_SERVICE_INDICATOR_ID + "= ?", new String[]{childId});
    }

    public Cursor RawCustomQueryForAdapter(String query) {
        Log.i(getClass().getName(), query);
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }

    public void customInsert(ContentValues contentValues) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        Log.d("customInsert", "tableName = " + TABLE_NAME);
        Log.d("customInsert", "content values = " + contentValues.toString());
        database.insert(TABLE_NAME, null, contentValues);

    }
}
