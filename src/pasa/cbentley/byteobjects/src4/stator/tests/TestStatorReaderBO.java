package pasa.cbentley.byteobjects.src4.stator.tests;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.objects.pointer.IBOPointer;
import pasa.cbentley.byteobjects.src4.stator.ITechStatorBO;
import pasa.cbentley.byteobjects.src4.stator.StatorBO;
import pasa.cbentley.byteobjects.src4.stator.StatorReaderBO;
import pasa.cbentley.byteobjects.src4.stator.StatorWriterBO;

public class TestStatorReaderBO extends TestCaseStatorBO implements ITechStatorBO {

   public void testBasic() {

      StatorBO sbo = new StatorBO(boc);

      StatorReaderBO w = (StatorReaderBO) sbo.getReader(TYPE_0_MASTER);

      assertNull(w);

      w = (StatorReaderBO) sbo.getReader(TYPE_1_VIEW);
      assertNull(w);

      w = (StatorReaderBO) sbo.getReader(TYPE_2_MODEL);
      assertNull(w);

      w = (StatorReaderBO) sbo.getReader(TYPE_2_MODEL);
      assertNull(w);
   }

   public void testStatorReader() {

      StatorBO sbo = new StatorBO(boc);

      ByteObject bo = facPointer.createPointer(45, 2, IBOTypesBOC.TYPE_002_LIT_INT, 1);

      StatorWriterBO writerPointer = sbo.getStatorWriterKeyedTo(bo, TYPE_0_MASTER);

      writerPointer.getWriter().writeInt(4564);

      byte[] data = sbo.serializeAll();
      StatorBO sboFuture = new StatorBO(boc);
      sboFuture.importFrom(data);

      //#debug
      toDLog().pTest("sboFuture", sboFuture, TestStatorReaderBO.class, "testStatorReader", LVL_05_FINE, false);

      ByteObject boFuture = facPointer.createPointer(45, 2, IBOTypesBOC.TYPE_002_LIT_INT, 1);

      StatorReaderBO readerPointerFuture = sboFuture.getStatorReaderKeyedToExisting(boFuture, TYPE_0_MASTER);

      assertNotNull(readerPointerFuture);
      assertNotNull(readerPointerFuture.getReader());

      int in = readerPointerFuture.getReader().readInt();

      assertEquals(in, 4564);
   }

   public void testOneSub() {

      ByteObject bo1 = facPointer.createPointer(11, 2, IBOTypesBOC.TYPE_002_LIT_INT, 1);
      ByteObject bo2 = facPointer.createPointer(12, 2, IBOTypesBOC.TYPE_002_LIT_INT, 1);

      bo1.addByteObject(bo2);

      StatorBO sbo = new StatorBO(boc);
      StatorWriterBO writer = sbo.getWriterBO(TYPE_0_MASTER);

      writer.dataWriteByteObject(bo1);

      byte[] data = sbo.serializeAll();
      StatorBO sboFuture = new StatorBO(boc);
      sboFuture.importFrom(data);
      
      StatorReaderBO readerPointerFuture = sboFuture.getReaderBO(TYPE_0_MASTER);
      
      ByteObject bo1Future = readerPointerFuture.readByteObject();
      
      assertEquals(11, bo1Future.get2(IBOPointer.POINTER_OFFSET_02_OFFSET2));
      
      ByteObject bo2Future = bo1Future.getSubFirst(IBOTypesBOC.TYPE_010_POINTER);
      
      assertEquals(12, bo2Future.get2(IBOPointer.POINTER_OFFSET_02_OFFSET2));


   }

}
