"use client"
import paletteProvider from "@/constants/color-palette";
import {
  Box,
  Button,
  Card,
  Container,
  Grid,
  IconButton,
  Input,
  InputAdornment,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import { DeleteOutline } from "@mui/icons-material";
import BetItem from "../CouponPage/BetItem";
import { coloredInputStyle, headerStyle } from "@/constants/Styles";
import { useBets } from "@/context/CouponBetsContext";
import { useEffect, useState } from "react";
import { jwtDecode } from "jwt-decode";



const CouponCreator = () => {
  const palette = paletteProvider();
  const { bets, clearBets } = useBets();
  const [stake, setStake] = useState<string>("");
  const [odds, setOdds] = useState<number>(0);
  const [possibleWin, setPossibleWin] = useState<number>(0);
  const [isHelperTextVisible, setIsHelperTextVisible] = useState<boolean>(false);

  const cardStyle = {
    display: "flex",
    flexDirection: "column",
    justifyContent: "space-between",
    backgroundColor: palette.primary.light,
    padding: "0.5rem 1rem",
    borderRadius: "1.25rem",
    minHeight: "85vh",
    maxHeight: "85vh",
    mt: "0.5rem",
  };

  const mainContentStyle = {
    flexGrow: 1,
    overflow: 'auto',
    my: '1rem', 
    px: '1rem',
  };

  const calculateOdds = (): number => {
    const totalOdds = bets.reduce((sum, bet) => sum * bet.odds, 1);
    setOdds(totalOdds);
    return totalOdds;
  };

  const calculatePossibleWin = (): void => {
    stake.replace(",", ".");
  setPossibleWin(Number(stake) * odds);
  };

  const getBetIdsFromLocalStorage = (): number[] => {
    const betIds = bets.map((bet) => bet.id);
    return betIds;
  };
  

  const handleSubmitCoupon = async () => {
    const url = `http://localhost:8080/api/coupon/add`;
    const token = sessionStorage.getItem("token");
    if (!token) {throw new Error("No token found")}
    const decoded = jwtDecode<{ userId: number }>(token!);
    const userId = decoded.userId;
    const betIds = getBetIdsFromLocalStorage();

    const requestBody = {
      stake: stake,
      userId: userId,
      bets: betIds,
    }

    const requestOptions = {
      method: "POST",
      headers: {
        "Content-Type":"application/json",
        "Authorization": `Bearer ${token}`
      },
      body: JSON.stringify(requestBody),
  };

  try{
    const response = await fetch(url, requestOptions);
    const message = await response.text();
    if(response.ok){
      console.log(message);
    }else if (response.status === 400){
      throw new Error(message);
    }else{
      console.log(message)
      throw new Error("Something went wrong");
    }
  }catch(error){
    console.log(error);
  }
}

  useEffect(() => {
    calculateOdds();
    calculatePossibleWin();
  }, [bets, stake]);


  const handleStakeChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    let value = event.target.value;
  

    value = value.replace(/[^\d.]/g, '');
  
    const periodIndex = value.indexOf('.');
  
    if (periodIndex > 8) {
      value = value.slice(0, 8) + '.' + value.slice(periodIndex + 1);
    }
  
    if (periodIndex === -1 && value.length > 8) {
      value = value.slice(0, 8) + '.' + value.slice(8);
    }
  
    if (/^\d{0,8}(\.\d{0,2})?$/.test(value)) {
      setStake(value);
    }
  };


  return (
    <Container maxWidth="md">
      <Card sx={cardStyle}>
        {/* Header */}
        <Box sx={headerStyle("space-around")}>
          <Typography color={palette.primary.main} fontWeight={"bold"}>
            {bets.length > 0
              ? `Events: ${bets.length} `
              : "Empty coupon"}
          </Typography>
          <IconButton onClick={clearBets}>
            <DeleteOutline htmlColor={palette.primary.main} />
          </IconButton>
        </Box>

        {/* Bets */}
        <Box sx={mainContentStyle}>
        {bets.map((bet, index) => (
            <BetItem key={index} bet={bet} />
          ))}
        </Box>

        {/* Footer */}
        <Box sx={headerStyle("none", true)}>
          <Grid container justifyContent="space-around" alignItems="center">
            <Grid item xs={5}>
              <TextField
                label="Stake"
                value={stake}
                variant="outlined"
                margin="dense"
                placeholder="1.00"
                onChange={handleStakeChange}
                helperText="Minimum stake is 1.00zł"
                size="small"
                sx={coloredInputStyle(palette.primary.main)}
                InputLabelProps={{
                  sx: {
                    fontWeight: "bold",
                    "&.Mui-focused": {
                      color: palette.primary.main,
                    },
                  },
                }}
                InputProps={{
                  sx: { fontWeight: "bold" },
                  endAdornment: (
                    <Typography
                      color={palette.primary.main}
                      fontWeight={"bold"}
                      variant="body2"
                    >
                      zł
                    </Typography>
                  ),
                }}
                FormHelperTextProps={{
                  sx: {display: isHelperTextVisible ? "block" : "none" ,fontWeight: "bold", color: palette.error.main },
                }}
              />
            </Grid>
            <Grid item xs={5}>
              <TextField
                label="Odds"
                value={odds.toFixed(2)}
                variant="outlined"
                margin="dense"
                size="small"
                sx={coloredInputStyle(palette.primary.main)}
                InputLabelProps={{
                  sx: {
                    fontWeight: "bold",
                    "&.Mui-focused": {
                      color: palette.primary.main,
                    },
                  },
                }}
                inputProps={{ readOnly: true }}
                InputProps={{ sx: { fontWeight: "bold" } }}
              />
            </Grid>
            <Grid item xs={5}>
              <TextField
                label="Possible win"
                variant="outlined"
                margin="dense"
                value={possibleWin.toFixed(2)}
                size="small"
                sx={coloredInputStyle(palette.primary.main)}
                InputLabelProps={{
                  sx: {
                    fontWeight: "bold",
                    "&.Mui-focused": {
                      color: palette.primary.main,
                    },
                  },
                }}
                inputProps={{ readOnly: true }}
                InputProps={{
                  sx: { fontWeight: "bold" },
                  endAdornment: (
                    <Typography
                      color={palette.primary.main}
                      fontWeight={"bold"}
                      variant="body2"
                    >
                      zł
                    </Typography>
                  ),
                }}
              />
            </Grid>
            <Grid item xs={5}>
              <Button
                variant="contained"
                fullWidth
                disabled={bets.length === 0 || stake === ""}
                onClick={handleSubmitCoupon}
                sx={{ borderRadius: "0.5rem" }}
              >
                Create coupon
              </Button>
            </Grid>
          </Grid>
        </Box>
      </Card>
    </Container>
  );
};

export default CouponCreator;
