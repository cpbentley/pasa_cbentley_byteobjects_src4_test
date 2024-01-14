package pasa.cbentley.byteobjects.src4.color;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.core.tests.TestCaseBOColor;
import pasa.cbentley.byteobjects.src4.objects.color.ColorIterator;
import pasa.cbentley.byteobjects.src4.objects.color.IBOGradient;
import pasa.cbentley.byteobjects.src4.objects.function.Function;
import pasa.cbentley.byteobjects.src4.objects.function.ITechFunction;
import pasa.cbentley.core.src4.utils.ColorUtils;

public class TestColorIterator extends TestCaseBOColor {

   /**
    * 
    */
   public TestColorIterator() {
   }

   public void setupAbstractDrawX() {

   }

   /**
    * Generic creation of a Gradient function to a green color.
    * <br>
    * <br>
    * A function of values is generated.
    * <br>
    * Gradient primary color and gradient size is contextual of function call.
    * <br>
    * <br>
    */
   public void testFunctionGradientValues() {

      //create gradient definition
      ByteObject grad = facGradient.getGradient(FULLY_OPAQUE_GREEN, 100);

      assertEquals(false, grad.hasFlag(IBOGradient.GRADIENT_OFFSET_02_FLAGK_EXCLUDE, IBOGradient.GRADIENT_FLAGK_3_PART1_EXCLUDE_LEFT));

      ColorIterator ci = getColorIterator(FULLY_OPAQUE_BLACK, grad, 10);
      int[] colors = ci.getColors();
      //function use of gradient colors.
      Function f = facFunction.createFunValues(colors, ITechFunction.FUN_COUNTER_OP_0_ASC);

      //users gets a color function and reads the values.
      int first = f.fx();
      assertEquals(toStringColor(FULLY_OPAQUE_BLACK), toStringColor(first));
      assertEquals(FULLY_OPAQUE_BLACK, first);
      assertEquals(1, f.getCounter());
      f.fx();
      assertEquals(2, f.getCounter());
      f.fx();
      assertEquals(3, f.getCounter());
      f.fx();
      assertEquals(4, f.getCounter());
      f.fx();
      assertEquals(5, f.getCounter());
      f.fx();
      assertEquals(6, f.getCounter());
      f.fx();
      assertEquals(7, f.getCounter());
      f.fx();
      assertEquals(8, f.getCounter());
      f.fx();
      assertEquals(9, f.getCounter());
      int last = f.fx();
      assertEquals(10, f.getCounter());
      assertEquals(toStringColor(FULLY_OPAQUE_GREEN), toStringColor(last));
      assertEquals(FULLY_OPAQUE_GREEN, last);

   }

   public void testGrad_IterateColor() {

      ByteObject gradBorder = facGradient.getGradient(FULLY_OPAQUE_WHITE, 100, GRADIENT_TYPE_RECT_02_VERT);

      ColorIterator ci = getColorIterator(FULLY_OPAQUE_GREEN, gradBorder, 4);

      assertEquals(toStringColor(ColorUtils.getRGBInt(255, 0, 255, 0)), toStringColor(ci.iterateColor()));
      assertEquals(toStringColor(ColorUtils.getRGBInt(255, 84, 255, 84)), toStringColor(ci.iterateColor()));
      assertEquals(toStringColor(ColorUtils.getRGBInt(255, 170, 255, 170)), toStringColor(ci.iterateColor()));
      assertEquals(toStringColor(ColorUtils.getRGBInt(255, 255, 255, 255)), toStringColor(ci.iterateColor()));
      //what should be the behavior here?
      assertEquals(toStringColor(ColorUtils.getRGBInt(255, 0, 255, 0)), toStringColor(ci.iterateColor()));
      assertEquals(toStringColor(ColorUtils.getRGBInt(255, 84, 255, 84)), toStringColor(ci.iterateColor()));

      int step = 2;
      // do it with step 2
      gradBorder = facGradient.getGradient(FULLY_OPAQUE_WHITE, 100, GRADIENT_TYPE_RECT_02_VERT, step, null);

      //test it with other kind of methods.
      ci = getColorIterator(FULLY_OPAQUE_GREEN, gradBorder, 4);

      assertEquals(toStringColor(ColorUtils.getRGBInt(255, 0, 255, 0)), toStringColor(ci.iterateColor()));
      assertEquals(toStringColor(ColorUtils.getRGBInt(255, 255, 255, 255)), toStringColor(ci.iterateColor()));

      //check offset
      gradBorder = facGradient.getGradient(FULLY_OPAQUE_WHITE, 100, GRADIENT_TYPE_RECT_02_VERT, 1, null);
      drc.getGradientOperator().setGradientOffset(gradBorder, 2);
      ci = getColorIterator(FULLY_OPAQUE_GREEN, gradBorder, 4);

      assertEquals(toStringColor(ColorUtils.getRGBInt(255, 170, 255, 170)), toStringColor(ci.iterateColor()));
      assertEquals(toStringColor(ColorUtils.getRGBInt(255, 255, 255, 255)), toStringColor(ci.iterateColor()));
      assertEquals(toStringColor(ColorUtils.getRGBInt(255, 0, 255, 0)), toStringColor(ci.iterateColor()));
      assertEquals(toStringColor(ColorUtils.getRGBInt(255, 84, 255, 84)), toStringColor(ci.iterateColor()));

   }

   public void testGrad_IteratePixelCount() {

      ColorSettableForTest g = new ColorSettableForTest();

      //gradient to white. from the given method
      ByteObject gradientWhite = facGradient.getGradient(FULLY_OPAQUE_WHITE, 100, GRADIENT_TYPE_RECT_02_VERT);
      int gradientSize = 4;
      ColorIterator ci = getColorIterator(FULLY_OPAQUE_GREEN, gradientWhite, gradientSize);

      int count = 0;
      count = ci.iteratePixelCount(g);
      assertEquals(count, 0);
      assertEquals(toStringColor(ColorUtils.getRGBInt(255, 0, 255, 0)), toStringColor(g.getColor()));
      count = ci.iteratePixelCount(g);
      assertEquals(count, 1);
      assertEquals(toStringColor(ColorUtils.getRGBInt(255, 84, 255, 84)), toStringColor(g.getColor()));
      count = ci.iteratePixelCount(g);
      assertEquals(count, 2);
      assertEquals(toStringColor(ColorUtils.getRGBInt(255, 170, 255, 170)), toStringColor(g.getColor()));
      count = ci.iteratePixelCount(g);
      assertEquals(count, 3);
      assertEquals(toStringColor(ColorUtils.getRGBInt(255, 255, 255, 255)), toStringColor(g.getColor()));
      count = ci.iteratePixelCount(g);
      assertEquals(count, -1);
      assertEquals(toStringColor(ColorUtils.getRGBInt(255, 255, 255, 255)), toStringColor(g.getColor()));

      int step = 2;
      // do it with step 2
      gradientWhite = facGradient.getGradient(FULLY_OPAQUE_WHITE, 100, GRADIENT_TYPE_RECT_02_VERT, step, null);

      //test it with other kind of methods.
      ci = getColorIterator(FULLY_OPAQUE_GREEN, gradientWhite, 4);
      count = 0;
      count = ci.iteratePixelCount(g);
      assertEquals(count, 0);
      assertEquals(toStringColor(ColorUtils.getRGBInt(255, 0, 255, 0)), toStringColor(g.getColor()));
      count = ci.iteratePixelCount(g);
      assertEquals(count, 2);
      assertEquals(toStringColor(ColorUtils.getRGBInt(255, 255, 255, 255)), toStringColor(g.getColor()));

      step = 1;
      //check the offset
      gradientWhite = facGradient.getGradient(FULLY_OPAQUE_WHITE, 100, GRADIENT_TYPE_RECT_02_VERT, step, null);
      drc.getGradientOperator().setGradientOffset(gradientWhite, 2);
      ci = getColorIterator(FULLY_OPAQUE_GREEN, gradientWhite, 4);

      count = 0;
      count = ci.iteratePixelCount(g);
      assertEquals(count, 0);
      assertEqualsToStringColor(ColorUtils.getRGBInt(255, 170, 255, 170), g.getColor());
      count = ci.iteratePixelCount(g);
      assertEquals(count, 1);
      assertEquals(ColorUtils.getRGBInt(255, 255, 255, 255), g.getColor());
      count = ci.iteratePixelCount(g);
      assertEquals(count, 2);
      assertEquals(ColorUtils.getRGBInt(255, 0, 255, 0), g.getColor());
      count = ci.iteratePixelCount(g);
      assertEquals(count, 3);
      assertEquals(ColorUtils.getRGBInt(255, 84, 255, 84), g.getColor());
      count = ci.iteratePixelCount(g);
      assertEquals(count, -1);
      assertEquals(ColorUtils.getRGBInt(255, 84, 255, 84), g.getColor());
   }

   public void testGrad_IterateTotalSize() {

      ByteObject gradBorder = facGradient.getGradient(FULLY_OPAQUE_WHITE, 100, GRADIENT_TYPE_RECT_02_VERT);

      //test it with other kind of methods.
      ColorSettableForTest g = new ColorSettableForTest();

      ColorIterator ci = getColorIterator(FULLY_OPAQUE_GREEN, gradBorder, 4);
      int count = 0;
      count = ci.iterateTotalSize(g);
      assertEquals(count, 0);
      assertEqualsToStringColor(ColorUtils.getRGBInt(255, 0, 255, 0), g.getColor());
      count = ci.iterateTotalSize(g);
      assertEquals(count, 1);
      assertEqualsToStringColor(ColorUtils.getRGBInt(255, 84, 255, 84), g.getColor());
      count = ci.iterateTotalSize(g);
      assertEquals(count, 2);
      assertEqualsToStringColor(ColorUtils.getRGBInt(255, 170, 255, 170), g.getColor());
      count = ci.iterateTotalSize(g);
      assertEquals(count, 3);
      assertEqualsToStringColor(ColorUtils.getRGBInt(255, 255, 255, 255), g.getColor());
      count = ci.iterateTotalSize(g);
      assertEquals(count, -1);
      assertEqualsToStringColor(ColorUtils.getRGBInt(255, 255, 255, 255), g.getColor());

   }

}
