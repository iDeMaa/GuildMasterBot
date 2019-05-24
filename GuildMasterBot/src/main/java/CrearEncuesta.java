import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.managers.GuildController;

import java.util.List;

public class CrearEncuesta extends Command {

  public CrearEncuesta() {
    this.name = "encuesta";
    this.aliases = new String[]{"encuesta"};
    this.help = "Crea una encuesta";
  }

  protected void execute(CommandEvent event) {
    if (event.getMessage().getTextChannel().equals(event.getGuild().getTextChannelsByName("bot", true).get(0))) {
      if (event.getMessage().getMember().getRoles().get(0).getName().equalsIgnoreCase("Baggio") || event.getMessage().getMember().getRoles().get(0).getName().equalsIgnoreCase("Citric")) { //Verifico si es admin
        Guild guild = event.getGuild();
        GuildController guildController = guild.getController();
        Role noVotoRole = guild.getRolesByName("No voto", true).get(0); //Rol "No voto"
        String encuesta = event.getMessage().getContentDisplay().split("\"")[1]; //Guardo el mensaje de la encuesta
        TextChannel channel = guild.getTextChannelsByName("bot", true).get(0);
        TextChannel channelEncuesta = guild.getTextChannelsByName("encuestas", true).get(0); //Obtengo el canal de encuestas

        List<Member> membersList = guild.getMembers(); //Obtengo la lista de miembros
        for (int i = 0; i < membersList.size(); i++) { //Por cada miembro
          if (!membersList.get(i).getRoles().get(0).getName().equalsIgnoreCase("Citric") && //Si no es Citric
                  !membersList.get(i).getRoles().get(0).getName().equalsIgnoreCase("Baggio") && //Bagio
                  !membersList.get(i).getRoles().get(0).getName().equalsIgnoreCase("Guild Master") && //GuildMaster
                  !membersList.get(i).getRoles().get(0).getName().equalsIgnoreCase("Invitado")) { //O invitado
            guildController.addSingleRoleToMember(membersList.get(i), noVotoRole).queue(); //Le pongo el rol "No voto"
          }
        }

        try {
          Thread.sleep(20000); //Espero un tiempo a que el que creo la encuesta agregue reacciones al mensaje
        } catch (InterruptedException e) {
          System.out.println(e);
        }

        Message msg = channel.getMessageById(channel.getLatestMessageId()).complete(); //Obtengo el mensaje que envio el creador de la encuesta
        List<MessageReaction> msgReactions = msg.getReactions(); //Obtengo las reacciones de dicho mensaje
        channelEncuesta.sendMessage("@everyone se creo una encuesta").queue(); //Informo al servidor que la encuesta se creo
        EmbedBuilder eb = new EmbedBuilder(); //Crea el mensaje embebido.
        eb.setDescription("\"" + encuesta + "\""); //Asigno el mensaje al mensaje embebido
        msg = channelEncuesta.sendMessage(eb.build()).complete(); //Envio el mensaje embebido y a la vez lo guardo en una variable tipo Message
        for (int i = 0; i < msgReactions.size(); i++) { //Por cada reaccion
          msg.addReaction(msgReactions.get(i).getReactionEmote().getName()).queue(); //Se la pongo al mensaje embebido
        }
      }
      event.getMessage().delete().queue(); //Borro el mensaje del usuario
    } else {
      event.getMessage().delete().queue();
    }
  }
}