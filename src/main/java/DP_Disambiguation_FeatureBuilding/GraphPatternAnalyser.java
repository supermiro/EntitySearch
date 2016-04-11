package DP_Disambiguation_FeatureBuilding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import DP_Disambiguation_DumpHandler.Anchor;
import DP_Disambiguation_DumpHandler.Dictionary;
import DP_Disambiguation_DumpHandler.ID;
import DP_Disambiguation_DumpHandler.Tuple;

public class GraphPatternAnalyser {

	
	public ArrayList<ID> getNumberOfOneHopTransferers (ID entity1, ID entity2)
	{
		ArrayList<ID> oneHops = new ArrayList<ID> ();
		
		//get number of outgoing links from entity1
		Set <ID> list1 = new HashSet(entity1.getOutgoings());
				
				//get number of outgoing links from entity2
		Set <ID> list2 = new HashSet(entity2.getOutgoings());
				
		//count overlappings between lists
				
		for (Object o : list1)
			{
			if (list2.contains(o))
				oneHops.add((ID) o);
			}

		
		return oneHops;
	}
	
	public int getNumberOfAdditionalDirectInterconnections (ID entity1, ID entity2)
	{
		int counter = 0;
	
		
		//get number of outgoingLinks from entity1
		ArrayList<ID> listTuples1 = entity1.getOutgoings();
				
		//get number of ingoingLinks to entity 2
		ArrayList<ID> listTuples2 = entity2.getIngoings();
		
		for (ID id : listTuples1)
		{
			if (id == entity2)
				counter++;
		}
		
		for (ID id : listTuples2)
		{
			if (id == entity1)
				counter++;
		}
									
		return counter;
	}
		
	
	public ArrayList<ID> getNumberOfListeners (ID entity1, ID entity2)
	{
		ArrayList<ID> listeners = new ArrayList<ID> ();
		
		//get number of outgoing links from entity1
		Set <ID> list1 = new HashSet(entity1.getOutgoings());
		
		//get number of outgoing links from entity2
		Set <ID> list2 = new HashSet(entity2.getOutgoings());
		
		//count overlappings between lists
		
		for (Object o : list1)
		{
			if (list2.contains(o))
				listeners.add((ID) o);
		}
		
		return listeners;
	}
	
	public ArrayList<ID>  getNumberOfSpokesmen (ID entity1, ID entity2)
	{

		ArrayList<ID> spokesmen = new ArrayList<ID> ();
		
		//get number of outgoing links from entity1
				Set <ID> list1 = new HashSet(entity1.getIngoings());
				
				//get number of outgoing links from entity2
				Set <ID> list2 = new HashSet(entity2.getIngoings());
		
		//count overlappings between lists
		
		for (Object o : list1)
		{
			if (list2.contains(o))
				spokesmen.add((ID) o);
		}
		
		return spokesmen;
	}
}
