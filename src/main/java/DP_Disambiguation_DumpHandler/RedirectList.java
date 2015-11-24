package DP_Disambiguation_DumpHandler;

import java.util.HashMap;

public class RedirectList {
	private HashMap <String,ID> redirectList = null;

	public RedirectList() {
		super();
		this.redirectList = new HashMap <String,ID> ();
	}

	public HashMap<String, ID> getRedirectList() {
		return redirectList;
	}

	public void addRedirect(String redirect, ID iD) {
		this.redirectList.put(redirect, iD);
	}
	
	public String getRedirectName(String redirect) {
		if (redirectList.get(redirect) != null)
			return redirectList.get(redirect).getName();
		else
			return null;
	}

}
