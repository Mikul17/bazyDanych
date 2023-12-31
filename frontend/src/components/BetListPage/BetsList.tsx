"use client";
import { Bet, Match } from "@/constants/Types";
import paletteProvider from "@/constants/color-palette";
import { Box, Container, Grid, Typography } from "@mui/material";
import BetTypeItem from "./BetTypeItem";
import { useEffect, useState } from "react";
import { jwtDecode } from "jwt-decode";

interface BetsListProps {
  match: Match;
}

interface GroupedBets {
  [key: string]: Bet[];
}

const BetsList = (props: BetsListProps) => {
  const palette = paletteProvider();
  const [groupedBets, setGroupedBets] = useState<GroupedBets>(
    {} as GroupedBets
  );

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
    if(betType.betStat === "score"){
      return `Who will win?`;
    }
     else if (
      betType.betTypeCode === "direct" &&
      betType.betStat !== "score"
    ) {
      return `Will there be a ${
        betType.betStat === "penalties" ? "penalty" : "red card"
      }?`;
    } else {
      return `${getTeamFromBetType(betType.team)} - ${betType.betStat} - ${betType.betTypeCode}`;
    }
  };

  useEffect(() => {
    const fetchBets = async () => {
      try {
        const token = sessionStorage.getItem("token");
        if (!token) {
          throw new Error("Token not found");
        }
        const url = `http://localhost:8080/api/bet/match/${props.match.id}`;
        const response = await fetch(url, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });
        if (!response.ok) {
          throw new Error("Failed to fetch bets");
        } else {
          const bets = await response.json();
          const betsGroups:GroupedBets = {};
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

  return (
    <Container>
      <Box display={"flex"} justifyContent={"center"} alignItems={"center"}>
        <Typography
          fontWeight={"bold"}
          fontSize={"1.5rem"}
          sx={matchHeaderStyle}
        >
          {props.match.homeTeam} - {props.match.awayTeam}
        </Typography>
      </Box>
      <Grid
        container
        spacing={1}
        justifyContent={"space-between"}
        overflow={"scroll"}
        maxHeight={"70vh"}
        marginTop={"1rem"}
        sx={{ "&::-webkit-scrollbar": { display: "none" }, scrollBehavior: "smooth"}}
      >
        {Object.keys(groupedBets).map((key, index) => (
          <Grid item xs={6} key={index}>
            <BetTypeItem key={key} desc={key} bets={groupedBets[key]} />
          </Grid>
        ))}
      </Grid>
    </Container>
  );
};

export default BetsList;
