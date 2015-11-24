package DP_Disambiguation_FeatureBuilding;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class Entity {
public String ID = "";
public int trafficRank = 0;
public HashMap<Date,Integer> traffic;	

public float pageRank = 1;

public int incomingRedirectPages = 0;
public int incomingUserTalkingPages = 0;
public int incomingTalkingPages = 0;
public int incomingTransclusions = 0;

public int userProfilesReffering = 0;
public int otherIncomingPages = 0;

public ArrayList<String> listOfCategories;
public ArrayList<String> listOfDBpediaTypes;
public ArrayList<String> listOfLinkedPages;
public ArrayList<String> listOfDisambiguations;
public ArrayList<String> listOfLinkingPages;
public ArrayList<String> listOfLinkingRedirects;
public ArrayList<String> listOfLinkingUserTalks;
public ArrayList<String> listOfLinkingUserProfiles;
public ArrayList<String> listOfLinkingTalks;
public ArrayList<String> listOfLinkingTransclusions;


public Entity (String ID)
{
	this.ID = ID;
}

String printCategories ()
{
	String categoriesString= " ";
	
	for (String currentCategory : this.listOfCategories)
	{
		categoriesString += currentCategory + " ";
	}
	
	return categoriesString;
}

}