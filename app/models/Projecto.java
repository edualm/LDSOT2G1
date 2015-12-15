package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import controllers.Project;
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
    @Column(unique = true)
    public Integer id;

    @Column
    public String nome;

    @Column
    public String descricao;

    @OneToMany(mappedBy = "projecto_id")
    @JsonManagedReference
    public List<Comentario> comentarios;

    @OneToMany(mappedBy = "projecto_id")
    @JsonManagedReference
    public List<VersaoProjecto> versoesProjecto;

    @ManyToMany()
    @JoinTable(name = "projecto_tag", joinColumns = @JoinColumn(name = "projecto"), inverseJoinColumns = @JoinColumn(name = "tag"))
    @JsonManagedReference
    public List<Tag> tags;

    @Column
    public String user_id;

    public Projecto(String nome, String descricao , String user_id){
        this.nome = nome;
        this.descricao = descricao;
        this.user_id = user_id;
    }

    public static Finder<Long, Projecto> find = new Finder(Long.class, Projecto.class);

    public static Projecto getLastProjectCreatedByUser(Integer user_id, String nome){

        return find.select("id").where().eq("nome", nome).eq("user_id", user_id.toString()).findUnique();
    }
}
