import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class ContextPOST implements HttpHandler {

    static String latestRequestBasicURL = "http://api.exchangeratesapi.io/v1/latest?access_key=c85220d14f38f870bae88e4bda422722&format=1";
    static String historyRequestBasicURL = "http://api.exchangeratesapi.io/v1/";
    static String ApiAccessKey = "c85220d14f38f870bae88e4bda422722";
    static String[] additionalTargetCurrencies = {"USD", "GBP", "EUR", "CAD", "PLN", "CHF"};

    static String postResponseHtmlBeginning = "<html>\n" +
            "<body>\n";

    static String postResponseHtmlEnding = "\n" +
            "</body>\n" +
            "</html>";

    public static String[] parseParams(String content) {
        String[] to_return = {null, null, null};

        String[] params = content.split("&");
        to_return[0] = params[0].split("=")[1];
        to_return[1] = params[1].split("=")[1];
        to_return[2] = params[2].split("=")[1];

        return to_return;
    }

    public static String getSymbolsForLatestRequest(String from, String to) {
        String targets = to;
        for (String curr: additionalTargetCurrencies) {
            if (!curr.equals(from) && !to.contains(curr)) {
                targets += "," + curr;
            }
        }
        return targets;
    }

    public static String[] getDatesWithPreviousTwoDays(String baseDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = dateFormat.parse(baseDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime (date1);
        cal.add(Calendar.DATE, -1);
        Date date2 = cal.getTime();
        cal.add(Calendar.DATE, -1);
        Date date3 = cal.getTime();

        String[] dates = {null, null, null};
        dates[0] = baseDate;
        dates[1] = dateFormat.format(date2);
        dates[2] = dateFormat.format(date3);

        return dates;
    }

    public static CompletableFuture<HttpResponse<String>> createAndSendLatestRequest(HttpClient httpClient, String from, String to, String date) {
        String symbols = getSymbolsForLatestRequest(from, to);
        HttpRequest latestRequest = HttpRequest.newBuilder(
                URI.create(latestRequestBasicURL + "&base=" + from + "&symbols=" + symbols))
                .header("accept", "application/json")
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = httpClient.sendAsync(latestRequest, HttpResponse.BodyHandlers.ofString());

        return responseFuture;
    }

    public static CompletableFuture<HttpResponse<String>> createAndSendHistoryRequest(HttpClient httpClient, String from, String to, String date) throws ParseException {
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(historyRequestBasicURL + date + "?access_key=" + ApiAccessKey + "&format=1&base=" + from + "&symbols=" + to))
                .header("accept", "application/json")
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        return responseFuture;
    }

    public static String getHtmlForLatestResponse(JSONObject latest, String to) throws JSONException {
        String latestHtml = "<h2>Latest currency info for given and most popular currencies</h2>\n";
        JSONObject rates = latest.getJSONObject("rates");
        String from = latest.getString("base");
        latestHtml += "Base: " + from + "<br><br>\n";

        for (String curr: to.split("%2C")) {
            latestHtml += curr + ": " + rates.get(curr) + "<br>\n";
        }

        for (String curr: additionalTargetCurrencies) {
            if (!curr.equals(from) && !to.contains(curr)) {
                latestHtml += curr + ": " + rates.get(curr) + "<br>\n";
            }
        }

        return latestHtml;
    }

    public static String getHtmlForHistoryData(JSONObject basicDate, JSONObject dayBefore, JSONObject twoDaysBefore, String to) throws JSONException {
        String historyHtml = "<h2>History data </h2>\n";

        historyHtml += "Base: " + basicDate.getString("base") + "<br>\n";

        for (String curr: to.split("%2C")) {
            historyHtml += "Target currency: " + curr + "<br><br>\n";

            double value1 = basicDate.getJSONObject("rates").getDouble(curr);
            double value2 = dayBefore.getJSONObject("rates").getDouble(curr);
            double value3 = twoDaysBefore.getJSONObject("rates").getDouble(curr);

            historyHtml += basicDate.getString("date") + ": \t" + value1 + "<br>\n";
            historyHtml += dayBefore.getString("date") + ": \t" + value2 + "<br>\n";
            historyHtml += twoDaysBefore.getString("date") + ": \t" + value3 + "<br><br>\n";

            historyHtml += "Mean from previous three days: " + ((value1 + value2 + value3) / 3) + "<br><br>\n";
        }

        return historyHtml;
    }

    public static String getHtmlForPostResponse(String latestHtml, String historyHtml) {
        String responseHtml = postResponseHtmlBeginning;

        responseHtml += latestHtml;
        responseHtml += historyHtml;

        responseHtml += postResponseHtmlEnding;

        return responseHtml;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if ("POST".equals(exchange.getRequestMethod())) {
            System.out.println("Post message.");
            String contents = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

            String[] params = parseParams(contents);
//            System.out.println("From: " + params[0]);
//            System.out.println("To: " + params[1]);
//            System.out.println("Currency: " + params[2]);

            HttpClient httpClient = HttpClient.newHttpClient();
            // create requests

            String toResponse = "";
            try {
                String[] dates = getDatesWithPreviousTwoDays(params[2]);

                CompletableFuture<HttpResponse<String>> responseFutureLatest = createAndSendLatestRequest(httpClient, params[0], params[1], params[2]);
                CompletableFuture<HttpResponse<String>> responseFutureHistory1 = createAndSendHistoryRequest(httpClient, params[0], params[1], dates[0]);
                CompletableFuture<HttpResponse<String>> responseFutureHistory2 = createAndSendHistoryRequest(httpClient, params[0], params[1], dates[1]);
                CompletableFuture<HttpResponse<String>> responseFutureHistory3 = createAndSendHistoryRequest(httpClient, params[0], params[1], dates[2]);

                HttpResponse<String> responseLatest = responseFutureLatest.get();
                JSONObject latest = new JSONObject(responseLatest.body());

                String latestResponse = getHtmlForLatestResponse(latest, params[1]);

                HttpResponse<String> responseHistory1 = responseFutureHistory1.get();
                HttpResponse<String> responseHistory2 = responseFutureHistory2.get();
                HttpResponse<String> responseHistory3 = responseFutureHistory3.get();

                JSONObject history1 = new JSONObject(responseHistory1.body());
                JSONObject history2 = new JSONObject(responseHistory2.body());
                JSONObject history3 = new JSONObject(responseHistory3.body());

                String historyResponse = getHtmlForHistoryData(history1, history2, history3, params[1]);

                toResponse = getHtmlForPostResponse(latestResponse, historyResponse);

            } catch (Exception e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(405, -1);
            }

            exchange.sendResponseHeaders(200, toResponse.length());
            OutputStream output = exchange.getResponseBody();
            output.write(toResponse.getBytes());
            output.flush();

        } else {
            exchange.sendResponseHeaders(405, -1);  // 405 Method Not Allowed
        }
    }
}
