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

package com.sun.ts.tests.el.common.functionmapper;

import java.lang.reflect.Method;
import java.util.HashMap;

import com.sun.ts.tests.el.common.util.ELTestUtil;

import jakarta.el.FunctionMapper;

import java.lang.System.Logger;

/* A simple implementation of FunctionMapper that maps only
   a single function to Integer.valueOf(String).
*/

public class TCKFunctionMapper extends FunctionMapper {

  private static final Logger logger = System.getLogger(TCKFunctionMapper.class.getName());

  private static final String KEY = "Int:val";

  private final Class<?> clazz = Integer.class;

  private final HashMap<String, Method> fMap;

  public TCKFunctionMapper() {

    fMap = new HashMap<>();
    try {
      fMap.put(KEY, clazz.getMethod("valueOf", String.class));
    } catch (NoSuchMethodException nsme) {
      logger.log(Logger.Level.ERROR, "CONSTRUCTOR: Can't find method!");
      ELTestUtil.printStackTrace(nsme);
    }
  }

  @Override
  public Method resolveFunction(String prefix, String localName) {

    String key = prefix + ":" + localName;
    return fMap.get(key);
  }

  public void update() {

    fMap.remove(KEY);
    try {
      fMap.put(KEY, clazz.getMethod("toString", int.class));
    } catch (NoSuchMethodException nsme) {
      logger.log(Logger.Level.ERROR, "UPDATE: Can't find method!");
      ELTestUtil.printStackTrace(nsme);
    }
  }
}
