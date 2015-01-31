package team.udacity.uos.doodle.util;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import team.udacity.uos.doodle.model.Doodle;

/**
 * Created by JYPark on 2015. 1. 31..
 */
public class Util {

    public static List<Doodle> getDoodles(Context context){
        List<Doodle> result = null;
        DBHelper dbHelper = OpenHelperManager.getHelper(context, DBHelper.class);

        try {
            Dao<Doodle, Integer> dao = dbHelper.getDao(Doodle.class);
            result = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        OpenHelperManager.releaseHelper();

        return result;
    }
}
