package com.jakz.hm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.BiConsumer;

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
  
  int length()
  {
    return data.length;
  }
  
  boolean matches(byte[] values, int offset)
  {
    if (offset + values.length >= data.length)
      return false;
    
    return Arrays.compare(values, 0, values.length, data, offset, offset + values.length) == 0;
  }
  
  long readU32(Offset offset)
  {
    int o = (int)offset.value();
    long value = (data[o] & 0xFF) | ((data[o + 1] & 0xFF) << 8) | ((data[o + 2] & 0xFF) << 16) | ((data[o + 3] & 0xFF) << 24);

    return value;
  }
  
  Offset readPointer(Offset offset)
  {
    return new Offset(readU32(offset));
  }
  
  Offset[] readPointers(Offset offset, int count, int stride)
  {
    Offset[] pointers = new Offset[count];
    
    for (int i = 0; i < count; ++i)
      pointers[i] = readPointer(offset.shift(i * stride));
    
    return pointers;
  }
  
  void readPointers(Offset offset, int count, int stride, BiConsumer<Offset, Offset> consumer)
  {
    for (int i = 0; i < count; ++i)
    {
      Offset address = offset.shift(i * stride);
      Offset pointer = readPointer(address);
      
      consumer.accept(address, pointer);
    }
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
