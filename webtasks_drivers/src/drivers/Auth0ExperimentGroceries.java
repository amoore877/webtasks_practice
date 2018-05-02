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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Basic driver for proof of concept. Not intended as hallmark of engineering
 * skills.
 */
// reference https://webtask.io/docs/storage
public class Auth0ExperimentGroceries {

	public static void main(String[] args) {
		System.out.println("Start");

		boolean running = true;
		final BufferedReader cmdReader = new BufferedReader(new InputStreamReader(System.in));

		HttpClient client = HttpClientBuilder.create().build();

		while (running) {
			try {
				System.out.println("Enter LIST, DEL, PUT, or EXIT");

				String commandType = cmdReader.readLine();

				if (commandType.equalsIgnoreCase("EXIT")) {
					// exit
					System.out.println("User chose to exit.");
					running = false;
					continue;
				} else if (!commandType.equalsIgnoreCase("LIST") && !commandType.equalsIgnoreCase("DEL")
						&& !commandType.equalsIgnoreCase("PUT")) {
					// invalid command
					System.out.println("Invalid command! Aborting.");
					continue;
				}

				// command is either LIST or PUT or DEL, possibly mixed case;
				// throw to upper
				commandType = commandType.toUpperCase();

				// item list
				JsonArray jsonItemArray = new JsonArray();
				// get items for del or put
				if (commandType.equalsIgnoreCase("DEL") || commandType.equalsIgnoreCase("PUT")) {
					System.out.println(
							"You have chosen: [" + commandType + "].\nList all items in comma-separated format.");
					String itemsString = cmdReader.readLine();
					itemsString = itemsString.trim();
					if (itemsString.isEmpty()) {
						System.out.println("Invalid empty list. Aborting.");
						continue;
					} else if (itemsString.equals(",")) {
						System.out.println("Invalid comma-only list. Aborting.");
						continue;
					}

					// convert to array
					String[] itemsArr = itemsString.split(",");

					// item list
					jsonItemArray = new JsonArray();
					for (String item : itemsArr) {
						item = item.trim();
						if (item.isEmpty()) {
							System.out.println("Not adding empty item.");
						} else {
							System.out.println("Adding array item: [" + item + "]");
							jsonItemArray.add(item);
						}
					}
				}

				// request object
				HttpPost request = new HttpPost(
						"https://wt-ec5bd62a56de3fe62cbb4b8ec4ad3f6c-0.sandbox.auth0-extend.com/groceries");

				// json to hold all data
				JsonObject mainJson = new JsonObject();
				// command type
				mainJson.addProperty("cmd", commandType);
				// add list to main json
				mainJson.add("items", jsonItemArray);

				// final debug log before send
				System.out.println("JSON: " + mainJson.toString());

				StringEntity parameters = new StringEntity(mainJson.toString());
				request.addHeader("content-type", "application/json");
				request.setEntity(parameters);

				HttpResponse response = client.execute(request);

				System.out.println("Status: " + response.getStatusLine().getStatusCode());
				String line;
				StringBuilder result = new StringBuilder();
				BufferedReader responseReader = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));
				while ((line = responseReader.readLine()) != null) {
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

		System.out.println("Exited");
	}

}
