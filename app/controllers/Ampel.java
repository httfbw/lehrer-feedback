package controllers;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import play.Logger;

import java.util.Map;

public class Ampel {
    static Logger.ALogger logger = Logger.of(Ampel.class);
    public static void ampelAktualisieren() {
        int ampel;
        Map<Integer, Integer> statistik = UserDB.feedbackStatistik();
        int gesamtStimmen = 0;
        for (int anzahl: statistik.values()) {
            gesamtStimmen += anzahl;
        }
        if (gesamtStimmen<UserDB.anzahlSchueler()/2) {
            // Ampel aus
            ampel = 0;
        } else {
            int gruen = statistik.get(3);
            int gelb = statistik.get(2) + statistik.get(1);
            int rot = statistik.get(0);

            if (gruen>=gelb && gruen>=rot) {
                ampel = 3;
            } else if (gelb>=gruen && gelb>=rot) {
                ampel = 2;
            } else {
                ampel= 1;
            }
        }

        logger.warn("Setze Ampel auf " + ampel);
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://192.168.137.198/led?command=" + ampel)
                    .build();
            Call call = client.newCall(request);
            Response response = call.execute();
            logger.warn("Ampelstatus " + response.code());
            response.close();
        } catch (Exception e) {
            logger.error("Fehler beim Aktualisieren der Ampel", e);
        }
    }
}
