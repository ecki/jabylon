/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package de.jutzig.jabylon.properties;

import java.util.Locale;
import org.eclipse.emf.cdo.CDOObject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Project</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.jutzig.jabylon.properties.Project#getName <em>Name</em>}</li>
 *   <li>{@link de.jutzig.jabylon.properties.Project#getWorkspace <em>Workspace</em>}</li>
 *   <li>{@link de.jutzig.jabylon.properties.Project#getVersions <em>Versions</em>}</li>
 *   <li>{@link de.jutzig.jabylon.properties.Project#getMaster <em>Master</em>}</li>
 *   <li>{@link de.jutzig.jabylon.properties.Project#getLocales <em>Locales</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.jutzig.jabylon.properties.PropertiesPackage#getProject()
 * @model
 * @extends CDOObject
 * @generated
 */
public interface Project extends CDOObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see de.jutzig.jabylon.properties.PropertiesPackage#getProject_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link de.jutzig.jabylon.properties.Project#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Workspace</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link de.jutzig.jabylon.properties.Workspace#getProjects <em>Projects</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Workspace</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Workspace</em>' container reference.
	 * @see #setWorkspace(Workspace)
	 * @see de.jutzig.jabylon.properties.PropertiesPackage#getProject_Workspace()
	 * @see de.jutzig.jabylon.properties.Workspace#getProjects
	 * @model opposite="projects" transient="false"
	 * @generated
	 */
	Workspace getWorkspace();

	/**
	 * Sets the value of the '{@link de.jutzig.jabylon.properties.Project#getWorkspace <em>Workspace</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Workspace</em>' container reference.
	 * @see #getWorkspace()
	 * @generated
	 */
	void setWorkspace(Workspace value);

	/**
	 * Returns the value of the '<em><b>Versions</b></em>' containment reference list.
	 * The list contents are of type {@link de.jutzig.jabylon.properties.ProjectVersion}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Versions</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Versions</em>' containment reference list.
	 * @see de.jutzig.jabylon.properties.PropertiesPackage#getProject_Versions()
	 * @model containment="true"
	 * @generated
	 */
	EList<ProjectVersion> getVersions();

	/**
	 * Returns the value of the '<em><b>Master</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Master</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Master</em>' containment reference.
	 * @see #setMaster(ProjectVersion)
	 * @see de.jutzig.jabylon.properties.PropertiesPackage#getProject_Master()
	 * @model containment="true"
	 * @generated
	 */
	ProjectVersion getMaster();

	/**
	 * Sets the value of the '{@link de.jutzig.jabylon.properties.Project#getMaster <em>Master</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Master</em>' containment reference.
	 * @see #getMaster()
	 * @generated
	 */
	void setMaster(ProjectVersion value);

	/**
	 * Returns the value of the '<em><b>Locales</b></em>' attribute list.
	 * The list contents are of type {@link java.util.Locale}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Locales</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Locales</em>' attribute list.
	 * @see de.jutzig.jabylon.properties.PropertiesPackage#getProject_Locales()
	 * @model unique="false" dataType="de.jutzig.jabylon.properties.Locale"
	 * @generated
	 */
	EList<Locale> getLocales();

} // Project
