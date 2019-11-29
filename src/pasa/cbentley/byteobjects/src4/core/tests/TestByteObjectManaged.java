package pasa.cbentley.byteobjects.src4.core.tests;

import pasa.cbentley.byteobjects.src4.core.ByteController;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.core.ByteObjectManaged;
import pasa.cbentley.byteobjects.src4.interfaces.IJavaObjectFactory;
import pasa.cbentley.byteobjects.src4.tech.ITechByteObject;
import pasa.cbentley.byteobjects.src4.tech.ITechObjectManaged;

/**
 * 
 * @author Charles Bentley
 *
 */
public class TestByteObjectManaged extends ByteObjectTestCase implements ITechByteObject {

   public TestByteObjectManaged() {
      super(true);
   }

   /**
    * Default of 50 DataSize
    * @return
    */
   public ByteObjectManaged doConfig() {
      return doConfig(50, 0);
   }

   /**
    * header size is 0;
    * @param dataSize
    * @return
    */
   protected ByteObjectManaged doConfig(int dataSize) {
      return doConfig(dataSize, 0);
   }

   protected ByteObjectManaged doConfig(int dataSize, int headerSize) {
      ByteObjectManaged b = boc.getByteObjectManagedFactory().getDefault(boc, headerSize, dataSize);
      return b;
   }

   protected void doCopyTo(ByteObjectManaged b) {
      //should 
      assertEquals(40, b.get2(AGENT_OFFSET_13_LEN_HEADER2));
      assertEquals(50, b.get2(AGENT_OFFSET_14_LEN_DATA_4));

      assertEquals(90, b.getLength());

      b.set4(55, 55);

      byte[] data = new byte[100];

      b.copyByteObjectTo(data, 10);

      ByteObjectManaged b1 = new ByteObjectManaged(boc, data, 10);
      assertEquals(true, b1.equals(b));

      byte[] data2 = new byte[b.getLength() + 1];
      b.copyByteObjectTo(data2, 1);

      ByteObjectManaged b2 = new ByteObjectManaged(boc, data2, 1);
      assertEquals(true, b2.equals(b));
   }

   protected void doExpandResetArray0(ByteObjectManaged b) {
      assertEquals(90, b.get4(AGENT_OFFSET_16_LEN4));
      assertEquals(50, b.get4(AGENT_OFFSET_14_LEN_DATA_4));
      assertEquals(0, b.get3(AGENT_OFFSET_15_LEN_BUFFER_3));

      b.expandResetArrayData(0);
      assertEquals(40, b.get4(AGENT_OFFSET_16_LEN4));
      assertEquals(0, b.get4(AGENT_OFFSET_14_LEN_DATA_4));
      assertEquals(0, b.get3(AGENT_OFFSET_15_LEN_BUFFER_3));
   }

   protected void doTrim(ByteObjectManaged b) {
      assertEquals(40, b.getLength());
      b.expandBuffer(100);

      assertEquals(140, b.getLength());
      b.expandData(30);

      b.set4(61 - 4, Integer.MAX_VALUE);

      assertEquals(140, b.getLength());

      assertEquals(30, b.get4(AGENT_OFFSET_14_LEN_DATA_4));
      assertEquals(70, b.get3(AGENT_OFFSET_15_LEN_BUFFER_3));

      b.bufferTrim();

      assertEquals(30, b.get4(AGENT_OFFSET_14_LEN_DATA_4));
      assertEquals(0, b.get3(AGENT_OFFSET_15_LEN_BUFFER_3));
      assertEquals(70, b.get4(AGENT_OFFSET_16_LEN4));
      assertEquals(70, b.getLength());

      assertEquals(b.get4(61 - 4), Integer.MAX_VALUE);
   }

   public IJavaObjectFactory getFac() {
      return new HelperByteFactory(boc);
   }

   @Override
   public void setupAbstract() {
      super.setupAbstract();
   }

   public void testBasic() {
      int headerSize = AGENT_BASIC_SIZE + 2;
      ByteObjectManaged b = doConfig(0, headerSize);
      ByteObjectManaged bom = boc.getByteObjectManagedFactory().createByteObject(headerSize);
      assertEquals(0, bom.getByteObjectOffset());
   }

   public void testByteObjectToByteArray() {
      ByteObjectManaged bom = doConfig();

      byte[] data = bom.toByteArray();

      ByteObject bo = boc.getByteObjectFactory().createByteObjectFromWrap(data, 0);
      assertNotNull(bo);
      assertEquals(true, bo instanceof ByteObjectManaged);

   }

   public void testConstructorByteController() {
      doConfig();
      ByteController bc = new ByteController(boc, getFac());
      ByteObjectManaged bom = new ByteObjectManaged(boc, bc);

      assertEquals(bc, bom.getMemController());

   }

   public void testConstructorDef() {
      ByteObjectManaged bom = doConfig();

      assertEquals(null, bom.getMemController());

      assertEquals(0, bom.get2(AGENT_OFFSET_05_CLASS_ID2));
      assertEquals(90, bom.get4(AGENT_OFFSET_16_LEN4));
      assertEquals(50, bom.get4(AGENT_OFFSET_14_LEN_DATA_4));
      assertEquals(0, bom.get2(AGENT_OFFSET_04_INTERFACE_ID2));
      assertEquals(40, bom.get2(AGENT_OFFSET_13_LEN_HEADER2));

      assertEquals(40, bom.getDataOffsetStartLoaded());
      assertEquals(90, bom.getLength()); //header 40 + data 50

      byte[] array = new byte[bom.getLength() + 2];
      bom.copyByteObjectTo(array, 2);
   }

   public void testCopyArray() {
      ByteObjectManaged bom = doConfig();
      byte[] array = new byte[bom.getLength() + 2];
      bom.copyByteObjectTo(array, 2);
   }

   public void testCopyData() {
      ByteObjectManaged b = doConfig();

      assertEquals(50, b.get4(AGENT_OFFSET_14_LEN_DATA_4));
      assertEquals(0, b.get3(AGENT_OFFSET_15_LEN_BUFFER_3));

      byte[] data = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
      b.copyAppendData(data, 1, 5);

      assertEquals(95, b.get4(AGENT_OFFSET_16_LEN4));
      assertEquals(55, b.get4(AGENT_OFFSET_14_LEN_DATA_4));

      int indexOfData = 90;
      assertEquals(2, b.get1(indexOfData));
      assertEquals(3, b.get1(indexOfData + 1));
      assertEquals(4, b.get1(indexOfData + 2));
      assertEquals(5, b.get1(indexOfData + 3));
      assertEquals(6, b.get1(indexOfData + 4));

      byte[] dd = new byte[60];
      b.copyAppendData(dd, 0, dd.length);

      assertEquals(155, b.get4(AGENT_OFFSET_16_LEN4));
      assertEquals(115, b.get4(AGENT_OFFSET_14_LEN_DATA_4));
      assertEquals(0, b.get3(AGENT_OFFSET_15_LEN_BUFFER_3));

   }

   public void testCopyTo() {
      ByteObjectManaged b = doConfig();
      doCopyTo(b);

   }

   public void testExpandArray() {
      int HEADER = 14;
      int dataSize = 16;
      ByteObjectManaged bom = doConfig(dataSize, HEADER);

      assertNull(bom.getByteController());
      int offsetstart = AGENT_BASIC_SIZE + HEADER;
      assertEquals(54, offsetstart);

      assertEquals(70, bom.getByteObjectData().length);
      assertEquals(70, bom.getLength());

      assertEquals(54, bom.getDataOffsetStartLoaded());
      assertEquals(16, bom.get4(AGENT_OFFSET_14_LEN_DATA_4));
      assertEquals(70, bom.getDataOffsetEndLoaded());

      bom.expandResetArrayData(25);

      assertEquals(25, bom.get4(AGENT_OFFSET_14_LEN_DATA_4));

      assertEquals(25, bom.get4(AGENT_OFFSET_14_LEN_DATA_4));
      assertEquals(79, bom.getDataOffsetEndLoaded());
      assertEquals(79, bom.getByteObjectData().length);
      assertEquals(79, bom.getLength());

      bom.expandResetArrayData(10);

      assertEquals(54, bom.getLengthFullHeaders());

      assertEquals(54, bom.getDataOffsetStartLoaded());
      assertEquals(10, bom.get4(AGENT_OFFSET_14_LEN_DATA_4));
      assertEquals(64, bom.getDataOffsetEndLoaded());
      assertEquals(0, bom.getLengthDynHeader());

   }

   public void testExpandBuffer() {
      ByteObjectManaged b = doConfig(0);

   }

   public void testExpandBufferWithByteController(ByteObjectManaged b) {
      assertNotNull(b.getByteController());

      logPrint(b);

      assertEquals(true, b.hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_7_SAVED));

      assertEquals(true, b.isModified());

      b.expandBuffer(50);

      assertEquals(false, b.isModified()); //expanding buffers
      assertEquals(0, b.get4(AGENT_OFFSET_14_LEN_DATA_4));
      assertEquals(50, b.get3(AGENT_OFFSET_15_LEN_BUFFER_3));

      b.expandData(30);

      assertEquals(true, b.isModified());
      assertEquals(30, b.get4(AGENT_OFFSET_14_LEN_DATA_4));
      assertEquals(20, b.get3(AGENT_OFFSET_15_LEN_BUFFER_3));
   }

   public void testExpandResetArray0() {
      ByteObjectManaged b = doConfig();
      doExpandResetArray0(b);

   }

   public void testExpandResetArray25() {
      ByteObjectManaged b = doConfig();

      assertEquals(90, b.get4(AGENT_OFFSET_16_LEN4));
      assertEquals(50, b.get4(AGENT_OFFSET_14_LEN_DATA_4));
      assertEquals(0, b.get3(AGENT_OFFSET_15_LEN_BUFFER_3));

      b.expandResetArrayData(25);
      assertEquals(65, b.get4(AGENT_OFFSET_16_LEN4));
      assertEquals(25, b.get4(AGENT_OFFSET_14_LEN_DATA_4));
      assertEquals(0, b.get3(AGENT_OFFSET_15_LEN_BUFFER_3));

   }

   public void testIsModified() {
      ByteObjectManaged b = doConfig();
      //
      //      //TODO what?
      //      
      //      assertEquals(false, b.isModified());
      //      assertEquals(false, b.hasFlagObject(A_OBJECT_FLAG_3_VERSIONING));
   }

   public void testTrim() {
      ByteObjectManaged b = doConfig(0);
      doTrim(b);

   }
}
