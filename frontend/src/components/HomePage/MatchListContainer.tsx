"use client";
import {
  Box,
  Button,
  ButtonGroup,
  Card,
  Container,
  Typography,
  alpha,
} from "@mui/material";
import MatchContainer from "./MatchContainer";
import paletteProvider from "@/constants/color-palette";
import { useEffect, useState } from "react";
import { Match } from "@/constants/Types";

const MatchListContainer = () => {
  const palette = paletteProvider();
  const [activeKey, setActiveKey] = useState("todays");
  const [matches, setMatches] = useState<Match[]>([]);

  const cardStyle = {
    display: "flex",
    flexDirection: "column",
    justifyContent: "space-between",
    alignItems: "center",
    backgroundColor: palette.primary.light,
    padding: "2rem 1rem 0 1rem",
    borderRadius: "1.25rem",
    marginTop: "1rem",
    minHeight: "75vh",
    maxHeight: "75vh",
    overflow: "scroll",
    "&::-webkit-scrollbar": {
      display: "none",
    },
  };

  const buttonStyle = {
    width: "20%",
    margin: "0.5rem 0rem",
    borderRadius: "0.5rem",
    backgroundColor: palette.primary.light,
    fontWeight: "bold",
    color: palette.text.secondary,
    "&:hover": {
      backgroundColor: alpha(palette.primary.light, 0.75),
    },
    "&:disabled": {
      backgroundColor: palette.primary.main,
      color: palette.text.secondary,
    },
  };

  const fetchMatches = async (key: string) => {
    const url = `http://localhost:8080/api/match/${key}`;
    const requestOptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    };

    try {
      const response = await fetch(url, requestOptions);
      const data: Match[] = await response.json();
      if (!response.ok) {
        throw new Error("Error while fetching matches");
      } else {
        setMatches(data);
      }
    } catch (error) {
      console.log(error);
      return {} as Match[];
    }
  };

  useEffect(() => {
    fetchMatches(activeKey);
  }, [activeKey]);

  const handleButtonClick = (key: string) => {
    setActiveKey(key);
  };

  const isActive = (key: string) => activeKey === key;

  return (
    <Container maxWidth="md">
      {/*Buttons section*/}
      <Box
        display="flex"
        justifyContent="space-around"
        alignItems="center"
        mt={"1rem"}
      >
        {["todays", "tomorrows", "upcoming"].map((key) => (
          <Button
            key={key}
            variant="contained"
            color={isActive(key) ? "secondary" : "primary"}
            onClick={() => handleButtonClick(key)}
            disabled={isActive(key)}
            sx={{
              ...buttonStyle,
              mx: 2,
              ...(isActive(key) && { backgroundColor: palette.error.main }),
            }}
          >
            {key.charAt(0).toUpperCase() + key.slice(1)}
          </Button>
        ))}
      </Box>

      {/*Matches section*/}

      <Card sx={cardStyle}>
        {matches.length === 0 && (
          <Typography
            fontWeight={"bold"}
            fontSize={"2rem"}
            color={palette.text.secondary}
            sx={{ mt: 2 }}
          >
            No matches scheduled for today
          </Typography>
        )}
        {matches.map((match) => (
          <MatchContainer key={match.id} match={match} />
        ))}
      </Card>
      {/* <Card sx={cardStyle}>
        <MatchContainer match={{
          id: 1,
          homeTeam: "homeTeam",
          awayTeam: "awayTeam",
          league: "league",
          matchDate: new Date("2024-10-10T10:10:10.000Z"),
        }} />
      </Card> */}
    </Container>
  );
};

export default MatchListContainer;
