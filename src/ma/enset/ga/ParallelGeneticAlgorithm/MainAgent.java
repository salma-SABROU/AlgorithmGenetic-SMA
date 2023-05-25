package ma.enset.ga.ParallelGeneticAlgorithm;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import ma.enset.ga.sequencial.GAUtils;
import ma.enset.ga.sma.AgentFitness;

import java.util.ArrayList;
import java.util.List;

public class MainAgent extends Agent {

    List<AgentSolution> agentSolutionList=new ArrayList<>();
    @Override
    protected void setup() {
        DFAgentDescription dfAgentDescription=new DFAgentDescription();
        ServiceDescription serviceDescription=new ServiceDescription();
        serviceDescription.setType("ga");
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFAgentDescription[] agentsDescriptions = DFService.search(this, dfAgentDescription);
            System.out.println("agentsDescriptions.length : "+agentsDescriptions.length);
            for (DFAgentDescription dfAD:agentsDescriptions) {
                agentSolutionList.add(new AgentSolution(dfAD.getName(),null));
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        SequentialBehaviour sequentialBehaviour = new SequentialBehaviour(this);

        sequentialBehaviour.addSubBehaviour(new Behaviour() {
            int nbr=0;
            @Override
            public void action() {
                for (int i=0;i<agentSolutionList.size();i++){
                    System.out.println("*********************************************for i = "+i);
                    ACLMessage aclMessage=new ACLMessage(ACLMessage.REQUEST);
                    aclMessage.addReceiver(agentSolutionList.get(i).getAid());
                    aclMessage.setContent("getSolution");
                    send(aclMessage);

                    aclMessage=blockingReceive();
                    System.out.println(agentSolutionList.get(i).getAid().getName()+" : "+aclMessage.getContent());
                    agentSolutionList.get(i).setChromosone(aclMessage.getContent());
                    System.out.println("*********************************************END i = "+i);
                }
                nbr++;
            }

            @Override
            public boolean done() {
                return nbr==GAUtils.ISLAND_NBR;
            }
        });
        sequentialBehaviour.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////");
                for (int i=0;i<agentSolutionList.size();i++){
                    System.out.println(agentSolutionList.get(i).getAid().getName()+" : "+agentSolutionList.get(i).getChromosone()+" : "+calculateFitness(agentSolutionList.get(i).getChromosone()));
                }
                System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////");
            }

        });

        addBehaviour(sequentialBehaviour);
    }

    public int calculateFitness(String chromo){
        int fitness=0;
        List<Character> cleanedList = new ArrayList<>();

        char[] charArray = chromo.toCharArray();
        char[] ArraySolution = GAUtils.SOLUTION.toCharArray();

        for (char c : charArray) {
            if (c != ' ' && c != ',' && c != '[' && c != ']') {
                cleanedList.add(c);
            }
        }

        char[] cleanedArray = new char[cleanedList.size()];
        for (int i = 0; i < cleanedList.size(); i++) {
            cleanedArray[i] = cleanedList.get(i);
        }

        for (int i=0;i<GAUtils.CHROMOSOME_SIZE;i++) {
            System.out.println("charArray[i] = "+cleanedArray[i]+"ArraySolution[i] = "+ArraySolution[i]);
            if(cleanedArray[i]==ArraySolution[i])
                fitness+=1;
        }
        return fitness;
    }


}
