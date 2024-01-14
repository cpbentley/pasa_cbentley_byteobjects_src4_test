/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.core.tests;

import pasa.cbentley.byteobjects.src4.core.ByteObjectManaged;
import pasa.cbentley.byteobjects.src4.core.interfaces.IBOAgentManaged;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.testing.engine.TestCaseBentley;

public class TestLockManager extends TestCaseBentley implements IBOAgentManaged {
   boolean       debugLock = false;

   final Integer lock      = new Integer(4);

   private BOCtx boc;


   private void debugLock() {
      if (debugLock) {
         toDLog().pTest("", boc.getLock(), TestLockManager.class, "debugLock");
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
      boc = new BOCtx(uc);
   }

   private void sleep(int val) {
      try {
         Thread.sleep(val);
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   public void testSynchroLock() {

      //enable thread name
      super.setEnableThreadName(true);

      final ByteObjectManaged cp = new ByteObjectManaged(boc);
      assertEquals(false, cp.isMultiThread());
      cp.setFlagMultiThread(true); //must enables locking with multi thread
      assertEquals(true, cp.isMultiThread());
      
      
      //get a lock
      cp.dataLock();
      logPrint(1, "Inside Lock for 1st Thread");

      //simulate a thread trying to access the lock

      assertEquals(true, cp.isLocked());
      assertEquals(false, cp.isWaiting());

      new Thread("2nd Thread") {

         public void run() {

            logPrint(2,"Before 2nd Thread Lock.. Waiting for Thread 1 to unlock #" + boc.getLock().getNumWaiting(cp));
            cp.dataLock();
            logPrint(8,"Inside Lock for 2nd Thread. FIFO Queue #" + boc.getLock().getNumWaiting(cp));

            //notify 3 thread to enter the dataLock
            //            synchronized (lock) {
            //               lock.notify();
            //            }

            assertEquals(true, cp.isLocked());
            assertEquals(true, cp.isWaiting());

            assertEquals(1, boc.getLock().getNumWaiting(cp));

            logPrint(7,"Before\t 2nd Thread UnLock #" + boc.getLock().getNumWaiting(cp));
            cp.dataUnLock();
            assertEquals(0, boc.getLock().getNumWaiting(cp));

            logPrint(8,"After\t 2nd Thread UnLock #" + boc.getLock().getNumWaiting(cp));

            assertEquals(false, cp.isLocked());
            assertEquals(true, cp.isWaiting());

         }
      }.start();

      //another will lock 
      new Thread("3rd Thread") {

         public void run() {

            //wait for 2nd thread to unlock
            logPrint(3, "Before", "3rd Thread Lock.. Waiting for Thread 2 to unlock");
            cp.dataLock();
            logPrint(9, " Inside Lock for 3rd Thread. FIFO Queue", " #" + boc.getLock().getNumWaiting(cp));

            assertEquals(0, boc.getLock().getNumWaiting(cp));
            assertEquals(true, cp.isLocked());
            assertEquals(false, cp.isWaiting());

            logPrint(10, " Before 3rd Thread UnLock #" + boc.getLock().getNumWaiting(cp));
            cp.dataUnLock();
            logPrint(11, " After 3rd Thread UnLock");

            assertEquals(false, cp.isLocked());
            assertEquals(false, cp.isWaiting());

         }
      }.start();

      //wait for threads 2 and 3 to lock
      sleep(1000);
      debugLock();
      assertEquals(true, cp.isWaiting());

      //the first thread entering the lock is never counted
      assertEquals(2, boc.getLock().getNumWaiting(cp));
      debugLock();

      logPrint(4, "Before 1st Thread UnLock");
      cp.dataUnLock();
      logPrint(5, "After 1st Thread UnLock");

      sleep(1100);

      debugLock();

      assertEquals(false, cp.isLocked());
      assertEquals(false, cp.isWaiting());
      assertEquals(0, boc.getLock().getNumWaiting(cp));

   }
}
