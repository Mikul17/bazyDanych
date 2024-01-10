"use client";
import paletteProvider from "@/constants/color-palette";
import {
  Box,
  Button,
  Card,
  CardActionArea,
  CardContent,
  Typography,
} from "@mui/material";
import { CircleRounded } from "@mui/icons-material";
import { use, useEffect, useState } from "react";
import BetButton from "./BetButton";
import { Match } from "@/constants/Types";
import { match } from "assert";
import { useRouter } from "next/navigation";

type MatchContainerProps = {
  match: Match;
};

const MatchContainer = (props: MatchContainerProps) => {
  const palette = paletteProvider();
  const [isLive, setisLive] = useState<boolean>(false);
  const router = useRouter();

  const teamNameStyle = {
    color: palette.text.secondary,
    fontWeight: "bold",
    fontSize: "1.75rem",
  };

  const cardStyle = {
    backgroundColor: palette.primary.main,
    padding: "1rem 0.5rem",
    borderRadius: "1.25rem",
    marginTop: "1rem",
    width: "85%",
  };

  const checkIfLive = () => {
    if (props.match.matchDate < new Date()) {
      setisLive(true);
    }
  };

  const getDateString = (date: Date) => {
    return date.toLocaleDateString("en-GB", {
      day: "2-digit",
      month: "short",
      year: "numeric",
    });
  };

  const getTimeString = (date: Date) => {
    date.setHours(date.getHours() - 2);
    return date.toLocaleTimeString("en-GB", {
      hour: "2-digit",
      minute: "2-digit",
      hour12: false,
    });
  };

  const handleCardClick = () => {
    isLive ? router.push(`/match/${props.match.id}`) : router.push(`/bets/${props.match.id}`)
  };

  useEffect(() => {
    checkIfLive();
  }, []);

  return (
    <Card sx={cardStyle}>
      <CardActionArea onClick={handleCardClick}>
        <Box display={"flex"} justifyContent={"center"} alignItems={"center"} flexDirection={"column"}>
          <Typography color={palette.text.light}>
            {getDateString(new Date(props.match.matchDate))}
          </Typography>
          <Typography fontWeight={"bold"} color={palette.text.light}>
            {props.match.league}
          </Typography>
        </Box>
        <Box display={"flex"} justifyContent={"space-around"} alignItems={"center"} mt={"1rem"}>
          <Typography textAlign={"start"} sx={teamNameStyle}>{props.match.homeTeam}</Typography>
          {isLive ? (
            <Box
              display={"flex"}
              justifyContent={"center"}
              alignItems={"center"}
              margin={"0 0.5rem"}
            >
              <CircleRounded fontSize="medium" htmlColor={palette.error.main} />
              <Typography fontSize={"1.5rem"} color={palette.error.main}>Live</Typography>
            </Box>
          ) : (
            <Typography  color={palette.text.light} fontSize={"1.5rem"}>
              {getTimeString(new Date(props.match.matchDate))}
            </Typography>
          )}
          <Typography sx={teamNameStyle} textAlign={"end"}>{props.match.awayTeam}</Typography>
        </Box>
      </CardActionArea>
    </Card>
  );
};

export default MatchContainer;
