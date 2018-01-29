package org.ei.opensrp.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.hardware.camera2.params.Face;

import com.google.gson.Gson;

import net.sqlcipher.database.SQLiteDatabase;
import org.apache.commons.lang3.StringUtils;
import org.ei.opensrp.domain.Facility;
import org.ei.opensrp.repository.DrishtiRepository;

import java.util.ArrayList;
import java.util.List;

import static net.sqlcipher.DatabaseUtils.longForQuery;
import static org.apache.commons.lang3.StringUtils.repeat;

public class FacilityRepository extends DrishtiRepository {
    private static final String CHILD_SQL = "CREATE TABLE facility(id VARCHAR PRIMARY KEY,relationalid VARCHAR,details VARCHAR, name VARCHAR)";
    public static final String FACILITY = "facility";
    private static final String ID_COLUMN = "id";
    private static final String RELATIONAL_COLUMN = "relationalid";
    private static final String DETAILS_COLUMN = "details";
    private static final String NAME = "name";
    public static final String[] FACILITY_TABLE_COLUMNS = {ID_COLUMN, RELATIONAL_COLUMN,DETAILS_COLUMN,NAME};
    

    @Override
    protected void onCreate(SQLiteDatabase database) {
        database.execSQL(CHILD_SQL);
    }

    public void add(Facility facility) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.insert(FACILITY, null, createValuesFor(facility));
    }

    public void update(Facility facility) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.update(FACILITY, createValuesFor(facility), ID_COLUMN + " = ?", new String[]{facility.getOpenMRSUIID()});
    }

    public List<Facility> all() {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(FACILITY, FACILITY_TABLE_COLUMNS, null, null, null, null, null);
        return readAll(cursor);
    }

    public Facility find(String caseId) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(FACILITY, FACILITY_TABLE_COLUMNS, ID_COLUMN + " = ?", new String[]{caseId}, null, null, null, null);
        List<Facility> children = readAll(cursor);

        if (children.isEmpty()) {
            return null;
        }
        return children.get(0);
    }

    public List<Facility> findFacilityByCaseIds(String... caseIds) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.rawQuery(String.format("SELECT * FROM %s WHERE %s IN (%s)", FACILITY, ID_COLUMN, insertPlaceholdersForInClause(caseIds.length)), caseIds);
        return readAll(cursor);
    }
    public Facility findByServiceName(String name) {

        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(FACILITY, FACILITY_TABLE_COLUMNS, NAME + " = ?", new String[]{name}, null, null, null, null);
        List<Facility> children = readAll(cursor);

        if (children.isEmpty()) {
            return null;
        }
        return children.get(0);
    }
    public void updateName(String caseId, String name ){
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        database.update(FACILITY, values, ID_COLUMN + " = ?", new String[]{caseId});
    }

    public List<Facility> findByServiceCaseId(String caseId) {
        SQLiteDatabase database = masterRepository.getReadableDatabase();
        Cursor cursor = database.query(FACILITY, FACILITY_TABLE_COLUMNS, NAME + " = ?", new String[]{caseId}, null, null, null, null);
        return readAll(cursor);
    }

    public long count() {
        return longForQuery(masterRepository.getReadableDatabase(), "SELECT COUNT(1) FROM " + FACILITY, new String[0]);
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


    public ContentValues createValuesFor(Facility facility) {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, facility.getOpenMRSUIID());
        values.put(RELATIONAL_COLUMN, facility.getOpenMRSUIID());
        values.put(NAME, facility.getFacilityName());
        values.put(DETAILS_COLUMN, new Gson().toJson(facility));
        return values;
    }

    private List<Facility> readAll(Cursor cursor) {
        cursor.moveToFirst();
        List<Facility> referralServicesListDataModel = new ArrayList<Facility>();
        while (!cursor.isAfterLast()) {
            referralServicesListDataModel.add(new Facility(cursor.getString(0), cursor.getString(1))

            );
            cursor.moveToNext();
        }
        cursor.close();
        return referralServicesListDataModel;
    }

    private List<Facility> readAllChildren(Cursor cursor) {
        cursor.moveToFirst();
        List<Facility> children = new ArrayList<Facility>();
        while (!cursor.isAfterLast()) {
            children.add(serviceFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return children;
    }

    private Facility serviceFromCursor(Cursor cursor) {
        return new Facility(
                getColumnValueByAlias(cursor, FACILITY, ID_COLUMN),
                getColumnValueByAlias(cursor, FACILITY, NAME));
    }

    private String getColumnValueByAlias(Cursor cursor, String table, String column) {
        return cursor.getString(cursor.getColumnIndex(table + column));
    }

    private String insertPlaceholdersForInClause(int length) {
        return repeat("?", ",", length);
    }


    public void delete(String childId) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        database.delete(FACILITY, ID_COLUMN + "= ?", new String[]{childId});
    }
}
