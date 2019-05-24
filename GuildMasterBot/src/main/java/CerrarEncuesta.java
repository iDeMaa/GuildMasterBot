import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.managers.GuildController;

import java.util.*;

public class CerrarEncuesta extends Command {
  public CerrarEncuesta() {
    this.name = "cerrarencuesta";
    this.aliases = new String[]{"cerrarencuesta"};
    this.help = "Cierra la ultima encuesta y devuelve los resultados";
  }

  protected void execute(CommandEvent event) {
    if (event.getMessage().getTextChannel().equals(event.getGuild().getTextChannelsByName("bot", true).get(0))) {
      event.getMessage().delete().complete();
      Guild guild = event.getGuild();
      GuildController guildController = guild.getController();
      List<Member> memberList = guild.getMembers();
      Role role = guild.getRolesByName("No voto", true).get(0);
      TextChannel textChannel = guild.getTextChannelsByName("encuestas", true).get(0);
      Message message = textChannel.getMessageById(textChannel.getLatestMessageId()).complete();
      String mensajeEmbebido = "";

      List<Member> listaAdmins = new ArrayList<>();
      for (int i = 0; i < memberList.size(); i++) {
        guildController.removeSingleRoleFromMember(memberList.get(i), role).queue();
        try {
          if (memberList.get(i).getRoles().get(0).getName().equalsIgnoreCase("Citric") || memberList.get(i).getRoles().get(0).getName().equalsIgnoreCase("Baggio")) {
            listaAdmins.add(memberList.get(i));
          }
        } catch (Exception e) {
          System.out.println(e);
        }
      }
      EmbedBuilder embedBuilder = new EmbedBuilder();
      for (int i = 0; i < message.getReactions().size(); i++) {
        mensajeEmbebido = message.getReactions().get(i).getCount() - 1 + " miembros votaron " + message.getReactions().get(i).getReactionEmote().getName() + "\n";
        ;
        for (int j = 0; j < message.getReactions().get(i).getUsers().complete().size(); j++) {
          if (!guild.getMember(message.getReactions().get(i).getUsers().complete().get(j)).getEffectiveName().equalsIgnoreCase("Guild Master")) {
            mensajeEmbebido += "    - " + guild.getMember(message.getReactions().get(i).getUsers().complete().get(j)).getEffectiveName() + "\n";
          }
        }
        embedBuilder.setDescription(mensajeEmbebido);
        for (int k = 0; k < listaAdmins.size(); k++) {
          sendPrivateMessage(listaAdmins.get(k).getUser(), embedBuilder.build());
        }
      }
      event.reply("Encuesta finalizada");
    } else {
      event.getMessage().delete().queue();
    }
  }

  public void sendPrivateMessage(User user, MessageEmbed content) {
    user.openPrivateChannel().queue((channel) ->
    {
      //Manda el mensaje privado
      channel.sendMessage(content).queue();
    });
  }
}