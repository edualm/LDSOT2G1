package controllers;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Componente;
import models.Projecto;
import models.Tipo;
import models.VersaoProjecto;
import play.api.i18n.Messages;
import play.api.libs.json.JsPath;
import play.libs.Json;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joao on 27/10/2015.
 */
public class Project extends Controller {


    //Finder
    public static Model.Finder<Long, Projecto> projectos = new Model.Finder(Long.class, Projecto.class);
    public static Model.Finder<Long, Tipo> tipos = new Model.Finder(Long.class, Tipo.class);

    public Result CriarProjecto(){
        try {
        DynamicForm form = new DynamicForm().bindFromRequest();

        String nome = form.get("nome");
        String descricao = form.get("descricao");
        String user = form.get("user");


        Projecto p = new Projecto(nome,descricao,user);
        VersaoProjecto vs = new VersaoProjecto(descricao, p, "1");
        p.save();
        vs.save();
        ObjectNode json = Json.newObject();
        json.put("result", "success");


        return ok(json);
        }
        catch (Exception e){
            ObjectNode json = Json.newObject();
            json.put("result", "error");
            json.put("excecao", e.getMessage());
            return badRequest(json);
        }

    }

    public Result editarNomeProjecto(){
        try {
            DynamicForm form = new DynamicForm().bindFromRequest();

            String id = form.get("id");
            String nome = form.get("nome");
            String descricao = form.get("descricao");
            String user = form .get("userid");

            Projecto p = projectos.byId(Long.valueOf(id));
            ObjectNode json = Json.newObject();

            if (p.user_id == Integer.parseInt(user)){
                p.nome = nome;
                p.descricao = descricao;
                p.update();
                json.put("result", "success");

                return ok(json);
            }

            json.put("result", "error");
            json.put("excecao", "Not authorized");

            return badRequest(json);

        }
        catch (Exception e){
            ObjectNode json = Json.newObject();
            json.put("result", "error");
            json.put("excecao", e.getMessage());

            return badRequest(json);
        }
    }

    public Result editarConteudoProjecto(){
        try {
            DynamicForm form = new DynamicForm().bindFromRequest();

            String id = form.get("id");
            String nome = form.get("nome");
            String conteudo = form.get("conteudo");
            String user = form .get("userid");

            Projecto p = projectos.byId(Long.valueOf(id));
            ObjectNode json = Json.newObject();

            if (p.user_id == Integer.parseInt(user)){
                VersaoProjecto oldVS = p.versoesProjecto.get(p.versoesProjecto.size() - 1);
                List<Componente> componentes = oldVS.componentes;

                String tipo = null;

                if (nome.equals("fis"))
                    tipo = "Fisica";
                else if (nome.equals("prog"))
                    tipo = "Programacao";
                else if (nome.equals("elec"))
                    tipo = "Elettrotecnica";

                boolean ran = false;

                for(Componente c :componentes)
                    if(c.tipo_id.nome.equals(tipo)){
                        ran = true;

                        VersaoProjecto newVS = new VersaoProjecto(oldVS.descricao,oldVS.projecto_id, oldVS.user_id.toString());
                        //  newVS.componentes = new ArrayList<Componente>(oldVS.componentes);

                        for (Componente cs : oldVS.componentes)
                            newVS.componentes.add(cs);

                        for (Componente newC : newVS.componentes){
                            if(newC.tipo_id.nome.equals(tipo)){
                                newC.conteudo = conteudo;
                                newVS.save();
                                newC.update();

                                json.put("result", "success");
                                return ok(json);

                            }
                        }
                    }

                if (!ran) {
                    json.put("result", "error");
                    json.put("excecao", "Componente nao existente");
                    return badRequest(json);
                }
            }

            json.put("result", "error");
            json.put("excecao", "Not authorized");

            return badRequest(json);


        }
        catch (Exception e){
            ObjectNode json = Json.newObject();
            json.put("result", "error");
            json.put("excecao", e.getMessage());

            return badRequest(json);
        }
    }

    public Result adicionarComponenteProjecto(){

        try {
            DynamicForm form = new DynamicForm().bindFromRequest();

            String id = form.get("id");
            String componente = form.get("componente");
            String user = form.get("userid");


            Projecto p = projectos.byId(Long.valueOf(id));
            ObjectNode json = Json.newObject();

            if (p.user_id == Integer.parseInt(user)) {
                //Os tipos sao estaticos (unicos)
                VersaoProjecto lastVS = p.versoesProjecto.get(p.versoesProjecto.size() -1 );


                Tipo t = tipos.where().eq("nome", componente).findUnique();
                Componente c = new Componente("",t);
                c.versaoprojectos.add(lastVS);
                c.save();

                json.put("result", "success");

                return ok(json);

            }


            json.put("result", "error");
            json.put("excecao", "Not authorized");

            return badRequest(json);


        }
        catch (Exception e){
            ObjectNode json = Json.newObject();

            json.put("result", "error");
            json.put("excecao", e.getMessage());

            return badRequest(json);
        }

    }

    public VersaoProjecto criarVersaoProjecto(String descricao, Projecto p, String user){
        VersaoProjecto vs = new VersaoProjecto(descricao, p, "1");
        vs.save();

        return vs;
    }

    public Result TestVersionamentoProjecto(){
        try {


            DynamicForm form = new DynamicForm().bindFromRequest();
            String projecto = form.get("projecto");
            String descricao = form.get("desc");

            Projecto p = projectos.byId(Long.valueOf(projecto));
            VersaoProjecto vs = criarVersaoProjecto(descricao,p,"1");


            //Componente de Fisica
            Tipo fisica = tipos.where().eq("nome","Fisica").findUnique();
            Componente c1 = new Componente("Conteudo da componente em MARKDOWN Fisica",fisica);


            //Componente de Programacao
            Tipo prog = tipos.where().eq("nome","Programacao").findUnique();
            Componente c2 = new Componente("Conteudo da componente em MARKDOWN Programacao",prog);

            //Componente de Electrotecnia
            Tipo eletro = tipos.where().eq("nome","Eletrotecnica").findUnique();
            Componente c3 = new Componente("Conteudo da componente em MARKDOWW Electrotecnia",eletro);

            c1.versaoprojectos.add(vs);
            c1.save();
            c2.versaoprojectos.add(vs);
            c2.save();
            c3.versaoprojectos.add(vs);
            c3.save();
            vs.save();


            ObjectNode json = Json.newObject();
            json.put("result", "success");



            return ok(json);
        }
        catch (Exception e){
            ObjectNode json = Json.newObject();
            json.put("result", "error");
            json.put("excecao", e.getMessage());
            return badRequest(json);
        }





    }
    public Result getProjectoById(Long id){

        ObjectNode response = Json.newObject();


        if(id == 0)
            return badRequest("Wrong Project ID");

        try {
            Projecto query = projectos.byId(id);
            return  ok(Json.toJson(query));
        }
        catch (Exception e)
        {
            response.put("result",e.getMessage());
            return badRequest(response);
        }
    }

    public  Result getAllProjectos(){
        return ok(Json.toJson(projectos.orderBy("id").findList()));
    }
}
