/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.eclipse.ui.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;

/**
 * AbstractPMDRecord for the WorkspaceRoot creates ProjectRecords when
 * instantiated
 *
 * @author SebastianRaffel ( 16.05.2005 ), Philippe Herlin, Sven Jacob
 *
 */
public class RootRecord extends AbstractPMDRecord {
    private final IWorkspaceRoot workspaceRoot;
    private AbstractPMDRecord[] children;

    /**
     * Constructor
     *
     * @param root, the WorkspaceRoot
     */
    public RootRecord(IWorkspaceRoot root) {
        super();

        if (root == null) {
            throw new IllegalArgumentException("roor cannot be null");
        }

        this.workspaceRoot = root;
        this.children = createChildren();
    }

    /**
     * @see net.sourceforge.pmd.eclipse.ui.model.AbstractPMDRecord#getParent()
     */
    @Override
    public AbstractPMDRecord getParent() {
        return this;
    }

    /**
     * @see net.sourceforge.pmd.eclipse.ui.model.AbstractPMDRecord#getChildren()
     */
    @Override
    public AbstractPMDRecord[] getChildren() {
        return children; // NOPMD by Herlin on 09/10/06 00:56
    }

    /**
     * @see net.sourceforge.pmd.eclipse.ui.model.AbstractPMDRecord#getResource()
     */
    @Override
    public IResource getResource() {
        return this.workspaceRoot;
    }

    /**
     * @see net.sourceforge.pmd.eclipse.ui.model.AbstractPMDRecord#createChildren()
     */
    @Override
    protected final AbstractPMDRecord[] createChildren() {
        // get the projects
        final IProject[] projects = this.workspaceRoot.getProjects();
        final List<AbstractPMDRecord> projectList = new ArrayList<AbstractPMDRecord>();

        // ... and create Records for them
        for (IProject project : projects) {
            if (project.isOpen()) {
                projectList.add(new ProjectRecord(project, this));
            }
        }

        // return the Array of children
        return projectList.toArray(new AbstractPMDRecord[projectList.size()]);
    }

    /**
     * @see net.sourceforge.pmd.eclipse.ui.model.AbstractPMDRecord#addResource(org.eclipse.core.resources.IResource)
     */
    @Override
    public AbstractPMDRecord addResource(IResource resource) {
        return resource instanceof IProject ? addProject((IProject) resource) : null;
    }

    /**
     * @see net.sourceforge.pmd.eclipse.ui.model.AbstractPMDRecord#removeResource(org.eclipse.core.resources.IResource)
     */
    @Override
    public AbstractPMDRecord removeResource(IResource resource) {
        return resource instanceof IProject ? removeProject((IProject) resource) : null;
    }

    /**
     * @see net.sourceforge.pmd.eclipse.ui.model.AbstractPMDRecord#getName()
     */
    @Override
    public String getName() {
        return workspaceRoot.getName();
    }

    /**
     * @see net.sourceforge.pmd.eclipse.ui.model.AbstractPMDRecord#getResourceType()
     */
    @Override
    public int getResourceType() {
        return TYPE_ROOT;
    }

    /**
     * Creates a new ProjectRecord and adds it to the List of ProjectRecords
     *
     * @param project
     * @return the ProjectRecord created for the Project or null, if the Project
     *         is not open
     */
    private ProjectRecord addProject(IProject project) {
        ProjectRecord addedProject = null;
        if (project.isOpen()) {
            final List<AbstractPMDRecord> projects = getChildrenAsList();
            final ProjectRecord projectRec = new ProjectRecord(project, this);
            projects.add(projectRec);

            children = new AbstractPMDRecord[projects.size()];
            projects.toArray(children);
            addedProject = projectRec;
        }
        return addedProject;
    }

    /**
     * Searches with a given Project for a Record containing this Project;
     * removes and returns this ProjectRecord
     *
     * @param project
     * @return the removed ProjectRecord
     */
    private ProjectRecord removeProject(IProject project) {
        ProjectRecord removedProject = null;

        final List<AbstractPMDRecord> projects = getChildrenAsList();
        for (int k = 0; k < projects.size() && removedProject == null; k++) {
            final ProjectRecord projectRec = (ProjectRecord) projects.get(k);
            final IProject proj = (IProject) projectRec.getResource();

            // get the Project-Resource from the List and compare
            if (proj.equals(project)) {
                projects.remove(projectRec);

                children = new AbstractPMDRecord[projects.size()];
                projects.toArray(children);
                removedProject = projectRec;
            }
        }

        return removedProject;
    }

    /**
     * @see net.sourceforge.pmd.eclipse.ui.model.AbstractPMDRecord#getNumberOfViolationsToPriority(int)
     */
    @Override
    public int getNumberOfViolationsToPriority(int prio, boolean invertMarkerAndFileRecords) {
        int number = 0;
        for (AbstractPMDRecord element : children) {
            number += element.getNumberOfViolationsToPriority(prio, invertMarkerAndFileRecords);
        }

        return number;
    }

    /* (non-Javadoc)
     * @see net.sourceforge.pmd.eclipse.ui.model.AbstractPMDRecord#getLOC()
     */
    @Override
    public int getLOC() {
        int number = 0;
        for (AbstractPMDRecord element : children) {
            number += element.getLOC();
        }

        return number;
    }

    /* (non-Javadoc)
     * @see net.sourceforge.pmd.eclipse.ui.model.AbstractPMDRecord#getNumberOfMethods()
     */
    @Override
    public int getNumberOfMethods() {
        int number = 0;
        for (AbstractPMDRecord element : children) {
            number += element.getNumberOfMethods();
        }

        return number;
    }
}
