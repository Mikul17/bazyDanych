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
import { use, useEffect, useState } from "react";
import { Bet } from "@/constants/Types";

const calculateBets = ():number => {
  return 0;
};

const CouponCreator = () => {
  const palette = paletteProvider();
  const [bets, setBets] = useState<Bet[]>([]);

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

  const fetchBets = (): Bet[] => {
    const storedBets = localStorage.getItem("bets");
    return storedBets ? JSON.parse(storedBets) : [];
  };
  
  useEffect(() => {
    const loadBetsFromLocalStorage = (): void => {
      const storedBets = localStorage.getItem('bets');
      const bets = storedBets ? JSON.parse(storedBets) : [];
    };

    loadBetsFromLocalStorage();
  }, []);


  return (
    <Container maxWidth="md">
      <Card sx={cardStyle}>
        {/* Header */}
        <Box sx={headerStyle("space-around")}>
          <Typography color={palette.primary.main} fontWeight={"bold"}>
            {calculateBets() > 0
              ? `Events: ${calculateBets()} `
              : "Empty coupon"}
          </Typography>
          <IconButton>
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
                value={530}
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
              <TextField
                label="Odds"
                value={3.21}
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
                value={5.35}
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
