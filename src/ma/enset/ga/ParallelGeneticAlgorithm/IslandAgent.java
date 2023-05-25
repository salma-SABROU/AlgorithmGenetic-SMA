package ma.enset.ga.ParallelGeneticAlgorithm;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import ma.enset.ga.sequencial.GAUtils;
import ma.enset.ga.sequencial.Population;

import java.util.Arrays;

public class IslandAgent extends Agent {

    @Override
    protected void setup() {
        DFAgentDescription dfAgentDescription=new DFAgentDescription();
        dfAgentDescription.setName(getAID());
        ServiceDescription serviceDescription=new ServiceDescription();
        serviceDescription.setType("ga");
        serviceDescription.setName("ga_ma");
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFService.register(this,dfAgentDescription);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        //System.out.println(" inside Agent setup");
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                //System.out.println(" inside Agent action befor recieve");
                ACLMessage receivedMSG = receive();
                //System.out.println(" inside Agent action after recieve");
                if(receivedMSG!=null){
                    String chromoSolution=algoGenetic();
                    ACLMessage aclMessage=new ACLMessage(ACLMessage.REQUEST);
                    aclMessage.setContent(chromoSolution);
                    aclMessage.addReceiver(receivedMSG.getSender());
                    send(aclMessage);
                }else {
                    block();
                }
            }
        });
    }

    private String algoGenetic(){
        String chromoFin=null;
        Population population=new Population();
        population.initialaizePopulation();
        population.calculateIndFintess();
        population.sortPopulation();
        int it=0;
        //System.out.println("Chromosome :"+ Arrays.toString(population.getFitnessIndivd().getGenes())+" fitness :"+population.getFitnessIndivd().getFitness());

        while (it< GAUtils.MAX_IT && population.getFitnessIndivd().getFitness()<GAUtils.CHROMOSOME_SIZE){
            population.selection();
            population.crossover();
            population.mutation();
            population.calculateIndFintess();
            population.sortPopulation();
            //System.out.println("It :"+it+"Chromosome :"+Arrays.toString(population.getFitnessIndivd().getGenes())+" fitness :"+population.getFitnessIndivd().getFitness());

            it++;
        }
        chromoFin=Arrays.toString(population.getFitnessIndivd().getGenes());

        return chromoFin;
    }
}
