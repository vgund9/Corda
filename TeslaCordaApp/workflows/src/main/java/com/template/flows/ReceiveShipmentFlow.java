package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;

@InitiatedBy(ShipmentFlow.class)
@StartableByRPC
public class ReceiveShipmentFlow extends FlowLogic<SignedTransaction> {
    FlowSession flowSession;
    public ReceiveShipmentFlow( FlowSession flowSession){
        this.flowSession = flowSession;
    }

    @Override
    @Suspendable
    public SignedTransaction call() throws FlowException {
        return subFlow(new ReceiveFinalityFlow(flowSession));
    }
}
