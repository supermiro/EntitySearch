package DP_Disambiguation_DumpHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Categories {
	private HashSet<String> categoryList = null;

	public HashSet<String> getCategoryList() {
		return categoryList;
	}
	
	public void getCategory() {
		//categoryList.get(index);
	}
	
	public void addCategory(String categoryName) {
			categoryList.add(categoryName);
		//return categoryList.in.indexOf(category);
	}

	public Categories() {
		super();
		this.categoryList = new HashSet<String> (1000000);
	}
}
