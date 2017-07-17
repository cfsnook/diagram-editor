package ac.soton.eventb.diagrameditor;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eventb.emf.core.context.Context;
import org.eventb.emf.core.context.ContextFactory;
import org.eventb.emf.core.machine.Machine;
import org.eventb.emf.core.machine.MachineFactory;

import ac.soton.eventb.diagrameditor.features.EventBElementFeature;

public class ReNameComponentsFeature extends AbstractCustomFeature{
	EventBElementFeature feature;
	public ReNameComponentsFeature(IFeatureProvider fp) {
		super(fp);
		// TODO Auto-generated constructor stub
	}
	
	public String getName(){
		return "Custom: re-name";
	}
	
	public String getDescription(){
		return "Re-name the name of the current selected component";
	}
	
	public boolean canExecute(ICustomContext context){
		boolean exe = false;
		PictogramElement[] pElement = context.getPictogramElements();
		if(pElement != null && pElement.length == 1){
			Object ob = getBusinessObjectForPictogramElement(pElement[0]);
			if(ob instanceof Machine || ob instanceof Context){
				exe = true;
			}
		}
		System.out.println(exe);
		return exe;
	}
	
	@Override
	public void execute(ICustomContext context) {
		// TODO Auto-generated method stub
		PictogramElement[] pElement = context.getPictogramElements();
		if(pElement != null && pElement.length ==1){
			Object ob = getBusinessObjectForPictogramElement(pElement[0]);
			if(ob instanceof Machine){
				Machine m = MachineFactory.eINSTANCE.createMachine();
				m = (Machine) ob;
				String currentName = m.getName();
				String newName = nameOfEClass(getName(), getDescription(), currentName);
				if(newName != null && !newName.equals(currentName)){
					m.doSetName(newName);
					updatePictogramElement(pElement[0]);
					System.out.println("Hi");
				}
			}
			else if(ob instanceof Context){
				Context c = ContextFactory.eINSTANCE.createContext();
				c = (Context) ob;
				String currentName = c.getName();
				String newName = nameOfEClass(getName(), getDescription(), currentName);
				if(newName != null && !newName.equals(currentName)){
					c.doSetName(newName);
					updatePictogramElement(pElement[0]);
					System.out.println("Hi");
				}
			}
		}
		
	}
	
	public static String nameOfEClass(String title, String message, String initialValue) {
		String className = null;
		Shell shell = getShell();
		InputDialog inputDialog = new InputDialog(shell, title, message, initialValue, null);
		int classNameDialog = inputDialog.open();
		if (classNameDialog == Window.OK) {
			className = inputDialog.getValue();
		}
		return className;
	}

	private static Shell getShell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}
	

}
