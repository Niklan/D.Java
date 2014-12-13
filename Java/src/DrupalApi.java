import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Drupal API for RESTful services.
 */
public class DrupalApi {

    /**
     * Constants.
     */
    private final String USER_AGENT = "Mozilla/5.0";
    private final String CONTENT_TYPE = "application/json";
    private final String ACCEPT = "application/json";


    /**
     * Send HTTP Post request to RESTful server.
     *
     * @param apiRequest
     * @param data
     * @return
     */
    private JSONObject sendRequest(String apiRequest, JSONObject data) throws Exception {

        String url = Globals.drupalWebsiteUrl + apiRequest;

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        /**
         * Set headers to request.
         */
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Content-Type", CONTENT_TYPE);
        post.setHeader("Accept", ACCEPT);

        /**
         * Set data to send.
         */
        StringEntity sendData = new StringEntity(data.toString());
        post.setEntity(sendData);

        HttpResponse response = client.execute(post);

        /**
         * Request statuses:
         *  - 200 - OK.
         *  - 401 - No access (wrong username or password).
         */
        Integer responseStatus = response.getStatusLine().getStatusCode();

        JSONObject resultJSON = new JSONObject();
        resultJSON.put("responseStatus", responseStatus);

        if (responseStatus == 200) {
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            String resultString = result.toString();
            JSONObject resultRequest = (JSONObject) JSONValue.parse(resultString);
            resultJSON.putAll(resultRequest);
        }

        return resultJSON;

    }

    /**
     * Authorization on Drupal site.
     *
     * @param data - username
     *             - password
     * @return - TRUE - if authorized.
     * - FALSE - if failed.
     */
    public boolean auth(JSONObject data) throws Exception {

        JSONObject authData = data;
        JSONObject authResult = sendRequest("api/user/login.json", authData);

        if ((Integer) authResult.get("responseStatus") == 200) {
            // All returned data.
            //System.out.println(authResult.toString());
            JOptionPane.showMessageDialog(null, "Authorization successful. Session ID: " + authResult.get("sessid"));
            return true;
        }
        else {
            JOptionPane.showMessageDialog(null, "Wrong username or password.");
            return false;
        }

    }
}
