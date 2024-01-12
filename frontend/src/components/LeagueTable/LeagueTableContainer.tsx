"use client";
import { buttonStyle } from "@/constants/Styles";
import { League, Match, Team } from "@/constants/Types";
import paletteProvider from "@/constants/color-palette";
import { Box, Button, Container, Typography, alpha } from "@mui/material";
import { useEffect, useState } from "react";
import LeagueTable from "./LeagueTable";




const LeagueTableContainer = () => {
    const palette = paletteProvider();
    const [activeKey, setActiveKey] = useState<number>(-1);
    const [leagueTable, setLeagueTable] = useState<Team[]>([]);
    const [leagues, setLeagues] = useState<League[]>([]);

    const fetchLeagues = async () => {
        const url = `http://localhost:8080/api/league/all`;
        const requestOptions = {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        };

        try{
            const response = await fetch(url, requestOptions);
            if(response.ok){
                setLeagues(await response.json());
            } else {
                throw new Error("Something went wrong");
            }
        }catch(error){
            console.error(error);
        }
    }

    const fetchLeagueTable = async (leagueId: number) => {
        const url = `http://localhost:8080/api/team/all/league/order/${leagueId}`;
        const requestOptions = {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        };

        try{
            const response = await fetch(url, requestOptions);
            if(response.ok){
                setLeagueTable(await response.json());
            } else {
                throw new Error("Something went wrong");
            }
        }catch(error){
            console.error(error);
        }
    }


    const updatedButtonStyle = {
        maxWidth: "20%",
        margin: "0.5rem 0rem",
        borderRadius: "0.5rem",
        backgroundColor: palette.primary.light,
        fontWeight: "bold",
        color: palette.text.secondary,
        padding: "0.5rem 1rem",
        mx: 2,
        textAlign: "center",
        "&:hover": {
          backgroundColor: alpha(palette.primary.light, 0.75),
        },
        "&:disabled": {
          backgroundColor: palette.primary.main,
          color: palette.text.secondary,
        },
      };

      const infoStyle = {
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
        padding: "1rem",
        borderRadius: "1rem",
        backgroundColor: palette.primary.light,
        marginTop: "1rem",
      };

    useEffect(() => {
        fetchLeagues();
    }, []);


    const isActive = (key: number) => activeKey === key;

    const handleButtonClick = (id: number): void => {
       setActiveKey(id);
       fetchLeagueTable(id);
       
    }

    const generatePageContent = () => {
        if(activeKey === -1){
            return (
                <Box sx={infoStyle}>
                    <Typography color={palette.text.secondary} fontWeight={"bold"} fontSize={"2rem"}>Select league first</Typography>
                </Box>
            )
        }else{
            return (
                <LeagueTable teams={leagueTable}/>
            )
        }
    }

    return (
        <Container maxWidth="lg">
            <Box
            display="flex"
            justifyContent={"space-around"}
            alignItems={"center"}
            mt={"1rem"}
            width={"100%"}
            >
                {leagues.map((league) => (
                    <Button
                    key={league.id}
                    variant="contained"
                    color={isActive(league.id) ? "secondary" : "primary"}
                    onClick={() => handleButtonClick(league.id)}
                    disabled={isActive(league.id)}
                    sx={{
                        ...updatedButtonStyle,
                        ...(isActive(league.id) && { backgroundColor: palette.error.main }),
                    }}
                    >
                        {league.leagueName}
                    </Button>
                ))}
            </Box>

            {generatePageContent()}
        </Container>
    )
}

export default LeagueTableContainer;    