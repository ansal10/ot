package models.ot;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by amd on 11/20/15.
 */

@Entity
public class Permission extends Model {

    @ManyToOne
    private Users user;

    private String permission;

    public void save(){
        super.save();
        super.refresh();
    }


}
