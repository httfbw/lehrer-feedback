package controllers;

import play.api.mvc.Cookie;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.*;

import javax.inject.Inject;


public class Anmeldung extends Controller {
    FormFactory formFactory;

    @Inject
    public Anmeldung(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result anmeldung(Http.Request request) {
        if (request.session().getOptional("user").isPresent()) {
            return redirect(routes.Feedback.feedbackseite());
        }
        return ok(views.html.anmeldung.render());
    }

    public Result abmeldung (Http.Request request) {
        return redirect(routes.Anmeldung.anmeldung())
                .removingFromSession(request,"user")
                .flashing("success", "Du bist abgemeldet!");
    }

    public Result kontrolle(Http.Request request) {
        DynamicForm formular = formFactory.form().bindFromRequest(request);
        String benutzername = formular.get("Benutzername");
        String passwort = formular.get("Passwort");
        if (UserDB.passwortOk(benutzername, passwort)) {
            if (UserDB.istLehrer(benutzername)) {
                return redirect(routes.Feedback.feedbacklehrer())
                        .addingToSession(request, "user", benutzername)
                        .addingToSession(request, "lehrer", "ja")
                        .flashing("success", "Guten Tag!");
            } else {
                return redirect(routes.Feedback.feedbackseite())
                        .addingToSession(request, "user", benutzername)
                        .addingToSession(request, "lehrer", "nein")
                        .flashing("success", "Hallo! :-)");
            }
        } else {
            return redirect(routes.Anmeldung.anmeldung())
                    .flashing("success", "Name oder Passwort falsch!");
        }
    }
}