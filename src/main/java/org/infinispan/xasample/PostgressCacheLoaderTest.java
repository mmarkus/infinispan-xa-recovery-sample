package org.infinispan.xasample;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * // TODO: Document this
 *
 * @author Mircea Markus
 * @since 5.0
 */
public class PostgressCacheLoaderTest {

   public static void main(String[] args) throws Exception {

//      testConnect();

      EmbeddedCacheManager ecm = new DefaultCacheManager("/Users/mmarkus/github/infinispan-xa-recovery-sample/src/main/resources/config.xml");
      Cache<Object,Object> c = ecm.getCache();

      int KEY_COUNT = 10000;
      long start = System.currentTimeMillis();
      for (int i = 0; i < KEY_COUNT; i++) {
         String s = key(i);
         c.put(s, s);
      }
      long duration  = System.currentTimeMillis() - start;

      System.out.println("duration(millis) for " + KEY_COUNT + " writes = " + duration);

      c.getAdvancedCache().getDataContainer().clear();

      Set<String> result = new HashSet<String>();
      start = System.currentTimeMillis();
      for (int i = 0; i < KEY_COUNT; i++) {
         result.add((String) c.get(key(i)));
      }
      duration  = System.currentTimeMillis() - start;

      System.out.println("duration(millis) for " + KEY_COUNT + " reads = " + duration);

      if (result.size() != KEY_COUNT) throw new RuntimeException("Huh?!!");

      start = System.currentTimeMillis();
      for (int i = 0; i < KEY_COUNT; i++) {
         c.remove(key(i));
      }
      duration  = System.currentTimeMillis() - start;

      System.out.println("duration(millis) for " + KEY_COUNT + " removes = " + duration);

   }

   private static String key(int i) {
      return i + "k";
   }

   private static void testConnect() throws SQLException {
      try {
          Class.forName("org.postgresql.Driver" );
      } catch (Exception e) {
          System.out.println("ERROR: failed to load HSQLDB JDBC driver.");
          e.printStackTrace();
      }

      Connection c = DriverManager.getConnection("jdbc:postgresql://localhost/BOUKCADB", "blueoptima", "password");
      PreparedStatement preparedStatement = c.prepareStatement("create table testt(id1 INT, id2 INT)");
      preparedStatement.executeUpdate();
      preparedStatement.close();
   }
}
