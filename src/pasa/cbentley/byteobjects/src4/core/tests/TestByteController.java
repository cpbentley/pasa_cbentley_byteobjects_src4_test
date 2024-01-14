/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.core.tests;

import pasa.cbentley.byteobjects.src4.core.ByteController;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.core.ByteObjectManaged;
import pasa.cbentley.byteobjects.src4.core.interfaces.IJavaObjectFactory;
import pasa.cbentley.byteobjects.src4.core.interfaces.IBOByteControler;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.ctx.IToStringFlagsBO;
import pasa.cbentley.byteobjects.src4.sources.ByteArraySource;
import pasa.cbentley.byteobjects.src4.sources.MemorySource;
import pasa.cbentley.byteobjects.src4.sources.RootSource;
import pasa.cbentley.core.src4.ctx.IToStringFlags;

public class TestByteController extends TestCaseByteObjectCtx implements IBOByteControler {

   private IJavaObjectFactory factory;


   public void addError() {
      ByteController bc = new ByteController(boc);
      ByteObjectManaged nbom = new ByteObjectManaged(boc, bc);

      try {
         //try adding an agent identical in values but different reference
         bc.addAgent(nbom);
         assertFalse(true);
      } catch (IllegalArgumentException e) {
         assertTrue(true);
      }
   }

   public MemorySource createSourceForName(String name) {
      return new ByteArraySource(boc, name);
   }

   /**
    * A controller without MemorySources
    * @return
    */
   public ByteController getBC() {
      ByteController bc = new ByteController(boc, factory);

      //two agents with the same bom
      ByteObjectManaged root = getBOM(401);
      root.setFlag(AGENT_OFFSET_01_FLAG_1, AGENT_FLAG_CTRL_5_ROOT, true);
      bc.addAgent(root);
      bc.addAgent(getBOM(402));
      bc.addAgent(getBOM(403));
      bc.addAgent(getBOM(404));

      return bc;
   }

   public ByteObjectManaged getBOM(int classid) {
      ByteObjectManaged bom = new ByteObjectManaged(boc);
      bom.set2(AGENT_OFFSET_05_CLASS_ID2, classid);

      return bom;
   }

   public IJavaObjectFactory getFactoryNew() {
      return new HelperByteFactory(boc);
   }

   public void helperAdd3Agents(ByteController bc) {
      int classid = 4;
      ByteObjectManaged ag1 = new ByteObjectManaged(boc);
      ag1.set2(AGENT_OFFSET_06_GSOURCE_ID2, 2);
      ag1.set2(AGENT_OFFSET_05_CLASS_ID2, classid);
      bc.addAgent(ag1);

      ByteObjectManaged ag2 = new ByteObjectManaged(boc);
      ag2.set2(AGENT_OFFSET_06_GSOURCE_ID2, 2);
      ag2.set2(AGENT_OFFSET_05_CLASS_ID2, classid);
      ag2.set2(AGENT_OFFSET_04_INTERFACE_ID2, 2); //this agent also implement this interface
      bc.addAgent(ag2);

      ByteObjectManaged ag3 = new ByteObjectManaged(boc);
      ag3.set2(AGENT_OFFSET_06_GSOURCE_ID2, 2);
      ag3.set2(AGENT_OFFSET_05_CLASS_ID2, 111);
      ag3.set2(AGENT_OFFSET_04_INTERFACE_ID2, 2);
      bc.addAgent(ag3);

   }

   public ByteController helperGetCtrl() {
      IJavaObjectFactory fac = new HelperByteFactory(boc);
      return new ByteController(boc, fac);
   }

   public void setupAbstract() {
      super.setupAbstract();

   }

   public void testAdd3Agents() {
      ByteController bc = new ByteController(boc, getFactoryNew());

      helperAdd3Agents(bc);

      assertEquals(3, bc.getNumLiveAgents());

      System.out.println(bc.toString());

   }

   public void testAddAgents() {

      ByteController bc = helperGetCtrl();

      assertEquals(bc.getNumLiveAgents(), 0);

      ByteObjectManaged ag1 = new ByteObjectManaged(boc);

      bc.addAgent(ag1);

      assertEquals(bc.getNumLiveAgents(), 1);

      assertEquals(bc, ag1.getByteController());
   }

   public void testAddSameAgent() {
      ByteController bc = helperGetCtrl();

      ByteObjectManaged bo = getBOM(450);

      bc.addAgent(bo);
      bc.addAgent(bo);

      assertEquals(1, bc.getNumLiveAgents());

      ByteObjectManaged bo2 = getBOM(450);
      bc.addAgent(bo2);

      assertEquals(2, bc.getNumLiveAgents());

   }

   public void testBasic() {

      ByteController bc = helperGetCtrl();

      int headerSize = AGENT_BASIC_SIZE + 2;
      ByteObjectManaged bom = boc.getByteObjectManagedFactory().createByteObject(headerSize);

      bc.addAgent(bom);

   }

   public void testConstructorMemorySource() {

      int classid = 450;
      byte[] array = getBOM(classid).getByteObjectData();
      MemorySource dataSource = new ByteArraySource(boc, "TestConstructor", array);

      ByteController bc = new ByteController(boc, getFactoryNew(), dataSource);

      bc.getAgentFromClassID(classid);
   }

   public void testExpandMemoryExpulse() {
      ByteController bc = new ByteController(boc, getFactoryNew());
      bc.setExpansionPolicy(MEMC_EX_POLICY_2_EXPULSE);

      ByteObjectManaged ag1 = new ByteObjectManaged(boc);

      ag1.expandBuffer(50);

   }

   public void testExpandMemoryMultiple() {
      ByteController bc = new ByteController(boc, getFactoryNew());
      bc.setExpansionPolicy(MEMC_EX_POLICY_1_MULTIPLE);

      ByteObjectManaged ag1 = new ByteObjectManaged(boc);
      ag1.expandBuffer(50);

   }

   public void testExpandMemorySingle() {
      ByteController bc = new ByteController(boc, getFactoryNew());
      bc.setExpansionPolicy(MEMC_EX_POLICY_3_SINGLE);

      ByteObjectManaged ag1 = new ByteObjectManaged(boc);
      ag1.expandBuffer(50);

   }

   public void testFindAgents() {

      ByteController bc = new ByteController(boc, getFactoryNew());

      helperAdd3Agents(bc);

      assertEquals(3, bc.getNumLiveAgents());

      System.out.println(bc.toString());

      //create a search template.  we only all class ids
      ByteObjectManaged ag1 = new ByteObjectManaged(boc);
      ag1.set2(AGENT_OFFSET_05_CLASS_ID2, 4);

      ByteObjectManaged[] fs = bc.findAgents(ag1);
      assertEquals(fs.length, 2);

      ag1.set2(AGENT_OFFSET_06_GSOURCE_ID2, 4);
      fs = bc.findAgents(ag1);
      assertEquals(fs.length, 0);

      ByteObjectManaged ag111 = new ByteObjectManaged(boc);
      ag111.set2(AGENT_OFFSET_05_CLASS_ID2, 111);

      ByteObjectManaged[] fs111 = bc.findAgents(ag111);
      assertEquals(fs111.length, 1);

   }

   public void testGetController() {
      MemorySource rss = createSourceForName("testSave");

      ByteController bc = new ByteController(boc, getFactoryNew(), rss);

      helperAdd3Agents(bc);

      ByteObjectManaged[] ags = bc.findAgentsGroup(2);
      System.out.println(bc);

      assertEquals(3, ags.length);
      ByteObjectManaged bo = bc.getByteController(ags);

      System.out.println(bo);

   }

   public void testInterfaces() {

      //

      int[] ids = new int[] { 78, 55, 99 };

      ByteObjectManaged bom = getTechDefault(boc, 45, 14, ids);

      int[] ii = bom.getIDInterfaces();
      assertEquals(3, ii.length);
      assertEquals(78, ii[0]);
      assertEquals(55, ii[1]);
      assertEquals(99, ii[2]);

   }

   /**
    * We have a complex user Trie.
    * A build version is created
    */
   public void testLifeObject() {

      //
   }

   public void testLinkAllAgents() {
      ByteController bc = new ByteController(boc, getFactoryNew());

      ByteObjectManaged bom1 = getTechDefault(boc, AGENT_BASIC_SIZE, 1);
      ByteObjectManaged bom2 = getTechDefault(boc, AGENT_BASIC_SIZE, 2);
      ByteObjectManaged bom3 = getTechDefault(boc, AGENT_BASIC_SIZE, 3);
      ByteObjectManaged bom4 = getTechDefault(boc, AGENT_BASIC_SIZE, 4);

      bom1.addByteObject(bom2);
      bom2.addByteObject(bom3);
      bom1.addByteObject(bom4);
      bom1.addByteObject(boc.getByteObjectFactory().createByteObject(IBOTypesBOC.TYPE_003_LIT_STRING, 45));
      bom1.addByteObject((ByteObject) null);

      bc.linkAllAgents(bom1);

      assertEquals(4, bc.getNumLiveAgents());

   }

   /**
    * Test loading Agents from Source
    */
   public void testLoadAgents() {

      ByteController bc = new ByteController(boc, getFactoryNew());

      bc.loadAllAgents();
   }

   /**
    * 
    */
   public void testLoadRoot() {
      //no roots
      ByteController bc = getBC();

      assertEquals(4, bc.getNumLiveAgents());
      bc.loadAllAgents();
      assertEquals(4, bc.getNumLiveAgents());

      bc.saveAgents();

      System.out.println(bc);
      byte[] serial = bc.serializeAll();

      ByteController w = new ByteController(boc, getFactoryNew(), serial);

      ByteObjectManaged bRoot = w.getAgentRoot();

      System.out.println(w.toString());
      assertNotNull(bRoot);
      assertEquals(401, bRoot.get2(AGENT_OFFSET_05_CLASS_ID2));

      assertEquals(4, w.getNumLiveAgents());
   }

   public void testLoadRootOut() {
      //no roots
      ByteController bc = getBC();

      assertEquals(4, bc.getNumLiveAgents());
      bc.loadAllAgents();
      assertEquals(4, bc.getNumLiveAgents());

      bc.saveAgents();

      System.out.println(bc);
      byte[] serial = bc.serializeAll();

      ByteController w = new ByteController(boc, getFactoryNew(), serial);

      ByteObjectManaged bRoot = w.getAgentRootOut();

      assertEquals(null, bRoot.getByteController());

      System.out.println(w.toString());
      assertNotNull(bRoot);
      assertEquals(401, bRoot.get2(AGENT_OFFSET_05_CLASS_ID2));

      assertEquals(3, w.getNumLiveAgents());
   }

   /**
    * 
    */
   public void testLoop() {

   }

   public void testMultipleSources() {

      //for this method we want those debug flags
      uc.toStringSetToStringFlag(IToStringFlags.FLAG_DATA_01_SUCCINT, true);
      boc.toStringSetToStringFlag(IToStringFlagsBO.TOSTRING_FLAG_4_BYTEOBJECT_1LINE, true);

      RootSource rs = boc.getRootSource();
      //the order of the source is unknown. the root might be in rss3
      MemorySource rss1 = rs.registerMemorySource(createSourceForName("testSave1"));
      MemorySource rss2 = rs.registerMemorySource(createSourceForName("testSave2"));
      MemorySource rss3 = rs.registerMemorySource(createSourceForName("testSave3"));

      ByteObjectManaged tech = boc.getByteControllerFactory().getTechDefault();
      tech.set1(IBOByteControler.MEMC_OFFSET_02_MODE1, MEMC_EX_POLICY_1_MULTIPLE);

      MemorySource[] dataSources = new MemorySource[] { rss1, rss2, rss3 };
      IJavaObjectFactory javaObjectFactory = getFactoryNew();
      ByteController bc = new ByteController(boc, javaObjectFactory, tech, dataSources);

      //this should not bomb even though the stores are empty. there is nothing to load
      bc.loadAllAgents();

      ByteObjectManaged main = getBOM(401);
      //design that this agent shall be in first memory source.
      main.set2(AGENT_OFFSET_06_GSOURCE_ID2, 0);

      bc.addAgent(main);

      assertEquals(0, main.getBCAgentIndex());
      assertEquals(0, main.getBCSourceIndex());
      assertEquals(0, main.getBCInstanceIndex());

      ByteObjectManaged shared1 = getBOM(406);
      shared1.set2(AGENT_OFFSET_06_GSOURCE_ID2, 2);

      bc.addAgent(shared1);

      ByteObjectManaged shared2 = getBOM(406);
      shared2.set2(AGENT_OFFSET_06_GSOURCE_ID2, 0);
      bc.addAgent(shared2);

      // 10 agents are linked to MemorySource
      ByteObjectManaged[] several = new ByteObjectManaged[10];
      for (int i = 0; i < several.length; i++) {
         several[i] = getBOM(405);
         several[i].set2(AGENT_OFFSET_06_GSOURCE_ID2, 1);
         several[i].set2(AGENT_OFFSET_07_INSTANCE_ID2, i);
         //several[i].addByteObject(shared);
         bc.addAgent(several[i]);
      }

      assertEquals(0, main.getBCAgentIndex());
      assertEquals(1, shared1.getBCAgentIndex());
      assertEquals(2, shared2.getBCAgentIndex());

      assertEquals(1, main.getIDRef());
      assertEquals(2, shared1.getIDRef());
      assertEquals(3, shared2.getIDRef());

      assertEquals(4, several[0].getIDRef());
      assertEquals(5, several[1].getIDRef());
      assertEquals(13, several[9].getIDRef());

      assertEquals(13, bc.getNumLiveAgents());
      //main.addByteObject(several);
      //main.addByteObject(shared);

      ByteObjectManaged root = bc.getAgentRoot();
      assertNotNull(root);

      System.out.println("ByteController Full BEFORE Save \n" + bc);

      bc.saveAgents();

      System.out.println(bc);

      System.out.println("ByteController Full AFTER Save \n" + bc);

      //TRY loading only 1st
      byte[] data1 = rs.findSource("testSave1").load();
      assertNotNull(data1);
      assertEquals(true, data1.length != 0);

      //we should be able to create a ByteController
      ByteController bc1 = new ByteController(boc, javaObjectFactory, data1);
      bc1.loadAllAgents();

      //what if there is no root?
      ByteObjectManaged root1 = bc1.getAgentRoot();
      assertNotNull(root1);

      System.out.println("ByteController from testSave1 \n" + bc1);

      assertEquals(2, bc1.getNumLiveAgents());
      //FYI

      //TRY loading only 2nd
      //try loading only a portion of the data
      byte[] data2 = rs.findSource("testSave2").load();

      //System.out.println(bs.toString("testSave2"));
      assertNotNull(data2);
      assertEquals(true, data2.length != 0);

      //you cannot create a store source of the same. each app has a RootSource 
      //when you try to create an already existing source, you get the old one
      MemorySource rssTestSave2 = rs.registerMemorySource(createSourceForName("testSave2"));
      assertEquals(rss2, rssTestSave2);
      //create a new byte controller and new agents on the same underlying memory source
      ByteController bc2 = new ByteController(boc, javaObjectFactory, rssTestSave2);
      System.out.println("ByteController from testSave2 \n" + bc2);
      bc2.loadAllAgents();
      System.out.println("ByteController from testSave2 \n" + bc2);
      assertEquals(10, bc2.getNumLiveAgents());
      ByteObjectManaged root2 = bc2.getAgentRoot();
      assertNotNull(root2);

      //loading a portion of the data means the byte array lives in 2 different object

      //assertEquals(1, bs.getSize("testSave2"));

      byte[] data3 = rs.findSource("testSave3").load();
      assertNotNull(data3);
      assertEquals(true, data3.length != 0);
      ByteController bc3 = new ByteController(boc, javaObjectFactory, data3);
      bc3.loadAllAgents();

      System.out.println("ByteController from testSave3 \n" + bc1);
      assertEquals(1, bc3.getNumLiveAgents());

      //assertEquals(1, bs.getSize("testSave3"));

      //reload into the an object.
      ByteController bcR = new ByteController(boc, javaObjectFactory, tech, dataSources);

      ByteObjectManaged mainR = bcR.getAgentRoot();
      assertNotNull(mainR);

      assertEquals(401, mainR.get2(AGENT_OFFSET_05_CLASS_ID2));

   }

   /**
    * Test Agent Save to single source
    */
   public void testSaveAgentsSingleSource() {

      RootSource rs = boc.getRootSource();
      //conception
      MemorySource rss = rs.registerMemorySource(createSourceForName("testSave"));

      ByteController bc = new ByteController(boc, getFactoryNew(), rss);

      int classid = 4;
      ByteObjectManaged agent1 = new ByteObjectManaged(boc);
      agent1.set2(AGENT_OFFSET_05_CLASS_ID2, classid);

      assertEquals(ByteObjectManaged.AGENT_BASIC_SIZE, 40);

      assertEquals(agent1.getLength(), 40);

      assertEquals(bc.getNumLiveAgents(), 0);

      bc.addAgent(agent1);
      assertEquals(bc.getNumLiveAgents(), 1);

      int[] report = bc.saveAgents();

      assertEquals(1, report[0]); //number of agents written
      assertEquals(178, report[1]); //number of bytes written

      ByteController bc2 = new ByteController(boc, getFactoryNew(), rss);

      bc2.loadAllAgents();

      logPrint(bc2.toString());

      ByteObjectManaged bom12 = bc2.getAgentFromClassID(classid);

      assertNotNull(bom12);

      logPrint("Root " + agent1.toString());
      logPrint("Serialized " + bom12.toString());
      //to test we must set the bytecontroller flag to zero
      bom12.set1(AGENT_OFFSET_03_FLAGZ_1, 0);
      assertTrue(bom12.equalsContent(agent1));
   }

   /**
    * Tests the method {@link ByteController#serializeToUpdateAgentData(ByteObjectManaged, byte[])}
    * <br>
    */
   public void testUpdateAgentDataEmpty() {

      //in build mode. it gives a ref. ref is the number
      ByteController bc = new ByteController(boc, getFactoryNew());

      assertEquals(bc.getNumLiveAgents(), 0);

      ByteObjectManaged ag1 = boc.getByteObjectManagedFactory().getDefault(boc, 0, 10);
      //first ref is zero. undefined
      assertEquals(0, ag1.getIDRef());
      assertEquals(bc.getNumMemoryAreas(), 0);

      bc.addAgent(ag1);
      assertEquals(bc.getNumLiveAgents(), 1);
      //adding the same agent reference should not change the state of the byte
      bc.addAgent(ag1);
      assertEquals(bc.getNumLiveAgents(), 1);

      int refid = ag1.getIDRef();
      assertEquals(1, refid);

      byte[] data = ag1.toByteArray();

      ByteObjectManaged nbom = new ByteObjectManaged(boc, data);
      nbom.set4(nbom.getDataOffsetStartLoaded(), 123456789);

      assertEquals(123456789, nbom.get4(nbom.getDataOffsetStartLoaded()));

      data = nbom.toByteArray();

      ByteController bc2 = new ByteController(boc, getFactoryNew());

      ByteObjectManaged upBom = bc2.serializeToUpdateAgentData(data);

      assertEquals(true, ag1 != upBom);
      assertEquals(bc.getNumLiveAgents(), 1);

      //check 123456789 update was made
      ByteObjectManaged nbom1 = bc.getAgentFromRef(refid);
      assertEquals(0, nbom1.get4(nbom.getDataOffsetStartLoaded()));

      assertEquals(bc.getNumLiveAgents(), 1);

      logPrint(bc.toString());

   }
}
