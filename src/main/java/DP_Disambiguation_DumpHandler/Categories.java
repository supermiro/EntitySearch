package DP_Disambiguation_DumpHandler;

import java.util.ArrayList;

public class Categories {
	private ArrayList <String> categoryList = null;

	public ArrayList<String> getCategoryList() {
		return categoryList;
	}
	
	public String getCategory(int index) {
		return categoryList.get(index);
	}
	
	public int addCategory(String category) {
		if (!categoryList.contains(category))
		{
			categoryList.add(category);
			return (categoryList.size()-1);
		}
		return categoryList.indexOf(category);
	}

	public Categories() {
		super();
		this.categoryList = new ArrayList<String> ();
	}
}
