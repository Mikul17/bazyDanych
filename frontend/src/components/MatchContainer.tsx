"use client";
import paletteProvider from "@/constants/color-palette";
import {
  Box,
  Button,
  Card,
  CardContent,
  Typography,
} from "@mui/material";
import { CircleRounded } from "@mui/icons-material";
import { useState } from "react";
import BetButton from "./BetButton";

type MatchContainerProps = {
  homeTeam:string,
  awayTeam:string,
  matchDate:string,
  matchTime:string
};

const MatchContainer = (props: MatchContainerProps) => {
  const palette = paletteProvider();
  const [isLive, setisLive] = useState<boolean>(false);


  return (
    <Box sx={{ width: "80%", margin: "3rem 10% 3rem 10%"}}>
  <Card sx={{borderRadius:"1rem", backgroundColor:palette.primary.light}}>
      <CardContent>
        <Box>
          {isLive ? (
            <Box
              sx={{
                display: "flex",
                alignItems: "center",
                justifyContent: "flex-start",
              }}
            >
              <CircleRounded
                sx={{ color: palette.error.main, marginRight: 1 }}
              />
              <Typography variant="h6" color="error">
                Live
              </Typography>
            </Box>
          ) : (
            <Typography color={palette.text.light} gutterBottom>
              {props.matchDate}
            </Typography>
          )}
        </Box>

        <Box display={"flex"} justifyContent={"space-between"} alignItems={"center"}> 
          <Box>
            <Typography variant="h5" fontWeight={"bold"} color={palette.text.secondary}>
              {props.homeTeam} - {props.matchTime} - {props.awayTeam}
            </Typography>
          </Box>

          <Box sx={{display:"flex", alignItems:"center"}}>
            <BetButton betName="Home" betOdds={1.5} />
            <BetButton betName="Draw" betOdds={2} />
            <BetButton betName="Away" betOdds={1.5}/>
          </Box>
        </Box>
      </CardContent>
  </Card>
</Box>
  );
};

export default MatchContainer;