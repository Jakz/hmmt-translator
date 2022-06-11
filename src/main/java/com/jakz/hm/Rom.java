package com.jakz.hm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Rom
{
  byte[] data;
  
  public Rom(String filename) throws IOException
  {
    File file = new File("T:\\hmft\\hmft\\trm-hmmt.gba");
    FileInputStream fis = new FileInputStream(file);
    
    data = new byte[(int)file.length()];

    fis.read(data);
    fis.close();
    
    App.log("Loaded rom into %d buffer", data.length);
  }
  
  Offset readPointer(Offset offset)
  {
    int o = (int)offset.value();
    
    long value = (data[o] & 0xFF) | ((data[o + 1] & 0xFF) << 8) | ((data[o + 2] & 0xFF) << 16) | ((data[o + 3] & 0xFF) << 24);
    
    return new Offset(value);
  }
  
  Offset[] readPointers(Offset offset, int count, int stride)
  {
    Offset[] pointers = new Offset[count];
    
    for (int i = 0; i < count; ++i)
      pointers[i] = readPointer(offset.shift(i * stride));
    
    return pointers;
  }
  
  String readNullTerminatedString(Offset offset)
  {
    int o = (int)offset.value();
    
    StringBuilder string = new StringBuilder();
    
    while (data[o] != 0x00)
    {
      string.append((char)data[o]);
      ++o;
    }
    
    return string.toString();
  }
}
