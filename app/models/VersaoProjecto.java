package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import controllers.Project;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

/**
 * Created by Joao Almeida on 03/11/15.
 */

@Entity
@Table(name = "versaoprojecto")
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
    public String user_id;
	
	@Column
	public Timestamp data;

    @ManyToMany
    @JsonManagedReference
    public List<Componente> componentes;


    public VersaoProjecto(String descricao , Projecto projecto_id, String user_id) {
        this.descricao = descricao;
        this.projecto_id = projecto_id;
        this.user_id = user_id;
		this.data = new Timestamp( new Date().getTime());
    }
}
