package DP_Disambiguation_DumpHandler;
import java.util.ArrayList;


public class Anchor {
private ArrayList<ID> referencedIDs;
private String name;

public String getName() {
	return name;
}

public Anchor(String newName) {
	super();
	this.referencedIDs = new ArrayList<ID>();
	this.name = newName;
}

public ArrayList<ID> getAllReferencedIDs() {
	return referencedIDs;
}

public void addReferencedID(ID newID) {
	this.referencedIDs.add(newID);
}
}
