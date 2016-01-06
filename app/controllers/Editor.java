package controllers;

import models.*;

import play.data.DynamicForm;

import play.mvc.Controller;
import play.mvc.Result;

import utilities.AuthManager;
import views.html.editor;
import views.html.generic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by MegaEduX on 03/12/15.
 */

public class Editor extends Controller {
    public static <T> T[] reverse(T[] array) {
        T[] copy = array.clone();
        Collections.reverse(Arrays.asList(copy));
        return copy;
    }
    
    public Result editProject(Long id) {
        return editProjectWithVersion(id, Integer.toUnsignedLong(0));
    }
    
    public Result editProjectWithVersion(Long id, Long verId) {
        DynamicForm form = new DynamicForm().bindFromRequest();
        
        if (AuthManager.authCheck(session(), form)) {
            Projecto p = Project.projectos.byId(id);
            String user = AuthManager.currentUsername(session("jwt"));

            if (p == null)
                return notFound(generic.render("Não Encontrado!", "Projeto não encontrado.", true));
            
            VersaoProjecto ver = p.versoesProjecto.get(p.versoesProjecto.size() - 1);
            
            if (verId != 0)
                for (VersaoProjecto v : p.versoesProjecto)
                    if (v.id.longValue() == verId) {
                        ver = v;
                        
                        break;
                    }
            
            if (p.user_id.equals(user)) {
                ArrayList<Tipo> missingTipos = new ArrayList<>();
                
                List<Componente> currentComponents = ver.componentes;
                
                for (Tipo t: Tipo.getTipos()) {
                    boolean contains = false;
                    
                    for (Componente c : currentComponents) {
                        if (c.tipo_id.equals(t)) {
                            contains = true;
                            
                            break;
                        }
                    }
                    
                    if (!contains)
                        missingTipos.add(t);
                }
                
                ArrayList<VersaoProjecto> vps = new ArrayList<>(p.versoesProjecto);
                
                Collections.reverse(vps);
                
                for (int i = 0; i < vps.size(); i++)
                    if (vps.get(i).id == ver.id) {
                        vps.remove(i);
                        
                        break;
                    }
                
                return ok(editor.render(p,
                                        ver.componentes,
                                        missingTipos,
                                        ver,
                                        vps,
                                        p.tags,p.ficheiros));
            }
        }
        
        return forbidden(generic.render("Sem Permissões!", "Não possui permissões para editar a página pretendida.", true));
    }
}