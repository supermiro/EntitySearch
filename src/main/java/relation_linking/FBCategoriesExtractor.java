package relation_linking;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.apache.lucene.index.*;
import org.apache.lucene.util.BytesRef;

import DP_entity_linking.TrainJsonRead;

public class FBCategoriesExtractor {

	ArrayList<String> fbCategories = new ArrayList<String>();

	public FBCategoriesExtractor(TrainJsonRead tjr) throws IOException, ClassNotFoundException {

		File fbStore = new File("FBStore");
		if (!fbStore.exists() || fbStore.isDirectory()) {
			IndexReader indexReader = tjr.getIndexReader();
			Fields fields = MultiFields.getFields(indexReader);
			Terms terms = fields.terms("fb_category");
			TermsEnum iterator = terms.iterator(null);
			BytesRef byteRef = null;

			while ((byteRef = iterator.next()) != null) {
				String term = new String(byteRef.bytes, byteRef.offset, byteRef.length);
				term = term.substring(term.lastIndexOf(".") + 1, term.length());
				fbCategories.add(term);
			}

			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("FBStore"));
			oos.writeObject(fbCategories);
			oos.flush();
			oos.close();
		} else {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("FBStore"));
			fbCategories = (ArrayList<String>) ois.readObject();
			ois.close();
		}
	}

	public ArrayList<String> getCategories() {
		return this.fbCategories;
	}

}
