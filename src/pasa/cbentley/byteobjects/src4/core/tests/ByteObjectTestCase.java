package pasa.cbentley.byteobjects.src4.core.tests;

import pasa.cbentley.byteobjects.src4.core.ByteObjectManaged;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.tech.ITechByteObject;
import pasa.cbentley.byteobjects.src4.tech.ITechObjectManaged;
import pasa.cbentley.testing.BentleyTestCase;

public abstract class ByteObjectTestCase extends BentleyTestCase implements ITechByteObject, ITechObjectManaged {

   protected BOCtx boc;

   public ByteObjectTestCase(boolean b) {
      super(b);
   }

   public void setupAbstract() {
      boc = new BOCtx(uc);
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
