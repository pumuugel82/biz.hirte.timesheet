package biz.hirte.timesheet.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class MergeDialog extends TitleAreaDialog {

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public MergeDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	public enum TextMergeOptionName {
		APPEND_LATEST, APPEND_FIRST, USE_FIRST, USE_LAST;
	}

	public enum TimeMergeOptionName {
		KEEP_NON_INTERFERING, // p1 und p2 berühren sich nicht, sie
								// bleiben so
		MERGE_INTERFERING_INTO_ONE_BIG, // überschneidene werte p1 und p2 werden
										// zu einer große p1'
		MERGE_INTO_ONE_BIG, // p1 und p2 berühren sich (nicht), es entsteht
		// eine große neue period
		INSERT_NEW_IN_GAP, // p1 und p2 berühren sich nicht, es wird eine dritte
							// Periode p3 eingefügt die zwischen die Grenzen von
							// p1 und p2 passt.
		SIZE_FIRST, // p1 wird bis an die Grenze von p2 verlängert. p2 bleibt
					// unverändert, respektive p1 wird verkürzt
		SIZE_LAST, // p1 bleibt unverändert, p2 wird bis an die Grenze von p1
					// nach vorne verlängert, repsektive verkürzt
		MOVE_FIRST, // p1's länge bleibt, wird aber nach hinten, an die vordere
					// Grenze von p2 verschoben
		// oder soweit nach vorne verschoben, dass p1's hintere Grenze an p2's
		// vordere Grenze passt
		MOVE_LAST; // p1 bleibt unverändert, p2 wird nach vorne, an die hintere
					// Grenze von p1 verschoben oder
		// p2 wird nach hinten verschoben, dass

	}

	public class MergeInformation {

	}

}
