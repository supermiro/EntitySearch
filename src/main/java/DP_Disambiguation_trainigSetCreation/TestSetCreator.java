package DP_Disambiguation_trainigSetCreation;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import DP_Disambiguation_FeatureBuilding.Entity;
import DP_Disambiguation_FeatureBuilding.FeatureBuilder;

public class TestSetCreator {

	//get list of Wikipedia elements
	
	FeatureBuilder wikipediaBuilder = new FeatureBuilder ();
	
	Entity entity = null;
	ArrayList<Entity> linkedEntities = new ArrayList<Entity>();
	ArrayList<Entity> linkingEntities = new ArrayList<Entity>();
	ArrayList<Entity> disambiguationEntities = new ArrayList<Entity>();
		
	public void createInstance (String entityID) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException, ParseException
	{
	}
	
	public void createPositive (Entity entity) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException, ParseException
	{
		Entity tempEntity = null;
		//get all elements linking to
		for (String linkedPageID : entity.listOfLinkedPages)
		{
			try {
			tempEntity = wikipediaBuilder.createEntity(linkedPageID);
			}
			catch (NullPointerException e) // In case page is not yet deployed
			{
				continue;
			}
			linkedEntities.add(tempEntity);
		}
		
		//combine them with existing entity a create positive examples
		for (Entity linkedEntity : linkedEntities)	
		{
			//wikipediaBuilder.buildVowpalInstance(entity, linkedEntity,true);
		}
		
	
	}
	public void createNegative (Entity entity) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException, ParseException
	{
		Entity tempEntity = null;
		//get all elements linking to
		for (String linkedPageID : entity.listOfLinkedPages)
		{
			try {
			tempEntity = wikipediaBuilder.createEntity(linkedPageID);
			}
			catch (NullPointerException e) // In case page is not yet deployed
			{
				continue;
			}
			linkedEntities.add(tempEntity);
		}
		
		for (String disambiguationPageID : entity.listOfDisambiguations)
		{
			try {
			tempEntity = wikipediaBuilder.createEntity(disambiguationPageID);
			}
			catch (NullPointerException e) // In case page is not yet deployed
			{
				continue;
			}
			disambiguationEntities.add(tempEntity);
		}
		
		
		//combine them with existing entity a create positive examples
		for (Entity disambiguationEntity : disambiguationEntities)	
		{
			for (Entity linkedEntity : linkedEntities)	
			{
				//if (linkedEntity.listOfLinkedPages != null && linkedEntity.listOfLinkingPages != null && linkedEntity.listOfLinkedPages.contains(disambiguationEntity.ID) && linkedEntity.listOfLinkingPages.contains(disambiguationEntity.ID))
					//wikipediaBuilder.buildVowpalInstance(disambiguationEntity, linkedEntity, false);
			}
		}
		
	}
	
}
