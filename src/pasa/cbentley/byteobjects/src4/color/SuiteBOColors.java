package pasa.cbentley.byteobjects.src4.color;

import junit.framework.Test;
import junit.framework.TestSuite;

public class SuiteBOColors extends TestSuite {

   public static Test suite() {

      TestSuite suite = new TestSuite("Test for All Base Draw Classes");
      
      suite.addTestSuite(TestColorIterator.class);
      suite.addTestSuite(TestGradientFunction.class);
      suite.addTestSuite(TestColorFunction.class);
      
      return suite;
   }
}
