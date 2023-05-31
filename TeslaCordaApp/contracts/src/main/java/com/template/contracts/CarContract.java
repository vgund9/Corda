package com.template.contracts;

import com.template.states.CarState;
import net.corda.core.contracts.*;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

import java.security.PublicKey;
import java.util.List;

import static net.corda.core.contracts.ContractsDSL.requireThat;

// ************
// * Contract *
// ************
public class CarContract implements Contract {
    // This is used to identify our contract when building a transaction.
    public static final String CAR_CONTRACT_ID = "com.template.contracts.CarContract";

    // A transaction is valid if verify() method of the contract of all the transaction's input and output states
    // does not throw an exception.
    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException{
        if(tx.getCommands().size() !=1) throw new IllegalArgumentException("There can only one command");
        Command command = tx.getCommand(0);
        CommandData commandData = command.getValue();
        if(commandData instanceof Shipment){
            //shipment rule
            //shape rule
            System.out.printf("tx.getInputStates().size()" + tx.getInputStates().size());
            if(tx.getInputStates().size() != 0){
                throw new IllegalArgumentException("There should not have input state");
            }
            System.out.printf("tx.getInputStates().size()" + tx.getOutputStates().size());
            if(tx.getOutputStates().size()!= 1){
                throw new IllegalArgumentException("There can only one output state");
            }
            //content rules
            ContractState outputState = tx.getOutput(0);
            if(!(outputState instanceof CarState)){
                throw new IllegalArgumentException("OutputState state has to be type of CarState");
            }
            CarState carState = (CarState) outputState;
            if(!carState.getModel().equals("CYBER_TRUCK")){
                throw new IllegalArgumentException("This is not CyberTruck");
            }

            //signer rules
            List<PublicKey> signers = command.getSigners();

            PublicKey manufactureKey = carState.getManufacturer().getOwningKey();
            if(!signers.contains(manufactureKey)){
                throw new IllegalArgumentException("Manufacturer should sign transaction");
            }
            }

        }

        /* We can use the requireSingleCommand function to extract command data from transaction.
         * However, it is possible to have multiple commands in a single transaction.*/
        //final CommandWithParties<Commands> command = requireSingleCommand(tx.getCommands(), Commands.class);
        /*final CommandData commandData = tx.getCommands().get(0).getValue();
233
        if (commandData instanceof Commands.Send) {
            //Retrieve the output state of the transaction
            TemplateState output = tx.outputsOfType(TemplateState.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {
                require.using("No inputs should be consumed when sending the Hello-World message.", tx.getInputStates().isEmpty());
                require.using("The message must be Hello-World", output.getMsg().equals("Hello-World"));
                return null;
            });
        }*/


    // Used to indicate the transaction's intent.
    public static class Shipment implements CommandData {}

}