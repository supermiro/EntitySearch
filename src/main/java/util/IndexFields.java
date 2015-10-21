package util;

import java.util.HashSet;
import java.util.Set;

public class IndexFields {
    public static final String TITLE = "title";
    public static final String TITLE_EXACT = "title_exact";
    public static final String TEXT = "text";
    public static final String ABSTRACT = "abs";
    public static final String ALTERNATIVE_NAME = "alt";
    public static final String ALTERNATIVE_NAME_EXACT = "alt_exact";
    public static final String ANCHOR_TEXT = "anchor";
    public static final String WIKI_CATEGORY = "category";
    public static final String FREEBASE_CATEGORY = "fb_category";
    public static final String FREEBASE_ALIAS = "fb_alias";
    public static final String FREEBASE_ALIAS_EXACT = "fb_alias_exact";
    public static final String FREEBASE_NAME = "fb_name";
    public static final String FREEBASE_NAME_EXACT = "fb_name_exact";
    public static final String FREEBASE_ID = "fb_id";
    public static final String FREEBASE_ID_ERD = "fb_id_erd"; //experimental for ERD challenge
    public static final String DBPEDIA_CATEGORY = "db_category";
    public static final String WIKI_TEMLATE = "template";
    public static final String WIKI_LINK = "link";
    public static final String WIKI_SECTION = "section";
    public static final String PAGE_TYPE = "page_type"; //Wikipedia, Disambiguation ...
    public static final String ID = "id"; //used for Solr
    
    public static final Set<String> SINGLE_VALUE_FIELDS = new HashSet<String>(){{ 
        add(TEXT); add(ABSTRACT); add(TITLE); add(TITLE_EXACT); add(FREEBASE_ID); add(FREEBASE_ID_ERD); add(PAGE_TYPE); add(ID); 
    }};

    /** Wiki page type constant.*/
    public static final String PAGE_WIKI = "Wiki";
    /** Disambiguation page type. */
    public static final String PAGE_DISAMBIGUATION = "Disambiguation";

    

    /**
     *
     * @param title WikiPage title.
     * @param text WikiPage text
     * @return String page type.
     */
    
    public static String getPageType(final String title, final String text) {
        String type = PAGE_WIKI; //reset for each page.
        // Following values need to be covered:
        //{{Disambiguation}} {{disambiguation}}
        // {{Disambig}}, {{disambig}}
        //{{Disamig, other_template}}
        if (title.trim().endsWith("(disambiguation)")
                || text.contains("{{Disambig")
                || text.contains("{{disambig")) {
            type = PAGE_DISAMBIGUATION;
        }
        return type;
    }
    
    
    public static boolean hasDisambiguationCategory(String[] categories) {
        boolean hasDC = false;
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].trim().equals("Disambiguation pages")) {
                hasDC = true;
                break;
            }         
        }
        return hasDC;
    }
}
