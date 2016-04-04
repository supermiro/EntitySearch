package DP_Disambiguation_FeatureBuilding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import DP_Disambiguation_DumpHandler.Anchor;
import DP_Disambiguation_DumpHandler.DBpediaType;
import DP_Disambiguation_DumpHandler.Dictionary;
import DP_Disambiguation_DumpHandler.DumpHandler;
import DP_Disambiguation_DumpHandler.ID;
import DP_Disambiguation_DumpHandler.Printer;
import DP_Disambiguation_DumpHandler.RedirectList;
import DP_Disambiguation_DumpHandler.Tuple;
import DP_Disambiguation_xgBoost.xgBoostWrapper;

public class EntityAnalyser {
	
	private Dictionary anchorLinkDictionary;
	private RedirectList listOfRedirects;
	private GraphPatternAnalyser patternAnalyser;
	private PrintWriter writer;
	

	public EntityAnalyser(Dictionary anchorLinkDictionary, RedirectList listOfRedirects) {
		super();
		this.anchorLinkDictionary = anchorLinkDictionary;
		this.listOfRedirects = listOfRedirects;
		this.patternAnalyser = new GraphPatternAnalyser ();
	}
	
	public boolean createSets (String positivePath, String negativePath) throws FileNotFoundException
	{
		Scanner sPositive = new Scanner(new File("positivePath"));
		@SuppressWarnings("resource")
		Scanner sNegative = new Scanner(new File("negativePath"));
		
		int counter = 0;
		
		ArrayList<String> listPositive = new ArrayList<String>();
		ArrayList<String> listNegative = new ArrayList<String>();
		
		while (sPositive.hasNextLine()){
		    listPositive.add(sPositive.nextLine());
		    counter ++;
		}
		while (sNegative.hasNextLine()){
		    listNegative.add(sNegative.nextLine());
		    counter ++;
		}
		sPositive.close();
		return true;
	}
	
	public String getCategoriesString (ID entity)
	{
		String categoriesString= "";
		
		for (String currentCategory : entity.getCategories())
		{
			categoriesString += currentCategory + " ";
		}
		
		if (categoriesString.length() > 0)
			categoriesString = categoriesString.substring(0, categoriesString.length()-1);
		return categoriesString;
	}
	
	public String getDBpediaTypesString (ID entity)
	{
		String DBpediaTypeString= "";
		
		for (DBpediaType currentDBpediaType : entity.getDBpediaTypes())
		{
			DBpediaTypeString += currentDBpediaType.getType() + " ";
		}
		
		if (DBpediaTypeString.length() > 0)
			DBpediaTypeString = DBpediaTypeString.substring(0, DBpediaTypeString.length()-1);
		
		return DBpediaTypeString;
	}
	
	public String buildVowpalInstance (ID entityCanonic, ID entityCandidate, boolean positive) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, ParseException
	{		
		String featureLine; //feature input format to of Vowpal Wabbit
		
		if (positive == true)
		{
			featureLine = "1 |a_CanonicCategory " + getCategoriesString(entityCanonic) + "|b_IngoingLinksToCanonic b1:"+ getNumberOfIngoing(entityCanonic) + " |c_OutgoingLinksFromCanonic c1:"+ getNumberOfOutgoing(entityCanonic) +  String.format(" |d_PageRankCanonic d1:%.6f",entityCanonic.getPageRank()).replace(",", ".") + " |e_DBpediaTypes " + getDBpediaTypesString(entityCanonic) + " |f_NumberOfListeners f1:" + getNumberOfListeners(entityCanonic,entityCandidate) +" |g_NumberOfSpokesmen g1:" + getNumberOfSpokesmen(entityCanonic,entityCandidate) + " |h_NumberOfOneHops h1:" + getNumberOfOneHopTransferers(entityCanonic,entityCandidate) + " |i_NumberOfAdditionalDirectConnections i1:" + getNumberOfAdditionalDirectInterconnections(entityCanonic,entityCandidate) + " |j_CandidateCategory "+ getCategoriesString(entityCandidate) + "|k_IngoingLinksToCandidate k1:"+ getNumberOfIngoing(entityCandidate) + " |l_OutgoingLinkFromCandidate l1:"+ getNumberOfOutgoing(entityCandidate) + String.format(" |m_PageRankCandidate m1:%.6f",entityCandidate.getPageRank()).replace(",", ".") + " |n_DBpediaTypes " + getDBpediaTypesString(entityCandidate);	
			return featureLine;
			//Printer.print("Positive instance created between " + entityCanonic.getName() + " and " + entityCandidate.getName(), featureLine);
		}
		else
		{
			featureLine = "-1 |a_CanonicCategory " + getCategoriesString(entityCanonic) + "|b_IngoingLinksToCanonic b1:"+ getNumberOfIngoing(entityCanonic) + " |c_OutgoingLinksFromCanonic c1:"+ getNumberOfOutgoing(entityCanonic) +  String.format(" |d_PageRankCanonic d1:%.6f",entityCanonic.getPageRank()).replace(",", ".") + " |e_DBpediaTypes " + getDBpediaTypesString(entityCanonic) + " |f_NumberOfListeners f1:" + getNumberOfListeners(entityCanonic,entityCandidate) +" |g_NumberOfSpokesmen g1:" + getNumberOfSpokesmen(entityCanonic,entityCandidate) + " |h_NumberOfOneHops h1:" + getNumberOfOneHopTransferers(entityCanonic,entityCandidate) + " |i_NumberOfAdditionalDirectConnections i1:" + getNumberOfAdditionalDirectInterconnections(entityCanonic,entityCandidate) + " |j_CandidateCategory "+ getCategoriesString(entityCandidate) + "|k_IngoingLinksToCandidate k1:"+ getNumberOfIngoing(entityCandidate) + " |l_OutgoingLinkFromCandidate l1:"+ getNumberOfOutgoing(entityCandidate) + String.format(" |m_PageRankCandidate m1:%.6f",entityCandidate.getPageRank()).replace(",", ".") + " |n_DBpediaTypes " + getDBpediaTypesString(entityCandidate);	
			return featureLine;
			//Printer.print("Negative instance created between " + entityCanonic.getName() + " and " + entityCandidate.getName(), featureLine);
		}	
		
	}

	public int getNumberOfOutgoing (ID entity)
	{
		return entity.getNumberOfOutgoing();
	}
	
	public int getNumberOfIngoing (ID entity)
	{
		return entity.getNumberOfIngoing();	
	}
	
	public int getNumberOfListeners (ID entity1, ID entity2)
	{
		return patternAnalyser.getNumberOfListeners(entity1, entity2).size();
	}
	
	public int getNumberOfSpokesmen (ID entity1, ID entity2)
	{
		return patternAnalyser.getNumberOfSpokesmen(entity1, entity2).size();
	}
	
	public int getNumberOfOneHopTransferers (ID entity1, ID entity2)
	{
		return patternAnalyser.getNumberOfOneHopTransferers(entity1, entity2).size();
	}	

	public int getNumberOfAdditionalDirectInterconnections (ID entity1, ID entity2)
	{
		return patternAnalyser.getNumberOfAdditionalDirectInterconnections(entity1, entity2);
	}
	
	public void analyse (String entityID, String pairedEntityName)
	{
		HashMap <String,ID> list = null;
		ID entity = null;
		
		if ((list = anchorLinkDictionary.getIDs()) != null)
				list = anchorLinkDictionary.getIDs();
		if ((entity = list.get(entityID)) != null)
		{
			System.out.println ("------Data concerning entity - " + entity.getName() + " ------");
			System.out.println("Number of outgoing links - " + entity.getNumberOfOutgoing());
			System.out.println("Number of ingoing links - " + entity.getNumberOfIngoing());
			System.out.println("Page rank - " + entity.getPageRank());
			
			if (entity.getIngoingTuples().size() != 0)
			{	
				System.out.println("Connected entities - ");
				if (entity.getIngoingTuples().size() != 0)
					{
						System.out.println("--Entities linking here--");
						for (ID refferencedID: entity.getIngoingTuples().keySet())
							for (Anchor refferencedAnchor: entity.getIngoingTuples().get(refferencedID))
								System.out.println("\t - " + refferencedID.getName() + "\t\t\t throug anchor " + refferencedAnchor.getName() );
					}
				if (entity.getOutgoingTuples().size() != 0)
				{
					System.out.println("--Entities linked from here--");
					for (ID refferencedID: entity.getOutgoingTuples().keySet())
						for (Anchor refferencedAnchor: entity.getOutgoingTuples().get(refferencedID))
							System.out.println("\t - " + refferencedID.getName() + "\t\t\t throug anchor " + refferencedAnchor.getName() );
				}
				
			}
			
			
			if (entity.getCategories().size() != 0 )
			{	
				System.out.println("Categories - ");
						System.out.println("--Entity is part of categories--");
						for (String category: entity.getCategories())
								System.out.println("\t - " + category );
				
			}
			
			if (entity.getDBpediaTypes().size() != 0 )
			{	
				System.out.println("DBpedia Types - ");
						System.out.println("--Entity is of DBpedia types--");
						for (Object type: entity.getDBpediaTypes())
								System.out.println("\t - " + ((DBpediaType)type).getType());
				
			}
			ID pairedEntity = null;
			if ((pairedEntity = list.get(pairedEntityName)) != null)
			{
				ArrayList <ID> listeners = patternAnalyser.getNumberOfListeners(entity, pairedEntity);
				ArrayList <ID> spokesmen = patternAnalyser.getNumberOfSpokesmen(entity, pairedEntity);
				ArrayList <ID> oneHops = patternAnalyser.getNumberOfOneHopTransferers(entity, pairedEntity);
				
				if (listeners.size() > 0)
				{
					System.out.println("Listeners with paired entity -"  + pairedEntity);
					
					for (Object type: listeners)
							System.out.println("\t - " + ((ID)type).getName());
				}
				
				if (listeners.size() > 0)
				{
					System.out.println("Spokesmen with paired entity -"  + pairedEntity);
					for (Object type: spokesmen)
						System.out.println("\t - " + ((ID)type).getName());
				}
				
				if (listeners.size() > 0)
				{
					System.out.println("One Hops with paired entity -"  + pairedEntity);
					for (Object type: oneHops)
						System.out.println("\t - " + ((ID)type).getName());
				}
			}
			
			
		}
		
	}
	
 	public void generatePositive (Dictionary anchorLink, String trueFileName) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, ParseException
	{	
 		//create an list of visited nodes
 		HashSet <ID> visited = new HashSet<ID>();
 		this.writer = new PrintWriter( trueFileName, "UTF-8");
 		Printer.print("Started generating positive examples","");
		 		
 		//take each element of the ID list
 		//take each element it is pointing to (because each pointing to is also somewhere documented as pointing in, what we dont need for duplicities)
 		
 		for (Object canonicElement : anchorLink.getIDs().values())
 		{
 			visited.add(((ID)canonicElement));
 			//take each element it is pointing to (because each pointing to is also somewhere documented as pointing in, what we dont need for duplicities)
 			for (ID candidateEntity : ((ID)canonicElement).getOutgoingIDs())
 			{
 				//If the element is not listed in the ID list before, create an positive association (in case of double linking bothsided, we will otherwise have duplicities)
 		 		if (!visited.contains(candidateEntity))
 		 		{
 		 			writer.println(buildVowpalInstance (((ID)canonicElement), candidateEntity, true));
 		 		}
 			}
 		}
 		this.writer.close();
	}

 	public void generateNegative (Dictionary anchorLink, String falseFileName) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, ParseException
	{	
 		//create an list of visited nodes
 		//ArrayList <Tuple<ID,ID>> visited = new <ID>ArrayList();
 		HashMap<ID,HashSet <ID>> visited = new HashMap<ID,HashSet <ID>>();
 		Boolean skip = false;
 		this.writer = new PrintWriter( falseFileName, "UTF-8");
 		Printer.print("Started generating negative examples","");
		 		
 		//take each element of the ID list
 		//take each element it is pointing to (because each pointing to is also somewhere documented as pointing in, what we dont need for duplicities)
 		
 		for (Object canonicEntity : anchorLink.getIDs().values())
 		{
 			//list all of the anchors that the canonicID has
 			for (ID refferencedID : ((ID)canonicEntity).getOutgoingTuples().keySet())
 			for (Anchor refferencedAnchor : ((ID)canonicEntity).getOutgoingTuples().get(refferencedID))
 			{
 				//we get anchor and the entire list of where this anchor links without the entity the list truly points to
 				HashSet <ID> referencedIDs = refferencedAnchor.getAllReferencedIDs();
 				
 				if (referencedIDs != null)
 				for (ID candidateEntity : referencedIDs)
 				{
 					if (!((ID)canonicEntity).getOutgoingIDs().contains(candidateEntity))
 					{
 						skip = false;
 						
 						HashSet<ID> list = null;
 						if (visited.containsKey(candidateEntity))
 						{
 							list = visited.get(candidateEntity);
 						}
 						if (visited.containsKey(canonicEntity))
 						{
 							list = visited.get(canonicEntity);
 						}
 						
 						if (list != null)
 						{
 							if (list.contains(canonicEntity) || list.contains(candidateEntity) )
 								break;
 						}
	 					
 					//-----we get here only, if we are not breaked in before
 						
 						writer.println(buildVowpalInstance (((ID)canonicEntity), candidateEntity, false));
 						
 						if (list == null)
 							list = new HashSet<ID>();
 						
 						list.add(candidateEntity);
 						visited.put((ID)canonicEntity, list);
	 						
  					}
 				}
 			}
 		}
 		this.writer.close();
 		
	}
 	
 	public Tuple <ArrayList <Integer>,Tuple<ArrayList <String>,ArrayList <String>>> generateInstaces (String canonicID, ArrayList<ArrayList<String>> entityCandidates) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException, ParseException {
 		
 		
 		String instance,parts[],outline,ns,tokens[],ns_name,val = "",token = "";
		String key="", temp;
		ID entityCandidate,entityCanonic;
 		int target,index=0;
 		String te;
 		ArrayList <Integer> endIndexes = new ArrayList <Integer> ();
 		ArrayList <String> toDelete = new <String> ArrayList();
 		endIndexes.add(0);
 		//---create instance file for VW----- 		
 		PrintWriter vowpalPrinter = new PrintWriter( "src\\main\\resources\\data\\dataSets\\candidateListVW" + ".txt", "UTF-8");
 		//PrintWriter xgBoost = new PrintWriter( "src\\main\\resources\\data\\dataSets\\candidateListXG" + ".lsvm", "UTF-8");
 		Printer.print("Started generating candidate list for Vowpal Wabbit and xgBoost","");
		
 		entityCanonic = this.anchorLinkDictionary.getIDs().get(canonicID);
 		
 		if (entityCanonic == null)
 		{
 			//first we check if we dont handle a redirect
 			if (this.listOfRedirects.getRedirectName(canonicID) != null)
 				entityCanonic = this.anchorLinkDictionary.getIDs().get(this.listOfRedirects.getRedirectName(canonicID));
 		}
 		
 		if (entityCanonic == null || entityCandidates.size()==0)
 		{
 			System.out.println("Either Canonic entity is not known, or candidate list is empty");
 			vowpalPrinter.close();
 	 		//xgBoost.close();
 			return null;
 		}
 		
 		ArrayList<String> vowpal = new ArrayList <String> ();
 		ArrayList<String> xgBoost = new ArrayList <String> ();
 		
 		for (ArrayList<String> currentList : entityCandidates)
	 		{
		 		for (String candidateID : currentList)
		 		{
		 			candidateID = DumpHandler.stringNormalisation(candidateID);
		 			entityCandidate = this.anchorLinkDictionary.getIDs().get(candidateID);
		 			if (entityCandidate == null)
		 			{
		 				//first we check for redirect
		 				if (this.listOfRedirects.getRedirectName(candidateID) != null)
		 					entityCandidate = this.anchorLinkDictionary.getIDs().get(this.listOfRedirects.getRedirectName(canonicID));
		 			}
		 			
		 			if (entityCandidate == null)
			 		{	
		 				System.out.println("Unable to find entity: " + candidateID + " in the knowledge base\n-Skipping this one");
		 				toDelete.add(candidateID);
		 				continue;
		 			}
		 			
		 			instance = buildVowpalInstance (entityCanonic,entityCandidate, true);
		 			vowpalPrinter.println(instance);
		 			vowpal.add(instance); 			
		 			
		 			
		 		index++;            
		 		}
		 		
		 		if (!endIndexes.contains(index))
		 				endIndexes.add(index);
		 		
		 		for (String needsToBeDeleted:toDelete)
		 		{
		 			currentList.remove(needsToBeDeleted);
		 		}
		 		toDelete = new <String> ArrayList();
	 		}
 		
 		
 		 vowpalPrinter.close();
 		 //xgBoost.close();
 		 
 		
 		xgBoost = xgBoostWrapper.convertFromVWFormat(vowpal);
 		
 		Tuple <ArrayList<String>,ArrayList<String>>predictLists = new Tuple <ArrayList<String>,ArrayList<String>> (vowpal,xgBoost);
 		Tuple <ArrayList<Integer>,Tuple <ArrayList<String>,ArrayList<String>>> output = new Tuple<ArrayList<Integer>,Tuple <ArrayList<String>,ArrayList<String>>> (endIndexes,predictLists);
 		 
		return output;
 		
 		
 		
 		
 		
 	}
}
