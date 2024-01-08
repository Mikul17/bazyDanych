import { Bet } from '@/constants/Types';
import paletteProvider from '@/constants/color-palette';
import { useBets } from '@/context/CouponBetsContext';
import { Box, Button, Typography } from '@mui/material';
import React from 'react';

interface BetButtonProps {
    bet: Bet;
    betName: string;
    isSelected: boolean;
    onBetSelected: (bet: Bet) => void;
}

const compareBets = (bet1: Bet, bet2: Bet) => {
    const { betType } = bet1;
    const { betType: betType2 } = bet2;

    return (
          betType.betStat === betType2.betStat &&
          betType.betTypeCode === betType2.betTypeCode &&
          betType.team === betType2.team &&
          betType.targetValue === betType2.targetValue
        );
  };


const BetButton= (props : BetButtonProps) => {
    const palette = paletteProvider();
    const {bets,addBet} = useBets();

    const isSelected = bets.some(bet => compareBets(bet, props.bet));

    const typographyStyle = {
        fontSize: "0.75rem",
        color: palette.text.primary,
    };

    const buttonStyle = {
        borderRadius: "1rem",
        backgroundColor: isSelected ? palette.primary.dark : palette.text.light,
        margin: "0 0.5rem",
        "&:hover": {
            backgroundColor: palette.primary.main,
        },
    }

    const onButtonClick = () => {
        addBet(props.bet);
        props.onBetSelected(props.bet);
    }
    return (
        <Button sx={buttonStyle} onClick={onButtonClick}> 
            <Box display={"flex"} justifyContent={"space-around"} alignContent={"center"} flexDirection={"column"}>
            <Typography sx={typographyStyle}>{props.betName}</Typography>
            <Typography sx={typographyStyle}>{props.bet.odds}</Typography>
            </Box>
        </Button>
    );
};

export default BetButton;
