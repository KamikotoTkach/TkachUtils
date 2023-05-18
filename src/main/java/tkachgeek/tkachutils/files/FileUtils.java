package tkachgeek.tkachutils.files;

import tkachgeek.tkachutils.collections.CollectionUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {
  /**
   * @return пустую строку, если не удалось прочитать файл или его нет
   */
  public static String readString(Path path) {
    try {
      CollectionUtils.toString(com.google.common.io.Files.readLines(path.toFile(), StandardCharsets.UTF_8), "", "", true);
    } catch (IOException ignored) {
    }
    return "";
  }
  
  public static void writeString(Path path, String text) {
    try {
      File file = path.toFile();
      if (!Files.exists(path)) {
        com.google.common.io.Files.createParentDirs(file);
      } else {
      }
      com.google.common.io.Files.write(text, file, StandardCharsets.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public static void downloadFileTo(String source, String destination) throws IOException {
    URL url = new URL(source);
    
    InputStream is = url.openStream();
    Path destinationPath = new File(destination).toPath();
    if (!Files.exists(destinationPath)) {
      com.google.common.io.Files.createParentDirs(destinationPath.toFile());
    }
    OutputStream os = new FileOutputStream(destinationPath.toFile());
    
    byte[] b = new byte[2048];
    int length;
    
    while ((length = is.read(b)) != -1) {
      os.write(b, 0, length);
    }
    
    is.close();
    os.close();
  }
}
