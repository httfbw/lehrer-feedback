package controllers;

import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Map;


public class Feedback extends Controller {
    FormFactory formFactory;

    @Inject
    public Feedback(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result feedbackseite(Http.Request request) {
        if (!request.session().getOptional("user").isPresent()) {
            return redirect(routes.Anmeldung.anmeldung());
        }
        if (!request.session().getOptional("lehrer").orElse("-").equals("nein")) {
            return forbidden();
        }

        return ok(views.html.Feedbackseite.render());
    }
    public Result danke(Http.Request request){
        if (!request.session().getOptional("user").isPresent()) {
            return redirect(routes.Anmeldung.anmeldung());
        }
        if (!request.session().getOptional("lehrer").orElse("-").equals("nein")) {
            return forbidden();
        }

        DynamicForm formular = formFactory.form().bindFromRequest(request);
        String feedbackString = formular.get("Feedback");
        int feedback;
        try {
            feedback = Integer.valueOf(feedbackString);
        } catch (Exception e) {
            return redirect(routes.Feedback.feedbackseite())
                    .flashing("success", " Das war wohl nix!");
        }
        String user = request.session().getOptional("user").get();

        UserDB.feedback(user, feedback);
        Ampel.ampelAktualisieren();

        return redirect(routes.Feedback.feedbackseite())
                .flashing("success", " Danke für Dein Feedback!");
    }

    public Result feedbacklehrer(Http.Request request) {
        if (!request.session().getOptional("user").isPresent()) {
            return redirect(routes.Anmeldung.anmeldung());
        }
        if (!request.session().getOptional("lehrer").orElse("-").equals("ja")) {
            return forbidden();
        }

        Map<Integer, Integer> statistik = UserDB.feedbackStatistik();
        return ok(views.html.Lehrerseite.render(statistik.get(3), statistik.get(2), statistik.get(1), statistik.get(0)));
    }
}

