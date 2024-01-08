"use client";
import { headerStyle } from "@/constants/Styles";
import paletteProvider from "@/constants/color-palette";
import { Box, Grid, Typography } from "@mui/material";
import BetButton from "../HomePage/BetButton";
import { Bet } from "@/constants/Types";
import { useEffect, useState } from "react";

interface BetTypeItemProps {
  bets: Bet[];
  desc: string;
}

const BetTypeItem = (props: BetTypeItemProps) => {
  const palette = paletteProvider();
  const [selectedBet, setSelectedBet] = useState<Bet | null>(null);

  const betHeaderStyle = {
    ...headerStyle("none"),
    backgroundColor: palette.primary.light,
  };

  const betStyle = {
    backgroundColor: palette.primary.main,
    padding: "0.5rem 1rem",
    borderRadius: "1.25rem",
    marginTop: "3rem",
    maxWidth: "70%",
  };

  const gridContainerStyle = {
    width: "100%",
    justifyContent: "center",
    alignItems: "center",
    mt: "0.5rem",
  };

  const getBetName = (bet: Bet) => {
    const { betType } = bet;
    if (betType.betStat === "score" && betType.targetValue !== 1.0) {
      switch (betType.team) { 
        case 0:
          return "Home";
        case 1:
          return "Away";
        case 2:
          return "Draw";
        default:
          throw new Error("Invalid bet type");
      }
    }else if (betType.betStat === "score" && betType.targetValue === 1.0) {
      switch (betType.team) { 
        case 0:
          return "Home/Draw";
        case 1:
          return "Away/Draw";
        default:
          throw new Error("Invalid bet type");
    }
  }else if (betType.betStat === "penalties" || betType.betStat === "redCards") {
      switch (betType.targetValue) {
        case 0:
          return "No";
        case 1:
          return "Yes";
        default:
          throw new Error("Invalid bet type");
      }
    }
    return betType.targetValue.toFixed(2);
  };

  const compareBets = (bet1: Bet, bet2: Bet) => {
    const { betType } = bet1;
    const { betType: betType2 } = bet2;

    return (
          betType.betStat === betType2.betStat &&
          betType.betTypeCode === betType2.betTypeCode &&
          betType.team === betType2.team &&
          betType.targetValue === betType2.targetValue
        );
  };

  useEffect(() => {
    // Load the selected bet from local storage when the component mounts
    const bets = JSON.parse(localStorage.getItem("bets") || "[]");
    const betTypeSelected = bets.find((bet: Bet) =>
      compareBets(bet, props.bets[0])
    );
    setSelectedBet(betTypeSelected || null);
  }, [props.bets]);

  const handleBetSelected = (bet: Bet) => {
    setSelectedBet(bet);
    // Save the selected bet to local storage
    const bets = JSON.parse(localStorage.getItem("bets") || "[]");
    const otherBets = bets.filter((b: Bet) => compareBets(b, bet) === false);
    localStorage.setItem("bets", JSON.stringify([...otherBets, bet]));
  };

  const isBetSelected = (bet: Bet) => {
    return selectedBet ? compareBets(selectedBet, bet) : false;
  };

  const sortBets = (bets: Bet[]) => {
    if (bets.some(bet => bet.betType.betTypeCode === "over" || bet.betType.betTypeCode === "under")) {
      return bets.sort((a, b) => a.betType.targetValue - b.betType.targetValue);
    }
    else if (bets.some(bet => bet.betType.betStat === 'score')) {
      const scoreOrder = ['Home', 'Draw', 'Away', 'Home/Draw', 'Away/Draw'];
      return bets.sort((a, b) => scoreOrder.indexOf(getBetName(a)) - scoreOrder.indexOf(getBetName(b)));
    }
    else if (bets.some(bet => bet.betType.betTypeCode === 'direct')) {
      const directOrder = ['Yes', 'No'];
      return bets.sort((a, b) => directOrder.indexOf(getBetName(a)) - directOrder.indexOf(getBetName(b)));
    }

    return bets;
  };

  return (
    <Box sx={betStyle}>
      <Box sx={betHeaderStyle}>
        <Typography
          color={palette.text.secondary}
          fontWeight={"bold"}
          fontSize={"1.5rem"}
          ml={"1rem"}
        >
          {props.desc}
        </Typography>
      </Box>
      <Grid container spacing={1} sx={gridContainerStyle}>
      {sortBets(props.bets).map((bet, index) => (
          <Grid item xs={4} key={index} display="flex" justifyContent="center">
            <BetButton
              bet={bet}
              betName={getBetName(bet)}
              isSelected={isBetSelected(bet)}
              onBetSelected={handleBetSelected}
            />
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default BetTypeItem;
