package DP_Disambiguation_DumpHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ID {

	private double pageRank = 0;
	private String name = null;
	private HashSet <String> categories = null;
	private HashSet <Anchor> anchors = null;
	private ArrayList <ID> outgoings = null;
	private ArrayList <ID> ingoings = null;
	private HashSet <DBpediaType> dBpediaTypes = null;
	

	
	
	public ID(String name) {
		super();
		this.name = name;
		this.categories = new HashSet <String> (10);
		this.anchors = new HashSet <Anchor>();
		this.outgoings = new ArrayList <ID> (10);
		this.ingoings = new ArrayList <ID> (10);
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

	public int getNumberOfOutgoingAnchors() {
		
		return outgoings.size();
	}

	public int getNumberOfIngoingAnchors() {
		return ingoings.size();
	}
	
	public void addOutgoing(Tuple <Anchor,ID> outgoingTuple) {
		
		this.outgoings.add(outgoingTuple.getSecond());
		this.anchors.add(outgoingTuple.getFirst());
	}

	public void addIngoing(Tuple <Anchor,ID> ingoingTuple) {
		this.ingoings.add(ingoingTuple.getSecond());
		
	}
	
	public int getNumberOfOutgoing(){
		return new HashSet(this.outgoings).size();
	}
	
	public int getNumberOfIngoing(){
		return new HashSet(this.ingoings).size();
	}
	
	public ArrayList <ID> getIngoingIDs(){
		
		return this.ingoings;
	}
	
	public ArrayList <ID> getOutgoingIDs(){
		
		return this.outgoings;
	}
	
	public HashSet<Anchor> getAnchors(){
		
		return this.anchors;
	}
	
	public ArrayList<ID> getIngoings(){
		
		return this.ingoings;
	}
	
	public ArrayList<ID> getOutgoings(){
		
		return this.outgoings;
	}
	
}
