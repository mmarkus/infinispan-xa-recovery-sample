package org.infinispan.xasample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * // TODO: Document this
 *
 * @author Mircea Markus
 * @since 5.0
 */
public class HsqlTest {

   public static void main(String[] args) throws Exception {
      try {
          Class.forName("org.hsqldb.jdbcDriver" );
      } catch (Exception e) {
          System.out.println("ERROR: failed to load HSQLDB JDBC driver.");
          e.printStackTrace();
          return;
      }

      Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:aname", "sa", "");
      PreparedStatement preparedStatement = c.prepareStatement("create table testt(id1 INT, id2 INT)");
      preparedStatement.executeUpdate();
      preparedStatement.close();

      preparedStatement = c.prepareStatement("insert into testt(id1, id2) values(?,?)");

      for (int i = 0;i < 10; i++) {
         preparedStatement.setInt(1, i);
         preparedStatement.setInt(2, 100 + i);
         preparedStatement.addBatch();
      }

      preparedStatement.executeBatch();
      preparedStatement.close();


      String toRun = String.format("SELECT TOP ? %s, %s FROM %s ", "id1", "id2", "testt");

      PreparedStatement ps = c.prepareStatement(toRun);
      ps.setInt(1,7);

      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
         System.out.println("rs.getString(\"id1\") = " + rs.getString("id1"));
//         System.out.println("rs.getString(\"id2\") = " + rs.getString("id2"));
      }

      System.out.println("c = " + c);
   }
}
