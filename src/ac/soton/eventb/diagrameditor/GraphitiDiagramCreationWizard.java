package ac.soton.eventb.diagrameditor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;


public class GraphitiDiagramCreationWizard extends Wizard implements INewWizard{
	
	private IWorkbench workbench;
	private IStructuredSelection selection;
	protected GraphitiDiagramCreationWizardPage diagramModelFilePage;
	protected Resource diagram;
	private boolean openNewlyCreatedDiagramEditor = true;
	
	public IWorkbench getWorkbench(){
		return workbench;
	}
	
	public IStructuredSelection getSelection() {
		return selection;
	}
	
	public final Resource getDiagram(){
		return diagram;
	}
	
	public final boolean isOpenNewlyCreatedDiagramEditor(){
		return openNewlyCreatedDiagramEditor;
	}
	
	public void setOpenNewlyCreatedDiagramEditor(boolean openNewlyCreatedDiagramEditor){
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

	public void addPages(){
		diagramModelFilePage = new GraphitiDiagramCreationWizardPage("DiagramModelFile", getSelection(), "diagram");
		diagramModelFilePage.setTitle("Graphiti Diagram Creation");
		addPage(diagramModelFilePage);
	}
	
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		IRunnableWithProgress op = new WorkspaceModifyOperation(null){
			protected void execute(IProgressMonitor monitor) throws CoreException, InterruptedException{
			}
		};
		
		return false;
	}

}
