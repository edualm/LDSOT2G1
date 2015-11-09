package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;


/**
 * Created by João on 09/11/2015.
 */
public class Componente extends Model {
    @Id
    @Column
    public Integer id;

    @Column
    public String conteudo;

    @Column
    public Integer tipo_id;


    @ManyToMany
    @JoinTable(name="versaoprojecto_componente",
            joinColumns = @JoinColumn(name = "versaoprojecto_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "componente_id", referencedColumnName = "id"))
    public List<VersaoProjecto> versaoprojectos;
}
