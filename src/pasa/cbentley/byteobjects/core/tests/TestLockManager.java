package pasa.cbentley.byteobjects.core.tests;

import pasa.cbentley.byteobjects.core.ByteObjectManaged;
import pasa.cbentley.byteobjects.ctx.BOCtx;
import pasa.cbentley.byteobjects.tech.ITechObjectManaged;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.testing.BentleyTestCase;

public class TestLockManager extends BentleyTestCase implements ITechObjectManaged {
   final Integer lock      = new Integer(4);

   boolean       debugLock = false;

   private BOCtx mod;

   public TestLockManager() {
      super(false);
   }

   public void testSynchroLock() {

      //enable thread name
      super.setEnableThreadName(true);

      final ByteObjectManaged cp = new ByteObjectManaged(mod);

      //get a lock
      cp.dataLock();
      logPrint(1, "Inside Lock for 1st Thread");

      //simulate a thread trying to access the lock

      assertEquals(true, cp.isLocked());
      assertEquals(false, cp.isWaiting());

      new Thread("2nd Thread") {

         public void run() {

            logPrint("2\t Before 2nd Thread Lock.. Waiting for Thread 1 to unlock #" + mod.getLock().getNumWaiting(cp));
            cp.dataLock();
            logPrint("6\t Inside Lock for 2nd Thread. FIFO Queue #" + mod.getLock().getNumWaiting(cp));

            //notify 3 thread to enter the dataLock
            //            synchronized (lock) {
            //               lock.notify();
            //            }

            assertEquals(true, cp.hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_5_LOCKED));
            assertEquals(true, cp.hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_6_WAITING));

            assertEquals(1, mod.getLock().getNumWaiting(cp));

            logPrint("7\t Before\t 2nd Thread UnLock #" + mod.getLock().getNumWaiting(cp));
            cp.dataUnLock();
            assertEquals(0, mod.getLock().getNumWaiting(cp));

            logPrint("8\t After\t 2nd Thread UnLock #" + mod.getLock().getNumWaiting(cp));

            assertEquals(false, cp.hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_5_LOCKED));
            assertEquals(true, cp.hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_6_WAITING));

         }
      }.start();

      //another will lock 
      new Thread("3rd Thread") {

         public void run() {

            //wait for 2nd thread to unlock
            logPrint(3, "Before", "3rd Thread Lock.. Waiting for Thread 2 to unlock");
            cp.dataLock();
            logPrint(9, " Inside Lock for 3rd Thread. FIFO Queue", " #" + mod.getLock().getNumWaiting(cp));

            assertEquals(0, mod.getLock().getNumWaiting(cp));
            assertEquals(true, cp.hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_5_LOCKED));
            assertEquals(false, cp.hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_6_WAITING));

            logPrint(10, " Before 3rd Thread UnLock #" + mod.getLock().getNumWaiting(cp));
            cp.dataUnLock();
            logPrint(11, " After 3rd Thread UnLock");

            assertEquals(false, cp.hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_5_LOCKED));
            assertEquals(false, cp.hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_6_WAITING));

         }
      }.start();

      //wait for threads 2 and 3 to lock
      sleep(1000);
      debugLock();
      assertEquals(true, cp.hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_6_WAITING));

      //the first thread entering the lock is never counted
      assertEquals(2, mod.getLock().getNumWaiting(cp));
      debugLock();

      logPrint(4, "Before 1st Thread UnLock");
      cp.dataUnLock();
      logPrint(5,"After 1st Thread UnLock");

      sleep(1100);

      debugLock();

      assertEquals(false, cp.hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_5_LOCKED));
      assertEquals(false, cp.hasFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_6_WAITING));
      assertEquals(0, mod.getLock().getNumWaiting(cp));

   }

   private void debugLock() {
      if (debugLock) {
         toDLog().pTest("", mod.getLock(), TestLockManager.class, "debugLock");
      }

   }

   private void sleep(int val) {
      try {
         Thread.sleep(val);
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   private void lockMe() {
      try {
         synchronized (lock) {
            lock.wait();
         }
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   public void setupAbstract() {
      mod = new BOCtx(uc);
   }
}
