package DP_Disambiguation_DumpHandler;

public class Tuple<First, Second> { 
	  private final First first; 
	
	public First getFirst() {
		return first;
	}

	public Second getSecond() {
		return second;
	}

	private final Second second; 
	  
	  public Tuple(First first, Second second) { 
	    this.first = first; 
	    this.second = second; 
	  } 
	  
	} 