/**
 * @author 2016112148 최서정
 */

package com.chloe;

import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CurrencyTest {

    ExchangeRate mock;
    Currency runner;

    @Before
    public void setUp() throws Exception {
        runner = new Currency(10000.0, "KRW");
        mock = createMock(ExchangeRate.class);
    }

    @Test
    public void testToEuros() {
        expect(mock.getRate("KRW", "EUR")).andReturn(0.00075);
        replay(mock);

        Currency euro = new Currency(7.5, "EUR");
        Currency toEuros = runner.toEuros(mock);
        assertEquals(toEuros, euro);
    }

    @Test
    public void testEquals() {
        Currency same = new Currency(10000.0, "KRW");
        Currency diff = new Currency(8000.0, "KRW");

        assertTrue(runner.equals(same));
        assertFalse(runner.equals(diff));
    }

    @Test
    public void testToString() {
        String str = runner.toString();

        assertEquals(str, "10000.0 KRW");
    }
}