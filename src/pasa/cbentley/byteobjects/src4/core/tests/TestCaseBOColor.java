package pasa.cbentley.byteobjects.src4.core.tests;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.objects.color.ColorFunctionFactory;
import pasa.cbentley.byteobjects.src4.objects.color.ColorIterator;
import pasa.cbentley.byteobjects.src4.objects.color.GradientFactory;
import pasa.cbentley.byteobjects.src4.objects.color.GradientOperator;
import pasa.cbentley.byteobjects.src4.objects.color.IBOColorFunction;
import pasa.cbentley.byteobjects.src4.objects.color.ITechGradient;
import pasa.cbentley.byteobjects.src4.objects.function.FunctionFactory;
import pasa.cbentley.core.src4.utils.interfaces.IColors;

public class TestCaseBOColor extends TestCaseByteObjectCtx implements IColors, ITechGradient, IBOColorFunction {

   protected ColorFunctionFactory facColorFun;

   protected FunctionFactory      facFunction;

   protected GradientFactory      facGradient;

   protected GradientOperator     opGradient;
   protected BOCtx     drc;

   public void setupAbstract() {
      super.setupAbstract();
      facColorFun = boc.getColorFunctionFactory();
      facFunction = boc.getFunctionFactory();
      facGradient = boc.getGradientFactory();
      opGradient = boc.getGradientOperator();
      drc = boc;
   }
   
   public ColorIterator getColorIterator(int color, ByteObject grad, int gradSize) {
      return drc.getColorFunctionFactory().getColorIterator(color, grad, gradSize);
   }

}
