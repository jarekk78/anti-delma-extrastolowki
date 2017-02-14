package antidelma;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DotEmailGeneratorTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test2() {
		DotEmailGenerator gen = new DotEmailGenerator("ab");
		assertEquals( 2, gen.maxId() );
		assertTrue( gen.hasNext() );
		assertEquals( "ab", gen.next() );
		assertTrue( gen.hasNext() );
		assertEquals( "a.b", gen.next() );
	}

	@Test
	public void test3() {
		DotEmailGenerator gen = new DotEmailGenerator("abc");
		assertEquals( 4, gen.maxId() );
		assertTrue( gen.hasNext() );
		assertEquals( "abc", gen.next() );
		assertTrue( gen.hasNext() );
		assertEquals( "ab.c", gen.next() );
		assertTrue( gen.hasNext() );
		assertEquals( "a.bc", gen.next() );
		assertTrue( gen.hasNext() );
		assertEquals( "a.b.c", gen.next() );
	}
}
