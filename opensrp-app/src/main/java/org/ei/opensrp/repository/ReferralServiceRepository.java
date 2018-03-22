package org.ei.opensrp.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sqlcipher.database.SQLiteDatabase;
import org.apache.commons.lang3.StringUtils;
import org.ei.opensrp.domain.Indicator;
import org.ei.opensrp.domain.ReferralServiceDataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.sqlcipher.DatabaseUtils.longForQuery;
import static org.apache.commons.lang3.StringUtils.repeat;

public class ReferralServiceRepository extends DrishtiRepository {
    private static final String TAG = ReferralServiceRepository.class.getSimpleName();
    private static final String CHILD_SQL = "CREATE TABLE referral_service(id VARCHAR PRIMARY KEY,relationalid VARCHAR,is_valid VARCHAR,category VARCHAR,details VARCHAR,indicators VARCHAR, name VARCHAR,name_sw VARCHAR)";
    public static final String REFERRAL_SERVICE = "referral_service";
    private static final String ID_COLUMN = "id";
    private static final String RELATIONAL_COLUMN = "relationalid";
    private static final String DETAILS_COLUMN = "details";
    private static final String NAME = "name";
    private static final String NAME_SW = "name_sw";
    private static final String CATEGORY = "category";
    private static final String INDICATOR = "indicators";
    private static final String IS_VALID = "is_valid";
    public static final String[] REFERRAL_SERVICE_TABLE_COLUMNS = {ID_COLUMN, RELATIONAL_COLUMN,CATEGORY,INDICATOR,IS_VALID,DETAILS_COLUMN,NAME,NAME_SW};
    public static final String NOT_CLOSED = "false";

    @Override
    protected void onCreate(SQLiteDatabase database) {
        database.execSQL(CHILD_SQL);
    }

    public void add(ReferralServiceDataModel referralServiceDataModel) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.insert(REFERRAL_SERVICE, null, createValuesFor(referralServiceDataModel));
        Log.d(TAG,"data base created successfully");
    }

    public void update(ReferralServiceDataModel referralServiceDataModel) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.update(REFERRAL_SERVICE, createValuesFor(referralServiceDataModel), ID_COLUMN + " = ?", new String[]{referralServiceDataModel.getId()});
    }

    public List<ReferralServiceDataModel> all() {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(REFERRAL_SERVICE, REFERRAL_SERVICE_TABLE_COLUMNS, null, null, null, null, null);
        return readAll(cursor);
    }

    public ReferralServiceDataModel find(String caseId) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(REFERRAL_SERVICE, REFERRAL_SERVICE_TABLE_COLUMNS, ID_COLUMN + " = ?", new String[]{caseId}, null, null, null, null);
        List<ReferralServiceDataModel> children = readAll(cursor);

        if (children.isEmpty()) {
            return null;
        }
        return children.get(0);
    }

    public List<ReferralServiceDataModel> findServiceByCaseIds(String... caseIds) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.rawQuery(String.format("SELECT * FROM %s WHERE %s IN (%s)", REFERRAL_SERVICE, ID_COLUMN, insertPlaceholdersForInClause(caseIds.length)), caseIds);
        return readAll(cursor);
    }

    public void updateName(String caseId, String name ){
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        database.update(REFERRAL_SERVICE, values, ID_COLUMN + " = ?", new String[]{caseId});
    }

    public ReferralServiceDataModel findByServiceName(String name) {

        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(REFERRAL_SERVICE, REFERRAL_SERVICE_TABLE_COLUMNS, NAME + " = ?", new String[]{name}, null, null, null, null);
        List<ReferralServiceDataModel> children = readAll(cursor);

        if (children.isEmpty()) {
            return null;
        }
        return children.get(0);
    }

    public long count() {
        return longForQuery(masterRepository.getReadableDatabase(), "SELECT COUNT(1) FROM " + REFERRAL_SERVICE, new String[0]);
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



    public ContentValues createValuesFor(ReferralServiceDataModel referralServiceDataModel) {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, referralServiceDataModel.getId());
        values.put(RELATIONAL_COLUMN, referralServiceDataModel.getId());
        values.put(NAME, referralServiceDataModel.getServiceName());
        values.put(NAME_SW, referralServiceDataModel.getServiceNameSw());
        values.put(CATEGORY, referralServiceDataModel.getServiceName());
        values.put(IS_VALID, referralServiceDataModel.getIsActive());
        values.put(DETAILS_COLUMN, new Gson().toJson(referralServiceDataModel));
        Log.d(TAG,"values"+values);
        return values;
    }

    private List<ReferralServiceDataModel> readAll(Cursor cursor) {
        cursor.moveToFirst();
        List<ReferralServiceDataModel> referralServicesListDataModel = new ArrayList<ReferralServiceDataModel>();
        while (!cursor.isAfterLast()) {

            referralServicesListDataModel.add(new ReferralServiceDataModel(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4)));

            cursor.moveToNext();
        }
        cursor.close();
        return referralServicesListDataModel;
    }

    private List<ReferralServiceDataModel> readAllChildren(Cursor cursor) {
        cursor.moveToFirst();
        List<ReferralServiceDataModel> children = new ArrayList<ReferralServiceDataModel>();
        while (!cursor.isAfterLast()) {
            children.add(serviceFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return children;
    }

    private ReferralServiceDataModel serviceFromCursor(Cursor cursor) {
        return new ReferralServiceDataModel(
                getColumnValueByAlias(cursor, REFERRAL_SERVICE, ID_COLUMN),
                getColumnValueByAlias(cursor, REFERRAL_SERVICE, NAME),
                getColumnValueByAlias(cursor, REFERRAL_SERVICE, NAME_SW),
                getColumnValueByAlias(cursor, REFERRAL_SERVICE, IS_VALID),
                getColumnValueByAlias(cursor, REFERRAL_SERVICE, CATEGORY));

    }

    private String getColumnValueByAlias(Cursor cursor, String table, String column) {
        return cursor.getString(cursor.getColumnIndex(table + column));
    }

    private String insertPlaceholdersForInClause(int length) {
        return repeat("?", ",", length);
    }


    public void delete(String childId) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.delete(REFERRAL_SERVICE, ID_COLUMN + "= ?", new String[]{childId});
    }
}
