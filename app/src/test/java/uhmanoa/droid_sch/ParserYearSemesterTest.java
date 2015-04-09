package uhmanoa.droid_sch;

import junit.framework.TestCase;

public class ParserYearSemesterTest extends TestCase {

    final int FALL = 0;
    final int SPRING = 1;
    final int SUMMER = 2;

    /*
        THESE TESTS HAVE TO BE UPDATED AS THE WEBSITE CHANGES!
     */

    public void testOldYears() throws Exception {
        ParserDataCheck prs = new ParserDataCheck();
        boolean check = prs.yearDataReadable(2011, FALL);
        assertEquals(false, check);
        check = prs.yearDataReadable(2012, FALL);
        assertEquals(false, check);
        check = prs.yearDataReadable(2013, FALL);
        assertEquals(false, check);
        check = prs.yearDataReadable(2014, FALL);
        assertEquals(false, check);

        check = prs.yearDataReadable(2011, SPRING);
        assertEquals(false, check);
        check = prs.yearDataReadable(2012, SPRING);
        assertEquals(false, check);
        check = prs.yearDataReadable(2013, SPRING);
        assertEquals(false, check);
        check = prs.yearDataReadable(2014, SPRING);
        assertEquals(false, check);

        check = prs.yearDataReadable(2011, SUMMER);
        assertEquals(false, check);
        check = prs.yearDataReadable(2012, SUMMER);
        assertEquals(false, check);
        check = prs.yearDataReadable(2013, SUMMER);
        assertEquals(false, check);
        check = prs.yearDataReadable(2014, SUMMER);
        assertEquals(false, check);
    }

    public void testCurrentYears() throws Exception {
        ParserDataCheck prs = new ParserDataCheck();
        boolean check = prs.yearDataReadable(2015, FALL);
        assertEquals(true, check);
        check = prs.yearDataReadable(2015, SPRING);
        assertEquals(true, check);
        check = prs.yearDataReadable(2013, SUMMER);
        assertEquals(true, check);
    }
}