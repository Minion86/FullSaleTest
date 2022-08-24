import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
  public static void main(String[] args) {
    AtomicInteger accesses = new AtomicInteger();
    Map<String, Integer> store = new HashMap<>() {
      {
        put("Tiger", 63);
        put("Aardvark", 19);
        put("Elephant", 32);
        put("Rhinoceros", 99);
        put("Dog", 3);
        put("Giraffe", 65);
        put("Zebra", 45);
        put("Crocodile", 23);
        put("Alligator", 75);
        put("Chimpanzee", 15);
      }

      @Override
      public Integer get(Object key) {
        accesses.incrementAndGet();
        return super.get(key);
      }

      @Override
      public boolean containsKey(Object key) {
        accesses.incrementAndGet();
        return super.containsKey(key);
      }
    };

    // should evict FIFO
    Cache<String, Integer> cache1 = new Cache<>(3, store);
    check(cache1.fetch("Elephant") == 32, "Elephant should be 32");
    check(cache1.size() == 1, "size should be 1, not " + cache1.size());
    check(cache1.fetch("Dog") == 3, "Dog should be 3");
    check(cache1.size() == 2, "size should be 2, not " + cache1.size());
    check(cache1.fetch("Dog") == 3, "Dog should be 3");
    check(cache1.size() == 2, "size should be 2, not " + cache1.size());
    check(cache1.fetch("Elephant") == 32, "Elephant should be 32");
    check(cache1.size() == 2, "size should be 2, not " + cache1.size());
    check(cache1.fetch("Aardvark") == 19, "Aardvark should be 19");
   
    check(cache1.size() == 3, "size should be 3, not " + cache1.size());
    check(cache1.fetch("Tiger") == 63, "Tiger should be 63");
    check(cache1.size() == 3, "size should be 3, not " + cache1.size());
    check(!cache1.containsKey("Dog"), "cache should no longer contain Dog");
    check(cache1.containsKey("Elephant"), "cache should contain Elephant");
    check(cache1.containsKey("Aardvark"), "cache should contain Aardvark");
    check(cache1.containsKey("Tiger"), "cache should contain Tiger");
      System.out.println("times "+accesses.intValue());
    check(accesses.intValue() == 4, "store should have been queried 4 times");
    System.out.println("First milestone PASSED!");

    // should evict oldest accessed
    accesses.set(0);
    Cache<String, Integer> cache2 = new Cache<>(5, store);
    check(cache2.fetch("Rhinoceros") == 99, "Rhinoceros should be 99");
    check(cache2.fetch("Zebra") == 45, "Zebra should be 45");
    check(cache2.fetch("Chimpanzee") == 15, "Chimpanzee should be 15");
    check(cache2.fetch(null) == null, "null should be null");
    check(cache2.fetch("Antelope") == null, "Antelope should be null");
    check(cache2.fetch("Antelope") == null, "Antelope should be null");
    check(cache2.fetch("Antelope") == null, "Antelope should be null");
    check(cache2.fetch("Rhinoceros") == 99, "Rhinoceros should be 99");
    check(cache2.fetch("Crocodile") == 23, "Crocodile should be 23");
    check(cache2.size() == 5, "size should be 5, not " + cache2.size());
    check(cache2.containsKey("Chimpanzee"), "cache should contain Chimpanzee");
    check(cache2.containsKey(null), "cache should contain null");
    check(cache2.containsKey("Antelope"), "cache should contain Aardvark");
    check(cache2.containsKey("Rhinoceros"), "cache should contain Rhinoceros");
    check(cache2.containsKey("Crocodile"), "cache should contain Tiger");
    check(accesses.intValue() == 6, "store should have been queried 6 times, not " + accesses.intValue());
    System.out.println("Second milestone PASSED!");
  }

  private static void check(boolean b, String message) {
    if (!b) {
      throw new AssertionError(message);
    }
  }
}
