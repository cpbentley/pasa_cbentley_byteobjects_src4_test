/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.stator.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class SuiteCoreStatorBOSrc4 extends TestSuite {
   
   public static Test suite() {

      TestSuite suite = new TestSuite("Tests for StatorBO");
      suite.addTestSuite(TestStatorBO.class);
      suite.addTestSuite(TestStatorReaderBO.class);
      suite.addTestSuite(TestStatorWriterBO.class);

      return suite;
   }

}
