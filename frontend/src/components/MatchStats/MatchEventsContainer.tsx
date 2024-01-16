"use client";
import { headerStyle } from "@/constants/Styles";
import { MatchEvent } from "@/constants/Types";
import paletteProvider from "@/constants/color-palette";
import { Box, Card, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import MatchEventItem from "./MatchEventItem";

interface MatchEventsContainerProps {
  matchId: number;
}

const MatchEventsContainer = (props: MatchEventsContainerProps) => {
  const palette = paletteProvider();
  const [events, setEvents] = useState<MatchEvent[]>([]);

  const cardStyle = {
    padding: "0.5rem 1rem 2rem 1rem",
    backgroundColor: palette.primary.light,
    borderRadius: "1.25rem",
    height: "35vh",
    width: "100%",
    overflowY: "scroll",
    "&::-webkit-scrollbar": {
      display: "none",
    },
  };

  const fetchEvents = async () => {
    try {
      const url = `http://localhost:8080/api/matchEvent/all/${props.matchId}`;
      const response = await fetch(url, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });
      if (!response.ok) {
        throw new Error("Failed to fetch events");
      } else {
        const events = await response.json();
        setEvents(events);
      }
    } catch (error) {
      console.error("Failed to fetch events:", error);
    }
  };

  useEffect(() => {
    const interval = setInterval(() => {
      fetchEvents();
    }, 5000);
    return () => clearInterval(interval);
  }, []);

  return (
    <Card sx={cardStyle}>
      <Box sx={headerStyle("center")}>
        <Typography
          fontWeight={"bold"}
          color={palette.primary.main}
          variant="h6"
        >
          Match Highlights
        </Typography>
      </Box>
      {events.map((event) => (
        <MatchEventItem key={event.id} event={event} />
      ))}
    </Card>
  );
};

export default MatchEventsContainer;
