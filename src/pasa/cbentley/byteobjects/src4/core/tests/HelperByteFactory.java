package pasa.cbentley.byteobjects.src4.core.tests;

import pasa.cbentley.byteobjects.src4.core.ByteController;
import pasa.cbentley.byteobjects.src4.core.ByteObjectManaged;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.interfaces.IJavaObjectFactory;
import pasa.cbentley.byteobjects.src4.tech.ITechObjectManaged;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;

public class HelperByteFactory implements IJavaObjectFactory, ITechObjectManaged {

   private BOCtx boc;

   public HelperByteFactory(BOCtx mod) {
      this.boc = mod;
   }

   public ByteObjectManaged createRootTech(int intID) {
      return boc.getByteObjectManagedFactory().getTechDefault(AGENT_BASIC_SIZE, 0, intID);
   }

   public Object createObject(ByteController bc, ByteObjectManaged tech) {
      return new ByteObjectManaged(boc, tech);
   }

   
   public Object createObjectInt(int intid, ByteObjectManaged tech) {
      ByteObjectManaged bom = new ByteObjectManaged(boc);
      bom.set2(AGENT_OFFSET_04_INTERFACE_ID2, intid);
      return bom;
   }
   
   public Object createObject(ByteObjectManaged tech, int intID) {
      return new ByteObjectManaged(boc, tech);
   }

   public Object createObject(ByteObjectManaged tech, int intID, ByteController bc) {
      return new ByteObjectManaged(boc, tech);
   }

   
   public ByteObjectManaged createMorphObject(ByteObjectManaged tech, ByteController bc, Object param) {
      return tech.cloneBOMHeader();
   }


   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "HelperByteFactory");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "HelperByteFactory");
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return boc.getUCtx();
   }

   //#enddebug
   


}
