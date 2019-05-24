import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.EmbedBuilder;
import org.json.JSONArray;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Update extends Command {

  public Update() {
    this.name = "update";
    this.aliases = new String[]{"update"};
    this.help = "Updatea los rangos de la guild";
  }

  protected void execute(CommandEvent event) {
    if (event.getMessage().getTextChannel().equals(event.getGuild().getTextChannelsByName("bot", true).get(0))) {
      Guild guild = event.getGuild();
      GuildController gc = new GuildController(guild);
      List<Member> memberList = guild.getMembers();
	  String updatedMember = "";
      EmbedBuilder eb = new EmbedBuilder(); //Crea el mensaje embebido.
      try {
		String token = Token.getToken();
        HttpResponse<JsonNode> response;
        response = Unirest.get("https://us.api.blizzard.com/wow/guild/quelthalas/divine juice?fields=members&locale=en_US&access_token=" + token).asJson();
        JSONArray arrayMiembros = response.getBody().getObject().getJSONArray("members");
        Role role;
        //Por cada miembro en el Discord
        for (int i = 0; i < memberList.size() - 1; i++) {
          //Busco si está en la guild
          for (int k = 0; k < arrayMiembros.length() - 1; k++) {
            String nickname=memberList.get(i).getEffectiveName();
            String nombreArreglado=nickname;
            Pattern p = Pattern.compile("[^a-z]", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(nickname);
            boolean b = m.find();
            if (b) {
              nombreArreglado = nickname.replaceFirst(m.group(0), "..");
            }
            //Verifico si el nombre del miembro está en la guild
   //         if (memberList.get(i).getEffectiveName().equalsIgnoreCase(arrayMiembros.getJSONObject(k).getJSONObject("character").getString("name"))) {
              if(arrayMiembros.getJSONObject(k).getJSONObject("character").getString("name").matches(nombreArreglado)){
              //Obtengo el rol en la guild
              int roleIndex = arrayMiembros.getJSONObject(k).getInt("rank");
              //Obtengo el rango del Discord según el rol en la guild
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
                  role = guild.getRolesByName("Invitado", true).get(0);
                  break;
              }
			//Verifico si el rol del Discord y el rango en la guild NO coinciden
			if (!memberList.get(i).getRoles().get(0).getName().equalsIgnoreCase(role.getName())) {
			  updatedMember += memberList.get(i).getEffectiveName() + ": " + memberList.get(i).getRoles().get(0).getName() + " -> " + role.getName() + "\n";
			  //Saco rango desactualizado
			  gc.removeSingleRoleFromMember(memberList.get(i), memberList.get(i).getRoles().get(0)).queue();
			  //Agrego rango nuevo
			  gc.addSingleRoleToMember(memberList.get(i), role).queue();
			}
              //Break para que si encuentra al miembro, no termine de pasar por el resto de los miembros
			break;
            }
          }
        }
		if(!updatedMember.equalsIgnoreCase("")) {
		  event.reply("Update terminada. Rangos actualizados:");
          eb.setDescription(updatedMember);
		  event.reply(eb.build());
        } else {
          event.reply("Update terminada. No se actualizó ningún rango");
        }
      } catch (Exception e) {
        System.out.println(e + "\t" + e.getStackTrace()[0].getLineNumber());
      }
    } else {
      event.getMessage().delete().queue();
    }
  }
}