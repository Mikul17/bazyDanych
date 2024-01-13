import { headerStyle } from "@/constants/Styles";
import { Player } from "@/constants/Types";
import paletteProvider from "@/constants/color-palette";
import { Box, Card, Typography } from "@mui/material";
import { useEffect } from "react";

interface TeamContainerProps {
  players: Player[];
}

const TeamContainer = (props: TeamContainerProps) => {
  const palette = paletteProvider();

  const cardStyle = {
    maxHeight: "80vh",
    height: "auto",
    backgroundColor: palette.primary.light,
    mt: "1rem",
    borderRadius: "1.25rem",
    width: "100%",
    paddingBottom: "1rem",
  };

  const updatedHeaderStyle = {
    ...headerStyle("center"),
    borderRadius: "1.25rem",
    width: "90%",
  };

  const playerStyle = {
    fontWeight: "bold",
    fontSize: "0.75rem",
    textAlign: "center",
    color: palette.text.secondary,
  };

  const getPlayerPositionShort = (position: string) => {
    console.log(position);
    switch (position) {
      case "goalkeeper":
        return "GK";
      case "defender":
        return "DEF";
      case "midfielder":
        return "MID";
      case "forward":
        return "FWD";
      default:
        return "";
    }
  };

  return (
    <Card sx={cardStyle}>
      <Box
        display={"flex"}
        justifyContent={"center"}
        alignItems={"center"}
        flexDirection={"column"}
      >
        <Box width={"90%"} sx={headerStyle("center")}>
          <Typography
            fontWeight={"bold"}
            fontSize={"1.5rem"}
            textAlign={"center"}
          >
            Home Team
          </Typography>
        </Box>

        {/*Forwards*/}
        <Box sx={updatedHeaderStyle}>
          <Typography fontWeight={"bold"} fontSize={"0.75rem"}>
            Forwards
          </Typography>
        </Box>
        <Box>
          {props.players
            .filter((player) => player.position === "forward")
            .map((player) => {
              return (
                <Typography key={player.id} sx={playerStyle}>
                  {player.firstName} {player.lastName} (
                  {getPlayerPositionShort(player.position)})
                </Typography>
              );
            })}
        </Box>
        {/*Midfielders*/}
        <Box sx={updatedHeaderStyle}>
          <Typography fontWeight={"bold"} fontSize={"0.75rem"}>
            Midfielders
          </Typography>
        </Box>
        <Box>
        {props.players
            .filter((player) => player.position === "midfielder")
            .map((player) => {
              return (
                <Typography key={player.id} sx={playerStyle}>
                  {player.firstName} {player.lastName} (
                  {getPlayerPositionShort(player.position)})
                </Typography>
              );
            })}
        </Box>
        {/*Defenders*/}
        <Box sx={updatedHeaderStyle}>
          <Typography fontWeight={"bold"} fontSize={"0.75rem"}>
            Defenders
          </Typography>
        </Box>
        <Box>
        {props.players
            .filter((player) => player.position === "defender")
            .map((player) => {
              return (
                <Typography key={player.id} sx={playerStyle}>
                  {player.firstName} {player.lastName} (
                  {getPlayerPositionShort(player.position)})
                </Typography>
              );
            })}
        </Box>
        {/*Goalkeeper*/}
        <Box sx={updatedHeaderStyle}>
          <Typography fontWeight={"bold"} fontSize={"0.75rem"}>
            Goalkeepers
          </Typography>
        </Box>
        <Box>
        {props.players
            .filter((player) => player.position === "goalkeeper")
            .map((player) => {
              return (
                <Typography key={player.id} sx={playerStyle}>
                  {player.firstName} {player.lastName} (
                  {getPlayerPositionShort(player.position)})
                </Typography>
              );
            })}
        </Box>
        {/*Substitutes*/}
        <Box sx={updatedHeaderStyle}>

          <Typography fontWeight={"bold"} fontSize={"0.75rem"}>
            Substitutes
          </Typography>
        </Box>
        <Box>
        {props.players
            .filter((player) => player.isBenched === true)
            .map((player) => {
              return (
                <Typography key={player.id} sx={playerStyle}>
                  {player.firstName} {player.lastName} (
                  {getPlayerPositionShort(player.position)})
                </Typography>
              );
            })}
        </Box>
      </Box>
    </Card>
  );
};

export default TeamContainer;
