"use client";
import { headerStyle } from "@/constants/Styles";
import paletteProvider from "@/constants/color-palette";
import { Box, Card, Typography } from "@mui/material";
import MatchStatItem from "./MatchStatItem";
import { MatchStats } from "@/constants/Types";
import { useEffect, useState } from "react";

interface MatchStatsContainerProps {
  matchId: number;
}

interface StatDetail {
  statName: string;
  homeTeamStat: number;
  awayTeamStat: number;
}

const MatchStatsContainer = (props: MatchStatsContainerProps) => {
  const palette = paletteProvider();
  const [homeTeamStats, setHomeTeamStats] = useState<MatchStats>(
    {} as MatchStats
  );
  const [awayTeamStats, setAwayTeamStats] = useState<MatchStats>(
    {} as MatchStats
  );

  const cardStyle = {
    padding: "0.5rem 1rem 2rem 1rem",
    backgroundColor: palette.primary.light,
    borderRadius: "1.25rem",
    width: "100%",
    height: "43vh",
    overflowY: "auto",
    "&::-webkit-scrollbar": {
      display: "none",
    },
  };

  const fetchHomeTeamStats = async () => {
    const url = `http://localhost:8080/api/matchStat/all/homeTeam/${props.matchId}`;
    try {
      const response = await fetch(url);
      if (!response.ok) throw new Error("Something went wrong");
      const data = await response.json();
      setHomeTeamStats(data);
    } catch (err) {
      console.error(err);
    }
  };

  const fetchAwayTeamStats = async () => {
    const url = `http://localhost:8080/api/matchStat/all/awayTeam/${props.matchId}`;
    try {
      const response = await fetch(url);
      if (!response.ok) throw new Error("Something went wrong");
      const data = await response.json();
      setAwayTeamStats(data);
    } catch (err) {
      console.error(err);
    }
  };

  const generateStatsArray = (stats: MatchStats): StatDetail[] => {
    const statDetails: StatDetail[] = [];

    for (const key of Object.keys(stats) as Array<keyof MatchStats>) {
      if (key !== "id" && key !== "matchId" && key !== "teamName") {
        const statName = key
          .replace(/_/g, " ")
          .split(/(?=[A-Z])/)
          .map((word, index) =>
            index === 0
              ? word.charAt(0).toUpperCase() + word.slice(1).toLowerCase()
              : word.toLowerCase()
          )
          .join(" ")
          .trim();
        statDetails.push({
          statName,
          homeTeamStat: stats[key],
          awayTeamStat: awayTeamStats ? awayTeamStats[key] : 0,
        });
      }
    }

    return statDetails;
  };

  useEffect(() => {
    fetchHomeTeamStats();
    fetchAwayTeamStats();
  }, []);

  return (
    <Card sx={{ ...cardStyle, mt: "1rem" }}>
      <Box sx={headerStyle("space-between")}>
        <Typography
          color={palette.primary.main}
          fontWeight={"bold"}
          variant="h6"
        >
          Home team
        </Typography>
        <Typography
          color={palette.primary.main}
          fontWeight={"bold"}
          variant="h6"
        >
          Match statistics
        </Typography>
        <Typography
          color={palette.primary.main}
          fontWeight={"bold"}
          variant="h6"
        >
          Away team
        </Typography>
      </Box>
      {homeTeamStats &&
        awayTeamStats &&
        generateStatsArray(homeTeamStats).map((stat, index) => (
          <MatchStatItem
            key={index}
            statName={
              stat.statName.charAt(0).toUpperCase() + stat.statName.slice(1)
            }
            homeTeamStat={stat.homeTeamStat}
            awayTeamStat={stat.awayTeamStat}
          />
        ))}
      <Box></Box>
    </Card>
  );
};

export default MatchStatsContainer;
