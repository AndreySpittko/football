package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.prefs.Preferences;

public class Main {

    public static void main(String[] args) {
        // write your code here
        disableSSL();

        try {
            String response = getMatchesToday();

            if(response == null) {
                return;
            }
            JSONArray jsonResponse = new JSONArray((response));
            for (int i = 0; i < jsonResponse.length(); i++) {
                JSONObject singleResponse = jsonResponse.getJSONObject(i);
                Object homeTeamStatisticsObject = singleResponse.get("home_team_statistics");

                if (homeTeamStatisticsObject == null || homeTeamStatisticsObject.toString().equalsIgnoreCase("null")) {
                    continue;
                }

                JSONObject homeTeamStatistics = new JSONObject(homeTeamStatisticsObject.toString());
                JSONArray startingElevenArray = homeTeamStatistics.getJSONArray("starting_eleven");

                String homeTeamStatisticsJSONArray = homeTeamStatistics.getString("tactics");

//                JSONArray startingElevenArray = homeTeamStatistics.getJSONArray("starting_eleven");

                for (int j = 0; j < startingElevenArray.length(); j++) {
                    JSONObject playerObject = startingElevenArray.getJSONObject(j);

                    boolean captain = playerObject.getBoolean("captain");

                    if(captain) {
                        System.out.println(homeTeamStatistics.getString("country"));
                        System.out.println(playerObject.getString("name"));
                    }
                }

                System.out.println(homeTeamStatisticsJSONArray);

//                for (int j = 0; j < homeTeamStatistics.length(); j++) {
//                    JSONObject tacticsObject = homeTeamStatisticsJSONArray.getJSONObject(j);
//
//                    String tactic = tacticsObject.getString("tactic");
//
//                    System.out.println(homeTeamStatistics.getString("tactic"));
//
//                    if(tactic.equalsIgnoreCase("tactic")) {
//                        System.out.println(homeTeamStatistics.getString("tactic"));
//                        System.out.println(tacticsObject.getString("name"));
//                    }
//                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void disableSSL() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getMatchesToday() throws Exception {
        String url = "https://worldcup.sfg.io/matches/today";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
