package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joao Almeida on 27/10/15.
 */

@Entity
public class Projecto extends Model {

    @Id
    @SequenceGenerator(name = "Projecto_id_seq", sequenceName = "Projecto_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Projecto_id_seq")
    @Column(unique = true)
    public Integer id;

    @Column
    public String nome;

    @Column
    public String descricao;

    @Column
    public Integer user_id;

    public Projecto(String nome, String descricao , String user_id){
        this.nome = nome;
        this.descricao = descricao;
        this.user_id = Integer.parseInt(user_id);
    }
}
