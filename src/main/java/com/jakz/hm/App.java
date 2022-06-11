package com.jakz.hm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class App
{
  private static Rom rom;
  
  public static void log(String format, Object... args) { System.out.println(String.format(format, args) + "\n"); }
  
  final static long ITEMS_TABLE = 0x000eab0c;
  
  public static void main(String[] args)
  {
    try
    {
      Rom rom = new Rom("T:\\hmft\\hmft\\trm-hmmt.gba");
   
      Offset offset = new Offset(ITEMS_TABLE);
      
      Offset[] items = rom.readPointers(offset, 81, 12);
      
      for (Offset io : items)
      {
        System.out.println(rom.readNullTerminatedString(io));
      }
      
      Offset[] itemDescs = rom.readPointers(offset.shift(8), 81, 12);
      
      for (Offset io : itemDescs)
      {
        System.out.println(rom.readNullTerminatedString(io));
      }

    } 
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }  
  }
}
