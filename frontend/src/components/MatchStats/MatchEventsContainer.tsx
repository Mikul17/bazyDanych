"use client";
import { headerStyle } from "@/constants/Styles";
import paletteProvider from "@/constants/color-palette";
import { Box, Card, Typography } from "@mui/material";

const MatchEventsContainer = () => {
  const palette = paletteProvider();

  const cardStyle = {
    padding: "0.5rem 1rem 2rem 1rem",
    backgroundColor: palette.primary.light,
    borderRadius: "1.25rem",
    height: "35vh",
    width: "100%",
  };

  return (
    <Card sx={cardStyle}>
      <Box sx={headerStyle("center")}>
        <Typography fontWeight={"bold"} color={palette.primary.main} variant="h6">
          Match Highlights
        </Typography>
      </Box>
    </Card>
  );
};

export default MatchEventsContainer;
