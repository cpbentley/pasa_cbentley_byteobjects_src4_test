package pasa.cbentley.byteobjects.src4.color;

import pasa.cbentley.byteobjects.src4.objects.color.IColorSettable;

public class ColorSettableForTest implements IColorSettable {

   private int color;

   public void setColor(int color) {
      this.color = color;
   }

   public int getColor() {
      return color;
   }
}
