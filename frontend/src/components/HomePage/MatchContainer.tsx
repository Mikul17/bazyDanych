"use client";
import paletteProvider from "@/constants/color-palette";
import { Box, Card, CardActionArea, Typography } from "@mui/material";
import { CircleRounded } from "@mui/icons-material";
import { useEffect, useState } from "react";
import { Match } from "@/constants/Types";
import { useRouter } from "next/navigation";

type MatchContainerProps = {
  match: Match;
};

const MatchContainer = (props: MatchContainerProps) => {
  const palette = paletteProvider();
  const router = useRouter();
  const [matchStatus, setMatchStatus] = useState<
    "played" | "live" | "upcoming"
  >("upcoming");
  const [matchScore, setMatchScore] = useState<[number, number]>([0, 0]);

  const teamNameStyle = {
    color: palette.text.secondary,
    fontWeight: "bold",
    fontSize: "1.75rem",
  };

  const cardStyle = {
    backgroundColor: palette.primary.main,
    padding: "1rem 0.5rem",
    borderRadius: "1.25rem",
    margin: "1rem 0",
    width: "85%",
    minHeight: "15%",
  };

  const getDateString = (date: Date) => {
    return date.toLocaleDateString("en-GB", {
      day: "2-digit",
      month: "short",
      year: "numeric",
    });
  };

  const getTimeString = (date: Date) => {
    date.setHours(date.getHours());
    return date.toLocaleTimeString("en-GB", {
      hour: "2-digit",
      minute: "2-digit",
      hour12: false,
    });
  };

  const handleCardClick = () => {
    matchStatus === "upcoming"
      ? router.push(`/bets/${props.match.id}`)
      : router.push(`/match_stats/${props.match.id}`);
  };

  const checkMatchStatus = () => {
    const matchDate = new Date(props.match.matchDate);
    const currentDate = new Date();
    const livePeriodEnd = new Date(matchDate.getTime() + 2 * 60000); // 2 minutes after the match date

    if (currentDate >= matchDate && currentDate <= livePeriodEnd) {
      setMatchStatus("live");
    } else if (currentDate > livePeriodEnd) {
      setMatchStatus("played");
    } else {
      setMatchStatus("upcoming");
    }
  };

  const fetchMatchScore = async () => {
    const url = `http://localhost:8080/api/matchStat/score/${props.match.id}`;
    try {
      const response = await fetch(url);
      const data = await response.json();
      setMatchScore([data.homeTeamGoals, data.awayTeamGoals]);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    checkMatchStatus();
  }, []);

  useEffect(() => {
    if (matchStatus === "played") {
      fetchMatchScore();
    }
  }, [matchStatus]);


  const renderConditionally = () => {
    switch (matchStatus) {
      case "played":
        return (
          <>
            <Typography sx={teamNameStyle}>{matchScore[0]}</Typography>
            <Typography fontSize={"2rem"} color={palette.text.light}>
              -
            </Typography>
            <Typography sx={teamNameStyle}>{matchScore[1]}</Typography>
          </>
        );
      case "live":
        return (
          <Box
            display={"flex"}
            justifyContent={"center"}
            alignItems={"center"}
            margin={"0 0.5rem"}
          >
            <CircleRounded fontSize="medium" htmlColor={palette.error.main} />
            <Typography fontSize={"1.5rem"} color={palette.error.main}>
              Live
            </Typography>
          </Box>
        );
      case "upcoming":
        return (
          <Typography color={palette.text.light} fontSize={"1.5rem"}>
            {getTimeString(new Date(props.match.matchDate))}
          </Typography>
        );
    }
  };

  return (
    <Card sx={cardStyle}>
      <CardActionArea onClick={handleCardClick}>
        <Box
          display={"flex"}
          justifyContent={"center"}
          alignItems={"center"}
          flexDirection={"column"}
        >
          <Typography color={palette.text.light}>
            {getDateString(new Date(props.match.matchDate))}
          </Typography>
          <Typography fontWeight={"bold"} color={palette.text.light}>
            {props.match.league}
          </Typography>
        </Box>
        <Box
          display={"flex"}
          justifyContent={"space-around"}
          alignItems={"center"}
          mt={"1rem"}
        >
          <Typography textAlign={"start"} sx={teamNameStyle}>
            {props.match.homeTeam}
          </Typography>
          {renderConditionally()}
          <Typography sx={teamNameStyle} textAlign={"end"}>
            {props.match.awayTeam}
          </Typography>
        </Box>
      </CardActionArea>
    </Card>
  );
};

export default MatchContainer;
