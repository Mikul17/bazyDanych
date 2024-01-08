import { Bet } from '@/constants/Types';
import paletteProvider from '@/constants/color-palette';
import { Box, Button, Typography } from '@mui/material';
import React from 'react';

interface BetButtonProps {
    bet: Bet;
    betName: string;
    isSelected: boolean;
    onBetSelected: (bet: Bet) => void;
}

const compareBets = (bet1:Bet, bet2:Bet) => {
    const {betType} = bet1;
    const {betType:betType2} = bet2;

    if(betType.betStat === "score" && betType2.betStat === "score"){
        return betType.betStat === betType2.betStat && betType.betTypeCode === betType2.betTypeCode;
    }else{
        return betType.betStat === betType2.betStat && betType.betTypeCode === betType2.betTypeCode && betType.team === betType2.team;
    }
}


const BetButton= (props : BetButtonProps) => {
    const palette = paletteProvider();


    const typographyStyle = {
        fontSize: "0.75rem",
        color: palette.text.primary,
    };

    const buttonStyle = {
        borderRadius: "1rem",
        backgroundColor: props.isSelected?palette.primary.dark:palette.text.light,
        margin: "0 0.5rem",
        "&:hover": {
            backgroundColor: palette.primary.main,
        },
    }

    const addBetToLocalStorage = () => {
        const bets = JSON.parse(localStorage.getItem("bets") || "[]");
        const existsByBetType:number = bets.findIndex((bet:Bet) => compareBets(bet, props.bet));
        if (existsByBetType !== -1) {
           bets[existsByBetType] = props.bet;
        }else{
            bets.push(props.bet);
        }
        localStorage.setItem("bets", JSON.stringify(bets));
        window.dispatchEvent(new Event('storageUpdate'));

    }

    const onButtonClick = () => {
        addBetToLocalStorage();
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
