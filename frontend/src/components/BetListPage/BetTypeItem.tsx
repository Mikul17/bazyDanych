"use client";
import { headerStyle } from "@/constants/Styles";
import paletteProvider from "@/constants/color-palette";
import { Box, Grid, Typography } from "@mui/material";
import BetButton from "../HomePage/BetButton";
import { Bet, BetType } from "@/constants/Types";
import { useState } from "react";

interface BetTypeItemProps {
  bets: BetType[];
}


const BetTypeItem = (props:BetTypeItemProps) => {
  const palette = paletteProvider();
  const betStat = props.bets[0].betStat? props.bets[0].betStat : "";
  const betType = props.bets[0].betTypeCode? props.bets[0].betTypeCode : "";
  const betTeam = props.bets[0].team? props.bets[0].team : "";
  const [selectedBet, setSelectedBet] = useState<Bet>({} as Bet);

  console.log(props.bets)
  console.log(props.bets[0].betStat)

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
    width: '100%',
    justifyContent: 'center', 
    alignItems: 'center',
    mt: '0.5rem',
  }

  return (
    <Box sx={betStyle}>
      <Box sx={betHeaderStyle}>
        <Typography
          color={palette.text.secondary}
          fontWeight={"bold"}
          fontSize={"1.5rem"}
          ml={"1rem"}
        >
            {betStat} - {betType} - {betTeam}
        </Typography>
      </Box>
      <Grid container spacing={2} sx={gridContainerStyle}>
        {props.bets.map((bet, index) => (
           <Grid item xs={4} key={index} display="flex" justifyContent="center">
            <BetButton betName={bet.targetValue.toFixed(2)} betOdds={bet.team} />
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default BetTypeItem;
