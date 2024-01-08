"use client"
import { Bet, Match } from "@/constants/Types";
import paletteProvider from "@/constants/color-palette";
import { Box, Container, Grid, Typography } from "@mui/material";
import BetTypeItem from "./BetTypeItem";
import { useState } from "react";

interface BetsListProps {
  match: Match;
}

const BetsList = (props: BetsListProps) => {
  const palette = paletteProvider();
  const [bets, setBets] = useState<Bet[]>([]);

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
      <Grid container spacing={1} justifyContent={"space-between"} overflow={"scroll"} maxHeight={"85vh"}>
        <Grid item md={6}>
        <BetTypeItem
          bets={[
            {
              id: 1,
              betStat: "Shots on Target",
              betTypeCode: "Over",
              targetValue: 2,
              team: 1,
            },
            {
              id: 2,
              betStat: "Goals",
              betTypeCode: "Over",
              targetValue: 3,
              team: 1,
            },
            {
              id: 3,
              betStat: "Goals",
              betTypeCode: "G",
              targetValue: 4,
              team: 1,
            },
            {
              id: 4,
              betStat: "Goals",
              betTypeCode: "G",
              targetValue: 5,
              team: 1,
            },
            {
              id: 5,
              betStat: "Goals",
              betTypeCode: "G",
              targetValue: 6,
              team: 1,
            },
            {
              id: 6,
              betStat: "Goals",
              betTypeCode: "G",
              targetValue: 7,
              team: 1,
            },
            {
              id: 7,
              betStat: "Goals",
              betTypeCode: "G",
              targetValue: 8,
              team: 1,
            },
          ]}
        />
        </Grid>
        <Grid item md={6}>
          <BetTypeItem bets={[
            {
              id: 1,
              betStat: "Shots on Target",
              betTypeCode: "Over",
              targetValue: 2,
              team: 1,
            },            {
                id: 2,
                betStat: "Shots on Target",
                betTypeCode: "Over",
                targetValue: 2,
                team: 1,
              }]} />
        </Grid>
        <Grid item md={6}>
        <BetTypeItem
          bets={[
            {
              id: 1,
              betStat: "Shots on Target",
              betTypeCode: "Over",
              targetValue: 2,
              team: 1,
            },
            {
              id: 2,
              betStat: "Goals",
              betTypeCode: "Over",
              targetValue: 3,
              team: 1,
            },
            {
              id: 3,
              betStat: "Goals",
              betTypeCode: "G",
              targetValue: 4,
              team: 1,
            },
            {
              id: 4,
              betStat: "Goals",
              betTypeCode: "G",
              targetValue: 5,
              team: 1,
            },
            {
              id: 5,
              betStat: "Goals",
              betTypeCode: "G",
              targetValue: 6,
              team: 1,
            },
            {
              id: 6,
              betStat: "Goals",
              betTypeCode: "G",
              targetValue: 7,
              team: 1,
            },
            {
              id: 7,
              betStat: "Goals",
              betTypeCode: "G",
              targetValue: 8,
              team: 1,
            },
          ]}
        />
        </Grid>
        <Grid item md={6}>
          <BetTypeItem bets={[
            {
              id: 1,
              betStat: "Shots on Target",
              betTypeCode: "Over",
              targetValue: 2,
              team: 1,
            },            {
                id: 2,
                betStat: "Shots on Target",
                betTypeCode: "Over",
                targetValue: 2,
                team: 1,
              }]} />
        </Grid>
        <Grid item md={6}>
          <BetTypeItem bets={[
            {
              id: 1,
              betStat: "Shots on Target",
              betTypeCode: "Over",
              targetValue: 2,
              team: 1,
            },            {
                id: 2,
                betStat: "Shots on Target",
                betTypeCode: "Over",
                targetValue: 2,
                team: 1,
              }]} />
        </Grid>
      </Grid>
    </Container>
  );
};

export default BetsList;
