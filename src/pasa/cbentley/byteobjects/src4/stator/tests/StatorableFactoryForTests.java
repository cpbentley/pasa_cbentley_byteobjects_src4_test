/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.stator.tests;

import pasa.cbentley.core.src4.ctx.ICtx;
import pasa.cbentley.core.src4.stator.IStatorFactory;
import pasa.cbentley.core.src4.stator.IStatorable;
import pasa.cbentley.core.src4.stator.StatorReader;
import pasa.cbentley.testing.ctx.TestCtx;

public class StatorableFactoryForTests implements IStatorFactory {

   protected final TestCtx tc;

   private StatorBOTestCtx   tsc;

   public StatorableFactoryForTests(StatorBOTestCtx tsc) {
      this.tsc = tsc;
      this.tc = tsc.getTC();

   }


   public ICtx getCtx() {
      return tsc;
   }
   
   public Object createObject(StatorReader reader, int classID) {
      switch (classID) {
         default:
            return null;
      }
   }

   public Object[] createArray(int classID, int size) {
      switch (classID) {

         default:
            return null;
      }
   }

   public boolean isSupported(IStatorable statorable) {
      int id = statorable.getStatorableClassID();
      switch (id) {
         default:
            return false;
      }
   }
}
