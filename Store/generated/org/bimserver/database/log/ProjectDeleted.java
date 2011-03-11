/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.bimserver.database.log;

import org.bimserver.database.store.Project;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Project Deleted</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.bimserver.database.log.ProjectDeleted#getProject <em>Project</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.bimserver.database.log.LogPackage#getProjectDeleted()
 * @model
 * @generated
 */
public interface ProjectDeleted extends LogAction {
	/**
	 * Returns the value of the '<em><b>Project</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Project</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Project</em>' reference.
	 * @see #setProject(Project)
	 * @see org.bimserver.database.log.LogPackage#getProjectDeleted_Project()
	 * @model
	 * @generated
	 */
	Project getProject();

	/**
	 * Sets the value of the '{@link org.bimserver.database.log.ProjectDeleted#getProject <em>Project</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Project</em>' reference.
	 * @see #getProject()
	 * @generated
	 */
	void setProject(Project value);

} // ProjectDeleted