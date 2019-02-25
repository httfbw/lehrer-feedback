package controllers;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Map;

public class UserDB {
    static class FeedbackInfo {
        String user;
        int feedback;
        LocalDateTime zeit;

        public FeedbackInfo(String user, int feedback) {
            this.user = user;
            this.feedback = feedback;
            zeit = LocalDateTime.now();
        }
    }

    static Map<String, FeedbackInfo> meldungen = new HashMap<>();

    public static boolean passwortOk(String benutzername, String passwort) {
        return (benutzername.equals("stefan") && passwort.equals("huhu")) ||
                (benutzername.equals("julia") && passwort.equals("julia")) ||
                (benutzername.equals("daniel") && passwort.equals("abc")) ||
                (benutzername.equals("maja") && passwort.equals("maja")) ||
                (benutzername.equals("sylvie") && passwort.equals("123"));
    }

    public static boolean istLehrer(String benutzername) {
        return benutzername.equals("stefan");
    }

    public static int anzahlSchueler() {
        return 4;
    }

    public static synchronized void feedback(String user, int feedback) {
        meldungen.put(user, new FeedbackInfo(user, feedback));
    }

    public static synchronized Map<Integer, Integer> feedbackStatistik() {
        LocalDateTime zeitspanne = LocalDateTime.now().minus(1, ChronoUnit.MINUTES);
        Map<Integer, Integer> statistik = new HashMap<>();
        for (int i=0; i<4; i++) {
            statistik.put(i, 0);
        }
        for (FeedbackInfo info: meldungen.values()) {
            if (info.zeit.isAfter(zeitspanne)) {
                statistik.put(info.feedback, statistik.get(info.feedback) + 1);
            }
        }
        return statistik;
    }
}
