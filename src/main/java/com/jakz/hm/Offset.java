package com.jakz.hm;

public class Offset
{
  long value;
  
  public Offset(long value)
  {
    this.value = value & 0x00FFFFFF;
  }
  
  public String toString() { return String.format("%06x", value); }
  
  public Offset shift(long amount) { return new Offset(value + amount); }
  
  public long value() { return value; }
  public long adjusted() { return value + 0x08000000; }
  
  public int hashCode() { return Long.hashCode(value); }
  public boolean equals(Object other) { return other instanceof Offset && ((Offset)other).value == value; }
}
