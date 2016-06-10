package DP_Disambiguation_FeatureBuilding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import DP_Disambiguation_DumpHandler.ID;
import DP_Disambiguation_DumpHandler.Printer;

public class PageRankCalculator {

	
	public void performCalculation(Collection <ID> iDs, int numberOfIterations, double dumpingFactor)
	{
		double pivotPageRank = 0;
		double linkingPageRank = 0;
	
	for (int i = 0; i<numberOfIterations;i++)
	{
		Printer.print("Calculatig Page Ranks", "starting phase - " + (i+1));
		//I iterate through the entire list of entities
		for (ID iD:iDs)
		{
			// i take ones Page Rank
			
			pivotPageRank = (1-dumpingFactor)*(1/((double)iDs.size()));
			
			//i create a list of all entities it is pointing to
			Set <ID>linkingIDs = new HashSet (iD.getIngoingIDs());
			for (ID linkingID:linkingIDs)
			{
				linkingPageRank = linkingID.getPageRank();
				
				pivotPageRank += dumpingFactor*(linkingPageRank/ (double) linkingID.getNumberOfOutgoing());
			}
			iD.setPageRank(pivotPageRank);
		}
		Printer.print("Calculatig Page Ranks", "phase - " + (i+1) + " finished");
	}
	//and  add the value of pivot entity PageRank divided by number of outgoing links and multiply by the dumping factor
	}
	
	
	
}
