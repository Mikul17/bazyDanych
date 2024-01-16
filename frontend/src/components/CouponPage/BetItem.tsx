"use client";
import { Bet } from "@/constants/Types";
import paletteProvider from "@/constants/color-palette";
import { useBets } from "@/context/CouponBetsContext";
import { RemoveOutlined } from "@mui/icons-material";
import { Box, Grid, IconButton, Typography } from "@mui/material";
import { useEffect, useState } from "react";

interface BetItemProps {
  bet: Bet;
  isDeleteable: boolean;
}

const BetItem = (props: BetItemProps) => {
  const palette = paletteProvider();
  const [betCategory, setBetCategory] = useState({ selectedBet: "", desc: "" });
  const { removeBet } = useBets();

  const betItemContainer = {
    padding: "0.5rem 1rem 0.5rem 1rem",
    backgroundColor: palette.primary.main,
    color: palette.text.secondary,
    borderRadius: "1.25rem",
    height: "10vh",
    maxWidth: "80%",
    minWidth: "50%",
    margin: "0.5rem 0",
  };

  const betOddsStyle = {
    padding: "0.5rem",
    backgroundColor: palette.text.secondary,
    color: palette.text.primary,
    borderRadius: "1.25rem",
    textAlign: "center",
    fontWeight: "bold",
  };

  const getTeamPart = (team: number) => {
    switch (team) {
      case 0:
        return "home team";
      case 1:
        return "away team";
      case 2:
        return "both teams";
      default:
        throw "Invalid team number";
    }
  };

  const displayWhatBettedOn = () => {
    if (props.bet.betType.betStat === "score") {
      if (props.bet.betType.team === 2) {
        setBetCategory({ selectedBet: "Draw", desc: "score" });
      } else {
        if (props.bet.betType.targetValue === 0) {
          setBetCategory({
            selectedBet:
              props.bet.betType.team === 0
                ? props.bet.homeTeam
                : props.bet.awayTeam,
            desc: "score",
          });
        } else {
          setBetCategory({
            selectedBet:
              props.bet.betType.team === 0
                ? `${props.bet.homeTeam} \\ Draw`
                : `${props.bet.awayTeam} \\ Draw`,
            desc: "score",
          });
        }
      }
    } else {
      if (props.bet.betType.betTypeCode === "direct") {
        setBetCategory({
          selectedBet: props.bet.betType.targetValue === 1 ? "Yes" : "No",
          desc:
            props.bet.betType.betStat === "penalties"
              ? "penalties"
              : "red card",
        });
      } else {
        setBetCategory({
          selectedBet: `${props.bet.betType.betTypeCode}  ${props.bet.betType.targetValue}`,
          desc: `${getTeamPart(props.bet.betType.team)} - ${
            props.bet.betType.betStat
          }`,
        });
      }
    }
  };

  const handleBetRemoval = () => {
    removeBet(props.bet.id);
  };

  useEffect(() => {
    displayWhatBettedOn();
  }, [props.bet]);

  return (
    <Box sx={betItemContainer}>
      <Box
        display={"flex"}
        justifyContent={"flex-start"}
        alignItems={"center"}
        width={"auto"}
      >
        <Typography fontSize={"1.25rem"} fontWeight={"bold"}>
          {props.bet.homeTeam} - {props.bet.awayTeam}
        </Typography>
        {props.isDeleteable && (
          <IconButton
            onClick={handleBetRemoval}
            size="small"
            sx={{ ml: "1rem" }}
          >
            <RemoveOutlined htmlColor={palette.text.secondary} />
          </IconButton>
        )}
      </Box>
      <Grid container mt={"0.25rem"}>
        <Grid item xs={7}>
          <Box>
            <Typography fontSize={"1rem"} fontWeight={"bold"}>
              {betCategory.selectedBet}
            </Typography>
            <Typography fontSize={"0.75rem"}>{betCategory.desc}</Typography>
          </Box>
        </Grid>
        <Grid item xs={3}>
          <Typography sx={betOddsStyle}>{props.bet.odds}</Typography>
        </Grid>
      </Grid>
    </Box>
  );
};

export default BetItem;
