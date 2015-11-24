package DP_Disambiguation_InputProcessing;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import DP_Disambiguation_FeatureBuilding.Entity;
import DP_Disambiguation_FeatureBuilding.FeatureBuilder;

public class CombinationModule {

	public CombinationModule (FeatureBuilder knowledgeBaseBuilder, ArrayList <String> canonicEntityIDs, ArrayList <String> candidateEntityIDs) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException, ParseException
	{
		//get all the entities involved
		ArrayList <Entity> canonicEntities = new ArrayList <Entity> ();
		ArrayList <Entity> candidateEntities = new ArrayList <Entity> ();
				
		for (String canonicEntityID : canonicEntityIDs) {
			canonicEntities.add(knowledgeBaseBuilder.createEntity(canonicEntityID));
		}
		
		for (String candidateEntityID : candidateEntityIDs) {
			candidateEntities.add(knowledgeBaseBuilder.createEntity(candidateEntityID));
		}
		
				
		//make list of vowpal instances based on the combination of them
		
		for (Entity canonicEntity : canonicEntities) 
		{
		
					for (Entity candidateEntity : candidateEntities) 
					{
						//knowledgeBaseBuilder.buildVowpalInstance(canonicEntity, candidateEntity,true);
					}
		}
	}
}
