package biz.hirte.timesheet.view;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import biz.hirte.timesheet.model.IPeriod;
import biz.hirte.timesheet.model.IProject;
import biz.hirte.timesheet.model.util.Durations;

public class ProjectPeriodTreeLabelProvider implements ITableLabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	private DateTimeFormatter	SDF_DATE	= DateTimeFormatter.ofPattern("dd.MM.yyyy");
	private DateTimeFormatter	SDF_HHMM	= DateTimeFormatter.ofPattern("HH:mm");
	private DateTimeFormatter	SDF_DAYNAME	= DateTimeFormatter.ofPattern("EEEE");

	@Override
	public String getColumnText(Object element, int columnIndex) {

		if (element instanceof IProject) {
			IProject project = (IProject) element;
			if (columnIndex == 0) {
				double totalDuration = Durations.calculateOverallPeriodsDuration(project.getPeriods());
				return String.format("%s (%s)", project.getName(), formatDuration((long) totalDuration));
			} else if (columnIndex == 7) {
				return project.getDescription();
			} else {
				return "";
			}
		}

		if (element instanceof IPeriod) {
			IPeriod p = (IPeriod) element;
			if (columnIndex == 0) {
				return SDF_DAYNAME.format(p.getDay());
			} else if (columnIndex == 1) {
				return SDF_DATE.format(p.getDay());
			} else if (columnIndex == 2) {
				return SDF_HHMM.format(p.getBegin());
			} else if (columnIndex == 3) {
				return SDF_HHMM.format(p.getEnd());
			} else if (columnIndex == 4) {
				return formatDuration(p.getDuration().toMinutes());
			} else if (columnIndex == 5) {
				return formatDuration(p.getBreakDuration().toMinutes());
			} else if (columnIndex == 6) {
				Duration effDuration = p.getDuration().minus(p.getBreakDuration());
				return formatDuration(effDuration.toMinutes());
			} else if (columnIndex == 7) {
				return p.getComment();
			}
		}
		return null;
	}

	private String formatDuration(long dur) {
		double hrs = (dur / 60d);
		return String.format("%1$.2f", hrs);
	}

}
