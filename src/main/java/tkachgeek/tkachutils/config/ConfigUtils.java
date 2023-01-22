package tkachgeek.tkachutils.config;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tkachgeek.tkachutils.messages.Message;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static tkachgeek.tkachutils.config.ItemStackConstituents.*;

public class ConfigUtils {
   public static String getPath(String root, String... keys) {
      StringBuilder path = new StringBuilder(root);
      for (String key : keys) {
         path.append(".");
         path.append(key);
      }

      return path.toString();
   }

   public static Component getComponent(String json) {
      Component component;
      if (json == null || json.isEmpty()) {
         component = Component.text("Предмет");
      } else {
         try {
            if (json.matches("^\\{.*}$")) {
               component = GsonComponentSerializer.gson().deserialize(json);
            } else {
               component = LegacyComponentSerializer.legacyAmpersand().deserialize(json.replace("§", "&"));
            }
         } catch (Exception exception) {
            component = Component.text(json).style(Style.style(TextColor.color(255, 255, 255)));
         }
      }
      return component.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
   }

   public static Component[] getComponents(List<String> json) {
      Component[] components;
      if (json.isEmpty()) {
         components = new Component[1];
         components[0] = Component.text("Предмет").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
         ;
      } else {
         components = new Component[json.size()];
         for (int i = 0; i < json.size(); i++) {
            try {
               if (json.get(i).matches("^\\{.*}$")) {
                  components[i] = GsonComponentSerializer.gson().deserialize(json.get(i)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
               } else {
                  components[i] = LegacyComponentSerializer.legacyAmpersand().deserialize(json.get(i).replace("§", "&")).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
               }
            } catch (Exception exception) {
               components[i] = Component.text(json.get(i)).style(Style.style(TextColor.color(255, 255, 255))).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
            }
         }
      }
      return components;
   }

   public static ItemStack loadItemStack(File file, YamlConfiguration config, String path) {
      String full_path;
      full_path = ConfigUtils.getPath(path, type.name());
      String item_type_name = config.getString(full_path, "dirt").toUpperCase();
      Material item_type = Material.getMaterial(item_type_name);
      if (item_type == null || item_type.isAir()) {
         item_type = Material.DIRT;
         new Message("§c[§4" + path + "§c]: предмет §4" + item_type_name + "§c не существует!")
               .send(Bukkit.getConsoleSender());
      }

      full_path = ConfigUtils.getPath(path, amount.name());
      int item_amount = Math.max(Math.min(config.getInt(full_path, 1), 64), 1);

      ItemStack item = new ItemStack(item_type, item_amount);
      ItemMeta item_meta = item.getItemMeta();

      full_path = ConfigUtils.getPath(path, name.name());
      if (config.contains(full_path)) {
         Component item_name = ConfigUtils.getComponent(config.getString(full_path));
         item_meta.displayName(item_name);
      }

      full_path = ConfigUtils.getPath(path, lore.name());
      if (config.contains(full_path)) {
         Component[] item_lore = ConfigUtils.getComponents(config.getStringList(full_path));
         item_meta.lore(Arrays.asList(item_lore));
      }

      full_path = ConfigUtils.getPath(path, model.name());
      if (config.contains(full_path)) {
         int model = Math.max(config.getInt(full_path, 0), 0);
         item_meta.setCustomModelData(model);
      }

      if (item_meta instanceof PotionMeta) {
         if (config.contains(ConfigUtils.getPath(path, effects.name()))) {
            for (String potion_effect : config.getConfigurationSection(ConfigUtils.getPath(path, effects.name())).getKeys(false)) {
               PotionEffectType effect_type = PotionEffectType.getByName(potion_effect.toUpperCase());
               if (effect_type != null) {
                  int duration = Math.max(config.getInt(ConfigUtils.getPath(path, effects.name(), potion_effect, "duration"), 1), 1) * 20;
                  int level = Math.max(config.getInt(ConfigUtils.getPath(path, effects.name(), potion_effect, "level"), 1), 1) - 1;
                  ((PotionMeta) item_meta).addCustomEffect(new PotionEffect(effect_type, duration, level), false);
               } else {
                  new Message("§c[§4" + path + "§c]: эффект §4" + potion_effect + "§c не существует!")
                        .send(Bukkit.getConsoleSender());
               }
            }
         }
      }

      if (item_meta instanceof SkullMeta) {
         full_path = ConfigUtils.getPath(path, head_base64.name());
         if (config.contains(full_path)) {
            String base64 = config.getString(full_path, "null");

            full_path = ConfigUtils.getPath(path, head_uuid.name());

            UUID uuid = null;
            if (config.contains(full_path)) {
               try {
                  uuid = UUID.fromString(config.getString(full_path, "null"));
               } catch (Exception ignored) {

               }
            }

            if (uuid == null) {
               uuid = UUID.randomUUID();
               config.set(full_path, uuid.toString());
               ConfigUtils.saveConfig(file, config);
            }

            PlayerProfile profile = Bukkit.createProfile(uuid, "Head");
            profile.getProperties().add(new ProfileProperty("textures", base64));
            ((SkullMeta) item_meta).setPlayerProfile(profile);
         }
      }

      if (config.contains(ConfigUtils.getPath(path, enchantments.name()))) {
         for (String enchantment_name : config.getConfigurationSection(ConfigUtils.getPath(path, enchantments.name())).getKeys(false)) {
            Enchantment enchantment = Enchantment.getByName(enchantment_name);
            if (enchantment != null) {
               int level = Math.max(config.getInt(ConfigUtils.getPath(path, enchantments.name(), enchantment_name), 1), 1);
               if (!item_meta.addEnchant(enchantment, level, true)) {
                  new Message("§c[§4" + path + "§c]: уровень зачарования §4" + enchantment_name + "§c задан некорректно!")
                        .send(Bukkit.getConsoleSender());
                  new Message("§cЕсли проблема не разрешаема, обратитесь к разработчику -> §dcwcode.ru/vk")
                        .send(Bukkit.getConsoleSender());
               }
            } else {
               new Message("§c[§4" + path + "§c]: зачарование §4" + enchantment_name + "§c не существует!");
            }
         }
      }

      item.setItemMeta(item_meta);

      return item;
   }

   public static boolean saveConfig(File file, YamlConfiguration config) {
      try {
         config.save(file);
         return true;
      } catch (IOException e) {
         e.printStackTrace();
         return false;
      }
   }
}