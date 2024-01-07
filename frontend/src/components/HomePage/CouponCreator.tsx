import paletteProvider from "@/constants/color-palette";
import {
  Box,
  Button,
  Grid,
  IconButton,
  Input,
  InputAdornment,
  TextField,
  Typography,
} from "@mui/material";
import MatchContainer from "./MatchContainer";
import { DeleteOutline } from "@mui/icons-material";
import BetItem from "../CouponPage/BetItem";

const calculateBets = () => {
  return 0;
};

const CouponCreator = () => {
  const palette = paletteProvider();

  const couponHeaderStyle = {
    margin: "1rem",
    backgroundColor: palette.text.light,
    borderRadius: "1.25rem 1.25rem 0.25rem 0.25rem",
    padding: "0.5rem 1.5rem",
  };

  const couponFooterStyle = {
    margin: "1rem",
    backgroundColor: palette.text.light,
    borderRadius: "0.25rem 0.25rem 1.25rem 1.25rem",
    padding: "0.5rem 1.5rem",
  };

  const matchContainerStyle = {
    marginLeft:"1rem",
    maxHeight: "60vh",
    overflowY: "auto",
    width: "100%",
    '&::-webkit-scrollbar': {display: "none"}
  };

  const placeBetButtonStyle = {
    background: palette.primary.main,
    "&:hover": {
      backgroundColor: palette.primary.light,
    },
  };

  return (
    <Box
      sx={{
        borderRadius: "1.25rem",
        backgroundColor: palette.primary.light,
        padding: "0.1rem",
        margin: "1.5rem 1.5rem",
        maxHeight: "80%",
      }}
    >
      <Box
        id="cupon-cretor-header"
        sx={couponHeaderStyle}
        display={"flex"}
        justifyContent={"space-between"}
        alignItems={"center"}
      >
        <Typography>{calculateBets()} events</Typography>
        <IconButton>
          <DeleteOutline />
        </IconButton>
      </Box>
      <Box display={"flex"} justifyContent={"center"} alignItems={"flex-start"} flexDirection={"column"} sx={matchContainerStyle}>
        <BetItem bet={{
          id: 0,
          matchId: 0,
          homeTeam: "",
          awayTeam: "",
          odds: 0,
          betStatus: 0,
          betType: {
            id: 0,
            betStat: "",
            betTypeCode: "",
            targetValue: 0,
            team: 0
          }
        }} />
              <BetItem bet={{
          id: 0,
          matchId: 0,
          homeTeam: "",
          awayTeam: "",
          odds: 0,
          betStatus: 0,
          betType: {
            id: 0,
            betStat: "",
            betTypeCode: "",
            targetValue: 0,
            team: 0
          }
        }} />
              <BetItem bet={{
          id: 0,
          matchId: 0,
          homeTeam: "",
          awayTeam: "",
          odds: 0,
          betStatus: 0,
          betType: {
            id: 0,
            betStat: "",
            betTypeCode: "",
            targetValue: 0,
            team: 0
          }
        }} />
              <BetItem bet={{
          id: 0,
          matchId: 0,
          homeTeam: "",
          awayTeam: "",
          odds: 0,
          betStatus: 0,
          betType: {
            id: 0,
            betStat: "",
            betTypeCode: "",
            targetValue: 0,
            team: 0
          }
        }} />
      </Box>
      <Box sx={couponFooterStyle} display={"flex"} justifyContent={"center"} alignItems={"center"} gap={"10rem"}>
        <Box display={"flex"} justifyContent={"space-around"} alignItems={"center"} flexDirection={"column"} gap={2}>
            <TextField
              id="outlined-adornment" 
              label="Stake"
              size="small"
              fullWidth
              InputProps={{
                endAdornment: (
                  <InputAdornment position="end">zł</InputAdornment>
                ),
              }}
            />

<TextField
              id="outlined-read-only-input"
              label="Possible win"
              defaultValue="0.00 zł"
              fullWidth
              size="small"
              InputProps={{
                readOnly: true,
              }}
            />
            
          </Box>
          <Box display={"flex"} justifyContent={"space-around"} alignItems={"center"} flexDirection={"column"} gap={2}>

          <TextField
              id="outlined-read-only-input"
              label="Odds"
              defaultValue="0.00"
              size="small"
              fullWidth 
              InputProps={{
                readOnly: true,
              }}
              InputLabelProps={{
                color: "primary",
              }}  
            />

            <Button variant="contained"  fullWidth sx={placeBetButtonStyle}>
              Place bet
            </Button>
          </Box>
      </Box>
    </Box>
  );
};

export default CouponCreator;