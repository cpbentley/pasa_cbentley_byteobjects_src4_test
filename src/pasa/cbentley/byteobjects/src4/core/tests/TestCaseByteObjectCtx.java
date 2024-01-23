/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.core.tests;

import pasa.cbentley.byteobjects.src4.core.ByteControllerFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObjectFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObjectManaged;
import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.byteobjects.src4.core.interfaces.IBOAgentManaged;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.objects.function.AcceptorFactory;
import pasa.cbentley.byteobjects.src4.objects.function.ActionFactory;
import pasa.cbentley.byteobjects.src4.objects.pointer.PointerFactory;
import pasa.cbentley.testing.engine.TestCaseBentley;

public abstract class TestCaseByteObjectCtx extends TestCaseBentley implements IByteObject, IBOAgentManaged {

   protected BOCtx                 boc;

   protected PointerFactory        pointerFac;

   protected PointerFactory        facPointer;

   protected AcceptorFactory       acceptorFac;

   protected ByteControllerFactory byteControllerFactory;

   protected ActionFactory         actionFactory;

   protected ActionFactory         facAction;

   protected ByteObjectFactory     facBO;

   public void setupAbstract() {
      boc = new BOCtx(uc);
      pointerFac = boc.getPointerFactory();
      acceptorFac = boc.getAcceptorFactory();
      byteControllerFactory = boc.getByteControllerFactory();
      actionFactory = boc.getActionFactory();
      facBO = boc.getByteObjectFactory();
      facAction = actionFactory;
      facPointer = pointerFac;
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
