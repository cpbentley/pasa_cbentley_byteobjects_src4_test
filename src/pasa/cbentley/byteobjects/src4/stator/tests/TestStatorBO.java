package pasa.cbentley.byteobjects.src4.stator.tests;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.stator.ITechStatorBO;
import pasa.cbentley.byteobjects.src4.stator.StatorBO;
import pasa.cbentley.byteobjects.src4.stator.StatorReaderBO;
import pasa.cbentley.byteobjects.src4.stator.StatorWriterBO;
import pasa.cbentley.core.src4.interfaces.IPrefsReader;
import pasa.cbentley.core.src4.interfaces.IPrefsWriter;
import pasa.cbentley.core.src4.stator.Stator;
import pasa.cbentley.core.src4.stator.StatorWriter;

public class TestStatorBO extends TestCaseStatorBO implements ITechStatorBO {

   public void testWriterOnlyKeys() {

      IPrefsWriter pw = stator.getPrefsWriter();

      pw.put("color", "black");
      pw.putInt("saturation", 100);

      byte[] data = stator.serializePrefs();

      Stator statorFuture = new Stator(uc);

      statorFuture.importPrefsFrom(data);

      IPrefsReader prefsFuture = statorFuture.getPrefsReader();

      assertEquals("black", prefsFuture.get("color", ""));
      assertEquals(100, prefsFuture.getInt("saturation", 0));
   }

   public void testComplex() {

      StatorBO sbo = new StatorBO(boc);

      ByteObject bo = facPointer.createPointer(45, 2, IBOTypesBOC.TYPE_002_LIT_INT, 1);

      ByteObject bo55 = facPointer.createPointer(55, 1, IBOTypesBOC.TYPE_008_LIT_ARRAY_STRING, 1);

      StatorWriterBO writerPointer = sbo.getStatorWriterKeyedTo(bo, TYPE_0_MASTER);
      writerPointer.getWriter().writeInt(4564);
      writerPointer.dataWriteByteObject(bo55);

      sbo.getPrefsWriter().put("hello", "today");

      StatorWriterBO writer = sbo.getWriterBO(TYPE_1_VIEW);
      ByteObject bo66 = facPointer.createPointer(66, 1, IBOTypesBOC.TYPE_008_LIT_ARRAY_STRING, 1);
      writer.dataWriteByteObject(bo66);

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

      StatorReaderBO readerView = sboFuture.getReaderBO(TYPE_1_VIEW);
      assertNotNull(readerView);
      assertNotNull(readerView.getReader());

   }

}
