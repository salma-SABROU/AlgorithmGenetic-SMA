package ma.enset.ga.sma;

import jade.core.AID;
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
import ma.enset.ga.sequencial.Individual;

import java.util.*;

public class MainAgentGA extends Agent {
    List<AgentFitness> agentsFitness=new ArrayList<>();
    Random rnd=new Random();

    @Override
    protected void setup() {
        DFAgentDescription dfAgentDescription=new DFAgentDescription();
        ServiceDescription serviceDescription=new ServiceDescription();
        serviceDescription.setType("ga");
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFAgentDescription[] agentsDescriptions = DFService.search(this, dfAgentDescription);
            //System.out.println(agentsDescriptions.length);
            for (DFAgentDescription dfAD:agentsDescriptions) {
                agentsFitness.add(new AgentFitness(dfAD.getName(),0));
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        calculateFintness();
        SequentialBehaviour sequentialBehaviour = new SequentialBehaviour(this);


        sequentialBehaviour.addSubBehaviour(new Behaviour() {
            int cpt=0;
            @Override
            public void action() {
                ACLMessage receivedMSG = receive();

                if (receivedMSG!=null){
                    cpt++;
                    System.out.println(cpt);
                    int fintess=Integer.parseInt(receivedMSG.getContent());
                    AID sender=receivedMSG.getSender();
                    //System.out.println(sender.getName()+" "+fintess);
                    setAgentFintess(sender,fintess);
                    if(cpt==GAUtils.POPULATION_SIZE){
                        Collections.sort(agentsFitness,Collections.reverseOrder());
                        showPopulation();
                    }
                }else {
                    block();
                }
            }

            @Override
            public boolean done() {
                return cpt==GAUtils.POPULATION_SIZE;
            }

        });

        sequentialBehaviour.addSubBehaviour(new Behaviour() {
            int it=0;
            AgentFitness agent1;
            AgentFitness agent2;

            @Override
            public void action() {
                selection();
                crossover();
                Collections.sort(agentsFitness,Collections.reverseOrder());

                sendMessage(agentsFitness.get(0).getAid(),"chromosome",ACLMessage.REQUEST);
                ACLMessage aclMessage=blockingReceive();
                System.out.println(it+" : "+aclMessage.getContent()+" :: "+agentsFitness.get(0).getFitness());
                it++;
            }
            private void selection(){
                //SELECTION
                agent1=agentsFitness.get(0);
                agent2=agentsFitness.get(1);

                sendMessage(agent1.getAid(),"chromosome",ACLMessage.REQUEST);
                sendMessage(agent2.getAid(),"chromosome",ACLMessage.REQUEST);
            }
            private void crossover(){
                ACLMessage receivedMSG1 = blockingReceive();
                ACLMessage receivedMSG2 = blockingReceive();


                int pointCroisment=rnd.nextInt(GAUtils.CHROMOSOME_SIZE-2);
                pointCroisment++;
                //System.out.println("Point croissement : "+pointCroisment);
                char [] chromoParent1=receivedMSG1.getContent().toCharArray();
                char [] chromoParent2=receivedMSG2.getContent().toCharArray();
                char [] chromoOffsring1=new char[GAUtils.CHROMOSOME_SIZE];
                char [] chromoOffsring2=new char[GAUtils.CHROMOSOME_SIZE];

                for (int i=0;i<chromoParent1.length;i++) {
                    chromoOffsring1[i]=chromoParent1[i];
                    chromoOffsring2[i]=chromoParent2[i];
                }

                for (int i=0;i<pointCroisment;i++) {
                    chromoParent1[i]=chromoOffsring2[i];
                    chromoParent2[i]=chromoOffsring1[i];
                }

                /*System.out.println("//////////////////////////////////Befor crossover//////////////////////////////////");
                System.out.println("/////// "+Arrays.toString(chromoParent1));
                System.out.println("/////// "+Arrays.toString(chromoParent2));
                System.out.println("//////////////////////////////////After crossover//////////////////////////////////");
                System.out.println("/////// "+Arrays.toString(chromoOffsring1));
                System.out.println("/////// "+Arrays.toString(chromoOffsring2));*/

                sendMessage(agentsFitness.get(GAUtils.POPULATION_SIZE-2).getAid(),new String(chromoOffsring1),ACLMessage.REQUEST);

                sendMessage(agentsFitness.get(GAUtils.POPULATION_SIZE-1).getAid(),new String(chromoOffsring2),ACLMessage.REQUEST);

                ACLMessage recieveMsg1=blockingReceive();
                ACLMessage recieveMsg2=blockingReceive();
                setAgentFintess(recieveMsg1.getSender(),Integer.parseInt(recieveMsg1.getContent()));
                setAgentFintess(recieveMsg2.getSender(),Integer.parseInt(recieveMsg2.getContent()));
            }
            @Override
            public boolean done() {
                return it==GAUtils.MAX_IT || agentsFitness.get(0).getFitness()==GAUtils.CHROMOSOME_SIZE;
            }
        });
        addBehaviour(sequentialBehaviour);
    }
private void calculateFintness(){
    ACLMessage message=new ACLMessage(ACLMessage.REQUEST);

    for (AgentFitness agf:agentsFitness) {
        message.addReceiver(agf.getAid());
    }
    message.setContent("fitness");
    send(message);

}
private void setAgentFintess(AID aid,int fitness){
        for (int i=0;i<GAUtils.POPULATION_SIZE;i++){
            if(agentsFitness.get(i).getAid().equals(aid)){
                agentsFitness.get(i).setFitness(fitness);
                //System.out.println(fitness+"=:="+agentsFitness.get(i).getFitness());
                break;
            }
        }
}

private void showPopulation(){
    for (AgentFitness agentFitness:agentsFitness) {
        System.out.println(agentFitness.getAid().getName()+" "+agentFitness.getFitness());
    }
}

private void sendMessage(AID aid,String content,int perfomative){
    ACLMessage message=new ACLMessage(perfomative);
    message.addReceiver(aid);
    message.setContent(content);
    send(message);
}

}
