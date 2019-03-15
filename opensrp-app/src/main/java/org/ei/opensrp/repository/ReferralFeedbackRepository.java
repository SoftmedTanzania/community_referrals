package org.ei.opensrp.repository;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import net.sqlcipher.database.SQLiteDatabase;
import org.apache.commons.lang3.StringUtils;
import org.ei.opensrp.domain.ReferralFeedback;
import java.util.ArrayList;
import java.util.List;
import static org.apache.commons.lang3.StringUtils.repeat;

/**
 * Created by ilakozejumanne on 3/15/19.
 */

public class ReferralFeedbackRepository extends DrishtiRepository {

    public static final String TABLE_NAME = "referral_feedback";
    public static final String NOT_CLOSED = "false";
    private static final String TAG = ReferralFeedbackRepository.class.getSimpleName();
    private static final String CHILD_SQL = "CREATE TABLE referral_feedback(id VARCHAR PRIMARY KEY," +
            "relationalid VARCHAR," +
            "desc VARCHAR," +
            "descSw VARCHAR," +
            "referralType VARCHAR)";
    private static final String ID_COLUMN = "id";
    private static final String DESC_COLUMN = "desc_en";
    private static final String DESC_SW_COLUMN = "desc_sw";
    private static final String REFERRAL_TYPE = "referralType";
    public static final String[] REFERRAL_FEEDBACK_TABLE_COLUMNS = {ID_COLUMN, DESC_COLUMN, DESC_SW_COLUMN, REFERRAL_TYPE};

    @Override
    protected void onCreate(SQLiteDatabase database) {
        database.execSQL(CHILD_SQL);
    }

    public void add(ReferralFeedback referralFeedback) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.insert(TABLE_NAME, null, createValuesFor(referralFeedback));
        Log.d(TAG, "data base created successfully");
    }

    public void update(ReferralFeedback referralFeedback) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.update(TABLE_NAME, createValuesFor(referralFeedback), ID_COLUMN + " = ?", new String[]{referralFeedback.getId()});
    }

    public List<ReferralFeedback> all() {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, REFERRAL_FEEDBACK_TABLE_COLUMNS, null, null, null, null, null);
        return readAll(cursor);
    }

    public ReferralFeedback find(String caseId) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, REFERRAL_FEEDBACK_TABLE_COLUMNS, ID_COLUMN + " = ?", new String[]{caseId}, null, null, null, null);
        List<ReferralFeedback> children = readAll(cursor);

        if (children.isEmpty()) {
            return null;
        }
        return children.get(0);
    }

    public List<ReferralFeedback> findServiceByCaseIds(String... caseIds) {
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


    public ContentValues createValuesFor(ReferralFeedback referralFeedback) {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, referralFeedback.getId());
        values.put(DESC_COLUMN, referralFeedback.getDesc());
        values.put(DESC_SW_COLUMN, referralFeedback.getDescSw());
        values.put(REFERRAL_TYPE, referralFeedback.getReferralType());
        Log.d(TAG, "values" + values);
        return values;
    }

    private List<ReferralFeedback> readAll(Cursor cursor) {
        cursor.moveToFirst();
        List<ReferralFeedback> referralServicesListDataModel = new ArrayList<ReferralFeedback>();
        while (!cursor.isAfterLast()) {

            referralServicesListDataModel.add(new ReferralFeedback(cursor.getString(0), cursor.getString(2), cursor.getString(3), cursor.getString(4)));

            cursor.moveToNext();
        }
        cursor.close();
        return referralServicesListDataModel;
    }

    private List<ReferralFeedback> readAllChildren(Cursor cursor) {
        cursor.moveToFirst();
        List<ReferralFeedback> children = new ArrayList<ReferralFeedback>();
        while (!cursor.isAfterLast()) {
            children.add(serviceFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return children;
    }

    private ReferralFeedback serviceFromCursor(Cursor cursor) {
        return new ReferralFeedback(
                getColumnValueByAlias(cursor, TABLE_NAME, ID_COLUMN),
                getColumnValueByAlias(cursor, TABLE_NAME, DESC_COLUMN),
                getColumnValueByAlias(cursor, TABLE_NAME, DESC_SW_COLUMN),
                getColumnValueByAlias(cursor, TABLE_NAME, REFERRAL_TYPE));

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
