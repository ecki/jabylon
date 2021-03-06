package de.jutzig.jabylon.ui.components;

import org.eclipse.core.runtime.IProgressMonitor;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.jutzig.jabylon.common.progress.RunnableWithProgress;
import de.jutzig.jabylon.ui.Activator;
import de.jutzig.jabylon.ui.styles.JabylonStyle;

public class ProgressMonitorDialog extends Window implements IProgressMonitor,
		ClickListener {

	private ProgressIndicator indicator;
	private Label mainTask, subTask;
	private Window parent;
	private RunnableWithProgress runnable;
	private Button cancel;
	private int totalWork, actualWork;
	private boolean canceled;

	private void createContents() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeFull();
		mainTask = new Label();
		layout.addComponent(mainTask);
		mainTask.setWidth(100, UNITS_PERCENTAGE);

		indicator = new ProgressIndicator(0f);
		indicator.setStyleName(JabylonStyle.PROGRESS_INDICATOR.getCSSName());
		indicator.setSizeFull();
		layout.addComponent(indicator);

		subTask = new Label();
		layout.addComponent(subTask);
		subTask.setWidth(100, UNITS_PERCENTAGE);

		cancel = new Button(Messages.getString("ProgressMonitorDialog_CANCEL_BUTTON_CAPTION")); //$NON-NLS-1$
		cancel.addListener((ClickListener) this);
		layout.addComponent(cancel);
		layout.setComponentAlignment(cancel, Alignment.MIDDLE_RIGHT);
		
		setContent(layout);

	}

	public ProgressMonitorDialog(Window parent) {
		this.parent = parent;
		setModal(true);
		setWidth(440, UNITS_PIXELS);
		setHeight(200, UNITS_PIXELS);
		createContents();
	}

	public void run(boolean cancelable, final RunnableWithProgress runnable) {
		cancel.setEnabled(cancelable);
		setTaskName(Messages.getString("ProgressMonitorDialog_GENERIC_PROGRESS_LABEL")); //$NON-NLS-1$
		parent.addWindow(this);
		this.runnable = runnable;
		Thread t = new Thread() {
			public void run() {
				try {
					runnable.run(ProgressMonitorDialog.this);
				} catch (Exception e) {
					Activator.error("Job execution failed", e); //$NON-NLS-1$
					end();
				}
			}
		};
		
		t.start();
	}

	@Override
	public void beginTask(String name, int totalWork) {
		setTaskName(name);
		if(totalWork==IProgressMonitor.UNKNOWN)
		{
			indicator.setStyleName(JabylonStyle.PROGRESS_INDICATOR_INDETERMINATE.getCSSName());
			indicator.setIndeterminate(true);
		}
		this.totalWork = totalWork;

	}

	@Override
	public void done() {
		end();

	}

	@Override
	public void internalWorked(double work) {

	}

	@Override
	public boolean isCanceled() {
		return canceled;
	}

	@Override
	public void setCanceled(boolean value) {
		canceled = value;

	}

	@Override
	public void setTaskName(String name) {
		mainTask.setValue(name);

	}

	@Override
	public void subTask(String name) {
		subTask.setValue(name);

	}

	@Override
	public void worked(int work) {
		actualWork += work;
		indicator.setValue(actualWork / (double) totalWork);

	}

	@Override
	public void buttonClick(ClickEvent event) {
		setCanceled(true);
		end();

	}

	private void end() {
		parent.removeWindow(this);

	}

}
