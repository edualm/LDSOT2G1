package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
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
    public Date data;

    @Column
    public String mensagem;

    @Column
    public Integer user_id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="projecto_id", referencedColumnName = "id")
    public Projecto projecto_id;

    public Comentario(Date data, String mensagem, Integer user_id, Projecto projecto_id){
        this.data = data;
        this.mensagem = mensagem;
        this.user_id = user_id;
        this.projecto_id = projecto_id;
    }


}

