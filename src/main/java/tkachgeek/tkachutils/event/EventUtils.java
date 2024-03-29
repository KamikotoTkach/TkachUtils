package tkachgeek.tkachutils.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class EventUtils {
  public static boolean canInteract(Player player, Location location) {
    return new PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, new ItemStack(Material.AIR), location.getBlock(), BlockFace.SELF, EquipmentSlot.OFF_HAND).callEvent();
  }
  
  public static boolean canPlace(Player player, Location location) {
    return new BlockPlaceEvent(location.getBlock(), location.getBlock().getState(), location.getBlock(), player.getInventory().getItemInMainHand(), player, true, EquipmentSlot.HAND).callEvent();
  }
  
  public static boolean canBreak(Player player, Location location) {
    return new BlockBreakEvent(location.getBlock(), player).callEvent();
  }
}
