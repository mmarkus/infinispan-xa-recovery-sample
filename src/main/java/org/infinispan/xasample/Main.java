package org.infinispan.xasample;

import org.infinispan.AdvancedCache;
import org.infinispan.Cache;
import org.infinispan.config.Configuration;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.transaction.lookup.JBossStandaloneJTAManagerLookup;

import javax.transaction.TransactionManager;


/**
 * @author Mircea Markus
 */
public class Main {

   public static void main(String[] args) throws Exception {
      Configuration configuration = new Configuration();
      configuration.fluent().mode(Configuration.CacheMode.LOCAL).transaction().
            transactionManagerLookupClass(JBossStandaloneJTAManagerLookup.class).recovery();

      DefaultCacheManager dcm = new DefaultCacheManager(configuration);
      Cache cache = dcm.getCache();

      AdvancedCache advancedCache = cache.getAdvancedCache();
      advancedCache.addInterceptor(new TxFaultInterceptor(), 1);

      TransactionManager transactionManager = advancedCache.getTransactionManager();
      transactionManager.begin();
      cache.put("k", "v");
      transactionManager.getTransaction().enlistResource(new DummyXAResource());
      transactionManager.getTransaction().enlistResource(new DummyXAResource());
      try {
         transactionManager.commit();
      } catch (Exception e) {
         e.printStackTrace();
      }

      System.out.println("Main.main");

      new RecoveryRunner().runRecovery(cache);

      while (true) {
         Thread.sleep(1000);
         System.out.println("Main.main ...");
      }
   }
}
