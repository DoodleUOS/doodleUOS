package team.udacity.uos.doodle.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Weather {
    @DatabaseField(id = true)
    public Integer num;
    @DatabaseField
    public String day;
    @DatabaseField
    public String description;
    @DatabaseField
    public String highAndLow;

    public Weather() {

    }

    public Weather(Integer num, String day, String description, String highAndLow) {
        this.num = num;
        this.day = day;
        this.description = description;
        this.highAndLow = highAndLow;
    }
}
