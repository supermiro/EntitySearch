package DP_Disambiguation_DumpHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ID {

	private double pageRank = 0;
	private String name = null;
	private HashSet <String> categories = null;
	private HashMap <ID,HashSet<Anchor>> outgoings = null;
	private HashMap <ID,HashSet<Anchor>> ingoings = null;
	private HashSet <DBpediaType> dBpediaTypes = null;
	

	
	
	public ID(String name) {
		super();
		this.name = name;
		this.categories = new HashSet <String> (10);
		this.outgoings = new HashMap <ID,HashSet<Anchor>> (10);
		this.ingoings = new HashMap <ID,HashSet<Anchor>> (10);
		this.dBpediaTypes = new HashSet <DBpediaType>(10);
	}

	public double getPageRank() {
		return pageRank;
	}

	public void setPageRank(double pageRank) {
		this.pageRank = pageRank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashSet<DBpediaType> getDBpediaTypes() {
		return dBpediaTypes;
	}

	public void addDBpediaType(DBpediaType dBpediaTypes) {
		this.dBpediaTypes.add(dBpediaTypes);
	}

	public HashSet<String> getCategories() {
		return categories;
	}

	public void addCategory(String categoryName) {
			this.categories.add(categoryName);
	}

	public HashMap<ID,HashSet<Anchor>> getOutgoingTuples() {
		return outgoings;
	}

	public HashMap<ID,HashSet<Anchor>>  getIngoingTuples() {
		return ingoings;
	}
	
	public void addOutgoing(Tuple <Anchor,ID> outgoingTuple) {
		/*
		for (Tuple <Anchor,ID> tuple : this.outgoings)
		{
			if (tuple.getFirst() == outgoingTuple.getFirst() && tuple.getSecond() == outgoingTuple.getSecond())
				return;
		}
		*/
		HashSet<Anchor> anchors = this.outgoings.get(outgoingTuple.getSecond());
		
		if (anchors == null)
			anchors = new HashSet<Anchor> ();
		
		anchors.add(outgoingTuple.getFirst());
		
		this.outgoings.put(outgoingTuple.getSecond(),anchors);
	}

	public void addIngoing(Tuple <Anchor,ID> ingoingTuple) {
		
		HashSet<Anchor> anchors = this.ingoings.get(ingoingTuple.getSecond());
		
		if (anchors == null)
			anchors = new HashSet<Anchor> ();
		
		anchors.add(ingoingTuple.getFirst());
		
		this.ingoings.put(ingoingTuple.getSecond(),anchors);
	}
	
	public int getNumberOfOutgoing(){
		return this.outgoings.size();
	}
	
	public int getNumberOfIngoing(){
		return this.ingoings.size();
	}
	
	public Set<ID> getIngoingIDs(){
		
		return this.ingoings.keySet();
	}
	
	public Set<ID> getOutgoingIDs(){
		
		return this.outgoings.keySet();
	}
	
}
