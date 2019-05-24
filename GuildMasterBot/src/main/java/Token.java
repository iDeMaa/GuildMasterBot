import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Token {
  public static String getToken() throws UnirestException {
    String usr = "73039285127f4c70891b1670003054cb"; //Client ID WoW API
    String pw = "8K3LODtVYxYDiZFz5pSy27NeM91MR2nG"; //Client Secret WoW API
    HttpResponse<String> response;

    response = Unirest.post("https://us.battle.net/oauth/token")
            .basicAuth(usr, pw)
            .field("grant_type", "client_credentials")
            .asString();

    return response.getBody().split(":")[1].split("\"")[1]; //Guardo el token en una variable
  }
}
