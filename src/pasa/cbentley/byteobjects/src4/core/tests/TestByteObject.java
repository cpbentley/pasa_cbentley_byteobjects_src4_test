/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.core.tests;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.core.LitteralManager;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.extra.PointerFactory;
import pasa.cbentley.byteobjects.src4.tech.ITechByteObject;
import pasa.cbentley.byteobjects.src4.tech.ITechPointer;
import pasa.cbentley.core.src4.ctx.UCtx;

import pasa.cbentley.core.src4.io.BADataIS;
import pasa.cbentley.core.src4.io.BADataOS;
import pasa.cbentley.core.src4.io.BAByteIS;
import pasa.cbentley.core.src4.io.BAByteOS;
import pasa.cbentley.core.src4.structs.IntToObjects;
import pasa.cbentley.testing.engine.TestCaseBentley;

public class TestByteObject extends TestCaseByteObjectCtx implements ITechByteObject {

   public TestByteObject() {
      setFlagHideSystemOutTrue();
   }

   public void testSetValueBits() {

      ByteObject te = new ByteObject(boc, 0, 10);

      int offset4 = 0;
      te.setValue2Bits1(offset4, 1);
      te.setValue2Bits2(offset4, 2);
      te.setValue2Bits3(offset4, 1);
      te.setValue2Bits4(offset4, 3);

      assertEquals(1, te.get2Bits1(offset4));
      assertEquals(2, te.get2Bits2(offset4));
      assertEquals(1, te.get2Bits3(offset4));
      assertEquals(3, te.get2Bits4(offset4));

      te.setValue4Bits1(offset4, 5);
      te.setValue4Bits2(offset4, 11);

      assertEquals(5, te.get4Bits1(offset4));
      assertEquals(11, te.get4Bits2(offset4));

   }

   public void testSetDynMaxFixedString() {
      int type = 10;
      int size = 30;
      ByteObject p = new ByteObject(boc, type, size);
      int INDEX_45 = 10;
      p.set1(INDEX_45, 45);

      int INDEX_STRING = 11;
      //shorter
      p.setDynMaxFixedString(INDEX_STRING, 5, "Bonjour");

      assertEquals("Bonjo", p.getVarCharString(INDEX_STRING, 5));
      
      //longer
      int INDEX_STRING_2 = 21;
      //shorter
      p.setDynMaxFixedString(INDEX_STRING_2, 4, "Hi");
      assertEquals("Hi", p.getVarCharString(INDEX_STRING_2, 5));

   }

   public void testString() {
      int type = 10;
      int size = 30;
      ByteObject p = new ByteObject(boc, type, size);
     
      int INDEX_STRING = 21;
      
      p.setString(INDEX_STRING, "Max");
      
      assertEquals("Max", p.getNumSizePrefixedString(INDEX_STRING));
 
   }
   public void testSerializationSub() {
      int mainSize = 50;
      ByteObject p = new ByteObject(boc, 61, mainSize);

      assertEquals(50, p.getByteObjectData().length);

      ByteObject sub = new ByteObject(boc, 21, 10);
      p.addByteObject(sub);

      BAByteOS baos = new BAByteOS(uc);
      BADataOS dos = new BADataOS(uc, baos);
      p.serialize(dos);

      byte[] da = baos.toByteArray();

      //67 = 4 (byte sizes) + 50 (1) + 3 (sub sizes) + 10 (2) 
      assertEquals(67, da.length);

      BAByteIS bais = new BAByteIS(uc, da);
      BADataIS db = new BADataIS(uc, bais);
      ByteObject bo = boc.getByteObjectFactory().serializeReverse(db);

      //serialization writes 10 bytes and 3 bytes for number 
      assertEquals(63, bo.getByteObjectData().length);

   }

   public void testSerialization() {

      ByteObject p = new ByteObject(boc, 61, 50);

      ByteObject sub = new ByteObject(boc, 21, 10);
      p.addByteObject(sub);

      BAByteOS bos = new BAByteOS(uc);
      BADataOS dos = new BADataOS(uc, bos);
      p.serialize(dos);

      byte[] da = bos.toByteArray();

      BAByteIS bais = new BAByteIS(uc, da);
      BADataIS dis = new BADataIS(uc, bais);

      ByteObject bo = boc.getByteObjectFactory().serializeReverse(dis);

      assertEquals(true, bo.equalsToByteArray(p));
   }

   public void testConstructor1() {

      byte[] data = new byte[A_OBJECT_BASIC_SIZE];
      ByteObject bo = new ByteObject(boc, data, 0, A_OBJECT_BASIC_SIZE);

      int len = bo.getLength();
      assertEquals(len, A_OBJECT_BASIC_SIZE);

      int type = 1;
      bo.setValue(A_OBJECT_OFFSET_1_TYPE1, 1, type);
      assertEquals(type, bo.get1(A_OBJECT_OFFSET_1_TYPE1));

      bo.setFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_3_VERSIONING, true);

      assertEquals(true, bo.hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_3_VERSIONING));

   }

   public void testConstructorType() {
      int type = 3;
      int size = ITechByteObject.A_OBJECT_BASIC_SIZE + 5;
      int START = ITechByteObject.A_OBJECT_BASIC_SIZE;
      int INDEX_VALUE_2_BYTES = START + 1;
      ByteObject bo = new ByteObject(boc, type, size);

      bo.setFlag(START, 4, true);

      //store it as signed
      bo.setValue(INDEX_VALUE_2_BYTES, 60000, 2);

      assertEquals(true, bo.hasFlag(START, 4));

      assertEquals(60000, bo.getShortIntUnSigned(INDEX_VALUE_2_BYTES));
      assertEquals(-27232, bo.get2(INDEX_VALUE_2_BYTES));

   }

   public void testToByteArrayWithParamNulls() {
      ByteObject bo = boc.getPointerFactory().createPointer(10, 4, 128, 5);

      ByteObject boLit = boc.getLitteralIntFactory().getIntBO(15);

      bo.setByteObjects(new ByteObject[] { null, null, boLit });
      assertNotNull(bo.getSubs()[2]);

      byte[] data = bo.toByteArray();

      ByteObject boN = boc.getByteObjectFactory().createByteObjectFromWrap(data, 0);

      assertNotNull(boN);
      assertNotNull(boN.getSubs());
      assertEquals(3, boN.getSubs().length);
      assertEquals(null, boN.getSubs()[0]);
      assertEquals(null, boN.getSubs()[1]);

      ByteObject boLitN = boN.getSubs()[2];

      assertNotNull(boLitN);

      assertEquals(IBOTypesBOC.TYPE_002_LIT_INT, boLitN.getType());

      assertEquals(true, boLit.equalsContent(boLitN));

   }

   public void testLitteralIntArray() {

      int[] ar = new int[] { 3, 5, 7 };
      ByteObject bo = boc.getLitteralIntFactory().getIntArrayBO(ar);

      int[] arU = boc.getLitteralStringOperator().getLitteralArray(bo);

      assertEquals(true, boc.getUCtx().getIU().equals(ar, arU));

      int[] ar2 = new int[] { 3, 5, 7 };
      ByteObject bo2 = boc.getLitteralIntFactory().getIntArrayBO(ar2);

      assertEquals(true, bo2.equalsContent(bo));

      int[] bigAr = new int[] { 1, 2, 3, 4, 5, 6 };

      ByteObject array23 = boc.getLitteralIntFactory().getLitteralArray(bigAr, 2, 3);

      int[] data = boc.getLitteralStringOperator().getLitteralArray(array23);

      assertEquals(data.length, 3);
      assertEquals(data[0], 3);
      assertEquals(data[1], 4);
      assertEquals(data[2], 5);

      //now access a value
      int[] ar5 = new int[] { 3, 5, 7, 0, 45687 };
      ByteObject array5BO = boc.getLitteralIntFactory().getIntArrayBO(ar5);

      assertEquals(5, boc.getLitteralIntOperator().getLitteralArrayLength(array5BO));
      assertEquals(7, boc.getLitteralIntOperator().getLitteralArrayValueAt(array5BO, 2));

   }

   public void testToByteArrayBasic() {
      ByteObject bo = boc.getPointerFactory().createPointer(10, 4, 128, 5);

      byte[] data = bo.toByteArray();

      ByteObject nbo = boc.getByteObjectFactory().createByteObjectFromWrap(data, 0);

      assertNotNull(nbo);
      assertEquals(true, bo.equalsContent(nbo));

      data = nbo.toByteArray();
      ByteObject nnbo = boc.getByteObjectFactory().createByteObjectFromWrap(data, 0);

      assertNotNull(nnbo);
      assertEquals(true, nbo.equalsContent(nnbo));
      assertEquals(true, bo.equalsContent(nnbo));

   }

   public void testToSigned1() {
      ByteObject bo = new ByteObject(boc, 10, 10);

      int index = 5;

      bo.set1(index, 150);
      assertEquals(-106, bo.get1Signed(index));
      assertEquals(150, bo.get1(index));

      bo.set1Signed(index, -5);
      assertEquals(-5, bo.get1Signed(index));
      assertEquals(251, bo.get1(index));

      bo.set1Signed(index, -1);
      assertEquals(-1, bo.get1Signed(index));
      assertEquals(255, bo.get1(index));

      bo.set1(index, 127);
      assertEquals(127, bo.get1Signed(index));
      assertEquals(127, bo.get1(index));

      bo.set1(index, 128);
      assertEquals(-128, bo.get1Signed(index));
      assertEquals(128, bo.get1(index));

      bo.set1(index, -128);
      assertEquals(-128, bo.get1Signed(index));
      assertEquals(128, bo.get1(index));

      bo.set1Signed(index, -128);
      assertEquals(-128, bo.get1Signed(index));
      assertEquals(128, bo.get1(index));

   }

   public void testToSigned2() {
      ByteObject bo = new ByteObject(boc, 10, 10);

      int index = 5;

      bo.set2(index, -1);
      assertEquals(-1, bo.get2(index));
      assertEquals(32769, bo.get2Unsigned(index));

      bo.set2(index, 1);
      assertEquals(1, bo.get2(index));
      assertEquals(1, bo.get2Unsigned(index));
   }

   public void testToSigned3() {
      ByteObject bo = new ByteObject(boc, 10, 10);

      int index = 5;

      bo.set3Signed(index, -1);
      assertEquals(-1, bo.get3Signed(index));

      assertEquals(8388609, bo.get3Unsigned(index));

      bo.set3Signed(index, 1);
      assertEquals(1, bo.get3Signed(index));
      assertEquals(1, bo.get3Unsigned(index));

   }

   public void testToByteArray1Sub() {

      ByteObject bo = boc.getPointerFactory().createPointer(10, 4, 128, 5);

      ByteObject boSub = boc.getPointerFactory().getPointerFlag(-135, 4, 128, 5);

      bo.addByteObject(boSub);

      assertEquals(boSub, bo.getSubFirst(IBOTypesBOC.TYPE_010_POINTER));

      ByteObject[] bos = bo.getSubs(IBOTypesBOC.TYPE_010_POINTER);
      assertEquals(1, bos.length);
      assertEquals(boSub, bos[0]);

      //merge tehn

      byte[] data = bo.toByteArray();

      //create ByteObject instances for subs keeping the array reference
      ByteObject nbo = boc.getByteObjectFactory().createByteObjectFromWrap(data, 0);

      assertNotNull(nbo);

      //#debug
      toDLog().pTest("", nbo, TestByteObject.class, "testToByteArray1Sub", LVL_05_FINE, false);

      assertEquals(true, bo.equalsContent(nbo));

   }

   public void testToByteArraySeveralLevels() {

      ByteObject bo = boc.getPointerFactory().createPointer(10, 4, 128, 5);

      ByteObject litInt10 = boc.getLitteralIntFactory().getIntBO(10);
      ByteObject litInt6 = boc.getLitteralIntFactory().getIntBO(6);
      ByteObject litHello = boc.getLitteralStringFactory().getLitteralString("Hello");

      ByteObject boSub = boc.getPointerFactory().getPointerFlag(-135, 4, 128, 5);

      bo.addSub(litInt6);
      bo.addSub(litHello);
      bo.addSub(boSub);

      boSub.addSub(litInt10);
      boSub.addSub(bo);

      byte[] d = bo.toByteArray();

      ByteObject boBack = boc.getByteObjectFactory().createByteObjectFromWrap(d, 0);

      assertEquals(true, bo.equalsContent(boBack));

      assertEquals(true, litHello.equalsContent(boBack.getSubFirst(IBOTypesBOC.TYPE_003_LIT_STRING)));

      assertEquals(true, litInt6.equalsContent(boBack.getSubFirst(IBOTypesBOC.TYPE_002_LIT_INT)));

      ByteObject boBackSubPointer = boBack.getSubFirst(IBOTypesBOC.TYPE_010_POINTER);

      assertEquals(true, boSub.equalsContent(boBackSubPointer));
      assertEquals(true, litInt10.equalsContent(boBackSubPointer.getSubFirst(IBOTypesBOC.TYPE_002_LIT_INT)));

   }

   public void testToByteArrayLoop() {
      ByteObject bo = boc.getPointerFactory().createPointer(10, 4, 128, 5);

      ByteObject boSub = boc.getPointerFactory().getPointerFlag(-135, 4, 128, 5);

      //create a loop
      bo.addByteObject(boSub);
      boSub.addByteObject(bo);

      assertEquals(false, bo.equals(boSub));

      byte[] data = bo.toByteArray();

      //create ByteObject instances for subs keeping the array reference
      ByteObject nbo = boc.getByteObjectFactory().createByteObjectFromWrap(data, 0);

      assertNotNull(nbo);

      //redo a serialization which will not change a single byte
      byte[] ndata = nbo.toByteArray();
      ByteObject nnbo = boc.getByteObjectFactory().createByteObjectFromWrap(ndata, 0);

      assertEquals(true, nbo.equals(nnbo));

      //byte object keeps the reference
      assertEquals(true, data == nbo.getByteObjectData());

      assertEquals(false, bo.equals(nbo));
      assertEquals(true, bo.equalsContent(nbo));
      assertEquals(true, bo.equalsToByteArray(nbo));

      assertEquals(true, boSub.equalsContent(nbo.getSubFirst(IBOTypesBOC.TYPE_010_POINTER)));

   }

   public void testEqualsToByteArray() {

      ByteObject bo1 = boc.getPointerFactory().createPointer(10, 4, 128, 5);

      ByteObject bo2 = boc.getPointerFactory().createPointer(10, 4, 128, 5);

      assertEquals(true, bo1.equalsToByteArray(bo2));
      assertEquals(true, bo2.equalsToByteArray(bo1));

      ByteObject bo3 = boc.getPointerFactory().createPointer(10, 4, 127, 5);

      assertEquals(false, bo1.equalsToByteArray(bo3));

   }

   public void testImmutability() {

   }

   public void testSerializeToLoop() {
      ByteObject bo = boc.getPointerFactory().createPointer(10, 4, 128, 5);

      ByteObject boSub = boc.getPointerFactory().getPointerFlag(-135, 4, 128, 5);

      bo.addByteObject(boSub);
      boSub.addByteObject(bo);

      assertEquals(false, bo.equals(boSub));

      assertEquals(10, bo.getLength());

      BAByteOS bos = new BAByteOS(uc);
      BADataOS dos = new BADataOS(uc, bos);
      IntToObjects ito = new IntToObjects(uc, 20);

      bo.serializeTo(ito, dos);
      assertEquals(10, bo.getLength());
      assertEquals(0, bo.getTrailerSize());

      byte[] data = bos.toByteArray();

      //create ByteObject instances for subs keeping the array reference
      ByteObject nbo = boc.getByteObjectFactory().createByteObjectFromWrap(data, 0);

      toDLog().pTest("", nbo, TestByteObject.class, "testSerializeToLoop");

      assertEquals(13, nbo.getLength()); //why is it 3 bytes more?
      assertEquals(0, bo.getTrailerSize());
      assertEquals(true, nbo.hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_4_SERIALIZED));
      assertEquals(125, nbo.getSerializedMagicByte());

      assertEquals(true, bo.equalsContent(nbo));

      assertEquals(true, boSub.equalsContent(nbo.getSubFirst(IBOTypesBOC.TYPE_010_POINTER)));

   }

   public void testIntraReference() {
      ByteObject pointer = pointerFac.getPointerFlag(-135, 4, 128, 5);

      int ref = 45;
      pointer.setIntraReference(ref);

      assertEquals(ref, pointer.getIntraReference());

      ref = 3;
      pointer.setIntraReference(ref);

      assertEquals(ref, pointer.getIntraReference());

      ByteObject pointer2 = pointerFac.getPointerFlag(-135, 4, 128, 5);
      //flag check?
      assertEquals(0, pointer2.getIntraReference());

   }

   public void testBasicVersioning() {

      ByteObject bo = boc.getPointerFactory().getPointerFlag(-135, 4, 128, 5);

      //#debug
      toDLog().pTest("", bo, TestByteObject.class, "testBasicVersioning", LVL_05_FINE, false);
      
      bo.setVersioning(true);

      //#debug
      toDLog().pTest("", bo, TestByteObject.class, "testBasicVersioning", LVL_05_FINE, false);

      checkPointer(bo);

      assertEquals(0, bo.getVersion());
      assertEquals(0, bo.getVersion());

      bo.setValue(ITechPointer.POINTER_OFFSET_05_TYPE_NUM1, 4, 1);

      assertEquals(1, bo.getVersion());

      bo.setValue(ITechPointer.POINTER_OFFSET_05_TYPE_NUM1, 5, 1);

      assertEquals(2, bo.getVersion());

      checkPointer(bo);

      assertEquals(2, bo.getVersion());

      bo.setFlag(ITechPointer.POINTER_OFFSET_01_FLAG, 8, true);

      checkPointer(bo);

      assertEquals(3, bo.getVersion());

      assertEquals(true, bo.hasFlag(ITechPointer.POINTER_OFFSET_01_FLAG, 8));

      //assertEquals(12, bo.getSuffixVersioningOffset());
      assertEquals(ITechPointer.POINTER_BASIC_SIZE + ITechByteObject.VERSION_BYTE_SIZE, bo.getLength());

      assertEquals(3, bo.getVersion());

      bo.setIntraReference(6);

      assertEquals(ITechPointer.POINTER_BASIC_SIZE, bo.getSuffixVersioningOffset());
      assertEquals(ITechPointer.POINTER_BASIC_SIZE + ITechByteObject.VERSION_BYTE_SIZE, bo.getSuffixIntraOffset());
      assertEquals(ITechPointer.POINTER_BASIC_SIZE + ITechByteObject.VERSION_BYTE_SIZE + ITechByteObject.INTRA_REF_BYTE_SIZE, bo.getLength());

      assertEquals(3, bo.getVersion());

      assertEquals(6, bo.getIntraReference());

      checkPointer(bo);

      bo.setFlag(ITechPointer.POINTER_OFFSET_01_FLAG, 4, true);

      assertEquals(4, bo.getVersion());

      assertEquals(6, bo.getIntraReference());

      assertEquals(4, bo.getVersion());

   }

   private void checkPointer(ByteObject bo) {
      assertEquals(-135, bo.get2(ITechPointer.POINTER_OFFSET_02_OFFSET2));
      assertEquals(4, bo.get1(ITechPointer.POINTER_OFFSET_03_SIZE_OR_FLAG1));
      assertEquals(128, bo.get1(ITechPointer.POINTER_OFFSET_04_TYPE1));
      assertEquals(5, bo.get1(ITechPointer.POINTER_OFFSET_05_TYPE_NUM1));
   }

   public void testSetByteObjects() throws Exception {
      int type = 10;
      int size = 30;
      ByteObject p = new ByteObject(boc, type, size);
      ByteObject[] ar = new ByteObject[4];
      p.setByteObjects(ar);

      assertEquals(ar, p.getSubs());

      p.setImmutable();
      
      ByteObject[] ar2 = new ByteObject[2];
      try {
         p.setByteObjects(ar2);
         assertNotReachable("");
      } catch (Exception e) {
         assertReachable();
      }
   }

}
