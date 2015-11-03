package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import controllers.Project;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joao Almeida on 03/11/15.
 */

@Entity
public class VersaoProjecto extends Model {

    @Id
    @Column(unique = true)
    public Integer id;

    @Column
    public String descricao;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="projecto_id", referencedColumnName = "id")
    @JsonBackReference
    public Projecto projecto_id;

    @Column
    public Integer user_id;

    public VersaoProjecto(String descricao , Projecto projecto_id, String user_id){
        this.descricao = descricao;
        this.projecto_id = projecto_id;
        this.user_id = (Integer) 1;
    }
}
