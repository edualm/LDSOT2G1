package controllers;

import models.Projecto;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by Joao on 27/10/2015.
 */
public class Project extends Controller {

    public Result CriarProjecto(){
        DynamicForm form = new DynamicForm().bindFromRequest();

        String nome = form.get("nome");
        String descricao = form.get("descricao");
        String user = form.get("user");


        Projecto p = new Projecto(nome,descricao,user);
        p.save();

        return ok("{\"result\": \"success\"}");

    }
}
