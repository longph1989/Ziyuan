/*
 * This file was automatically generated by EvoSuite
 * Tue Oct 17 07:09:22 GMT 2017
 */

package org.apache.commons.math.analysis.integration;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.math.analysis.Expm1Function;
import org.apache.commons.math.analysis.SincFunction;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.integration.TrapezoidIntegrator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true, useJEE = true) 
public class TrapezoidIntegrator_ESTest extends TrapezoidIntegrator_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test0()  throws Throwable  {
      Expm1Function expm1Function0 = new Expm1Function();
      UnivariateRealFunction univariateRealFunction0 = expm1Function0.derivative();
      TrapezoidIntegrator trapezoidIntegrator0 = new TrapezoidIntegrator();
      double double0 = trapezoidIntegrator0.integrate(univariateRealFunction0, (-1002.48), (-593.8692));
      assertEquals(3, trapezoidIntegrator0.getIterationCount());
      assertEquals(3.1122417640714872E-257, double0, 0.01);
  }

  @Test(timeout = 4000)
  public void test1()  throws Throwable  {
      Expm1Function expm1Function0 = new Expm1Function();
      TrapezoidIntegrator trapezoidIntegrator0 = new TrapezoidIntegrator();
      trapezoidIntegrator0.setMaximalIterationCount(6);
      try { 
        trapezoidIntegrator0.integrate((UnivariateRealFunction) expm1Function0, (-408.9), 1488.0406776887396);
        fail("Expecting exception: Exception");
      
      } catch(Exception e) {
         //
         // maximal number of iterations (6) exceeded
         //
         verifyException("org.apache.commons.math.analysis.integration.TrapezoidIntegrator", e);
      }
  }

  @Test(timeout = 4000)
  public void test2()  throws Throwable  {
      SincFunction sincFunction0 = new SincFunction();
      TrapezoidIntegrator trapezoidIntegrator0 = new TrapezoidIntegrator(sincFunction0);
      trapezoidIntegrator0.integrate((UnivariateRealFunction) sincFunction0, 0.0, 1.0);
  }
}