package tkachgeek.tkachutils.colors;

import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class ColorUtils {
   @NotNull
   public static TextColor lerpManyColors(int i, float sectionLen, TextColor... colors) {
      return TextColor.lerp((i % sectionLen) / sectionLen, colors[(int) Math.floor(i / sectionLen)], colors[(int) Math.ceil(i / sectionLen)]);
   }

   private static final List<String> colors = Arrays.asList("0123456789abcdef".split(""));

   public static boolean isColor(String color) {
      return colors.contains(color);
   }

   public static List<String> getColors() {
      return colors;
   }
   
   public static TextColor lerpManyColors(float lerp, TextColor... colors) {
      double sectionLen = 255.0 * (colors.length - 1);
      int i = (int) ((sectionLen * (colors.length - 1)) * lerp);
      return TextColor.lerp((float) ((i % sectionLen) / sectionLen), colors[(int) Math.floor(i / sectionLen)], colors[(int) Math.ceil(i / sectionLen)]);
   }
}
