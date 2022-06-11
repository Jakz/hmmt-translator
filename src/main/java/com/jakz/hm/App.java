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
  
  public static String escapeString(String text)
  {
    return text.replaceAll("\\r\\n", "<0x0d0a>");
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
