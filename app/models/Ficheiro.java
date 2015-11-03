package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

/**
 * Created by João on 03/11/2015.
 */
@Entity
public class Ficheiro extends Model {

    @Id
    @Column(unique = true)
    public Integer id;

    @Column
    public String nome;

    @Column
    @Lob
    public byte[] ficheiro;


    Ficheiro(String nome, byte[] ficheiro){
        this.nome = nome;
        this.ficheiro = ficheiro;
    }

}
