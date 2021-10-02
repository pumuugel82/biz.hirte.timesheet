package biz.hirte.timesheet.view;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import biz.hirte.timesheet.model.IProject;

public class ProjectPeriodTreeContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getElements(Object inputElement) {
		return ((java.util.List) inputElement).toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {

		if (parentElement instanceof IProject) {
			return ((IProject) parentElement).getPeriods().toArray();
		}

		return null;
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {

		if (element instanceof IProject) {
			return ((IProject) element).getPeriods().size() > 0;
		}

		return false;
	}

}
