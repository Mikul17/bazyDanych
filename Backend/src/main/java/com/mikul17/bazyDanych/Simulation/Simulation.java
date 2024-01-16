package com.mikul17.bazyDanych.Simulation;

import com.mikul17.bazyDanych.Enums.EventType;
import com.mikul17.bazyDanych.Enums.PlayerPosition;
import com.mikul17.bazyDanych.Enums.Stat;
import com.mikul17.bazyDanych.Models.Matches.Match;
import com.mikul17.bazyDanych.Models.Matches.MatchEvents;
import com.mikul17.bazyDanych.Models.Matches.MatchStats;
import com.mikul17.bazyDanych.Models.Simulation.Players.Player;
import com.mikul17.bazyDanych.Models.Simulation.Players.PlayerSkill;
import com.mikul17.bazyDanych.Models.Simulation.Players.PlayerStat;
import com.mikul17.bazyDanych.Models.Simulation.Team;
import com.mikul17.bazyDanych.Repository.*;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class Simulation {

    private final Logger logger = LoggerFactory.getLogger(Simulation.class);
    //Repositories
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PlayerSkillRepository playerSkillRepository;
    private final PlayerStatRepository playerStatRepository;
    private final MatchStatsRepository matchStatsRepository;
    private final MatchEventRepository matchEventsRepository;
    //Match related objects
    private final Match match;
    private final MatchStats homeTeamStats;
    private final MatchStats awayTeamStats;
    private final Team homeTeam;
    private final Team awayTeam;
    private List<MatchEvents> matchEvents;
    private final Map<Player,PlayerSkill> playerSkillMap;
    private final Map<Player, PlayerStat> playerStatMap;
    private Player activePlayer;
    private Player previousPlayer;
    //Match variables
    private boolean isThrowIn=false;
    private boolean isCorner=false;
    private boolean isFreeKick=false;
    private boolean isPenalty=false;
    private boolean gameResume=true;
    private boolean haveDribbled =false;
    int homeTeamPossessionTime = 0;
    int awayTeamPossessionTime = 0;
    int minute =1;


    public Simulation (Match match,
                       PlayerRepository playerRepository,
                       TeamRepository teamRepository,
                       PlayerSkillRepository playerSkillRepository,
                       PlayerStatRepository playerStatRepository,
                       MatchStatsRepository matchStatsRepository,
                       MatchEventRepository matchEventsRepository) {
        this.match = match;
        this.homeTeamStats = new MatchStats();
        this.awayTeamStats = new MatchStats();
        this.matchEvents = new ArrayList<>();
        this.homeTeam = match.getHomeTeam();
        this.awayTeam = match.getAwayTeam();
        this.playerSkillMap = new HashMap<>();
        this.playerStatMap = new HashMap<>();
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.playerSkillRepository = playerSkillRepository;
        this.playerStatRepository = playerStatRepository;
        this.matchStatsRepository = matchStatsRepository;
        this.matchEventsRepository = matchEventsRepository;
    }
    private void checkWhoWin(){
        homeTeam.setGoalsScored(homeTeam.getGoalsScored()+ homeTeamStats.getGoalsScored());
        homeTeam.setGoalsConceded(homeTeam.getGoalsConceded()+ awayTeamStats.getGoalsScored());
        awayTeam.setGoalsScored(awayTeam.getGoalsScored()+ awayTeamStats.getGoalsScored());
        awayTeam.setGoalsConceded(awayTeam.getGoalsConceded()+ homeTeamStats.getGoalsScored());
        if(homeTeamStats.getGoalsScored() > awayTeamStats.getGoalsScored()) {
            homeTeam.setWins(homeTeam.getWins()+1);
            awayTeam.setLoses(awayTeam.getLoses()+1);
            homeTeam.setLeaguePoints(homeTeam.getLeaguePoints()+3);
        }else if(homeTeamStats.getGoalsScored() < awayTeamStats.getGoalsScored()) {
            awayTeam.setWins(awayTeam.getWins() + 1);
            homeTeam.setLoses(homeTeam.getLoses() + 1);
            awayTeam.setLeaguePoints(awayTeam.getLeaguePoints() + 3);
        }else{
            homeTeam.setDraws(homeTeam.getDraws()+1);
            awayTeam.setDraws(awayTeam.getDraws()+1);
            homeTeam.setLeaguePoints(homeTeam.getLeaguePoints()+1);
            awayTeam.setLeaguePoints(awayTeam.getLeaguePoints()+1);

        }
    }
    private void saveAll(){
        checkWhoWin();
        matchStatsRepository.save(homeTeamStats);
        matchStatsRepository.save(awayTeamStats);
        matchEventsRepository.saveAll(matchEvents);
        teamRepository.save(homeTeam);
        teamRepository.save(awayTeam);
        playerStatRepository.saveAll(playerStatMap.values());
        playerRepository.saveAll(homeTeam.getPlayers());
        playerRepository.saveAll(awayTeam.getPlayers());
    }
    private void endAsWalkover (Team team) throws WalkoverException {
        boolean isHome = this.match.getHomeTeam().equals(team);
        if (isHome) {
            homeTeamStats.setGoalsScored(0);
            awayTeamStats.setGoalsScored(3);
        } else {
            homeTeamStats.setGoalsScored(3);
            awayTeamStats.setGoalsScored(0);
        }

        matchStatsRepository.save(homeTeamStats);
        matchStatsRepository.save(awayTeamStats);
        matchEventsRepository.saveAll(matchEvents);
        throw new WalkoverException("Match ended as walkover");
    }
    private boolean checkIfTeamDontHaveEnoughPlayers (Team team) throws WalkoverException {
        Map<String, Integer> requiredPlayers = new HashMap<>();
        requiredPlayers.put(PlayerPosition.GOALKEEPER.getPosition(), 1);
        requiredPlayers.put(PlayerPosition.DEFENDER.getPosition(), 4);
        requiredPlayers.put(PlayerPosition.MIDFIELDER.getPosition(), 4);
        requiredPlayers.put(PlayerPosition.FORWARD.getPosition(), 2);

        Map<String, Integer> currentPlayers = new HashMap<>();
        for (Player player : team.getPlayers()) {
            if (player.getIsBenched()) {
                continue;
            }
            if(!player.isAbleToPlay()){
                if(!swapIfPossible(player)){
                    logger.info("Ending match between " + match.getHomeTeam().getTeamName() + " and "
                            + match.getAwayTeam().getTeamName() + " as walkover - insufficient amount of players.");
                    endAsWalkover(team);
                    return true;
                }
            }
            if (currentPlayers.containsKey(player.getPosition())) {
                currentPlayers.put(player.getPosition(), currentPlayers.get(player.getPosition()) + 1);
            } else {
                currentPlayers.put(player.getPosition(), 1);
            }
        }

        for (Map.Entry<String, Integer> entry : requiredPlayers.entrySet()) {
            if (!currentPlayers.containsKey(entry.getKey()) || currentPlayers.get(entry.getKey()) < entry.getValue()) {
                logger.info("Ending match between " + match.getHomeTeam().getTeamName() + " and "
                        + match.getAwayTeam().getTeamName() + " as walkover - insufficient amount of players.");
                endAsWalkover(team);
                return true;
            }
        }
        return false;
    }


    private void InitializeDefaultVariables() {
        this.isThrowIn=false;
        this.isCorner=false;
        this.isFreeKick=false;
        this.isPenalty=false;
        this.gameResume=true;
    }
    private void InitializePlayerSkills() throws MatchSimulationException {
        for(Player player : homeTeam.getPlayers()){
            playerSkillMap.put(player, playerSkillRepository.findById(player.getId())
                    .orElseThrow(() -> new MatchSimulationException("Skill of player not found")));
        }
        for(Player player : awayTeam.getPlayers()){
            playerSkillMap.put(player, playerSkillRepository.findById(player.getId())
                    .orElseThrow(() -> new MatchSimulationException("Skill of player not found")));
        }
    }
    private void InitializePlayerStat() throws MatchSimulationException{
        for(Player player : homeTeam.getPlayers()){
            playerStatMap.put(player, playerStatRepository.findById(player.getId())
                    .orElseThrow(() -> new MatchSimulationException("Stat of player not found")));
        }
        for(Player player : awayTeam.getPlayers()){
            playerStatMap.put(player, playerStatRepository.findById(player.getId())
                    .orElseThrow(() -> new MatchSimulationException("Stat of player not found")));
        }
    }
    private void InitializeMatchStats(){
        homeTeamStats.setMatch(this.match);
        homeTeamStats.setTeam(match.getHomeTeam().getId());
        homeTeamStats.setGoalsScored(0);
        homeTeamStats.setPossession(0);
        homeTeamStats.setShots(0);
        homeTeamStats.setThrowIns(0);
        homeTeamStats.setShotsOnTarget(0);
        homeTeamStats.setPasses(0);
        homeTeamStats.setCorners(0);
        homeTeamStats.setCorners(0);
        homeTeamStats.setFreeKicks(0);
        homeTeamStats.setPenalties(0);
        homeTeamStats.setYellowCards(0);
        homeTeamStats.setRedCards(0);
        homeTeamStats.setFouls(0);

        awayTeamStats.setMatch(this.match);
        awayTeamStats.setTeam(match.getAwayTeam().getId());
        awayTeamStats.setGoalsScored(0);
        awayTeamStats.setPossession(0);
        awayTeamStats.setShots(0);
        awayTeamStats.setThrowIns(0);
        awayTeamStats.setShotsOnTarget(0);
        awayTeamStats.setPasses(0);
        awayTeamStats.setCorners(0);
        awayTeamStats.setCorners(0);
        awayTeamStats.setFreeKicks(0);
        awayTeamStats.setPenalties(0);
        awayTeamStats.setYellowCards(0);
        awayTeamStats.setRedCards(0);
        awayTeamStats.setFouls(0);

        matchStatsRepository.save(homeTeamStats);
        matchStatsRepository.save(awayTeamStats);
    }
    private boolean swapIfPossible(Player player) {
        Team team = player.getTeam();
        List<Player> possibleSubstitutes = team.getPlayers().stream()
                .filter(substitute -> substitute.getPosition().equals(player.getPosition())
                        && substitute.getIsBenched() && substitute.isAbleToPlay()).toList();
        if (possibleSubstitutes.isEmpty()) {
            return false;
        }

        int randomIndex = (int) (Math.random() * possibleSubstitutes.size());
        Player playerToSwap = possibleSubstitutes.get(randomIndex);

        //swap captain if needed
        if(player.getTeam().getCaptain() == player){
            player.getTeam().setCaptain(playerToSwap);
            teamRepository.save(player.getTeam());
        }

        //swap players
        playerToSwap.setIsBenched(false);
        player.setIsBenched(true);
        //update player in db
        playerRepository.save(player);
        playerRepository.save(playerToSwap);
        return true;
    }

    private void incrementTeamStat (Team team, Stat stat) throws MatchSimulationException {
        boolean isHome = this.match.getHomeTeam().equals(team);
        switch (stat.getStat()) {
            case "goals":
                if (isHome) {
                    homeTeamStats.setGoalsScored(homeTeamStats.getGoalsScored() + 1);
                } else {
                    awayTeamStats.setGoalsScored(awayTeamStats.getGoalsScored() + 1);
                }
                break;
            case "possession":
                if (isHome) {
                    homeTeamStats.setPossession(homeTeamStats.getPossession() + 1);
                } else {
                    awayTeamStats.setPossession(awayTeamStats.getPossession() + 1);
                }
                break;
            case "shots":
                if (isHome) {
                    homeTeamStats.setShots(homeTeamStats.getShots() + 1);
                } else {
                    awayTeamStats.setShots(awayTeamStats.getShots() + 1);
                }
                break;
            case "shotsOnTarget":
                if (isHome) {
                    homeTeamStats.setShotsOnTarget(homeTeamStats.getShotsOnTarget() + 1);
                } else {
                    awayTeamStats.setShotsOnTarget(awayTeamStats.getShotsOnTarget() + 1);
                }
                break;
            case "passes":
                if (isHome) {
                    homeTeamStats.setPasses(homeTeamStats.getPasses() + 1);
                } else {
                    awayTeamStats.setPasses(awayTeamStats.getPasses() + 1);
                }
                break;
            case "corners":
                if (isHome) {
                    homeTeamStats.setCorners(homeTeamStats.getCorners() + 1);
                } else {
                    awayTeamStats.setCorners(awayTeamStats.getCorners() + 1);
                }
                break;
            case "throwIns":
                if (isHome) {
                    homeTeamStats.setThrowIns(homeTeamStats.getThrowIns() + 1);
                } else {
                    awayTeamStats.setThrowIns(awayTeamStats.getThrowIns() + 1);
                }
                break;
            case "freeKicks":
                if (isHome) {
                    homeTeamStats.setFreeKicks(homeTeamStats.getFreeKicks() + 1);
                } else {
                    awayTeamStats.setFreeKicks(awayTeamStats.getFreeKicks() + 1);
                }
                break;
            case "penalties":
                if (isHome) {
                    homeTeamStats.setPenalties(homeTeamStats.getPenalties() + 1);
                } else {
                    awayTeamStats.setPenalties(awayTeamStats.getPenalties() + 1);
                }
                break;
            case "yellowCards":
                if (isHome) {
                    homeTeamStats.setYellowCards(homeTeamStats.getYellowCards() + 1);
                } else {
                    awayTeamStats.setYellowCards(awayTeamStats.getYellowCards() + 1);
                }
                break;
            case "redCards":
                if (isHome) {
                    homeTeamStats.setRedCards(homeTeamStats.getRedCards() + 1);
                } else {
                    awayTeamStats.setRedCards(awayTeamStats.getRedCards() + 1);
                }
                break;
            case "fouls":
                if (isHome) {
                    homeTeamStats.setFouls(homeTeamStats.getFouls() + 1);
                } else {
                    awayTeamStats.setFouls(awayTeamStats.getFouls() + 1);
                }
                break;
            default:
                throw new MatchSimulationException("Invalid team stat");
        }
    }
    private void incrementPlayerStat(Player player, String stat) throws MatchSimulationException {
        switch(stat){
            case "goalsScored":
                playerStatMap.get(player).setGoalsScored(playerStatMap.get(player).getGoalsScored()+1);
                break;
            case "assists":
                playerStatMap.get(player).setAssists(playerStatMap.get(player).getAssists()+1);
                break;
            case "yellowCards":
                playerStatMap.get(player).setYellowCards(playerStatMap.get(player).getYellowCards()+1);
                break;
            case "redCards":
                playerStatMap.get(player).setRedCards(playerStatMap.get(player).getRedCards()+1);
                break;
            case "fouls":
                playerStatMap.get(player).setFouls(playerStatMap.get(player).getFouls()+1);
                break;
            case "gamesPlayed":
                playerStatMap.get(player).setGamesPlayed(playerStatMap.get(player).getGamesPlayed()+1);
                break;
            default:
                throw new MatchSimulationException("Invalid player stat");
        }
    }
    private Player getRandomPlayer (Team team, PlayerPosition position) throws WalkoverException {
        List<Player> players = team.getPlayers().stream()
                .filter(player ->
                        position.getPosition().equals(player.getPosition())
                                && !player.getIsBenched() && player.isAbleToPlay()).toList();
        if (players.isEmpty()) {
            logger.info("Ending match between " + match.getHomeTeam().getTeamName() + " and "
                    + match.getAwayTeam().getTeamName() + " as walkover - getRandomPlayer returns null.");
            endAsWalkover(team);
        }

        int randomIndex = (int) (Math.random() * players.size());
        return players.get(randomIndex);
    }
    private Team getOpponentTeam (Player player) {
        return player.getTeam().getId().equals(homeTeam.getId()) ? awayTeam : homeTeam;
    }
    private Player getOpponent(boolean isLongPass) throws MatchSimulationException, WalkoverException {
        boolean isHome = activePlayer.getTeam().getId().equals(homeTeam.getId());
        switch(activePlayer.getPosition()){
            case "goalkeeper", "defender":
                return isLongPass?
                        getRandomPlayer(isHome?awayTeam:homeTeam, PlayerPosition.MIDFIELDER)
                        :getRandomPlayer(isHome?awayTeam:homeTeam, PlayerPosition.FORWARD);
            case "midfielder":
                int random = (int) (Math.random() * 100);
                if(isLongPass){
                    return random <= 65?
                            getRandomPlayer(isHome?awayTeam:homeTeam, PlayerPosition.FORWARD)
                            :getRandomPlayer(isHome?awayTeam:homeTeam, PlayerPosition.DEFENDER);
                }
                return getRandomPlayer(isHome?awayTeam:homeTeam, PlayerPosition.MIDFIELDER);
            case "forward":
                return getRandomPlayer(isHome?awayTeam:homeTeam, PlayerPosition.DEFENDER);
            default:
                logger.error("Match stopped - invalid player position");
                throw new MatchSimulationException("Invalid player position [" + activePlayer.getPosition()+"] in getOpponent()");
        }
    }
    private Player getTeammate(boolean isLongPass) throws MatchSimulationException, WalkoverException {
        switch(activePlayer.getPosition()){
            case "goalkeeper", "defender":
                return isLongPass?
                        getRandomPlayer(activePlayer.getTeam(), PlayerPosition.MIDFIELDER)
                        :getRandomPlayer(activePlayer.getTeam(), PlayerPosition.DEFENDER);
            case "midfielder":
                int random = (int) (Math.random() * 100);
                if(isLongPass){
                    return random <= 65?
                            getRandomPlayer(activePlayer.getTeam(), PlayerPosition.DEFENDER)
                            :getRandomPlayer(activePlayer.getTeam(), PlayerPosition.FORWARD);
                }
                return getRandomPlayer(activePlayer.getTeam(), PlayerPosition.MIDFIELDER);
            case "forward":
                return getRandomPlayer(activePlayer.getTeam(), PlayerPosition.FORWARD);
            default:
                logger.error("Match stopped - invalid player position [" + activePlayer.getPosition()+"] in getTeammate()");
                throw new MatchSimulationException("Invalid player position");
        }
    }
    private boolean calculateChance(int chance){
        if(chance > 100){
            chance=80;
        }
        if(chance < 0){
            chance=5;
        }

        int random = (int) (Math.random() * 100);
        return random <= chance;
    }
    private boolean checkForFoul(Player opponent) throws MatchSimulationException, WalkoverException {
        int foulChance = 10;
        if(calculateChance(foulChance)){
            incrementTeamStat(opponent.getTeam(), Stat.FOULS);
            incrementPlayerStat(opponent,"fouls");
            checkForCard(opponent);
            activePlayer = checkForInjury(activePlayer);
            return true;
        }
        return false;
    }
    private void saveEvents(Player relatedPlayer, EventType type, String desc){
        MatchEvents event = MatchEvents.builder()
                .match(match)
                .player(relatedPlayer)
                .minute(minute)
                .actionDescription(desc)
                .actionType(type.getDesc()).build();
        matchEventsRepository.save(event);
    }
    private void checkForCard(Player opponent) throws MatchSimulationException, WalkoverException {
        int yellowCardChance = 25;
        int redCardChance = 2;
        if (calculateChance(yellowCardChance)) {
            if (opponent.getIsYellowCarded()) {
                handleRedCard(opponent,true);
                return;
            }
            incrementPlayerStat(opponent, "yellowCards");
            incrementTeamStat(opponent.getTeam(), Stat.YELLOW_CARDS);
            opponent.setIsYellowCarded(true);
            String desc = opponent.getLastName() + " committed a foul and got yellow card!";
            saveEvents(opponent,EventType.YellowCard,desc);
        } else if (calculateChance(redCardChance)) {
            handleRedCard(opponent,false);
        }else{
            String desc = opponent.getLastName() + " committed a foul but got only a warning!";
            saveEvents(opponent,EventType.Warning,desc);
        }
    }
    private void handleRedCard(Player player, boolean isSecondYellow) throws MatchSimulationException, WalkoverException {
        incrementTeamStat(player.getTeam(), Stat.RED_CARDS);
        incrementPlayerStat(player, "redCards");
        player.setIsRedCarded(true);

        int redCardsAmount = player.getTeam().getId().equals(homeTeam.getId())?
                homeTeamStats.getRedCards():awayTeamStats.getRedCards();
        if(redCardsAmount >=2){
            logger.info("Ending match between " + match.getHomeTeam().getTeamName() + " and "
                    + match.getAwayTeam().getTeamName() + " as walkover - too many red cards.");
            endAsWalkover(player.getTeam());
            return;
        }
        player.setIsBenched(true);
        String desc = isSecondYellow? player.getLastName() + "got second yellow cad ! He is sent off the field" :
                player.getLastName() + " committed a nasty foul and got red card in return ! He is sent off the field";
        saveEvents(player,EventType.RedCard,desc);
    }
    private Player checkForInjury(Player player) throws WalkoverException {
        int injuryChance = 5;
        if (calculateChance(injuryChance)) {
            player.setIsInjured(true);
            LocalDateTime injuredUntil = match.getMatchDate().toLocalDateTime()
                    .plusDays(1)
                    .withHour(0)
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0);
            player.setInjuredUntil(injuredUntil.toLocalDate());
            String desc = player.getLastName() + " got injured and is unable to play !";
            saveEvents(player,EventType.Injury,desc);
            if (!swapIfPossible(player)) {
                logger.info("Ending match between " + match.getHomeTeam().getTeamName() + " and "
                        + match.getAwayTeam().getTeamName() + " as walkover - insufficient amount of players.");
                endAsWalkover(player.getTeam());
                return getRandomPlayer(player.getTeam(), PlayerPosition.MIDFIELDER);
            }
        }
        return player;
    }
    private Player shortPass() throws MatchSimulationException, WalkoverException {
        Player opponent = getOpponent(false);
        Player teammate = getTeammate(false);
        int chance = 50 + (playerSkillMap.get(activePlayer)).getShortPass() -
                (playerSkillMap.get(opponent)).getDefending();

        incrementTeamStat(activePlayer.getTeam(), Stat.PASSES);
        if (calculateChance(chance)) {
            //successful pass
            previousPlayer = activePlayer;
            return teammate;
        }else {
            int paceBonus = playerSkillMap.get(opponent).getPace() / 10;
            if (!calculateChance(50 + paceBonus)) {
                //throw in
                incrementTeamStat(getOpponentTeam(activePlayer), Stat.THROW_INS);
                isThrowIn = true;
                return opponent;
            }
            if (checkForFoul(opponent)) {
                //foul
                isFreeKick = true;
                return teammate;
            }
            //successful interception
            return opponent;
        }
    }
    private Player longPass() throws MatchSimulationException, WalkoverException {
        Player opponent = getOpponent(true);
        Player teammate = getTeammate(true);
        int chance = 50 + (playerSkillMap.get(activePlayer)).getLongPass() -
                (playerSkillMap.get(opponent)).getDefending()+
                ((playerSkillMap.get(activePlayer)).getVision()/10);

        incrementTeamStat(activePlayer.getTeam(), Stat.PASSES);
        if (calculateChance(chance)) {
            //successful pass
            previousPlayer = activePlayer;
            return teammate;
        }else {
            int paceBonus = playerSkillMap.get(opponent).getPace() / 10;
            if (!calculateChance(50+paceBonus)) {
                //throw in
                incrementTeamStat(getOpponentTeam(activePlayer), Stat.THROW_INS);
                isThrowIn = true;
                return opponent;
            }
            if(checkForFoul(opponent)){
                //foul
                isFreeKick = true;
                return teammate;
            }
            //successful interception
            return opponent;
        }
    }
    private Player dribble() throws MatchSimulationException, WalkoverException {
        Player opponent = getOpponent(false);
        int chance = 50+ (playerSkillMap.get(activePlayer)).getDribbling() +
                ((playerSkillMap.get(activePlayer)).getPace()/10)-
                (playerSkillMap.get(opponent)).getDefending()-
                (playerSkillMap.get(opponent)).getPace()/10;

        if(calculateChance(chance)) {
            //successful dribble
            haveDribbled = true;
            return activePlayer;
        }else{
            //foul
            if(checkForFoul(opponent)){
                isFreeKick = true;
                return activePlayer;
            }
            //successful interception
            return opponent;

        }
    }
    private Player shot() throws MatchSimulationException, WalkoverException {
        Player opponent = getOpponent(false);
        Player goalkeeper = getRandomPlayer(getOpponentTeam(activePlayer), PlayerPosition.GOALKEEPER);

        if (haveDribbled){
            int goalChance = 10 + (playerSkillMap.get(activePlayer)).getCloseRangeShooting() -
                    (playerSkillMap.get(goalkeeper)).getCloseRangeDefending()-
                    (playerSkillMap.get(goalkeeper).getReflex()/10);
            haveDribbled = false;
            return closeRangeShot(opponent, goalkeeper, goalChance);
        }

        return longRangeShot(goalkeeper);
    }
    private Player shortPassNoBlock(boolean resume) throws MatchSimulationException, WalkoverException {
        Player teammate = resume?getRandomPlayer(activePlayer.getTeam(),PlayerPosition.MIDFIELDER) :getTeammate(false);
        previousPlayer = activePlayer;
        gameResume = false;

        return teammate;
    }
    private Player closeRangeShot(Player opponent, Player goalkeeper, int goalChance) throws MatchSimulationException, WalkoverException {
        int blockChance = (playerSkillMap.get(opponent)).getDefending()+
                (playerSkillMap.get(opponent)).getVision()/10;


        if(calculateChance(blockChance)) {
            if(calculateChance(20)){
                //corner
                isCorner = true;
                return getRandomPlayer(activePlayer.getTeam(), PlayerPosition.MIDFIELDER);
            }else{
                if(checkForFoul(opponent)){
                    //foul
                    isPenalty = true;
                    return activePlayer;
                }
                return opponent;
            }
        }else{
            incrementTeamStat(activePlayer.getTeam(), Stat.SHOTS);
            if(calculateChance(goalChance)){
                //goal
                return goal();
            }else{
                if(calculateChance(20)){
                    //corner
                    isCorner = true;
                    return getRandomPlayer(activePlayer.getTeam(), PlayerPosition.MIDFIELDER);
                }
                //miss
                return goalkeeper;
            }
        }
    }
    private Player longRangeShot(Player goalkeeper) throws MatchSimulationException, WalkoverException {
        int goalChance = 5+(playerSkillMap.get(activePlayer)).getLongRangeShooting()-
                (playerSkillMap.get(goalkeeper)).getLongRangeDefending();

        incrementTeamStat(activePlayer.getTeam(), Stat.SHOTS);
        if(calculateChance(goalChance)) {
            //goal
            return goal();
        }else{

            if(calculateChance(15)){
                //corner
                isCorner = true;
                return getRandomPlayer(activePlayer.getTeam(), PlayerPosition.MIDFIELDER);
            }
            //miss
            return goalkeeper;
        }
    }
    private Player headerShot() throws MatchSimulationException, WalkoverException {
        Player opponent = getRandomPlayer(getOpponentTeam(activePlayer), PlayerPosition.DEFENDER);
        Player goalkeeper = getRandomPlayer(getOpponentTeam(activePlayer), PlayerPosition.GOALKEEPER);

        int goalChance = 5+(playerSkillMap.get(activePlayer)).getHeader()-
                (playerSkillMap.get(goalkeeper)).getCloseRangeDefending()-
                (playerSkillMap.get(goalkeeper).getReflex()/10);
        int passLanding = 25+(playerSkillMap.get(activePlayer)).getHeader()-
                (playerSkillMap.get(opponent)).getHeader();

        incrementTeamStat(activePlayer.getTeam(), Stat.PASSES);
        if(calculateChance(passLanding)) {
            //successful pass
            if(calculateChance(goalChance)) {
                //goal
                return goal();
            }else{
                //goalkeeper save
                incrementTeamStat(activePlayer.getTeam(), Stat.SHOTS_ON_TARGET);
                return goalkeeper;
            }
        }else{
            //opponent interception
            return opponent;
        }
    }
    private Player penalty() throws MatchSimulationException, WalkoverException {
        Player goalkeeper = getRandomPlayer(getOpponentTeam(activePlayer), PlayerPosition.GOALKEEPER);
        int goalChance = 20+(playerSkillMap.get(activePlayer)).getPenalties()-
                (playerSkillMap.get(goalkeeper)).getCloseRangeDefending()-
                (playerSkillMap.get(goalkeeper).getDiving()/10);

        String desc = "There will be a penalty for "+activePlayer.getTeam().getTeamName();
        saveEvents(activePlayer,EventType.Penalty,desc);

        incrementTeamStat(activePlayer.getTeam(), Stat.PENALTIES);
        incrementTeamStat(activePlayer.getTeam(), Stat.SHOTS);
        isPenalty = false;
        if(calculateChance(goalChance)) {
            //goal
            return goal();
        }else{
            //miss
            return goalkeeper;
        }
    }
    private Player goal() throws MatchSimulationException {
        incrementTeamStat(activePlayer.getTeam(), Stat.GOALS);
        incrementTeamStat(activePlayer.getTeam(), Stat.SHOTS_ON_TARGET);
        incrementPlayerStat(activePlayer, "goalsScored");
        if(previousPlayer!=activePlayer && previousPlayer!=null && previousPlayer.getTeam().getId().equals(activePlayer.getTeam().getId())){
            incrementPlayerStat(previousPlayer, "assists");
            String desc = activePlayer.getLastName() + "("+ previousPlayer.getLastName()+") scored a goal !";
            saveEvents(activePlayer,EventType.Goal,desc);
            previousPlayer=null;
        }else{
            String desc = activePlayer.getLastName() + " scored a goal !";
            saveEvents(activePlayer,EventType.Goal,desc);
        }
        return getOpponentTeam(activePlayer).getCaptain();
    }
    private Player freekick() throws MatchSimulationException, WalkoverException {
        Player goalkeeper = getRandomPlayer(getOpponentTeam(activePlayer), PlayerPosition.GOALKEEPER);
        int goalChance = 20+(playerSkillMap.get(activePlayer)).getFreeKick()-
                (playerSkillMap.get(activePlayer)).getLongRangeDefending()-
                (playerSkillMap.get(activePlayer).getDiving()/10);

        String desc = "There will be a freekick for "+activePlayer.getTeam().getTeamName();
        saveEvents(activePlayer,EventType.Freekick,desc);

        incrementTeamStat(activePlayer.getTeam(), Stat.FREE_KICKS);
        isFreeKick = false;
        if(calculateChance(20)) {
            incrementTeamStat(activePlayer.getTeam(), Stat.SHOTS);
            if(calculateChance(goalChance)) {
                //goal
                return goal();
            }else{
                if(calculateChance(15)){
                    //corner
                    isCorner = true;
                    return getRandomPlayer(activePlayer.getTeam(), PlayerPosition.MIDFIELDER);
                }
                //miss
                return goalkeeper;
            }
        }else{
            //pass
            return shortPassNoBlock(false);
        }
    }
    private Player corner() throws MatchSimulationException, WalkoverException {
        incrementTeamStat(activePlayer.getTeam(), Stat.CORNERS);
        isCorner = false;

        Player teammateDef = getRandomPlayer(activePlayer.getTeam(), PlayerPosition.DEFENDER);
        Player teammateFor = getRandomPlayer(activePlayer.getTeam(), PlayerPosition.FORWARD);
        Player teammateMid = null;
        do{
            teammateMid = getRandomPlayer(activePlayer.getTeam(), PlayerPosition.MIDFIELDER);
        }while(teammateMid.getId().equals(activePlayer.getId()));

        List<Player> possibleTeammates = new ArrayList<>();
        possibleTeammates.add(teammateDef);
        possibleTeammates.add(teammateFor);
        possibleTeammates.add(teammateMid);

        int random = (int) (Math.random() * possibleTeammates.size());
        previousPlayer = activePlayer;
        activePlayer = possibleTeammates.get(random);

        return headerShot();
    }
    private Player action() throws MatchSimulationException, WalkoverException, InterruptedException {
        int shortPassChance = 0;
        int longPassChance = 0;
        int dribbleChance = 0;

        Thread.sleep(200);


        if(isPenalty){
            return penalty();
        }

        if(isFreeKick){
            return freekick();
        }

        if(isThrowIn){
            isThrowIn = false;
            return shortPassNoBlock(false);
        }

        if(isCorner){
            return corner();
        }

        if(gameResume){
            return shortPassNoBlock(true);
        }

        switch (activePlayer.getPosition()) {
            case "goalkeeper":
                shortPassChance = 80;
                longPassChance = 20;
                break;
            case "defender":
                shortPassChance = 70;
                longPassChance = 30;
                break;
            case "midfielder":
                shortPassChance = 40;
                dribbleChance = 20;
                longPassChance = 30;
                break;
            case "forward":
                shortPassChance = 30;
                dribbleChance = 20;
                longPassChance = 30;
                break;
            default:
                throw new MatchSimulationException("");
        }

        int random = (int) (Math.random() * 100);
        if (random <= shortPassChance) {
            System.out.println("SHORT PASS");
            return shortPass();
        } else if (random <= shortPassChance + longPassChance) {
            System.out.println("LONG PASS");
            return longPass();
        } else if (random <= shortPassChance + longPassChance + dribbleChance) {
            System.out.println("DRIBBLE");
            return dribble();
        } else {
            System.out.println("SHOOT");
            return shot();
        }
    }

    public void simulateMatch(){
        try {
            checkIfTeamDontHaveEnoughPlayers(homeTeam);
            checkIfTeamDontHaveEnoughPlayers(awayTeam);

            if (homeTeam.getCaptain() == null || awayTeam.getCaptain() == null) {
                logger.error("Captain of one of the teams is null");
                throw new MatchSimulationException("Captain of one of the teams is null");
            }

            activePlayer = homeTeam.getCaptain();
            InitializePlayerSkills();
            InitializePlayerStat();
            InitializeDefaultVariables();
            InitializeMatchStats();

            String beg = "Match start";
            saveEvents(null,EventType.Beginning,beg);

            while(minute <= 45){
                for(int i=0; i<5; i++){
                    activePlayer=action();
                    if(activePlayer.getTeam().getId().equals(homeTeam.getId())){
                        homeTeamPossessionTime++;
                    }else{
                        awayTeamPossessionTime++;
                    }
                }
                minute++;
            }
            InitializeDefaultVariables();
            activePlayer=awayTeam.getCaptain();
            String mid = "Half-time";
            saveEvents(null,EventType.HalfTime,mid);
            while(minute<=90){
                for(int i=0; i<5; i++){
                    activePlayer=action();
                    if(activePlayer.getTeam().getId().equals(homeTeam.getId())){
                        homeTeamPossessionTime++;
                    }else{
                        awayTeamPossessionTime++;
                    }
                }
                minute++;
            }
            int sum = homeTeamPossessionTime+awayTeamPossessionTime;
            double homeTeamPossessionPercent = (double) homeTeamPossessionTime / sum * 100;
            double awayTeamPossessionPercent = (double) awayTeamPossessionTime / sum * 100;
            homeTeamStats.setPossession((int) homeTeamPossessionPercent);
            awayTeamStats.setPossession((int) awayTeamPossessionPercent);
            String end = "Match ended";
            saveEvents(null,EventType.End,end);
            saveAll();
        }catch (WalkoverException e ){
            logger.info(e.getMessage());
        }catch (MatchSimulationException | InterruptedException mE){
            logger.error(mE.getMessage());
        } finally {
            saveAll();
        }
    }
}