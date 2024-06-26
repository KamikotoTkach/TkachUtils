package ru.cwcode.cwutils.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class Scheduler<T> extends AbstractScheduler {
  private final T anything;
  private volatile Consumer<T> action = (x) -> {};
  private volatile Consumer<T> lastlyAction = (x) -> {};
  private volatile Predicate<T> condition = null;
  
  protected Scheduler(T anything) {
    this.anything = anything;
  }
  
  public static <T> Scheduler<T> create(T anything) {
    return new Scheduler<T>(anything);
  }
  
  public static VoidScheduler create() {
    return VoidScheduler.create();
  }
  
  /**
   * Действие
   */
  public Scheduler<T> perform(Consumer<T> action) {
    this.action = action;
    return this;
  }
  
  /**
   * Условие, при котором будет совершаться действие
   */
  public Scheduler<T> until(Predicate<T> condition) {
    this.condition = condition;
    return this;
  }
  
  /**
   * Совершать действие асинхронно
   */
  public Scheduler<T> async() {
    this.asyncTask = true;
    this.blocked = true;
    return this;
  }
  
  public Scheduler<T> async(boolean async) {
    this.asyncTask = async;
    this.blocked = async || blocked;
    return this;
  }
  
  /**
   * Не останавливать выполнение, если условие не соблюдено. В таком случае действие не будет выполняться, если условие false
   */
  public Scheduler<T> infinite() {
    this.infinite = true;
    return this;
  }
  
  public Scheduler<T> infinite(boolean infinite) {
    this.infinite = infinite;
    return this;
  }
  
  /**
   * Действие, выполняющееся вместо основного, когда условие false
   */
  public Scheduler<T> otherwise(Consumer<T> lastlyAction) {
    this.lastlyAction = lastlyAction;
    return this;
  }
  
  /**
   * Регистрирует и запускает планировщик
   */
  public BukkitTask registerTask(JavaPlugin plugin, long delay) {
    BukkitTask task;
    if (asyncTask) {
      task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::tick, delay, delay);
    } else {
      task = Bukkit.getScheduler().runTaskTimer(plugin, this::tick, delay, delay);
    }
    
    registrant = plugin;
    taskId = task.getTaskId();
    
    Tasks.put(id, this);
    
    return task;
  }
  
  /**
   * Регистрирует и запускает планировщик
   */
  public int register(JavaPlugin plugin, long delay) {
    return registerTask(plugin, delay).getTaskId();
  }
  
  private void tick() {
    if (blocked && running) return;
    
    if (condition == null) {
      runWithCancelling(action);
    } else if (condition.test(anything)) {
      run(action);
    } else {
      runWithCancelling(lastlyAction);
    }
  }
  
  private void runWithCancelling(Consumer<T> action) {
    try {
      running = true;
      
      action.accept(anything);
      if (!infinite) Tasks.cancelTask(id);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      running = false;
    }
  }
  
  private void run(Consumer<T> action) {
    try {
      running = true;
      
      action.accept(anything);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      running = false;
    }
  }
  
  public static void runSync(@NotNull final JavaPlugin plugin, @NotNull final Runnable runnable) {
    if (Bukkit.isPrimaryThread()) {
      runnable.run();
    } else {
      Bukkit.getScheduler().runTask(plugin, runnable);
    }
  }
}
