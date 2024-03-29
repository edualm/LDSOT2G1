package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Joao on 28/10/2015.
 */


@Entity
public class Comentario extends Model {
    @Id
    @Column(unique = true)
    public Integer id;

    @Column
    public Timestamp data;

    @Column
    public String mensagem;

    @Column
    public String user_id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="projecto_id", referencedColumnName = "id")
    @JsonBackReference
    public Projecto projecto_id;

    public Comentario(String mensagem, String user_id, Projecto projecto_id){
        this.data = new Timestamp( new Date().getTime());
        this.mensagem = mensagem;
        this.user_id = user_id;
        this.projecto_id = projecto_id;
    }


}

