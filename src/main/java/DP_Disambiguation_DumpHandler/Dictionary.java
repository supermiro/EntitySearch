package DP_Disambiguation_DumpHandler;
import java.util.HashMap;
import java.util.HashSet;


public class Dictionary {
private HashMap <String,Anchor> anchors;
private HashMap <String,ID> iDs;



public Dictionary() {
	super();
	this.anchors = new HashMap<String,Anchor>(5000000);
	this.iDs = new HashMap<String,ID>(1000000);
}
public HashMap getAnchors() 
	{
		return anchors;
	}
public HashMap<String, ID> getIDs() 
	{
		return iDs;
	}
public ID addLink (String linkID)
{
	
		ID iD = new ID(linkID);
		ID tempId = iDs.put(linkID,iD);
		
		if (tempId != null)
			iDs.put(linkID,tempId);
		
	return iD;
}

public Tuple<Anchor,ID> addAnchorLink (String anchorID, String linkID)
{
	ID iDObject = null;
	Anchor anchorObject = null;
	
		
	if ((iDObject = iDs.get(linkID)) != null)
	{
			if ((anchorObject = anchors.get(anchorID)) == null)
					anchorObject = new Anchor (anchorID);
			
			anchorObject.addReferencedID(iDObject);
			this.anchors.put(anchorID, anchorObject);
			
		return new Tuple<Anchor, ID> (anchorObject,iDObject);
	}
	
	return null;
}


}
