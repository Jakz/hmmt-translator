package com.jakz.hm;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jakz.hm.ui.StringsTable;
import com.pixbits.lib.lang.Size;
import com.pixbits.lib.ui.UIUtils;
import com.pixbits.lib.ui.table.DataSource;

/*
 * 0x000eab0c - 81 x 12 bytes
 * 4 bytes - pointer to item name
 * 4 bytes - unknown
 * 4 bytes - pointer to item description
 * 
 * 0x000edcd8 - 171 x 16 bytes
 * 4 bytes - point to item name
 * 8 bytes - uknown
 * 4 bytes - point to item description
 * 
 * 0x000efed4 - 95 x 12 bytes 
 * 4 bytes - point to item name
 * 4 bytes - unknown
 * 4 bytes - point to item description
 */

public class App
{
  private static Rom rom;
  
  public static void log(String format, Object... args) { System.out.println(String.format(format, args) + "\n"); }
  
  public static List<Text> strings;
  
  
  public static String allowed = " .-,?'!():/\r\n";
  public static String escapeString(String text)
  {
    for (int i = 0; i < text.length(); ++i)
    {
      char c = text.charAt(i);
      
      if (c >= 'a' && c <= 'z')
        continue;
      else if (c >= 'A' && c <= 'Z')
        continue;
      else if (c >= '0' && c <= '9')
        continue;
      else if (allowed.indexOf(c) != -1)
        continue;
      else
        ;//System.out.println("Unknown char: "+(int)c+" "+c+"   "+text);
    }
    
    
    return text.replaceAll("\\r\\n", "<0x0d0a>");
  }
  
  public static void loadItems(Offset base, int count, int stride, int inBlockShift)
  {
    Offset[] itemNames = rom.readPointers(base, count, stride);
    
    for (Offset io : itemNames)
    {
      String text = rom.readNullTerminatedString(io);
      strings.add(new Text(null, escapeString(text)));
    }
    
    Offset[] itemDescs = rom.readPointers(base.shift(inBlockShift), count, stride);
    
    for (Offset io : itemDescs)
    {
      String text = rom.readNullTerminatedString(io);
      strings.add(new Text(null, escapeString(text)));
    }
  }
  
  public static void loadScatteredBlocks()
  {
    final byte[] MAGIC = new byte[] {'S', 'T', 'R', ' '};
    boolean first = true;
    
    for (int i = 0; i < rom.length(); ++i)
    {
      if (rom.matches(MAGIC, i))
      {
        if (first)
        {
          first = false;
          continue;
        }
        
        long count = rom.readU32(new Offset(i + 8));
        
        log("Found STR block at %08x (%d entries)", i, count);
        
        Offset tableBase = new Offset(i + 12);
        Offset textBase = tableBase.shift(4 * count);
        
        for (int j = 0; j < count; ++j)
        {
          long textOffset = rom.readU32(tableBase.shift(4 * j));
          log("  %s", textOffset);
          
          String text = rom.readNullTerminatedString(textBase.shift(textOffset));
          strings.add(new Text(tableBase.shift(4 * j), escapeString(text)));
        }
      }
    }
  }

  
  public static void main(String[] args)
  {
    try
    {
      rom = new Rom("T:\\hmft\\hmft\\trm-hmmt.gba");
      strings = new ArrayList<>();

      loadItems(new Offset(0x000eab0c), 81, 12, 8);
      loadItems(new Offset(0x000edcd8), 171, 16, 12);
      loadItems(new Offset(0x000efed4), 95, 12, 8);
      
      loadScatteredBlocks();

      StringsTable table = new StringsTable();
      var panel = UIUtils.buildFillPanel(table, new Size.Int(1024, 768));
      var frame = UIUtils.buildFrame(panel, "Strings");
      
      table.refresh(strings);
      
      frame.exitOnClose();
      frame.centerOnScreen();
      frame.setVisible(true);
      
    } 
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }  
  }
}
