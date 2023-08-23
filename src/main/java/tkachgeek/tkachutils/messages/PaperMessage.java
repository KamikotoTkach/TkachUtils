package tkachgeek.tkachutils.messages;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import tkachgeek.tkachutils.server.ServerUtils;

import java.util.Collection;

public class PaperMessage extends Message {
   private static PaperMessage instance;

   public static PaperMessage getInstance(TextComponent message) {
      if (instance == null) {
         instance = new PaperMessage(message);
      }

      instance.message = message;
      return instance;
   }

   public static PaperMessage getInstance(String message) {
      return PaperMessage.getInstance(Message.from(message));
   }

   public static PaperMessage getInstance(Component component) {
      if (component instanceof TextComponent) {
         return PaperMessage.getInstance((TextComponent) component);
      } else {
         return PaperMessage.getInstance(Message.from(component));
      }
   }

   public PaperMessage(String message) {
      super(message);
   }

   public PaperMessage(TextComponent message) {
      super(message);
   }

   public void send(Player player) {
      if (ServerUtils.isVersionBefore1_16_5()) {
         player.sendMessage(this.message);
         return;
      }

      player.sendMessage(this::get);
   }

   @Override
   public void send(Audience player) {
      if (player instanceof Player) {
         this.send((Player) player);
      }
   }

   @Override
   public void send(String name, Collection<Audience> receivers) {
      for (Audience audience : receivers) {
         if (audience instanceof Player) {
            Player player = (Player) audience;
            if (player.getName().equals(name)) {
               this.send(player);
               return;
            }
         }
      }
   }

   public void sendActionBar(Player player) {
      if (ServerUtils.isVersionBefore1_16_5()) {
         player.sendActionBar(this.message);
         return;
      }

      player.sendActionBar(this::get);
   }

   @Override
   public void sendActionBar(Audience player) {
      if (player instanceof Player) {
         this.sendActionBar((Player) player);
      }
   }

   @Override
   public void sendActionBar(String name, Collection<Audience> audiences) {
      for (Audience audience : audiences) {
         if (audience instanceof Player) {
            Player player = (Player) audiences;
            if (player.getName().equals(name)) {
               this.sendActionBar(player);
               return;
            }
         }
      }
   }

   @Override
   protected Message clone() {
      return new PaperMessage(this.message);
   }
}
