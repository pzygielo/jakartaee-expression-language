/*
 * Copyright (c) 2009, 2025 Oracle and/or its affiliates and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * $Id$
 */

package com.sun.ts.tests.el.api.jakarta_el.methodinfo;

import com.sun.ts.tests.el.common.api.expression.ExpressionTest;
import com.sun.ts.tests.el.common.elcontext.SimpleELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;
import com.sun.ts.tests.el.common.util.ResolverType;

import jakarta.el.ELContext;
import jakarta.el.ExpressionFactory;
import jakarta.el.MethodExpression;
import jakarta.el.MethodInfo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.lang.System.Logger;

public class ELClientIT {

  private static final Logger logger = System.getLogger(ELClientIT.class.getName());

  @AfterEach
  public void cleanup() throws Exception {
    logger.log(Logger.Level.INFO, "Cleanup method called");
  }

  @BeforeEach
  void logStartTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "STARTING TEST : "+testInfo.getDisplayName());
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "FINISHED TEST : "+testInfo.getDisplayName());
  }

  /**
   * @testName: methodInfoTest
   *
   * @assertion_ids: EL:JAVADOC:411;EL:JAVADOC:412;EL:JAVADOC:413
   * @test_Strategy: Validate the behavior of MethodInfo MethodInfo class
   *                 methods: MethodInfo.getName() MethodInfo.getReturnType()
   *                 MethodInfo.getParamTypes()
   */
  @Test
  public void methodInfoTest() throws Exception {

    StringBuffer buf = new StringBuffer();

    boolean pass1, pass2, pass3, pass4 = false;

    try {
      ExpressionFactory expFactory = ExpressionFactory.newInstance();

      SimpleELContext simpleContext = new SimpleELContext(
          ResolverType.VECT_ELRESOLVER);
      ELContext context = simpleContext.getELContext();

      // case 1: non-null return value
      Class<?>[] paramTypes1 = { Object.class };
      MethodExpression mexp1 = expFactory.createMethodExpression(context,
          "#{vect.add}", boolean.class, paramTypes1);
      MethodInfo minfo1 = mexp1.getMethodInfo(context);
      pass1 = ExpressionTest.testMethodInfo(minfo1, "add", boolean.class, 1,
          paramTypes1, buf);

      // case 2: null return value
      Class<?>[] paramTypes2 = { int.class, Object.class };

      MethodExpression mexp2 = expFactory.createMethodExpression(context,
          "#{vect.add}", null, paramTypes2);
      MethodInfo minfo2 = mexp2.getMethodInfo(context);
      pass2 = ExpressionTest.testMethodInfo(minfo2, "add", void.class, 2,
          paramTypes2, buf);

      // case 3: literal expression returning String
      Class<?>[] paramTypes3 = {};

      MethodExpression mexp3 = expFactory.createMethodExpression(context,
          "true", String.class, paramTypes3);
      MethodInfo minfo3 = mexp3.getMethodInfo(context);
      pass3 = ExpressionTest.testMethodInfo(minfo3, "true", String.class, 0,
          paramTypes3, buf);

      // case 4: literal expression returning non-String value
      Class<?>[] paramTypes4 = {};

      MethodExpression mexp4 = expFactory.createMethodExpression(context,
          "true", Boolean.class, paramTypes4);
      MethodInfo minfo4 = mexp4.getMethodInfo(context);
      pass4 = ExpressionTest.testMethodInfo(minfo4, "true", Boolean.class, 0,
          paramTypes4, buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!(pass1 && pass2 && pass3 && pass4))
      throw new Exception(ELTestUtil.FAIL + buf.toString());
  }
}
