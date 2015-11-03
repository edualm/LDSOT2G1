package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by João on 03/11/2015.
 */
public class Tag extends Model{

    @Id
    @Column(unique = true)
    public Integer id;

    @Column
    public String nome;

    @ManyToMany(cascade = CascadeType.ALL)
    List<Ficheiro> ficheiros;

    Tag(String nome){
        this.nome = nome;
    }
}
