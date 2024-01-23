package pasa.cbentley.byteobjects.src4.stator.tests;

import pasa.cbentley.byteobjects.src4.core.tests.TestCaseByteObjectCtx;
import pasa.cbentley.byteobjects.src4.stator.ITechStatorBO;
import pasa.cbentley.byteobjects.src4.stator.StatorBO;

public class TestCaseStatorBO extends TestCaseByteObjectCtx implements ITechStatorBO {

   protected StatorBO      stator;

   protected StatorBOTestCtx tsc;

   public void setupAbstract() {
      super.setupAbstract();
      //what about the logging ?
      tsc = new StatorBOTestCtx(tc);

      stator = new StatorBO(boc);
   }

   protected StatorBO createNewStator() {
      StatorBO stator = new StatorBO(boc);
      return stator;
   }
}
