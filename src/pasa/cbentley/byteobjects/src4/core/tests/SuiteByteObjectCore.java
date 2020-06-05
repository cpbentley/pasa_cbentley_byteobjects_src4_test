/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.core.tests;

import junit.framework.Test;
import pasa.cbentley.core.src4.ctx.IConfigU;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.testing.ctx.TestCtx;
import pasa.cbentley.testing.engine.ConfigUTest;
import pasa.cbentley.testing.engine.TestSuiteBentley;

public class SuiteByteObjectCore extends TestSuiteBentley {

   public SuiteByteObjectCore(TestCtx tc, String name) {
      super(tc, name);
   }

   public static Test suite() {

      //create the configuration here

      IConfigU configU = new ConfigUTest();
      UCtx uc = new UCtx(configU);

      //custom test context
      TestCtx tc = new TestCtx(uc);

      SuiteByteObjectCore suite = new SuiteByteObjectCore(tc, "Tests for byteobjects");

      suite.addTestSuite(TestByteObject.class);
      suite.addTestSuite(TestByteObjectManaged.class);
      suite.addTestSuite(TestByteController.class);
      suite.addTestSuite(TestByteObjectManagedWithController.class);
      suite.addTestSuite(TestLockManager.class);

      return suite;
   }

}
