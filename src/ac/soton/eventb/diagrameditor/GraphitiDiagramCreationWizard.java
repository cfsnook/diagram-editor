package ac.soton.eventb.diagrameditor;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.part.FileEditorInput;

public class GraphitiDiagramCreationWizard extends Wizard implements INewWizard {

	private IWorkbench workbench;
	private IStructuredSelection selection;
	protected GraphitiDiagramCreationWizardPage diagramModelFilePage;
	protected GraphitiDiagramCreationWizardPage domainModelFilePage;
	protected Resource diagram;
	private String domainFileExtension;
	private String editorId;
	private boolean openNewlyCreatedDiagramEditor = true;

	public IWorkbench getWorkbench() {
		return workbench;
	}

	public IStructuredSelection getSelection() {
		return selection;
	}

	public final Resource getDiagram() {
		return diagram;
	}

	public final boolean isOpenNewlyCreatedDiagramEditor() {
		return openNewlyCreatedDiagramEditor;
	}

	public void setOpenNewlyCreatedDiagramEditor(
			boolean openNewlyCreatedDiagramEditor) {
		this.openNewlyCreatedDiagramEditor = openNewlyCreatedDiagramEditor;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
		this.workbench = workbench;
		this.selection = selection;
		setWindowTitle("Graphiti Diagram");
		setNeedsProgressMonitor(true);

	}

	@Override
	public void addPages() {
		diagramModelFilePage = new GraphitiDiagramCreationWizardPage(
				"DiagramModelFile", getSelection(), "diagram");
		diagramModelFilePage.setTitle("Graphiti Diagram Creation");
		addPage(diagramModelFilePage);

		domainModelFilePage = new GraphitiDiagramCreationWizardPage(
				"DomainModelFile", selection, domainFileExtension);
		domainModelFilePage.setTitle("Graphiti Domain Model");
		addPage(domainModelFilePage);
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		IRunnableWithProgress op = new WorkspaceModifyOperation(null) {
			@Override
			protected void execute(IProgressMonitor monitor)
					throws CoreException, InterruptedException {
				Resource diagramResource = GraphitiDiagramEditorUtil
						.createDiagram(diagramModelFilePage.getURI(),
								domainModelFilePage.getURI(), monitor);
				if (diagramResource != null && editorId != null) {
					try {
						openDiagram(diagramResource);
					} catch (PartInitException exception) {

					}
				}

			}
		};

		try {
			getContainer().run(false, true, op);
		} catch (InterruptedException exception) {
			return false;
		} catch (InvocationTargetException exception) {

		}
		return true;
	}

	private void openDiagram(final Resource diagramResource)
			throws PartInitException {
		String path = diagramResource.getURI().toPlatformString(true);
		IResource workspaceResource = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(path));
		if (workspaceResource instanceof IFile) {
			IWorkbenchPage page = workbench.getActiveWorkbenchWindow()
					.getActivePage();
			page.openEditor(new FileEditorInput((IFile) workspaceResource),
					editorId);
		}
	}
}
