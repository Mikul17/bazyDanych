"use client";

import paletteProvider from "@/constants/color-palette";
import { Box, LinearProgress, Typography } from "@mui/material";

interface MatchStatItemProps {
    homeTeamStat: number;
    awayTeamStat: number;
    statName: string;
};

const MatchStatItem = (props:MatchStatItemProps) => {
    const palette = paletteProvider();

    const progressionBarStyle = {
        height: "1rem",
        width: "90%",
        borderRadius:"1.25rem",
        backgroundColor: palette.secondary.main,
        ".MuiLinearProgress-bar": {
            backgroundColor: palette.error.main,
        }
    }

    const boxStyle = {
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        flexDirection: "column",
        mt: 1,
        backgroundColor: palette.primary.main,
        padding: "1rem 1rem",
        borderRadius: "0.75rem",
    }

    const statSectionStyle = {
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        width: "100%",
        mt:"0.5rem"
    }

    const calculateProgression = () => {
        return (props.homeTeamStat / (props.homeTeamStat + props.awayTeamStat)) * 100;
    }

    const handlePercentageValue = (stat:number) => {
        if(props.statName === "Possession") {
            return `${stat}%`
        }else{
            return stat
        }
    }

    return (
        <Box sx={boxStyle}>
        <Box>
        <Typography fontSize={"1.25rem"} color={palette.text.secondary}>
          {props.statName}
        </Typography>
        </Box>
        <Box sx={statSectionStyle}>
        <Typography m={"0 0.5rem"} fontSize={"1rem"} color={palette.text.secondary}>
            {handlePercentageValue(props.homeTeamStat)}
          </Typography>

        <LinearProgress sx={progressionBarStyle} variant="determinate" value={calculateProgression()} />

          <Typography m={"0 0.5rem"} fontSize={"1rem"} color={palette.text.secondary}>
            {handlePercentageValue(props.awayTeamStat)}
          </Typography>
          </Box>
      </Box>
    );
}

export default MatchStatItem;