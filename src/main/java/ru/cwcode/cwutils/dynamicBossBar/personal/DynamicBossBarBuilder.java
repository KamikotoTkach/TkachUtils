package ru.cwcode.cwutils.dynamicBossBar.personal;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import ru.cwcode.cwutils.server.PaperServerUtils;

import java.util.UUID;
import java.util.function.Supplier;

public class DynamicBossBarBuilder {
  private UUID uuid = UUID.randomUUID();
  private Supplier<Component> title;
  private Supplier<Float> progress = () -> 1.0f;
  private Supplier<Boolean> shouldRemove;
  private Supplier<Boolean> shouldDisplay = () -> true;
  private Supplier<BossBar.Color> color = () -> BossBar.Color.WHITE;
  private Supplier<BossBar.Overlay> overlay = () -> BossBar.Overlay.PROGRESS;
  
  public DynamicBossBarBuilder setUuid(@NotNull UUID uuid) {
    this.uuid = uuid;
    return this;
  }
  
  public DynamicBossBarBuilder setTitle(@NotNull Supplier<Component> title) {
    this.title = title;
    return this;
  }
  
  public DynamicBossBarBuilder setProgress(@NotNull Supplier<Float> progress) {
    this.progress = progress;
    return this;
  }
  
  public DynamicBossBarBuilder setShouldRemove(@NotNull Supplier<Boolean> shouldRemove) {
    this.shouldRemove = shouldRemove;
    return this;
  }
  
  public DynamicBossBarBuilder setShouldDisplay(@NotNull Supplier<Boolean> shouldDisplay) {
    this.shouldDisplay = shouldDisplay;
    return this;
  }
  
  public DynamicBossBarBuilder setColor(@NotNull Supplier<BossBar.Color> color) {
    this.color = color;
    return this;
  }
  
  public DynamicBossBarBuilder setOverlay(@NotNull Supplier<BossBar.Overlay> overlay) {
    this.overlay = overlay;
    return this;
  }
  
  public DynamicBossBar build() {
    return PaperServerUtils.isVersionGreater_1_16_5() ?
       new DynamicBossBar(uuid, title, progress, shouldRemove, shouldDisplay, color, overlay)
       : new DynamicBossBar_v1_16_5(uuid, title, progress, shouldRemove, shouldDisplay, color, overlay);
  }
}