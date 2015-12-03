package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import views.html.editor;

/**
 * Created by MegaEduX on 03/12/15.
 */
public class Editor extends Controller {
    public Result showTestPage() {
        return ok(editor.render());
    }
}
