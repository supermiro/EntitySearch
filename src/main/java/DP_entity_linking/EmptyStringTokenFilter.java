package DP_entity_linking;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import java.io.IOException;

/**
 * Created by miroslav.kudlac on 10/4/2015.
 */
public final class EmptyStringTokenFilter extends TokenFilter {
    public EmptyStringTokenFilter(TokenStream tokenStream) {
        super(tokenStream);
    }

    /* Like the PlusSignTokenizer class, we are going to save the text of the
     * current token in a CharTermAttribute object. In addition, we are going
     * to use a PositionIncrementAttribute object to store the position
     * increment of the token. Lucene uses this latter attribute to determine
     * the position of a token. Given a token stream with "This", "is", "",
     * ”some", and "text", we are going to ensure that "This" is saved at
     * position 1, "is" at position 2, "some" at position 3, and "text" at
     * position 4. Note that we have completely ignored the empty string at
     * what was position 3 in the original stream.
     */
    protected CharTermAttribute charTermAttribute = addAttribute(CharTermAttribute.class);
    protected PositionIncrementAttribute positionIncrementAttribute = addAttribute(PositionIncrementAttribute.class);


    @Override
    public final boolean incrementToken() throws IOException {
        String nextToken = null;
        while (nextToken == null) {
            if (!this.input.incrementToken()) {
                return false;
            }

            // Get text of the current token and remove anyleading/trailing whitespace.
            String currentTokenInStream = this.input.getAttribute(CharTermAttribute.class).toString().trim();

            // Save the token if it is not an empty string
            if (currentTokenInStream.length() > 0) {
                nextToken = currentTokenInStream;
            }
        }

        // Save the current token
        this.charTermAttribute.setEmpty().append(nextToken);
        this.positionIncrementAttribute.setPositionIncrement(1);
        return true;
    }
}
