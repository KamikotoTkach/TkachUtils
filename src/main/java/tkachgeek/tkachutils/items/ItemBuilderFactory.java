package tkachgeek.tkachutils.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tkachgeek.tkachutils.server.ServerUtils;

public class ItemBuilderFactory {
  public static ItemBuilder of(Material material) {
    if (ServerUtils.isVersionBefore1_16_5()) {
      return new ItemBuilderLegacy(material);
    }
    return new ItemBuilder(material);
  }
  
  public static ItemBuilder of(ItemStack itemStack) {
    if (ServerUtils.isVersionBefore1_16_5()) {
      return new ItemBuilderLegacy(itemStack);
    }
    return new ItemBuilder(itemStack);
  }
}
