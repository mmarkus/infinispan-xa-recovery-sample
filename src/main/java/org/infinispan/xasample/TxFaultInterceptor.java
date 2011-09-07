package org.infinispan.xasample;

import org.infinispan.CacheException;
import org.infinispan.commands.tx.CommitCommand;
import org.infinispan.context.impl.TxInvocationContext;
import org.infinispan.interceptors.base.CommandInterceptor;

/**
 * @author Mircea Markus
 */
public class TxFaultInterceptor extends CommandInterceptor {

   private boolean fail = true;

   @Override
   public Object visitCommitCommand(TxInvocationContext ctx, CommitCommand command) throws Throwable {
      if (fail) {
         fail = false;
         throw new CacheException("Induced!");
      }
      return super.visitCommitCommand(ctx, command);
   }
}
