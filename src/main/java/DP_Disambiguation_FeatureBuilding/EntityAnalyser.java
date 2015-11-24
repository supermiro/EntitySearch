package DP_Disambiguation_FeatureBuilding;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import DP_Disambiguation_DumpHandler.Anchor;
import DP_Disambiguation_DumpHandler.DBpediaType;
import DP_Disambiguation_DumpHandler.Dictionary;
import DP_Disambiguation_DumpHandler.ID;
import DP_Disambiguation_DumpHandler.Printer;
import DP_Disambiguation_DumpHandler.Tuple;

public class EntityAnalyser {
	
	private Dictionary anchorLinkDictionary;
	private GraphPatternAnalyser patternAnalyser;
	private PrintWriter writer;
	

	public EntityAnalyser(Dictionary anchorLinkDictionary) {
		super();
		this.anchorLinkDictionary = anchorLinkDictionary;
		this.patternAnalyser = new GraphPatternAnalyser ();
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
	
	private void buildVowpalInstance (ID entityCanonic, ID entityCandidate, boolean positive) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, ParseException
	{		
		String featureLine; //feature input format to of Vowpal Wabbit
		
		if (positive == true)
		{
			featureLine = "1 |a_CanonicCategory " + getCategoriesString(entityCanonic) + "|b_IngoingLinksToCanonic b1:"+ getNumberOfIngoing(entityCanonic) + " |c_OutgoingLinksFromCanonic c1:"+ getNumberOfOutgoing(entityCanonic) +  String.format(" |d_PageRankCanonic d1:%.6f",entityCanonic.getPageRank()).replace(",", ".") + " |e_DBpediaTypes " + getDBpediaTypesString(entityCanonic) + " |f_NumberOfListeners f1:" + getNumberOfListeners(entityCanonic,entityCandidate) +" |g_NumberOfSpokesmen g1:" + getNumberOfSpokesmen(entityCanonic,entityCandidate) + " |h_NumberOfOneHops h1:" + getNumberOfOneHopTransferers(entityCanonic,entityCandidate) + " |i_NumberOfAdditionalDirectConnections i1:" + getNumberOfAdditionalDirectInterconnections(entityCanonic,entityCandidate) + " |j_CandidateCategory "+ getCategoriesString(entityCandidate) + "|k_IngoingLinksToCandidate k1:"+ getNumberOfIngoing(entityCandidate) + " |l_OutgoingLinkFromCandidate l1:"+ getNumberOfOutgoing(entityCandidate) + String.format(" |m_PageRankCandidate m1:%.6f",entityCandidate.getPageRank()).replace(",", ".") + " |n_DBpediaTypes " + getDBpediaTypesString(entityCandidate);	
			writer.println(featureLine);
			//Printer.print("Positive instance created between " + entityCanonic.getName() + " and " + entityCandidate.getName(), featureLine);
		}
		else
		{
			featureLine = "-1 |a_CanonicCategory " + getCategoriesString(entityCanonic) + "|b_IngoingLinksToCanonic b1:"+ getNumberOfIngoing(entityCanonic) + " |c_OutgoingLinksFromCanonic c1:"+ getNumberOfOutgoing(entityCanonic) +  String.format(" |d_PageRankCanonic d1:%.6f",entityCanonic.getPageRank()).replace(",", ".") + " |e_DBpediaTypes " + getDBpediaTypesString(entityCanonic) + " |f_NumberOfListeners f1:" + getNumberOfListeners(entityCanonic,entityCandidate) +" |g_NumberOfSpokesmen g1:" + getNumberOfSpokesmen(entityCanonic,entityCandidate) + " |h_NumberOfOneHops h1:" + getNumberOfOneHopTransferers(entityCanonic,entityCandidate) + " |i_NumberOfAdditionalDirectConnections i1:" + getNumberOfAdditionalDirectInterconnections(entityCanonic,entityCandidate) + " |j_CandidateCategory "+ getCategoriesString(entityCandidate) + "|k_IngoingLinksToCandidate k1:"+ getNumberOfIngoing(entityCandidate) + " |l_OutgoingLinkFromCandidate l1:"+ getNumberOfOutgoing(entityCandidate) + String.format(" |m_PageRankCandidate m1:%.6f",entityCandidate.getPageRank()).replace(",", ".") + " |n_DBpediaTypes " + getDBpediaTypesString(entityCandidate);	
			writer.println(featureLine);
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
						for (Tuple <Anchor,ID> refferencedID: entity.getIngoingTuples())
								System.out.println("\t - " + refferencedID.getSecond().getName() + "\t\t\t throug anchor " + refferencedID.getFirst().getName() );
					}
				if (entity.getOutgoingTuples().size() != 0)
				{
					System.out.println("--Entities linked from here--");
					for (Tuple <Anchor,ID> refferencedID: entity.getOutgoingTuples())
							System.out.println("\t - " + refferencedID.getSecond().getName() + "\t\t\t throug anchor " + refferencedID.getFirst().getName() );
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
 		this.writer = new PrintWriter( trueFileName + ".txt", "UTF-8");
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
 		 			buildVowpalInstance (((ID)canonicElement), candidateEntity, true);
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
 		this.writer = new PrintWriter( falseFileName + ".txt", "UTF-8");
 		Printer.print("Started generating negative examples","");
		 		
 		//take each element of the ID list
 		//take each element it is pointing to (because each pointing to is also somewhere documented as pointing in, what we dont need for duplicities)
 		
 		for (Object canonicEntity : anchorLink.getIDs().values())
 		{
 			//list all of the anchors that the canonicID has
 			for (Tuple<Anchor,ID> tuple : ((ID)canonicEntity).getOutgoingTuples())
 			{
 				//we get anchor and the entire list of where this anchor links without the entity the list truly points to
 				ArrayList <ID> referencedIDs = tuple.getFirst().getAllReferencedIDs();
 				
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
 						
 						buildVowpalInstance (((ID)canonicEntity), candidateEntity, false);
 						
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
 	
}
