package DP_Disambiguation_DumpHandler;

import java.util.HashMap;

public class DBpediaTypes {
	
	private HashMap<String, DBpediaType> dBpediaTypes = new HashMap<String, DBpediaType> ();
	
	public DBpediaTypes() {
		super();
		this.dBpediaTypes = new HashMap<String, DBpediaType> ();
	}

	public HashMap<String, DBpediaType> getdBpediaTypes() {
		return dBpediaTypes;
	}

	public void setdBpediaTypes(HashMap<String, DBpediaType> dBpediaTypes) {
		this.dBpediaTypes = dBpediaTypes;
	}

}
