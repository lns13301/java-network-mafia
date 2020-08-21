package Server;

import java.util.List;

public class MafiaInformation {
    private Job job;
    private List<Skill> skills;
    private boolean isCitizen;

    public MafiaInformation(Job job, List<Skill> skills) {
        this.job = job;
        this.skills = skills;

        isCitizen = !job.equals(Job.VILLAIN) && !job.equals(Job.MAFIA) && !job.equals(Job.SPY) && !job.equals(Job.BEAST);
    }
}

enum Job {
    NONE,
    CITIZEN,
    VILLAIN,
    MAFIA,
    POLICE,
    DOCTOR,
    SPY,
    BEAST,
    SOLDIER,
    POLITICIAN,
    GANGSTER,
}

enum Skill {
    NONE,
    KILL,
    SEARCH,
    TREAT,
    RESEARCH,
    EAT,
    VIGIL,
    BULLETPROOF,
    VOTE,
    HIT
}