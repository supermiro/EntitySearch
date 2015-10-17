package util;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

public class TestAnalysis {
	
	public static void shortKeywords(String queryFile) throws IOException {
		
		Set<String> oks = new HashSet<String>();
		oks.add("in");
		oks.add("on");
		oks.add("of");
		oks.add("to");
		oks.add("is");
		
		LineReader lr = new LineReader(queryFile);
		String line;
		int qCnt = 0;
		HashMap<String, Integer> mmap =new HashMap<String, Integer>();
		while ((line=lr.getLine())!=null) {
			String[] tokens1 = line.split("\t");
			String[] qTokens = tokens1[1].split(" ");
			boolean hasShort=false; 
			for (String t: qTokens) {
				if (t.length()<3 && !oks.contains(t)) {
					System.out.println(t+"\t"+line);
					Integer poc = mmap.get(t);
					if (poc==null) poc=new Integer(0);
					poc++;
					mmap.put(t, poc);
					if (!hasShort) {
						hasShort=true;
						qCnt++;
					}
				}
			}
		}
		
		System.err.println("#queries: "+qCnt);
		for (Entry<String, Integer> ent : mmap.entrySet()) {
			System.err.println(ent.getKey()+"\t"+ent.getValue());
		}
	}

	
	public static void main(String[] args) throws IOException {
		TestAnalysis.shortKeywords("/home/marek/Plocha/ERD/500queries.txt");
	}
}
