package controllers.ot.dbdata;

/**
 * Created by amd on 11/27/15.
 */
public class DataFiller {

    public DataFiller(){
        try {
            new UsersData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
