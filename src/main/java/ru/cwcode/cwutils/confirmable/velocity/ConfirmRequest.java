package ru.cwcode.cwutils.confirmable.velocity;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;

import java.time.Duration;

public class ConfirmRequest {
  Player sender;
  String required;
  long timeToExpire;
  ScheduledTask expiredTask = null;
  Runnable onSuccess = null;
  Runnable onExpired = null;
  
  /**
   * @param sender         отправитель
   * @param required       сообщение которое нужно отправить
   * @param millisToExpire время действия (в миллисекундах)
   */
  public ConfirmRequest(Player sender, String required, long millisToExpire) {
    this.sender = sender;
    this.required = required;
    this.timeToExpire = millisToExpire;
  }
  
  public void startTimer(ProxyServer server, Object plugin) {
    expiredTask = server.getScheduler().buildTask(plugin, () -> {
      if (onExpired != null) onExpired.run();
      ConfirmAPI.requests.remove(sender, this);
    }).delay(Duration.ofMillis(this.timeToExpire)).schedule();
  }
  
  public void stopTimer() {
    expiredTask.cancel();
  }
}
