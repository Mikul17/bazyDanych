import { coloredInputStyle, headerStyle, inputStyle } from "@/constants/Styles";
import { Coupon } from "@/constants/Types";
import paletteProvider from "@/constants/color-palette";
import { HighlightOffOutlined } from "@mui/icons-material";
import {
  Box,
  Card,
  Container,
  Grid,
  IconButton,
  Modal,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import BetItem from "./BetItem";
import BetItemWithIcon from "./BetItemWithIcon";

interface CouponDetailsModalProps {
  isCouponModalOpen: boolean;
  setIsCouponModalOpen: (isOpen: boolean) => void;
  coupon: Coupon;
}

const CouponDetailsModal = (props: CouponDetailsModalProps) => {
  const palette = paletteProvider();

  const modalStyle = {
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    width: "60%",
    "@media (min-width:450px)": {
      width: "50%",
    },
    "@media (min-width:600px)": {
      width: "40%",
    },
    "@media (min-width:1050px)": {
      width: "35%",
    },
    bgcolor: palette.primary.light,
    p: "2rem",
    borderRadius: "1.25rem",
  };

  const handleCloseCouponModal = () => {
    props.setIsCouponModalOpen(false);
  };

  return (
    <Modal open={props.isCouponModalOpen} onClose={handleCloseCouponModal}>
      <Card sx={modalStyle}>
        {/** Header */}
        <Box sx={headerStyle("space-between")}>
          <Typography fontWeight={"bold"} fontSize={"1.25rem"} ml={"35%"}>
            Coupon Details
          </Typography>
          <IconButton
            onClick={handleCloseCouponModal}
            sx={{ cursor: "pointer" }}
          >
            <HighlightOffOutlined
              fontSize="inherit"
              sx={{ color: palette.primary.main }}
            />
          </IconButton>
        </Box>
          
        <BetItemWithIcon
          key={1}
          bet={{
            id: 0,
            matchId: 0,
            homeTeam: "Test team 1",
            awayTeam: "Test team 2",
            odds: 3.25,
            betStatus: 2,
            betType: {
              id: 0,
              betStat: "fouls",
              betTypeCode: "over",
              targetValue: 5.5,
              team: 2,
            },
          }}
        />

        {/** Footer */}
        <Box sx={headerStyle("none", true)}>
          <Grid container justifyContent="space-around" alignItems="center">
            <Grid item xs={4}>
              <TextField
                label="Stake"
                value={props.coupon.stake.toFixed(2)}
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
            <Grid item xs={4}>
              <TextField
                label="Odds"
                value={props.coupon.totalOdds.toFixed(2)}
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
                fullWidth
                value={props.coupon.possibleWin.toFixed(2)}
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
          </Grid>
        </Box>
      </Card>
    </Modal>
  );
};

export default CouponDetailsModal;
