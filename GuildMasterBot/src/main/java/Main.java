import com.jagrosh.jdautilities.command.*;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;

public class Main {

  public static void main(String[] args) throws LoginException, IllegalArgumentException, UnirestException {

    CommandClientBuilder client = new CommandClientBuilder();
    client.setGame(Game.playing("World of Warcraft")); //Setea el juego del Bot
    client.setOwnerId("137013304961794048"); //ID de usuario del dueño
    client.setPrefix("."); //Prefijo del bot

    //Método que contiene todos los comandos
    client.addCommands(
            //new Prueba(),
            new Registrar(),
            new Update(),
            new CrearEncuesta(),
            new CerrarEncuesta()
    );

    new JDABuilder(AccountType.BOT) //Constructor que crea al bot y lo enciende (build())
        //    .setToken("Token Oculto") //Token Bot Original
        //    .setToken("Token Oculto") //Token Bot Prueba
            .addEventListener(client.build())
            .addEventListener(new Listener())
            .build();
  }
}