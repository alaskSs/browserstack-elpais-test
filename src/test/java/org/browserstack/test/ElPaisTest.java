package org.browserstack.test;


import org.browserstack.implementation.ElPaisImplementation;
import org.junit.Test;

public class ElPaisTest extends ElPaisImplementation {

    @Test
    public void test() {
        openElPaisWebPage();
        verifyThatPageTextIsSpanish();
        navigateToOpinionSection();
        fetchAndSaveArticles();
        translateArticleHeaders();
        analyzeTranslatedHeaders();
        closeElPaisWebPage();
    }

}
