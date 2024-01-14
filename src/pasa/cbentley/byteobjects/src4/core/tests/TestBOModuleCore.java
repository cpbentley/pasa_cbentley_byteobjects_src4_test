package pasa.cbentley.byteobjects.src4.core.tests;

import pasa.cbentley.byteobjects.src4.core.BOModuleCore;
import pasa.cbentley.byteobjects.src4.ctx.IToStringsDIDsBocFun;
import pasa.cbentley.byteobjects.src4.objects.color.ITechFilter;
import pasa.cbentley.byteobjects.src4.objects.color.ITechGradient;

public class TestBOModuleCore extends TestCaseByteObjectCtx implements IToStringsDIDsBocFun {

   BOModuleCore mod;

   public void setupAbstract() {
      super.setupAbstract();
      mod = (BOModuleCore) boc.getModule();
   }

   public void testDIDs() {

      assertEquals("Grayscale", mod.toStringGetDIDString(DID_15_FILTER_TYPE, ITechFilter.FILTER_TYPE_01_GRAYSCALE));
      
      //test the linkage from root
      assertEquals("Grayscale", boc.toStringGetDIDString(DID_15_FILTER_TYPE, ITechFilter.FILTER_TYPE_01_GRAYSCALE));
    
      
      
      assertEquals("TopLeft", boc.toStringGetDIDString(DID_01_GRAD_RECT, ITechGradient.GRADIENT_TYPE_RECT_03_TOPLEFT));
      
      
   }
}
