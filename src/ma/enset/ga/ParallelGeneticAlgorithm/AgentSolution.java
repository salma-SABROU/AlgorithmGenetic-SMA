package ma.enset.ga.ParallelGeneticAlgorithm;

import jade.core.AID;

public class AgentSolution {
    private AID aid;
    private String chromosone;

    public AID getAid() {
        return aid;
    }

    public AgentSolution(AID aid, String chromosone) {
        this.aid = aid;
        this.chromosone = chromosone;
    }

    public void setAid(AID aid) {
        this.aid = aid;
    }

    public String getChromosone() {
        return chromosone;
    }

    public void setChromosone(String chromosone) {
        this.chromosone = chromosone;
    }
}
