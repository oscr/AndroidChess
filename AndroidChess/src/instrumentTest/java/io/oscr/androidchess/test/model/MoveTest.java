package io.oscr.androidchess.test.model;

import junit.framework.TestCase;

import io.oscr.androidchess.model.BoardPosition;
import io.oscr.androidchess.model.Move;

/**
 * Testing the Move class according to the specification and guarantees provided by the Move class.
 *
 * Always using the Arrange-Act-Assert pattern
 */
public class MoveTest extends TestCase {

    public void test_normalConstructionContainsSameValuesAsArgument() {
        // Arrange
        BoardPosition e2 = new BoardPosition("E2");
        BoardPosition e3 = new BoardPosition("E3");

        // Act
        Move move = new Move(e2, e3);

        // Assert
        assertNotNull(move);
        assertTrue(e2.equals(move.from));
        assertTrue(e3.equals(move.to));
    }

    public void test_constructWithFromNullArgument(){
        // Arrange
        BoardPosition e2 = null;
        BoardPosition e3 = new BoardPosition("E3");

        try {
            // Act
            new Move(e2, e3);

            // Assert
            fail("Move constructor should have thrown exception for null argument");

        } catch(NullPointerException npe){
            // Do nothing here
        }
    }

    public void test_constructWithToNullArgument(){
        // Arrange
        BoardPosition e2 = new BoardPosition("E2");
        BoardPosition e3 = null;

        try {
            // Act
            new Move(e2, e3);

            // Assert
            fail("Move constructor should have thrown exception for null argument");

        } catch(NullPointerException npe){
            // Do nothing here
        }
    }
}