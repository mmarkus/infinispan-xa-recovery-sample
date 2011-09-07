package org.infinispan.xasample;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

/**
* // TODO: Document this
*
* @author Mircea Markus
* @since 5.0
*/
class DummyXAResource implements XAResource {
   public void commit(Xid xid, boolean b) throws XAException {
      // TODO: Customise this generated block
   }

   public void end(Xid xid, int i) throws XAException {
      // TODO: Customise this generated block
   }

   public void forget(Xid xid) throws XAException {
      // TODO: Customise this generated block
   }

   public int getTransactionTimeout() throws XAException {
      return 0;  // TODO: Customise this generated block
   }

   public boolean isSameRM(XAResource xaResource) throws XAException {
      return false;  // TODO: Customise this generated block
   }

   public int prepare(Xid xid) throws XAException {
      return 0;  // TODO: Customise this generated block
   }

   public Xid[] recover(int i) throws XAException {
      return new Xid[0];  // TODO: Customise this generated block
   }

   public void rollback(Xid xid) throws XAException {
      // TODO: Customise this generated block
   }

   public boolean setTransactionTimeout(int i) throws XAException {
      return false;  // TODO: Customise this generated block
   }

   public void start(Xid xid, int i) throws XAException {
      // TODO: Customise this generated block
   }
}
