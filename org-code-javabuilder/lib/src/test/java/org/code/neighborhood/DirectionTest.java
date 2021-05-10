package org.code.neighborhood;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class DirectionTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void constructorIgnoresCase() {
        Direction dir = Direction.fromString("NoRtH");
        assertEquals(dir, Direction.NORTH);
    }

    @Test
    void printsErrorMessageIfBadDirectionGiven() {
        Direction dir = Direction.fromString("not a direction");
        assertTrue(outputStreamCaptor.toString().trim().contains("bad direction"));
    }

    @Test
    void facesWestAfterTurningLeftFromNorth() {
        Direction dir = Direction.fromString("North");
        dir = dir.turnLeft();
        assertTrue(dir.isWest());
        assertTrue(outputStreamCaptor.toString().trim().contains("pointing west"));
    }

    @Test
    void facesNorthAfterTurningLeftFromEast() {
        Direction dir = Direction.fromString("East");
        dir = dir.turnLeft();
        assertTrue(dir.isNorth());
        assertTrue(outputStreamCaptor.toString().trim().contains("pointing north"));
    }
}