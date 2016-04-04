package DP_Disambiguation_DumpHandler;
import java.util.HashSet;


public class Anchor {
private HashSet<ID> referencedIDs = null;
private String name;

public String getName() {
	return name;
}

public Anchor(String newName) {
	super();
	this.name = newName;
}

public HashSet<ID> getAllReferencedIDs() {
	return referencedIDs;
}

public void addReferencedID(ID newID) {
	if (this.referencedIDs == null)
		this.referencedIDs = new HashSet<ID>();
	
	this.referencedIDs.add(newID);
}
}
