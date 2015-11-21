package models;

import com.avaje.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by amd on 11/21/15.
 */

@Entity
public class Option extends Model {


    @ManyToOne
    private Question question;

    @Column(length = 10240)
    private String option;
}
