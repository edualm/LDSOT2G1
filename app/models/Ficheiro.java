package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.File;
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

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="projecto_id", referencedColumnName = "id")
    @JsonBackReference
    public Projecto projecto_id;

    public Ficheiro(String nome, byte[] ficheiro, Projecto projecto){
        this.nome = nome;
        this.ficheiro = ficheiro;
        this.projecto_id = projecto;
    }
}
