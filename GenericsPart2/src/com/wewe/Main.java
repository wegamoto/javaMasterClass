package com.wewe;

import java.util.ArrayList;
import java.util.Collections;

public class Main {

    public static void main(String[] args) {

        FootballPlayer joe = new FootballPlayer("Joe");
        BaseballPlayer pat = new BaseballPlayer("Pat");
        SoccerPlayer beckham = new SoccerPlayer("Beckham");

        Team<FootballPlayer> adeladieCrows = new Team<>("Adelaide Crows");
        adeladieCrows.addPlayer(joe);
//        adeladieCrows.addPlayer(pat);
//        adeladieCrows.addPlayer(beckham);

        System.out.println(adeladieCrows.numPlayers());

        Team<BaseballPlayer> baseballPlayerTeam = new Team<>("Chicago Cubs");
        baseballPlayerTeam.addPlayer(pat);

        Team<SoccerPlayer> brokenTeam = new Team<>("this won't work");
        brokenTeam.addPlayer(beckham);

        Team<FootballPlayer> melbourne = new Team<>("Melbourne");
        FootballPlayer banks = new FootballPlayer("Gordon");
        melbourne.addPlayer(banks);

        Team<FootballPlayer> hawthorn= new Team<>("Hawthorn");
        Team<FootballPlayer> fremantle = new Team<>("Fremantle");

        hawthorn.matchResult(fremantle, 1, 0);
        hawthorn.matchResult(adeladieCrows, 3, 8);

        adeladieCrows.matchResult(fremantle, 2, 1);
//        adeladieCrows.matchResult(baseballPlayerTeam,1,1);

        System.out.println("Rankings");
        System.out.println(adeladieCrows.getName() + ": " + adeladieCrows.ranking());
        System.out.println(melbourne.getName() + ": " + melbourne.ranking());
        System.out.println(fremantle.getName() + ": " + fremantle.ranking());
        System.out.println(hawthorn.getName() + ": " + hawthorn.ranking());

        System.out.println(adeladieCrows.compareTo(melbourne));
        System.out.println(adeladieCrows.compareTo(hawthorn));
        System.out.println(hawthorn.compareTo(adeladieCrows));
        System.out.println(melbourne.compareTo(fremantle));

//        ArrayList<Team> teams;
//        Collections.sort(teams);
    }
}
