package tkachgeek.tkachutils.server;

import org.bukkit.Bukkit;
import tkachgeek.tkachutils.numbers.NumbersUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerUtils {
   private static final Pattern pattern = Pattern.compile("([\\d.]+)");
   
   public static int getVersionWeight(String version) {
      Matcher matcher = pattern.matcher(version);
      if (!matcher.find()) return 0;

      String[] data = matcher.group(1).split("\\.");

      int multiplier = 10000;
      int intVersion = 0;
      for (String num : data) {
         if (!NumbersUtils.isInteger(num)) return 0;
         intVersion += Integer.parseInt(num) * Math.max(multiplier, 1);
         multiplier /= 100;
      }

      return intVersion;
   }

   public static boolean isVersionBefore1_16_5() {
      int version = getVersionWeight(Bukkit.getBukkitVersion());

      return version < getVersionWeight("1.16.5");
   }

   public static boolean isVersionBeforeOrEqual1_12_2() {
      int version = getVersionWeight(Bukkit.getBukkitVersion());

      return version <= getVersionWeight("1.12.2");
   }
}
