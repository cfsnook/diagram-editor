package ac.soton.eventb.diagrameditor.relations;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eventb.emf.core.machine.Machine;
import org.eventb.emf.persistence.ProjectResource;

public class MachineRefinesRelation implements EventBRelation {
	Machine source;
	Machine target;

	// source refines target

	public MachineRefinesRelation(Machine source, Machine target) {
		this.source = source;
		this.target = target;
		if (!source.getRefines().contains(target)) {
			source.getRefines().add(target);
		}
	}

	public MachineRefinesRelation(String key, ProjectResource pr) {
		final String[] keys = key.substring("refines:".length()).split( //$NON-NLS-1$
				"<!refines!>"); //$NON-NLS-1$
		this.source = (Machine) pr.getEObject(URI.createURI(keys[0], true)
				.fragment());
		this.target = (Machine) pr.getEObject(URI.createURI(keys[1], true)
				.fragment());
	}

	@Override
	public void delete() {
		this.getSource().getRefines().remove(this.getTarget());
	}

	@Override
	public String getKey() {
		return "refines:" + EcoreUtil.getURI(this.getSource()).toString() //$NON-NLS-1$
				+ "<!refines!>" + EcoreUtil.getURI(this.getTarget()).toString(); //$NON-NLS-1$
	}

	public Machine getSource() {
		return this.source;
	}

	public Machine getTarget() {
		return this.target;
	}
}