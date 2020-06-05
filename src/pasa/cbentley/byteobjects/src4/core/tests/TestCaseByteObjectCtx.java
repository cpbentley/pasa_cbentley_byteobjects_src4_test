/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.core.tests;

import pasa.cbentley.byteobjects.src4.core.ByteControllerFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObjectManaged;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.extra.PointerFactory;
import pasa.cbentley.byteobjects.src4.functions.AcceptorFactory;
import pasa.cbentley.byteobjects.src4.functions.ActionFactory;
import pasa.cbentley.byteobjects.src4.tech.ITechByteObject;
import pasa.cbentley.byteobjects.src4.tech.ITechObjectManaged;
import pasa.cbentley.testing.engine.TestCaseBentley;

public abstract class TestCaseByteObjectCtx extends TestCaseBentley implements ITechByteObject, ITechObjectManaged {

   protected BOCtx                 boc;

   protected PointerFactory        pointerFac;

   protected AcceptorFactory       acceptorFac;

   protected ByteControllerFactory byteControllerFactory;

   protected ActionFactory         actionFactory;

   public void setupAbstract() {
      boc = new BOCtx(uc);
      pointerFac = boc.getPointerFactory();
      acceptorFac = boc.getAcceptorFactory();
      byteControllerFactory = boc.getByteControllerFactory();
      actionFactory = boc.getActionFactory();
   }

   public ByteObjectManaged getTechDefault(BOCtx mod, int headerSize, int classid) {
      return getTechDefault(mod, headerSize, classid, 0);
   }

   public ByteObjectManaged getTechDefault(BOCtx mod, int headerSize, int classid, int intid) {
      return boc.getByteObjectManagedFactory().getTechDefault(headerSize, classid, intid);
   }

   public ByteObjectManaged getTechDefault(BOCtx mod, int headerSize, int classid, int[] intids) {
      return boc.getByteObjectManagedFactory().getTechDefault(headerSize, classid, intids);
   }
}
