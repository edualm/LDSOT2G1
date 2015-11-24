package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by João on 03/11/2015.
 */
@Entity
public class Tag extends Model{

    @Id
    @Column(unique = true)
    public Integer id;

    @Column
    public String nome;

    public  Tag(String nome) {
        this.nome = nome;
    }
}
