package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import controllers.Project;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Base64;
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

    @Column
    @Lob
    public byte[] imagem;

    @Column
    public String user_id;

    @OneToMany(mappedBy = "projecto_id")
    @JsonManagedReference
    public List<Comentario> comentarios;

    @OneToMany(mappedBy = "projecto_id")
    @JsonManagedReference
    public List<VersaoProjecto> versoesProjecto;


    public Projecto(String nome, String descricao , String user_id , byte[] imagem){
        this.nome = nome;
        this.descricao = descricao;
        this.user_id = user_id;
        this.imagem = imagem;
    }

    public String getImageBase64() {
        if (imagem != null) {
            try {
                return "data:image/png;base64," + (new String(Base64.getEncoder().encode(imagem), "UTF-8"));
            } catch (Exception e) {

            }
        }

        return "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAlFJREFUeNqUU8tOFEEUPVVdNV3dPe8xYRBnjGhmBgKjKzCIiQvBoIaNbly5Z+PSv3Aj7DSiP2B0rwkLGVdGgxITSCRIJGSMEQWZR3eVt5sEFBgTb/dN1yvnnHtPNTPG4PqdHgCMXnPRSZrpSuH8vUJu4DE4rYHDGAZDX62BZttHqTiIayM3gGiXQsgYLEvATaqxU+dy1U13YXapXptpNHY8iwn8KyIAzm1KBdtRZWErpI5lEWTXp5Z/vHpZ3/wyKKwYGGOdAYwR0EZwoezTYApBEIObyELl/aE1/83cp40Pt5mxqCKrE4Ck+mVWKKcI5tA8BLEhRBKJLjez6a7MLq7XZtp+yyOawwCBtkiBVZDKzRk4NN7NQBMYPHiZDFhXY+p9ff7F961vVcnl4R5I2ykJ5XFN7Ab7Gc61VoipNBKF+PDyztu5lfrSLT/wIwCxq0CAGtXHZTzqR2jtwQiXONma6hHpj9sLT7YaPxfTXuZdBGA02Wi7FS48YiTfj+i2NhqtdhP5RC8mh2/Op7y0v6eAcWVLFT8D7kWX5S9mepp+C450MV6aWL1cGnvkxbwHtLW2B9AOkLeUd9KEDuh9fl/7CEj7YH5g+3r/lWfF9In7tPz6T4IIwBJOr1SJyIGQMZQbsh5P9uBq5VJtqHh2mo49pdw5WFoEwKWqWHacaWOjQXWGcifKo6vj5RGS6zykI587XeUIQDqJSmAp+lE4qt19W5P9o8+Lma5DcjsC8JiT607lMVkdqQ0Vyh3lHhmh52tfNy78ajXv0rgYzv8nfwswANuk+7sD/Q0aAAAAAElFTkSuQmCC";
    }

    public static Finder<Long, Projecto> find = new Finder(Long.class, Projecto.class);

    public static Projecto getLastProjectCreatedByUser(Integer user_id, String nome){

        return find.select("id").where().eq("nome", nome).eq("user_id", user_id.toString()).findUnique();
    }
}
