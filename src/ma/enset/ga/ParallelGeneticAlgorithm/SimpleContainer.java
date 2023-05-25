package ma.enset.ga.ParallelGeneticAlgorithm;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import ma.enset.ga.sequencial.GAUtils;
import ma.enset.ga.sma.IndividualAgent;
import ma.enset.ga.sma.MainAgentGA;

public class SimpleContainer {
    public static void main(String[] args) throws StaleProxyException {
        Runtime runtime=Runtime.instance();
        ProfileImpl profile=new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST,"localhost");
        AgentContainer agentContainer = runtime.createAgentContainer(profile);
        AgentController mainAgent=null;
        for (int i=0;i< GAUtils.ISLAND_NBR;i++){
            mainAgent = agentContainer.createNewAgent("island"+String.valueOf(i), IslandAgent.class.getName(), new Object[]{});
            mainAgent.start();
        }
        mainAgent = agentContainer.createNewAgent("mainAgent", MainAgent.class.getName(), new Object[]{});
        mainAgent.start();


    }
}
