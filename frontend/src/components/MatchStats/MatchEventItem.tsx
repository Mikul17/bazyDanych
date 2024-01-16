import { MatchEvent } from "@/constants/Types";
import paletteProvider from "@/constants/color-palette";
import { Box, Typography } from "@mui/material";


const MatchEventItem = ({ event }: { event: MatchEvent }) => {
    const palette = paletteProvider();

    const keyEventStyle = {
        fontWeight: "bold",
        padding:"0.5rem",
        color: palette.text.secondary,
        backgroundColor: palette.primary.main,
        borderRadius: "1.25rem",
        width: "30%",
        textAlign: "center",
    }


    const restEventStyle = {
        color: event.homeTeam?palette.text.secondary : palette.text.primary,
        textAlign: event.homeTeam?"left":"right",
    }
    
    const cutMinuteFromDesc = (desc:string) => {
        return desc.substring(desc.indexOf(":")+1);
    }

    const formatStringBasedOnTeam = (desc:string) => {
        if(event.homeTeam){
            return desc;
        }else{
            let minute = desc.substring(0,desc.indexOf(":"));
            let action = cutMinuteFromDesc(desc);
            return `${action} :${minute}`;
        }
    }

    const generateByType = () => {
        if(event.actionType==="beginning" || event.actionType==="halfTime" || event.actionType==="end"){
            return (
                <Box display={"flex"} justifyContent={"center"} alignItems={"center"} mb={"0.5rem"}>
                <Typography sx={keyEventStyle}>
                    {cutMinuteFromDesc(event.desc)}
                </Typography>
                </Box>
            );
        }else{
            return (
                <Typography sx={restEventStyle}>
                    {formatStringBasedOnTeam(event.desc)}
                </Typography>
            );
        }
    }

    return (
        generateByType()
    );
};

export default MatchEventItem;