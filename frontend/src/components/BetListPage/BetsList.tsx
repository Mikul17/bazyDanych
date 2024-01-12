"use client";
import { Bet, Match } from "@/constants/Types";
import paletteProvider from "@/constants/color-palette";
import { Box, Container, Grid, Link, Typography } from "@mui/material";
import BetTypeItem from "./BetTypeItem";
import { useEffect, useState } from "react";
import { jwtDecode } from "jwt-decode";
import MatchHistoryModal from "../MatchHistory/MatchHistoryModal";

interface BetsListProps {
  matchId: number;
}

interface GroupedBets {
  [key: string]: Bet[];
}

const BetsList = (props: BetsListProps) => {
  const palette = paletteProvider();
  const [groupedBets, setGroupedBets] = useState<GroupedBets>(
    {} as GroupedBets
  );
  const [openModal, setOpenModal] = useState<boolean>(false);
  const [isHomeTeam, setIsHomeTeam] = useState<boolean>(false);
  const [teamName, setTeamName] = useState<string>("");
  const [match, setMatch] = useState<Match>({} as Match);

  const getTeamFromBetType = (team: number) => {
    switch (team) {
      case 0:
        return "Home team";
      case 1:
        return "Away team";
      case 2:
        return "Both teams";
    }
  };

  const matchTableKey = (bet: Bet) => {
    const { betType } = bet;
    if (betType.betStat === "score") {
      return `Who will win?`;
    } else if (
      betType.betTypeCode === "direct" &&
      betType.betStat !== "score"
    ) {
      return `Will there be a ${
        betType.betStat === "penalties" ? "penalty" : "red card"
      }?`;
    } else {
      return `${getTeamFromBetType(betType.team)} - ${betType.betStat} - ${
        betType.betTypeCode
      }`;
    }
  };

  const fetchMatch = async () => {
    const url = `http://localhost:8080/api/match/${props.matchId}`;
    try {
      const response = await fetch(url, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });
      if (!response.ok) {
        throw new Error("Failed to fetch match");
      } else {
        const match = await response.json();
        setMatch(match);
      }
    } catch (error) {
      console.error("Failed to fetch match:", error);
    }
  };

  const fetchBets = async () => {
    try {
      const url = `http://localhost:8080/api/bet/match/${props.matchId}`;
      const response = await fetch(url, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });
      if (!response.ok) {
        throw new Error("Failed to fetch bets");
      } else {
        const bets = await response.json();
        const betsGroups: GroupedBets = {};
        bets.forEach((bet: Bet) => {
          const key = matchTableKey(bet);
          if (!betsGroups[key]) {
            betsGroups[key] = [];
          }
          betsGroups[key].push(bet);
        });
        setGroupedBets(betsGroups);
      }
    } catch (error) {
      console.error("Failed to fetch bets:", error);
    }
  };

  useEffect(() => {
    fetchMatch();
    fetchBets();
  }, []);

  const matchHeaderStyle = {
    display: "inline-flex",
    textAlign: "center",
    backgroundColor: palette.primary.light,
    color: palette.text.secondary,
    padding: "0.5rem 2rem",
    borderRadius: "1rem",
  };

  const linkStyle = {
    color: palette.text.secondary,
    cursor: "pointer",
    textDecoration: "none",
    "&:hover": {
      color: palette.primary.main,
      textDecoration: "none",
    },
  };

  const handleOpenModal = (isHomeTeam: boolean) => {
    setOpenModal(true);
    setIsHomeTeam(isHomeTeam);
    setTeamName(isHomeTeam ? match.homeTeam : match.awayTeam);
  };

  return (
    <Container>
      <Box display={"flex"} justifyContent={"center"} alignItems={"center"}>
        <Typography
          fontWeight={"bold"}
          fontSize={"1.5rem"}
          sx={matchHeaderStyle}
        >
          <Link
            onClick={() => handleOpenModal(true)}
            sx={linkStyle}
            mr={"1rem"}
          >
            {match.homeTeam}
          </Link>
          -
          <Link
            onClick={() => handleOpenModal(false)}
            ml={"1rem"}
            sx={linkStyle}
          >
            {match.awayTeam}
          </Link>
        </Typography>
      </Box>
      <Grid
        container
        spacing={1}
        justifyContent={"space-between"}
        overflow={"scroll"}
        maxHeight={"70vh"}
        marginTop={"1rem"}
        sx={{
          "&::-webkit-scrollbar": { display: "none" },
          scrollBehavior: "smooth",
        }}
      >
        {Object.keys(groupedBets).map((key, index) => (
          <Grid item xs={6} key={index}>
            <BetTypeItem key={key} desc={key} bets={groupedBets[key]} />
          </Grid>
        ))}
      </Grid>
      <MatchHistoryModal
        openModal={openModal}
        handleCloseModal={() => setOpenModal(false)}
        matchId={match.id}
        isHomeTeam={isHomeTeam}
        teamName={teamName}
      />
    </Container>
  );
};

export default BetsList;
