/*
 * Copyright (c) 2011 Google Inc.
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.google.eclipse.protobuf.scoping;

import static java.util.Collections.emptySet;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.resource.IEObjectDescription;

import com.google.eclipse.protobuf.model.util.Options;
import com.google.eclipse.protobuf.protobuf.*;
import com.google.inject.Inject;

/**
 * @author alruiz@google.com (Alex Ruiz)
 */
class CustomOptionFieldNameFinder {
  @Inject private Options options;

  Collection<IEObjectDescription> findFieldNamesSources(ComplexValue value, FinderStrategy finderStrategy) {
    MessageField source = sourceOf(value);
    if (source == null) {
      return emptySet();
    }
    return finderStrategy.findMessageFields(source);
  }

  private MessageField sourceOf(ComplexValue value) {
    IndexedElement source = null;
    EObject container = value.eContainer();
    if (container instanceof AbstractCustomOption) {
      AbstractCustomOption option = (AbstractCustomOption) container;
      source = options.sourceOf(option);
    }
    if (container instanceof ComplexValueField) {
      source = sourceOfNameOf((ComplexValueField) container);
    }
    return (source instanceof MessageField) ? (MessageField) source : null;
  }

  private MessageField sourceOfNameOf(ComplexValueField field) {
    FieldName name = field.getName();
    return (name == null) ? null : name.getTarget();
  }

  static interface FinderStrategy {
    Collection<IEObjectDescription> findMessageFields(MessageField reference);
  }
}
