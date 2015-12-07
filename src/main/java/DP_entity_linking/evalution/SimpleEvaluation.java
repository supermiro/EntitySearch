package DP_entity_linking.evalution;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by miroslav.kudlac on 10/26/2015.
 */
public class SimpleEvaluation implements IEvaluation {
    private static Logger LOGGER = Logger.getLogger(SimpleEvaluation.class);
    private IndexSearcher indexSearcher;

    public SimpleEvaluation(IndexSearcher indexSearcher) {
        this.indexSearcher = indexSearcher;
    }

    private boolean contains(Document doc, String answer) {
        String title = doc.get("title");
        String fbName = doc.get("fb_name");
        String fbAlias = doc.get("fb_alias");
        return contains(title, answer) ||
                contains(fbName, answer) ||
                contains(fbAlias, answer);
    }

    private static boolean contains(String a, String b) {
        if (a == null) {
            return false;
        }
        return a.toLowerCase().contains(b.toLowerCase()) || b.toLowerCase().contains(a.toLowerCase());
    }

    /**
     *
     * @param hits
     * @param answer
     * @return
     * @throws IOException
     */
    public float getScore(ScoreDoc[] hits, String answer) throws IOException {
        List<Document> documentList = new ArrayList<>();
            for (int i = 0; i < hits.length; i++) {
                Document doc = indexSearcher.doc(hits[i].doc);
                documentList.add(doc);

            if (contains(doc, answer)) {
                return 1f / ( (float) i + 1f);
            }
        }

        return 0;
    }

}
