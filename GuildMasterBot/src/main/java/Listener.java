import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;
//import net.dv8tion.jda.core.hooks.ListenerAdapter.onMessageReceived;

public class Listener extends ListenerAdapter {

  public void onGuildMemberJoin(GuildMemberJoinEvent event){
    Member member = event.getMember();
    Guild guild = event.getGuild();
    TextChannel channel = guild.getTextChannelsByName("identifiquese", true).get(0); //Obtiene el primer canal llamado "identifiquese", ignorando caps
    Role role = guild.getRolesByName("No registrado", true).get(0); //Obtiene el primer rango "No registrado", ignorando caps
    GuildController guildController = new GuildController(guild); //Crea el guild controller
    guildController.addSingleRoleToMember(member, role).complete(); //Asigna el rol al miembro
    try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          System.out.println(e);
        }
	channel.sendMessage("Bienvenido, <@"+member.getUser().getId()+">, para acceder a los canales del servidor tenes que registrarte usando:").complete(); //Envía el mensaje de bienvenida
    EmbedBuilder eb = new EmbedBuilder(); //Crea el mensaje embebido.
    eb.setDescription(".registrar [nombre de personaje]");
    channel.sendMessage(eb.build()).complete(); //Envía el mensaje embebido
  }

  public void onMessageReactionAdd(MessageReactionAddEvent event) {
    super.onMessageReactionAdd(event);
    Member member= event.getMember();
    Role role = event.getGuild().getRolesByName("No voto", true).get(0);
    TextChannel textChannel = event.getTextChannel();
    TextChannel encuestasChannel = event.getGuild().getTextChannelsByName("encuestas", true).get(0);
    if (textChannel.getLatestMessageId().equalsIgnoreCase(event.getMessageId())){ //Si reacciona al último mensaje
      if (textChannel.getId().equalsIgnoreCase(encuestasChannel.getId())) { //Si reacciona en el canal "encuesta"
        GuildController guildController = event.getGuild().getController();
        guildController.removeSingleRoleFromMember(member, role).complete(); //Le saco el rol
      }
    }
  }
  
/*  public void onMessageReceived(MessageReceivedEvent event)
     {
	  if (event.getMessage().getTextChannel().equals(event.getGuild().getTextChannelsByName("bot", true).get(0))){
		  event.reply("It works :D");
	  }
  }*/
}
