"use client"
import { headerStyle, inputStyle } from "@/constants/Styles";
import { Coupon } from "@/constants/Types";
import paletteProvider from "@/constants/color-palette";
import {
  Box,
  Card,
  CardActionArea,
  Grid,
  TextField,
  Typography,
} from "@mui/material";
import { useEffect, useState } from "react";
import CouponDetailsModal from "./CouponDetailsModal";

interface CouponItemProps {
  coupon: Coupon;
}


interface betStatusProps {
  color: string;
  text: string;
}

const CouponItem = (props: CouponItemProps) => {
  const palette = paletteProvider();
  const [betStatus, setBetStatus] = useState<betStatusProps>({
    color: palette.primary.dark,
    text: "Error",
  });
  const [isCouponModalOpen, setIsCouponModalOpen] = useState<boolean>(false);

  console.log(props);

  const calculateEventsAmount = () => {
    return 3;
  };

  const betStatusColor = () => {
    if (props.coupon.couponStatus === "ACTIVE") {
      setBetStatus({ color: palette.secondary.main, text: "Active" });
    } else if (props.coupon.couponStatus === "WON") {
      setBetStatus({ color: palette.primary.light, text: "Won" });
    } else if (props.coupon.couponStatus === "LOST") {
      setBetStatus({ color: palette.error.main, text: "Lost" });
    }
  };

  const cardStyle = {
    padding: "0.5rem 1rem 2rem 1rem",
    backgroundColor: palette.primary.main,
    borderRadius: "1.25rem",
    height: "15vh",
  };

  const betStatusBox = {
    p: 1,
    borderRadius: "1.25rem",
    backgroundColor: betStatus.color,
    color: palette.text.secondary,
    width: "40%",
    fontSize: "1.25rem",
    fontWeight: "bold",
    mt: 0.5,
    textAlign: "center",
    ml: 5,
  };

  useEffect(() => {
    betStatusColor();
  }, []);

  const handleCouponClick = () => {
    setIsCouponModalOpen(true);
  };

  return (
    <Card sx={cardStyle}>
      <CardActionArea onClick={handleCouponClick}>
        <Box sx={headerStyle("center")}>
          <Typography fontWeight={"bold"} fontSize={"1rem"}>
            {calculateEventsAmount()} Events
          </Typography>
        </Box>
        <Grid
          container
          spacing={1}
          sx={{ p: 0, width: "100%", height: "100%" }}
        >
          <Grid item xs={6}>
            <TextField
              margin="dense"
              size="small"
              label="Stake"
              variant="outlined"
              sx={inputStyle}
              value={props.coupon.stake.toFixed(2)}
              InputProps={{ readOnly: true }}
              InputLabelProps={{
                sx: {
                  "&.Mui-focused": {
                    color: palette.text.secondary,
                  },
                },
              }}
            />
            <TextField
              margin="dense"
              size="small"
              label="Possible win"
              variant="outlined"
              sx={inputStyle}
              value={props.coupon.possibleWin.toFixed(2)}
              InputProps={{ readOnly: true }}
              InputLabelProps={{
                sx: {
                  "&.Mui-focused": {
                    color: palette.text.secondary,
                  },
                },
              }}
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              margin="dense"
              size="small" 
              label="Odds"
              variant="outlined"
              sx={inputStyle}
              value={props.coupon.totalOdds.toFixed(2)}
              InputProps={{ readOnly: true }}
              InputLabelProps={{
                sx: {
                  "&.Mui-focused": {
                    color: palette.text.secondary,
                  },
                },
              }}
            />
            <Typography sx={betStatusBox}>{betStatus.text}</Typography>
          </Grid>
        </Grid>
      </CardActionArea>
      <CouponDetailsModal isCouponModalOpen={isCouponModalOpen} setIsCouponModalOpen={setIsCouponModalOpen} coupon={props.coupon}/>
    </Card>
    
  );
};

export default CouponItem;
