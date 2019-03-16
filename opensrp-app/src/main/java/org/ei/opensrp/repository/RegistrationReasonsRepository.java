package org.ei.opensrp.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.ei.opensrp.domain.RegistrationReasons;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.repeat;

/**
 * Created by ilakozejumanne on 3/15/19.
 */

public class RegistrationReasonsRepository extends DrishtiRepository {

    public static final String TABLE_NAME = "registration_reasons";
    private static final String TAG = RegistrationReasonsRepository.class.getSimpleName();
    private static final String CHILD_SQL = "CREATE TABLE registration_reasons(id VARCHAR PRIMARY KEY," +
            "relationalid VARCHAR," +
            "desc_en VARCHAR," +
            "desc_sw VARCHAR," +
            "active VARCHAR)";
    private static final String ID_COLUMN = "id";
    private static final String DESC_COLUMN = "desc_en";
    private static final String DESC_SW_COLUMN = "desc_sw";
    private static final String ACTIVE = "active";
    public static final String[] REFERRAL_FEEDBACK_TABLE_COLUMNS = {ID_COLUMN, DESC_COLUMN, DESC_SW_COLUMN, ACTIVE};

    @Override
    protected void onCreate(SQLiteDatabase database) {
        database.execSQL(CHILD_SQL);
    }

    public void add(RegistrationReasons registrationReasons) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.insert(TABLE_NAME, null, createValuesFor(registrationReasons));
        Log.d(TAG, "adding registration reasons");
    }

    public void update(RegistrationReasons registrationReasons) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.update(TABLE_NAME, createValuesFor(registrationReasons), ID_COLUMN + " = ?", new String[]{registrationReasons.getId()});
    }

    public List<RegistrationReasons> all() {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, REFERRAL_FEEDBACK_TABLE_COLUMNS, null, null, null, null, null);
        return readAll(cursor);
    }

    public RegistrationReasons find(String caseId) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, REFERRAL_FEEDBACK_TABLE_COLUMNS, ID_COLUMN + " = ?", new String[]{caseId}, null, null, null, null);
        List<RegistrationReasons> children = readAll(cursor);

        if (children.isEmpty()) {
            return null;
        }
        return children.get(0);
    }

    public List<RegistrationReasons> findServiceByCaseIds(String... caseIds) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.rawQuery(String.format("SELECT * FROM %s WHERE %s IN (%s)", TABLE_NAME, ID_COLUMN, insertPlaceholdersForInClause(caseIds.length)), caseIds);
        return readAll(cursor);
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


    public ContentValues createValuesFor(RegistrationReasons registrationReasons) {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, registrationReasons.getId());
        values.put(DESC_COLUMN, registrationReasons.getDescEn());
        values.put(DESC_SW_COLUMN, registrationReasons.getDescSw());
        values.put(ACTIVE, registrationReasons.getActive());
        Log.d(TAG, "values" + values);
        return values;
    }

    private List<RegistrationReasons> readAll(Cursor cursor) {
        cursor.moveToFirst();
        List<RegistrationReasons> referralServicesListDataModel = new ArrayList<RegistrationReasons>();
        while (!cursor.isAfterLast()) {

            referralServicesListDataModel.add(new RegistrationReasons(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));

            cursor.moveToNext();
        }
        cursor.close();
        return referralServicesListDataModel;
    }

    private List<RegistrationReasons> readAllChildren(Cursor cursor) {
        cursor.moveToFirst();
        List<RegistrationReasons> children = new ArrayList<RegistrationReasons>();
        while (!cursor.isAfterLast()) {
            children.add(serviceFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return children;
    }

    private RegistrationReasons serviceFromCursor(Cursor cursor) {
        return new RegistrationReasons(
                getColumnValueByAlias(cursor, TABLE_NAME, ID_COLUMN),
                getColumnValueByAlias(cursor, TABLE_NAME, DESC_COLUMN),
                getColumnValueByAlias(cursor, TABLE_NAME, DESC_SW_COLUMN),
                getColumnValueByAlias(cursor, TABLE_NAME, ACTIVE));

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
