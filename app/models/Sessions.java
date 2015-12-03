package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by joao on 02/12/2015.
 */
@Entity
public class Sessions extends Model {

    @Column
    public String username;

    @Column
    public String token;

    @Column
    public Timestamp expires;

    public Sessions(String username, String jwt){
        this.username = username;
        this.token = jwt;
        this.expires = new Timestamp(new Date().getTime());
    }
}