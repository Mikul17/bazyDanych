"use client";
import { Match, Player } from "@/constants/Types";
import paletteProvider from "@/constants/color-palette";
import { Box, Container, Grid, Link, Typography } from "@mui/material";
import { use, useEffect, useState } from "react";
import TeamContainer from "./TeamContainer";

interface TeamsSectionProps {
    matchId: number;
}

const TeamsSection = (props: TeamsSectionProps) => {
    const palette = paletteProvider();
    const [match, setMatch] = useState<Match>({} as Match);
    const [homeTeamPlayers, setHomeTeamPlayers] = useState<Player[]>([]);
    const [awayTeamPlayers, setAwayTeamPlayers] = useState<Player[]>([]);

    const matchHeaderStyle = {
        backgroundColor: palette.primary.light,
        color: palette.text.secondary,
        padding: "0.5rem 2rem",
        borderRadius: "1rem",
        width:"100%",
      };

      const fetchMatch = async () => {
        const url = `http://localhost:8080/api/match/${props.matchId}`;
        try {
          const response = await fetch(url, {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
            },
          });
          if (!response.ok) {
            throw new Error("Failed to fetch match");
          } else {
            const match = await response.json();
            setMatch(match);
          }
        } catch (error) {
          console.error("Failed to fetch match:", error);
        }
      };

      const fetchPlayers = async (name:string) => {
        if(!name || name === undefined) return;
        const url = `http://localhost:8080/api/player/all/name/${name}`;
        try{
            const response = await fetch(url);
            if(!response.ok) throw new Error("Something went wrong");
            const data = await response.json();
            if(name === match.homeTeam){
                setHomeTeamPlayers(data);
            }else{
                setAwayTeamPlayers(data);
            }
        }catch(err){
            console.error(err);
        }
      };
      
      useEffect(() => {
        fetchMatch();
      },[]);  

      useEffect(() => {
        fetchPlayers(match.homeTeam);
        fetchPlayers(match.awayTeam);
      }, [match.homeTeam, match.awayTeam]);

    
    return (
    <Container maxWidth="xl">
        <Typography
          fontWeight={"bold"}
          fontSize={"1.5rem"}
          textAlign={"center"}
          sx={matchHeaderStyle}
        >
            {match.homeTeam} - {match.awayTeam}
        </Typography>
        <Grid container spacing={3} justifyContent="space-around" alignItems={"flex-start"}>
        <Grid item xs={12} md={5}>
                <TeamContainer players={homeTeamPlayers} />
            </Grid>
            <Grid item xs={12} md={5}>
                <TeamContainer players={awayTeamPlayers}/>
            </Grid>
        </Grid>
        </Container>
    )
}

export default TeamsSection;