package org.infinispan.xasample;

import com.arjuna.ats.arjuna.common.RecoveryEnvironmentBean;
import com.arjuna.ats.arjuna.common.recoveryPropertyManager;
import com.arjuna.ats.arjuna.recovery.RecoveryManager;
import com.arjuna.ats.arjuna.recovery.RecoveryModule;
import com.arjuna.ats.internal.jta.recovery.arjunacore.XARecoveryModule;
import com.arjuna.ats.jta.recovery.XAResourceRecoveryHelper;
import org.infinispan.AdvancedCache;
import org.infinispan.Cache;
import org.infinispan.config.Configuration;
import org.infinispan.context.InvocationContextContainer;
import org.infinispan.factories.ComponentRegistry;
import org.infinispan.transaction.TransactionCoordinator;
import org.infinispan.transaction.TransactionTable;
import org.infinispan.transaction.xa.LocalXaTransaction;
import org.infinispan.transaction.xa.TransactionXaAdapter;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.util.Collections;

/**
 * // TODO: Document this
 *
 * @author Mircea Markus
 * @since 5.0
 */
public class RecoveryRunner {

   public void runRecovery(Cache cache) {
      RecoveryEnvironmentBean reb = recoveryPropertyManager.getRecoveryEnvironmentBean();
      reb.setPeriodicRecoveryPeriod(5);
      reb.setRecoveryBackoffPeriod(3);

      RecoveryManager rm = RecoveryManager.manager(RecoveryManager.INDIRECT_MANAGEMENT);


      XARecoveryModule module = new XARecoveryModule();
      module.addXAResourceRecoveryHelper(new MyXAResourceRecoveryHelper(cache.getAdvancedCache()));
      reb.setRecoveryModules(Collections.<RecoveryModule>singletonList(module));

      rm.addModule(module);
   }

   private static class MyXAResourceRecoveryHelper implements XAResourceRecoveryHelper {

      private final AdvancedCache advancedCache;

      private MyXAResourceRecoveryHelper(AdvancedCache advancedCache) {
         this.advancedCache = advancedCache;
      }

      public boolean initialise(String p) throws Exception {
         System.out.println("RecoveryRunner.initialise");
         return true;
      }

      public XAResource[] getXAResources() throws Exception {
         Cache cache = advancedCache;
         cache.getAdvancedCache().getZ
         System.out.println("RecoveryRunner.getXAResources");
         ComponentRegistry componentRegistry = advancedCache.getComponentRegistry();
         TransactionTable tt = componentRegistry.getComponent(TransactionTable.class);
         org.infinispan.transaction.xa.recovery.RecoveryManager component = componentRegistry.getComponent(org.infinispan.transaction.xa.recovery.RecoveryManager.class);
         XAResource result = new MyTransactionXAAdapter(null, tt, advancedCache.getConfiguration(),
                                                      advancedCache.getInvocationContextContainer(),
                                                      component, componentRegistry.getComponent(TransactionCoordinator.class));
         return new XAResource[]{result};
      }
   }


   public static class MyTransactionXAAdapter extends TransactionXaAdapter {

      Long time = System.currentTimeMillis();

      public MyTransactionXAAdapter(LocalXaTransaction localTransaction, TransactionTable txTable, Configuration configuration, InvocationContextContainer icc, org.infinispan.transaction.xa.recovery.RecoveryManager rm, TransactionCoordinator txCoordinator) {
         super(localTransaction, txTable, configuration, icc, rm, txCoordinator);
      }

//      @Override
//      public int hashCode() {
//         return time.hashCode();
//      }

      @Override
      public int prepare(Xid externalXid) throws XAException {
         log("RecoveryRunner$MyTransactionXAAdapter.prepare");
         return super.prepare(externalXid);    // TODO: Customise this generated block
      }

      @Override
      public void commit(Xid externalXid, boolean isOnePhase) throws XAException {
         log("RecoveryRunner$MyTransactionXAAdapter.commit");
         super.commit(externalXid, isOnePhase);    // TODO: Customise this generated block
      }

      @Override
      public void rollback(Xid externalXid) throws XAException {
         log("RecoveryRunner$MyTransactionXAAdapter.rollback");
         super.rollback(externalXid);    // TODO: Customise this generated block
      }

      @Override
      public void forget(Xid externalXid) throws XAException {
         log("RecoveryRunner$MyTransactionXAAdapter.forget");
         super.forget(externalXid);    // TODO: Customise this generated block
      }

      @Override
      public Xid[] recover(int flag) throws XAException {
         log("RecoveryRunner$MyTransactionXAAdapter.recover");
         return super.recover(flag);    // TODO: Customise this generated block
      }

      void log(String msg) {
         System.out.println("[" + System.identityHashCode(this) + "] " + System.currentTimeMillis() + " - " + msg);
      }
   }
}
