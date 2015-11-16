package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;


/**
 * Created by João on 09/11/2015.
 */
@Entity
public class Componente extends Model {
    @Id
    @Column
    public Integer id;

    @Column
    public String conteudo;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="tipo_id", referencedColumnName = "id")
    @JsonBackReference
    public Tipo tipo_id;


    @ManyToMany
    public List<VersaoProjecto> versaoprojectos;

    public Componente(String conteudo, Tipo tipo_id){
        this.conteudo = conteudo;
        this.tipo_id = tipo_id;
    }
}
