
package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.commons.io.IOUtils;

import play.libs.Json;

import play.data.DynamicForm;

import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utilities.AuthManager;

import models.*;
import views.html.*;


public class Project extends Controller {
    
    //Finder
    public static Model.Finder<Long, Projecto> projectos = new Model.Finder(Long.class, Projecto.class);
    public static Model.Finder<Long, Tipo> tipos = new Model.Finder(Long.class, Tipo.class);
    public static Model.Finder<Long, Sessions> sessions = new Model.Finder(String.class, Sessions.class);
    
    public Result criarProjecto() {
        ObjectNode response = Json.newObject();
        
        DynamicForm form = new DynamicForm().bindFromRequest();
        
        if (AuthManager.authCheck(session(), form)) {
            Http.MultipartFormData multipartForm = request().body().asMultipartFormData();
            
            String nome = form.get("title");
            String descricao = form.get("description");
            String user = AuthManager.currentUsername(session("jwt"));
            
            Projecto p = null;
            
            try {
                Http.MultipartFormData.FilePart ficheiro = multipartForm.getFile("file");
                byte[] bytes = null;
                
                if (ficheiro != null) {
                    File imgFile = ficheiro.getFile();
                    
                    bytes = IOUtils.toByteArray(new FileInputStream(imgFile));
                    p = new Projecto(nome, descricao, user, bytes);
                }
                else{
                    System.out.print("Este projeto vem sem imagem!");
                    p = new Projecto(nome, descricao, user, null);
                }
                
            } catch (Exception e) {
                p = new Projecto(nome, descricao, user, null);
            }
            
            VersaoProjecto vs = new VersaoProjecto(descricao, p, AuthManager.currentUsername(session("jwt")));
            
            p.save();
            vs.save();
            
            //response.put("result", "success");

            ArrayList<VersaoProjecto> vps = new ArrayList<>();

            return ok(project.render(p, true, vs, vs.componentes, vps, p.tags, p.ficheiros));
        } else {
            return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.getServerURL(request()));
        }
    }
    
    public Result editarNomeProjecto(){
        ObjectNode response = Json.newObject();
        
        DynamicForm form = new DynamicForm().bindFromRequest();
        
        if (AuthManager.authCheck(session(), form)) {
            String id = form.get("id");
            String nome = form.get("nome");
            String descricao = form.get("descricao");
            String user = AuthManager.currentUsername(session("jwt"));
            
            Projecto p = projectos.byId(Long.valueOf(id));
            
            if (p.user_id.equals(user)){
                p.nome = nome;
                p.descricao = descricao;
                
                p.update();
                
                response.put("result", "success");
                
                return ok(response);
            } else {
                response.put("result", "error");
                response.put("excecao", "Not authorized");
                
                return unauthorized(response);
            }
        } else
            return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.getServerURL(request()));
    }
    
    public Result editarConteudoProjecto() {
        ObjectNode response = Json.newObject();
        
        DynamicForm form = new DynamicForm().bindFromRequest();
        
        if (AuthManager.authCheck(session(), form)) {
            String id = form.get("id");
            String nome = form.get("nome");
            String conteudo = form.get("conteudo");
            String user = AuthManager.currentUsername(session("jwt"));
            List<Tipo> tipos = Tipo.getTipos();

            Projecto p = projectos.byId(Long.valueOf(id));

            if (p.user_id.equals(user)) {
                VersaoProjecto oldVS = p.versoesProjecto.get(p.versoesProjecto.size() - 1);
                List<Componente> componentes = oldVS.componentes;

                VersaoProjecto newVS = new VersaoProjecto(oldVS.descricao, oldVS.projecto_id, oldVS.user_id.toString());

                newVS.componentes = new ArrayList<Componente>(oldVS.componentes);

                boolean ran = false;

                for (Tipo t : tipos) {
                    String componentContent = form.get(t.nome);

                    if (componentContent != null) {
                        for (Componente c : componentes) {
                            System.out.println("Componente: " + c.tipo_id.nome);
                            System.out.println("Componente API: " + t.nome);

                            if (c.tipo_id.nome.equals(t.nome)) {
                                ran = true;
                                System.out.println("Found the component name.");

                                System.out.println("Removing old component...");
                                newVS.componentes.remove(c);

                                Componente cNew = new Componente(componentContent, c.tipo_id);
                                cNew.save();

                                System.out.println("Adding new Component to Versao projeto");
                                newVS.componentes.add(cNew);

                                cNew.update();
                            }
                        }

                    }
                }

                if (!ran) {
                    response.put("result", "error");
                    response.put("excecao", "Componente nao existente");

                    return badRequest(response);
                } else {
                    newVS.save();

                    response.put("result", "success");
                    return ok(response);
                }
            }

            response.put("result", "error");
            response.put("excecao", "Not authorized");

            return unauthorized(response);
        } else
            return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.getServerURL(request()));
    }
    
    public Result adicionarComponenteProjecto(){
        ObjectNode response = Json.newObject();
        
        DynamicForm form = new DynamicForm().bindFromRequest();
        
        if (AuthManager.authCheck(session(), form)) {
            String id = form.get("id");
            String componente = form.get("componente");
            String user = AuthManager.currentUsername(session("jwt"));
            
            Projecto p = projectos.byId(Long.valueOf(id));
            
            if (p.user_id.equals(user)) {
                VersaoProjecto lastVS = p.versoesProjecto.get(p.versoesProjecto.size() -1 );
                Tipo t = tipos.where().eq("nome", componente).findUnique();
                Componente c = new Componente("",t);
                
                c.versaoprojectos.add(lastVS);
                c.save();
                
                response.put("result", "success");
                
                return ok(response);
            } else {
                response.put("result", "error");
                response.put("excecao", "Not authorized");
                
                return unauthorized(response);
            }
        } else
            return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.getServerURL(request()));
    }

    public Result getProjectoVersionById(Long projectId, Long versionId) {
        Projecto p = projectos.byId(projectId);

        if (p == null)
            return notFound(generic.render("Not Found!", "Project not found.", true));

        VersaoProjecto ver = null;

        List<VersaoProjecto> versions = p.versoesProjecto;

        for (VersaoProjecto v : versions) {
            if (versionId.intValue() == v.id.intValue()) {
                ver = v;

                break;
            }
        }

        if (ver == null)
            return notFound(generic.render("Not Found!", "Project version not found.", true));

        ArrayList<VersaoProjecto> vps = new ArrayList<>(p.versoesProjecto);

        Collections.reverse(vps);

        return ok(project.render(p, true, ver, ver.componentes, vps, p.tags, p.ficheiros));
    }

    public Result getProjectoById(Long id){
        Projecto p = projectos.byId(id);

        if (p == null)
            return notFound(generic.render("Not Found!", "Project not found.", true));

        VersaoProjecto ver = p.versoesProjecto.get(p.versoesProjecto.size() - 1);

        ArrayList<VersaoProjecto> vps = new ArrayList<>(p.versoesProjecto);

        Collections.reverse(vps);

        return ok(project.render(p, true, ver, ver.componentes, vps, p.tags, p.ficheiros));
    }
    
    public Result getAllProjectos() {
        DynamicForm form = new DynamicForm().bindFromRequest();

        return ok(projects.render(projectos.orderBy("id").findList(), AuthManager.authCheck(session(), form)));
    }

    public Result getUserProjectos() {
        DynamicForm form = new DynamicForm().bindFromRequest();

        if (AuthManager.authCheck(session(), form)) {
            String user = AuthManager.currentUsername(session("jwt"));

            List<Projecto> res = Ebean.find(Projecto.class).where().eq("user_id", user).findList();

            return ok(myprojects.render(user, res, AuthManager.authCheck(session(), form)));
        }

        return forbidden();
    }

    public Result editarTagsProjecto(/* Long projectId, Long tagId */) {
        //  create tag if it doesn't exist
        //  add to project

        ObjectNode response = Json.newObject();

        DynamicForm form = new DynamicForm().bindFromRequest();

        String tagsArrJSON = form.get("tags");

        if (AuthManager.authCheck(session(), form)) {
            Projecto q = projectos.byId(Long.valueOf(form.get("project")));
            String user = AuthManager.currentUsername(session("jwt"));

            JsonNode tagsNode = Json.parse(tagsArrJSON);

            while (q.tags.size() > 0) {
                Tag t = q.tags.get(0);

                t.projectos.remove(q);
                q.tags.remove(t);

                t.save();
            }

            q.save();

            for (JsonNode j : tagsNode) {
                Tag t = Tag.getTagNamed(j.asText());

                if (t == null) {
                    t = new Tag(j.asText());

                    t.save();
                }

                q.tags.add(t);

                t.save();

                System.out.println("Added tag: " + t.nome);
            }

            System.out.println("Tags: " + q.tags);

            q.save();

            return ok();
        } else
            return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.getServerURL(request()));
    }
    
    public  Result removerProjecto(Long id) {
        if (id == 0 || id < 0)
            return badRequest("Wrong Project ID");
        
        ObjectNode response = Json.newObject();
        
        DynamicForm form = new DynamicForm().bindFromRequest();
        
        if (AuthManager.authCheck(session(), form)) {
            Projecto q = projectos.byId(id);
            String user = AuthManager.currentUsername(session("jwt"));
            
            if (q.user_id.equals(user)) {
                q.delete();
                
                response.put("result", "success");
                
                return ok(response);
            } else {
                response.put("result", "Not authorized");
                
                return unauthorized(response);
            }
        } else
            return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.getServerURL(request()));
    }
}