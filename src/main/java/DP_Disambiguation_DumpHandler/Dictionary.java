package DP_Disambiguation_DumpHandler;
import java.util.HashMap;
import java.util.HashSet;


public class Dictionary {
private HashMap <String,Anchor> anchors;
private HashMap <String,ID> iDs;



public Dictionary() {
	super();
	this.anchors = new HashMap<String,Anchor>();
	this.iDs = new HashMap<String,ID>();
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
	ID iD = null;
	if (!iDs.containsKey(linkID) )
	{
		iD = new ID(linkID);
		iDs.put(linkID,iD);
	}
	else
		iD = iDs.get(linkID);

	
	return iD;
}

public Tuple<Anchor,ID> addAnchorLink (String anchorID, String linkID)
{
	ID iDObject = null;
	Anchor anchorObject = null;
	
	//we take this link as relevant only if we account given entity ID
	if (iDs.containsKey(linkID))
	{
		iDObject = iDs.get(linkID);
	}
	else
		return null;
	
	//check whether we already have this ANCHOR...if not we build it
	if (anchors.containsKey(anchorID))
	{
		anchorObject = anchors.get(anchorID);
	}
	else
	{
		anchorObject = new Anchor (anchorID);
		this.anchors.put(anchorID, anchorObject);
	}
	
	if (! anchorObject.getAllReferencedIDs().contains(iDObject))
	{
		anchorObject.addReferencedID(iDObject);
	}
	
	return new Tuple<Anchor, ID> (anchorObject,iDObject);
}


}
