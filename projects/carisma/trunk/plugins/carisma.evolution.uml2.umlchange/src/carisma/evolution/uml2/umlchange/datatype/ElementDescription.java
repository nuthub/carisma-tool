/*******************************************************************************
 * Copyright (c) 2011 Software Engineering Institute, TU Dortmund.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    {SecSE group} - initial API and implementation and/or initial documentation
 *******************************************************************************/
package carisma.evolution.uml2.umlchange.datatype;

/**
 * Abstract class of element descriptions in
 * the UMLchange grammar.
 * @author Daniel Warzecha
 *
 */
public abstract class ElementDescription {
	/**
	 * The corresponding grammar string.
	 */
	private String grammarString = "";
	
	public ElementDescription(final String grammar) {
		this.grammarString = grammar.trim();	
	}
	
	public String getGrammarString() {
		return this.grammarString;
	}
	
	public abstract boolean isValid();
	
}
