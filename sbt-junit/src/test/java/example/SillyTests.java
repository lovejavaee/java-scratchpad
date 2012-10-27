package example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class SillyTests {

  @Test
  public void onePlusOneShouldBeTwo() {
    assertEquals(2, 1 + 1);
  }

  @Test public void internedStringsShouldBeTheSame() {
    assertSame("foo", "foo");
  }
}
