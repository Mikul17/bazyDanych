import { Match, MatchHistory } from "@/constants/Types";
import paletteProvider from "@/constants/color-palette";
import { Box, Typography } from "@mui/material";

interface MatchHistoryItemProps {
  match: MatchHistory;
  isHomeTeam: boolean;
}

const MatchHistoryItem = (props: MatchHistoryItemProps) => {
  const palette = paletteProvider();

  const itemStyle = {
    width:"90%",
    backgroundColor: palette.text.secondary,
    borderRadius: "1rem",
    marginBottom: "1rem",
  };

  const textStyle = {
    fontSize: "1.25rem",
    fontWeight: "600",
    padding: "0.5rem",
  }

  const getTextColor = () => {
    if (props.match.homeTeamScore > props.match.awayTeamScore) {
      return props.isHomeTeam ? palette.primary.light : palette.error.main;
    } else if (props.match.homeTeamScore === props.match.awayTeamScore) {
      return palette.text.primary;
    } else {
      return props.isHomeTeam ? palette.error.main : palette.primary.light;
    }
  };

  return (
    <Box sx={itemStyle}>
      <Typography color={getTextColor} textAlign={"center"} sx={textStyle}>
        {props.match.homeTeam} {props.match.homeTeamScore} -{" "}
        {props.match.awayTeamScore} {props.match.awayTeam} 
      </Typography>
    </Box>
  );
};

export default MatchHistoryItem;
