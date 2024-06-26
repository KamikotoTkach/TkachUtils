package ru.cwcode.cwutils.messages;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.cwcode.cwutils.server.PaperServerUtils;

import java.util.Collection;

public class PaperMessage extends Message {
  private static PaperMessage instance;
  
  private PaperMessage() {
    this("empty");
  }
  
  public PaperMessage(String message) {
    super(message);
  }
  
  public PaperMessage(TextComponent message) {
    super(message);
  }
  
  public static PaperMessage getInstance(TextComponent message) {
    if (instance == null) {
      instance = new PaperMessage(message);
    }
    
    instance.set(message);
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
  
  @Override
  public void send(Audience receiver) {
    if (PaperServerUtils.isVersionBefore1_16_5()) {
      if (receiver instanceof CommandSender) {
        ((CommandSender) receiver).sendMessage(this.toString());
      }
      return;
    }
    
    receiver.sendMessage(this::get);
  }
  
  @Override
  public void send(String name, Collection<? extends Audience> receivers) {
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
  
  @Override
  public void sendActionBar(Audience receiver) {
    if (PaperServerUtils.isVersionBefore1_16_5()) {
      if (receiver instanceof Player) {
        ((Player) receiver).sendActionBar(this.toString());
      }
      return;
    }
    
    receiver.sendActionBar(this::get);
  }
  
  @Override
  public void sendActionBar(String name, Collection<? extends Audience> audiences) {
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
    return new PaperMessage(this.get());
  }
}
