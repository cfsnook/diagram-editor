package ac.soton.eventb.diagrameditor.relations;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eventb.emf.core.context.Context;
import org.eventb.emf.core.machine.Machine;
import org.eventb.emf.persistence.ProjectResource;

public class MachineSeesRelation implements EventBRelation {
	private Machine source;
	private Context target;
	
	public MachineSeesRelation(Machine machine, Context context) {
		this.source = machine;
		this.target = context;
		if(!source.getSees().contains(target)) {
			source.getSees().add(target);
		}
	}
	public MachineSeesRelation(String key, ProjectResource pr) {
		String[] keys = key.substring("sees:".length()).split("<!sees!>");
		this.source = (Machine) pr.getEObject(URI.createURI(keys[0], true).fragment());
		this.target = (Context) pr.getEObject(URI.createURI(keys[1], true).fragment());
	}
	public Machine getSource() {
		return this.source;
	}
	public Context getTarget() {
		return this.target;
	}
	@Override
	public boolean equals(Object obj) {
		return obj instanceof MachineSeesRelation && 
				((MachineSeesRelation)obj).source.equals(this.getSource()) &&
				((MachineSeesRelation)obj).target.equals(this.getTarget());
	}
	@Override
	public String getKey() {
		return "sees:" + 
			EcoreUtil.getURI(this.getSource()).toString() +
			"<!sees!>" +
			EcoreUtil.getURI(this.getTarget()).toString();
	}
	@Override
	public void delete() {
		this.getSource().getSees().remove(this.getTarget());
	}
}
