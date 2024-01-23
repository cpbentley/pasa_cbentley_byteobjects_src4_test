package pasa.cbentley.byteobjects.src4.stator.tests;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
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

}
