package com.jakz.hm;

public class TextReference
{
  static enum Mode { Absolute, Pointed }
  
  Offset address;
  Offset pointer;
  Mode mode;
  int maxLength;
  
  public TextReference(Offset address, Offset pointer)
  {
    this.mode = Mode.Pointed;

    this.address = address;
    this.pointer = pointer;
  }
  
  public TextReference(Offset address, int maxLength)
  {
    this.mode = Mode.Absolute;
    
    this.address = address;
    this.maxLength = maxLength;
  }
  
  @Override
  public String toString()
  {
    if (mode == Mode.Pointed)
    {
      return address + " -> " + pointer;
    }
    else
      return super.toString();
  }
}
