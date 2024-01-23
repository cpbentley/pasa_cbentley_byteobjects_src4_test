package pasa.cbentley.byteobjects.src4.stator.tests;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.stator.ITechStatorBO;
import pasa.cbentley.byteobjects.src4.stator.StatorBO;
import pasa.cbentley.byteobjects.src4.stator.StatorReaderBO;
import pasa.cbentley.byteobjects.src4.stator.StatorWriterBO;

public class TestStatorWriterBO extends TestCaseStatorBO implements ITechStatorBO {
   
   public void testBasic() {
      
      StatorBO bo = new StatorBO(boc);
      
      StatorWriterBO w = (StatorWriterBO) bo.getWriter(TYPE_0_MASTER);
      
      assertNotNull(w);
      
      assertEquals(0, w.getNumWrittenObject()); 
      
   }
   
   public void testGetterKey() {
      StatorBO sbo = new StatorBO(boc);
      
      ByteObject bo = boc.getPointerFactory().createPointer(45, 2, IBOTypesBOC.TYPE_002_LIT_INT, 1);
      
      StatorWriterBO writerPointer = sbo.getStatorWriterKeyedTo(bo, TYPE_0_MASTER);
    
      assertNotNull(writerPointer);
      
      StatorWriterBO writerPointer2 = sbo.getStatorWriterKeyedTo(bo, TYPE_0_MASTER);
      
      assertEquals(true, writerPointer == writerPointer2);
      
      StatorWriterBO writerPointerModel = sbo.getStatorWriterKeyedTo(bo, TYPE_2_MODEL);
      
      assertNotNull(writerPointerModel);
      
      assertEquals(true, writerPointer != writerPointerModel);
      
   }
   
   public void testWriteBO() {
      
      StatorBO sbo = new StatorBO(boc);
      
      StatorWriterBO w = (StatorWriterBO) sbo.getWriter(TYPE_0_MASTER);
      
      assertNotNull(w);
      
      assertEquals(0, w.getNumWrittenObject()); 
      
      ByteObject bo = facPointer.getPointer(45, 2);
      
      w.writeByteObject(bo);
      
      //byteobject is not considered an object but a litteral
      assertEquals(0, w.getNumWrittenObject()); 
      
      
      byte[] data = sbo.serializeAll();
      StatorBO sboFuture = new StatorBO(boc);
      sboFuture.importFrom(data);
   
      StatorReaderBO r = (StatorReaderBO) sboFuture.getReader(TYPE_0_MASTER);
      
      assertNotNull(r);
      
      ByteObject boFut = r.readByteObject();
      
      assertEquals(true, bo.equalsToByteArray(boFut));
      
   }

}
