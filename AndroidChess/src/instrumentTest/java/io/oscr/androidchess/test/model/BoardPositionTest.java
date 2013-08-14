package io.oscr.androidchess.test.model;

import junit.framework.TestCase;

import io.oscr.androidchess.model.BoardPosition;

/**
 * Testing the BoardPosition class according to the specification and guarantees provided by it.
 *
 * Always using the Arrange-Act-Assert pattern
 */
public class BoardPositionTest extends TestCase {

    /*
     * Creating a valid position gives no error.
     */
    public void test_createNormalPosition() throws Exception {
        String position = "E2";
        BoardPosition e2 = new BoardPosition(position);
        assertNotNull(e2);
        assertTrue(e2.toString().equals("E2"));
    }

    public void test_nullArgumentGivesException() throws Exception {
        try {
            // Act
            new BoardPosition(null);

            // Assert
            fail("Move constructor should have thrown exception for null argument");
        } catch(NullPointerException npe){
            // Do nothing here

        } catch(Exception e){
            fail("Wrong type of exception thrown");
        }
    }

    public void test_toShortArgumentGivesException() throws Exception {
        try {
            // Arrange
            String position = "E";

            // Act
            new BoardPosition(position);

            // Assert
            fail("Move constructor should have thrown IllegalArgumentException");
        } catch(IllegalArgumentException iae){
            // Do nothing here

        } catch(Exception e){
            fail("Wrong type of exception thrown");
        }
    }

    public void test_toLongArgumentGivesException() throws Exception {
        try {
            // Arrange
            String position = "E33";

            // Act
            new BoardPosition(position);

            // Assert
            fail("Move constructor should have thrown exception for argument");
        } catch(IllegalArgumentException iae){
            // Do nothing here

        } catch(Exception e){
            fail("Wrong type of exception thrown");
        }
    }

    public void test_illegalRankArgumentGivesException() throws Exception {
        try {
            // Arrange
            String position ="E9";

            // Act
            new BoardPosition(position);

            // Assert
            fail("Move constructor should have thrown exception for argument");
        } catch(IndexOutOfBoundsException ioe){
            // Do nothing here

        } catch(Exception e){
            fail("Wrong type of exception thrown");
        }

    }

    public void test_toStringEqualsOriginalPosition() throws Exception {
        // Arrange
        String position = "E2";
        BoardPosition e2 = new BoardPosition(position);
        BoardPosition e2_copy = new BoardPosition(e2.toString());

        // Assert
        assertNotNull(e2);
        assertNotNull(e2_copy);
        assertEquals(e2, e2_copy);
    }

    public void test_samePositionEqualsIsTrue() throws Exception {
        // Arrange
        String position = "E2";
        BoardPosition e2 = new BoardPosition(position);
        BoardPosition e2_copy = new BoardPosition(position);

        // Assert
        assertNotNull(e2);
        assertNotNull(e2_copy);
        assertEquals(e2, e2_copy);
        assertFalse(e2 == e2_copy);
    }
}
