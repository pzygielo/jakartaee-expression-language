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
package com.sun.ts.tests.el.common.elcontext;

import java.util.Properties;


import com.sun.ts.tests.el.api.jakarta_el.valueexpression.Worker;
import com.sun.ts.tests.el.common.elresolver.VariableELResolver;
import com.sun.ts.tests.el.common.functionmapper.TCKFunctionMapper;

import jakarta.el.BeanELResolver;
import jakarta.el.CompositeELResolver;
import jakarta.el.ELContext;
import jakarta.el.ELResolver;
import jakarta.el.FunctionMapper;
import jakarta.el.VariableMapper;

import java.lang.System.Logger;
import java.lang.reflect.InvocationTargetException;

public class VRContext extends ELContext {

  private static final Logger logger = System.getLogger(VRContext.class.getName());

  private final VariableMapper varMapper;

  private final CompositeELResolver compResolver;

  public VRContext(Properties testProps) {
    this.compResolver = new CompositeELResolver();
    varMapper = this.getVariableMapperImpl(testProps);
  }

  @Override
  public ELResolver getELResolver() {
    ELResolver elResolver;
    ELResolver rel = new VariableELResolver();
    rel.setValue(this, "worker", null, new Worker());
    compResolver.add(rel);

    BeanELResolver br = new BeanELResolver();
    br.setValue(this, "worker", null, new Worker());
    compResolver.add(br);
    elResolver = compResolver;

    return elResolver;
  }

  @Override
  public FunctionMapper getFunctionMapper() {

    return new TCKFunctionMapper();
  }

  @Override
  public VariableMapper getVariableMapper() {
    return varMapper;
  }

  private Object getImplSpecificInstance(String classname) {

    Class<?> clazz;
    Object instance = null;

    try {
      clazz = Class.forName(classname);
      instance = clazz.getConstructor().newInstance();
    } catch (ClassNotFoundException cnfe) {
      logger.log(Logger.Level.ERROR, "ClassNotFoundException: " + cnfe.getMessage());
    } catch (InstantiationException ie) {
      logger.log(Logger.Level.ERROR, "InstantiationException: " + ie.getMessage());
    } catch (IllegalAccessException iae) {
      logger.log(Logger.Level.ERROR, "IllegalAccessException: " + iae.getMessage());
    } catch (IllegalArgumentException e) {
      logger.log(Logger.Level.ERROR, "IllegalArgumentException: " + e.getMessage());
    } catch (InvocationTargetException e) {
      logger.log(Logger.Level.ERROR, "InvocationTargetException: " + e.getMessage());
    } catch (NoSuchMethodException e) {
      logger.log(Logger.Level.ERROR, "NoSuchMethodException: " + e.getMessage());
    } catch (SecurityException e) {
      logger.log(Logger.Level.ERROR, "SecurityException: " + e.getMessage());
    }
    return instance;
  }

  private VariableMapper getVariableMapperImpl(Properties testProps) {
    String implSpecificClassName = testProps.getProperty("variable.mapper");

    return (implSpecificClassName == null) ? null
        : (VariableMapper) getImplSpecificInstance(implSpecificClassName);
  }
}
