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
            transactionManagerLookupClass(JBossStandaloneJTAManagerLookup.class);

      DefaultCacheManager dcm = new DefaultCacheManager(configuration);
      Cache cache = dcm.getCache();

      AdvancedCache advancedCache = cache.getAdvancedCache();
      advancedCache.addInterceptor(new TxFaultInterceptor(), 1);

      TransactionManager transactionManager = advancedCache.getTransactionManager();
      transactionManager.begin();
      cache.put("k", "v");
      transactionManager.commit();
   }
}
