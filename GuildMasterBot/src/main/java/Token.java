import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Token {
  public static String getToken() throws UnirestException {
    String usr = "ID Oculta"; //Client ID WoW API
    String pw = "ID Oculta"; //Client Secret WoW API
    HttpResponse<String> response;

    response = Unirest.post("https://us.battle.net/oauth/token")
            .basicAuth(usr, pw)
            .field("grant_type", "client_credentials")
            .asString();

    return response.getBody().split(":")[1].split("\"")[1]; //Guardo el token en una variable
  }
}
