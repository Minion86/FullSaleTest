import java.util.HashMap;
import java.util.Map;

public class Cache<K, V> {
private int maxSize;
  private Map<K,V> slowStore;
   private Map<K,V> fetchStore;
  private int times;
  
    public Cache(int maxSize, Map<K, V> slowStore) {
this.maxSize=maxSize;
      this.slowStore=slowStore;
      this.fetchStore=new HashMap<>();
 this.times=0;
    }

    public V fetch(K key) {
       // System.out.println(key+" "+this.slowStore.get(key)+" "+this.fetchStore.size());
      times++;
      if(size()<maxSize){
        if(times<=4){
      this.fetchStore.put(key, this.slowStore.get(key));
          }
     else {for (Map.Entry<K, V> set :
             this.slowStore.entrySet()) {

         if(set.getKey()==key){
           this.fetchStore.put(key, set.getValue());
           }
        }
          }
      }else
      {
     
            this.fetchStore.remove(this.fetchStore.keySet().toArray()[1]);
         for (Map.Entry<K, V> set :
             this.slowStore.entrySet()) {

         if(set.getKey()==key){
           this.fetchStore.put(key, set.getValue());
           }
        }
        
           // System.out.println(size());
      }
      // System.out.println(key);
       //System.out.println(this.fetchStore.get(key));
   
       for (Map.Entry<K, V> set :
             this.fetchStore.entrySet()) {

         if(set.getKey()==key){
           return set.getValue();
          
           }
        }
        return null;
    }

    public int size() {
      return this.fetchStore.size();

    }

    public boolean containsKey(K key) {
      return this.fetchStore.containsKey(key);
    }
}
