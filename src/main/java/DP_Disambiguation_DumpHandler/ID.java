package DP_Disambiguation_DumpHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ID {

	private double pageRank = 0;
	private String name = null;
	private ArrayList <String> categories = null;
	private HashSet <Tuple<Anchor,ID>> outgoings = null;
	private HashSet <Tuple<Anchor,ID>> ingoings = null;
	private ArrayList <DBpediaType> dBpediaTypes = null;
	

	
	
	public ID(String name) {
		super();
		this.name = name;
		this.categories = new ArrayList <String> ();
		this.outgoings = new HashSet <Tuple<Anchor,ID>>();
		this.ingoings = new HashSet <Tuple<Anchor,ID>>();
		this.dBpediaTypes = new ArrayList <DBpediaType>();
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

	public ArrayList<DBpediaType> getDBpediaTypes() {
		return dBpediaTypes;
	}

	public void addDBpediaType(DBpediaType dBpediaTypes) {
		this.dBpediaTypes.add(dBpediaTypes);
	}

	public ArrayList<String> getCategories() {
		return categories;
	}

	public void addCategory(String category) {
		this.categories.add(category);
	}

	public HashSet<Tuple<Anchor,ID>> getOutgoingTuples() {
		return outgoings;
	}

	public HashSet<Tuple<Anchor,ID>> getIngoingTuples() {
		return ingoings;
	}
	
	public void addOutgoing(Tuple <Anchor,ID> outgoingTuple) {
		
		for (Tuple <Anchor,ID> tuple : this.outgoings)
		{
			if (tuple.getFirst() == outgoingTuple.getFirst() && tuple.getSecond() == outgoingTuple.getSecond())
				return;
		}
		
		this.outgoings.add(outgoingTuple);
	}

	public void addIngoing(Tuple <Anchor,ID> ingoingTuple) {
		
		for (Tuple <Anchor,ID> tuple : this.ingoings)
		{
			if (tuple.getFirst() == ingoingTuple.getFirst() && tuple.getSecond() == ingoingTuple.getSecond())
				return;
		}
		
		this.ingoings.add(ingoingTuple);
	}
	
	public int getNumberOfOutgoing(){
		return this.outgoings.size();
	}
	
	public int getNumberOfIngoing(){
		return this.ingoings.size();
	}
	
	public HashSet<ID> getIngoingIDs(){

		HashSet<ID> uniqueIngoingIDs = new HashSet<ID>();
		
		for (Tuple <Anchor,ID> tuple : this.ingoings)
		{
			uniqueIngoingIDs.add(tuple.getSecond());
		}
		
		return uniqueIngoingIDs;
	}
	
	public HashSet<ID> getOutgoingIDs(){
		
		HashSet<ID> uniqueOutgoingIDs = new HashSet<ID>();
		
		for (Tuple <Anchor,ID> tuple : this.outgoings)
		{
			uniqueOutgoingIDs.add(tuple.getSecond());
		}
		
		return uniqueOutgoingIDs;
	}
	
}
