package team.udacity.uos.doodle.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import team.udacity.uos.doodle.model.Doodle;
import team.udacity.uos.doodle.model.Weather;

public class DBHelper extends OrmLiteSqliteOpenHelper{
    private static final String DATABASE_NAME = "doodle.db";
    private static final int DATABASE_VERSION = 1;
    private Context mContext;


    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }


    public DBHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            SharedPreferences pref = mContext.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            pref.edit()
                    .remove(Constants.PREF_CONTACT_SYNC_DATE)
                    .apply();
            TableUtils.createTable(connectionSource, Doodle.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Doodle.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onCreate(database, connectionSource);
    }
}
