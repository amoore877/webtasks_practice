package drivers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.JsonObject;

/**
 * Basic driver for proof of concept. Not intended as hallmark of engineering
 * skills.
 */
// reference https://webtask.io/docs/storage
public class Auth0ExperimentGroceries {

	public static void main(String[] args) {
		HttpClient client = HttpClientBuilder.create().build();

		try {
			HttpPost request = new HttpPost(
					"https://wt-ec5bd62a56de3fe62cbb4b8ec4ad3f6c-0.sandbox.auth0-extend.com/groceries");
			JsonObject json = new JsonObject();
			json.addProperty("name", "Andrew");
			json.addProperty("other", "blah");
			System.out.println("JSON: " + json.toString());
			StringEntity parameters = new StringEntity(json.toString());
			request.addHeader("content-type", "application/json");
			request.setEntity(parameters);

			HttpResponse response = client.execute(request);

			System.out.println("Status: " + response.getStatusLine().getStatusCode());

			String line;
			StringBuilder result = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
			System.out.println("Response content: " + result);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
