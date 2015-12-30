package models;

        import com.avaje.ebean.Ebean;
        import com.fasterxml.jackson.annotation.JsonManagedReference;

        import javax.persistence.Column;
        import javax.persistence.Entity;
        import javax.persistence.Id;
        import javax.persistence.OneToMany;
        import java.util.List;

/**
 * Created by João on 09/11/2015.
 */

@Entity
public class Tipo {
    @Id
    @Column
    public Integer id;

    @Column
    public String nome;

    @OneToMany(mappedBy = "tipo_id")
    @JsonManagedReference
    public List<Componente> componentes;

    public Tipo(String nome){
        this.nome = nome;
    }

    public static List<Tipo> getTipos() {
        return Ebean.find(Tipo.class).findList();
    }
}
