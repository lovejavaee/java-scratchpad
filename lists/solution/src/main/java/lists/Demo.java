package lists;

import static lists.Lists.*;

public class Demo {

  public static void main(String[] args) {
    System.out.println(show(take(30, new Lists().ints)));
  }

}
