import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.managers.GuildController;
import org.json.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registrar extends Command {

  public Registrar() {
    this.name = "registrar";
    this.aliases = new String[]{"registrar"};
    this.help = "registra a un usuario";
  }

  protected void execute(CommandEvent event) {
    if (event.getMessage().getTextChannel().getName().equalsIgnoreCase("identifiquese")) {
      //event.getMessage().delete().queue();
      Guild guild = event.getGuild(); //Obtiene el objeto Guild (servidor)
      GuildController guildController = guild.getController();
      String message = event.getMessage().getContentDisplay();
      Member member = event.getMessage().getMember();
      String nombreAnterior = member.getEffectiveName();
      String nickname = message.split(" ")[1];
      String nombreArreglado = nickname;
      Pattern p = Pattern.compile("[^a-z]", Pattern.CASE_INSENSITIVE);
      Matcher m = p.matcher(nickname);
      boolean b = m.find();
      if (b) {
        nombreArreglado = nickname.replaceFirst(m.group(0), "..");
      }
      Role role = null;
      try {
		String token = Token.getToken();
        HttpResponse<JsonNode> response;
        response = Unirest.get("https://us.api.blizzard.com/wow/guild/quelthalas/divine juice?fields=members&locale=en_US&access_token=" + token).asJson();
        JSONArray arrayMiembros = response.getBody().getObject().getJSONArray("members");
        int roleIndex = -1;
        int i;
        System.out.println(arrayMiembros.length());
        for (i = 0; i < arrayMiembros.length(); i++) {
          if (arrayMiembros.getJSONObject(i).getJSONObject("character").getString("name").matches(nombreArreglado)) {
            roleIndex = arrayMiembros.getJSONObject(i).getInt("rank");
            switch (roleIndex) {
              case 0:
                role = guild.getRolesByName("Citric", true).get(0);
                break;
              case 1:
                role = guild.getRolesByName("Baggio", true).get(0);
                break;
              case 2:
                role = guild.getRolesByName("Multifruta", true).get(0);
                break;
              case 3:
                role = guild.getRolesByName("Levite", true).get(0);
                break;
              case 4:
                role = guild.getRolesByName("Aquarius", true).get(0);
                break;
              case 5:
                role = guild.getRolesByName("Rinde 2", true).get(0);
                break;
              case 6:
                role = guild.getRolesByName("La Jaula", true).get(0);
                break;
              case 7:
                role = guild.getRolesByName("Cepita", true).get(0);
                break;
              default:
                role = null;
                break;
            }
            break;
          } else {
            role = guild.getRolesByName("Invitado", true).get(0);
          }
        }
        if (roleIndex == 0 || roleIndex == 1 || roleIndex == 2) {
          try {
            if (message.split(" ")[2].equalsIgnoreCase("322")) {
              cambiarMiembro(event, guild, guildController, member, nombreAnterior, role, nickname, i);
            } else {
              event.reply("Contraseña incorrecta");
            }
          } catch (IndexOutOfBoundsException iobe) {
            event.reply("Este usuario requiere contraseña");
          }
        } else {
          cambiarMiembro(event, guild, guildController, member, nombreAnterior, role, nickname, i);
        }
      } catch (Exception e) {
        System.out.println(e);
      }
    }
  }

  private void cambiarMiembro(CommandEvent event, Guild guild, GuildController guildController, Member member, String nombreAnterior, Role role, String nombre, int i) {
    guildController.addSingleRoleToMember(member, role).queue();
    guildController.removeSingleRoleFromMember(member, guild.getRolesByName("No registrado", true).get(0)).queue();
    guildController.setNickname(member, nombre).queue();
    event.reply(nombreAnterior + ", fuiste registrado como \"" + nombre + "\" con el rol \"" + role.getName() + "\"");
  }
}