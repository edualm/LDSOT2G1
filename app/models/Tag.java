package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.annotation.Nullable;
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

    @ManyToMany()
    @JoinTable(name = "projecto_tag", joinColumns = @JoinColumn(name = "tag"), inverseJoinColumns = @JoinColumn(name = "projecto"))
    @JsonManagedReference
    public List<Projecto> projectos;

    public  Tag(String nome) {
        this.nome = nome;
    }

    @Nullable
    public static Tag getTagName(String name) {
        List<Tag> results = Ebean.find(Tag.class).where().eq("nome", name).findList();

        if (results.size() != 0)
            return results.get(0);

        return null;
    }
}
